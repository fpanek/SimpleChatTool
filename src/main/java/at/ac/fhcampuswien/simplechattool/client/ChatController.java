package at.ac.fhcampuswien.simplechattool.client;

import at.ac.fhcampuswien.simplechattool.Message;
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
        private final String welcomeMsg;


        public ChatController() {
                client = LoginController.getClientFromLoginController();
                chatcontroller = this;
                System.out.println("ChatController initialized" + Integer.toHexString(hashCode()));

                Client.getStage().setTitle("Simple Chat Tool @ " + client.getUsername());
                welcomeMsg = "Welcome to Simple Chat, " + client.getUsername() + "!";

                Message msgWithUsername = new Message(client.getUsername(), "myUsername");
                msgWithUsername.setInternalInformation(true);
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
                Platform.runLater(()->{
                        items.clear();
                        System.out.println("Method displayUsers called");
                        System.out.println("Online users: " + message.getUsers());
                        flow_onlineUsers.setCellFactory((ListView<String> l) -> new OnlineUsersPanel());
                        items = FXCollections.observableArrayList(message.getUsers());
                        flow_onlineUsers.setItems(items);
                });
        }

        public void addClientMessage(String msg) {
                Message message = new Message(client.getUsername(), msg);
                Text text = new Text(message.getMessage());
                chatBubble(text, "Client");
        }

        public void addOfflineMessage(String msg) {
                Text text = new Text(msg);
                chatBubble(text, "Server");
        }

        public void addRemoteMessage(Message message) {
                Message message1 = new Message(message.getUsername(), message.getText());
                Text text = new Text(message1.getMessage());
                chatBubble(text, "Partner");
        }

        @FXML
        public void sendMessage() {
                if (client == null){
                        addOfflineMessage("Server not yet started...");
                }
                if (field_text.getText().isEmpty()) {
                        addOfflineMessage("Please enter a non-empty message!");
                }
                else if ( field_text.getText().isBlank()){
                        field_text.clear();
                }
                else {
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
                System.out.println("Closing connection to Server.");
                Message disconnectChatMessage = new Message(client.getUsername(), "closeSocket");
                disconnectChatMessage.setInternalInformation(true);
                client.sendObject(disconnectChatMessage);
                client.closeConnection();
                LoginController login = new LoginController();
                login.changeScene("loginwindow.fxml");
        }

        @FXML
        public void exitProgram() {
                try{
                        disconnectChat();
                }catch (Exception e){
                        e.printStackTrace();
                }
                Stage stg = Client.getStage();
                System.out.println("Stage is closing");
                stg.close();
                System.exit(0);
        }

        public void chatBubble(Text text, String source) {
                Color textColorClient = Color.color(0.934, 0.945, 0.996);
                Color textColorServer = Color.color(0.934, 0.945, 0.996);
                Color textColorPartner = Color.BLACK;
                HBox hBox = new HBox();
                TextFlow textFlow = new TextFlow(text);
                hBox.setPadding(new Insets(5,5,5,5));
                textFlow.setPadding(new Insets(8,10,8,10));
                if (source.equals("Client")) {
                        hBox.setAlignment(Pos.TOP_RIGHT);
                        text.setFill(textColorClient);
                        textFlow.setStyle("-fx-color: rgb(239,242,255); " + "-fx-background-color: #7d43e2;" + "-fx-background-radius: 25px;");
                }
                if (source.equals("Partner")) {
                        hBox.setAlignment(Pos.TOP_LEFT);
                        text.setFill(textColorPartner);
                        textFlow.setStyle("-fx-color: rgb(239,242,255); " + "-fx-background-color: #e4e6eb;" + "-fx-background-radius: 25px;");
                }
                if (source.equals("Server")) {
                        hBox.setAlignment(Pos.TOP_LEFT);
                        text.setFill(textColorServer);
                        textFlow.setStyle("-fx-color: rgb(239,242,255); " + "-fx-background-color: #28282a;" + "-fx-background-radius: 25px;");
                }
                text.setStyle("-fx-font: 16 Calibri;");
                hBox.getChildren().add(textFlow);
                vbox_message.getChildren().add(hBox);
                vbox_message.heightProperty().addListener(observable -> ScrollPaneChat.setVvalue(1D));
                flow_onlineUsers.setItems(items);
        }
}
