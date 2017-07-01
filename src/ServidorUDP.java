import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.swing.JFrame;

public class ServidorUDP implements Runnable {
	
	private ArrayList<JFrame> listaJanelas = new ArrayList<JFrame>();
	private ArrayList<String> listaIps = new ArrayList<String>();
	
	public ServidorUDP ( ArrayList<JFrame> oi, ArrayList<String> tudobom){
		this.listaJanelas = oi;
		this.listaIps = tudobom;
	}

	public void run() {
		try {
			DatagramSocket serverSocket = new DatagramSocket(2020);
			DatagramSocket sendSocket = new DatagramSocket();
			
			byte[] dados = new byte[300];
			DatagramPacket pacote = new DatagramPacket(dados, dados.length);
			while (true) {
				serverSocket.receive(pacote);
				ByteArrayInputStream bao = new ByteArrayInputStream(pacote.getData());
				ObjectInputStream ous = new ObjectInputStream(bao);
				Pacote p = (Pacote) ous.readObject();

				int serverIsn = -2;
				Pacote synAck = new Pacote(0, 0, 0, 0, false, false, false, false, 0, 0, null);
				if (p.syn) {
					if (p.numConfirmacao == -1) {
						// envio da 2 via
						synAck.numConfirmacao = p.numSeq + 1;
						synAck.numSeq = serverIsn;
						synAck.syn = true;
					} else {
						// envia da 3 via
						synAck.numConfirmacao = p.numSeq + 1;
						synAck.numSeq = 8;
					}

					byte msgTcp[] = serializeObject(synAck);
					InetAddress ip = InetAddress.getByName("localhost");
					DatagramPacket pkt = new DatagramPacket(msgTcp, msgTcp.length, ip, 2020);

					sendSocket.send(pkt);
				} else {
					if (p.numConfirmacao == (serverIsn + 1)) {
						// recebeu a 3 via
						System.out.println(p.ack);
						
						//aqui eh pra abrir a janela caso alguem dê "conversar" comigo:
						Chat aeporra = new Chat(serverSocket.getInetAddress().getHostAddress(), 2020, serverSocket.getLocalAddress().getHostAddress());
						listaJanelas.add(aeporra);
						listaIps.add(serverSocket.getLocalAddress().getHostAddress()); //é de p msm? ou de synack?
					} else {
						// Dados da aplicação
					}
				}
			}
		} catch (IOException | ClassNotFoundException e) {
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

}