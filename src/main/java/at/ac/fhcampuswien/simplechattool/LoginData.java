package at.ac.fhcampuswien.simplechattool;

public class LoginData {
    private String ServerIP;
    private int ServerPort = 5056;
    private String Username;
    private static final LoginData logindata = new LoginData();

    private LoginData(){
    }

    public static LoginData getLoginData() {
        return logindata;
    }

    public void setServerIP(String serverIP) {
        ServerIP = serverIP;
    }

    public void setServerPort(int serverPort) {
        ServerPort = serverPort;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getServerIP() {
        return ServerIP;
    }

    public int getServerPort() {
        return ServerPort;
    }

    public String getUsername() {
        return Username;
    }
}
