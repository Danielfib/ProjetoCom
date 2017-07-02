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
import java.util.Arrays;

import javax.swing.JFrame;
import javax.xml.ws.EndpointReference;

public class ServidorUDP implements Runnable {
	
	private ArrayList<Chat> listaJanelas = new ArrayList<>();
	private ArrayList<String> listaIps = new ArrayList<>();
	static final int TAM_PKT = 100;
	static final int CABECALHO = 28;//se mudar, só mudar aqui
	
	public ServidorUDP(ArrayList<Chat> listaJanelas, ArrayList<String> listaIps) {
		this.listaJanelas = listaJanelas;
		this.listaIps = listaIps;
	}

	public void run() {
		int proxNumSeq = 0;
		int ultimoNumSeq = -1;
		
		try {
			int porta = 2020;
			DatagramSocket serverSocket = new DatagramSocket(porta);
			DatagramSocket sendSocket = new DatagramSocket();
			
			
			byte[] dados = new byte[TAM_PKT];
			DatagramPacket pacote = new DatagramPacket(dados, dados.length);
			
			//TRATAR PARA SE FOR UM ARQUIVO E N UMA MSG
			
			while (true) {
				serverSocket.receive(pacote);			
				
				String ipDestino = serverSocket.getInetAddress().getHostAddress();
				InetAddress ipDestinoInet = serverSocket.getInetAddress();
				String ipRemetente = serverSocket.getLocalAddress().getHostAddress();
				
				Pacote p = deserializeObject(pacote.getData());				
				
				int serverIsn = -2;

				if (p.syn) {
					Pacote synAck = new Pacote(0, 0, serverIsn, p.numSeq + 1, false, false, true, false, 0, 0, null);

					byte msgTcp[] = serializeObject(synAck);
					DatagramPacket pkt = new DatagramPacket(msgTcp, msgTcp.length, serverSocket.getInetAddress(), serverSocket.getPort());

					serverSocket.send(pkt);
				} else {
					if (p.numConfirmacao == (serverIsn + 1)) {
						// recebeu a 3 via
						
						//aqui eh pra abrir a janela caso alguem dê "conversar" comigo:
						Chat chat = new Chat(ipDestino, porta, ipRemetente);
						listaJanelas.add(chat);
						listaIps.add(ipRemetente); //é de p msm? ou de synack?
					} else {
						// Dados da aplicação
						//recebendo a mensagem, direcionando para o chat certo:
						for (int c = 0; c < listaIps.size(); c++){
							if (serverSocket.getInetAddress().getHostAddress().equals(listaIps.get(c))){ //se der bug, colocar ip do pacote
								listaJanelas.get(c).addText(new String(p.dados));
							}
						}
						//int numSeq = ByteBuffer.wrap(Arrays.copyOfRange(dados, 0, CABECALHO)).getInt();
						int numSeq = p.numSeq;
						
						//se o pacote recebido estiver em ordem:
						if (numSeq == proxNumSeq){
//							//enviar ack FIM caso seja o ultimo pacote (sem dados)
//							if (pacote.getLength() == CABECALHO){
//								byte[] ackFim = criarPacote(-2); //administrar o uso disso pra reconhecimento de ACK final
//								sendSocket.send(new DatagramPacket(ackFim, ackFim.length, ipDestinoInet, porta));
//								//boolean de transf = true
//							} else {
							proxNumSeq = numSeq + (TAM_PKT - CABECALHO);
							byte[] ack = criarPacote(proxNumSeq);
							sendSocket.send(new DatagramPacket(ack, ack.length, ipDestinoInet, porta));
							System.out.println("ack enviado: " + ack.toString()); //debug
							
							ultimoNumSeq = numSeq;
						} else {//se pacote estiver fora de ordem, manda o duplicado
							byte[] ackDuprikred = criarPacote(ultimoNumSeq);
							sendSocket.send(new DatagramPacket(ackDuprikred, ackDuprikred.length, ipDestinoInet, porta));
							System.out.println("ack duplicado enviado: " + ultimoNumSeq);
							
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
	
	public byte[] criarPacote(int numAck){
		//cria um array de bytes do numero do ack
		byte[] bytesAckNumber = ByteBuffer.allocate(4).putInt(numAck).array();
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.put(bytesAckNumber);
		return buffer.array();
		//cria um pacote em formato array de bytes com o inteiro passado
		
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