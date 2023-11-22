import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
	private String sentBy; //username
	private Date dateSent;
	private String chatroomUID; //the chatroom ID
	private msgStatus currentMsgStatus;
	private String content;
    
    public Message(String username, Date originalDate, String chatroomID, String status ,String text){
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

    public Date getDateSent(){
        return dateSent;
    }
    public String getChatroomID(){
        return this.chatroomUID;
    }

}
