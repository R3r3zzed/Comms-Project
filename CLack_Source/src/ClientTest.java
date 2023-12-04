import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

public class ClientTest {

    private Client client;
    private final String testServerIP = "localhost";
    private final int testServerPort = 1235;

    @BeforeEach
    public void setUp() throws IOException {
        Socket mockSocket = new Socket(testServerIP, testServerPort);
        client = new Client(mockSocket);
    }

    @Test
    public void testLogin() {
        String validUsername = "validUser";
        String validPassword = "validPass";
        assertTrue(client.login(validUsername, validPassword), "Login should return true for valid credentials");

        String invalidUsername = "invalidUser";
        String invalidPassword = "invalidPass";
        assertFalse(client.login(invalidUsername, invalidPassword), "Login should return false for invalid credentials");
    }

    @Test
    public void testUpdateMessage() {
        Message testMessage = new Message("user1", new java.util.Date(), "testChatRoom", msgStatus.SENT, msgType.TEXT, "Hello World");
        client.updateMessage(testMessage);
        assertTrue(client.getChatRooms().stream().anyMatch(room -> room.getChatID().equals("testChatRoom")), "Chat room with the given ID should exist");
        assertTrue(client.getChatRooms().stream().filter(room -> room.getChatID().equals("testChatRoom")).anyMatch(room -> room.getHistory().contains(testMessage)), "Updated chat room should contain the test message");
    }

    @Test
    public void testSendMessageToServer() {
        Message testMessage = new Message("user1", new java.util.Date(), "testChatRoom", msgStatus.SENT, msgType.TEXT, "Hello Server");
        client.sendMessageToServer(testMessage);
        assertDoesNotThrow(() -> client.sendMessageToServer(testMessage), "Sending message to server should not throw an exception");
    }

    @Test
    public void testGetChatRooms() {
        Vector<ChatRoom> chatRooms = client.getChatRooms();
        assertNotNull(chatRooms, "getChatRooms should not return null");
    }

    @Test
    public void testGetDirectory() {
        Vector<User> directory = client.getDirectory();
        assertNotNull(directory, "Directory should not be null");
    }

    @Test
    public void testGetCurrentUser() {
        User currentUser = client.getCurrentUser();
        assertNotNull(currentUser, "Current user shouldn't be null");
    }

    @Test
    public void testCloseConnection() {
        assertDoesNotThrow(() -> client.close(), "Closing the client shouldn't throw an exception");
    }
}
