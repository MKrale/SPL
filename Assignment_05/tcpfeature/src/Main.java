
import java.awt.GridLayout;
import javax.swing.JPanel;



/**
 * Colour feature main 
 * By: Roel Duijsings
 */

public class Main {
	protected void print() {
		original();
		System.out.print("Colour");
	}
	
	// Normal/Red option
	// @Override
	public static JPanel initOptionsPane() {
	    original();
		JPanel pane = null;
	    ActionAdapter buttonListener = null;
	
	    // Create an options pane
	    JPanel optionsPane = new JPanel(new GridLayout(4, 1));
	    
		buttonListener = new ActionAdapter() {
			public void actionPerformed(ActionEvent e) {
				isBlue = e.getActionCommand().equals("blue");
				}
			};
		ButtonGroup bgc = new ButtonGroup();
		blueOption = new JRadioButton("Blue", false);
		blueOption.setMnemonic(KeyEvent.VK_B);
		blueOption.setActionCommand("blue");
		blueOption.addActionListener(buttonListener);
		redOption = new JRadioButton("Red", true);
		redOption.setMnemonic(KeyEvent.VK_R);
		redOption.setActionCommand("red");
		redOption.addActionListener(buttonListener);
		bgc.add(blueOption);
		bgc.add(redOption);
		pane = new JPanel(new GridLayout(1, 2));
		pane.add(blueOption);
		pane.add(redOption);
		optionsPane.add(pane);
	    return optionsPane;
	
	}
	
// Placeholder function for colouring text:
	private static String colour(String s) {
		if (!isBlue) {	
			return s+" *RED*";
		}
		return s+" *BLUE*";
	}
 
// Add text to send-buffer 
	static void sendString(String s) {
		original();
		synchronized (toSend) {
			s = colour(s);
			toSend.append(s + "\n");
		}
	}
}