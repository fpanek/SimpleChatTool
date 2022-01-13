package at.ac.fhcampuswien.simplechattool;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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

public class Client {
    private String username;
    private Socket clientSocket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private DataInputStream inData;
    private DataOutputStream outData;
    private boolean option = true;
    private ChatController chatView;
    private ChatController chatViewMeins;


    public Client(ChatController chatView) {
        this.chatView = chatView;
    }

    public Client() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        username = name;
    }



    public ChatController getChatController(){
        return chatViewMeins;
    }

    public void setConnection(String server, int port) {
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
            Thread clientThread = new Thread(() -> {
                while(option) {
                    listenData(clientSocket);

                }
            });
            clientThread.start();
            System.out.println("Connection successful to server " + server + " and port " + port + ".");
        } catch (IOException e) {
            System.err.println("ERROR: Connection error");
            System.exit(0);
        }
    }

    public void sendMessage(String msg) {
        try {
            outputStream = clientSocket.getOutputStream();
            outData = new DataOutputStream(outputStream);
            //Add Date?
            //SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            //outData.write(formatter.format(new Date()).toString());
            //Add username?
            outData.writeUTF(msg);
            outData.flush();
        } catch (IOException e) {
            System.err.println("ERROR: Error sending data");
        }
    }

    public void listenData(Socket clientSocket) {
        try {
            Client client = LoginController.getMyClient();
            inputStream = clientSocket.getInputStream();
            inData = new DataInputStream(inputStream);
            //String outTest = new BufferedReader(new InputStreamReader(inputStream));
            //System.out.print("Received Message: " + inData.readUTF());
            //ChatController asdf = getChatController();
            //asdf.addRemoteMessage(inData.readUTF());
            //asdf.addClientMessage(inData.readUTF());
            //chatView.addRemoteMessage("adsfasdlfkjlk");
            //System.out.println(inData);
            Message newMessage = new Message(client.getUsername(), "Textiwas");

            //client.chatView.addRemoteMessage(inData.readUTF());
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
        ChatController chatView = new ChatController();
        Client client = new Client(chatView);

        LoginController loginController = new LoginController();
        String username = loginController.getUsername();
        client.setUsername(username);

        String ip = loginController.getIP();
        int port = loginController.getPort();
        client.setConnection(ip, port);

        chatView.setClient(client);
        //chatViewMeins = chatView;
    }


}

/*
public class Client {

    private Socket clientSocket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private String username = "";
    private ChatController c = new ChatController();
    private TextFlow chat = c.getTextFlow();

    public Client(String server, int port, String nickname) {

        try {
            clientSocket = new Socket(server, port);
            in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            out = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

            username = nickname;
            setUsername(username);
            Text welcome = new Text("Welcome " + username + "! You are now online.");
            chat.getChildren().add(welcome);
            welcome.setFill(Color.GREEN);
            chat.getChildren().add(new Text(System.lineSeparator()));

        } catch (Exception e) {
            e.printStackTrace();
        }

        Thread sender = new Thread(() -> {
            String msg;
            LocalDate localDate = LocalDate.now();
            LocalTime localTime = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
            try {
                msg = c.getMessage();
                String input = username + " @ " + localDate + " " + localTime + ": " + msg + "\n";
                Text text = new Text(input);
                chat.getChildren().addAll(text);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        sender.start();

        Thread receiver = new Thread(new Runnable() {
            String msg;
            LocalDate localDate = LocalDate.now();
            LocalTime localTime = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);

            public void run() {
                try {
                    msg = c.getMessage();
                    while (msg != null) {
                        String input = "Server @ " + localDate + " " + localTime + ": " + msg + "\n";
                        Text text = new Text(input);
                        chat.getChildren().addAll(text);
                        msg = c.getMessage();
                    }
                    chat.getChildren().add(new Text("Server out of service"));
                    out.close();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        receiver.start();
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

    //public static void main(String[] args) {
        /*
        try {

            Thread receiver = new Thread(new Runnable()
            {
                String msg;
                SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

                public void run() {
                    try {
                        msg = in.readLine();
                        while(msg!=null)
                        {
                            System.out.println("Server " + formatter.format(new Date()) + " : " + msg);
                            msg = in.readLine();
                        }

                        System.out.println("Server out of service");
                        out.close();
                        clientSocket.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            receiver.start();
        }catch (IOException e){
            e.printStackTrace();
        }


    }
}
*/