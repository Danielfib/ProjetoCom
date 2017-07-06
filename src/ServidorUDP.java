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

public class ServidorUDP implements Runnable {

	public void run() {

		try {
			int porta = 2022;

			DatagramSocket serverSocket = new DatagramSocket(porta);

			byte[] dados = new byte[1029];
			DatagramPacket pacote = new DatagramPacket(dados, dados.length);

			while (true) {
				serverSocket.receive(pacote);

				InetAddress ipDestinoInet = pacote.getAddress();
				String ipDestino = ipDestinoInet.getHostAddress();

				int portaDestino = pacote.getPort();

				Pacote p = deserializeObject(pacote.getData());

				int serverIsn = -2;
				
				GDPServer serverGdp = new GDPServer(new DatagramSocket());

				if (p.syn) {
					Pacote synAck = new Pacote(serverGdp.socket.getLocalPort(), portaDestino, serverIsn, p.numSeq + 1, false, false, true, false,
							false, 0, 0, "Teste 2".getBytes());

					byte msgTcp[] = serializeObject(synAck);
					DatagramPacket pkt = new DatagramPacket(msgTcp, msgTcp.length, ipDestinoInet, portaDestino);
					
					
					//System.out.println(new String(p.dados));

					serverSocket.send(pkt);
				} else {
					//recebeu a 3 via

					//System.out.println(new String(p.dados));
						
					Chat chat = new Chat(ipDestino, p.portOrigem);

					serverGdp.chat = chat;
						
					chat.NewScreen(chat);
					
					new Thread(serverGdp).start();
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