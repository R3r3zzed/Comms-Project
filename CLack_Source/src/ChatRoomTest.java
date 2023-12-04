
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;
import java.util.Vector;

public class ChatRoomTest {
	private ChatRoom chatRoom;
	private final String chatRoomID = "testChatRoom";
	private final String testFileName = "testHistory.txt";
	private Vector<User> users;
	private User testUser;

	@BeforeEach
	public void setUp() {
		users = new Vector<>();
		testUser = new User("User Name", "user1", "password", UserType.BASIC); // Adjust as needed
		users.add(testUser);
		chatRoom = new ChatRoom(chatRoomID, users, testFileName);
	}

	@Test
	public void testAddMessage() {
		Message testMessage = new Message(testUser.getUsername(), new Date(), chatRoomID, msgStatus.SENT, msgType.TEXT,
				"Hello World");
		chatRoom.addMessage(testMessage);
		assertFalse(chatRoom.getHistory().isEmpty(), "History should not be empty after adding a message");
		assertEquals("Hello World", chatRoom.getHistory().get(0).getContent(), "Content of the message should match");
		assertEquals(msgStatus.SENT, chatRoom.getHistory().get(0).getMsgStatus(), "Message status should match");
		assertEquals(msgType.TEXT, chatRoom.getHistory().get(0).getType(), "Message type should match");
	}

	@Test
	public void testGetUsers() {
		Vector<User> retrievedUsers = chatRoom.getUsers();
		assertNotNull(retrievedUsers, "Users vector should not be null");
		assertFalse(retrievedUsers.isEmpty(), "Users vector should not be empty");
		assertTrue(retrievedUsers.contains(testUser), "Users vector should contain the test user");
	}

	@Test
	public void testChatRoomConstructor() {
		assertNotNull(chatRoom.getHistory(), "History should not be null after initialization");
		assertEquals(chatRoomID, chatRoom.getChatID(), "Chat ID should match the one provided at initialization");
	}

	@Test
	public void testAddNullMessage() {
		assertDoesNotThrow(() -> chatRoom.addMessage(null), "Adding a null message should not throw an exception");
	}
	
	@Test
	public void testMessageOrderInHistory() {
	    Message firstMessage = new Message(testUser.getUsername(), new Date(), chatRoomID, msgStatus.SENT, msgType.TEXT, "First Message");
	    Message secondMessage = new Message(testUser.getUsername(), new Date(), chatRoomID, msgStatus.SENT, msgType.TEXT, "Second Message");
	    chatRoom.addMessage(firstMessage);
	    chatRoom.addMessage(secondMessage);

	    assertEquals(2, chatRoom.getHistory().size(), "There should be two messages in the history");
	    assertEquals("First Message", chatRoom.getHistory().get(0).getContent(), "First message content should match");
	    assertEquals("Second Message", chatRoom.getHistory().get(1).getContent(), "Second message content should match");
	}
	
}