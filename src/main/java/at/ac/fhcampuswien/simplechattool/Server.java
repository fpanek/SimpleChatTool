package at.ac.fhcampuswien.simplechattool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Server {

    public static void main(String[] args){

        final ServerSocket serverSocket;
        final Socket clientSocket;
        final BufferedReader in;
        final PrintWriter out;
        final Scanner sc = new Scanner(System.in);

        try {
            serverSocket = new ServerSocket(60000);
            System.out.println("**********************************************");
            System.out.println("WELCOME TO THE SIMPLE CHAT TOOL - SERVER SIDE");
            System.out.println("**********************************************");
            System.out.println("Server is waiting for clients...");
            clientSocket = serverSocket.accept();
            System.out.println("A new Client has been connected!");
            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader (new InputStreamReader(clientSocket.getInputStream()));

            Thread sender = new Thread(new Runnable() {
                String msg;
                SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm");

                @Override
                public void run() {
                    while (true) {
                        msg = sc.nextLine();
                        System.out.println("[Server, " + formatter.format(new Date()) + "]: "  + msg);
                        out.println(msg);    // write data stored in msg in the clientSocket
                        out.flush();   // forces the sending of the data
                    }
                }
            });
            sender.start();

            Thread receive = new Thread(new Runnable() {
                String msg;
                SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm");

                @Override
                public void run() {
                    try {
                        msg = in.readLine();
                        while (msg != null) {
                            System.out.println(msg);
                            msg = in.readLine();
                        }

                        System.out.println("Client disconnected.");
                        out.close();
                        clientSocket.close();
                        serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            receive.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}