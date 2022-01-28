package at.ac.fhcampuswien.simplechattool.client;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
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
    @FXML
    private ImageView simpleLogo;
    private static Client myClient;


    public LoginController() {
        Platform.runLater(()->{
            input_nickname.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ENTER)  {
                    try {
                        userLogin();
                    } catch (IOException e) {
                        System.out.println("Error checking login");
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
                Text message = new Text("Error: Port must be a number.");
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
            if (!host.isReachable(1000)) {
                warning_msg.getChildren().clear();
                Text message = new Text("Error: Entered server is offline. Please enter a reachable server.");
                message.setFill(Color.RED);
                message.setStyle("-fx-font: 14 System;");
                warning_msg.getChildren().addAll(message);
                return;
            }
        }

        if (!input_nickname.getText().isEmpty() && !input_server.getText().isEmpty()) {
            System.out.println("Username from input: " + input_nickname.getText() + ", server: " + input_server.getText() + ", port: " + port);
            Client client = new Client();
            myClient = client;
            client.setConnection(input_server.getText(), Integer.parseInt(port));
            client.setUsername(input_nickname.getText());
            changeScene("chatwindow.fxml");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setNode(simpleLogo);
        fadeTransition.setDuration(Duration.millis(20000));
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }
}
