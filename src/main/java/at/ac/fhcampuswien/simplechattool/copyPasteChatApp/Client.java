package at.ac.fhcampuswien.simplechattool.copyPasteChatApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


public class Client {

    public static void main(String[] args){
        final Socket clientSocket;
        final BufferedReader in;
        final PrintWriter out;
        final Scanner sc = new Scanner(System.in);


        try {

            System.out.print("Enter the Server(Host name or IP) : " );
            String server = sc.nextLine();

            System.out.print("Enter the port : " );
            String port = sc.nextLine();

            clientSocket = new Socket(server, Integer.parseInt(port));

            System.out.print("Enter your username : ");
            String username = sc.nextLine();

            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            System.out.println("You are now online");

            Thread sender = new Thread(new Runnable() {
                String msg;
                SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

                public void run() {
                    while(true){
                        msg = sc.nextLine();
                        System.out.println(username + " " + formatter.format(new Date()) + " : "  + msg );
                        out.println(username + " " + formatter.format(new Date()) + " : "  + msg );
                        out.flush();
                    }
                }
            });

            sender.start();
            Thread receiver = new Thread(new Runnable()
            {
                String msg;
                SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

                public void run() {
                    try {
                        msg = in.readLine();
                        while(msg!=null)
                            {
                            System.out.println("Server " + formatter.format(new Date()) + " : " + msg);
                            msg = in.readLine();
                            }

                        System.out.println("Server out of service");
                        out.close();
                        clientSocket.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            receiver.start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
