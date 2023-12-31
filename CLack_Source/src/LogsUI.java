import javax.swing.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;
import java.util.Vector;


public class LogsUI implements GUI{
	private Client client;
	private JFrame frame;
	private JPanel panel;
	private User selectedUser;
	
	private JTextField filterDirectoryTextField;
	private JButton filterDirectorySubmitButton;
	private JScrollPane directoryScrollPane;
	private Vector<JButton> directoryButtons;
	
	private Vector<ChatRoom> chatRooms;
	private JTextField filterChatRoomTextField;
	private JButton filterChatRoomSubmitButton;
	private Vector<JButton> chatRoomButtons;
	private JScrollPane chatScrollPane;
	
	private JLabel userLabel;
	
	public LogsUI(Client client) {
		this.client = client;
		selectedUser = client.getCurrentUser();
		chatRooms = client.getChatRooms();
	}
	
	@Override
	public void display() {
		frame = new JFrame("CLack LOGS");
		panel = new JPanel(new GridBagLayout());
		createUI();

	}
	
	private void createUI() {
		frame.setSize(1280, 720);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	// might need to change to call client.close()
		createPanelComponents();
		createChatButtons();
		createDirectoryButtons();
		doFilterChatScrollPane(null);
		doFilterDirectoryScrollPane(null);
		placePanelComponents();
		
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
		panel.updateUI();
	}
	
	private void createPanelComponents(){
		userLabel = new JLabel(selectedUser.getName(), SwingConstants.CENTER);
		
		filterChatRoomTextField = new JTextField();
		filterChatRoomSubmitButton = new JButton("filter ChatRooms");
		filterDirectoryTextField = new JTextField();
		filterDirectorySubmitButton = new JButton("filter Directory");
		
		directoryButtons = new Vector<JButton>();
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
		
		for(int i = 0; i < chatRooms.size(); i++) {
			ChatRoom currentChatRoom = chatRooms.elementAt(i);
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
					ChatUI chatUI = new ChatUI(client, client.openChatRoom(id), true);	// TODO needs to be implemented on client side
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
	private void createDirectoryButtons() {
		directoryButtons.clear();
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
			directoryButtons.add(new JButton(user.getUserID() + ":" + user.getName()));
			directoryButtons.elementAt(i).setPreferredSize(new Dimension(directoryScrollPane.getWidth(),frame.getHeight()/10));
			scrollPanel.add(directoryButtons.elementAt(i), constraints);
		}
		
		for(int i = 0; i < directoryButtons.size(); i++) {
			// ask user for confirmation if they want logs for selected user
			// if yes. Then load up the chatrooms for selected user
			directoryButtons.elementAt(i).addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String label = ((JButton) e.getSource()).getText();
					int isConfirmed = JOptionPane.showConfirmDialog(scrollPanel, "Get logs for " + label + "?");
					if (isConfirmed != 0) {
						return;
					}
					
					// get the user ID from the label
					StringTokenizer strtok = new StringTokenizer(label);
					String id = strtok.nextToken(":");
					User newSelectedUser = null;
					for(int i = 0; i < client.getDirectory().size(); i++) {
						if (client.getDirectory().elementAt(i).getUserID().compareToIgnoreCase(id) == 0) {
							newSelectedUser = client.getDirectory().elementAt(i);
							break;
						}
					}
					
					if (newSelectedUser == selectedUser) {
						return;	// user already selected
					}
					
					selectedUser = newSelectedUser;
					userLabel.setText(selectedUser.getName());
					chatRooms = client.getLogs(selectedUser.getUsername());
					createChatButtons();
					panel.removeAll();
					placePanelComponents();
					panel.updateUI();
				}
			});
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
				directoryButtons.elementAt(i).setVisible(true);
			}
			else {
				directoryButtons.elementAt(i).setVisible(false);
			}
		}
		directoryScrollPane.updateUI();
	}
}
