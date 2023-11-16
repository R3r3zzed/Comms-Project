

public class User {
    	private String userID;
	private String name;
	private String username;
	private String password;
	private UserStatus status;
	private UserType type;
	
    	private static long id = 0;  // Counter for generating unique user IDs
	
	// Generate random ID for user
	public static synchronized String generateID() {
		return String.valueOf(id++);
	}
	
	// Constructor for creating a new user
    	public User(String name, String username, String password, UserType type) {
        	this.userID = generateID();   // Generate a unique user ID
		this.name = name;
        	this.username = username;
        	this.password = password;
        	this.type = type;
        	this.status = UserStatus.OFFLINE; // Initial status for a new user (assumed offline)
    	}
	
    	// Getter method for retrieving the user ID
	public String getUserID() {
		return userID;
	}

	// Getter method for retrieving the user's name
	public String getName() {
		return name;
	}
	
   	// Getter method for retrieving the username
	public String getUsername() {
		return username;
	}
	
    	// Getter method for retrieving the password
	public String getPassword() {
		return password;
	}
	
    	// Getter method for retrieving the user status
	public UserStatus getUserStatus() {
		return status;
	}

    	// Getter method for retrieving the user type
	public UserType getUserType() {
		return type;
	}
	
	// Method to set the user status
	public void changeStatus(UserStatus newStatus) {
		this.status = newStatus;
	}
}
