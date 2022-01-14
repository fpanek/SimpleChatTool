package at.ac.fhcampuswien.simplechattool;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import at.ac.fhcampuswien.simplechattool.Client;
import at.ac.fhcampuswien.simplechattool.Message;
import at.ac.fhcampuswien.simplechattool.Server;

public class ChatController {
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
        private static ChatController chatController;

        public ChatController() {
               //chatController = new ChatController();
        }

        public static ChatController getChatController(){
                return chatController;
        }



        public void addClientMessage(String msg) {
                Client client = LoginController.getMyClient();
                System.out.println("Message to addClientMessage:" + msg + "username" + client.getUsername());
                Message message = new Message(client.getUsername(), msg);
                String addMessage = "[" + message.getTime() + " " + message.getUsername() + "]->\t" + message.getText();
                Text text = new Text(addMessage);
                textFlow.getChildren().add(text);
                textFlow.getChildren().add(new Text(System.lineSeparator()));

        }

        public  void addRemoteMessage(String msg) {
                Client client = LoginController.getMyClient();

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
                Client client = LoginController.getMyClient();
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
