import java.util.Comparator;

public class NumSeqComparator implements Comparator<Pacote> {

	@Override
	public int compare(Pacote p1, Pacote p2) {
		if (p1.numSeq < p2.numSeq){
			return -1;
		} else {
			return 1;
		}
	}
	
}
