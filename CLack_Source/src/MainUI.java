import javax.swing.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.Border;

import java.awt.BorderLayout;
import java.awt.Color;

import java.util.Vector;

public class MainUI implements GUI{
	private ClientFake client;
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
	
	public MainUI(ClientFake client) {
		this.client = client;
		currentUser = client.getCurrentUser();
	}
	
	@Override
	public void display() {
		frame = new JFrame("CLack");
		panel = new JPanel(new GridBagLayout());
		createChatRoomButton = new JButton("Create Chat Room");
		logoutButton = new JButton("Logout");
		viewLogsButton = new JButton("View Logs");
		userLabel = new JLabel(client.getCurrentUser().getName());
		directoryLabels = new Vector<JLabel>();
		directoryScrollPane = new JScrollPane();
		chatRoomButtons = new Vector<JButton>();
		chatScrollPane = new JScrollPane();
		createUI();

	}
	
	private void createUI() {
		createChatButtons();
		createDirectoryLabels();
		doUpdateChatScrollPane(null);
		doUpdateDirectoryScrollPane(null);
		placeComponents();
		frame.setSize(1280, 720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// might need to change to call client.close()
		frame.setVisible(true);
		frame.getContentPane().add(panel);
	}
	
	// tells the client to close. Closes sockets
	private void logout() {
		frame.setVisible(false);
		frame.dispose();
		client.close();
	}
	
	// handles setting up how buttons will placed on panel
	// does not handle functionality
	private void placeComponents() {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 0.5;
		constraints.weighty = 0.5;
		constraints.gridy = 0;
		constraints.gridx = 0;
		constraints.gridwidth = 1;
		userLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panel.add(userLabel,constraints);
		
		// display logs view button only if user is IT user
		if(currentUser.getUserType() == UserType.IT) {
			constraints.fill = GridBagConstraints.NONE;
			constraints.weightx = 0.5;
			constraints.weighty = 0.5;
			constraints.gridx = 1;
			constraints.gridy = 0;
			panel.add(viewLogsButton, constraints);
		}
		
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 0.5;
		constraints.weighty = 0.5;
		constraints.gridx = 2;
		constraints.gridy = 0;
		panel.add(createChatRoomButton, constraints);
		
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 0.5;
		constraints.weighty = 0.5;
		constraints.gridx = 3;
		constraints.gridy = 0;
		panel.add(logoutButton, constraints);
		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 3;
		directoryScrollPane.setColumnHeaderView(new JLabel("Directory", SwingConstants.CENTER));
		panel.add(directoryScrollPane, constraints);
		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.gridx = 3;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		chatScrollPane.setColumnHeaderView(new JLabel("Chat Rooms", SwingConstants.CENTER));
		panel.add(chatScrollPane, constraints);
	}
	
	// displays only the chat rooms that have the substring filter in the participant names
	// if "" then display everything
	private void doUpdateChatScrollPane(String filter) {
		JPanel scrollPanel = new JPanel();
		chatScrollPane.add(scrollPanel);
	}
	
	// displays only the users that have the substring filter in their names
	// if "" then display everything
	private void doUpdateDirectoryScrollPane(String filter) {
		if (filter == null) {
			filter = "";
		}
		
		// look through list of users. If user's name contains substring filter then create make JLabel visible for it
		for(int i = 0; i < client.getDirectory().size();i++) {
			if(client.getDirectory().elementAt(i).getName().contains(filter)) {
				directoryLabels.elementAt(i).setVisible(true);
			}
			else {
				directoryLabels.elementAt(i).setVisible(false);
			}
		}
		directoryScrollPane.updateUI();
	}
	
	// create JButtons for each chatroom
	private void createChatButtons() {

	}
	
	// create the Labels for each user in directory
	// displays the name of the user;
	private void createDirectoryLabels() {
		JPanel scrollPanel = new JPanel();
		scrollPanel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		
		for(int i = 0; i < client.getDirectory().size(); i++) {
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.weightx = 0.5;
			constraints.weighty = 0.5;
			constraints.gridy = i;
			constraints.gridx = 0;
			directoryLabels.add(new JLabel(client.getDirectory().elementAt(i).getName()));
			scrollPanel.add(directoryLabels.elementAt(i), constraints);
		}
		directoryScrollPane = new JScrollPane(scrollPanel);
	}
	
	private void doOpenChatRoom() {
		
	}
}
