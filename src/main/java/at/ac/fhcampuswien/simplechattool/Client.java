package at.ac.fhcampuswien.simplechattool;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import at.ac.fhcampuswien.simplechattool.ChatController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import at.ac.fhcampuswien.simplechattool.LoginController;
import at.ac.fhcampuswien.simplechattool.ChatController;

/*
Sources:
https://github.com/dvcarrillo/sockets-chat
https://github.com/ashmeet4293/Chat-Application-in-java-using-javafx
https://luisgcenci.medium.com/building-a-group-chat-app-with-javafx-multithread-socket-programming-in-java-c8c11fd8c927
*/

public class Client extends Application{
    private String username;
    private Socket clientSocket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private DataInputStream inData;
    private DataOutputStream outData;
    private boolean option = true;
    private ChatController chatView;
    private static Stage stg;
    private static String msg;
    public ChatController controller;
    public  TextFlow textFlow;


    public Client() {
    }

    public static String getMsg(){
        return msg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        username = name;
    }


    public static Stage getStage(){
        return stg;
    }


    public void setConnection(String server, int port) {
        ChatController chatcontroller = ChatController.getChatcontroller();
        boolean success = false;
        while(!success) {
            try {
                InetAddress host = InetAddress.getByName(server);
                System.out.println("Server " + server + " is Online");
                success = true;

            } catch (UnknownHostException e) {
                System.out.println("Server is offline, please provide a correct server");
                System.exit(0);
            }
        }
        if( port < 0 || port > 65535)
        {
            System.out.print("Enter the right port number ..! " );
            System.exit(0);
        }
        try {
            clientSocket = new Socket(server, port);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Adding to gui..");
            //c.addRemoteMessage("Connection established - Ready to chat:");
            Task clientThread = new Task() {
                @Override
                protected Object call() throws Exception {
                    while (true) {
                        listenData(clientSocket);
                        //System.out.println("Executing Threaaad  - still alive");
                    }
                }
            };

            new Thread(clientThread).start();
            System.out.println("Connection successful to server " + server + " and port " + port + ".");
            chatcontroller.addOfflineMessage("Successfully connected to " + server + " port: " + port + " Have fun ;)");
        } catch (IOException e) {
            System.err.println("ERROR: Connection error");
            e.printStackTrace();
            //System.exit(0);
        }
    }

    public void sendMessage(String msg) {
        try {
            outputStream = clientSocket.getOutputStream();
            outData = new DataOutputStream(outputStream);
            outData.writeUTF(msg);
            outData.flush();
        } catch (IOException e) {
            System.err.println("ERROR: Error sending data");
        }
    }

    public void listenData(Socket clientSocket) {
        try {
            inputStream = clientSocket.getInputStream();
            inData = new DataInputStream(inputStream);
            String Message =  inData.readUTF();
            //System.out.println("Message as Tring: " + Message);
            Platform.runLater(()->{
                    ChatController chatcontroller = ChatController.getChatcontroller();
                    chatcontroller.addRemoteMessage(Message);
            });
        } catch(IOException e) {
            System.err.println("ERROR: Error listening to data");
        }
    }

    public void closeConnection() {
        try {
            outData.close();
            inData.close();
            clientSocket.close();
        } catch (IOException ex) {
            System.err.println("ERROR: Error closing connection");
        }
    }

    public  static void main(String [] args) throws Exception {
        launch(args);
    }

    @Override
    public void start (Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Client.class.getResource("loginwindow.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Simple Chat Tool");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
        stg = primaryStage;
    }


}
