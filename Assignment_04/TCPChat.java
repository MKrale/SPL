import java.lang.*;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;

public class TCPChat implements Runnable {


    public final static int NULL = 0;
    public final static int DISCONNECTED = 1;
    public final static int DISCONNECTING = 2;
    public final static int BEGIN_CONNECT = 3;
    public final static int CONNECTED = 4;

    // Other constants
    public final static String[] statusMessages = {" Error! Could not connect!", " Disconnected", " Disconnecting...", " Connecting...", " Connected"};
    public final static TCPChat tcpObj = new TCPChat();
    public final static String END_CHAT_SESSION = Character.toString((char) 0); // Indicates the end of a session


    // Connection state info
    public static String hostIP = "localhost";
    public static int port = 1234;
    public static int code = 0000;
    public static int connectionStatus = DISCONNECTED;
    public static String statusString = statusMessages[connectionStatus];
    public static boolean isHost = true;
    public static StringBuffer toAppend = new StringBuffer();
    public static StringBuffer toSend = new StringBuffer();
    public static StringBuffer toSend0 = new StringBuffer();


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

    public static Plugin plugin = new Plugin_CLI();

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
//                    isHost = e.getActionCommand().equals("host");
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
//                    String logFileName = (isHost ? "host" : "guest");
//                    logFileName += port;
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

    // The non-thread-safe way to change the GUI components while
    // changing state
    static void changeStatusNTS(int newConnectStatus, boolean noError) {
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

    // Add text to send-buffer
    static void sendString(String s) {
        synchronized (toSend) {
//        	// Colour
//        	s = colour(s);
//        	//Logging
//        	if (Conf.Logging) {
//                logMessages("OUTGOING", s);
//                printOutMessage("OUTGOING", s);
//        	}
//        	//Encryption
//        	if (Conf.Encryption) {
//        		s = rot13(reverse(s));
//        	}
        	/*=============================================================================================
             * 										Plugin hotspot Out-messages
             =============================================================================================*/
            // Wrong?? s = plugin.message_out(s);
            // Sending
            toSend.append(s + "\n");


        }
    }

//    private static void logMessages(String type, String s) {
//        try {
//            logFile.write(type + ": " + s + "\n");
//        }
//        catch (Exception e) {
//            e.getStackTrace();
//        }
//
//    }

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

//    // Placeholder function for colouring text:
//    private static String colour(String s) {
//    	if (!isBlue) {
//    		return s+"*RED*";
//    	}
//    	return s+"*BLUE*";
//    }

    /////////////////////////////////////////////////////////////////

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

    // The main procedure
    public static void main(String[] args) {

        String s;
        plugin.initGUI();

        while (true) {
            try { // Poll every ~10 ms
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }


            switch (connectionStatus) {
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
                        if (can_start) {


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
                        } else {
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

    /////////////////////////////////////////////////////////////////

    public void set_plugin(Plugin p) {
        plugin = p;
        plugin.get_chat(this);
    }

    /////////////////////////////////////////////////////////////////

    // Checks the current state and sets the enables/disables
    // accordingly
    public void run() {
        plugin.checkStatus(connectionStatus);


        // Make sure that the button/text field states are consistent
        // with the internal states


    }
}

////////////////////////////////////////////////////////////////////

// Action adapter for easy event-listener coding
class ActionAdapter implements ActionListener {
    public void actionPerformed(ActionEvent e) {
    }
}

////////////////////////////////////////////////////////////////////
