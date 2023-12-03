import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.*;
import java.io.*;

public class Client {
    private Socket clienSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String serverIP;
    private int serverPort;
    
    private User currentUser;
    private Vector<User> directory;
    private Vector<ChatRoom> rooms;

    public static void main(String args[]) {
        Socket s;
        try {
            s = new Socket("localhost", 1235);
            ClientFake client = new ClientFake(s);
            LoginUI loginScreen = new LoginUI(client);
            loginScreen.display();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Client(Socket s) {
        try{
            this.clienSocket = s;
            this.output = new ObjectOutputStream(s.getOutputStream());
            this.input = new ObjectInputStream(s.getInputStream());

            directory = new Vector<User>();
            rooms = new Vector<ChatRoom>();

        }catch (IOException e) {
            e.printStackTrace();
        } 
    }
		

    public boolean login(String username, String password) {
        try {
            String auth = String.format("%s;;;%s", username, password);
            output.writeObject(auth);
            output.flush();

            System.out.println("Waiting on Server...");
            Boolean confirmation = (Boolean) input.readBoolean();

            if (confirmation) {
                System.out.println(username.compareTo(username) == 0);
                System.out.println(password.compareTo(password) == 0);
                System.out.println("SUCCESS: logged in");
                this.currentUser = (User) this.input.readObject();

                //get the directory
                this.directory = (Vector<User>) input.readObject();

                //get the rooms
                output.writeObject(username);
                output.flush();
                this.rooms = (Vector<ChatRoom>) input.readObject();
                
                return true;
            }
            System.out.println("FAILED: logged in");
            return false;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        } 
        
    }
    
    public Vector<User> getDirectory(){
        return directory;
    }
    
    public Vector<ChatRoom> getChatRooms(){
        return rooms;
    }
    
    public User getCurrentUser() {
        return currentUser;
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

    public Vector<ChatRoom> getRooms() {
        return rooms;
    }
    
    // Close the connection
    public void close() {
        try {
            if (this.clienSocket != null || input != null || output != null)
                this.clienSocket.close();
                System.out.println("CLOSING: client");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
