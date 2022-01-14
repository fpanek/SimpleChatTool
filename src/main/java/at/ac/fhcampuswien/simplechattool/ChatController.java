package at.ac.fhcampuswien.simplechattool;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.net.URL;
import at.ac.fhcampuswien.simplechattool.Client;
import at.ac.fhcampuswien.simplechattool.Message;
import at.ac.fhcampuswien.simplechattool.Server;

import java.util.ResourceBundle;

public class ChatController  implements Initializable{
        @FXML
        private Button btn_disconnect;
        @FXML
        private Button btn_send;
        @FXML
        private Button btn_SendMessage;
        @FXML
        private TextField field_text;
        @FXML
        public  TextFlow textFlow;

        private Client client;
        private Server server;
        private int mode;
        //private static ChatController myChatController;

        private static ChatController chatcontroller;


        private static Client myClient;

        @Override
        public void initialize(URL location, ResourceBundle resources){
// Implementing the Initializable interface means that this method
                // will be called when the controller instance is created

        }

        public ChatController() {
                //Client client = LoginController.getMyClient();
                //client.setConnection("localhost", 5056);
                System.out.println("");
                System.out.println("Chat Controller created: " + Integer.toHexString(hashCode()));

                LoginData logindata = LoginData.getLogindata();
                Client client = new Client();
                client.setConnection(logindata.getServerIP(), logindata.getServerPort());
                client.setUsername(logindata.getUsername());
                myClient = client;
                //addRemoteMessage("sdfsdfewre");
                chatcontroller = this;

        }

        public static Client getClientFromChatController(){
                return myClient;
        }

        public  static ChatController getChatcontroller(){
                return chatcontroller;
        }


        public void addClientMessage(String msg) {
                //Client client = LoginController.getMyClient();
                Client client = ChatController.getClientFromChatController();
                System.out.println("Message to addClientMessage:" + msg + "username" + client.getUsername());
                Message message = new Message(client.getUsername(), msg);
                String addMessage = "[" + message.getTime() + " " + message.getUsername() + "]->\t" + message.getText();
                Text text = new Text(addMessage);
                textFlow.getChildren().add(text);
                textFlow.getChildren().add(new Text(System.lineSeparator()));

        }

        public  void addRemoteMessage(String msg) {
                Client client = LoginController.getMyClient();
                System.out.println("mehtot for rempote output");
                System.out.println("Message to GUI: " + msg);
                //Message message = new Message("Partner", msg);
                //String addMessage = "[" + message.getTime() + " " + message.getUsername() + "]->\t" + message.getText();
                //Text text = new Text(addMessage);
                //textFlow.getChildren().add(text);
                Text text = new Text(msg);
                textFlow.getChildren().add(text);
                textFlow.getChildren().add(new Text(System.lineSeparator()));
        }

        @FXML
        public void sendMessage() {
                //Client client = LoginController.getMyClient();
                Client client = ChatController.getClientFromChatController();
                //System.out.0.println("Button for Message send pressed before any if...");
                if (field_text.getText().isEmpty()) {
                        Text warning = new Text("Please enter a non-empty message!");
                        warning.setFill(Color.RED);
                        textFlow.getChildren().add(warning);
                        textFlow.getChildren().add(new Text(System.lineSeparator()));
                } else {
                        System.out.println("Button for Message send pressed");
                        String msg = field_text.getText();
                        System.out.println("Message read from input: " + msg);
                        //client.sendMessage("Automated Message after pressing Button:");
                        //client.sendMessage(msg);
                        client.sendMessage(msg);
                        addClientMessage(msg);

                }


                //addClientMessage(msg);
                field_text.clear();
        }

        @FXML
        public void disconnectChat() throws Exception {
                Client client = LoginController.getMyClient();
                client.closeConnection();
                LoginController login = new LoginController();
                login.changeScene("loginwindow.fxml");

        }
}
