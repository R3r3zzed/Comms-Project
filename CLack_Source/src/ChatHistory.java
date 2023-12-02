package Implementation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class ChatHistory {
	private String chatRoomFilename;

	public ChatHistory(String newChatroomFileName) {
		setChatHistoryFile(newChatroomFileName);
	}

	public void updateChatRoomFile(Message m) {
		try (FileWriter f = new FileWriter(this.chatRoomFilename, true);
				BufferedWriter b = new BufferedWriter(f);
				PrintWriter p = new PrintWriter(b);) {
			p.println(String.format("%s;;;%s;;;%s;;;%s;;;%s", m.getSendBy(), m.getDateSent().toString(),
					m.getChatroomID(), m.getMsgStatus().toString(), m.getContent()));
		} catch (IOException i) {
			i.printStackTrace();
		}

	}

	public Message[] loadChatRoomFiles() {
		Vector<Message> messageVector = new Vector<>();
		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy"); // Adjust this format to
																							// match the one used in
																							// your chat history file.

		try (BufferedReader br = new BufferedReader(new FileReader(chatRoomFilename))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(";;;");
				if (parts.length == 5) {
					// Assuming the order is: sentBy, dateSent, chatroomID, status, content
					String sentBy = parts[0];
					java.util.Date dateSent = formatter.parse(parts[1]);
					String chatroomID = parts[2];
					String status = parts[3];
					String content = parts[4];

					Message message = new Message(sentBy, dateSent, chatroomID, status, msgType.TEXT, content);
					messageVector.add(message);
				}
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			// Depending on your error handling policy, you may want to re-throw, log, or
			// handle this exception.
		}

		// Convert the Vector to an array and return it
		return messageVector.toArray(new Message[0]);
	}

	private void setChatHistoryFile(String newFile) {
		this.chatRoomFilename = newFile;
	}

	public String getChatHistoryFile() {
		return this.chatRoomFilename;
	}

}
