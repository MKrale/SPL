import java.lang.*;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;


class Plugin_UI extends Plugin {
	private final int NULL = 0;
	private final int DISCONNECTED = 1;
	private final int DISCONNECTING = 2;
	private final int BEGIN_CONNECT = 3;
	private final int CONNECTED = 4;

    public JPanel extend_ChatUI(JPanel panel) {
		JPanel pane = null;
	    ActionAdapter buttonListener = null;

	    // Create an options pane
	    JPanel optionsPane = new JPanel(new GridLayout(4, 1));

	    // Port input
	    pane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    pane.add(new JLabel("Port:"));
	    portField = new JTextField(10);
	    portField.setEditable(true);
	    portField.setText((new Integer(chat.port)).toString());
	    portField.addFocusListener(new FocusAdapter() {
	        public void focusLost(FocusEvent e) {
	            // should be editable only when disconnected
	            if (chat.connectionStatus != DISCONNECTED) {
	                chat.changeStatusNTS(NULL, true);
	            } else {
	                int temp;
	                try {
	                    temp = Integer.parseInt(portField.getText());
	                    chat.port = temp;
	                } catch (NumberFormatException nfe) {
	                    portField.setText((new Integer(chat.port)).toString());
	                    mainFrame.repaint();
	                }
	            }
	        }
	    });
	    pane.add(portField);
	    optionsPane.add(pane);



	    // Host/guest option
	    buttonListener = new ActionAdapter() {
	        public void actionPerformed(ActionEvent e) {
	            if (chat.connectionStatus != DISCONNECTED) {
	                chat.changeStatusNTS(NULL, true);
	            } else {
	                chat.isHost = e.getActionCommand().equals("host");

					chat.hostIP = "localhost";

	            }
	        }
	    };
	    ButtonGroup bg = new ButtonGroup();
	    hostOption = new JRadioButton("Host", true);
	    hostOption.setMnemonic(KeyEvent.VK_H);
	    hostOption.setActionCommand("host");
	    hostOption.addActionListener(buttonListener);
	    guestOption = new JRadioButton("Guest", false);
	    guestOption.setMnemonic(KeyEvent.VK_G);
	    guestOption.setActionCommand("guest");
	    guestOption.addActionListener(buttonListener);
	    bg.add(hostOption);
	    bg.add(guestOption);
	    pane = new JPanel(new GridLayout(1, 2));
	    pane.add(hostOption);
	    pane.add(guestOption);
	    optionsPane.add(pane);


		// Connect/disconnect buttons
		JPanel buttonPane = new JPanel(new GridLayout(1, 2));
		buttonListener = new ActionAdapter() {
			public void actionPerformed(ActionEvent e) {
				// Request a connection initiation
				if (e.getActionCommand().equals("connect")) {
					// create log file for this client
					String logFileName = (chat.isHost ? "host" : "guest");
					logFileName += chat.port;
					logFileName += ".log";
//                    try {
//                        file = new FileWriter(logFileName);
//                        logFile = new BufferedWriter(file);
//                    } catch (Exception err) {
//                        err.getStackTrace();
//                    }

					chat.changeStatusNTS(BEGIN_CONNECT, true);
				}
				// Disconnect
				else {
					chat.changeStatusNTS(DISCONNECTING, true);
				}
			}
		};
		connectButton = new JButton("Connect");
		connectButton.setMnemonic(KeyEvent.VK_C);
		connectButton.setActionCommand("connect");
		connectButton.addActionListener(buttonListener);
		connectButton.setEnabled(true);
		disconnectButton = new JButton("Disconnect");
		disconnectButton.setMnemonic(KeyEvent.VK_D);
		disconnectButton.setActionCommand("disconnect");
		disconnectButton.addActionListener(buttonListener);
		disconnectButton.setEnabled(false);
		buttonPane.add(connectButton);
		buttonPane.add(disconnectButton);
		optionsPane.add(buttonPane);

		return optionsPane;
	}


	public void initGUI() {
		// Set up the status bar
		statusField = new JLabel();
		statusField.setText(chat.statusMessages[DISCONNECTED]);
		statusColor = new JTextField(1);
		statusColor.setBackground(Color.red);
		statusColor.setEditable(false);
		statusBar = new JPanel(new BorderLayout());
		statusBar.add(statusColor, BorderLayout.WEST);
		statusBar.add(statusField, BorderLayout.CENTER);

        /*=============================================================================================
         * 										Plugin hotspot UI
         =============================================================================================*/
		// Set up the options pane
		JPanel optionsPane = extend_ChatUI(null);

		// Set up the chat pane
		JPanel chatPane = new JPanel(new BorderLayout());
		chatText = new JTextArea(10, 20);
		chatText.setLineWrap(true);
		chatText.setEditable(false);
		chatText.setForeground(Color.blue);
		JScrollPane chatTextPane = new JScrollPane(chatText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		chatLine = new JTextField();
		chatLine.setEnabled(false);
		chatLine.addActionListener(new ActionAdapter() {
			public void actionPerformed(ActionEvent e) {
				String s = chatLine.getText();
				if (!s.equals("")) {
                	/*=============================================================================================
                     * 										Plugin hotspot Out-messages
                     =============================================================================================*/
					s = message_out(s);
					//appendToChatBox("OUTGOING: " + s + "\n");
					chatLine.selectAll();

					// Send the string
					chat.sendString(s);
				}
			}
		});
		chatPane.add(chatLine, BorderLayout.SOUTH);
		chatPane.add(chatTextPane, BorderLayout.CENTER);
		chatPane.setPreferredSize(new Dimension(200, 200));

		// Set up the main pane
		JPanel mainPane = new JPanel(new BorderLayout());
		mainPane.add(statusBar, BorderLayout.SOUTH);
		mainPane.add(optionsPane, BorderLayout.WEST);
		mainPane.add(chatPane, BorderLayout.CENTER);

		// Set up the main frame
		mainFrame = new JFrame("Simple TCP Chat");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setContentPane(mainPane);
		mainFrame.setSize(mainFrame.getPreferredSize());
		mainFrame.setLocation(200, 200);
		mainFrame.pack();
		mainFrame.setVisible(true);
	}

}