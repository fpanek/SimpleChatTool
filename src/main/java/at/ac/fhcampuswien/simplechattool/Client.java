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
    //private static String Message;
    String Message;
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
        ChatController c = ChatController.getChatcontroller();
        //c.addClientMessage("ooooooo");
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
            //ChatController c = ChatController.getChatcontroller();
            //c.addClientMessage("haloasdf");
            //ChatController c = ChatController.getChatcontroller();
            c.addRemoteMessage("Connection established - Ready to chat:");

            Task clientThread = new Task() {
                @Override
                protected Object call() throws Exception {
                    while (true) {
                        listenData(clientSocket);
                        System.out.println("Executing Threaaad  - still alive");
                        //ChatController c = ChatController.getChatcontroller();
                        //c.addRemoteMessage("ooooooo");
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }
                        //ChatController d = ChatController.getChatcontroller();
                        //d.addRemoteMessage("teeeeest");
                    }
                    //return null;
                }
            };

            new Thread(clientThread).start();
            /*
            Thread clientThread = new Thread(() -> {
                while(option) {
                    listenData(clientSocket);
                }
            });
            clientThread.start();

             */
            System.out.println("Connection successful to server " + server + " and port " + port + ".");
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
            ChatController c = ChatController.getChatcontroller();
            c.addRemoteMessage("Message from Button: haloasdf");
        } catch (IOException e) {
            System.err.println("ERROR: Error sending data");
        }
    }

    public void setMesage(String Message){
        this.Message = Message;
    }

    public String getMessage(){
        return Message;
    }

    public void listenData(Socket clientSocket) {
        try {
            inputStream = clientSocket.getInputStream();
            inData = new DataInputStream(inputStream);




            //System.out.print("Received Message: " + inData.readUTF());
            String Message = (String) inData.readUTF();

            System.out.println("Message as Tring: " + Message);
            //System.out.print("Received Message after STringoutadsf: " + inData.readUTF());

            Platform.runLater(()->{
                    ChatController x = ChatController.getChatcontroller();
                    x.addRemoteMessage(Message);
                    //sendMessage("asdflllllllllll");
            });
            //Message = inData.readUTF();
            //controller.addRemoteMessage("asdfasdf");
            //Text text = new Text("sdfdsf");
            //addRemoteMessage("asdfasdf");


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
        //ChatController chatview
        //ChatController unserController = ChatController.getChatController();
        //chatView = unserController;


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Client.class.getResource("loginwindow.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Simple Chat Tool");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
        stg = primaryStage;
        //stg = primaryStage;
        //Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("loginwindow.fxml")));
        //primaryStage.setTitle("Simple Chat Tool");
        //primaryStage.setScene(new Scene(root, 600, 400));
        //primaryStage.show();
    }


}
