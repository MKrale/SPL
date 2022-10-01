import java.lang.*;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;

public class TCPChat {
    

    public final static int NULL = 0;
    public final static int DISCONNECTED = 1;
    public final static int DISCONNECTING = 2;
    public final static int BEGIN_CONNECT = 3;
    public final static int CONNECTED = 4;

    // Other constants
    public final static String statusMessages[] = {" Error! Could not connect!", " Disconnected", " Disconnecting...", " Connecting...", " Connected"};
    public final static String END_CHAT_SESSION = new Character((char) 0).toString(); // Indicates the end of a session

    // Connection state info
    public static String hostIP = "localhost";

    public static int code = 0000;
    public static StringBuffer toSend0 = new StringBuffer("");


    // TCP Components
    public static ServerSocket hostServer = null;
    public static ServerSocket hostServer0 = null;
    public static Socket socket = null;
    public static Socket socket0 = null;
    public static BufferedReader in = null;
    public static BufferedReader in0 = null;
    public static PrintWriter out = null;
    public static PrintWriter out0 = null;
	
	
	    public static JFrame confFrame = null;

   public static Plugin plugin = new Plugin_UI();

    		
	public static void set_plugin(Plugin p) {
		plugin = p;
		plugin.get_chat(this);
	}


    // CONFIGURATION PANE -> Unused
//    private static void initConfigGUI() {
//
//        JPanel pane = new JPanel(new FlowLayout());
//        boolean defaultChecked = true;
//
//        loggingBox = new JCheckBox ("Enable logging", defaultChecked);
//        loggingBox.setMnemonic(KeyEvent.VK_G);
//        pane.add(loggingBox);
//
//        encryptionBox = new JCheckBox ("Enable encryption", defaultChecked);
//        encryptionBox.setMnemonic(KeyEvent.VK_G);
//        pane.add(encryptionBox);
//
//        colorsBox = new JCheckBox ("Enable colors", defaultChecked);
//        colorsBox.setMnemonic(KeyEvent.VK_G);
//        pane.add(colorsBox);
//
//        authenticationBox = new JCheckBox ("Enable authentication", defaultChecked);
//        authenticationBox.setMnemonic(KeyEvent.VK_G);
//        pane.add(authenticationBox);
//
//        ActionAdapter buttonListener = new ActionAdapter() {
//            public void actionPerformed(ActionEvent e) {
//                if(loggingBox.isSelected()){
//                    Conf.Logging = true;
//                } else {
//                    Conf.Logging = false;
//                }
//                if(encryptionBox.isSelected()){
//                    Conf.Encryption = true;
//                } else {
//                    Conf.Encryption = false;
//                }
//
//
//                if(authenticationBox.isSelected()){
//                    Conf.Authentication = true;
//                } else {
//                    Conf.Authentication = false;
//                }
//
//                if(colorsBox.isSelected()){
//                    Conf.Colors = true;
//                } else {
//                    Conf.Colors = false;
//                }
//
//                confFrame.dispose();
//                initGUI();
//            }
//        };

//        JButton btn = new JButton("Confirm");
//        btn.setMnemonic(KeyEvent.VK_C);
//        btn.setActionCommand("confirm");
//        btn.addActionListener(buttonListener);
//        btn.setEnabled(true);
//        pane.add(btn);  // Add the button to the pane
//
//
//        // Now for the frame
//        confFrame = new JFrame();
//        confFrame.setContentPane(pane);  // Use our pane as the default pane
//        confFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit program when frame is closed
//        confFrame.setLocation(200, 200); // located at (200, 200)
//        confFrame.pack();                // Frame is ready. Pack it up for display.
//        confFrame.setVisible(true);      // Make it visible
//
//    }



    /////////////////////////////////////////////////////////////////

//    private static JPanel initOptionsPane() {
//        JPanel pane = null;
//        ActionAdapter buttonListener = null;
//
//        // Create an options pane
//        JPanel optionsPane = new JPanel(new GridLayout(4, 1));
//
//        // Host/guest option
//        buttonListener = new ActionAdapter() {
//            public void actionPerformed(ActionEvent e) {
//                if (plugin.connectionStatus != DISCONNECTED) {
//                    changeStatusNTS(NULL, true);
//                } else {
//                    plugin.isHost = e.getActionCommand().equals("host");
//
//                    // Cannot supply host IP if host option is chosen
//
//                    hostIP = "localhost";
//                }
//            }
//        };
//        ButtonGroup bg = new ButtonGroup();
//        hostOption = new JRadioButton("Host", true);
//        hostOption.setMnemonic(KeyEvent.VK_H);
//        hostOption.setActionCommand("host");
//        hostOption.addActionListener(buttonListener);
//        guestOption = new JRadioButton("Guest", false);
//        guestOption.setMnemonic(KeyEvent.VK_G);
//        guestOption.setActionCommand("guest");
//        guestOption.addActionListener(buttonListener);
//        bg.add(hostOption);
//        bg.add(guestOption);
//        pane = new JPanel(new GridLayout(1, 2));
//        pane.add(hostOption);
//        pane.add(guestOption);
//        optionsPane.add(pane);
//
//
////     // Normal/Red option
////        buttonListener = new ActionAdapter() {
////            public void actionPerformed(ActionEvent e) {
////            	isBlue = e.getActionCommand().equals("blue");
////            }
////        };
////        ButtonGroup bgc = new ButtonGroup();
////        blueOption = new JRadioButton("Blue", false);
////        blueOption.setMnemonic(KeyEvent.VK_B);
////        blueOption.setActionCommand("blue");
////        blueOption.addActionListener(buttonListener);
////        redOption = new JRadioButton("Red", true);
////        redOption.setMnemonic(KeyEvent.VK_R);
////        redOption.setActionCommand("red");
////        redOption.addActionListener(buttonListener);
////        bgc.add(blueOption);
////        bgc.add(redOption);
////        pane = new JPanel(new GridLayout(1, 2));
////        pane.add(blueOption);
////        pane.add(redOption);
////        optionsPane.add(pane);
//
//
//        // Connect/disconnect buttons
//        JPanel buttonPane = new JPanel(new GridLayout(1, 2));
//        buttonListener = new ActionAdapter() {
//            public void actionPerformed(ActionEvent e) {
//                // Request a connection initiation
//                if (e.getActionCommand().equals("connect")) {
//                    // create log file for this client
//                    String logFileName = (plugin.isHost ? "host" : "guest");
//                    logFileName += plugin.port;
//                    logFileName += ".log";
////                    try {
////                        file = new FileWriter(logFileName);
////                        logFile = new BufferedWriter(file);
////                    } catch (Exception err) {
////                        err.getStackTrace();
////                    }
//
//                    changeStatusNTS(BEGIN_CONNECT, true);
//                }
//                // Disconnect
//                else {
//                    changeStatusNTS(DISCONNECTING, true);
//                }
//            }
//        };
//        connectButton = new JButton("Connect");
//        connectButton.setMnemonic(KeyEvent.VK_C);
//        connectButton.setActionCommand("connect");
//        connectButton.addActionListener(buttonListener);
//        connectButton.setEnabled(true);
//        disconnectButton = new JButton("Disconnect");
//        disconnectButton.setMnemonic(KeyEvent.VK_D);
//        disconnectButton.setActionCommand("disconnect");
//        disconnectButton.addActionListener(buttonListener);
//        disconnectButton.setEnabled(false);
//        buttonPane.add(connectButton);
//        buttonPane.add(disconnectButton);
//        optionsPane.add(buttonPane);
//
//        /*=============================================================================================
//         * 										Plugin hotspot UI
//         =============================================================================================*/
//        optionsPane = plugin.extend_ChatUI(optionsPane);
//        return optionsPane;
//    }

    /////////////////////////////////////////////////////////////////



    // Initialize all the GUI components and display the frame

    /////////////////////////////////////////////////////////////////

    // The thread-safe way to change the GUI components while
    // changing state
    private static void changeStatusTS(int newConnectStatus, boolean noError) {
        // Change state if valid state
        if (newConnectStatus != NULL) {
            plugin.connectionStatus = newConnectStatus;
        }

        // If there is no error, display the appropriate status message
        if (noError) {
            plugin.statusString = statusMessages[plugin.connectionStatus];
        }
        // Otherwise, display error message
        else {
            plugin.statusString = statusMessages[NULL];
        }

        // Call the run() routine (Runnable interface) on the
        // error-handling and GUI-update thread
        SwingUtilities.invokeLater(plugin.tcpObj);
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
//                    	if (Conf.Encryption && !isHost) {
//                    		s = reverse(rot13(s));
//                    	}
//                    	if (Conf.Logging) {
//                    		logMessages("INCOMING", s);
//                            printOutMessage("INCOMING", s);
//
//                    	}
                    	s = plugin.message_in(s);
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
        set_plugin(plugin);

        String s;
        System.out.print("asdasd");
        plugin.initGUI();

        while (true) {
            try { // Poll every ~10 ms
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }


            switch (plugin.connectionStatus) {
                case BEGIN_CONNECT:
                    try {

                    	// Check pass code
//                    	if (code != 0) {
//                    		// Do relevant stuff: now we just don't connect
//                    		cleanUp();
//                            changeStatusTS(DISCONNECTED, false);
//                    	}
                    	/*=============================================================================================
                         * 										Plugin hotspot Start Check
                         =============================================================================================*/
                    	boolean can_start = plugin.can_start();
                    	if(can_start) {

	                        // Try to set up a server if host
	                        if (plugin.isHost) {
	                            //Setup a socket for each client
	                            hostServer = new ServerSocket(plugin.port);
	                            socket = hostServer.accept();
	                            hostServer0 = new ServerSocket(1230); // TODO Make UI for this
	                            socket0 = hostServer0.accept();
	                        }

	                        // If guest, try to connect to the server
	                        else {
	                            socket = new Socket(hostIP, plugin.port);

	                        }

                            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            out = new PrintWriter(socket.getOutputStream(), true);

                            //Host needs additional port
	                        if (plugin.isHost) {
	                            in0 = new BufferedReader(new InputStreamReader(socket0.getInputStream()));
	                            out0 = new PrintWriter(socket0.getOutputStream(), true);
	                        }

	                        changeStatusTS(CONNECTED, true);
                    	}
                    	else {
                    		// Print some message maybe?
                        	cleanUp();
                            changeStatusTS(DISCONNECTED, false);
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
                    	sendMessage(plugin.toSend, out);
                    	// Receive what's in the queue
                    	s = receiveMessage(in, plugin.isHost);

                    	// Hosts handles forwarding and the other queue
                        if (plugin.isHost) {
                        	//Send the other queue
                        	sendMessage(toSend0, out0);
                            //Forward what's in the queue
                        	if (s != null) {
                                toSend0.append(s + "\n"); //Forwards
                        	}
                        	// And receive / forward what is in the remaining queue
                        	s = receiveMessage(in0, plugin.isHost);
                        	if (s != null) {
                                plugin.toSend.append(s + "\n"); //Forwards
                        	}
                        }

                    break;

                case DISCONNECTING:
                    // Closes the file writer
//                    try {
//                        logFile.close();
//                    } catch (IOException e) {}

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
}

////////////////////////////////////////////////////////////////////

// Action adapter for easy event-listener coding
class ActionAdapter implements ActionListener {
    public void actionPerformed(ActionEvent e) {
    }
}

////////////////////////////////////////////////////////////////////
