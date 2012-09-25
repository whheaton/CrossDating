package treeRingAlignment;

public class SumOfSquaresRingScorer implements RingScorer {
	
	public SumOfSquaresRingScorer(){
		
	}

	@Override
	public int score(double master, double series, int skippedmaster, int skippedring) {
		int score = (int)(-1000*Math.pow((master-series),2));
		return  score - 10000*skippedring - 4000*skippedmaster  + 1500;
	}

}
