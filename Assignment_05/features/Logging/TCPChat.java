import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TCPChat{

  private static void logMessages(String type, String s) {
  try {
      logFile.write(type + ": " + s + "\n");
  }
  catch (Exception e) {
      e.getStackTrace();
  }

}
	
	

	public static String message_out(String s){
		
		s = original(s);
		logMessages("TYPE?", s);
		return s;
	}
	
}
