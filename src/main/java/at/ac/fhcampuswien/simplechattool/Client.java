package at.ac.fhcampuswien.simplechattool;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import javafx.stage.WindowEvent;

/*
Sources:
https://github.com/dvcarrillo/sockets-chat
https://github.com/ashmeet4293/Chat-Application-in-java-using-javafx
https://luisgcenci.medium.com/building-a-group-chat-app-with-javafx-multithread-socket-programming-in-java-c8c11fd8c927
*/

public class Client extends Application {
    private String username;
    private Socket clientSocket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private ObjectInputStream objectInStream;
    private ObjectOutputStream objectOutStream;
    private boolean option = true;
    private ChatController chatView;
    private static Stage stg;
    private static String msg;
    public ChatController controller;
    public  TextFlow textFlow;
    boolean validData = true;
    private ObjectOutputStream sendObject;
    private ObjectInputStream receiveObject;
    private static ObjectOutputStream myObjectOutputStream;
    private static ObjectInputStream myObjectInputStream;
    private static Socket myClientSocket;

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

    public static Stage getStage() {
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
        if (port < 0 || port > 65535) {
            System.out.print("Enter the right port number ..! " );
            System.exit(0);
        }

        try {
            clientSocket = new Socket(server, port);
            myClientSocket = clientSocket;

            try {
                OutputStream outputStream = myClientSocket.getOutputStream();
                InputStream inputStream = myClientSocket.getInputStream();
                ObjectOutputStream sendObject = new ObjectOutputStream(outputStream);
                ObjectInputStream receiveObject = new ObjectInputStream(inputStream);
                myObjectOutputStream = sendObject;
                myObjectInputStream = receiveObject;
            } catch (Exception e){
                e.printStackTrace();
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }


        try {
            Task clientThread = new Task() {
                @Override
                protected Object call() throws Exception {
                    while (validData) {
                        listenData(clientSocket);
                        //Thread.sleep(10000);
                        //System.out.println("Executing Threaaad  - still alive");
                    }
                    return null;
                }
            };
            new Thread(clientThread).start();

            System.out.println("Connection successful to server " + server + " and port " + port + ".");
            chatcontroller.addOfflineMessage("Successfully connected to " + server + " port: " + port + ". Have fun ;)");
        } catch (Exception e) {
            System.err.println("ERROR: Connection error");
            e.printStackTrace();
            //System.exit(0);
        }
    }

    public void sendMessage(String msg) {
        try {
            Message myMessage = new Message(getUsername(), msg, "nothing");
            System.out.println("Sending Object to Server..." + myMessage.getText());
            myObjectOutputStream.writeObject(myMessage);
            myObjectOutputStream.flush();

        } catch (Exception e) {
            System.err.println("ERROR: Error sending data");
            e.printStackTrace();
        }
    }

    public void listenData(Socket clientSocket) {
        try {
            Message myMessage = (Message) myObjectInputStream.readObject();
            Platform.runLater(()->{
                ChatController chatcontroller = ChatController.getChatcontroller();
                chatcontroller.addRemoteMessage(myMessage.getText());
            });
        } catch(Exception e) {
            try {
                e.printStackTrace();
                clientSocket.close();
            } catch (Exception ex){
                ex.printStackTrace();
            }
            System.err.println("ERROR: Error listening to data");
            validData = false;
        }
    }

    public void closeConnection() {
        try {
            myObjectOutputStream.close();
            myObjectInputStream.close();
            myClientSocket.close();
        } catch (IOException ex) {
            System.err.println("ERROR: Error closing connection");
        }
    }

    public static void main(String [] args) throws Exception {
        launch(args);
    }

    @Override
    public void start (Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Client.class.getResource("loginwindow.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Simple Chat Tool");
        primaryStage.setScene(new Scene(root, 670, 450));
        primaryStage.setResizable(false);
        primaryStage.show();
        stg = primaryStage;
        stg.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("logo2.png"))));

        stg.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.out.println("Stage is closing");
                stg.close();
                System.exit(0);
            }
        });
    }


}
