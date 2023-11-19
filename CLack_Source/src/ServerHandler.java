import java.io.*;
import java.net.Socket;

class ServerHandler implements Runnable {
		private final Socket clientSocket;
		private String ip;
		private ChatRoom[] currentChatRooms;
		
		private ObjectInputStream objectInputStream;
		private ObjectOutputStream objectOutputStream;
		
		private static Server ServerInfo = new Server(); //each thread made has access to the same server object
		
		public ServerHandler(Socket socket){
			this.clientSocket = socket;
			this.ip = this.clientSocket.getInetAddress().getHostAddress();
			
				
				//create the in and out to the client 
				try {
					OutputStream outputStream = this.clientSocket.getOutputStream();
					InputStream inputStream = this.clientSocket.getInputStream();
					this.objectOutputStream = new ObjectOutputStream(outputStream);
					this.objectInputStream = new ObjectInputStream(inputStream);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
		
			
			
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
			
			
		}
		public void ReceiveNewMsg() {
			
		}
		public void SendNewMsg() {
			
		}
		
		public void logOut() {
			
		}

		public void run(){
			//port = 1234
			
		}
			
	}