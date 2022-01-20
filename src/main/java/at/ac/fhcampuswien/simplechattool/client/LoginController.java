package at.ac.fhcampuswien.simplechattool.client;

import at.ac.fhcampuswien.simplechattool.client.Client;
import at.ac.fhcampuswien.simplechattool.client.LoginData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

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
    public LoginController() {
        Platform.runLater(()->{
        btn_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    userLogin();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        input_nickname.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER)  {
                    try {
                        userLogin();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        });
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
    public void userLogin() throws IOException {
        System.out.println("Button login pressed");
        checkLogin();
    }

    @FXML
    public void checkLogin() throws IOException {
        if (input_server.getText().isEmpty() || input_port.getText().isEmpty() || input_nickname.getText().isEmpty()) {
            warning_msg.setText("Error: Please enter your data!");

        } else if (!input_nickname.getText().isEmpty()) {

            server = input_server.getText();
            port = Integer.parseInt(input_port.getText());
            System.out.println("Username from input " + input_nickname.getText() + "method: " );

            logindata = LoginData.getLogindata();
            logindata.setUsername(input_nickname.getText());
            logindata.setServerIP(input_server.getText());
            logindata.setServerPort(Integer.parseInt(input_port.getText()));

            System.out.println(server + " " + port);
            changeScene("basic_chat_v2.fxml");
        }
    }
}
