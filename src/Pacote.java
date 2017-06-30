import java.io.Serializable;

public class Pacote implements Serializable {
	int portOrigem;
	int portDestino;
	int numSeq;
	int numConfirmacao;
	boolean ack;
	boolean rst;
	boolean syn;
	boolean fin;
	int janelaReceptor;
	int checkSum;
	byte[] dados;
	
	public Pacote(int portOrigem, int portDestino, int numSeq, int numConfirmacao, boolean ack, boolean rst,
			boolean syn, boolean fin, int janelaReceptor, int checkSum, byte[] dados) {
		this.portOrigem = portOrigem;
		this.portDestino = portDestino;
		this.numSeq = numSeq;
		this.numConfirmacao = numConfirmacao;
		this.ack = ack;
		this.rst = rst;
		this.syn = syn;
		this.fin = fin;
		this.janelaReceptor = janelaReceptor;
		this.checkSum = checkSum;
		this.dados = dados;
	}

}
