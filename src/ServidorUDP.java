import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServidorUDP {

	public static void main(String[] args) {
		try {
			DatagramSocket serverSocket = new DatagramSocket(2020);
			byte[] dados = new byte[400];
			DatagramPacket pacote = new DatagramPacket(dados, dados.length);
			while(true) {
				System.out.println("1");
				serverSocket.receive(pacote);
				System.out.println("2");
				ByteArrayInputStream bao = new ByteArrayInputStream(pacote.getData());                
				ObjectInputStream ous = new ObjectInputStream(bao);
				System.out.println("3");
                Pacote p = (Pacote) ous.readObject();
                System.out.println("4");
                System.out.println(p.syn);
               
//                System.out.println(pacote.getAddress());
//                System.out.println(pacote.getSocketAddress());
                
                int server_isn = -2;
                if(p.syn) {
                	if(server_isn == -1) {
                		//primeira via e envia a segunda                		
                	} else {
                		//envia terceira via
                	}
                } else {
                	//recebeu 3 via
                }
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
