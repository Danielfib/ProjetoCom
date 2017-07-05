
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class GDPClient {

	int sendBase;
	int nextSeqNum;

	String ipDestino;
	int portDestino;

	static int tamanhoJanela = 4;
	static final int TAMANHO_PACOTE = 1000;

	Pacote pacote;
	ArrayList<byte[]> listPacotes;

	DatagramSocket entradaSocket;
	DatagramSocket saidaSocket;
	
	public GDPClient(String ipDestino, int portDestino) throws SocketException {

		this.sendBase = 0;
		this.nextSeqNum = 0;
		this.pacote = new Pacote(-1, -1, -1, 0, false, false, false, false, false, 0, 0, null);

		this.ipDestino = ipDestino;
		this.portDestino = portDestino;

		listPacotes = new ArrayList<>(tamanhoJanela);

		try {
			entradaSocket = new DatagramSocket(2045);
			saidaSocket = new DatagramSocket();

			ThreadEntrada threadIn = new ThreadEntrada(entradaSocket);
			ThreadSaida threadOut = new ThreadSaida(saidaSocket, portDestino, ipDestino);

			threadIn.start();
			threadOut.start();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		// variáveis de estado
	}

	public class ThreadSaida extends Thread {

		private DatagramSocket socketSaida;
		private int portDestino;
		private InetAddress ipDestino;

		public ThreadSaida(DatagramSocket socketSaida, int portDestino, String ipDestino) throws UnknownHostException {
			this.socketSaida = socketSaida;
			this.portDestino = portDestino;
			this.ipDestino = InetAddress.getByName(ipDestino);
		}

		public Pacote deserializeObject(byte[] dado) {
			ByteArrayInputStream bao = new ByteArrayInputStream(dado);
			ObjectInputStream ous;
			try {
				ous = new ObjectInputStream(bao);
				return (Pacote) ous.readObject();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			return null;
		}

		public byte[] serializeObject(Pacote p) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos;
			byte msgTcp[] = null;
			try {
				oos = new ObjectOutputStream(baos);
				oos.writeObject(p);
				oos.close();
				msgTcp = baos.toByteArray();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return msgTcp;
		}

		@Override
		public void run() {
			try {
				int ultimoNumSeq = 0;
				while (true) {
					byte[] segmento = null;
					if(ultimoNumSeq != pacote.numSeq) {
						pacote.numSeq = nextSeqNum;
						if (pacote.numSeq >= sendBase) {
							if (nextSeqNum < sendBase + (tamanhoJanela * TAMANHO_PACOTE)) {
								if (nextSeqNum == sendBase) {
									// inicia timer
								}
								if ((nextSeqNum / TAMANHO_PACOTE) < listPacotes.size()) {
									segmento = listPacotes.get(nextSeqNum / TAMANHO_PACOTE);
								} else { // se não for retransmissão
									segmento = serializeObject(pacote);
									listPacotes.add(segmento);
								}
								
								nextSeqNum += TAMANHO_PACOTE;
								socketSaida.send(new DatagramPacket(segmento, segmento.length, ipDestino, portDestino));
							} else {
								listPacotes.add(segmento);
							}
						}
					}
					ultimoNumSeq = pacote.numSeq;
					sleep(7);
				}
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			} finally {
				// mata thread do timer
				socketSaida.close();
			}
		}

	}

	public class ThreadEntrada extends Thread {

		private DatagramSocket socketEntrada;

		public ThreadEntrada(DatagramSocket socketEntrada) {
			this.socketEntrada = socketEntrada;
		}
		
		int getnumAck(byte[] pacote) {
            byte[] numAckBytes = Arrays.copyOfRange(pacote, 0, 28);
            return ByteBuffer.wrap(numAckBytes).getInt();
        }


		@Override
		public void run() {
			byte[] ack = new byte[4];
			DatagramPacket packet = new DatagramPacket(ack, ack.length);
			while (true) {
				try {
					socketEntrada.receive(packet);
					int numAck = getnumAck(ack);
					System.out.println(numAck);
					// ACK duplicado
					if (sendBase == numAck) {
						nextSeqNum = sendBase;
					} else { // ACK normal
						sendBase = numAck;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}