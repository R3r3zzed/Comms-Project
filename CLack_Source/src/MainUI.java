import javax.swing.*;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Vector;

public class MainUI implements GUI{
	private Client client;
	private JFrame frame;
	private JPanel panel;
	private JScrollPane directoryScrollPane;
	
	private JTextField filterDirectoryTextField;
	private JButton filterDirectorySubmitButton;
	private Vector<JButton> selectUserButtons;
	private Vector<User> directory;
	private JScrollPane chatScrollPane;
	
	private JTextField filterChatRoomTextField;
	private JButton filterChatRoomSubmitButton;
	private Vector<JButton> chatRoomButtons;
	private Vector<ChatRoom> chatRooms;
	private JButton logoutButton;
	
	private JButton viewLogsButton;
	
	private User currentUser;
	private JLabel userLabel;
	private ChatUI chatUI;
	private LogsUI logsUI;
	
	@Override
	public void display(Client client) {
		this.client = client;
		frame = new JFrame("CLack");
		panel = new JPanel();
		createUI();

	}
	
	private void createUI() {
		frame.setVisible(true);
		frame.setSize(1280, 720);
	}
	
	// tells the client to close. Closes sockets
	private void logout() {
		client.close();
	}
	
	private void doFilterDirectory(String filter) {
		
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
