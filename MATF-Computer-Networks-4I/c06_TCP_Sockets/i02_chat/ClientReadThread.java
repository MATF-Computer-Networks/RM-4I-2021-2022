package i02_chat;

import java.io.*;
import java.net.Socket;

final class ClientReadThread extends Thread {
    private BufferedReader fromServer;
    private String username;


    ClientReadThread(String username, Socket socket) {
        this.username = username;
        try {
            this.fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    @Override
    public void run() {
        // Continuously receive and print messages from the server
        while (true) {
            try {
                // Wait for message and print it
                String response = this.fromServer.readLine();
                if (response == null) {
                    System.err.println("\rConnection lost.");
                    return;
                }
                System.out.println("\r" + response);

                // Print prompt
                System.out.printf("\r[%s]: ", this.username);
            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    }
}