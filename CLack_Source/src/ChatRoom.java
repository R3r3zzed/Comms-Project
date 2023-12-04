import java.io.Serializable;
import java.util.Vector;


public class ChatRoom implements Serializable{
	private String chatID;
	private ChatHistory history;
	private Vector<Message> roomMessages;
	private Vector<User> participantHandlers;
	
	public ChatRoom(String chatID, Vector<User> users, String filename){
		this.chatID = chatID;
		this.history = new ChatHistory(filename);
		this.roomMessages = this.history.loadChatRoomFiles();
		this.participantHandlers = users;

	}
	public ChatRoom(String chatID, Vector<User> users, Vector<Message> newMessages, String filename){
		this.chatID = chatID;
		this.history = new ChatHistory(filename);
		this.roomMessages = newMessages;
		this.participantHandlers = users;
	}

	public void addMessage(Message m){
		roomMessages.add(m);
		writeToFile(m);
	}

	//getters	
	public String getChatID() {
		return chatID;
	}
	
	public void displayMessage(Message message) {
		System.out.println(message.getContent());
	}
	
	public Vector<Message> getHistory(){
		return this.roomMessages;
	}
	
	public Vector<User> getUsers() {
		return participantHandlers;
	}

	public String getHistoryFile(){
		return this.history.getChatHistoryFile();
	}
	public void writeToFile(Message m){
		this.history.updateChatRoomFile(m);
	}
}

