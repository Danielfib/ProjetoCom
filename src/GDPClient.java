
//funções que n podem estar direto no cliente pq o cliente tem que estar ouvindo a Interface
import java.net.DatagramSocket;
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
		clientSocket = new DatagramSocket(2021);
	}

	
	
	public void GDPsend(Pacote pacote, String addr, int porta) {
	}
}
