package at.ac.fhcampuswien.simplechattool;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {




    public static void main(String[] args) throws IOException {
        // server is listening on port 5056
        ServerSocket ss = new ServerSocket(5056);

        // running infinite loop for getting
        // client request
        while (true) {
            Socket s = null;
            try {
                // socket object to receive incoming client requests
                s = ss.accept();

                System.out.println("A new client is connected: " + s);

                // obtaining input and out streams
                //DataInputStream dis = new DataInputStream(s.getInputStream());
                //DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                OutputStream outputStream = s.getOutputStream();
                InputStream inputStream = s.getInputStream();
                ObjectOutputStream oos = new ObjectOutputStream(outputStream);
                ObjectInputStream ois = new ObjectInputStream(inputStream);

                //TEst of receiving Object:
                // get the input stream from the connected socket
                /*
                OutputStream outputStream = s.getOutputStream();
                InputStream inputStream = s.getInputStream();
                // create a DataInputStream so we can read data from it.
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                ObjectInputStream objectInputStream =  new ObjectInputStream(inputStream);
                System.out.println("red Input object");
                Message myMessage = (Message) objectInputStream.readObject();
                System.out.println("Received Object Text: " + myMessage.getText());

                 */
                //System.out.println(myMessage.getText());
                //String test = dis.readUTF();
                //System.out.println("Received message in Server ClassL: " + test);
                //End Test

                System.out.println("Assigning new thread for this client");
                //create a new thread object
                Thread t = new ClientHandler(s, ois, oos);

                // Invoking the start() method
                t.start();

            }
            catch (Exception e){
                s.close();
                e.printStackTrace();
            }
        }
    }
}
