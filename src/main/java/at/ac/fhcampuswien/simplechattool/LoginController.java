package at.ac.fhcampuswien.simplechattool;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

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
    private Label warning_msg;

    private String server;
    private int port;
    private String nickname;
    private static Stage stg;

    public LoginController() throws Exception {
        Stage primaryStage = new Stage();
        start(primaryStage);
    }

    public void start(Stage primaryStage) throws Exception {
        stg = primaryStage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("loginwindow.fxml")));
        primaryStage.setTitle("Simple Chat Tool");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public String getIP() {
        return server;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return nickname;
    }

    public void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
        stg.getScene().setRoot(pane);
    }

    @FXML
    public void userLogin(ActionEvent event) throws IOException {
        checkLogin();
    }

    @FXML
    public void checkLogin() throws IOException {
        if (input_server.getText().isEmpty() || input_port.getText().isEmpty() || input_nickname.getText().isEmpty()) {
            warning_msg.setText("Error: Please enter your data!");
        } else if (!input_nickname.getText().isEmpty()) {
            this.nickname = input_nickname.getText();
            this.server = input_server.getText();
            this.port = Integer.parseInt(input_port.getText());
            changeScene("basic-chat.fxml");
        }
    }

    public static void main(String[] args) {
    }
}
