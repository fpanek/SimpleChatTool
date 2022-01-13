package at.ac.fhcampuswien.simplechattool;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import at.ac.fhcampuswien.simplechattool.LoginController;
import at.ac.fhcampuswien.simplechattool.ChatController;


public class Server {
    private String username;
    private Socket myService;
    private ServerSocket serviceSocket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private DataOutputStream outputData;
    private DataInputStream inputData;
    private boolean option = true;
    private ChatController chatView;

    public Server() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public void setConnection(int port) {
        try {
            serviceSocket = new ServerSocket(port);
            System.out.println("Server listening on port " + port);
            System.out.println("Client not connected...");
            myService = serviceSocket.accept();
            System.out.println("Client accepted");
            Thread serverThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (option) {
                        listenData();
                    }
                }
            });
            serverThread.start();
            System.out.println("Successfully connected");
        } catch (IOException ex) {
            System.err.println("ERROR: Connection error");
            System.exit(0);
        }
    }

    public void listenData() {
        try {
            inputStream = myService.getInputStream();
            inputData = new DataInputStream(inputStream);
            //chatView.addRemoteMessage(inputData.readUTF());
            System.out.println(inputData.readUTF());

        } catch (IOException ex) {
            System.err.println("ERROR: Error listening to data");
            try{
                myService.close();
            } catch (IOException exx){
                ex.printStackTrace();
            }
            ex.printStackTrace();
        }
    }

    public void sendMessage(String msg) {
        try {
            outputStream = myService.getOutputStream();
            outputData = new DataOutputStream(outputStream);
            outputData.writeUTF(msg);
            outputData.flush();
        } catch (IOException ex) {
            System.err.println("ERROR: Error sending data");
        }
    }

    public void closeConnection() {
        try {
            outputData.close();
            inputData.close();
            serviceSocket.close();
            myService.close();
        } catch (IOException ex) {
            System.err.println("ERROR: Error closing connection");
        }
    }

    public static void main(String [] args) throws Exception {
        //ChatController chatView = new ChatController();
        //Server[] server = new Server[10];
        Server server = new Server();

        //LoginController loginController = new LoginController();
        String username = "Server";
        //server[0] = new Server();
        server.setUsername(username);

        int port = 60000;


        //while(true){
        //    server[0].setConnection(port);
        //}
        server.setConnection(port);
        //chatView.setServer(server);
    }

}


/*
public class Server {

    public static void main(String[] args){
        final ServerSocket serverSocket ;
        final Socket clientSocket ;
        final BufferedReader in;
        final PrintWriter out;
        final Scanner sc = new Scanner(System.in);

        try {
            serverSocket = new ServerSocket(60000);
            System.out.println("Server is waiting for clients!");
            clientSocket = serverSocket.accept();
            System.out.println("A new Client has been connected!");
            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader (new InputStreamReader(clientSocket.getInputStream()));

            Thread sender= new Thread(new Runnable() {
                String msg; //variable that will contains the data writter by the user
                SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                @Override   // annotation to override the run method
                public void run() {
                    while(true){
                        msg = sc.nextLine(); //reads data from user's keybord
                        System.out.println("Server " + formatter.format(new Date()) + " : "  + msg );
                        out.println(msg);    // write data stored in msg in the clientSocket
                        out.flush();   // forces the sending of the data
                    }
                }
            });
            sender.start();

            Thread receive= new Thread(new Runnable() {
                String msg ;
                SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                @Override
                public void run() {
                    try {
                        msg = in.readLine();
                        //tant que le client est connecté
                        while(msg!=null){
                            System.out.println(msg);
                            msg = in.readLine();
                        }

                        System.out.println("Client déconecté");

                        out.close();
                        clientSocket.close();
                        serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            receive.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
 */
