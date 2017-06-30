
//funções que n podem estar direto no cliente pq o cliente tem que estar ouvindo a Interface
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class GDPClient {
	int sendBase;
	int nextSeqNum;
	int state;
	DatagramSocket clientSocket;

	public GDPClient(int sendBase, int nextSeqNum, int state) throws SocketException {
		this.sendBase = sendBase;
		this.nextSeqNum = nextSeqNum;
		this.state = state;
		clientSocket = new DatagramSocket();
	}
	
	//apresentação de 3-vias
	public boolean startConection() {
		Pacote p = new Pacote(0, 0, 7, -1, false, false, true, false, 0, 0, "Teste".getBytes());
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(p);
			oos.close();
			byte msgTcp [] = baos.toByteArray();
			
			InetAddress ip = InetAddress.getByName("localhost");
			DatagramPacket pkt = new DatagramPacket(msgTcp, msgTcp.length, ip, 2020);

			clientSocket.send(pkt);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public void GDPsend(Pacote enviar, byte[] msg, InetAddress addr, int porta) throws SocketException {
		
	}
	//falta timeout
	

}
