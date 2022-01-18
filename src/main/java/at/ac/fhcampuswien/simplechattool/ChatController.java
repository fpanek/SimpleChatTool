package at.ac.fhcampuswien.simplechattool;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.ObjectOutputStream;
import java.net.URL;
import at.ac.fhcampuswien.simplechattool.Client;
import at.ac.fhcampuswien.simplechattool.Message;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class ChatController {
        @FXML
        private Button btn_disconnect;
        @FXML
        private Button btn_SendMessage;
        @FXML
        private Button btn_StartServer;
        @FXML
        private Button btn_Help;
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

        private Client client;
        private static ChatController chatcontroller;
        private static Client myClient;

        public ChatController() {
                myClient = client;
                chatcontroller = this;
                System.out.println("initialize wihtou mehtod Calleasdfasdf");

                Platform.runLater(()->{
                        ChatController chatcontroller = ChatController.getChatcontroller();
                        Client client = ChatController.getClientFromChatController();
                        if (client == null) {
                                chatcontroller.btn_SendMessage.setDisable(true);
                                chatcontroller.btn_disconnect.setDisable(true);
                                chatcontroller.btn_StartServer.setTooltip(new Tooltip("Connect"));
                                chatcontroller.flow_onlineUsers.setItems(items);
                        }
                });
        }

        public static Client getClientFromChatController() {
                return myClient;
        }

        public static ChatController getChatcontroller() {
                return chatcontroller;
        }

        public void addClientMessage(String msg) {
                Client client = ChatController.getClientFromChatController();
                Message message = new Message(client.getUsername(), msg);
                Text text = new Text(message.getMessage());

                HBox hBox = new HBox();
                hBox.setAlignment(Pos.TOP_RIGHT);

                hBox.setPadding(new Insets(5,5,5,5));
                TextFlow textFlow = new TextFlow(text);
                textFlow.setStyle("-fx-color: rgb(239,242,255); " +
                        "-fx-background-color:  #7d43e2;" +
                        "-fx-background-radius: 25px;");

                textFlow.setPadding(new Insets(5,10,5,10));
                text.setFill(Color.color(0.934, 0.945, 0.996));
                text.setStyle("-fx-font: 16 Calibri;");

                hBox.getChildren().add(textFlow);
                vbox_message.getChildren().add(hBox);
                ScrollPaneChat.setVvalue(1.0);

                //textFlow.getChildren().add(text);
                //textFlow.getChildren().add(new Text(System.lineSeparator()));

                items = FXCollections.observableArrayList(message.getUsers());
                flow_onlineUsers.setItems(items);
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

                textFlow.setPadding(new Insets(5,10,5,10));
                text.setFill(Color.color(0.934, 0.945, 0.996));
                text.setStyle("-fx-font: 16 Calibri;");

                hBox.getChildren().add(textFlow);
                vbox_message.getChildren().add(hBox);
                ScrollPaneChat.setVvalue(1.0);

                //text.setStyle("-fx-font: 16 Calibri;");
                //textFlow.getChildren().add(text);
                //textFlow.getChildren().add(new Text(System.lineSeparator()));
                //ScrollPaneChat.setVvalue(1.0);
        }

        public void addRemoteMessage(Message message) {
                Message message1 = new Message(message.getUsername(), message.getText());
                Text text = new Text(message1.getMessage());

                HBox hBox = new HBox();
                hBox.setAlignment(Pos.TOP_LEFT);
                hBox.setPadding(new Insets(5,5,5,5));
                TextFlow textFlow = new TextFlow(text);
                textFlow.setStyle("-fx-color: rgb(239,242,255); " +
                        "-fx-background-color:  #7d43e2;" +
                        "-fx-background-radius: 25px;");

                textFlow.setPadding(new Insets(5,10,5,10));
                text.setFill(Color.color(0.934, 0.945, 0.996));
                text.setStyle("-fx-font: 16 Calibri;");

                hBox.getChildren().add(textFlow);
                vbox_message.getChildren().add(hBox);
                ScrollPaneChat.setVvalue(1.0);

                /*
                text.setStyle("-fx-font: 16 Calibri;");
                textFlow.getChildren().add(text);
                textFlow.getChildren().add(new Text(System.lineSeparator()));
                ScrollPaneChat.setVvalue(1.0);
                 */
        }

        @FXML
        public void startServer() {
                System.out.println("Using Chat Controller: " + Integer.toHexString(hashCode()));
                LoginData logindata = LoginData.getLogindata();
                Client client = new Client();
                myClient = client;
                client.setConnection(logindata.getServerIP(), logindata.getServerPort());
                client.setUsername(logindata.getUsername());
                ScrollPaneChat.setVvalue(1.0);

                ChatController meins = ChatController.getChatcontroller();
                meins.btn_SendMessage.setDisable(false);
                meins.btn_SendMessage.setTooltip(new Tooltip("Send"));
                meins.btn_disconnect.setDisable(false);
                meins.btn_disconnect.setTooltip(new Tooltip("Disconnect"));
                meins.btn_StartServer.setDisable(true);
                meins.btn_Help.setTooltip(new Tooltip("Help"));
                meins.btn_Exit.setTooltip(new Tooltip("Exit"));

                btn_SendMessage.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                                sendMessage();
                        }
                });

                field_text.setOnKeyPressed(new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent keyEvent) {
                                if (keyEvent.getCode() == KeyCode.ENTER)  {
                                        sendMessage();
                                }
                        }
                });
        }

        @FXML
        public void sendMessage() {
                Client client = ChatController.getClientFromChatController();
                if (client == null){
                        addOfflineMessage("Server not yet started...");
                }

                if (field_text.getText().isEmpty()) {
                        Text warning = new Text("Please enter a non-empty message!");
                        warning.setFill(Color.RED);
                        warning.setStyle("-fx-font: 16 Calibri;");
                        //textFlow.getChildren().add(warning);
                        //textFlow.getChildren().add(new Text(System.lineSeparator()));
                        vbox_message.getChildren().add(warning);
                        vbox_message.getChildren().add(new Text(System.lineSeparator()));
                        ScrollPaneChat.setVvalue(1.0);
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
                Client client = ChatController.getClientFromChatController();
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

        public static void addLabel(String msgFromServer, VBox vBox){
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.TOP_LEFT);
                hBox.setPadding(new Insets(5,5,5,10));

                Text text = new Text(msgFromServer);
                TextFlow textFlow = new TextFlow(text);
                textFlow.setStyle("-fx-color: rgb(239,242,255); " +
                        "-fx-background-color:  #7d43e2;" +
                        "-fx-background-radius: 25px;");
                textFlow.setPadding(new Insets(5,10,5,10));
                text.setFill(Color.color(0.934, 0.945, 0.996));
                hBox.getChildren().add(textFlow);
                Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                                vBox.getChildren().add(hBox);
                        }
                });

        }
}
