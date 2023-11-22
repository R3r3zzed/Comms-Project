import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ChatHistory {
    private String chatRoomFilename;

    public ChatHistory(String newChatroomFileName){
        setChatHistoryFile(newChatroomFileName);
    }

    public void updateChatRoomFile(Message m){
        try (FileWriter f = new FileWriter(this.chatRoomFilename, true); 
        BufferedWriter b = new BufferedWriter(f); 
        PrintWriter p = new PrintWriter(b);) {
            p.println(String.format("%s;;;%s;;;%s;;;%s;;;%s", m.getSendBy(), m.getDateSent().toString(), m.getChatroomID(), m.getMsgStatus().toString(), m.getContent() )); 
        } 
        catch (IOException i) { i.printStackTrace(); }

    }

    public Message[] loadChatRoomFiles(){
        return null;
    }

    private void setChatHistoryFile(String newFile){
        this.chatRoomFilename = newFile;
    }


    
}
