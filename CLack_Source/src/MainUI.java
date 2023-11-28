import javax.swing.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Vector;

public class MainUI implements GUI{
	private Client client;
	private JFrame frame;
	private JPanel panel;
	private User currentUser;
	
	private JTextField filterDirectoryTextField;
	private JButton filterDirectorySubmitButton;
	private JScrollPane directoryScrollPane;
	private Vector<JLabel> directoryLabels;
	private Vector<User> directory;
	
	private JTextField filterChatRoomTextField;
	private JButton filterChatRoomSubmitButton;
	private Vector<JButton> chatRoomButtons;
	private Vector<ChatRoom> chatRooms;
	private JScrollPane chatScrollPane;
	
	private JButton createChatRoomButton;
	private JButton logoutButton;
	private JButton viewLogsButton;
	private JLabel userLabel;
	
	private ChatUI chatUI;
	private LogsUI logsUI;
	
	@Override
	public void display(Client client) {
		this.client = client;
		// currentUser = client.getCurrentUser;		// needs to be implemented in Client class
		frame = new JFrame("CLack");
		panel = new JPanel();
		createUI();

	}
	
	private void createUI() {
		placeComponents();
		frame.setSize(1280, 720);
		frame.setVisible(true);
	}
	
	// tells the client to close. Closes sockets
	private void logout() {
		client.close();
		frame.setVisible(false);
		frame.dispose();
	}
	
	// handles setting up how buttons will placed on panel
	private void placeComponents() {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridy = 0;
		constraints.gridx = 0;
		panel.add(userLabel,constraints);
		
		// display logs view button only if user is IT user
		if(currentUser.getUserType() == UserType.IT) {
			constraints.gridx = 1;
			constraints.gridy = 0;
			panel.add(viewLogsButton, constraints);
		}
		
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		panel.add(createChatRoomButton, constraints);
		
		constraints.gridx = 3;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		panel.add(logoutButton, constraints);
		
		constraints.fill = constraints.VERTICAL;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		panel.add(directoryScrollPane, constraints);
		
		constraints.fill = constraints.VERTICAL;
		constraints.gridx = 2;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		panel.add(chatScrollPane, constraints);
	}
	
	private void doFilterDirectory(String filter) {
		if (filter.compareTo(filter) == 0 || filter == null) {
			
		}
	}
	
	private void doFilterChatRooms(String filter) {
		
	}
	
	private void doUpdateChatScrollPane() {
		
	}
	
	private void doUpdateDirectoryScrollPane() {
		
	}
	
	private void doOpenChatRoom() {
		
	}
}
