import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class ClienteUDP extends Thread {

	private String ipDestino;
	private int portDestino;
	private ArrayList<Chat> listaJanelas = new ArrayList<>();
	private ArrayList<String> listaIps = new ArrayList<>();
	
	private DatagramSocket clientSocket;
	
	public ClienteUDP(String ipDestino, int portDestino, ArrayList<Chat> listaJanelas, ArrayList<String> listaIps) {
		this.ipDestino = ipDestino;
		this.portDestino = portDestino;
		this.listaJanelas = listaJanelas;
		this.listaIps = listaIps;
		try {
			clientSocket = new DatagramSocket(2020);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		if(startConection()) {
			Chat chat = new Chat(ipDestino, portDestino);
			
			listaJanelas.add(chat);
			listaIps.add(ipDestino);
			
			chat.NewScreen(chat);
		}
	}

	public boolean startConection() {
		Pacote p = new Pacote(2020, portDestino, 7, -1, false, false, true, false, false, 0, 0, "Teste".getBytes());

		try {

			// enviando 1 via
			byte msgTcp[] = serializeObject(p);
			InetAddress ip = InetAddress.getByName(ipDestino);
			DatagramPacket pkt = new DatagramPacket(msgTcp, msgTcp.length, ip, portDestino);
			clientSocket.send(pkt);

			// recebendo 2 via
			byte[] dados = new byte[300];
			DatagramPacket pacote = new DatagramPacket(dados, dados.length);
			clientSocket.receive(pacote);
			Pacote receiveP = deserializeObject(pacote.getData());
			System.out.println(new String(receiveP.dados));

			// enviando 3 via
			p.dados = "Teste 3".getBytes();
			p.syn = false;
			p.numSeq = receiveP.numConfirmacao + 1;
			p.numConfirmacao = receiveP.numSeq + 1;
			msgTcp = serializeObject(p);

			pkt.setData(msgTcp);
			pkt.setLength(msgTcp.length);
			clientSocket.send(pkt);
			
			return true;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
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

}