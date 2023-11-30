import java.util.List;
import java.util.Vector;
import java.util.ArrayList;

import java.util.stream.Collectors;


public class ChatRoom {
	private String chatID;
	private ChatHistory history;
	private Vector<Message> roomMessages;
	private Vector<String> participantHandlers;
	
	public ChatRoom(String chatID, Vector<String> users, String filename){
		this.chatID = chatID;
		this.history = new ChatHistory(filename);
		this.roomMessages = new Vector<Message>();
		this.participantHandlers = users;

	}
	public ChatRoom(String chatID, Vector<String> users, Vector<Message>  newMessages, String filename){
		this.chatID = chatID;
		this.history = new ChatHistory(filename);
		this.roomMessages = newMessages;
		this.participantHandlers = users;
	}

	public void addMessage(Message m){
		roomMessages.add(m);
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
	
	public Vector<String>  getUsers() {
		return participantHandlers;
	}
	public String getHistoryFile(){
		return this.history.getChatHistoryFile();
	}
}
