package at.ac.fhcampuswien.simplechattool.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

public class basicChatController {
    @FXML
    private Label welcomeText;

    @FXML
    private TextField chatWindow;
    @FXML
    private ListView chatPartner;



    @FXML
    protected void onClickSend() {
        Long date;
        date = System.currentTimeMillis();
        //chatWindow.setText(date.toString());
        //chatWindow.setText(Inet4Address.getLocalHost().getHostAddress(););
        //chatWindow.setText(Inet4Address.getLocalHost().getHostAddress());
        chatPartner.getItems().add(date.toString());
    }
}