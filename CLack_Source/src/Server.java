import java.io.*;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Vector;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

public class Server {
	private Vector<User> listUsers;
	private Vector<ChatRoom> chatrooms;
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
  		loadInUsers("/Users/orion/Desktop/School/Fall_2023/Software_engineering/Comms-Project/CLack_Source/src/directory.txt"); // loads all companies Users into the server.Users array
//  		loadInChatRooms("/Users/orion/Desktop/School/Fall_2023/Software_engineering/Comms-Project/CLack_Source/src/logs"); // loads all Chat rooms for all users into the server.chatrooms array	
  	}
  	public Server(String testDirectory, String testLogDirectory) { 		
  		this.terminalLogger = System.getLogger("Test Server");
  		this.terminalLogger.log(Level.INFO, "Test Server is starting");	
		loadInUsers(testDirectory); // loads all companies Users into the server.Users array
		loadInChatRooms(testLogDirectory); // loads all Chat rooms for all users into the server.chatrooms array	
	}
  	
	private void setListUsers(Vector<User> listUsers) {
		this.listUsers = listUsers;
	}

	public Vector<User> getListUsers() {
		return listUsers;
	}

	public void loadInUsers(String directoryFileName) {
		Vector<User> users = new Vector<User>();
		//grab users from file then set them as users
		try {
			File fileObj = new File(directoryFileName);
	        Scanner reader = new Scanner(fileObj);
	        
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
	        	users.add(newUser);
	        }
	        reader.close();
			setListUsers(users);
		} 
		catch (Exception e) {
			System.out.println(e);
		}
	}

	private void setChatrooms(Vector<ChatRoom> chatrooms) {
		this.chatrooms = chatrooms;
	}

	public Vector<ChatRoom> getChatrooms() {
		return chatrooms;
	}
	
	public Vector<ChatRoom> getUserChatrooms(String username) {
		Vector<ChatRoom> userChatroom = new Vector<ChatRoom>();
	    Iterator<ChatRoom> iterate = chatrooms.iterator();
        while(iterate.hasNext()) {
			ChatRoom current = iterate.next();
			Iterator<String> userIterate = current.getUsers().iterator();
			while(userIterate.hasNext()) {
				String currentUser = userIterate.next();
				if (username.matches(currentUser)) {
					userChatroom.add(current);
					break;
				}
			}
		}
		return userChatroom;
	}

	public void loadInChatRooms(String logDirectory) {
		File folder = new File(logDirectory);
		File[] listOfFiles = folder.listFiles();
		Vector<ChatRoom> chatrooms = new Vector<ChatRoom>();
		if (listOfFiles == null) {
			// add logging to show that logs were empty
			return;
		}
		
		for (int i = 0; i < listOfFiles.length; i++) {		
			String chatroomFilename = listOfFiles[i].getName();
			try {
				File fileObj = new File(chatroomFilename);

				String[] users = chatroomFilename.replace(".log","").split("-");
				Vector<String> vectorUsers = new Vector<String>(); 
				Collections.addAll(vectorUsers, users);
				Vector<Message> tmpMessageVec= new Vector<Message>();
				Scanner reader = new Scanner(fileObj);
				String chatroomID = null;
				while (reader.hasNextLine()) {                
					String messageInfo = reader.nextLine();
					//sentBy;;;dateSent;;;chatroomUID;;;msgStatus;;;msgType;;;content
					String[] messageArray = messageInfo.split(";;;");
					DateFormat formatter = new SimpleDateFormat("d-MMM-yyyy,HH:mm:ss aaa");
					Date date = formatter.parse(messageArray[1]);
					chatroomID = messageArray[2];
					tmpMessageVec.add(new Message(messageArray[0], date, messageArray[2], messageArray[3], msgType.TEXT, messageArray[5]));
				}
				reader.close();
				chatrooms.add(new ChatRoom(chatroomID, vectorUsers, tmpMessageVec, chatroomFilename));

			} catch (Exception e) {
				System.out.println(e);
			}
		}
		setChatrooms(chatrooms);
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
        Iterator<User> iterate = listUsers.iterator(); //first user
        while(iterate.hasNext()) {
			User current = iterate.next();
            if (user.matches(current.getUsername()) && pass.matches(current.getPassword())) {
				return true;
			}
        }
		return false;	
	}

	public void setTerminalLogger(Logger terminalLogger) {
		this.terminalLogger = terminalLogger;
	}
	
	public Logger getTerminalLogger() {
		return terminalLogger;
	}

	public void updateNewMessage(Message m){
		//figure out which chatroom this message belongs to
		String chatroomID = m.getChatroomID();
		Iterator<ChatRoom> iterate = chatrooms.iterator();
		ChatRoom foundChatroom = null;
        while(iterate.hasNext()) {
			ChatRoom current = iterate.next();
            if (chatroomID.matches(current.getChatID() )) {
				foundChatroom = current;
			}
        }
		//update it
		Vector<String> Users = null;
		if (foundChatroom != null){
			int index = chatrooms.indexOf(foundChatroom);
			ChatRoom c = chatrooms.get(index);
			c.addMessage(m);
			chatrooms.set(index, c);
			Users = c.getUsers();
		}
		//figure out which users are online
		Iterator<User> iterateUsers = listUsers.iterator();

        while(iterateUsers.hasNext()) {
			User current = iterateUsers.next();
			String username = current.getUsername();
			int indexUser = Users.indexOf(username);
            if ( indexUser != -1 ) { // if one of the users in the chatroom
				if (current.getUserStatus() == UserStatus.ONLINE){ // and if online
					//send each the message
					
				}
			}
        }
	}
	
	public void updateUserStatus(String user) {
        Iterator<User> iterate = listUsers.iterator();
		User foundUser = null;
        while(iterate.hasNext()) {
			User current = iterate.next();
            if (user.matches(current.getUsername())) {
				foundUser = current;
			}
        }
		if (foundUser != null){
			int index = listUsers.indexOf(foundUser);

			if (foundUser.getUserStatus() == UserStatus.OFFLINE){
				foundUser.changeStatus(UserStatus.ONLINE);
				listUsers.set(index, foundUser);
			}
			else if (foundUser.getUserStatus() == UserStatus.ONLINE){
				foundUser.changeStatus(UserStatus.OFFLINE);
				listUsers.set(index, foundUser);
			}
		}
	}
    
}
