
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class GDPClient {
	int sendBase;
	int nextSeqNum;
	byte[] pacote;
	DatagramSocket entradaSocket;
	DatagramSocket saidaSocket;

	public GDPClient(int sendBase, int nextSeqNum) throws SocketException {
		this.sendBase = sendBase;
		this.nextSeqNum = nextSeqNum;
		entradaSocket = new DatagramSocket(2021);
		saidaSocket = new DatagramSocket();
		//janela
		//Threads envio e recebimento
		//variáveis de estado
	}
	
	public class ThreadSaida implements Runnable {
		
		private DatagramSocket socketSaida;
		private int portDestino;
		private InetAddress ipDestino;
		
		public ThreadSaida(DatagramSocket socketSaida, int portDestino, String ipDestino) throws UnknownHostException {
			this.socketSaida = socketSaida;
			this.portDestino = portDestino;
			this.ipDestino = InetAddress.getByName(ipDestino);
		}

		@Override
		public void run() {
		}
		
	}
	
	public class ThreadEntrada implements Runnable {

		@Override
		public void run() {
			
		}
		
	}
}
