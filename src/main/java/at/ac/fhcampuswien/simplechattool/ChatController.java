package at.ac.fhcampuswien.simplechattool;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ChatController {
        @FXML
        private Button btn_disconnect;
        @FXML
        private Button btn_SendMessage;
        @FXML
        private Button btn_Exit;
        @FXML
        private TextField field_text;
        @FXML
        public TextFlow textFlow;
        @FXML
        private ListView<String> flow_onlineUsers;
        private ObservableList<String> items = FXCollections.observableArrayList();
        @FXML
        private VBox vbox_message;
        @FXML
        private ScrollPane ScrollPaneChat;
        private static Client client;
        private static ChatController chatcontroller;
        private String msg;
        private String welcomeMsg;


        public ChatController() {
                client = LoginController.getClientFromLoginController();
                chatcontroller = this;
                System.out.println("ChatController initialized");
                System.out.println("Using ChatController: " + Integer.toHexString(hashCode()));
                welcomeMsg = "Welcome to Simple Chat, " + client.getUsername() + "!";
                Message msgWithUsername = new Message(client.getUsername(), "myUsername");
                msgWithUsername.InternalInformation = true;
                client.sendObject(msgWithUsername);

                Platform.runLater(()->{
                        chatcontroller.btn_SendMessage.setTooltip(new Tooltip("Send"));
                        chatcontroller.btn_disconnect.setTooltip(new Tooltip("Disconnect"));
                        chatcontroller.btn_Exit.setTooltip(new Tooltip("Exit"));

                        btn_SendMessage.setOnAction(actionEvent -> sendMessage());
                        field_text.setOnKeyPressed(keyEvent -> {
                                if (keyEvent.getCode() == KeyCode.ENTER) {
                                        sendMessage();
                                }
                        });
                });
        }

        @FXML
        public void initialize() {
                Platform.runLater(()->{
                        msg = client.successMsg;
                        addOfflineMessage(msg);
                });
                addOfflineMessage(welcomeMsg);
        }

        public static ChatController getChatController() {
                return chatcontroller;
        }

        public void displayUsers(Message message) {
                        items.clear();
                        System.out.println("Method displayUsers called");
                        System.out.println("Online users: " + message.getUsers());
                        items = FXCollections.observableArrayList(message.getUsers());
                        flow_onlineUsers.setItems(items);
        }

        public void addClientMessage(String msg) {
                Message message = new Message(client.getUsername(), msg);
                message.setInternalInformation(false);
                Text text = new Text(message.getMessage());
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.TOP_RIGHT);
                hBox.setPadding(new Insets(5,5,5,5));
                TextFlow textFlow = new TextFlow(text);
                textFlow.setStyle("-fx-color: rgb(239,242,255); " +
                        "-fx-background-color:  #7d43e2;" +
                        "-fx-background-radius: 25px;");
                textFlow.setPadding(new Insets(8,10,8,10));
                text.setFill(Color.color(0.934, 0.945, 0.996));
                text.setStyle("-fx-font: 16 Calibri;");
                hBox.getChildren().add(textFlow);
                vbox_message.getChildren().add(hBox);
                vbox_message.heightProperty().addListener(observable -> ScrollPaneChat.setVvalue(1D));
        }

        public void addOfflineMessage(String msg) {
                Text text = new Text(msg);
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.TOP_LEFT);
                hBox.setPadding(new Insets(5,5,5,5));
                TextFlow textFlow = new TextFlow(text);
                textFlow.setStyle("-fx-color: rgb(239,242,255); " +
                        "-fx-background-color:  #28282a;" +
                        "-fx-background-radius: 25px;");
                textFlow.setPadding(new Insets(8,10,8,10));
                text.setFill(Color.color(0.934, 0.945, 0.996));
                text.setStyle("-fx-font: 16 Calibri;");
                hBox.getChildren().add(textFlow);
                vbox_message.getChildren().add(hBox);
                vbox_message.heightProperty().addListener(observable -> ScrollPaneChat.setVvalue(1D));
        }

        public void addRemoteMessage(Message message) {
                Message message1 = new Message(message.getUsername(), message.getText());
                message1.setInternalInformation(false);
                Text text = new Text(message1.getMessage());

                HBox hBox = new HBox();
                hBox.setAlignment(Pos.TOP_LEFT);
                hBox.setPadding(new Insets(5,5,5,5));
                TextFlow textFlow = new TextFlow(text);
                textFlow.setStyle("-fx-color: rgb(239,242,255); " +
                        "-fx-background-color:  #e4e6eb;" +
                        "-fx-background-radius: 25px;");
                textFlow.setPadding(new Insets(8,10,8,10));
                text.setFill(Color.BLACK);
                text.setStyle("-fx-font: 16 Calibri;");
                hBox.getChildren().add(textFlow);
                vbox_message.getChildren().add(hBox);
                vbox_message.heightProperty().addListener(observable -> ScrollPaneChat.setVvalue(1D));
        }

        @FXML
        public void sendMessage() {
                if (client == null){
                        addOfflineMessage("Server not yet started...");
                }

                if (field_text.getText().isEmpty() || field_text.getText().isBlank()) {
                        addOfflineMessage("Please enter a non-empty message!");
                } else {
                        String msg = field_text.getText();
                        try {
                                client.sendMessage(msg);
                        } catch (NullPointerException e){
                                e.printStackTrace();
                        }
                        addClientMessage(msg);
                }
                field_text.clear();
        }

        @FXML
        public void disconnectChat() throws Exception {
                client.sendMessage("CloseSocket");
                client.closeConnection();
                LoginController login = new LoginController();
                login.changeScene("loginwindow.fxml");
        }

        @FXML
        public void exitProgram() {
                Stage stg = Client.getStage();
                System.out.println("Stage is closing");
                stg.close();
                System.exit(0);
        }
}
