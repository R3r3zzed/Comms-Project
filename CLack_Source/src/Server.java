import java.io.*;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.net.*;
import java.util.Scanner;

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
  		loadInUsers(); // loads all comapanies Users into the server.Users array
  		loadInChatRooms(); // loads all Chatrooms for all users into the server.chatrooms array	
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
	
	private void loadInUsers() {
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
	
	private void loadInChatRooms() {
		File folder = new File("log");
		File[] listOfFiles = folder.listFiles();
		int currentNumberChatrooms = listOfFiles.length;

		ChatRoom[] chatrooms = new ChatRoom[currentNumberChatrooms];

		for (int i = 0; i < listOfFiles.length; i++) {		
			String chatroomName = listOfFiles[i].getName();

			try {
				File fileObj = new File(chatroomName);
				Scanner counter = new Scanner(fileObj);

				int messageCounter = 0;
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

					// Message newUser = new Message(messageArray[0], messageArray[1], messageArray[2], messageArray[3], messageArray[4]);
					// users[pos] = newUser;
					pos++;
				}
				reader.close();
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
		return false;
	}

	private void sendNewMsgUsers() {
		
	}
	
	private void createHandler() {
		
	}
	
	public void updateChatRoom() {
		
	}
    
}
