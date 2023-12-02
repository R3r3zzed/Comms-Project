import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Vector;
import java.util.*;
import java.io.*;

public class Client {
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String serverIP;
    private int serverPort;
    
    private User currentUser;
    private Vector<User> directory;
    private Vector<ChatRoom> rooms;

    public Client(String serverIP, int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.directory = new Vector<>();
        this.rooms = new Vector<>();
        connect();
    }
    
    // Establishes connection with the server
    public boolean connect() {
        try {
            this.socket = new Socket(this.serverIP, this.serverPort);
            this.output = new ObjectOutputStream(socket.getOutputStream());
            this.input = new ObjectInputStream(socket.getInputStream());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Attempts to log in to the server
    public boolean login(String username, String password) {
        try {
            this.output.writeObject(username + ";;;" + password);
            this.output.flush();
            if (this.input.readObject() instanceof User)
                this.currentUser = (User) this.input.readObject();                
            return this.input.readObject() instanceof User;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Requests chat logs from the server
    public void requestLogs(String username) {
        try {
            this.output.writeObject("LOG_REQUEST: " + username);
            this.output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Opens a chat room
    public void openChatRoom(String id){
        try {
            this.output.writeObject("OPEN_CHAT: " + id);
            this.output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Loads messages from a chat room
    public void loadChatRoom(String id) {
        try {
            this.output.writeObject("LOAD_CHAT: " + id);
            this.output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Sends a message to the server
    public void sendMessageToServer(String message) {
        try {
            this.output.writeObject("MESSAGE: " + message);
            this.output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void updateDirectory(Vector<User> updatedDirectory) {
        this.directory = updatedDirectory;
    }
    
    public void updateChatRoom(String chatRoomId) {
        try {
            this.output.writeObject("UPDATE_CHATROOM: " + chatRoomId);
            this.output.flush();
            
            if (this.input.readObject() instanceof ChatRoom) {
                ChatRoom updatedRoom = (ChatRoom) this.input.readObject();
                
                for (int i = 0; i < this.rooms.size(); i++) {
                    if (this.rooms.get(i).getChatID().equals(updatedRoom.getChatID())) {
                        this.rooms.set(i, updatedRoom);
                        System.out.println("Chat room updated: " + updatedRoom.getChatID());
                        return;
                    }
                }
                this.rooms.add(updatedRoom);
            } else {
                System.out.println("ERROR: Invalid instance");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    
    public void addRoom(ChatRoom room) {
    	if(room == null)
    		this.rooms.add(room);
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public Vector<User> getDirectory() {
        return directory;
    }

    public Vector<ChatRoom> getRooms() {
        return rooms;
    }
    
    // Close the connection
    public void close() {
        try {
            if (socket != null || input != null || output != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client("134.154.20.147", 1234);
        client.connect();
        
        LoginUI loginScreen = new LoginUI();
        loginScreen.display(client);

        while (!loginScreen.isLoggedIn()){/* Wait for successful login */}
        
        do {
        	MainUI mainUI = new MainUI();
        	mainUI.display(client);
        }while(loginScreen.isLoggedIn());
    }
}
