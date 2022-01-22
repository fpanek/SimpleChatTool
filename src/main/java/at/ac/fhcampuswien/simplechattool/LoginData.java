package at.ac.fhcampuswien.simplechattool;

public class LoginData {
    private String ServerIP;
    private int ServerPort = 5056;
    private String Username;
    private static LoginData logindata = new LoginData();

    private LoginData(){
    }

    public static LoginData getLogindata() {
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

    public void setLogindata(LoginData logindata) {
        this.logindata = logindata;
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
