import javax.swing.*;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainUI implements GUI {
	private JFrame frame;
	private JScrollPane directoryScrollPane;
	private ArrayList<JButton> selectUserButtons;
	private JScrollPane chatScrollPane;
	private ArrayList<JButton> chatRoomButtons;
	private JButton logoutButton;
	private JButton viewLogsButton;
	private JTextField filterDirectoryTextField;
	private User currentUser;
	private JLabel userLabel;
	private ChatUI chatUI;
	private LogsUI logsUI;
	
	// to be erased, simply for debugging
	public void tempFunction() {
		// initializes some of the variables for testing 
	}
	
	@Override
	public void display(Client client) {
		/*
		 * initialize client
		 * client = new Client;
		 */
		
		createUI();

	}
	
	private void createUI() {
		
	}
	
	private void logout() {
		
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
