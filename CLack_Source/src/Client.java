
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
	
	public boolean login(String username, String password) {
		/*
		 * For testing
		 */
		String name = "admin";
		String pass = "admin";
		if(name.compareTo(username) != 0 || pass.compareTo(password) != 0) {
			return false;
		}
		return true;
		/*
		 * For testing
		 */
	}
}
