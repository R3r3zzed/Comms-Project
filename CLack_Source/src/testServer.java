import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
//import org.junit.jupiter.api.RepeatedTest;
import org.junit.BeforeClass;
//import org.junit.jupiter.api.Test;
import org.junit.Test;
import java.util.Date;
import java.util.Vector;

//import org.junit.jupiter.api.BeforeEach;


public class testServer {
	static Server normalServer = null;
	
	@BeforeClass
	public static void loadDefaultServer(){
		normalServer = new Server();
		//Userlist must be defined before the chatroom can be loaded in

		normalServer.loadInUsers("/Users/madisonavila/Comms-Project/CLack_Source/src/directory.txt");
		normalServer.loadInChatRooms("/Users/madisonavila/Comms-Project/CLack_Source/src/logs");
	}

	@Test 
	public void testUsers(){
		Server nofileServer = new Server("notareallyfile.txt", "nologs" );
		// assertEquals(6, this.normalServer.getListUsers().size()); // the number of users in the directory.txt file is 5
		assertNull(nofileServer.getListUsers());
	}

	@Test 
	public void testGetChatrooms () {
		Server nofileServer = new Server("notareallyfile.txt", "nologs" );
		Vector<ChatRoom> chatroom = normalServer.getChatrooms();
		assertEquals(1, chatroom.size() );
		assertEquals(1, chatroom.firstElement().getHistory().size()); 
		assertNull(nofileServer.getChatrooms());
	}
	@Test 
	public void testCheckIT(){
		User madison = normalServer.getListUsers().firstElement();
		User root = normalServer.getListUsers().lastElement();
		assertFalse(normalServer.isITUser(madison));
		assertTrue(normalServer.isITUser(root));
	}
	@Test 
	public void testUserAuthentication(){
		String[] realUser = "madison;;;password".split(";;;");

		String[] fakeUser = "not;;;real".split(";;;");

		assertTrue(normalServer.UserAuthentication(realUser[0], realUser[1]));
		assertFalse(normalServer.UserAuthentication(fakeUser[0], fakeUser[1]));
	}
	@Test 
	public void testGetUserFromServer(){
		assertTrue(normalServer.getUser("madison") instanceof User);
		assertNull(normalServer.getUser("not a really user"));
	}

	@Test 
	public void testNewMessage(){
		String sender = "madison";
		Message existingChatRoom = new Message(sender, new Date(), "madison-sean-david-sedat-joseph", msgStatus.SENT, msgType.TEXT, "message from existingChatRoom");
		Message newChatRoom = new Message(sender, new Date(), "madison-root", msgStatus.SENT, msgType.TEXT, "message from existingChatRoom");
		//send the new message to the 
		int originalChatroomsNumber = normalServer.getChatrooms().size();
		normalServer.updateNewMessage(existingChatRoom);

		Vector<ChatRoom> chatrooms = normalServer.getChatrooms();
		assertEquals(originalChatroomsNumber, chatrooms.size() );
		assertEquals(2, chatrooms.firstElement().getHistory().size()); 

		normalServer.updateNewMessage(newChatRoom);
		assertEquals(originalChatroomsNumber+1, chatrooms.size() );
		assertEquals(1, chatrooms.get(1).getHistory().size()); 

		// assertNull(this.normalServer.getUser(fakeUser[0], fakeUser[1]));
	}
	@Test 
	public void testUpdateUserStatus(){
		//real user
		normalServer.updateUserStatus("madison"); //make user online
		User madison = normalServer.getUser("madison");
		assertEquals(UserStatus.ONLINE, madison.getUserStatus());
		normalServer.updateUserStatus("madison");//make user offline
		madison = normalServer.getUser("madison");
		assertEquals(UserStatus.OFFLINE, madison.getUserStatus());
	}

	@Test
	public void testMessageQ(){
		normalServer.setUpUserQ("madison");
		normalServer.setUpUserQ("root");
		normalServer.updateUserStatus("madison"); //madison online
		normalServer.updateUserStatus("root"); //root online
		normalServer.setUpUserQ("madison");
		normalServer.setUpUserQ("root");

		Message toRoot = new Message("madison", new Date(), "madison-root", msgStatus.SENT, msgType.TEXT, "testing Q sending to root");
		Message toMadison = new Message("root", new Date(), "madison-root", msgStatus.SENT, msgType.TEXT, "testing Q sending to madison");

		normalServer.addToNewMessageQ("root", toRoot);
		normalServer.addToNewMessageQ("madison", toMadison);

		//check to see if the queue of root has madison's message
		Vector<Message> rootQ = normalServer.grabFromNewMessageQ("root");
		assertEquals(1, rootQ.size());

		//check to see if root have 
		Vector<Message> madisonQ = normalServer.grabFromNewMessageQ("madison");
		assertEquals(1, madisonQ.size());
	}
}
