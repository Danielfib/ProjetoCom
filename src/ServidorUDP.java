import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServidorUDP {

	public static void main(String[] args) {
		try {
			DatagramSocket serverSocket = new DatagramSocket(2020);
<<<<<<< HEAD
			byte[] dados = new byte[400];
=======
			DatagramSocket sendSocket = new DatagramSocket();
			byte[] dados = new byte[300];
>>>>>>> a6ee30a73e3101b90de658b795be94c2f2baf2a9
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
                
<<<<<<< HEAD
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
=======
                int serverIsn = -2;
                Pacote synAck = new Pacote(0, 0, 0, 0, 
                		false, false, false, false, 0, 0, null);
                if(p.syn) {
                	if(p.numConfirmacao == -1) {
                		//envio 2 via
                		synAck.numConfirmacao = p.numSeq + 1;
                		synAck.numSeq = serverIsn;
                		synAck.syn = true;
                	} else {
                		//envia 3 via
                		synAck.numConfirmacao = p.numSeq + 1;
                		synAck.numSeq = 8;
                	}
                	
                	byte msgTcp [] = serializeObject(synAck);
                	InetAddress ip = InetAddress.getByName("localhost");
        			DatagramPacket pkt = new DatagramPacket(msgTcp, msgTcp.length, ip, 2020);
        			
        			sendSocket.send(pkt);
                } else {
                	if(p.numConfirmacao == (serverIsn + 1)) {
                		//recebeu 3 via
                		System.out.println(p.ack);
                	} else {
                		//Dados aplicação
                	}
        		}
>>>>>>> a6ee30a73e3101b90de658b795be94c2f2baf2a9
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static byte[] serializeObject(Pacote p) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		byte msgTcp [] = null;
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
