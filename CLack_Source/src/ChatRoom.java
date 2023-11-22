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
		this.roomMessages = new Message[DEFAULT_ARRAY_SIZE];
		this.participantHandlers = users;
		this.history = new ChatHistory(filename);
	}

	//getters	
	public String getChatID() {
		return chatID;
	}
	
	public List<String> getParticipantIDs() {
        // Assuming ServerHandler has a method to get the participant's ID (e.g., getClientId())
        return participantHandlers.stream()
                                  .map(handler -> handler.getClientId()) // This method needs to exist in ServerHandler
                                  .distinct() // To ensure unique IDs
                                  .collect(Collectors.toList());
    }
	
	
	//Changing chatroom participants
	
	   public void addParticipantHandler(ServerHandler handler) {
	        if (!participantHandlers.contains(handler)) {
	            participantHandlers.add(handler);
	            System.out.println("User " + handler.getParticipantID() + " has been added.");
	        }
	    }
	
	   public void removeParticipantHandler(ServerHandler handler) {
	        participantHandlers.remove(handler);
	        System.out.println("User " + handler.getParticipantID() + " has been removed.");
	    }
	
	//Message methods
	public void sendMessage(Message message) {
		history.add(message);
		
		for(ServerHandler handler: participantHandlers) {
			handler.SendNewMsg(message);
		}
	}
	
	public void displayMessage(Message message) {
		System.out.println(message.getContent());
	}
	
	public List<Message> getHistory(){
		return new ArrayList<>(history);
	}

	public void setParticipants(String[] users){

	}
}
