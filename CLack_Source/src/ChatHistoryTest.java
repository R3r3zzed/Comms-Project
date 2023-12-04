package Implementation;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class ChatHistoryTest {
    private ChatHistory chatHistory;
    private final String testFileName = "testChatHistory.txt";
    private File testFile;

    @BeforeEach
    public void setUp() throws IOException {
        testFile = new File(testFileName);
        testFile.createNewFile(); // Create a new file for each test
        chatHistory = new ChatHistory(testFileName);
    }

    @AfterEach
    public void tearDown() {
        testFile.delete(); // Clean up by deleting the file after each test
    }

    @Test
    public void testUpdateChatRoomFile() throws IOException {
        Message testMessage = new Message("user1", new java.util.Date(), "testChatRoom", msgStatus.SENT, msgType.TEXT, "Hello World");
        chatHistory.updateChatRoomFile(testMessage);

        assertTrue(testFile.length() > 0, "File should not be empty after writing a message");
        // Optionally, read the file contents and assert the message is correctly written
    }

    @Test
    public void testLoadChatRoomFiles() throws IOException, ParseException {
        // Write a test message to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("user1;;;Mon Mar 15 00:00:00 PDT 2021;;;testChatRoom;;;SENT;;;Hello World\n");
        }

        Vector<Message> messages = chatHistory.loadChatRoomFiles();
        assertFalse(messages.isEmpty(), "Message vector should not be empty after loading messages");
        assertEquals(1, messages.size(), "There should be one message loaded from the file");
        assertEquals("Hello World", messages.get(0).getContent(), "Content of the loaded message should match");
    }

    @Test
    public void testLoadChatRoomFilesEmptyFile() {
        Vector<Message> messages = chatHistory.loadChatRoomFiles();
        assertTrue(messages.isEmpty(), "Message vector should be empty when loading from an empty file");
    }

    @Test
    public void testChatHistoryFileSetting() {
        assertEquals(testFileName, chatHistory.getChatHistoryFile(), "Chat history file name should match the one set");
    }

    // Add additional tests as needed...
}