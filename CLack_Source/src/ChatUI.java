import javax.swing.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Vector;

public class ChatUI implements GUI {
	private boolean viewAsLogs;
	
	private ChatRoom chatRoom;
	private JFrame frame;
	private JPanel panel;
	
	private Vector<User> participants;
	private JScrollPane participantScrollPane;
	private Vector<JLabel> participantLabels;
	private JPanel participantPanel;
	
	private Vector<Message> messages;
	private JScrollPane messageScrollPane;
	private Vector<JLabel> messageLabels;
	private JPanel messagePanel;
	
	private JScrollPane sendMessageTextAreaScrollPane;
	private JTextArea sendMessageTextArea;
	private JButton sendMessageButton;
	
	// TODO add ChatRoom chatRoom, boolean viewAsLogs arguments
	public ChatUI() {
		// this.chatRoom = chatRoom;
		// this.viewAsLogs = viewAsLogs;
		viewAsLogs = false;
	}
	
	public void display() {
		//frame = new JFrame(chatRoom.getChatID());	//TODO uncomment
		frame = new JFrame("Chat Room");			//TODO remove
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(1280,720);
		panel = new JPanel(new GridBagLayout());
		// participants = chatRoom.getUsers();		//TODO remove
		//messages = chatRoom.getHistory();			//TODO remove
		
		createUI();
	}
	
	private void createUI() {
		createPanelComponents();
		createMessagesScrollPaneComponents();
		createParticipantScrollPaneComponents();
		placePanelComponents();
		
		sendMessageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// if only whitespace, don't send
				if (sendMessageTextArea.getText().trim().length() == 0) {
					return;
				}
				JOptionPane.showMessageDialog(frame, sendMessageTextArea.getText());
				
				/* TODO uncomment for final. ChatRoom.sendMessage needs to be implemented
				 * chatRoom.sendMessage(messageTextArea.getText());
				 */
				sendMessageTextArea.setText(null);
			}
		});
		frame.getRootPane().setDefaultButton(sendMessageButton);
		frame.setVisible(true);
		frame.getContentPane().add(panel);
		panel.updateUI();
	}
	
	private void createPanelComponents() {
		participantScrollPane = new JScrollPane();
		participantPanel = new JPanel(new GridBagLayout());
		
		messageScrollPane = new JScrollPane();
		messagePanel = new JPanel(new GridBagLayout());
		
		sendMessageTextArea = new JTextArea();
		sendMessageTextAreaScrollPane = new JScrollPane();
		sendMessageButton = new JButton("Send Message");
	}
	
	private void placePanelComponents() {
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 3;
		constraints.weightx = 0.8;
		constraints.weighty = 1;
		messageScrollPane.setColumnHeaderView(new JLabel("Messages", SwingConstants.CENTER));
		messageScrollPane.setPreferredSize(new Dimension(frame.getWidth() * 3/4, frame.getHeight() * 4/5));
		panel.add(messageScrollPane, constraints);
		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 3;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.weightx = 0.2;
		constraints.weighty = 1;
		participantScrollPane.setColumnHeaderView(new JLabel("Members", SwingConstants.CENTER));
		participantScrollPane.setPreferredSize(new Dimension(frame.getWidth() * 1/4, frame.getHeight() * 4/5));
		panel.add(participantScrollPane, constraints);
		
		if (!viewAsLogs) {
			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx = 0;
			constraints.gridy = 1;
			constraints.gridwidth = 3;
			constraints.weightx = 0.8;
			constraints.weighty = 0.2;
			sendMessageTextArea.setWrapStyleWord(true);
			sendMessageTextArea.setLineWrap(true);
			sendMessageTextAreaScrollPane = new JScrollPane(sendMessageTextArea);
			sendMessageTextAreaScrollPane.setPreferredSize(new Dimension(frame.getWidth() * 3/4, frame.getHeight() * 1/5));
			panel.add(sendMessageTextAreaScrollPane, constraints);
			
			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx = 3;
			constraints.gridy = 1;
			constraints.gridwidth = 1;
			constraints.weightx = 0.2;
			constraints.weighty = 0.2;
			sendMessageButton.setPreferredSize(new Dimension(frame.getWidth() * 1/4, frame.getHeight() * 1/5));
			panel.add(sendMessageButton, constraints);
		}
	}
	
	// creates the Labels for the messages
	// follows the format
	// Sent By : <name of sender>
	// Date Sent: <date>
	// <Content>
	private void createMessagesScrollPaneComponents() {
		GridBagConstraints constraints = new GridBagConstraints();
		/*	TODO uncomment
		for (int i = 0; i < chatRoom.getHistory().size(); i++) {
			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx = 0;
			constraints.gridy = i;
			constraints.weightx = 0.5;
			constraints.weighty = 0.5;
			Message currentMessage = chatRoom.getHistory().elementAt(i);
			String labelText = String.format("<html> Sent By: %s <br> Date sent %s <br> %s </html>"
					, currentMessage.getSendBy(), currentMessage.getDateSent(), currentMessage.getContent());
			JLabel messageLabel = new JLabel(labelText);
			messageLabel.setPreferredSize(new Dimension(participantScrollPane.getWidth(), frame.getHeight() / 5));
			messageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			messagePanel.add(participantLabel, constraints);
		}
		*/
		// TODO Remove following for loop block
		for (int i = 0; i < 30; i++) {
			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx = 0;
			constraints.gridy = i;
			constraints.weightx = 0.5;
			constraints.weighty = 0.5;
			String sentBy = "name" + i;
			String content = "Hello WORLD Hello WORLDHello WORLDHello WORLDHello WORLDHello WORLDHello WORLDHello WORLDHello WORLDHello WORLDHello WORLDHello WORLDHello WORLDHello WORLDHello WORLDHello WORLDHello WORLDHello WORLDHello WORLDHello WORLDHello WORLDHello WORLD" + i;
			String labelText = String.format("<html> Sent By: %s <br> Date sent %s <br> %s </html>"
					, sentBy, new Date(), content);
			JLabel messageLabel = new JLabel(labelText);
			messageLabel.setPreferredSize(new Dimension(messageScrollPane.getWidth(), frame.getHeight() / 5));
			messageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			messagePanel.add(messageLabel, constraints);
		}
		messageScrollPane = new JScrollPane(messagePanel);
	}
	
	public void updateMessagesScrollPane() {
		messagePanel.removeAll();
		createMessagesScrollPaneComponents();
		panel.updateUI();
	}
	
	// create labels for the participants
	// the labels displays only the name of the participants
	private void createParticipantScrollPaneComponents() {
		GridBagConstraints constraints = new GridBagConstraints();
		/*	TODO uncomment
		for (int i = 0; i < chatRoom.getUsers().size(); i++) {
			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx = 0;
			constraints.gridy = i;
			constraints.weightx = 0.5;
			constraints.weighty = 0.5;
			JLabel participantLabel = new JLabel(chatRoom.getUsers().elementAt(i).getName());
			participantLabel.setPreferredSize(new Dimension(participantScrollPane.getWidth(), frame.getHeight() / 10));
			participantPanel.add(participantLabel, constraints);
		}
		*/
		// TODO remove for loop block
		for (int i = 0; i < 30; i++) {
			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx = 0;
			constraints.gridy = i;
			constraints.weightx = 0.5;
			constraints.weighty = 0.5;
			JLabel participantLabel = new JLabel("participant" + i);
			participantLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			participantLabel.setPreferredSize(new Dimension(participantScrollPane.getWidth(), frame.getHeight() / 10));
			participantPanel.add(participantLabel, constraints);
		}
		
		participantScrollPane = new JScrollPane(participantPanel);
		
	}
	
	public void updateParticipantScrollPane() {
		participantPanel.removeAll();
		createParticipantScrollPaneComponents();
		panel.updateUI();
	}
}
