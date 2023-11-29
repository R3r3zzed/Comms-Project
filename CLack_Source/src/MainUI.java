import javax.swing.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.Color;
import java.awt.Dimension;
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
	
	private JTextField filterChatRoomTextField;
	private JButton filterChatRoomSubmitButton;
	private Vector<JButton> chatRoomButtons;
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
		createUI();

	}
	
	private void createUI() {
		frame.setSize(1280, 720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// might need to change to call client.close()
		createPanelComponents();
		createChatButtons();
		createDirectoryLabels();
		doUpdateChatScrollPane(null);
		doUpdateDirectoryScrollPane(null);
		placePanelComponents();
		
		// open the LogsUI
		viewLogsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logsUI = new LogsUI(client);
				logsUI.display();
			}
		} );
		
		// TODO needs to be implemented on client side
		/*
		// create new chatroom, then open chatroom UI for it
		createChatRoomButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client.createChatRoom()
			}
		});
		*/
		
		// stop the program
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		
		// filter directory based on filter input by user
		filterDirectorySubmitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doUpdateDirectoryScrollPane(filterDirectoryTextField.getText());
			}
		});
				
		// filter chatrooms based on filter input by user
		filterChatRoomSubmitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doUpdateChatScrollPane(filterChatRoomTextField.getText());
			}
		});
				
		frame.setVisible(true);
		frame.getContentPane().add(panel);
	}
	
	private void createPanelComponents(){
		userLabel = new JLabel(client.getCurrentUser().getName(), SwingConstants.CENTER);
		viewLogsButton = new JButton("View Logs");
		createChatRoomButton = new JButton("Create Chat Room");
		logoutButton = new JButton("Logout");
		
		filterChatRoomTextField = new JTextField();
		filterChatRoomSubmitButton = new JButton("filter ChatRooms");
		filterDirectoryTextField = new JTextField();
		filterDirectorySubmitButton = new JButton("filter Directory");
		
		directoryLabels = new Vector<JLabel>();
		directoryScrollPane = new JScrollPane();
		chatRoomButtons = new Vector<JButton>();
		chatScrollPane = new JScrollPane();
	}
	
	// handles setting up how buttons will placed on panel
	// does not handle functionality
	private void placePanelComponents() {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1;
		constraints.weighty = 0.1;
		constraints.gridy = 0;
		constraints.gridx = 0;
		constraints.gridwidth = 1;
		userLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panel.add(userLabel,constraints);
		
		// display logs view button only if user is IT user
		if(currentUser.getUserType() == UserType.IT) {
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.weightx = 0.9;
			constraints.weighty = 0.1;
			constraints.gridx = 1;
			constraints.gridy = 0;
			panel.add(viewLogsButton, constraints);
		}
		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0.9;
		constraints.weighty = 0.1;
		constraints.gridx = 2;
		constraints.gridy = 0;
		panel.add(createChatRoomButton, constraints);
		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0.9;
		constraints.weighty = 0.1;
		constraints.gridx = 3;
		constraints.gridy = 0;
		panel.add(logoutButton, constraints);
		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1;
		constraints.weighty = 0.1;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		panel.add(filterDirectoryTextField, constraints);
		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1;
		constraints.weighty = 0.1;
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		panel.add(filterDirectorySubmitButton, constraints);
		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1;
		constraints.weighty = 0.1;
		constraints.gridx = 2;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		panel.add(filterChatRoomTextField, constraints);
		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1;
		constraints.weighty = 0.1;
		constraints.gridx = 3;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		panel.add(filterChatRoomSubmitButton, constraints);
		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		directoryScrollPane.setColumnHeaderView(new JLabel("Directory", SwingConstants.CENTER));
		panel.add(directoryScrollPane, constraints);
		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.gridx = 2;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		chatScrollPane.setColumnHeaderView(new JLabel("Chat Rooms", SwingConstants.CENTER));
		panel.add(chatScrollPane, constraints);
	}
	
	// create JButtons for each chatroom
	private void createChatButtons() {
		JPanel scrollPanel = new JPanel();
		scrollPanel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		
		/* TODO waiting for client side implementation 
		for(int i = 0; i < client.getChatRooms().size(); i++) {
			constraints.fill = GridBagConstraints.BOTH;
			constraints.weightx = 0.5;
			constraints.weighty = 0.5;
			constraints.gridy = i;
			constraints.gridx = 0;
			chatRoomButtons.add(new JButton("Button: " + i));
			chatRoomButtons.elementAt(i).setPreferredSize(new Dimension(directoryScrollPane.getWidth(),frame.getHeight()/10));
			scrollPanel.add(chatRoomButtons.elementAt(i), constraints);
		}
		*/
		for(int i = 0; i < 10; i++) {
			constraints.fill = GridBagConstraints.BOTH;
			constraints.weightx = 0.5;
			constraints.weighty = 0.5;
			constraints.gridy = i;
			constraints.gridx = 0;
			chatRoomButtons.add(new JButton("Button: " + i));
			chatRoomButtons.elementAt(i).setPreferredSize(new Dimension(directoryScrollPane.getWidth(),frame.getHeight()/10));
			scrollPanel.add(chatRoomButtons.elementAt(i), constraints);
		}
		for(int i = 0; i < chatRoomButtons.size(); i++) {
			chatRoomButtons.get(i).addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(scrollPanel, ((JButton)e.getSource()).getText());
				}
			});
		}
		
		chatScrollPane = new JScrollPane(scrollPanel);
	}
		
	// displays only the chat rooms that have the substring filter in the participant names
	// if "" then display everything
	private void doUpdateChatScrollPane(String filter) {
		if (filter == null) {
			filter = "";
		}
		
		/* TODO Wait for Chatroom to implemented then uncomment
		// look through list of participants. If any of the users' name contains substring filter then make JButton visible for it
		for(int i = 0; i < client.getChatRooms().size();i++) {
			Vector<User> participants = client.getChatRooms().elementAt(i).getParticipants();		// TODO Needs to be implemented
			for(int j = 0; j < participants.size(); j++) {
				if(participants.elementAt(i).getName().contains(filter)) {
					chatRoomButtons.elementAt(i).setVisible(true);
				}
				else {
					chatRoomButtons.elementAt(i).setVisible(false);
				}
			}
		}
		*/
		
		// TODO remove for final
		for(int i = 0; i < chatRoomButtons.size(); i++) {
			if (chatRoomButtons.elementAt(i).getText().contains(filter)) {
				chatRoomButtons.elementAt(i).setVisible(true);
			}
			else {
				chatRoomButtons.elementAt(i).setVisible(false);
			}
		}
		
		chatScrollPane.updateUI();
	}
	
	// create the Labels for each user in directory
	// displays the name of the user;
	private void createDirectoryLabels() {
		JPanel scrollPanel = new JPanel();
		scrollPanel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		
		for(int i = 0; i < client.getDirectory().size(); i++) {
			constraints.fill = GridBagConstraints.BOTH;
			constraints.weightx = 0.5;
			constraints.weighty = 0.5;
			constraints.gridy = i;
			constraints.gridx = 0;
			directoryLabels.add(new JLabel(client.getDirectory().elementAt(i).getName()));
			directoryLabels.elementAt(i).setBorder(BorderFactory.createLineBorder(Color.BLACK));
			directoryLabels.elementAt(i).setPreferredSize(new Dimension(directoryScrollPane.getWidth(),frame.getHeight()/10));
			scrollPanel.add(directoryLabels.elementAt(i), constraints);
		}
		directoryScrollPane = new JScrollPane(scrollPanel);
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
	
	// tells the client to close. Closes sockets
	public void close() {
		frame.setVisible(false);
		frame.dispose();
		client.close();
	}
}
