import java.lang.*;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;


class Plugin_UI extends Plugin implements Runnable {
	private final int NULL = 0;
	private final int DISCONNECTED = 1;
	private final int DISCONNECTING = 2;
	private final int BEGIN_CONNECT = 3;
	private final int CONNECTED = 4;
	private String hostIP = "localhost";
    private JTextField portField = null;


    private JTextArea chatText = null;
    private JTextField chatLine = null;
    private JPanel statusBar = null;
    private JLabel statusField = null;
    private JTextField statusColor = null;
    private JTextField codeField = null;
    private JRadioButton hostOption = null;
    private JRadioButton guestOption = null;
    private JButton connectButton = null;
    private JButton disconnectButton = null;

	private JTextField ipField = null;

    private void changeStatusNTS(int newConnectStatus, boolean noError) {
        // Change state if valid state
        if (newConnectStatus != NULL) {
            connectionStatus = newConnectStatus;
        }

        // If there is no error, display the appropriate status message
        if (noError) {
            statusString = statusMessages[connectionStatus];
        }
        // Otherwise, display error message
        else {
            statusString = statusMessages[NULL];
        }

        // Call the run() routine (Runnable interface) on the
        // current thread
        tcpObj.run();
    }


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
	    portField.setText((new Integer(port)).toString());
	    portField.addFocusListener(new FocusAdapter() {
	        public void focusLost(FocusEvent e) {
	            // should be editable only when disconnected
	            if (connectionStatus != DISCONNECTED) {
	                changeStatusNTS(NULL, true);
	            } else {
	                int temp;
	                try {
	                    temp = Integer.parseInt(portField.getText());
	                    port = temp;
	                } catch (NumberFormatException nfe) {
	                    portField.setText((new Integer(port)).toString());
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
	            if (connectionStatus != DISCONNECTED) {
	                changeStatusNTS(NULL, true);
	            } else {
	                isHost = e.getActionCommand().equals("host");

	                // Cannot supply host IP if host option is chosen
	                if (isHost) {
	                    ipField.setEnabled(false);
	                    ipField.setText("localhost");
	                    hostIP = "localhost";
	                } else {
	                    ipField.setEnabled(true);
	                }
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
					String logFileName = (isHost ? "host" : "guest");
					logFileName += port;
					logFileName += ".log";
//                    try {
//                        file = new FileWriter(logFileName);
//                        logFile = new BufferedWriter(file);
//                    } catch (Exception err) {
//                        err.getStackTrace();
//                    }

					changeStatusNTS(BEGIN_CONNECT, true);
				}
				// Disconnect
				else {
					changeStatusNTS(DISCONNECTING, true);
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
		statusField.setText(statusMessages[DISCONNECTED]);
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
					sendString(s);
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

	// Add text to send-buffer
	public void sendString(String s) {
		synchronized (toSend) {
        	/*=============================================================================================
             * 										Plugin hotspot Out-messages
             =============================================================================================*/
			// Wrong?? s = plugin.message_out(s);
			// Sending
			toSend.append(s + "\n");


		}
	}

	public void run() {
		switch (connectionStatus) {
			case DISCONNECTED:
				connectButton.setEnabled(true);
				disconnectButton.setEnabled(false);
				hostOption.setEnabled(true);
				guestOption.setEnabled(true);
				chatLine.setText("");
				chatLine.setEnabled(false);
				statusColor.setBackground(Color.red);
				break;

			case DISCONNECTING:
				connectButton.setEnabled(false);
				disconnectButton.setEnabled(false);
				hostOption.setEnabled(false);
				guestOption.setEnabled(false);
				chatLine.setEnabled(false);
				statusColor.setBackground(Color.orange);
				break;

			case CONNECTED:
				connectButton.setEnabled(false);
				disconnectButton.setEnabled(true);
				hostOption.setEnabled(false);
				guestOption.setEnabled(false);
				chatLine.setEnabled(true);
				statusColor.setBackground(Color.green);
				break;

			case BEGIN_CONNECT:
				connectButton.setEnabled(false);
				disconnectButton.setEnabled(false);
				hostOption.setEnabled(false);
				guestOption.setEnabled(false);
				chatLine.setEnabled(false);
				chatLine.grabFocus();
				statusColor.setBackground(Color.orange);
				break;
		}

		// Make sure that the button/text field states are consistent
		// with the internal states
		// TODO: portField.setText((new Integer(port)).toString());
		hostOption.setSelected(isHost);
		guestOption.setSelected(!isHost);
		statusField.setText(statusString);
//		chatText.append(toAppend.toString());
//		toAppend.setLength(0);

		mainFrame.repaint();
	}


}