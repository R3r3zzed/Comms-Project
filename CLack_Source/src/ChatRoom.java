package Implementation;

import java.util.List;
import java.util.Vector;
import java.util.ArrayList;

import java.util.stream.Collectors;

public class ChatRoom {
	private String chatID;
	private List<Message> history;
	private Vector<ServerHandler> participantHandlers;

	
	
	public ChatRoom(String chatID){
		this.chatID = chatID;
		this.history = new Vector<>();
		this.participantHandlers = new Vector<>();

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
}
