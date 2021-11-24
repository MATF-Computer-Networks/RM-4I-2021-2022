package i03_echo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

final class EchoServer extends Thread implements AutoCloseable {
	private final DatagramSocket socket;


	EchoServer(int port) throws SocketException {
		this.socket = new DatagramSocket(port);
	}


	@Override
	public void run() {
		System.err.println("Server started...");

		try {
			while (true) {
				byte[] buf = new byte[256];
				try {
					// Wait for packets...
					DatagramPacket request = new DatagramPacket(buf, buf.length);
					this.socket.receive(request);

					// Send response (using same buffer from request packet)
					DatagramPacket response = new DatagramPacket(
						buf, request.getLength(), request.getAddress(), request.getPort()
					);
					this.socket.send(response);

					// Check if "end" is received
					String received = new String(buf, 0, response.getLength(), StandardCharsets.UTF_8);
					System.err.println("Server recv: " + received);
					if (received.equalsIgnoreCase("end")) {
						System.err.println("Server received end signal.");
						break;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} finally {
			System.err.println("Server is shutting down...");
			this.close();
			System.err.println("Server successfully closed!");
		}
	}

	@Override
	public void close() {
		this.socket.close();
	}
}
