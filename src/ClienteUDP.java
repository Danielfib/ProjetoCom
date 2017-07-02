import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ClienteUDP implements Runnable{

	public void run() {
		DatagramSocket clientSocket;
		try {
			clientSocket = new DatagramSocket(2021);
			DatagramSocket socketSaida = new DatagramSocket();
			
			startConection(clientSocket);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void startConection(DatagramSocket clientSocket) {
		Pacote p = new Pacote(0, 0, 7, -1, false, false, true, false, 0, 0, "Teste".getBytes());
		
		try {
			
			//enviando 1 via
			byte msgTcp [] = serializeObject(p);
			InetAddress ip = InetAddress.getByName("localhost");
			DatagramPacket pkt = new DatagramPacket(msgTcp, msgTcp.length, ip, 2020);
			clientSocket.send(pkt);
			
			//recebendo 2 via
			byte[] dados = new byte[300];
			DatagramPacket pacote = new DatagramPacket(dados, dados.length);
			clientSocket.receive(pacote);
			Pacote receiveP = deserializeObject(pacote.getData());
			
			//enviando 3 via
			p.syn = false;
			p.numSeq = receiveP.numConfirmacao + 1;
			p.numConfirmacao = receiveP.numSeq + 1;
			msgTcp = serializeObject(p);
			
			pkt.setData(msgTcp);
			pkt.setLength(msgTcp.length);
			clientSocket.send(pkt);
			
			//Cria Chat
			
		} catch(IOException e) {
			e.printStackTrace();
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

}