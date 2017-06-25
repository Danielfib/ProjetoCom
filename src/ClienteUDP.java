import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ClienteUDP {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int porta = 2027;

		while (true) {
			try {
				DatagramSocket clientSocket = new DatagramSocket();
				String ip = "192.168.25.19"; // botar pra pegar da interface
				InetAddress addr = InetAddress.getByName(ip);
				String msg = in.nextLine(); // botar pra pegar da interface
				byte[] msgBytes = msg.getBytes();

				DatagramPacket pkg = new DatagramPacket(msgBytes, msgBytes.length, addr, porta);
				clientSocket.send(pkg);

				clientSocket.close();
			} catch (IOException ioe) {
				System.out.println("exception do cliente");

			}
		}
	}

}