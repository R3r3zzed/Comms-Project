import static org.junit.Assert.*;
import org.junit.Before;
import java.util.Date;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.RepeatedTest;

//import org.junit.jupiter.api.Test;
import org.junit.Test;
import java.util.Date;

public class MessageTests {

    private Message testMessage;

    @Before
    public void setUp() {
        // Set up a sample message for testing
        Date currentDate = new Date();
        testMessage = new Message("User1", currentDate, "Chatroom1", msgStatus.SENT, msgType.TEXT, "Hello world!");
    }

    @Test
    public void testGetters() {
    	// Checking if getters are working properly
        assertEquals("Hello world!", testMessage.getContent());
        assertEquals("User1", testMessage.getSendBy());
        assertEquals(msgStatus.SENT, testMessage.getMsgStatus());
        assertEquals(msgType.TEXT, testMessage.getType());
        assertNotNull(testMessage.getDateSent());
        assertEquals("Chatroom1", testMessage.getChatroomID());
    }

    @Test
    public void testChangeMsgStatus() {
        testMessage.changeMsgStatus();
        assertEquals(msgStatus.RECEIVED, testMessage.getMsgStatus());
    }

    @Test
    public void testToString() {
        assertEquals("Sent by User1: Hello world!", testMessage.toString());
    }

    @Test
    public void testLogoutMessageType() {
        Message logoutMessage = new Message("User1", new Date(), "Chatroom1", msgStatus.SENT, msgType.LOGOUT, "Logging out");
        assertEquals(msgType.LOGOUT, logoutMessage.getType());
    }

    @Test
    public void testChangeMsgStatusReceived() {
        // Verify that changeMsgStatus sets the status to RECEIVED
        testMessage.changeMsgStatus();
        assertEquals(msgStatus.RECEIVED, testMessage.getMsgStatus());
    }

    @Test
    public void testChangeMsgStatusTwice() {
        // Verify that changeMsgStatus doesn't change the status if called twice
        testMessage.changeMsgStatus();
        testMessage.changeMsgStatus();
        assertEquals(msgStatus.RECEIVED, testMessage.getMsgStatus());
    }
}
