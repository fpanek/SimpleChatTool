package at.ac.fhcampuswien.simplechattool.copyPasteChatApp;

import java.net.Inet4Address;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.net.InetAddress;
public class getOwnIP {
    public static void main(String[] args) {
        try {
            InetAddress ia = InetAddress.getLocalHost();
            String str = ia.getHostAddress();
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
