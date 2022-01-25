package at.ac.fhcampuswien.simplechattool;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {

        // server is listening on port 5056
        ServerSocket ss = new ServerSocket(5056);

        // running infinite loop for getting client request
        while (true) {
            Socket s = null;
            try {
                // socket object to receive incoming client requests
                s = ss.accept();

                System.out.println("A new client is connected: " + s);

                // obtaining input and out streams
                OutputStream outputStream = s.getOutputStream();
                InputStream inputStream = s.getInputStream();
                ObjectOutputStream oos = new ObjectOutputStream(outputStream);
                ObjectInputStream ois = new ObjectInputStream(inputStream);

                System.out.println("Assigning new thread for this client");

                //create a new thread object
                Thread t = new ClientHandler(s, ois, oos);

                // Invoking the start() method
                t.start();
            }
            catch (Exception e) {
                assert s != null;
                s.close();
                e.printStackTrace();
            }
        }
    }
}
