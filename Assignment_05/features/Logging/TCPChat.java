import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.FileWriter;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TCPChat{
	
	private static void logMessages(String type, String s) {
		try {
			fr.write(type + ": " + s + "\n");
		}
		catch (Exception e) {
			e.getStackTrace();
		}
  	}
	

	public static String message_out(String s) {
		s = original(s);
		logMessages("OUTGOING", s);
		return s;
	}
	
	public static String message_in(String s) {
		s = original(s);
		logMessages("INCOMING", s);
		return s;
	}
	
}
