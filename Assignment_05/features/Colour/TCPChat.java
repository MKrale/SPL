import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TCPChat{
	
	private static String colour = "";

	
	public static JPanel extend_ChatUI(JPanel panel) {
		
	  panel = original(panel);
	  JPanel pane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	  pane.add(new JLabel("Colour:"));
	  JTextField codeField = new JTextField(10);
	  codeField.setEditable(true);
	  codeField.setText((new String(colour)).toString());
	  codeField.addFocusListener(new FocusAdapter() {
	      public void focusLost(FocusEvent e) {
	          // Removed some checks here...
	    	  String temp;
	          try {
	              temp = (codeField.getText());
	              colour = temp;
	          } catch (NumberFormatException nfe) {
	              codeField.setText((new String(colour)).toString());
	          }
	
	      }
	  });
	  pane.add(codeField);
	  panel.add(pane);
		  
		  return  panel;
		}
	
	private static String colour(String s) {
		return (s+"*" + colour + "*");
    }
	public static String message_out(String s){
		s = original(s);
		if (!isHost){
			s = colour(s);
		}
		return s;
	}
}
