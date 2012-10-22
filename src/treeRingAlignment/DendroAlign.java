package treeRingAlignment;

import java.util.HashMap;
import java.util.TreeMap;

import dataStructures.MapAlignment;
import dataStructures.RingSeries;
import dataStructures.TreeRingSeries;

public class DendroAlign implements Aligner{

	private RingScorer the_scorer;
	private int[][] score_matrix;
	private int[][] backtrace_a;
	private int[][] backtrace_b;
	private int[][] path_length;
	private int max_gappage = 1;

	public DendroAlign(RingScorer score){
		the_scorer = score;
		score_matrix = new int[40][40];
		backtrace_a = new int[40][40];
		backtrace_b = new int[40][40];
		path_length = new int[40][40];
	}


	@Override
	public MapAlignment align(RingSeries a, RingSeries b) {
		doubleIfNeeded(a.length()+1,b.length()+1);



		int length = 0;
		for(int adex = 0; adex < a.length(); adex++){
			for(int bdex = 0; bdex < b.length(); bdex++){
				//if(adex == 2)System.exit(0);
				int bestscore = Integer.MIN_VALUE;
				int bestadex = -1;
				int bestbdex = -1;
				int bestlength = 0;
				//System.out.println("At adex "+adex+" and bdex "+bdex);
				for(int aa = 1; aa <= max_gappage +1; aa++){
					for(int bb = 1; bb <= max_gappage - aa + 2; bb++){

						int pair0 = adex - aa;
						int pair1 = bdex - bb;
						if(aa + bb - 2 > max_gappage)break;
						int localscore = 0;
						if(pair0 < 0 && pair1 < 0){}
						if(pair0 < 0){
							//localscore = the_scorer.score(0,0,pair1);
							//	System.out.println("confused, localscore = "+localscore+" and pair1 "+pair1);
						}
						else if(pair1 < 0){
							//localscore = the_scorer.score(0,0,pair0);
						}
						else{
							localscore = score_matrix[pair0][pair1] + 
									the_scorer.score(a.getRadius(adex)-a.getRadius(pair0), b.getRadius(bdex)-b.getRadius(pair1), aa-1,bb-1);

						}
//						if(the_scorer instanceof PearsonCorrelationScorer){
//							if(localscore/(length+aa+bb) > bestscore){
//								bestscore = localscore;
//								bestadex = pair0;
//								bestbdex = pair1;
//								bestlength = length+aa+bb;
//							}
//						}
//						else{
							if(localscore > bestscore){
								bestscore = localscore;
								bestadex = pair0;
								bestbdex = pair1;

							}
//						}
						//	System.out.println("\tLooking back at "+pair0+", "+pair1+" and localscore is "+localscore +" and best score is "+bestscore);
					}
				}

				score_matrix[adex][bdex] = bestscore;
				backtrace_a[adex][bdex] = bestadex;
				backtrace_b[adex][bdex] = bestbdex;
				length = bestlength;
				path_length[adex][bdex] = length;
			}
		}

		System.out.println("\n\nScore matrix");
		for(int ii = 0; ii < a.length(); ii ++){
			for(int jj = 0; jj < b.length(); jj++){
				System.out.print(score_matrix[ii][jj]+"\t");
			}System.out.println();

		}System.out.println();

		System.out.println("\n\nBacktrace");
		for(int ii = 0; ii < a.length(); ii ++){
			for(int jj = 0; jj < b.length(); jj++){
				System.out.print("["+backtrace_a[ii][jj]+","+backtrace_b[ii][jj]+"]\t");
			}System.out.println();

		}System.out.println();

		MapAlignment toReturn = backtrace(a,b);




		return toReturn;
	}


	private MapAlignment backtrace(RingSeries a, RingSeries b) {
		TreeMap<Integer,Integer> map = new TreeMap<Integer,Integer>();

		int maxScore = Integer.MIN_VALUE;
		int adex = -1;
		int bdex = -1;
		for(int ii = 0; ii < a.length(); ii++){
//			if(the_scorer instanceof PearsonCorrelationScorer){
//				if(score_matrix[ii][b.length()-1]/path_length[ii][b.length()-1] > maxScore){
//					maxScore = score_matrix[ii][b.length()-1]/path_length[ii][b.length()-1];
//					adex = ii;
//					bdex = b.length()-1;
//				}
//			}
//			else{
				if(score_matrix[ii][b.length()-1] > maxScore){
					maxScore = score_matrix[ii][b.length()-1];
					adex = ii;
					bdex = b.length()-1;
				}
//			}
		}
		for(int ii = 0; ii < b.length(); ii++){
//			if(the_scorer instanceof PearsonCorrelationScorer){
//				if(score_matrix[a.length()-1][ii]/path_length[a.length()-1][ii] > maxScore){
//					maxScore = score_matrix[a.length()-1][ii]/path_length[a.length()-1][ii];
//					adex = a.length()-1;
//					bdex = ii;
//				}
//			}
//			else{
				if(score_matrix[a.length()-1][ii] > maxScore){
					maxScore = score_matrix[a.length()-1][ii];
					adex = a.length()-1;
					bdex = ii;
				}
//			}
		}
		System.out.println("best backtrace starts at "+adex+","+bdex+" with a score of "+maxScore);
		while(adex >= 0 && bdex >= 0){
			map.put(adex, bdex);
			int ladex = adex;
			adex = backtrace_a[adex][bdex];
			bdex = backtrace_b[ladex][bdex];
		}

		MapAlignment toReturn = new MapAlignment(map,maxScore);
		return toReturn;
	}


	private void doubleIfNeeded(int a, int b) {
		if(a >= score_matrix.length || b >= score_matrix[0].length){
			score_matrix = new int[Math.max(a, score_matrix.length*2)][Math.max(b, score_matrix[0].length*2)];
			backtrace_a = new int[Math.max(a, score_matrix.length*2)][Math.max(b, score_matrix[0].length*2)];
			backtrace_b = new int[Math.max(a, score_matrix.length*2)][Math.max(b, score_matrix[0].length*2)];
			path_length = new int[Math.max(a, score_matrix.length*2)][Math.max(b, score_matrix[0].length*2)];
		}

	}

}
