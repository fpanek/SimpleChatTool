package at.ac.fhcampuswien.simplechattool;

import java.io.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ClientHandler extends Thread{
    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
    final ObjectInputStream ois;
    final ObjectOutputStream oos;
    final Socket s;
    ArrayList<Socket> connectedClients = new ArrayList<Socket>();
    private static ArrayList<String> users = new ArrayList<String>();
    private static ArrayList<ClientHandler> ActiveClientHandlers = new ArrayList<ClientHandler>();
    private String username;

    // Constructor
    public ClientHandler(Socket s, ObjectInputStream ois, ObjectOutputStream oos) {
        this.s = s;
        this.ois = ois;
        this.oos = oos;
        connectedClients.add(s);
        ActiveClientHandlers.add(this);
        this.username="";
        //users.add()
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

    public ArrayList<Socket> getConnectedClients(){
        return connectedClients;
    }

    public static ArrayList<ClientHandler> getActiveClientHandlers(){
        return ActiveClientHandlers;
    }

    public boolean socketEqualWithClientHandler(ClientHandler clientHandler, Socket socket) {
        if (clientHandler.getClientHandlerPort(clientHandler) == socket.getPort()) {
            //System.out.println("Ports are equal");
            return true;
        } else {
            //System.out.println("Port not equal...");
            return false;
        }
    }

    public int getClientHandlerPort(ClientHandler clientHandler){
        return clientHandler.s.getPort();
    }

    @Override
    public void run() {
        String received;
        String toreturn;
        Message receivedMessage;
        Message toReturnMessage;
        while (true) {
            try {
                receivedMessage = (Message) ois.readObject();
                System.out.println("Received Object Text: " + receivedMessage.getText());


                System.out.println("Port: " + s.getPort() + " AdditionalInformation: " + receivedMessage.getInternalInformation() + " received Message: " + receivedMessage.getText() + "Users: " + receivedMessage.getUsers());


                String username = receivedMessage.getUsername();
                Message myMessage = new Message(receivedMessage.getUsername(), receivedMessage.getText(), "iwas");
                //users.add(username);

                //TODO:
                //Send all connected Clients to User if new message is retrieved  - in both cases = internalMessage=true (additional message)/false (default behavior)
                //forward it to all users
                //TODO : !! testing purpose set if to false condition again - is only to not for testing purpose
                if(receivedMessage.getInternalInformation()) {
                    if ((receivedMessage.getText().equals("myUsername")) && !users.contains(receivedMessage.getUsername())) {
                        System.out.println("New User:..." + receivedMessage.getText());
                        users.add(receivedMessage.getUsername());
                        this.username = receivedMessage.getUsername();
                        for (ClientHandler handler : ActiveClientHandlers) {
                            //handler.oos.writeObject(myMessage);
                            receivedMessage.setUsers(users);
                            receivedMessage.setText("allConnectedUsers");
                            System.out.println(handler);
                            handler.oos.writeObject(receivedMessage);
                            //myMessage.user_list.addAll(users);
                            handler.oos.flush();
                        }
                    }
                }




                //Send received Message to All Clients except to itself
                if (!(receivedMessage.getText().equals("CloseSocket"))) {
                    System.out.println("Message not equal CloseSocket..");
                    for (ClientHandler handler: ActiveClientHandlers) {
                        //handler.oos.writeObject(myMessage);
                        System.out.println(handler);
                        if (handler.s.getPort() != s.getPort()) {
                            //handler.dos.writeUTF(received);
                            //handler.dos.flush();
                            receivedMessage.setUsers(users);
                            System.out.println("Forwarding Message: " + receivedMessage.getText());
                            handler.oos.writeObject(receivedMessage);
                            myMessage.user_list.addAll(users);
                            handler.oos.flush();
                        }
                    }
                }


                //Client disconnects and closes Socket
                if (receivedMessage.getText().equals("CloseSocket")) {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    for (ClientHandler handler: ActiveClientHandlers){
                        if (socketEqualWithClientHandler(handler, s)) {
                            System.out.println("Removing Client: " + handler.s.getPort());
                            users.remove(username);
                            ActiveClientHandlers.remove(handler);
                            break;
                        }
                    }
                    break;
                }

                // creating Date object
                Date date = new Date();

                // write on output stream based on the
                // answer from the client
                //TODO: Add previous if/else statements to switch/case below:
                switch (receivedMessage.getText()) {
                    case "Date" :
                        toreturn = fordate.format(date);
                        Message returnMessageDate = new Message("Automatic Message", toreturn, "Automated Message");
                        oos.writeObject(returnMessageDate);
                        oos.flush();
                        //dos.writeUTF(toreturn);
                        //oos.writeObject();
                        break;

                    case "Time" :
                        toreturn = fortime.format(date);
                        Message returnMessageTime = new Message("Automatic Message", toreturn, "Automated Message");
                        oos.writeObject(returnMessageTime);
                        oos.flush();
                        //dos.writeUTF(toreturn);
                        break;

                    case "Users" :
                        toreturn = fortime.format(date);
                        Message returnConnectedUsers = new Message("Automatic Message", toreturn, "Automated Message Print connected User on Server CMD");
                        returnConnectedUsers.setUsers(users);
                        oos.writeObject(returnConnectedUsers);
                        oos.flush();
                        System.out.println("Connected Users requested on Server Side: " + users);

                        //dos.writeUTF(toreturn);
                        break;
/*
                    default:
                        //dos.writeUTF("Invalid input");
                        toreturn = "Invalid input";
                        Message returnMessageDefault = new Message("Automatic Message", toreturn, "Automated Message");
                        oos.writeObject(returnMessageDefault);
                        oos.flush();
                        break;

 */
                }


            } catch (Exception e) {
                System.err.println("Client disconnected");
                e.printStackTrace();
                for (ClientHandler handler: ActiveClientHandlers) {
                    if (socketEqualWithClientHandler(handler, s)) {
                        System.out.println("Removing Client: " + handler.s.getPort());
                        ActiveClientHandlers.remove(handler);
                        users.remove(this.username);
                        break;
                    }
                }
                try {
                    this.s.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            }
        }
    }
}
