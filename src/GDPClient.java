
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
			while(true) {
				/*
				 * se a janela não estiver cheia {
				 * 		se o temporizador estiver parado -> inicia temporizador
				 * 		envia o pacote e atualiza o nextSeqNum
				 * } senão {
				 * 		guarda msg em algum buffer e espera liberar espaço na janela pra enviar
				 * }
				 * se o temporizador expirou {
				 * 		retransmite o pacote ainda não reconhecido com o menor numSeq
				 * 		inicia temporizador
				 * }*/
			}
		}
		
	}
	
	public class ThreadEntrada implements Runnable {
		
		private DatagramSocket socketEntrada;
		
		public ThreadEntrada(DatagramSocket socketEntrada) {
			this.socketEntrada = socketEntrada;
		}

		@Override
		public void run() {
			while(true) {
				/*
				 * recebe ACK
				 * se ack não for duplicado {
				 * 		atualiza a janela
				 * 		se tiver algum pacote não reconhecido -> inicia o temporizador
				 * } senão {
				 * 		retransmissão rápida
				 * }
				 * */
			}
		}
		
	}
}
