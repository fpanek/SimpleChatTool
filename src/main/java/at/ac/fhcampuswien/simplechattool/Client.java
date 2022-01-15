package at.ac.fhcampuswien.simplechattool;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/*
Sources:
https://github.com/dvcarrillo/sockets-chat
https://github.com/ashmeet4293/Chat-Application-in-java-using-javafx
https://luisgcenci.medium.com/building-a-group-chat-app-with-javafx-multithread-socket-programming-in-java-c8c11fd8c927
https://www.hubberspot.com/2012/08/creating-simple-chat-clientserver.html
*/

public class Client {
    public static void main(String[] args){
        final Socket clientSocket;
        final BufferedReader in;
        final PrintWriter out;
        final Scanner sc = new Scanner(System.in);

        try {
            System.out.println("**********************************************");
            System.out.println("WELCOME TO THE SIMPLE CHAT TOOL - CLIENT SIDE");
            System.out.println("**********************************************");
            System.out.print("Enter the desired server (host name or IP): ");
            String server = sc.nextLine();

            boolean success = false;
            while (!success) {
                try {
                    InetAddress host = InetAddress.getByName(server);
                    System.out.println("Server " + server + " is online.");
                    success = true;
                } catch (UnknownHostException e) {
                    System.out.println("Server is offline, please provide a correct server!");
                    server = sc.nextLine();
                }
            }

            System.out.print("Enter the port: ");
            String port = sc.nextLine();

            while(Integer.parseInt(port) < 0 || Integer.parseInt(port) > 65535) {
                System.out.print("Enter the right port number: ");
                port = sc.nextLine();
            }

            clientSocket = new Socket(server, Integer.parseInt(port));
            System.out.print("Enter your username: ");
            String username = sc.nextLine();

            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            System.out.println("You are now online!");

            Thread sender = new Thread(new Runnable() {
                String msg;
                SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm");

                public void run() {
                    while (true) {
                        msg = sc.nextLine();
                        if (msg.isEmpty()) {
                            System.out.println("Please enter a non-empty message!");
                        } else if (msg.equals("exit") || msg.equals("Exit")) {
                            out.close();
                            System.out.println("Disconnected from Server!");
                            try {
                                clientSocket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                        else {
                            System.out.println("[" + username + ", " + formatter.format(new Date()) + "]: "  + msg);
                            out.println("[" + username + ", " + formatter.format(new Date()) + "]: "  + msg);
                            out.flush();
                        }
                    }
                }
            });
            sender.start();
            Thread receiver = new Thread(new Runnable() {
                String msg;
                SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm");

                public void run() {
                    try {
                        msg = in.readLine();
                        while (msg != null) {
                            System.out.println("[Server, " + formatter.format(new Date()) + "]: " + msg);
                            msg = in.readLine();
                        }
                        System.out.println("Server out of service");
                        out.close();
                        clientSocket.close();

                    } catch (UnknownHostException e) {
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                        //e.printStackTrace();
                    }
                }
            });
            receiver.start();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}