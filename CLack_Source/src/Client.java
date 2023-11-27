import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	public static void main(String args[]) {
		/* 
		 * For testing
		 * Opens the login screen.
		 */
		Client client = new Client();
		
		LoginUI loginScreen = new LoginUI();
		loginScreen.display(client);
		
		while(loginScreen.isLoggedIn() == false) {
			// don't open anything else while not logged in
			System.out.println(loginScreen.isLoggedIn());
		}
		
		//
		// Setup client information; Server <-> Client interactions
		//
	
		MainUI mainUI = new MainUI();
		mainUI.display(client);
		
		/*
		 * For testing
		 */
		
	}
	
	private static Socket createSocket(String ipad, int portnum) throws IOException {
		return new Socket(ipad, portnum);
	}
	
	private static String getUserInput(Scanner sc) {
		System.out.println("\nEnter: ");
		return sc.nextLine();
	}
	
	public boolean login(String username, String password) throws IOException, ClassNotFoundException{
		/*
		 * For testing
		 */
		String name = "admin";
		String pass = "admin";
		if(!(name.equals(username)) || !(pass.equals(password)))
			return false;
		return true;
		/*
		 * For testing
		 */
	}
	
	public void requestLogs(String username) {
		
	}
	
	public int findUserFromDirectory(User u) {
		return 0;
		
	}
	
	public void openChatRoom(String id) {
		
	}
	
	public void loadChatRoom(String id) {
		
	}
	
	public void sendMessageToServer() {
		
	}
}
