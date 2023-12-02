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
import java.util.Queue;
import java.util.concurrent.*; 

public class Server {
	private Vector<User> listUsers;
	private Vector<ChatRoom> chatrooms;
	private ConcurrentHashMap<String, Queue<Message>> newMsg;
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
  		loadInUsers("directory.txt"); // loads all companies Users into the server.Users array
 		loadInChatRooms("logs"); // loads all Chat rooms for all users into the server.chatrooms array	
		newMsg = new ConcurrentHashMap<String, Queue<Message>>();
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
		File folder = new File(logDirectory);
		File[] listOfFiles = folder.listFiles();
		Vector<ChatRoom> chatrooms = new Vector<ChatRoom>();
		if (listOfFiles == null) {
			// add logging to show that logs were empty
			return;
		}
		for (int i = 0; i < listOfFiles.length; i++) {		
			String chatroomFilename = logDirectory + "/"  + listOfFiles[i].getName();
			try {
				File fileObj = new File(chatroomFilename);
				String[] userString = chatroomFilename.replace(".log","").split("-");
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
				Vector<Message> tmpMessageVec= new Vector<Message>();
				Scanner reader = new Scanner(fileObj);
				String chatroomID = null;
				while (reader.hasNextLine()) {                
					String messageInfo = reader.nextLine();
					//file contents per line
					//sentBy;;;dateSent;;;chatroomUID;;;msgStatus;;;content
					String[] messageArray = messageInfo.split(";;;");
					DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss zzz yyyy");
					Date date = df.parse(messageArray[1]);
					chatroomID = messageArray[2];
					tmpMessageVec.add(new Message(messageArray[0], date, messageArray[2], messageArray[3], msgType.TEXT, messageArray[4]));
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

	public User getUser(String user, String pass) {
        Iterator<User> iterate = listUsers.iterator(); //first user
        while(iterate.hasNext()) {
			User current = iterate.next();
            if (user.matches(current.getUsername()) && pass.matches(current.getPassword())) {
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
		Vector<User> chatroomUsers = null;
		if (foundChatroom != null){
			int index = chatrooms.indexOf(foundChatroom);
			ChatRoom c = chatrooms.get(index);
			c.addMessage(m);
			chatrooms.set(index, c);
			chatroomUsers = c.getUsers();
		}
		//figure out which users are online
		Iterator<User> iterateChatroomUsers = chatroomUsers.iterator();
        while(iterateChatroomUsers.hasNext()) {

			User currentChatroom = iterateChatroomUsers.next();
			String usernameChatroom = currentChatroom.getUsername();

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
	
	public void addToNewMessageQ(String User, Message m) {
		Queue<Message> newMessages = newMsg.get(User);
		newMsg.remove(User, newMessages);
		newMessages.add(m);
		newMsg.put(User, newMessages);
	}
	
	public Vector<Message> grabFromNewMessageQ(String User) {
		//gets the messages from the queue
		Vector<Message> vectorMessage= new Vector<Message>();
		Queue<Message> newMessages = newMsg.get(User);
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
		try{
			Queue<Message> newMessages = newMsg.get(User);
			newMsg.remove(User, newMessages);
		}
		catch (java.lang.NullPointerException e) {

		}

	}
    

	
	private static class ServerHandler implements Runnable {
		private final Socket clientSocket;
		private String ip;
		private Vector<ChatRoom> currentChatRooms;
		private ObjectInputStream objectInputStream;
		private ObjectOutputStream objectOutputStream;
		private DataOutputStream dataOutputStream;

		private User currentUser;
		
		public static Server ServerInfo = new Server(); //each thread made has access to the same server object
		private Logger ScreenLog;

		public ServerHandler(Socket socket){
			this.clientSocket = socket;
			this.ip = this.clientSocket.getInetAddress().getHostAddress();
		}		
		
		public void run(){
			//create the in and out to the client 
			try {
				OutputStream outputStream = this.clientSocket.getOutputStream();
				InputStream inputStream = this.clientSocket.getInputStream();

				//for single messages and other objects
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
				ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
				//for files
				DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(outputStream));

				// //Who is logging on
				boolean ValidUser = false;
				while(! ValidUser){
					String UserAndPassword = (String) objectInputStream.readObject();
					String[] userInfo = UserAndPassword.split(";;;");
					ValidUser = ServerInfo.UserAuthentication(userInfo[0], userInfo[1]);
					this.currentUser = ServerInfo.getUser(userInfo[0], userInfo[1]);
					System.out.println(UserAndPassword);
					System.out.println(ValidUser);

					if (ValidUser){
						//add log to server to show which user is online
						// return a true boolean to tell the user is online
						objectOutputStream.writeBoolean(ValidUser);
						ServerInfo.updateUserStatus(userInfo[0]);

						System.out.println(this.currentUser.getUsername() + " is online");
					}
					else {
						// return a false boolean to tell the user is not valid
						objectOutputStream.writeBoolean(ValidUser);
					}
				}
				// Send over all messages to client, and updated server on the online status of the user
				FirstTimeLogin();

				// Both method for getting new messages and sending new messages get its own thread
				// Getting messages from server is constant in another thread but getting messages from client this main thread (need to know when to log out)
				
				receiveNewMessageClass newMessageClass = new receiveNewMessageClass(this.currentUser, this.ServerInfo, objectOutputStream);
				Thread newMessageThread = new Thread(newMessageClass);
				newMessageThread.start();
				
				// Get the different messages from client
				while (true) {
					// wait for a new messages from client
					Message newMessage = (Message) objectInputStream.readObject();
					// check if message is a log out message
					if (newMessage.getType() == msgType.LOGOUT){
					newMessageThread.interrupt();
					System.out.println("Logging out");
					logOut();
					return;
					}
					System.out.println(newMessage.toString());
					//update server and other users of new message - it will send to all users including the user it was sent from
					ServerInfo.updateNewMessage(newMessage);
				}
			} catch (SocketException e){
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			catch (ClassNotFoundException e) {
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
	 
		public void FirstTimeLogin() {
			//Sent all messages and chats with this user in it
			//Step 1: Find the chats with user in it
			Vector<ChatRoom> userChatroomsFromServer = ServerInfo.getUserChatrooms(this.currentUser.getUsername());
			
			//Step 2: Send their file over the network using the dataOutputStream object
			Iterator<ChatRoom> iterate = userChatroomsFromServer.iterator();
			while(iterate.hasNext()) {
				ChatRoom current = iterate.next();
				String chatRoomFilename = current.getHistoryFile();
				sendFile(chatRoomFilename);
			}
		}

		private void sendFile(String path) {
			try {
				File file = new File(path);
				FileInputStream fileInputStream;
				fileInputStream = new FileInputStream(file);
				this.dataOutputStream.writeLong(file.length());
				int bytes = 0;
				byte[] buffer = new byte[4096];
				while ((bytes = fileInputStream.read(buffer)) != -1) {
				dataOutputStream.write(buffer, 0, bytes);
				dataOutputStream.flush();
				}
				fileInputStream.close();
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void logOut() {
			ServerInfo.updateUserStatus(this.currentUser.getUsername());
			ServerInfo.revoveUserFromQ(currentUser.getUsername());
		}
	}

	public static class receiveNewMessageClass implements Runnable {
		private ObjectOutputStream outTunnel;
		private User user;
		private Server serverInfo;
		
		public receiveNewMessageClass(User u, Server s, ObjectOutputStream outObject) {
			this.outTunnel = outObject;
			this.user = u;
			this.serverInfo = s;
		}
		
		public void run(){
			//check to see if there are new messages for User
			Vector<Message> newMessages = this.serverInfo.grabFromNewMessageQ(this.user.getUsername());
			if (newMessages != null){
				//Send all messages in the queue to the the client
				Iterator<Message> iterate = newMessages.iterator();
				while(iterate.hasNext()) {
					Message currentMessage = iterate.next();
					try {
						this.outTunnel.writeObject(currentMessage);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
			}
			
		}
	}



}


