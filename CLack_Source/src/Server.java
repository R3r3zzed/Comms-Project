import java.io.*;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Date;

public class Server {
	private User[] listUsers;
	private ChatRoom[] chatrooms;
	private Thread[] threads[];
	private Logger terminalLogger;
		
  	public static void main(String[] args){
		ServerSocket server = null;
		try {			
			server = new ServerSocket(1234);
			server.setReuseAddress(true);
			while (true) {
				Socket client = server.accept();
				ServerHandler clientSock = new ServerHandler(client);
				new Thread(clientSock).start();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (server != null) {
				try {
					server.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    }
  	
  	public Server() {  		
		this.terminalLogger = System.getLogger("Server");
        this.terminalLogger.log(Level.INFO, "Server is starting");	
  		loadInUsers(); // loads all companies Users into the server.Users array
  		loadInChatRooms(); // loads all Chat rooms for all users into the server.chatrooms array	
  	}
  	
	public User[] getListUsers() {
		return listUsers;
	}

	private void setListUsers(User[] listUsers) {
		this.listUsers = listUsers;
	}

	public ChatRoom[] getChatrooms() {
		return chatrooms;
	}
	
	public ChatRoom[] getUserChatrooms(String username) {
		//TO DO add a way to get all chatrooms of give user
	
		return chatrooms;
	}

	private void setChatrooms(ChatRoom[] chatrooms) {
		this.chatrooms = chatrooms;
	}

	public Thread[][] getThreads() {
		return threads;
	}

	public void setThreads(Thread[] threads[]) {
		this.threads = threads;
	}

	public Logger getTerminalLogger() {
		return terminalLogger;
	}

	public void setTerminalLogger(Logger terminalLogger) {
		this.terminalLogger = terminalLogger;
	}
	
	public void loadInUsers() {
		User[] users;
		//grab users from file then set them as users
		try {
			File fileObj = new File("directory.txt");
			Scanner counter = new Scanner(fileObj);
			int totalUsers = 0;
	        while (counter.hasNextLine()) {                
	            counter.nextLine();
	            totalUsers++;
	        }
	        counter.close();
	        
	        users = new User[totalUsers];
	        Scanner reader = new Scanner(fileObj);
	        
	        int pos = 0;
	        while (reader.hasNextLine()) {                
	        	String userInfo = reader.nextLine();
				String[] userArray = userInfo.split(";;;");
				UserType currentUserType;
				if (userArray[3].toLowerCase().matches("it")){
					currentUserType = UserType.IT;
				}
				else{
					currentUserType = UserType.BASIC;
				}
				User newUser = new User(userArray[0], userArray[1], userArray[2], currentUserType);
	        	users[pos] = newUser;
	        	pos++;
	        }
	        reader.close();
			setListUsers(users);
		} 
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void loadInChatRooms() {
		File folder = new File("log");
		File[] listOfFiles = folder.listFiles();
		int currentNumberChatrooms = listOfFiles.length;
		ChatRoom[] chatrooms = new ChatRoom[currentNumberChatrooms];
		for (int i = 0; i < listOfFiles.length; i++) {		
			String chatroomFilename = listOfFiles[i].getName();
			try {
				File fileObj = new File(chatroomFilename);
				Scanner counter = new Scanner(fileObj); 
				String[] users = chatroomFilename.replace(".log","").split("-");
				int messageCounter = 0; //number of messages in this chat room
				while (counter.hasNextLine()) {                
					counter.nextLine();
					messageCounter++;
				}
				counter.close();
				Message[] tmpMessageArray = new Message[messageCounter];
				Scanner reader = new Scanner(fileObj);
				int pos = 0;
				while (reader.hasNextLine()) {                
					String messageInfo = reader.nextLine();
					//sentBy;;;dateSent;;;chatroomUID;;;msgStatus;;;content
					String[] messageArray = messageInfo.split(";;;");
					DateFormat formatter = new SimpleDateFormat("d-MMM-yyyy,HH:mm:ss aaa");
					Date date = formatter.parse(messageArray[1]);
					tmpMessageArray[pos] = new Message(messageArray[0], date, messageArray[2], messageArray[3], messageArray[4]);
					pos++;
				}
				reader.close();
				chatrooms[i] = new ChatRoom(tmpMessageArray[0].getChatroomID(), users, tmpMessageArray, chatroomFilename);
				// setListUsers(users);
				}
			catch (Exception e) {
				System.out.println(e);
			}
		}

		setChatrooms(chatrooms);
	}
	
	public User findUser() {
		return null;
	}
	
	public boolean isITUser(User u) {
		if (u.getUserType() == UserType.IT) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean UserAuthentication(String user, String pass) {
		for (int i = 0; i < listUsers.length; i++) {
			if (user.matches(listUsers[i].getUsername()) && pass.matches(listUsers[i].getPassword())) {
				return true;
			}
		}
		return false;		
	}

	private void sendNewMsgUsers() {
		
	}
	
	public void updateChatRoom() {
		
	}
	
	public void updateUserStatus(String user) {
		for (int i = 0; i < listUsers.length; i++) {
			if (user.matches(listUsers[i].getUsername())) {
				listUsers[i].changeStatus(UserStatus.ONLINE);
			}
		}
	}
    
}
