import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.RepeatedTest;
//import org.junit.jupiter.api.Test;
import org.junit.Test;

public class UserTests {
	 @Test
	 public void testGenerateID() {
		 String id1 = User.generateID();
	     String id2 = User.generateID();
	     assertNotEquals(id1, id2);
	 }
	 
	 
	 @Test
	 public void testConstructorWithGeneratedID() {
		 // Test constructor generates a unique ID 
		 User user = new User("John Doe", "john.doe", "password123", UserType.BASIC);
		 assertNotNull(user.getUserID());
	 }
	 
	 
	 @Test
	 public void testConstructorWithSpecificID() {
		 // Test constructor accepts a specific ID
		 String specificID = "123456";
		 User user = new User(specificID, "Jane Smith", "jane.smith", "pass456", UserType.IT);
		 assertEquals(specificID, user.getUserID());
	 }

    @Test
    public void testGetters() {
        // Test getter methods
        User user = new User("John Doe", "john.doe", "password123", UserType.BASIC);

        assertEquals("John Doe", user.getName());
        assertEquals("john.doe", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals(UserStatus.OFFLINE, user.getUserStatus());
        assertEquals(UserType.BASIC, user.getUserType());
    }

    @Test
    public void testChangeStatus() {
        // Test changing user status
        User user = new User("John Doe", "john.doe", "password123", UserType.BASIC);

        assertEquals(UserStatus.OFFLINE, user.getUserStatus());

        user.changeStatus(UserStatus.ONLINE);
        assertEquals(UserStatus.ONLINE, user.getUserStatus());

        user.changeStatus(UserStatus.OFFLINE);
        assertEquals(UserStatus.OFFLINE, user.getUserStatus());
    }
    
    @Test
    public void testUniqueIDGeneration() {
    	// Test generated IDs are unique
        User user1 = new User("User 1", "user1", "pass1", UserType.BASIC);
        User user2 = new User("User 2", "user2", "pass2", UserType.IT);

        assertNotEquals(user1.getUserID(), user2.getUserID());
    }
}
