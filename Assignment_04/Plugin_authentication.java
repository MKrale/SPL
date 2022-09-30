import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Plugin_authentication extends Plugin {
	
	// Plugin variables
	private int password = 0;
	private int code = 0;

	public JPanel extend_ChatUI(JPanel panel) {
	// Adds Password input to GUI
	    
	  JPanel pane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	  pane.add(new JLabel("Entry code:"));
	  JTextField codeField = new JTextField(10);
	  codeField.setEditable(true);
	  codeField.setText((new Integer(code)).toString());
	  codeField.addFocusListener(new FocusAdapter() {
	      public void focusLost(FocusEvent e) {
	          // Removed some checks here...
	    	  int temp;
              try {
                  temp = Integer.parseInt(codeField.getText());
                  code = temp;
              } catch (NumberFormatException nfe) {
                  codeField.setText((new Integer(code)).toString());
              }

	      }
	  });
	  pane.add(codeField);
	  panel.add(pane);
	  return  panel;
	}
  
	public boolean can_start() {
		// Adds start-up condition that code = password
		return this.code == this.password;
	}
	
	
}
