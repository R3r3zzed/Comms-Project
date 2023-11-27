import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String serverIP;
    private int serverPort;

    public Client(String serverIP, int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    // Establishes connection with the server
    public boolean connect() {
        try {
            this.socket = new Socket(this.serverIP, this.serverPort);
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Attempts to log in to the server
    public boolean login(String username, String password) {
        try {
            this.out.writeObject(username + ": " + password);
            this.out.flush();

            // Await response from the server
            String response = (String) this.in.readObject();
            return Boolean.parseBoolean(response);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Requests chat logs from the server
    public void requestLogs(String username) {
        try {
            this.out.writeObject("LOG_REQUEST: " + username);
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Opens a chat room
    public void openChatRoom(String id) {
        try {
            this.out.writeObject("OPEN_CHAT: " + id);
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Loads messages from a chat room
    public void loadChatRoom(String id) {
        try {
            this.out.writeObject("LOAD_CHAT: " + id);
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Sends a message to the server
    public void sendMessageToServer(String message) {
        try {
            this.out.writeObject("MESSAGE: " + message);
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Close the connection
    public void close() {
        try {
            if (socket != null) 
                socket.close();
            if (in != null) 
                in.close();
            if (out != null)
                out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 1234); // Example IP and port

        LoginUI loginScreen = new LoginUI();
        loginScreen.display(client);

        while (!loginScreen.isLoggedIn()) {
            // Wait for successful login
        }

        MainUI mainUI = new MainUI();
        mainUI.display(client);
    }
}
