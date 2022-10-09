import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TCPChat{


	public static String message_out(String s){
		String s_ = original(s);
		Toolkit.getDefaultToolkit().beep();
		return s_;
	}
	
	public static String message_in(String s){
		String s_ = original(s);
		Toolkit.getDefaultToolkit().beep();
		return s_;
	}

}
