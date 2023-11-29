/*
 * For Debugging UI.
 * TODO REMOVE FOR FINAL SUBMISSION
 */

import java.util.Vector;

public class ClientFake{
		private User currentUser;
		private Vector<User> directory;
		private Vector<ChatRoom> rooms;
		
		public static void main(String args[]) {
			ClientFake client = new ClientFake();
			
			LoginUI loginScreen = new LoginUI(client);
			loginScreen.display();
		}
		
		public ClientFake() {
			directory = new Vector<User>();
			rooms = new Vector<ChatRoom>();
			currentUser = new User("Sean Alcantara", "sean", "sean", UserType.IT);
			for(int i = 0; i < 30; i++) {
				directory.add(new User("name"+Integer.toString(i), "user"+Integer.toString(i), "pass"+Integer.toString(i), UserType.BASIC));
			}
			/*
			for(int i = 0; i < 10; i++) {
				rooms.add(new ChatRoom("room" + Integer.toString(i)));
			}
			*/
		}
		public boolean login(String username, String password) {
			String user = "admin";
			String pass = "admin";
			if (username.compareTo(user) == 0 && username.compareTo(pass) == 0) {
				System.out.println("SUCCESS: logged in");
				return true;
			}
			System.out.println("FAILED: logged in");
			return false;
		}
		
		public void close() {
			System.out.println("CLOSING: client");
		}
		
		public Vector<User> getDirectory(){
			return directory;
		}
		
		public Vector<ChatRoom> getChatRooms(){
			return rooms;
		}
		
		public User getCurrentUser() {
			return currentUser;
		}
}