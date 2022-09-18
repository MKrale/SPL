import java.lang.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;

public class TCPChat implements Runnable {
    // Connect status constants
    public final static int NULL = 0;
    public final static int DISCONNECTED = 1;
    public final static int DISCONNECTING = 2;
    public final static int BEGIN_CONNECT = 3;
    public final static int CONNECTED = 4;

    // Other constants
    public final static String statusMessages[] = {" Error! Could not connect!", " Disconnected", " Disconnecting...", " Connecting...", " Connected"};
    public final static TCPChat tcpObj = new TCPChat();
    public final static String END_CHAT_SESSION = new Character((char) 0).toString(); // Indicates the end of a session

    // Connection state info
    public static String hostIP = "localhost";
    public static int port = 1234;
    public static int code = 0000;
    public static int connectionStatus = DISCONNECTED;
    public static String statusString = statusMessages[connectionStatus];
    public static boolean isHost = true;
    public static boolean isBlue = false;
    public static StringBuffer toAppend = new StringBuffer("");
    public static StringBuffer toSend = new StringBuffer("");
    public static StringBuffer toSend0 = new StringBuffer("");

    // Various GUI components and info
    public static JFrame mainFrame = null;
    public static JTextArea chatText = null;
    public static JTextField chatLine = null;
    public static JPanel statusBar = null;
    public static JLabel statusField = null;
    public static JTextField statusColor = null;
    public static JTextField ipField = null;
    public static JTextField portField = null;
    public static JTextField codeField = null;
    public static JRadioButton hostOption = null;
    public static JRadioButton guestOption = null;
    public static JRadioButton blueOption = null;
    public static JRadioButton redOption = null;
    public static JButton connectButton = null;
    public static JButton disconnectButton = null;

    // TCP Components
    public static ServerSocket hostServer = null;
    public static ServerSocket hostServer0 = null;
    public static Socket socket = null;
    public static Socket socket0 = null;
    public static BufferedReader in = null;
    public static BufferedReader in0 = null;
    public static PrintWriter out = null;
    public static PrintWriter out0 = null;

    public static FileWriter file = null;
    public static BufferedWriter logFile = null;

    //Variability
    class Conf {
    	public static boolean Encryption = true;
        public static boolean Logging = true;
    }





    /////////////////////////////////////////////////////////////////

    // Initialize the Options Panel
    private static JPanel initOptionsPane() {
        JPanel pane = null;
        ActionAdapter buttonListener = null;

        // Create an options pane
        JPanel optionsPane = new JPanel(new GridLayout(4, 1));

        // IP address input
        pane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pane.add(new JLabel("Host IP:"));
        ipField = new JTextField(10);
        ipField.setText(hostIP);
        ipField.setEnabled(false);
        ipField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                ipField.selectAll();
                // Should be editable only when disconnected
                if (connectionStatus != DISCONNECTED) {
                    changeStatusNTS(NULL, true);
                } else {
                    hostIP = ipField.getText();
                }
            }
        });
        pane.add(ipField);
        optionsPane.add(pane);

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

     // Password input
        pane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pane.add(new JLabel("Entry code:"));
        codeField = new JTextField(10);
        codeField.setEditable(true);
        codeField.setText((new Integer(code)).toString());
        codeField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                // should be editable only when disconnected
                if (connectionStatus != DISCONNECTED) {
                    changeStatusNTS(NULL, true);
                } else {
                    int temp;
                    try {
                        temp = Integer.parseInt(codeField.getText());
                        code = temp;
                    } catch (NumberFormatException nfe) {
                        portField.setText((new Integer(code)).toString());
                        mainFrame.repaint();
                    }
                }
            }
        });
        pane.add(codeField);
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


     // Normal/Red option
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
                    try {
                        file = new FileWriter(logFileName);
                        logFile = new BufferedWriter(file);
                    } catch (Exception err) {
                        err.getStackTrace();
                    }

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

    /////////////////////////////////////////////////////////////////

    // Initialize all the GUI components and display the frame
    private static void initGUI() {
        // Set up the status bar
        statusField = new JLabel();
        statusField.setText(statusMessages[DISCONNECTED]);
        statusColor = new JTextField(1);
        statusColor.setBackground(Color.red);
        statusColor.setEditable(false);
        statusBar = new JPanel(new BorderLayout());
        statusBar.add(statusColor, BorderLayout.WEST);
        statusBar.add(statusField, BorderLayout.CENTER);

        // Set up the options pane
        JPanel optionsPane = initOptionsPane();

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
                    appendToChatBox("OUTGOING: " + colour(s) + "\n");
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

    /////////////////////////////////////////////////////////////////

    // The thread-safe way to change the GUI components while
    // changing state
    private static void changeStatusTS(int newConnectStatus, boolean noError) {
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
        // error-handling and GUI-update thread
        SwingUtilities.invokeLater(tcpObj);
    }

    /////////////////////////////////////////////////////////////////

    // The non-thread-safe way to change the GUI components while
    // changing state
    private static void changeStatusNTS(int newConnectStatus, boolean noError) {
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

    /////////////////////////////////////////////////////////////////

    // Thread-safe way to append to the chat box
    private static void appendToChatBox(String s) {
        synchronized (toAppend) {
            toAppend.append(s);
        }
    }

    /////////////////////////////////////////////////////////////////
    public static String rot13(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c >= 'a' && c <= 'm') c += 13;
            else if (c >= 'A' && c <= 'M') c += 13;
            else if (c >= 'n' && c <= 'z') c -= 13;
            else if (c >= 'N' && c <= 'Z') c -= 13;
            sb.append(c);
        }
        return sb.toString();
    }

    public static String reverse(String input) {
        StringBuilder input1 = new StringBuilder();
        input1.append(input);

        // reverse StringBuilder input1
        input1.reverse();
        return input1.toString();
    }

    private static void logMessages(String type, String s) {
        try {
            logFile.write(type + ": " + s + "\n");
            System.out.print("logged");
        }
        catch (Exception e) {
            e.getStackTrace();
        }

    }


    // Add text to send-buffer
    private static void sendString(String s) {
        synchronized (toSend) {
        	// Colour
        	s = colour(s);
        	//Logging
        	if (Conf.Logging) {
                logMessages("OUTGOING", s);
        	}
        	//Encryption
        	if (Conf.Encryption) {
        		s = rot13(reverse(s));
        	}
        	// Sending
        	toSend.append(s + "\n");


        }
    }

    // Placeholder function for colouring text:
    private static String colour(String s) {
    	if (!isBlue) {
    		return s+"*RED*";
    	}
    	return s+"*BLUE*";
    }

    /////////////////////////////////////////////////////////////////

    // Cleanup for disconnect
    private static void cleanUp() {
        try {
            if (hostServer != null) {
                hostServer.close();
                hostServer = null;
            }
        } catch (IOException e) {
            hostServer = null;
        }

        try {
            if (socket != null) {
                socket.close();
                socket = null;
            }
        } catch (IOException e) {
            socket = null;
        }

        try {
            if (in != null) {
                in.close();
                in = null;
            }
        } catch (IOException e) {
            in = null;
        }

        if (out != null) {
            out.close();
            out = null;
        }
    }

    static void sendMessage(StringBuffer toSend, PrintWriter out) {
        if (toSend.length() != 0) {
            out.print(toSend);
            out.flush();
            toSend.setLength(0);
            changeStatusTS(NULL, true);
        }
      }
    static String receiveMessage(BufferedReader in, boolean isHost) {
		String s = null;
    	try {
            if (in.ready()) {
                s = in.readLine();
                if ((s != null) && (s.length() != 0)) {
                    // Check if it is the end of a transmission TODO this probably doesnt work with encryption
                    if (s.equals(END_CHAT_SESSION)) {
                        changeStatusTS(DISCONNECTING, true);
                    }

                    // Otherwise, receive what text
                    else {
                    	if (Conf.Encryption && !isHost) {
                    		s = reverse(rot13(s));
                    	}
                    	if (Conf.Logging) {
                    		logMessages("INCOMING", s);
                    	}
                        appendToChatBox("INCOMING: " + s + "\n");
                        changeStatusTS(NULL, true);
                    }
                }
            }
        } catch (IOException e) {
            cleanUp();
            changeStatusTS(DISCONNECTED, false);
        }
        return s;
    }

    /////////////////////////////////////////////////////////////////

    // The main procedure
    public static void main(String args[]) {

        String s;

        initGUI();

        while (true) {
            try { // Poll every ~10 ms
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }


            switch (connectionStatus) {
                case BEGIN_CONNECT:
                    try {

                    	// Check pass code
                    	if (code != 0) {
                    		// Do relevant stuff: now we just don't connect
                    		cleanUp();
                            changeStatusTS(DISCONNECTED, false);
                    	}
                    	else {

	                        // Try to set up a server if host
	                        if (isHost) {
	                            //Setup a socket for each client
	                            hostServer = new ServerSocket(port);
	                            socket = hostServer.accept();
	                            hostServer0 = new ServerSocket(1230); // TODO Make UI for this
	                            socket0 = hostServer0.accept();
	                        }

	                        // If guest, try to connect to the server
	                        else {
	                            socket = new Socket(hostIP, port);

	                        }

                            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            out = new PrintWriter(socket.getOutputStream(), true);

                            //Host needs additional port
	                        if (isHost) {
	                            in0 = new BufferedReader(new InputStreamReader(socket0.getInputStream()));
	                            out0 = new PrintWriter(socket0.getOutputStream(), true);
	                        }

	                        changeStatusTS(CONNECTED, true);
                    	}
                    }
                    // If error, clean up and output an error message
                    catch (IOException e) {
                        cleanUp();
                        changeStatusTS(DISCONNECTED, false);
                    }
                    break;

                case CONNECTED:
                    	//Send what is in the queue
                    	sendMessage(toSend, out);
                    	// Receive what's in the queue
                    	s = receiveMessage(in, isHost);

                    	// Hosts handles forwarding and the other queue
                        if (isHost) {
                        	//Send the other queue
                        	sendMessage(toSend0, out0);
                            //Forward what's in the queue
                        	if (s != null) {
                        		appendToChatBox("forwarding..." + "\n");
                                toSend0.append(s + "\n"); //Forwards
                        	}
                        	// And receive / forward what is in the remaining queue
                        	s = receiveMessage(in0, isHost);
                        	if (s != null) {
                        		appendToChatBox("forwarding..." + "\n");
                                toSend.append(s + "\n"); //Forwards
                        	}
                        }

                    break;

                case DISCONNECTING:
                    // Closes the file writer
                    try {
                        logFile.close();
                    } catch (IOException e) {}

                    // Tell other chatter to disconnect as well
                    out.print(END_CHAT_SESSION);
                    out.flush();

                    // Clean up (close all streams/sockets)
                    cleanUp();
                    changeStatusTS(DISCONNECTED, true);
                    break;

                default:
                    break; // do nothing
            }
        }
    }

    /////////////////////////////////////////////////////////////////

    // Checks the current state and sets the enables/disables
    // accordingly
    public void run() {
        switch (connectionStatus) {
            case DISCONNECTED:
                connectButton.setEnabled(true);
                disconnectButton.setEnabled(false);
                ipField.setEnabled(true);
                portField.setEnabled(true);
                hostOption.setEnabled(true);
                guestOption.setEnabled(true);
                chatLine.setText("");
                chatLine.setEnabled(false);
                statusColor.setBackground(Color.red);
                break;

            case DISCONNECTING:
                connectButton.setEnabled(false);
                disconnectButton.setEnabled(false);
                ipField.setEnabled(false);
                portField.setEnabled(false);
                hostOption.setEnabled(false);
                guestOption.setEnabled(false);
                chatLine.setEnabled(false);
                statusColor.setBackground(Color.orange);
                break;

            case CONNECTED:
                connectButton.setEnabled(false);
                disconnectButton.setEnabled(true);
                ipField.setEnabled(false);
                portField.setEnabled(false);
                hostOption.setEnabled(false);
                guestOption.setEnabled(false);
                chatLine.setEnabled(true);
                statusColor.setBackground(Color.green);
                break;

            case BEGIN_CONNECT:
                connectButton.setEnabled(false);
                disconnectButton.setEnabled(false);
                ipField.setEnabled(false);
                portField.setEnabled(false);
                hostOption.setEnabled(false);
                guestOption.setEnabled(false);
                chatLine.setEnabled(false);
                chatLine.grabFocus();
                statusColor.setBackground(Color.orange);
                break;
        }

        // Make sure that the button/text field states are consistent
        // with the internal states
        ipField.setText(hostIP);
        portField.setText((new Integer(port)).toString());
        hostOption.setSelected(isHost);
        guestOption.setSelected(!isHost);
        statusField.setText(statusString);
        chatText.append(toAppend.toString());
        toAppend.setLength(0);

        mainFrame.repaint();
    }
}

////////////////////////////////////////////////////////////////////

// Action adapter for easy event-listener coding
class ActionAdapter implements ActionListener {
    public void actionPerformed(ActionEvent e) {
    }
}

////////////////////////////////////////////////////////////////////
