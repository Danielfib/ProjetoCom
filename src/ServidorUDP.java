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
	
<<<<<<< HEAD
	private ArrayList<JFrame> listaJanelas = new ArrayList<>();
	private ArrayList<String> listaIps = new ArrayList<>();
	
	public ServidorUDP (ArrayList<JFrame> oi, ArrayList<String> tudobom){
=======
	private ArrayList<JFrame> listaJanelas = new ArrayList<JFrame>();
	private ArrayList<String> listaIps = new ArrayList<String>();
	
	public ServidorUDP ( ArrayList<JFrame> oi, ArrayList<String> tudobom){
>>>>>>> 82a038c7a2e508d03bbd33ba6c35b733e8ceeab2
		this.listaJanelas = oi;
		this.listaIps = tudobom;
	}

	public void run() {
		try {
			DatagramSocket serverSocket = new DatagramSocket(2020);
<<<<<<< HEAD
=======
			DatagramSocket sendSocket = new DatagramSocket();
			
>>>>>>> 82a038c7a2e508d03bbd33ba6c35b733e8ceeab2
			byte[] dados = new byte[300];
			DatagramPacket pacote = new DatagramPacket(dados, dados.length);
			
			while (true) {
				serverSocket.receive(pacote);
				Pacote p = deserializeObject(pacote.getData());
				
				int serverIsn = -2;

				if (p.syn) {
					Pacote synAck = new Pacote(0, 0, serverIsn, p.numSeq + 1, false, false, true, false, 0, 0, null);

					byte msgTcp[] = serializeObject(synAck);
					DatagramPacket pkt = new DatagramPacket(msgTcp, msgTcp.length, serverSocket.getInetAddress(), serverSocket.getPort());

					serverSocket.send(pkt);
				} else {
					// recebeu a 3 via
					if (p.numConfirmacao == (serverIsn + 1)) {
<<<<<<< HEAD
=======
						// recebeu a 3 via
						System.out.println(p.ack);
>>>>>>> 82a038c7a2e508d03bbd33ba6c35b733e8ceeab2
						
						//aqui eh pra abrir a janela caso alguem dê "conversar" comigo:
						Chat aeporra = new Chat(serverSocket.getInetAddress().getHostAddress(), 2020, serverSocket.getLocalAddress().getHostAddress());
						listaJanelas.add(aeporra);
						listaIps.add(serverSocket.getLocalAddress().getHostAddress()); //é de p msm? ou de synack?
					} else {
						// Dados da aplicação
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