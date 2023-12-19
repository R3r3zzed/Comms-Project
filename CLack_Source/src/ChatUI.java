import javax.swing.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class ChatUI implements GUI {
	private boolean viewAsLogs;
	
	private Client client;
	private ChatRoom chatRoom;
	private JFrame frame;
	private JPanel panel;
	
	private JScrollPane participantScrollPane;
	private JPanel participantPanel;
	
	private JScrollPane messageScrollPane;
	private JPanel messagePanel;
	
	private JScrollPane sendMessageTextAreaScrollPane;
	private JTextArea sendMessageTextArea;
	private JButton sendMessageButton;
	
	public ChatUI(Client client, ChatRoom chatRoom, boolean viewAsLogs) {
		this.client = client;
		this.chatRoom = chatRoom;
		this.viewAsLogs = viewAsLogs;
	}
	
	public void display() {
		frame = new JFrame("ChatRoom " + chatRoom.getChatID());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(1280,720);
		panel = new JPanel(new GridBagLayout());
		
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
				Message message = new Message(client.getCurrentUser().getUsername(), new Date(),
						chatRoom.getChatID(), msgStatus.SENT, msgType.TEXT, sendMessageTextArea.getText());
				client.updateMessage(message);
				sendMessageTextArea.setText("");
			}
		});
		frame.getRootPane().setDefaultButton(sendMessageButton);
		frame.setVisible(true);
		frame.getContentPane().add(panel);
		panel.updateUI();
		
		messageScrollPane.validate();
		JScrollBar scrollBar = messageScrollPane.getVerticalScrollBar();
		scrollBar.setValue((int)messagePanel.getSize().getHeight());
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
		
		messageScrollPane.validate();
		JScrollBar scrollBar = messageScrollPane.getVerticalScrollBar();
		scrollBar.setValue((int)messagePanel.getSize().getHeight());
	}
	
	// creates the Labels for the messages
	// follows the format
	// Sent By : <name of sender>
	// Date Sent: <date>
	// <Content>
	private void createMessagesScrollPaneComponents() {
		GridBagConstraints constraints = new GridBagConstraints();
		
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
			messagePanel.add(messageLabel, constraints);
		}
		
		messageScrollPane = new JScrollPane(messagePanel);
	}
	
	public void updateMessagesScrollPane() {
		messagePanel.removeAll();
		createMessagesScrollPaneComponents();
		panel.removeAll();
		placePanelComponents();
		panel.updateUI();
	}
	
	// create labels for the participants
	// the labels displays only the name of the participants
	private void createParticipantScrollPaneComponents() {
		GridBagConstraints constraints = new GridBagConstraints();
		
		for (int i = 0; i < chatRoom.getUsers().size(); i++) {
			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx = 0;
			constraints.gridy = i;
			constraints.weightx = 0.5;
			constraints.weighty = 0.5;
			User participant = chatRoom.getUsers().elementAt(i);
			JLabel participantLabel = new JLabel(participant.getUserID() + ":" + participant.getName());
			participantLabel.setPreferredSize(new Dimension(participantScrollPane.getWidth(), frame.getHeight() / 10));
			participantPanel.add(participantLabel, constraints);
		}
		
		participantScrollPane = new JScrollPane(participantPanel);
	}
	
	public void updateParticipantScrollPane() {
		participantPanel.removeAll();
		createParticipantScrollPaneComponents();
		panel.removeAll();
		placePanelComponents();
		panel.updateUI();
	}
}
