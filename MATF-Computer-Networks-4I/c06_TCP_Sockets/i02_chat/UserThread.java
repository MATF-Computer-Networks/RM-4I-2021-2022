package i02_chat;

import java.io.*;
import java.net.*;

final class UserThread extends Thread {
    private final ChatServer server;
    private final Socket sock;
    private BufferedReader fromUser;
    private PrintWriter toUser;
    private String username;


    UserThread(Socket socket, ChatServer server) {
        this.sock = socket;
        this.server = server;
        try {
            this.fromUser = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
            this.toUser = new PrintWriter(this.sock.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        try {
            // Upon connecting, read username and send connected users list
            this.username = fromUser.readLine();
            this.sendMessage("Connected users: " + this.server.getUserNames());

            // Broadcast that new user has entered the chat
            this.server.broadcast(this, "New user connected: " + this.username);

            // Process the user (until he leaves the chat)
            String clientMessage;
            do {
                // Read message from user
                clientMessage = fromUser.readLine();
                if (clientMessage == null)
                    break;

                // Broadcast the message
                this.server.broadcast(this, "[" + this.username + "]: " + clientMessage);
            } while (!clientMessage.equals("bye"));

            // Broadcast that user has disconnected
            this.server.broadcast(this, this.username + " has left the chat.");

        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            // Remove user from set
            this.server.remove(this);

            // Close socket
            try {
                this.sock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void sendMessage(String message) {
        if (this.toUser != null)
            this.toUser.println(message);
    }

    String getNickname() {
        return this.username;
    }
}