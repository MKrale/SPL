import javax.swing.*;


public class Plugin { 
	// Making sure required variables are shared/can be read by both
	
	public TCPChat chat;
	/* Gets chat as a reference (For if reading any chat-variables is required): */
	public void get_chat(TCPChat c) {
		this.chat = c;
	};
	// Ideally, we would put all relevant variables in a class and set up an observer pattern thingy, but that seem like a lot of work...
	
	// For reading/altering messages
	public String message_in(String s) {return s;};
	public String message_out(String s) {return s;}
	
	// For extending the chat UI 
	public JPanel extend_ChatUI(JPanel panel) {return panel;};
	// If we both the option to change in runtime and compile-time, we could also add this:
	//public JPanel extend_OptionsUI(JPanel panel);

	// Everything Chat and UI related
	public void initGUI() {};

	public void checkStatus(int connectionStatus) {};


	// Adds options to cancel start-up
	public boolean can_start() {return true;};
	
}