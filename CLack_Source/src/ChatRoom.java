import java.util.List;
import java.util.ArrayList;

import java.util.stream.Collectors;


public class ChatRoom {
	int DEFAULT_ARRAY_SIZE = 10;

	private String chatID;
	private ChatHistory history;
	private Message[] roomMessages;
	private String[] participantHandlers;
	
	public ChatRoom(String chatID, String[] users, String filename){
		this.chatID = chatID;
		this.history = new ChatHistory(filename);
		this.roomMessages = new Message[DEFAULT_ARRAY_SIZE];
		this.participantHandlers = users;

	}
	public ChatRoom(String chatID, String[] users, Message[] newMessages, String filename){
		this.chatID = chatID;
		this.history = new ChatHistory(filename);
		this.roomMessages = newMessages;
		this.participantHandlers = users;
	}

	//getters	
	public String getChatID() {
		return chatID;
	}
	
	public void displayMessage(Message message) {
		System.out.println(message.getContent());
	}
	
	public Message[] getHistory(){
		return this.roomMessages;
	}
	
	public String[] getUsers() {
		return participantHandlers;
	}
}
