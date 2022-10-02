import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

class Plugin_CLI extends Plugin {
    private final int NULL = 0;
    private final int DISCONNECTED = 1;
    private final int DISCONNECTING = 2;
    private final int BEGIN_CONNECT = 3;
    private final int CONNECTED = 4;


    @Override
    public void initGUI() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("port:");
            TCPChat.port = Integer.parseInt(br.readLine());
            System.out.println("host? (y/n)");
            String s = br.readLine();
            if (s.equals("y")) {
                TCPChat.isHost = true;
            } else {
                TCPChat.isHost = false;
            }
            TCPChat.hostIP = "localhost";

            TCPChat.changeStatusNTS(BEGIN_CONNECT, true);

        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    @Override
    public void checkStatus(int connectionStatus) {
        System.out.println(TCPChat.toAppend.toString());
        TCPChat.toAppend.setLength(0);
    }
}
