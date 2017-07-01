import java.net.SocketException;

public class ClienteUDP implements Runnable{

	public void run() {
		try {
			GDPClient g;
			g = new GDPClient(0, 0, 0);
			g.startConection();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
