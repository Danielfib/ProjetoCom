import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class RttSend implements Runnable{
	String address;
	int port = 2051;
	
	public RttSend(String Address){
		this.address = Address;
	}
	
	@Override
	public void run(){
		while(true){
			try{
				byte[] data = "RTT".getBytes();
				GDPClient sender = new GDPClient(address, port);
				Pacote rttData = new Pacote(port, port, 0, 0,false,false,false,false,false,0,0,data);
				long probe = System.nanoTime();
				sender.listPacotes.add(rttData);
				GDPServer receiver = new GDPServer(new DatagramSocket());
				if(receiver.receiveConfirm){
					long rtt = System.nanoTime() - probe;
					Chat.setRTT(rtt);
				}
			} catch (IOException e) {
				System.out.println("IOException: " + e);
				System.out.println("RttSender");
			}
		}
	}
}
