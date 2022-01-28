package at.ac.fhcampuswien.simplechattool;

import java.io.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ClientHandler extends Thread {
    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
    final ObjectInputStream ois;
    final ObjectOutputStream oos;
    final Socket s;
    ArrayList<Socket> connectedClients = new ArrayList<>();
    private static final ArrayList<String> users = new ArrayList<>();
    private static final ArrayList<ClientHandler> ActiveClientHandlers = new ArrayList<>();
    private String username;

    public ClientHandler(Socket s, ObjectInputStream ois, ObjectOutputStream oos) {
        this.s = s;
        this.ois = ois;
        this.oos = oos;
        connectedClients.add(s);
        ActiveClientHandlers.add(this);
        this.username="";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        users.add(username);
    }

    @Override
    public String toString(){
        return "Socket: " + s.getPort() + s.getRemoteSocketAddress();
    }

    public boolean socketEqualWithClientHandler(ClientHandler clientHandler, Socket socket) {
        return clientHandler.getClientHandlerPort(clientHandler) == socket.getPort();
    }

    public int getClientHandlerPort(ClientHandler clientHandler){
        return clientHandler.s.getPort();
    }

    @Override
    public void run() {
        String toreturn;
        Message receivedMessage;
        while (true) {
            try {
                receivedMessage = (Message) ois.readObject();
                username = receivedMessage.getUsername();
                System.out.println("Port: " + s.getPort() + ", InternalInformation: " + receivedMessage.getInternalInformation() +
                                    ", received message: " + receivedMessage.getText() + ", Users: " + receivedMessage.getUsername());

                //InternalInformation + Username is not in userlist = new User
                if (receivedMessage.getInternalInformation() && !users.contains(receivedMessage.getUsername())) {
                    if (receivedMessage.getText().equals("myUsername")) {
                        System.out.println("New User: " + receivedMessage.getUsername());
                        users.add(receivedMessage.getUsername());
                        System.out.println(users);
                        this.username = receivedMessage.getUsername();
                        for (ClientHandler handler : ActiveClientHandlers) {
                            receivedMessage.setUsers(users);
                            System.out.println("Sending Users to: " + handler.getUsername() + "Users: " + users);
                            handler.oos.writeObject(receivedMessage);
                            handler.oos.flush();
                            handler.oos.reset();
                        }
                    }
                }

                // creating Date object
                Date date = new Date();
                // write on output stream based on the answer from the client
                if(!receivedMessage.getInternalInformation()){
                    switch (receivedMessage.getText()) {
                        case "Date" :
                            toreturn = fordate.format(date);
                            Message returnMessageDate = new Message("Automatic Message", toreturn, "Automated Message");
                            returnMessageDate.setUsers(users);
                            System.out.println("Message Object users: " + returnMessageDate.getUsers());
                            oos.writeObject(returnMessageDate);
                            oos.flush();
                            break;

                        case "Time" :
                            toreturn = fortime.format(date);
                            Message returnMessageTime = new Message("Automatic Message", toreturn, "Automated Message");
                            returnMessageTime.setUsers(users);
                            oos.writeObject(returnMessageTime);
                            oos.flush();
                            break;

                        case "myUsername" :
                            toreturn = fortime.format(date);
                            Message returnConnectedUsers = new Message("Automatic Message", toreturn, "Automated Message Print connected User on Server CMD");
                            returnConnectedUsers.setInternalInformation(true);
                            returnConnectedUsers.setUsers(users);
                            oos.writeObject(returnConnectedUsers);
                            oos.flush();
                            System.out.println("Connected Users requested on Server Side: " + users);
                            break;
                        default:          //default = forward Message to all connected clients except to itself
                            for (ClientHandler handler: ActiveClientHandlers) {
                                if (handler.s.getPort() != s.getPort()) {
                                    receivedMessage.setUsers(users);
                                    System.out.println("Forwarding Message to All Clients: " + receivedMessage.getText() + " Users: " + receivedMessage.getUsers());
                                    handler.oos.writeObject(receivedMessage);
                                    handler.oos.flush();
                                    handler.oos.reset();
                                }
                            }
                    }
                }else {
                    switch (receivedMessage.getText()) {
                        case "closeSocket":
                            System.out.println("Client " + this.s + " sends exit...");
                            System.out.println("Closing this connection.");
                            this.s.close();
                            //socketToClose = this.s;
                            System.out.println("Connection closed");

                            for (ClientHandler handler: ActiveClientHandlers) {
                                if (socketEqualWithClientHandler(handler, s)) {
                                    System.out.println("Removing Client: " + handler.s.getPort());
                                    users.remove(username);
                                    ActiveClientHandlers.remove(handler);
                                    try {
                                        Message updatingUser = new Message("Automatic Message", "updating users", "Automated Message updating users");
                                        updatingUser.setInternalInformation(true);
                                        updatingUser.setUsers(users);
                                        try {
                                            handler.oos.writeObject(updatingUser);
                                            handler.oos.flush();
                                            handler.oos.reset();
                                        } catch (IOException e) {
                                            System.out.println("Socket closed");
                                        }
                                    } catch (Exception ex){
                                        ex.printStackTrace();
                                    }
                                    break;
                                }
                            }
                            break;
                    }
                }

            } catch (Exception e) {
                System.err.println("Client disconnected");
                e.printStackTrace();
                String userToRemove = "";
                for (ClientHandler handler: ActiveClientHandlers) {
                    if (socketEqualWithClientHandler(handler, s)) {
                        System.out.println("Removing Client: " + handler.s.getPort());
                        userToRemove = this.username;
                        ActiveClientHandlers.remove(handler);
                        break;
                    }
                }
                users.remove(userToRemove);
                System.out.println("All Clients still connected : " + ActiveClientHandlers);
                for (ClientHandler handler: ActiveClientHandlers) {
                        System.out.println("Sending new users to User: " + handler.getUsername());
                        try{
                            Message updatingUser = new Message("Automatic Message", "Updating Users", "Automated Message updating users");
                            updatingUser.setInternalInformation(true);
                            updatingUser.setUsers(users);
                            System.out.println("Forwarding UserList to All Clients: " + " Users: " + updatingUser.getUsers());
                            handler.oos.writeObject(updatingUser);
                            handler.oos.flush();
                            handler.oos.reset();
                        } catch (Exception ex){
                            ex.printStackTrace();
                        }
                }
                try {
                    this.s.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            }

            try {
                oos.reset();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
