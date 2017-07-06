
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

public class GDPClient {

	int sendBase;
	int nextSeqNum;

	String ipDestino;
	int portDestino;
	
    Timer timer;
    Semaphore semaforo;

	static int tamanhoJanela = 4;
	static final int TAMANHO_PACOTE = 1000;

	ArrayList<Pacote> listPacotes;

	DatagramSocket entradaSocket;
	DatagramSocket saidaSocket;
	int janelaRecepcao;
	int lastByteSent;
	int lastByteAcked;
	
	private static int porta = 2045;

	public GDPClient(String ipDestino, int portDestino) throws SocketException {

		this.sendBase = 0;
		this.nextSeqNum = 0;

		this.ipDestino = ipDestino;
		this.portDestino = portDestino;
		
		semaforo = new Semaphore(1);

		listPacotes = new ArrayList<>(tamanhoJanela);
		
		janelaRecepcao  = 1000000;
		lastByteSent = -1;
		lastByteAcked = -1;

		try {
			entradaSocket = new DatagramSocket(porta);
			porta += 5;
			saidaSocket = new DatagramSocket();

			ThreadEntrada threadIn = new ThreadEntrada(entradaSocket);
			ThreadSaida threadOut = new ThreadSaida(saidaSocket, portDestino, ipDestino);

			threadIn.start();
			threadOut.start();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		// variáveis de estado
	}
	
    public class Temporizador extends TimerTask {
    	 
        public void run() {
            try {
                semaforo.acquire();
                System.out.println("Cliente: Tempo expirado!");
                nextSeqNum = sendBase;  //reseta numero de sequencia
                semaforo.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
 
    //para iniciar ou parar o temporizador
    public void manipularTemporizador(boolean novoTimer) {
        if (timer != null) {
            timer.cancel();
        }
        if (novoTimer) {
            timer = new Timer();
            timer.schedule(new Temporizador(), 1000);
        }
    }

	public class ThreadSaida extends Thread {

		private DatagramSocket socketSaida;
		private int portDestino;
		private InetAddress ipDestino;

		public ThreadSaida(DatagramSocket socketSaida, int portDestino, String ipDestino) throws UnknownHostException {
			this.socketSaida = socketSaida;
			this.portDestino = portDestino;
			this.ipDestino = InetAddress.getByName(ipDestino);
		}

		@Override
		public void run() {
			try {
				int ultimoNumSeq = -1;
				Pacote pacote;
				while (true) {
					if(janelaRecepcao >= (lastByteSent - lastByteAcked)) {
						byte[] segmento = null;
						if (!listPacotes.isEmpty() && (listPacotes.size() > (nextSeqNum / TAMANHO_PACOTE))) {
							pacote = listPacotes.get(nextSeqNum / TAMANHO_PACOTE);
							if (ultimoNumSeq != pacote.numSeq || sendBase == nextSeqNum) {
								pacote.portOrigem = entradaSocket.getLocalPort();
								if (pacote.numSeq >= sendBase) {
									if (nextSeqNum < sendBase + (tamanhoJanela * TAMANHO_PACOTE)) {
										semaforo.acquire();
										if (nextSeqNum == sendBase) {
											// inicia timer
											manipularTemporizador(true);
										}
										segmento = serializeObject(pacote);
	
										lastByteSent = pacote.numSeq;
										nextSeqNum += TAMANHO_PACOTE;
										socketSaida.send(
												new DatagramPacket(segmento, segmento.length, ipDestino, portDestino));
										ultimoNumSeq = pacote.numSeq;
										
										semaforo.release();
										
									}
								}
							}
						}
					} else if(janelaRecepcao == 0) {
						//envia bytes de monitoramento para saber quando a janela diminuirá
						byte[] enviaDados = new byte[0];
						socketSaida.send(new DatagramPacket(enviaDados, enviaDados.length, ipDestino, portDestino));
					}
					sleep(7);
				}
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			} finally {
				// mata thread do timer
				socketSaida.close();
			}
		}

	}

	public class ThreadEntrada extends Thread {

		private DatagramSocket socketEntrada;

		public ThreadEntrada(DatagramSocket socketEntrada) {
			this.socketEntrada = socketEntrada;
		}

		int getnumAck(byte[] pacote) {
			byte[] numAckBytes = Arrays.copyOfRange(pacote, 0, 28);
			return ByteBuffer.wrap(numAckBytes).getInt();
		}

		@Override
		public void run() {
			byte[] ack = new byte[TAMANHO_PACOTE];
			DatagramPacket packet = new DatagramPacket(ack, ack.length);
			while (true) {
				try {
					socketEntrada.receive(packet);
					Pacote Ack = deserializeObject(packet.getData());
					
					janelaRecepcao = Ack.janelaReceptor;
					
					System.out.println(Ack.numConfirmacao);
					
					// ACK duplicado
					if (sendBase == Ack.numConfirmacao) {
						semaforo.acquire();
                        manipularTemporizador(false);
						nextSeqNum = sendBase;
						semaforo.release();
					} else { // ACK normal
						sendBase = Ack.numConfirmacao;
						semaforo.acquire();
						if(sendBase == nextSeqNum) {
							manipularTemporizador(false);
						} else {
							manipularTemporizador(true);
						}
						semaforo.release();
					}
					
					lastByteAcked = sendBase;
					
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

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

	public byte[] serializeObject(Pacote p) {
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