import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.Vector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class testServer {
	Server normalServer = null;
	@BeforeEach
	void loadDefaultServer(){
		this.normalServer = new Server();
		//Userlist must be defined before the chatroom can be loaded in

		this.normalServer.loadInUsers("/Users/madisonavila/Comms-Project/CLack_Source/src/directory.txt");
		this.normalServer.loadInChatRooms("/Users/madisonavila/Comms-Project/CLack_Source/src/logs");
	}

	@Test 
	void testUsers(){
		Server nofileServer = new Server("notareallyfile.txt", "nologs" );
		// assertEquals(6, this.normalServer.getListUsers().size()); // the number of users in the directory.txt file is 5
		assertNull(nofileServer.getListUsers());
	}

	@Test 
	void testGetChatrooms () {
		Server nofileServer = new Server("notareallyfile.txt", "nologs" );
		Vector<ChatRoom> chatroom = this.normalServer.getChatrooms();
		assertEquals(1, chatroom.size() );
		assertEquals(1, chatroom.firstElement().getHistory().size()); 
		assertNull(nofileServer.getChatrooms());
	}
	@Test 
	void testCheckIT(){
		User madison = this.normalServer.getListUsers().firstElement();
		User root = this.normalServer.getListUsers().lastElement();
		assertFalse(this.normalServer.isITUser(madison));
		assertTrue(this.normalServer.isITUser(root));
	}
	@Test 
	void testUserAuthentication(){
		String[] realUser = "madison;;;password".split(";;;");

		String[] fakeUser = "not;;;real".split(";;;");

		assertTrue(this.normalServer.UserAuthentication(realUser[0], realUser[1]));
		assertFalse(this.normalServer.UserAuthentication(fakeUser[0], fakeUser[1]));
	}
	@Test 
	void testGetUserFromServer(){
		assertTrue(this.normalServer.getUser("madison") instanceof User);
		assertNull(this.normalServer.getUser("not a really user"));
	}

	@Test 
	void testNewMessage(){
		String sender = "madison";
		Message existingChatRoom = new Message(sender, new Date(), "madison-sean-david-sedat-joseph", msgStatus.SENT, msgType.TEXT, "message from existingChatRoom");
		Message newChatRoom = new Message(sender, new Date(), "madison-root", msgStatus.SENT, msgType.TEXT, "message from existingChatRoom");
		//send the new message to the 
		int originalChatroomsNumber = this.normalServer.getChatrooms().size();
		this.normalServer.updateNewMessage(existingChatRoom);

		Vector<ChatRoom> chatrooms = this.normalServer.getChatrooms();
		assertEquals(originalChatroomsNumber, chatrooms.size() );
		assertEquals(2, chatrooms.firstElement().getHistory().size()); 

		this.normalServer.updateNewMessage(newChatRoom);
		assertEquals(originalChatroomsNumber+1, chatrooms.size() );
		assertEquals(1, chatrooms.get(1).getHistory().size()); 

		// assertNull(this.normalServer.getUser(fakeUser[0], fakeUser[1]));
	}
	@Test 
	void testUpdateUserStatus(){
		//real user
		this.normalServer.updateUserStatus("madison"); //make user online
		User madison = this.normalServer.getUser("madison");
		assertEquals(UserStatus.ONLINE, madison.getUserStatus());
		this.normalServer.updateUserStatus("madison");//make user offline
		madison = this.normalServer.getUser("madison");
		assertEquals(UserStatus.OFFLINE, madison.getUserStatus());
	}

	@Test
	void testMessageQ(){
		this.normalServer.setUpUserQ("madison");
		this.normalServer.setUpUserQ("root");
		this.normalServer.updateUserStatus("madison"); //madison online
		this.normalServer.updateUserStatus("root"); //root online
		this.normalServer.setUpUserQ("madison");
		this.normalServer.setUpUserQ("root");

		Message toRoot = new Message("madison", new Date(), "madison-root", msgStatus.SENT, msgType.TEXT, "testing Q sending to root");
		Message toMadison = new Message("root", new Date(), "madison-root", msgStatus.SENT, msgType.TEXT, "testing Q sending to madison");

		this.normalServer.addToNewMessageQ("root", toRoot);
		this.normalServer.addToNewMessageQ("madison", toMadison);

		//check to see if the queue of root has madison's message
		Vector<Message> rootQ = this.normalServer.grabFromNewMessageQ("root");
		assertEquals(1, rootQ.size());

		//check to see if root have 
		Vector<Message> madisonQ = this.normalServer.grabFromNewMessageQ("madison");
		assertEquals(1, madisonQ.size());
	}
}
