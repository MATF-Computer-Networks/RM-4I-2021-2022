package i02_chat;

import java.net.*;
import java.io.*;

final class ChatClient {

    // NOTE: In IntelliJ IDEA you need to allow parallel run for this class
    // To do so, open Run Configurations dialog and check `Allow parallel run`
    // for ChatClient configuration
    public static void main(String[] args) {
        ChatClient client = new ChatClient("localhost", ChatServer.SERVER_TEST_PORT);
        System.err.println("Connecting to local port: " + ChatServer.SERVER_TEST_PORT);
        client.execute();
    }


    private final String hostname;
    private final int port;
    private String name;


    ChatClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }


    void execute() {
        try {
            this.setName();

            try (Socket socket = new Socket(this.hostname, this.port)) {
                System.out.println("Connected to the chat server @ " + this.hostname);

                // Dispatch threads
                var rt = new ClientReadThread(this.name, socket);
                rt.start();
                var wt = new ClientWriteThread(this.name, socket);
                wt.start();

                // Wait for threads so we can close the socket (try-with-resources)
                rt.join();
                wt.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void setName() throws IOException {
        System.out.print("Enter your name: ");
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        this.name = stdin.readLine();
        // We cannot close stdin, since we will use it later
    }
}