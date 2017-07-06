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
import java.util.Random;

public class GDPServer implements Runnable {

	DatagramSocket socket;
	Chat chat;

	/*private Comparator comparador = new NumSeqComparator();
	private PriorityQueue<Pacote> PQPacotes = new PriorityQueue<Pacote>(comparador);*/
	
	private ArrayList<Pacote> bufferRcv = new ArrayList<>();

	static final int CABECALHO = 29;// se mudar, só mudar aqui
	static final int TAM_DADOS = 1000;
	static final int TAM_PKT = TAM_DADOS + CABECALHO;
	final int RCV_BUFFER = 1000000;
	static final int USER_PERCENT = 77;

	public GDPServer(DatagramSocket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		int proxNumSeq = 0;
		int ultimoNumSeq = -1;
		int rwnd= RCV_BUFFER;
		int contPktPerdidos = 0;
		int lastByteRcvd = -TAM_DADOS;
		int lastByteRead = -TAM_DADOS - 1;
		
		byte[] dados = new byte[TAM_PKT];
		DatagramPacket pkt = new DatagramPacket(dados, dados.length);

		while (true) {

			try {
				socket.receive(pkt);
				
				InetAddress ipDestinoInet = pkt.getAddress();
				
				if(pkt.getLength() == 1) {
					byte[] ack = serializeObject(new Pacote(socket.getLocalPort(), pkt.getPort(), -1, -1, true, false, false, false, 
							false, rwnd, 0, null));
					
					socket.send(new DatagramPacket(ack, ack.length, ipDestinoInet, pkt.getPort()));
					continue;
				}


				Pacote p = deserializeObject(pkt.getData());
				System.out.println("Server: " + new String(p.dados));
				
				Random r = new Random();

				if(r.nextInt(99) < USER_PERCENT) {
					
					int numSeq = p.numSeq;
					
					if(!bufferRcv.isEmpty() && (p.numSeq < bufferRcv.get(bufferRcv.size() - 1).numSeq)) {
						bufferRcv.add((p.numSeq / TAM_DADOS), p); // adiciona no buffer						
					} else {
						bufferRcv.add(p);
						lastByteRcvd = p.numSeq + TAM_DADOS - 1;
					}
					
					// se o pacote recebido estiver em ordem:
					if (numSeq == proxNumSeq) {
	
						for (Pacote b : bufferRcv) {
							if(b.numSeq == (lastByteRead + TAM_DADOS)) {
								chat.addText(new String(p.dados) + "\n"); //entrega a aplicação
								lastByteRead += TAM_DADOS;
								bufferRcv.remove(lastByteRead / TAM_DADOS);
							} else {
								break;
							}
						}
						
						rwnd = RCV_BUFFER - (lastByteRcvd - lastByteRead); //tamanho da janela de recepção -> mandar para o remetente
						
						proxNumSeq = lastByteRead + TAM_DADOS;
						
						//byte[] ack = criarPacote(proxNumSeq);
						
						byte[] ack = serializeObject(new Pacote(socket.getLocalPort(), p.portOrigem, -1, proxNumSeq, true, 
								false, false, false, false, rwnd, 0, null));
						
						socket.send(new DatagramPacket(ack, ack.length, ipDestinoInet, p.portOrigem));
						System.out.println("ack enviado: " + proxNumSeq); // debug
	
						ultimoNumSeq = proxNumSeq;
					} else {
						//byte[] ackDuprikred = criarPacote(ultimoNumSeq);
						
						byte[] ackDuprikred = serializeObject(new Pacote(socket.getLocalPort(), p.portOrigem, -1, ultimoNumSeq, true, 
								false, false, false, false, rwnd, 0, null));
						
						socket.send(new DatagramPacket(ackDuprikred, ackDuprikred.length, ipDestinoInet, p.portOrigem));
						System.out.println("ack duplicado enviado: " + ultimoNumSeq);
					}
				} else {
					contPktPerdidos++;
					chat.label.setText(contPktPerdidos + "");
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
