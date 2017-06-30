import java.net.SocketException;

public class ClienteUDP {

	public static void main(String[] args) throws SocketException {
		GDPClient g = new GDPClient(0, 0, 0);
		g.startConection();
	}

}
