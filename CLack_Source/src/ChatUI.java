import javax.swing.*;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.util.ArrayList;

public class ChatUI implements GUI {
	private JFrame frame;
	private ArrayList<User> participants;
	private ArrayList<User> messages;
	private JScrollPane participantScrollPane;
	private ArrayList<JLabel> participantLabels;
	private JScrollPane messageScrollPane;
	private ArrayList<JLabel> messageLabels;
	private JTextField messageTextField;
	private JButton sendMessageButton;
	
	@Override
	public void display() {
		// TODO Auto-generated method stub

	}
	
	private void createUI(boolean isAdmin) {
		
	}
	
	private void doSendMessage(String text) {
		
	}
	
	private void updateMessageScrollPane() {
		
	}
	
	private void updateParticipantScrollPane() {
		
	}
}
