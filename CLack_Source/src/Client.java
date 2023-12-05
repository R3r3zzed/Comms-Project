import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
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
    private MainUI mainUI;
    private Thread messageListenerThread;
    private Thread clientUpdaterThread;
    private ConcurrentLinkedQueue<Message> queueMessages;
    private ConcurrentLinkedQueue<Vector<ChatRoom>> queueLogs;


    public static void main(String args[]) throws UnknownHostException {
    	System.out.println(System.getProperty("user.dir"));
    	Socket s = new Socket();
        String ip = "0.0.0.0";
        int port = 42; 
        BufferedReader filebr;
        BufferedReader systemInbr = new BufferedReader( new InputStreamReader(System.in));
        while(true){
        try {
        	try { 
                filebr = new BufferedReader(new FileReader("./ipFile.txt"));
                ip = filebr.readLine();
                port = Integer.parseInt(filebr.readLine());
                filebr.close();
            } catch (IOException | NumberFormatException e) {
                System.out.println("Error with file");
            }
            s.connect(new InetSocketAddress(ip, port), 5000);
            s.setSoTimeout(0);
            OutputStream outputStream = s.getOutputStream();
            Client client = new Client(s);
            systemInbr.close();
            LoginUI loginScreen = new LoginUI(client);
            loginScreen.display();
            break;
        } catch (IOException e) {
            try {
                System.out.println("Oops! Default IP and Port Didn't work...Please provide new ones.");
                System.out.print("IP: ");
                ip = systemInbr.readLine();
                System.out.print("Port: ");
                port = Integer.parseInt(systemInbr.readLine());
                FileWriter myWriter = new FileWriter("./ipFile.txt");
                myWriter.write(ip+"\n");
                myWriter.write(port+"\n");
                myWriter.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
    }
    }

    public Client(Socket s) {
        try{
            this.clienSocket = s;
            this.output = new ObjectOutputStream(s.getOutputStream());
            this.input = new ObjectInputStream(s.getInputStream());

            directory = new Vector<User>();
            rooms = new Vector<ChatRoom>();
            queueMessages = new ConcurrentLinkedQueue<Message>();
            queueLogs = new ConcurrentLinkedQueue<Vector<ChatRoom>>();

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
        updateUI();
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

    public Vector<ChatRoom> getLogs(String username){
        Vector<ChatRoom> userChatRooms = null;
        try {
            output.writeObject(username);
            output.flush();
            while(queueLogs.peek() == null) {
            	// waiting for the message from the server
            }
            userChatRooms = queueLogs.poll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return userChatRooms;
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
    
    // Try to open the chatRoom that has similar id to CHATID
    public ChatRoom openChatRoom(String chatID) {
    	boolean isFound;	// becomes true if the room with similar ChatID is found
    	System.out.println(chatID);
    	for(int i = 0; i < rooms.size(); i++) {
    		isFound = true;
    		ChatRoom currentRoom = rooms.get(i);
    		String currentRoomID = currentRoom.getChatID();
    		StringTokenizer tokenizer = new StringTokenizer(currentRoomID);
    		while(tokenizer.hasMoreTokens()) {
    			String userID = tokenizer.nextToken();
    			if(!chatID.contains(userID)) {
    				isFound = false;
    				break;
    			}
    		}
    		if (isFound == true) {
    			return currentRoom;
    		}
    	}
    	Vector<User> participants = new Vector<User>();
    	for(int i = 0; i < directory.size(); i++) {
    		if(chatID.contains(directory.get(i).getUserID())) {
    			participants.add(directory.get(i));
    		}
    	}
    	
    	// generate filename
    	String filename = "";
    	for(int i = 0; i < participants.size(); i++) {
    		filename += participants.get(i).getUsername();
    		if(i != participants.size() - 1) {
    			filename += "-";
    		}
    	}
    	filename += ".log";
    	ChatRoom newChatRoom = new ChatRoom(chatID, participants, new Vector<Message>(), filename);
    	rooms.add(newChatRoom);
    	updateUI();
    	
    	return newChatRoom;
    }
    
    public void setMainUI(MainUI mainUI) {
    	this.mainUI = mainUI;
    }
    
    public void updateUI() {
    	mainUI.updateChatScrollPane();
    	if(mainUI.getChatUI() == null) {
    		return;
    	}
    	mainUI.getChatUI().updateMessagesScrollPane();
    }
    
    private void pushMessageQueue(Message message) {
    	queueMessages.add(message);
    }
    
    private void pushLogsQueue(Vector<ChatRoom> logs) {
    	queueLogs.add(logs);
    }
    
    // Close the connection
    public void close() {
        try {
            if (this.clienSocket != null || input != null || output != null)
                this.clienSocket.close();
                System.out.println("CLOSING: client");
                messageListenerThread.interrupt();
                clientUpdaterThread.interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void startObserverThreads() {
    	MessageListener messageListener = new MessageListener(this);
    	messageListenerThread = new Thread(messageListener);
    	messageListenerThread.start();
    	ClientUpdater clientUpdater = new ClientUpdater(this);
    	clientUpdaterThread = new Thread(clientUpdater);
    	clientUpdaterThread.start();
    }
    
    private static class ClientUpdater implements Runnable{
    	private final Client client;
    	
    	public ClientUpdater(Client client) {
    		this.client = client;
    	}
    	
    	public void run() {
    		while(!Thread.currentThread().isInterrupted()) {
    			if(client.queueMessages.peek() != null) {
    				client.updateMessage(client.queueMessages.poll());
    			}
    		}
    	}
    }
    
    // handles listening to messages from the Server that are sent by other users
    // and does the appropriate operations
    private static class MessageListener implements Runnable{
    	private final Client client;
    	
    	public MessageListener(Client client) {
    		this.client = client;
    	}
    	
    	public void run() {
    		while(!Thread.currentThread().isInterrupted()) {
    			try{
        			// TODO remove comment
        			while(true) {
        				Object object = client.input.readObject();
        				if(object instanceof Message) {
        					client.pushMessageQueue((Message) object);
        				}
        				else {
        					client.pushLogsQueue((Vector<ChatRoom>) object);
        				}
        				
        			}
        		}
        		catch(IOException e) {
        			System.out.println("Failed receiving inputstream in Message Listener");
        			return;
        		}
        		catch(ClassNotFoundException e) {
        			System.out.println("Wrong Classs sent Message Listener");
        			return;
        		}
    		}
    	}
    }
}
