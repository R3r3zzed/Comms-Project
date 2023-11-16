import java.io.*;
import java.lang.System.Logger;
import java.net.*;


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
    
}
