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
        private TextField field_text;
        @FXML
        private TextFlow textFlow;

        private Client client;
        private Server server;
        private int mode;

        public ChatController() {
                mode = 0;
        }

        public void setClient(Client cli) {
                mode = 1;
                this.client = cli;
        }

        public void setServer(Server serv) {
                mode = 2;
                this.server = serv;
        }

        public void addClientMessage(String msg) {
                if (mode == 1) {
                        Message message = new Message(client.getUsername(), msg);
                        String addMessage = "[" + message.getTime() + " " + message.getUsername() + "]->\t" + message.getText();
                        Text text = new Text(addMessage);
                        textFlow.getChildren().add(text);
                        textFlow.getChildren().add(new Text(System.lineSeparator()));
                } else {
                        Message message = new Message(server.getUsername(), msg);
                        String addMessage = "[" + message.getTime() + " " + message.getUsername() + "]->\t" + message.getText();
                        Text text = new Text(addMessage);
                        textFlow.getChildren().add(text);
                        textFlow.getChildren().add(new Text(System.lineSeparator()));
                }
        }

        public void addRemoteMessage(String msg) {
                if (mode == 1) {
                        Message message = new Message("Partner", msg);
                        String addMessage = "[" + message.getTime() + " " + message.getUsername() + "]->\t" + message.getText();
                        Text text = new Text(addMessage);
                        textFlow.getChildren().add(text);
                        textFlow.getChildren().add(new Text(System.lineSeparator()));
                } else {
                        Message message = new Message("Partner", msg);
                        String addMessage = "[" + message.getTime() + " " + message.getUsername() + "]->\t" + message.getText();
                        Text text = new Text(addMessage);
                        textFlow.getChildren().add(text);
                        textFlow.getChildren().add(new Text(System.lineSeparator()));
                }
        }

        @FXML
        public void sendMessage(ActionEvent event) {
                if (field_text.getText().isEmpty()) {
                        Text warning = new Text("Please enter a non-empty message!");
                        warning.setFill(Color.RED);
                        textFlow.getChildren().add(warning);
                        textFlow.getChildren().add(new Text(System.lineSeparator()));
                } else {
                        String msg = field_text.getText();
                        if (mode == 1) {
                                client.sendMessage(msg);
                        } else {
                                server.sendMessage(msg);
                        }
                        addClientMessage(msg);
                }

                String msg = field_text.getText();
                if (mode == 1) {
                        client.sendMessage(msg);
                } else {
                        server.sendMessage(msg);
                }
                addClientMessage(msg);
                field_text.clear();
        }

        @FXML
        public void disconnectChat() throws Exception {
                if(mode == 1) {
                        client.closeConnection();
                } else {
                        server.closeConnection();
                }
                LoginController login = new LoginController();
                login.changeScene("loginwindow.fxml");

        }
}
