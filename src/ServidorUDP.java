import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServidorUDP {

	public static void main(String[] args) {
		try {
			DatagramSocket serverSocket = new DatagramSocket(2020);
			byte[] dados = new byte[200];
			DatagramPacket pacote = new DatagramPacket(dados, dados.length);
			while(true) {
				serverSocket.receive(pacote);
				ByteArrayInputStream bao = new ByteArrayInputStream(pacote.getData());
                ObjectInputStream ous = new ObjectInputStream(bao);
                Pacote p = (Pacote) ous.readObject();
                
                if(p.syn) {
                	if(p.numSeq == 7) {
                		//primeira via e envia a segunda
                	} else {
                		//terceira via
                	}
                }
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
