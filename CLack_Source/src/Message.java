import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
	private String sentBy; //username
	private Date dateSent;
	private String chatroomUID; //the chatroom ID
	private msgStatus currentMsgStatus;
	private String content;
    private msgType type;
    
    public Message(String username, Date originalDate, String chatroomID, String status, msgType type, String text){
        this.sentBy = username;
        this.dateSent = originalDate;
        this.chatroomUID = chatroomID;
        if (status.toUpperCase().matches(msgStatus.SENT.toString())){
            currentMsgStatus = msgStatus.SENT;
        }
        else{
            currentMsgStatus = msgStatus.RECEIVED;
        }
        this.content = text;
        this.type = type;

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
