package treeRingAlignment;

public class PearsonCorrelationScorer implements RingScorer {

	/**
	 * For this scoring method, we assume that only residuals are used.
	 */
	@Override
	public int score(double master, double series, int skippedmaster,
			int skippedseries) {
		return (int)(1000*master*series - (skippedseries*150000 + skippedmaster*1000));
	}

}
