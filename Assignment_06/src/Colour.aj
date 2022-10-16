import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;

public aspect Colour {

	protected String colour = "";

	pointcut colourString(String s):
		execution(void sendString(String))
		&& args(s);
	
	after(String s): colourString(s){
		if (true){//!isHost){            !!! add ishost!
			s = colour(s);
		}
		System.out.println("Test colour");
	}
	
//	private String colour(String s) {
//		return (s+"*" + TCPChat.colour + "*");
//    }
	private static String colour(String s) {
    	if (!isBlue) {
    		return s+"*RED*";
    	}
    	return s+"*BLUE*";
    }

	pointcut extendUI(JPanel panel): 
		execution(JPanel TCPChat.extend_ChatUI(JPanel)) 
		&& args(panel);
	
	before(JPanel panel): extendUI(panel){
		  JPanel pane = new JPanel();
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
