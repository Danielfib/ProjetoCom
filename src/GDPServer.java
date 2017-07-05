import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class GDPServer implements Runnable {

	DatagramSocket socket;
	Chat chat;
	boolean receiveConfirm = false;

	/*private Comparator comparador = new NumSeqComparator();
	private PriorityQueue<Pacote> PQPacotes = new PriorityQueue<Pacote>(comparador);*/
	
	private ArrayList<Pacote> bufferRcv = new ArrayList<>();

	static final int CABECALHO = 28;// se mudar, só mudar aqui
	static final int TAM_PKT = 1000 + CABECALHO;

	public GDPServer(DatagramSocket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		while (true) {
			int proxNumSeq = 0;
			int ultimoNumSeq = -1;

			byte[] dados = new byte[TAM_PKT];
			DatagramPacket pkt = new DatagramPacket(dados, dados.length);

			try {
				socket.receive(pkt);
				receiveConfirm = true;
				InetAddress ipDestinoInet = pkt.getAddress();

				Pacote p = deserializeObject(pkt.getData());

				bufferRcv.add((p.numSeq / (TAM_PKT - CABECALHO)), p); // adiciona no buffer

				int numSeq = p.numSeq;

				// se o pacote recebido estiver em ordem:
				if (numSeq == proxNumSeq) {

					chat.addText(new String(p.dados) + "\n");
					proxNumSeq = numSeq + (TAM_PKT - CABECALHO);
					byte[] ack = criarPacote(proxNumSeq);
					socket.send(new DatagramPacket(ack, ack.length, ipDestinoInet, 2045));
					System.out.println("ack enviado: " + proxNumSeq); // debug

					ultimoNumSeq = proxNumSeq;
				} else {
					byte[] ackDuprikred = criarPacote(ultimoNumSeq);
					socket.send(new DatagramPacket(ackDuprikred, ackDuprikred.length, ipDestinoInet, 2045));
					System.out.println("ack duplicado enviado: " + ultimoNumSeq);

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static byte[] serializeObject(Pacote p) {
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

	public byte[] criarPacote(int numAck) {
		byte[] bytesAckNumber = ByteBuffer.allocate(4).putInt(numAck).array();
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.put(bytesAckNumber);
		return buffer.array();

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
}
