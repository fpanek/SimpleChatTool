package at.ac.fhcampuswien.simplechattool;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

public class ClientHandler extends Thread{
    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;
    ArrayList<Socket> connectedClients = new ArrayList<Socket>();
    private static ArrayList<ClientHandler> ActiveClientHandlers = new ArrayList<ClientHandler>();

    //ToDO
    // remove socket from Array List if Socket gets Disconnected

    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        connectedClients.add(s);
        ActiveClientHandlers.add(this);

    }




    @Override
    public  String toString(){
        return "Socket: " + s.getPort() + s.getRemoteSocketAddress();
    }

    public ArrayList<Socket> getConnectedClients(){
        return connectedClients;
    }

    public static ArrayList<ClientHandler> getActiveClientHandlers(){
        return ActiveClientHandlers;
    }

    public boolean socketEqualWithClientHandler(ClientHandler clientHandler, Socket socket){
        if(clientHandler.getClientHandlerPort(clientHandler) == socket.getPort()){
            //System.out.println("Ports are equal");
            return true;
        }else{
            //System.out.println("Port not qual...");
            return false;
        }

    }

    public int getClientHandlerPort(ClientHandler clientHandler){
        return clientHandler.s.getPort();
    }

    @Override
    public void run()
    {
        String received;
        String toreturn;
        while (true)
        {
            try {

                // Ask user what he wants
                //dos.writeUTF("What do you want");
                //dos.flush();
                // receive the answer from client
                received = dis.readUTF();


                //Send received Message to All Clients
                if(!received.equals("CloseSocket")){
                    for(ClientHandler handler: ActiveClientHandlers){
                        //System.out.println(handler);
                        if(handler.s.getPort()!=s.getPort()){
                            handler.dos.writeUTF(received);
                            handler.dos.flush();
                        }
                    }
                }


                System.out.println("Port: " + s.getPort() + " received Message: " + received);
                if(received.equals("CloseSocket"))
                {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    for(ClientHandler handler: ActiveClientHandlers){
                        if(socketEqualWithClientHandler(handler, s)){
                            System.out.println("Removing Client: " + handler.s.getPort());
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
                switch (received) {

                    case "Date" :
                        toreturn = fordate.format(date);
                        dos.writeUTF(toreturn);
                        break;

                    case "Time" :
                        toreturn = fortime.format(date);
                        dos.writeUTF(toreturn);
                        break;

                    default:
                        //dos.writeUTF("Invalid input");
                        break;
                }


            } catch (IOException e) {
                System.err.println("Client disconnectd");
                for(ClientHandler handler: ActiveClientHandlers){
                    if(socketEqualWithClientHandler(handler, s)){
                       //ActiveClientHandlers.remove(handler);
                        // ClientHandler ClientHandlertoRemove = handler;
                        System.out.println("Removing Client: " + handler.s.getPort());
                        ActiveClientHandlers.remove(handler);
                        break;
                    }
                }

                try{
                    this.s.close();
                } catch (Exception ex){
                    ex.printStackTrace();
                }
                break;
            }
        }

        try
        {
            // closing resources
            this.dis.close();
            this.dos.close();

        }catch(IOException e){
            e.printStackTrace();
        }

    }
}
