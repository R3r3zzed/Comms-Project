import javax.swing.*;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.util.ArrayList;


public class LogsUI implements GUI {
	private Client client;
	private JFrame frame;
	private JScrollPane directoryScrollPane;
	private ArrayList<JButton> selectUserButtons;
	private JScrollPane chatScrollPane;
	private ArrayList<JButton> chatRoomButtons;
	private JTextField filterDirectoryTextField;	// might need to change
	private User selectedUser;
	private JLabel userLabel;
	private ChatUI chatUI;
	
	public LogsUI(Client client) {
		this.client = client;
	}
	
	@Override
	public void display() {
		// TODO Auto-generated method stub
		
	}

	private void createUI() {
		
	}
	
	private void doFilterDirectory(String filter) {
		
	}
	
	private void doFilterChatRooms(String filter) {
		
	}
	
	private void doUpdateDirectoryScrollPane() {
		
	}
	
	private void doOpenChatRoom() {
		
	}
}
