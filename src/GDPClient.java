
//funções que n podem estar direto no cliente pq o cliente tem que estar ouvindo a Interface
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;
import java.net.InetAddress;

public class GDPClient {
	int sendBase;
	int nextSeqNum;
	int state;

	public GDPClient(int sendBase, int nextSeqNum, int state) throws SocketException {
		this.sendBase = sendBase;
		this.nextSeqNum = nextSeqNum;
		this.state = state;
	}

	public static void GDPsend(Pacote enviar, InetAddress addr, int porta) throws SocketException {
		try {
			DatagramSocket clientSocket = new DatagramSocket();
			DatagramPacket pkg = new DatagramPacket(enviar.dados, enviar.dados.length, addr, porta);
			clientSocket.send(pkg);
			clientSocket.close();
		} catch (IOException ioe) {
			System.out.println("deu ruim no gdpclient");
		}
	}
	//falta timeout
	

}
