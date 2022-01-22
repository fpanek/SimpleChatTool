package at.ac.fhcampuswien.simplechattool;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;

public class LoginController {
    @FXML
    private Button btn_login;
    @FXML
    private TextField input_nickname;
    @FXML
    private TextField input_port;
    @FXML
    private TextField input_server;
    @FXML
    private TextFlow warning_msg;
    private static Client myClient;


    public LoginController() {
        Platform.runLater(()->{
            input_nickname.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ENTER)  {
                    try {
                        userLogin();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            btn_login.setOnAction(actionEvent -> {
                try {
                    userLogin();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    public static Client getClientFromLoginController() {
        return myClient;
    }

    public void changeScene(String fxml) throws IOException {
        Stage stg = Client.getStage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent pane = loader.load();
        stg.getScene().setRoot(pane);
    }

    @FXML
    public void userLogin() throws IOException {
        System.out.println("Login Button pressed");
        checkLogin();
    }

    @FXML
    public void checkLogin() throws IOException {

        if (input_server.getText().isEmpty() || input_nickname.getText().isEmpty()) {
            warning_msg.getChildren().clear();
            Text message = new Text("Error: Please enter your data!");
            message.setFill(Color.RED);
            message.setStyle("-fx-font: 14 System;");
            warning_msg.getChildren().addAll(message);
        }

        String port;
        if (input_port.getText().isEmpty()) {
            port = "5056";
        } else {
            port = input_port.getText();
            boolean flag = true;
            for (int i = 0; i < port.length(); i++) {
                if (i==0 && port.charAt(i) == '-')
                    continue;
                if (!Character.isDigit(port.charAt(i)))
                    flag = false;
            }
            if (!flag) {
                warning_msg.getChildren().clear();
                Text message = new Text("Error: Port number must be a number.");
                message.setFill(Color.RED);
                message.setStyle("-fx-font: 14 System;");
                warning_msg.getChildren().addAll(message);
                return;
            } else if (Integer.parseInt(input_port.getText()) < 0 || Integer.parseInt(input_port.getText()) > 65535) {
                warning_msg.getChildren().clear();
                Text message = new Text("Error: Port number must be between 0 and 65535.");
                message.setFill(Color.RED);
                message.setStyle("-fx-font: 14 System;");
                warning_msg.getChildren().addAll(message);
                return;
            } else {
                port = input_port.getText();
            }
        }

        if (!input_server.getText().isEmpty()) {
            InetAddress host = InetAddress.getByName(input_server.getText());
            if (host.isReachable(1000)) {
                String ip = input_server.getText();
            } else {
                warning_msg.getChildren().clear();
                Text message = new Text("Error: Entered server is offline. Please enter a reachable server.");
                message.setFill(Color.RED);
                message.setStyle("-fx-font: 14 System;");
                warning_msg.getChildren().addAll(message);
                return;
            }
        }

        if (!input_nickname.getText().isEmpty() && !input_server.getText().isEmpty()) {
            LoginData logindata = LoginData.getLogindata();
            logindata.setUsername(input_nickname.getText());
            logindata.setServerIP(input_server.getText());
            logindata.setServerPort(Integer.parseInt(port));

            System.out.println("Username from input: " + input_nickname.getText() + ", server: " + input_server.getText() + ", port: " + port);

            Client client = new Client();
            myClient = client;
            client.setConnection(logindata.getServerIP(), logindata.getServerPort());
            client.setUsername(logindata.getUsername());

            changeScene("basic_chat_v2.fxml");
        }
    }
}
