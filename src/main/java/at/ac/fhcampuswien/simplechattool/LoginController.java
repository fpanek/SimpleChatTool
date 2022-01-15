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
import java.util.concurrent.TimeUnit;

public class LoginController  {
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

    private static Client myClient;
    private static LoginData logindata;

/*
    public void start(Stage primaryStage) throws Exception {
        stg = primaryStage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("loginwindow.fxml")));
        primaryStage.setTitle("Simple Chat Tool");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }
*/
    public LoginData getLogindataObject(){
        return logindata;
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
        Stage stg = Client.getStage();
        Parent pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
        stg.getScene().setRoot(pane);
    }

    public static Client getMyClient() {
        return myClient;
    }

    @FXML
    public void userLogin(ActionEvent action) throws IOException {
        System.out.println("Button login pressed");
        checkLogin();
    }

    @FXML
    public void checkLogin() throws IOException {
        if (input_server.getText().isEmpty() || input_port.getText().isEmpty() || input_nickname.getText().isEmpty()) {
            warning_msg.setText("Error: Please enter your data!");
        } else if (!input_nickname.getText().isEmpty()) {
            //this.nickname = input_nickname.getText();
            server = input_server.getText();
            port = Integer.parseInt(input_port.getText());
            System.out.println("Username from ipnut " + input_nickname.getText() + "method: " );
            logindata = LoginData.getLogindata();
            logindata.setUsername(input_nickname.getText());
            logindata.setServerIP(input_server.getText());
            logindata.setServerPort(Integer.parseInt(input_port.getText()));
            //Client client = new Client();
            //client.setConnection(server, port);
            //client.setUsername(input_nickname.getText());
            System.out.println(server + " " + port);

            //client.setUsername(input_nickname.getText());
            //try {
            //    TimeUnit.SECONDS.sleep(1);
            //} catch (InterruptedException ie) {
            //    Thread.currentThread().interrupt();
            //}
            //client.sendMessage("Automated Test Message");
            changeScene("basic-chat.fxml");
            //myClient = client;
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }


}
