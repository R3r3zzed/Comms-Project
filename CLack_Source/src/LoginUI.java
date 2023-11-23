import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginUI implements GUI {
	Client client;
	private JFrame frame;
	private JPanel panel;
	private String username;
	private String password;
	private JLabel usernameLabel;
	private JLabel passwordLabel;
	private JTextField usernameInput;
	private JTextField passwordInput;
	private JButton submitButton;
	private Boolean isLoggedIn = false;
	int textFieldCharLimit = 20;
	
	@Override
	public void display(Client client) {
		frame = new JFrame("login screen");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new JPanel(new GridBagLayout());
		usernameLabel = new JLabel("username:");
		passwordLabel = new JLabel("password:");
		usernameInput = new JTextField(textFieldCharLimit);
		passwordInput = new JTextField(textFieldCharLimit);
		submitButton = new JButton("Submit");
		this.client = client;
		
		createUI();
	}
	
	private void createUI() {
		login();
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridy = 0;
		constraints.gridx = 0;
		panel.add(usernameLabel,constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		panel.add(passwordLabel, constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		panel.add(usernameInput, constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		panel.add(passwordInput, constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		panel.add(submitButton, constraints);
		
		frame.getRootPane().setDefaultButton(submitButton);
		frame.getContentPane().add(panel);
		frame.setSize(480, 360);
		frame.setLocale(null);
		frame.setVisible(true);
	}
	
	// opens a login frame that prompts the user for their username and password
	private void login() {
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				username = usernameInput.getText();
				password = passwordInput.getText();
				System.out.println("username: " + username + "\npassword: " + password);
				
				// isLoggedIn = client.login(username, password);							/* ADD THIS METHOD IN Client class*/
				
				if(isLoggedIn) {
					JOptionPane.showMessageDialog(frame, "Successfully logged in. Opening...");
				}
				else {
					JOptionPane.showMessageDialog(frame, "Login Failed: Incorrect username or password");
				}
			}
		});
	}
	
	/************************************** For Main Driver **************************************/
	
	public boolean isLoggedIn() {
		return isLoggedIn;
	}
	
	public void close() {
		frame.dispose();
	}
}
