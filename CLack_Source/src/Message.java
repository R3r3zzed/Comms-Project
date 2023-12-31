import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
	private static final long serialVersionUID = 20000000l;
	private String sentBy; //username
	private Date dateSent;
	private String chatroomUID; //the chatroom ID
	private msgStatus currentMsgStatus;
	private String content;
    private msgType type;
    
    public Message(String username, Date originalDate, String chatroomID, msgStatus status, msgType type, String text){
        this.sentBy = username;
        this.dateSent = originalDate;
        this.chatroomUID = chatroomID;
        this.currentMsgStatus = status;
        this.content = text;
        this.type = type;

    }

    public String toString() {
        return String.format("Sent by %s: %s", getSendBy(), getContent());
    }

    public String getContent(){
        return content;
    }


    public String getSendBy(){
        return sentBy;
    }

    public msgStatus getMsgStatus(){
        return currentMsgStatus;
    }

    public void changeMsgStatus(){
        currentMsgStatus = msgStatus.RECEIVED;
    }

    public msgType getType(){
        return type;
    }

    public Date getDateSent(){
        return dateSent;
    }
    public String getChatroomID(){
        return this.chatroomUID;
    }

}
