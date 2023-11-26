
public class Client {
	public static void main(String args[]) {
		
		/*
		 * Opens the login screen. For testing
		 */
		Client client = new Client();
		
		LoginUI loginScreen = new LoginUI();
		loginScreen.display(client);
		
		while(loginScreen.isLoggedIn() == false) {
			// don't open anything else while not logged in
		}
		
	}
}
