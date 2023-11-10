package Implementation;

import java.io.*;
import java.lang.System.Logger;
import java.net.*;


public class Server {
	private User[] listUsers;
	private ChatRoom[] chatrooms;
	private Thread[] threads[];
	private Logger terminalLogger;
		
  	public static void main(String[] args){
  		Server s = new Server();
  		s.loadInUsers();  
  		
  		
    }
	
	public User[] getListUsers() {
		return listUsers;
	}

	public void setListUsers(User[] listUsers) {
		this.listUsers = listUsers;
	}

	public ChatRoom[] getChatrooms() {
		return chatrooms;
	}

	public void setChatrooms(ChatRoom[] chatrooms) {
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
		//grab users then set them as users
		setListUsers(null);
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

	private static class ServerHandler implements Runnable {
		private final Socket clientSocket;
		private String ip;
		private ChatRoom[] currentChatRooms;

		public ServerHandler(Socket socket)
		{
			this.clientSocket = socket;
		}

		public void run()
		{
			
		}
		public void FirstTimeLogin() {
			
		}
		public void ReceiveNewMsg() {
			
		}
		public void SendNewMsg() {
			
		}
	}
    
}
