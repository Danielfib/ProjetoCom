import java.util.ArrayList;


public class Pacote {

	int portOrigem;
	int portDestino;
	int numSeq;
	int numConfirmacao;
	boolean ack;
	boolean rst;
	boolean syn;
	boolean fin;
	//janelaReceptor;
	int checkSum;
	ArrayList<Byte> dados;
	
	public Pacote(int portOrigem, int portDestino, int numSeq,
			int numConfirmacao, boolean ack, boolean rst, boolean syn,
			boolean fin, int checkSum, ArrayList<Byte> dados) {
		this.portOrigem = portOrigem;
		this.portDestino = portDestino;
		this.numSeq = numSeq;
		this.numConfirmacao = numConfirmacao;
		this.ack = ack;
		this.rst = rst;
		this.syn = syn;
		this.fin = fin;
		this.checkSum = checkSum;
		this.dados = dados;
	}
	
	//pendente
	public int calcCheckSum() {
		return 0;
	}
	
}
