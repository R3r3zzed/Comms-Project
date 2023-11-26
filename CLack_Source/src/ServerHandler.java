import java.io.*;
import java.net.Socket;

class ServerHandler implements Runnable {
		private final Socket clientSocket;
		private String ip;
		private ChatRoom[] currentChatRooms;
		private ObjectInputStream objectInputStream;
		private ObjectOutputStream objectOutputStream;
		private User currentUser;
		
		private static Server ServerInfo = new Server(); //each thread made has access to the same server object
		
		public ServerHandler(Socket socket){
			this.clientSocket = socket;
			this.ip = this.clientSocket.getInetAddress().getHostAddress();
		}		
		
		public String getIp() {
			return ip;
		}

		public ChatRoom[] getCurrentChatRooms() {
			return currentChatRooms;
		}

		public void setCurrentChatRooms(ChatRoom[] currentChatRooms) {
			this.currentChatRooms = currentChatRooms;
		}
	 
		public void FirstTimeLogin() {
			//Sent all messages and chats with this user in it
			//Step 1: Find the chats with user in it
			ChatRoom[] userChatroomsFromServer = ServerInfo.getUserChatrooms(this.currentUser.getUsername());
			
			//Step 2: Send them over the objectOutputStream
			for (int i = 0; i < userChatroomsFromServer.length; i++) {  
				Message[] msgs = userChatroomsFromServer[i].getHistory();
				for (int j = 0; j < msgs.length; j++){
					try {
						objectOutputStream.writeObject(msgs[j]);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}	
			}
			
			//Step 3: Send empty object to signal end of transfer
			try {
				objectOutputStream.writeObject(new Message(null, null, null, null, null));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//Step 4: Update Server to show user is online
			ServerInfo.updateUserStatus(this.currentUser.getUsername());
				
		}
		public Message ReceiveNewMsg() {
			// look through the users chat rooms for a new message and send to the client
			
			return null;
			
		}
		public void sendNewMessage() {
			//Receives a new message from Client from the objectInputStream
			try {
				Message newMessage = (Message) objectInputStream.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//updates the server so all the threads of other clients' get the message
			updateServerWithNewMessage();
						
		}
		
		public void updateServerWithNewMessage() {

		}
		
		public void logOut() {
			
		}

		public void run(){
			//port = 1234
			//create the in and out to the client 
			try {
				OutputStream outputStream = this.clientSocket.getOutputStream();
				InputStream inputStream = this.clientSocket.getInputStream();
				this.objectOutputStream = new ObjectOutputStream(outputStream);
				this.objectInputStream = new ObjectInputStream(inputStream);


				//Who is logging on
				boolean doesntUser = true;
				while(doesntUser){
					String UserAndPassword;
					try {
						UserAndPassword = (String) objectInputStream.readObject();
						String[] userInfo = UserAndPassword.split(";;;");
						doesntUser = ! ServerInfo.UserAuthentication(userInfo[0], userInfo[1]);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}

				 FirstTimeLogin();

				//get the different messages from client
				while (true) {
					// Both method for getting new messages and sending new messages get its own thread
					//Getting messages from server is constant but getting messages from client is main thread (need to know when to log out)
					
					//get new messages from server
					
		
					//get new messages from client
					
					break;
				}
				
				logOut();
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
			
	}