import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public aspect Colour {

	protected String colour = "";

	before(): execution(void sendString(String)){
		if (true){//!isHost){
			s = colour(s);
		}
		System.out.println("Test colour");
	}
	
	private String colour(String s) {
		return (s+"*" + colour + "*");
    }

	pointcut extendUI() : execution(JPanel extend_ChatUI(JPanel));
	
	after(JPanel panel): extendUI() && args(panel){
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
		              colour  = temp;
		          } catch (NumberFormatException nfe) {
		              codeField.setText((new String(colour)).toString());
		          }
		
		      }
		  }
		  );
		  pane.add(codeField);
		  panel.add(pane);
		  return panel;
	}
}
