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
import java.util.concurrent.*; 

public class Server {
	private String logPathString;
	private Vector<User> listUsers;
	private Vector<ChatRoom> chatrooms;
	private ConcurrentHashMap<String, ConcurrentLinkedQueue<Message>> newMsg;
	private Logger terminalLogger;
		
  	public static void main(String[] args){
		ServerSocket server = null;
		try {	
			Logger terminalLogger;	
			terminalLogger = System.getLogger("Server");
			terminalLogger.log(Level.INFO, "Server is starting");
			server = new ServerSocket(1235);
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
		loadInUsers("./directory.txt"); // loads all companies Users into the server.Users array
		loadInChatRooms("./logs"); // loads all Chat rooms for all users into the server.chatrooms array
		newMsg = new ConcurrentHashMap<String, ConcurrentLinkedQueue<Message>>();
  	}
  	public Server(String testDirectory, String testLogDirectory) { 		
		loadInUsers(testDirectory); // loads all companies Users into the server.Users array
		loadInChatRooms(testLogDirectory); // loads all Chat rooms for all users into the server.chatrooms array	
	}
  	
	private void setListUsers(Vector<User> listUsers) {
		this.listUsers = listUsers;
	}

	public Vector<User> getListUsers() {
		return listUsers;
	}

	private void setChatrooms(Vector<ChatRoom> chatrooms) {
		this.chatrooms = chatrooms;
	}

	public Vector<ChatRoom> getChatrooms() {
		return chatrooms;
	}

	public void loadInUsers(String directoryFileName) {
		setListUsers(null);
		Vector<User> users = new Vector<User>();
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
	
	public Vector<ChatRoom> getUserChatrooms(String username) {
		Vector<ChatRoom> userChatroom = new Vector<ChatRoom>();
	    Iterator<ChatRoom> iterate = chatrooms.iterator();
        while(iterate.hasNext()) {
			ChatRoom current = iterate.next();
			Iterator<User> userIterate = current.getUsers().iterator();
			while(userIterate.hasNext()) {
				User currentUser = userIterate.next();
				if (username.matches(currentUser.getUsername())) {
					userChatroom.add(current);
					break;
				}
			}
		}
		return userChatroom;
	}

	public void loadInChatRooms(String logDirectory) {
		this.logPathString = logDirectory;
		setChatrooms(null);
		File folder = new File(logDirectory);
		File[] listOfFiles = folder.listFiles();
		Vector<ChatRoom> chatrooms = new Vector<ChatRoom>();
		if (listOfFiles == null) {
			// add logging to show that logs were empty
			System.out.println("Unable to see log file in dir: " + logDirectory);
			return;
		}
		for (int i = 0; i < listOfFiles.length; i++) {		
			String chatroomFilename = logDirectory + "/"  + listOfFiles[i].getName();
			try {
				File fileObj = new File(chatroomFilename);
				String[] fileSplit = chatroomFilename.replace(".log","").split("/");
				String chatroomID = fileSplit[fileSplit.length-1];
				String[] userString = chatroomID.split("-");
				Vector<User> vectorUsers = new Vector<User>(); 
				// Get the server version of the User from the username (String)
				for (int j = 0; j < userString.length; j++){
					Iterator<User> iterate = listUsers.iterator();
					while(iterate.hasNext()) {
						User current = iterate.next();
						if (current.getUsername().matches(userString[j])) {
							vectorUsers.add(current);
							break;
						}
					}
				}
				chatrooms.add(new ChatRoom(chatroomID, vectorUsers, chatroomFilename));

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

	public User getUser(String user) {
        Iterator<User> iterate = listUsers.iterator(); //first user
        while(iterate.hasNext()) {
			User current = iterate.next();
            if (user.matches(current.getUsername())) {
				return current;
			}
        }
		return null;	
	}

	public void setTerminalLogger(Logger terminalLogger) {
		this.terminalLogger = terminalLogger;
	}
	
	public Logger getTerminalLogger() {
		return terminalLogger;
	}

	public void updateNewMessage(Message m){
		String chatroomID = m.getChatroomID();

		//figure out which chatroom this message belongs to
		Iterator<ChatRoom> iterate = chatrooms.iterator();
		ChatRoom foundChatroom = null;
        while(iterate.hasNext()) {
			ChatRoom current = iterate.next();
            if (chatroomID.matches(current.getChatID() )) {
				foundChatroom = current;
				break;
			}
        }
		//update it if it exist
		Vector<User> chatroomUsers = null;
		if (foundChatroom != null){
			int index = chatrooms.indexOf(foundChatroom);
			ChatRoom c = chatrooms.get(index);
			c.addMessage(m);
			c.writeToFile(m);
			chatrooms.set(index, c);
			chatroomUsers = c.getUsers();
		}
		//make a new chatroom
		else{
			chatroomUsers = new Vector<User>();
			String[] users = chatroomID.split("-");
			for (int i = 0; i < users.length; i++){
				chatroomUsers.add(getUser(users[i]));
			}
			ChatRoom newChatroom = new ChatRoom(chatroomID, chatroomUsers, new Vector<Message>(), this.logPathString+"/"+chatroomID+".log" );
			newChatroom.addMessage(m);
			newChatroom.writeToFile(m);
			chatrooms.add(newChatroom);
		}

		//figure out which users are online
		Iterator<User> iterateChatroomUsers = chatroomUsers.iterator();
        while(iterateChatroomUsers.hasNext()) {
			User currentChatroom = iterateChatroomUsers.next();
			String usernameChatroom = currentChatroom.getUsername();
			if (m.getSendBy().matches(usernameChatroom)){
				continue;
			}
			//Get the Server version of the user
			Iterator<User> iterateServerUsers = listUsers.iterator();
			while(iterateServerUsers.hasNext()) {
				User currentServer = iterateServerUsers.next();
				String usernameServer = currentServer.getUsername();
				if (usernameServer.matches(usernameChatroom) ) {
					if (currentServer.getUserStatus() == UserStatus.ONLINE){
					//send each the message
					addToNewMessageQ(usernameServer, m);
					}
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
	
	public ConcurrentHashMap<String, ConcurrentLinkedQueue<Message>> getNewMsgQ(){
		return this.newMsg;
	}

	public void setUpUserQ(String User){
		ConcurrentLinkedQueue<Message> newQ = new ConcurrentLinkedQueue<Message>();
		newMsg.put(User, newQ);
	}

	public void addToNewMessageQ(String User, Message m) {
		ConcurrentLinkedQueue<Message> newMessages = newMsg.get(User);
		newMsg.remove(User, newMessages);
		newMessages.add(m);
		newMsg.put(User, newMessages);
	}
	
	public Vector<Message> grabFromNewMessageQ(String User) {
		//gets the messages from the queue
		Vector<Message> vectorMessage= new Vector<Message>();
		ConcurrentLinkedQueue<Message> newMessages = newMsg.get(User);
		try{
			if (newMessages.isEmpty()){
				return null;
			}

			while (! newMessages.isEmpty()){
				vectorMessage.add(newMessages.remove());
			}
			return vectorMessage;
		}
		catch (java.lang.NullPointerException e) {
			return null;
		}

	}
	
	public void revoveUserFromQ(String User) {
		ConcurrentLinkedQueue<Message> newMessages = newMsg.get(User);
		newMsg.remove(User, newMessages);
	}
    

	public static class ServerHandler implements Runnable {
		private final Socket clientSocket;
		private String ip;
		private Vector<ChatRoom> currentChatRooms;
		private ObjectInputStream objectInputStream;
		private ObjectOutputStream objectOutputStream;

		private User currentUser;
		
		public static Server ServerInfo = new Server(); //each thread made has access to the same server object
		private Logger ScreenLog;

		public ServerHandler(Socket socket){
			this.clientSocket = socket;
			if (this.clientSocket != null){
			this.ip = this.clientSocket.getInetAddress().getHostAddress();
			}
		}		
		
		public void run(){
			//create the in and out to the client 
			if (this.clientSocket == null){
				return;
			}
			try {
				OutputStream outputStream = this.clientSocket.getOutputStream();
				InputStream inputStream = this.clientSocket.getInputStream();

				//for single messages and other objects
				this.objectOutputStream = new ObjectOutputStream(outputStream);
				this.objectInputStream = new ObjectInputStream(inputStream);

				//Who is logging on
				Boolean ValidUser = false;
				System.out.println("A connection was made");

				while(! ValidUser){
					//first read from client
					String UserAndPassword = (String) objectInputStream.readObject();
					String[] userInfo = UserAndPassword.split(";;;");
					if (userInfo.length < 2){
						ValidUser = false;
					}else{
						ValidUser = ServerInfo.UserAuthentication(userInfo[0], userInfo[1]);
						this.currentUser = ServerHandler.ServerInfo.getUser(userInfo[0]);
						System.out.println(UserAndPassword);
						System.out.println(ValidUser);
					}

					if (ValidUser){
						//all the housekeeping for a newly logged in user

						//add log to server to show which user is online
						// return a true boolean to tell the user is online
						//send over Boolean
						objectOutputStream.writeBoolean(ValidUser);
						objectOutputStream.flush();

						ServerInfo.updateUserStatus(userInfo[0]);
						// Thread.sleep(40);
						//send over User
						objectOutputStream.writeObject(this.currentUser);
						objectOutputStream.flush();
						System.out.println(this.currentUser.getUsername() + " is online");

						//send directory
						objectOutputStream.writeObject(ServerHandler.ServerInfo.getListUsers());
						objectOutputStream.flush();

						//get the chatrooms of the user given
						String userGiven = (String) objectInputStream.readObject();
						this.currentChatRooms = ServerHandler.ServerInfo.getUserChatrooms(userGiven);
						objectOutputStream.writeObject(this.currentChatRooms);
						objectOutputStream.flush();

						ServerInfo.setUpUserQ(this.currentUser.getUsername());

					}
					else {
						// return a false boolean to tell the user is not valid
						objectOutputStream.writeBoolean(ValidUser);
						objectOutputStream.flush();
						System.out.println("Error with confirmation, try again...");
					}
				}

				// Both method for getting new messages and sending new messages get its own thread
				// Getting messages from server is constant in another thread but getting messages from client this main thread (need to know when to log out)
				
				receiveNewMessageClass newMessageClass = new receiveNewMessageClass(this.currentUser, objectOutputStream);
				Thread newMessageThread = new Thread(newMessageClass);
				newMessageThread.start();

				// wait for a object from client
				Object unknownObjectFromClient = objectInputStream.readObject();
				//check if client gave use a String (asking for chatrooms of that users)
				if (unknownObjectFromClient instanceof String){
					String username = (String) unknownObjectFromClient;
					this.objectOutputStream.writeObject(ServerInfo.getUserChatrooms(username));
					this.objectOutputStream.flush();
				} else if (unknownObjectFromClient instanceof Message){
					Message newMessage = (Message) unknownObjectFromClient;
					// check if message is a log out message
					if (newMessage.getType() == msgType.LOGOUT){
						newMessageThread.interrupt();
						System.out.println("Logging out");
						logOut();
						return;
					}
					System.out.println(newMessage.toString());
					//update server and other users of new message - it will send to all users excluding the user it was sent from
					ServerInfo.updateNewMessage(newMessage);
				}
				
				} catch (SocketException e){
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println("User exited without logging out");
					try{
					if (this.ServerInfo.getUser(this.currentUser.getUsername()).getUserStatus() == UserStatus.ONLINE){
						System.out.println("User is still showing online");
						this.ServerInfo.updateUserStatus(this.currentUser.getUsername());
					}
					} catch (NullPointerException n){
						System.out.println("No users were entered for this thread");
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
		}
			
		public String getIp() {
			return ip;
		}

		public Vector<ChatRoom> getCurrentChatRooms() {
			return currentChatRooms;
		}

		public void setCurrentChatRooms(Vector<ChatRoom> currentChatRooms) {
			this.currentChatRooms = currentChatRooms;
		}
		
		public void logOut() {
			ServerInfo.updateUserStatus(this.currentUser.getUsername());
			ServerInfo.revoveUserFromQ(currentUser.getUsername());
		}
	
	public static class receiveNewMessageClass implements Runnable {
		private ObjectOutputStream outTunnel;
		private User user;
		
		public receiveNewMessageClass(User u, ObjectOutputStream outObject) {
			this.outTunnel = outObject;
			this.user = u;
			// this.serverInfo = s;
		}
		
		public void run(){
			while(!Thread.interrupted()){
				if (ServerHandler.ServerInfo.getUser(this.user.getUsername()).getUserStatus() == UserStatus.OFFLINE ){
					return;
				}
				//check to see if there are new messages for User
				Vector<Message> newMessages = ServerHandler.ServerInfo.grabFromNewMessageQ(this.user.getUsername());
				if (newMessages != null){
					//Send all messages in the queue to the the client
					System.out.println(String.format("%s has %d new messages",this.user.getUsername(), newMessages.size()));
					Iterator<Message> iterate = newMessages.iterator();
					while(iterate.hasNext()) {
						Message currentMessage = iterate.next();
						try {
							this.outTunnel.writeObject(currentMessage);
							this.outTunnel.flush();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				else{
					System.out.println(String.format("%s has no new messages", this.user.getUsername()));
				}
				
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					return;
				}
			}
		}
	}


	}
}


