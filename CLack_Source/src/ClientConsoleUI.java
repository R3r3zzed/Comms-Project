import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;

public class ClientConsoleUI {
    
    public static void main(String[] args) {
        System.out.println("Welcome to CLack:");
        // Client client = new Client("127.0.0.1", 1235);
        Scanner myObj = new Scanner(System.in);

        User currentUser;

        try (Socket s = new Socket("10.0.0.151", 1235)) {
            ObjectOutputStream output = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(s.getInputStream());
            DataInputStream dataOutputStream = new DataInputStream(new BufferedInputStream(s.getInputStream()));

            while(true){
                System.out.print("Enter in a username: ");
                String userName = myObj.nextLine();
                System.out.print("Enter in a password: ");
                String password = myObj.nextLine();
                //log in
                String auth = String.format("%s;;;%s", userName, password);
                output.writeObject(auth);
                output.flush();

                System.out.println("Waiting on Server...");
                Boolean confirmation = (Boolean) input.readBoolean();
                // Thread.sleep(40);
                if (confirmation){
                    currentUser = (User) input.readObject();
                    
                    break;
                }
                else{
                    System.out.println("Error with confirmation, try again");
                }
            }

            receiveNewMessageClass newMessageClass = new receiveNewMessageClass(input);
            Thread newMessageThread = new Thread(newMessageClass);
            newMessageThread.start();

            while(true){
                System.out.println("Enter in a message (logout to end): ");
                String messagetext = myObj.nextLine();
                if (messagetext.matches("logout")){
                    newMessageThread.interrupt();
                    Message logoutMessage = new Message(currentUser.getUsername(), null, "", "", msgType.LOGOUT, "");
                    output.writeObject(logoutMessage);
                    output.flush();
                    break;
                }
                Message newMessage = new Message(currentUser.getUsername(), new Date(), "madison-sean-david-sedat-joseph", "Sent", msgType.TEXT, messagetext);
                output.writeObject(newMessage);
                output.flush();
            }

            //get files

            // ending the socket connection
            s.close();
        
        } catch (IOException e) {
            e.printStackTrace();
        } 
        // catch (SocketException e) {
        //     e.printStackTrace();
        // } 
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        } 

    }
    public boolean authentication(){
            return false;
        }
    public static class receiveNewMessageClass implements Runnable {
		private ObjectInputStream inTunnel;
        Thread t;
		
		public receiveNewMessageClass(ObjectInputStream inTunnel) {
			this.inTunnel = inTunnel;
            t = new Thread(this);
		}
		
		public void run(){
			//check to see if there are new messages for User
            while(!Thread.interrupted()){
                try {
                    Message confirmation = (Message) inTunnel.readObject();
                    System.out.println(confirmation.toString());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
		}
	}
    
}
