import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServidorUDP {

	public static void main(String[] args) {
	int porta = 2020; // ver de onde é

		while (true) {
			try {
				DatagramSocket serverSocket = new DatagramSocket(porta);
				// preparando buffer de recebimento da msg:
				byte[] msg = new byte[256];
				
				// prepara o pacote de dados
				DatagramPacket receivePacket = new DatagramPacket(msg, msg.length);
				serverSocket.receive(receivePacket);				
				
				System.out.println("msg recebida: " + new String(receivePacket.getData()));
				serverSocket.close();

			} catch (IOException ioe) {
				System.out.println("deu ruim no ServidorUDP");
			}
		}
	}

}
