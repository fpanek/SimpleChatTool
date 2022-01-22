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
    public ArrayList<String> users = new ArrayList<String>();
    private static ArrayList<ClientHandler> ActiveClientHandlers = new ArrayList<ClientHandler>();

    // Constructor
    public ClientHandler(Socket s, ObjectInputStream ois, ObjectOutputStream oos) {
        this.s = s;
        this.ois = ois;
        this.oos = oos;
        connectedClients.add(s);
        ActiveClientHandlers.add(this);
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
            System.out.println("Ports are equal");
            return true;
        } else {
            System.out.println("Port not equal...");
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
                String username = receivedMessage.getUsername();
                users.add(username);

                Message myMessage = new Message(receivedMessage.getUsername(), receivedMessage.getText(), "iwas");

                //Send received Message to All Clients
                if (!(receivedMessage.getText().equals("CloseSocket"))) {
                    System.out.println("Message not equal CloseSocket..");

                    for (ClientHandler handler: ActiveClientHandlers) {

                        //handler.oos.writeObject(myMessage);
                        System.out.println(handler);
                        if (handler.s.getPort() != s.getPort()) {
                            //handler.dos.writeUTF(received);
                            //handler.dos.flush();
                            System.out.println("Forwarding Message: " + receivedMessage.getText());
                            handler.oos.writeObject(receivedMessage);
                            myMessage.user_list.addAll(users);
                            handler.oos.flush();
                        }
                    }
                }

                System.out.println("Port: " + s.getPort() + " received Message: " + receivedMessage.getText());

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
