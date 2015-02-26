package utills;

import javax.swing.JOptionPane;

public class Uiutills {
	
	
	public static void showDialog(String title ,Message msg , int messageType){
		JOptionPane.showMessageDialog(null,
				msg.getMessage(),
				title, messageType);
	}
	
}
