package at.ac.fhcampuswien.simplechattool;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

/*
Sources:
https://github.com/dvcarrillo/sockets-chat
https://github.com/ashmeet4293/Chat-Application-in-java-using-javafx
https://luisgcenci.medium.com/building-a-group-chat-app-with-javafx-multithread-socket-programming-in-java-c8c11fd8c927
*/

public class Client extends Application {
    private String username;
    public String successMsg;
    private Socket clientSocket;
    private static Stage stg;
    boolean validData = true;
    private static ObjectOutputStream myObjectOutputStream;
    private static ObjectInputStream myObjectInputStream;
    private static Socket myClientSocket;

    public Client() {
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
                    }
                    return null;
                }
            };
            new Thread(clientThread).start();
            System.out.println("Connection successful to server " + server + " and port " + port + ".");
            successMsg = "You are connected via " + server + " and port " + port + ". Have fun ;)";

        } catch (Exception e) {
            System.err.println("ERROR: Connection error");
            e.printStackTrace();
            //System.exit(0);
        }
    }

    public void sendObject(Message message) {
        try {
            System.out.println("Sending Object to Server..." + message.getText());
            myObjectOutputStream.writeObject(message);
            //ChatController chatcontroller = ChatController.getChatController();
            //chatcontroller.displayUsers(message);
            myObjectOutputStream.flush();
        } catch (Exception e) {
            System.err.println("ERROR: Error sending data");
            e.printStackTrace();
        }
    }


    public void sendMessage(String msg) {
        try {
            Message myMessage = new Message(getUsername(), msg, "nothing");
            if (!myMessage.InternalInformation) {
                System.out.println("Sending Object to Server..." + myMessage.getText());
                myObjectOutputStream.writeObject(myMessage);
                myObjectOutputStream.flush();
            } else {
                //myObjectOutputStream.writeObject(myMessage.getUsers());
                //ChatController chatcontroller = ChatController.getChatController();
                //chatcontroller.displayUsers(myMessage);
                //myObjectOutputStream.flush();
            }
        } catch (Exception e) {
            System.err.println("ERROR: Error sending data");
            e.printStackTrace();
        }
    }

    public void listenData(Socket clientSocket) {
        try {
            Message myMessage = (Message) myObjectInputStream.readObject();
            ChatController chatcontroller = ChatController.getChatController();
                if (myMessage.InternalInformation) {
                    Platform.runLater(()->{;
                        chatcontroller.displayUsers(myMessage);
                    });
                } else {
                    System.out.println("Connected User: " + myMessage.getUsers());
                    Platform.runLater(()->{;
                        chatcontroller.addRemoteMessage(myMessage);
                    });
                }
            System.out.println("received connected clients:");
            System.out.println(myMessage.getUsers());
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

    public static void main(String [] args) {
        launch(args);
    }

    @Override
    public void start (Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Client.class.getResource("loginwindow.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Simple Chat Tool");
        primaryStage.setScene(new Scene(root, 670, 450));
        primaryStage.setResizable(false);
        primaryStage.show();
        stg = primaryStage;
        stg.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("logo2.png"))));

        stg.setOnCloseRequest(we -> {
            System.out.println("Stage is closing");
            stg.close();
            System.exit(0);
        });
    }
}
