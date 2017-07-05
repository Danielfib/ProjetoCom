import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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

public class ServidorUDP implements Runnable {

	private ArrayList<Chat> listaJanelas = new ArrayList<>();
	private ArrayList<String> listaIps = new ArrayList<>();

	Comparator comparador = new NumSeqComparator();
	private PriorityQueue<Pacote> PQPacotes = new PriorityQueue<Pacote>(comparador);

	static final int CABECALHO = 28;// se mudar, só mudar aqui
	static final int TAM_PKT = 1000 + CABECALHO;
	final String diretorioTeste = "C:/Users/danie/Desktop";

	public ServidorUDP(ArrayList<Chat> listaJanelas, ArrayList<String> listaIps) {
		this.listaJanelas = listaJanelas;
		this.listaIps = listaIps;
	}

	public void run() {
		int proxNumSeq = 0;
		int ultimoNumSeq = -1;

		try {
			FileOutputStream fos = null;
			int porta = 2032;

			DatagramSocket serverSocket = new DatagramSocket(porta);
			DatagramSocket sendSocket = new DatagramSocket();

			byte[] dados = new byte[TAM_PKT];
			DatagramPacket pacote = new DatagramPacket(dados, dados.length);

			while (true) {
				serverSocket.receive(pacote);

				InetAddress ipDestinoInet = pacote.getAddress();
				String ipDestino = ipDestinoInet.getHostAddress();

				int portaDestino = pacote.getPort();

				Pacote p = deserializeObject(pacote.getData());

				int serverIsn = -2;

				if (p.syn) {
					Pacote synAck = new Pacote(porta, portaDestino, serverIsn, p.numSeq + 1, false, false, true, false,
							false, 0, 0, "Teste 2".getBytes());

					byte msgTcp[] = serializeObject(synAck);
					DatagramPacket pkt = new DatagramPacket(msgTcp, msgTcp.length, ipDestinoInet, portaDestino);

					System.out.println(new String(p.dados));

					serverSocket.send(pkt);
				} else {
					if (p.numConfirmacao == (serverIsn + 1)) {
						// recebeu a 3 via

						System.out.println(new String(p.dados));

						Chat chat = new Chat(ipDestino, 2045);

						listaJanelas.add(chat);
						listaIps.add(ipDestino);
						chat.NewScreen(chat);

					} else {
						// Dados da aplicação

						// PQPacotes.add(p);// adiciona no buffer
						if (p.isFile) {
							//se o ngc enviado for um arquivo
							if (fos == null){
								//se for o primeiro pacote, cria o arquivo
								String caminho = p.dados.toString();
								File arquivo = new File(caminho);
								if(!arquivo.exists()){
									arquivo.createNewFile();
								}
								fos = new FileOutputStream(arquivo);
							} else {
								//se n for o primeiro pacote do arquivo
								if (p.numSeq == proxNumSeq){
									//se o pacote estiver em ordem
									if (pacote.getLength() == CABECALHO){
										//se for o ultimo pacote do arquivo
										byte[] ackFim = criarPacote(-2);
										sendSocket.send(new DatagramPacket(ackFim, ackFim.length, ipDestinoInet, portaDestino));
										System.out.println("Arquivo Transferido");
									} else {
										//se n for o ultimo pacote do arquivo
										proxNumSeq = p.numSeq + (TAM_PKT - CABECALHO); //atualiza prox numero esperado na sequencia
										byte[] pacoteAck = criarPacote(proxNumSeq);
										sendSocket.send(new DatagramPacket(pacoteAck, pacoteAck.length, ipDestinoInet, portaDestino));
										System.out.println("ack de arquivo enviado: " + proxNumSeq);
									}
									
								}
								//escreve dados no arquivo
							}
							fos.write(dados, CABECALHO, pacote.getLength() - CABECALHO);;
							
						} else {

							int numSeq = p.numSeq;

							// se o pacote recebido estiver em ordem:
							if (numSeq == proxNumSeq) {

								proxNumSeq = numSeq + (TAM_PKT - CABECALHO);
								byte[] ack = criarPacote(proxNumSeq);
								sendSocket.send(new DatagramPacket(ack, ack.length, ipDestinoInet, 2045));
								System.out.println("ack enviado: " + proxNumSeq); // debug

								listaJanelas.get(0).addText(new String(p.dados));
								ultimoNumSeq = proxNumSeq;
							} else {
								byte[] ackDuprikred = criarPacote(ultimoNumSeq);
								sendSocket.send(
										new DatagramPacket(ackDuprikred, ackDuprikred.length, ipDestinoInet, 2045));
								System.out.println("ack duplicado enviado: " + ultimoNumSeq);

							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
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