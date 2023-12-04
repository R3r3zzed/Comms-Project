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
            Client client = new Client(s);
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

    public void updateMessage(Message m){
        //update chatroom 
        String chatroomID = m.getChatroomID();
        updateChatRoom(chatroomID, m);

        //send to the server
        sendMessageToServer(m);
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

    // Sends a message to the server
    public void sendMessageToServer(Message m) {
        try {
            this.output.writeObject(m);
            this.output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateDirectory(Vector<User> updatedDirectory) {
        this.directory = updatedDirectory;
    }
    
    public void updateChatRoom(String chatroomID, Message m) {
        //find the chatroom in rooms object
		//figure out which chatroom this message belongs to
		Iterator<ChatRoom> iterate = rooms.iterator();
		ChatRoom foundChatroom = null;
        while(iterate.hasNext()) {
			ChatRoom current = iterate.next();
            if (chatroomID.matches(current.getChatID() )) {
				foundChatroom = current;
				break;
			}
        }
		//update it if it exist
		if (foundChatroom != null){
			int index = rooms.indexOf(foundChatroom);
			ChatRoom c = rooms.get(index);
			c.addMessage(m);
			rooms.set(index, c);
		}
    }

    // public void createChatRoom(String chatroomID){
    //     Vector<User> chatroomUsers = new Vector<User>();
    //     String[] users = chatroomID.split("-");
    //     for (int i = 0; i < users.length; i++){
    //         directory
    //         User u = 
    //         chatroomUsers.add(getUser(users[i]));
    //     }
    //     ChatRoom newChatroom = new ChatRoom(chatroomID, chatroomUsers, new Vector<Message>(), this.logPathString+"/"+chatroomID+".log" );
    //     newChatroom.addMessage(m);
    //     rooms.add(newChatroom);
    // }

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
