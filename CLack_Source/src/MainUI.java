import javax.swing.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.Color;
import java.awt.Dimension;
import java.util.StringTokenizer;
import java.util.Vector;

public class MainUI implements GUI{
	private Client client;
	private JFrame frame;
	private JPanel panel;
	private User currentUser;
	
	private JTextField filterDirectoryTextField;
	private Vector<JCheckBox> selectedUsers;
	private JButton filterDirectorySubmitButton;
	private JScrollPane directoryScrollPane;
	
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
	
	public MainUI(Client client) {
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
		doFilterChatScrollPane(null);
		doFilterDirectoryScrollPane(null);
		placePanelComponents();
		
		// open the LogsUI
		viewLogsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logsUI = new LogsUI(client);
				logsUI.display();
			}
		} );
		
		
		// create new chatroom, then open chatroom UI for it
		createChatRoomButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int countSelected = 0;
				StringTokenizer strtok = null;
				String chatID = "";
				String names = "";
				for(int i = 0; i < selectedUsers.size(); i++) {
					JCheckBox current = selectedUsers.get(i);
					if(current.isSelected()) {
						countSelected++;
						strtok = new StringTokenizer(current.getText());
						chatID += strtok.nextToken(":") + "-";
						names += strtok.nextToken(":") + ",";
					}
				}
				names += "\b";		// remove excess ","
				if (countSelected < 1) {
					JOptionPane.showMessageDialog(panel, "You must select at least one other user to create a Chat Room");
					return;
				}
				
				String prompt = "Create a room with the following participants?\n" + names;
				int isConfirmed = JOptionPane.showConfirmDialog(panel, prompt);
				if(isConfirmed != 0) {
					// unselect all selected Users
					for(int i = 0; i < selectedUsers.size(); i++) {
						selectedUsers.get(i).setSelected(false);
					}
					return;
				}
				
				chatID = chatID.trim();		// remove all whitespace
				
				Vector<User> participants = new Vector<User>();
				
				// parse the input
				strtok = new StringTokenizer(chatID);
				boolean isCurrentUserAdded = false;
				
				while (strtok.hasMoreTokens()) {
					String userID = strtok.nextToken("-");
					int otherID = -1;
					try {
						otherID = Integer.parseInt(userID);
						if(otherID == Integer.parseInt(currentUser.getUserID())) {
							participants.add(currentUser);
							isCurrentUserAdded = true;
							continue;
						}
						if(isCurrentUserAdded == false && otherID > Integer.parseInt(currentUser.getUserID())) {
							participants.add(currentUser);
							isCurrentUserAdded = true;
						}
						participants.add(client.getDirectory().get(otherID));
					}
					catch(NumberFormatException exc) {
						String errorMessage = "Your input must only be valid user IDs: e.g. 1-5-10-2";
						errorMessage += "\n" + userID + " is not a valid user's ID";
						JOptionPane.showMessageDialog(panel, errorMessage);
						return;
					}
					catch(ArrayIndexOutOfBoundsException exc) {
						String errorMessage = "Your input must only be valid user IDs: e.g. 1-5-10-2";
						errorMessage += "\n" + Integer.toString(otherID) + " is not a valid user's ID";
						JOptionPane.showMessageDialog(panel, errorMessage);
						return;
					}
				}

				if (isCurrentUserAdded == false) {
					participants.add(currentUser);
				}
				
				// Check if any user was selected
				// if vector is empty don't create a chatroom and display error message
				if (participants.size() <= 1) {
					String errorMessage = "Follow the format or make sure that at least one of the user ID is valid";
					JOptionPane.showMessageDialog(panel, errorMessage);
					return;
				}
				
				chatID = "";
				for(int i = 0; i < participants.size(); i++) {
					chatID += participants.elementAt(i).getUserID();
					if(i != participants.size() - 1) {
						chatID += "-";
					}
				}
				
				chatUI = new ChatUI(client, client.openChatRoom(chatID), false);
				chatUI.display();
				// unselect all selected Users
				for(int i = 0; i < selectedUsers.size(); i++) {
					selectedUsers.get(i).setSelected(false);
				}
			}
		});
		
		
		// stop the program
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client.sendMessageToServer( new Message(currentUser.getUsername(), null, "", msgStatus.SENT, msgType.LOGOUT, ""));
				close();
			}
		});
		
		// filter directory based on filter input by user
		filterDirectorySubmitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doFilterDirectoryScrollPane(filterDirectoryTextField.getText());
			}
		});
				
		// filter chatrooms based on filter input by user
		filterChatRoomSubmitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doFilterChatScrollPane(filterChatRoomTextField.getText());
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
		
		selectedUsers = new Vector<JCheckBox>();
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
		chatRoomButtons.clear();
		JPanel scrollPanel = new JPanel();
		scrollPanel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		 
		for(int i = 0; i < client.getChatRooms().size(); i++) {
			ChatRoom currentChatRoom = client.getChatRooms().elementAt(i);
			constraints.fill = GridBagConstraints.BOTH;
			constraints.weightx = 0.5;
			constraints.weighty = 0.5;
			constraints.gridy = i;
			constraints.gridx = 0;
			
			// Set chatroom button name
			// name of all the users in the 
			String participants = "";
			participants += currentChatRoom.getChatID() + ":";
			for(int j = 0; j < currentChatRoom.getUsers().size(); j++) {
				participants += currentChatRoom.getUsers().elementAt(j).getName();
				if(j != currentChatRoom.getUsers().size() - 1) {
					participants += ", ";
				}
			}
			chatRoomButtons.add(new JButton(participants));
			chatRoomButtons.elementAt(i).setPreferredSize(new Dimension(directoryScrollPane.getWidth(),frame.getHeight()/10));
			scrollPanel.add(chatRoomButtons.elementAt(i), constraints);
		}

		for(int i = 0; i < chatRoomButtons.size(); i++) {
			chatRoomButtons.get(i).addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JButton eventSource = (JButton) e.getSource();
					StringTokenizer strtok = new StringTokenizer(eventSource.getText());
					String id = strtok.nextToken(":");
					chatUI = new ChatUI(client, client.openChatRoom(id), false);
					chatUI.display();
				}
			});
		}
		chatScrollPane = new JScrollPane(scrollPanel);
	}
		
	// displays only the chat rooms that have the substring filter in the participant names
	// if "" then display everything
	private void doFilterChatScrollPane(String filter) {
		if (filter == null) {
			filter = "";
		}
		
		// look through list of participants. If any of the users' name contains substring filter then make JButton visible for it
		for(int i = 0; i < chatRoomButtons.size();i++) {
			String participants =  chatRoomButtons.elementAt(i).getText();
				if(participants.contains(filter)) {
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
		selectedUsers.clear();
		JPanel scrollPanel = new JPanel();
		scrollPanel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		
		for(int i = 0; i < client.getDirectory().size(); i++) {
			constraints.fill = GridBagConstraints.BOTH;
			constraints.weightx = 0.5;
			constraints.weighty = 0.5;
			constraints.gridy = i;
			constraints.gridx = 0;
			User user = client.getDirectory().elementAt(i);
			selectedUsers.add(new JCheckBox(user.getUserID() + ":" + user.getName()));
			selectedUsers.elementAt(i).setBorder(BorderFactory.createLineBorder(Color.BLACK));
			selectedUsers.elementAt(i).setPreferredSize(new Dimension(directoryScrollPane.getWidth(),frame.getHeight()/10));
			scrollPanel.add(selectedUsers.elementAt(i), constraints);
		}
		directoryScrollPane = new JScrollPane(scrollPanel);
	}
	
	// displays only the users that have the substring filter in their names
	// if "" then display everything
	private void doFilterDirectoryScrollPane(String filter) {
		if (filter == null) {
			filter = "";
		}
		
		// look through list of users. If user's name contains substring filter then create make JLabel visible for it
		for(int i = 0; i < client.getDirectory().size();i++) {
			if(client.getDirectory().elementAt(i).getName().toUpperCase().contains(filter.toUpperCase())) {
				selectedUsers.elementAt(i).setVisible(true);
			}
			else {
				selectedUsers.elementAt(i).setVisible(false);
			}
		}
		directoryScrollPane.updateUI();
	}
	
	public void updateChatScrollPane() {
		createChatButtons();
		panel.removeAll();
		placePanelComponents();
		panel.updateUI();
	}
	
	// tells the client to close. Closes sockets
	public void close() {
		frame.setVisible(false);
		frame.dispose();
		client.close();
	}
	
	 public ChatUI getChatUI() {
		 return chatUI;
	 } 
}
