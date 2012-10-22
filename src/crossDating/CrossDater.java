package crossDating;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;

import treeRingAlignment.Aligner;
import treeRingAlignment.DendroAlign;
import treeRingAlignment.PearsonCorrelationScorer;
import treeRingAlignment.RingScorer;
import treeRingAlignment.SumOfSquaresRingScorer;
import dataStructures.MapAlignment;
import dataStructures.MasterSeries;
import dataStructures.TreeRingSeries;
import fourierFun.FourierTesting;


/**
 * This class is depricated, was initially used for testing
 * Now there is a GUI from which testing is more efficient.
 * See MainGUI in the visual package
 * 
 * 
 * @author wheaton
 *
 */
public class CrossDater {


	private Aligner the_aligner;

	public CrossDater(TreeRingSeries series, MasterSeries master){

		//RingScorer score = new SumOfSquaresRingScorer();
		RingScorer score = new PearsonCorrelationScorer();
		the_aligner = new DendroAlign(score);
		MapAlignment alignment = the_aligner.align(master,series);
		System.out.println("done? "+alignment.getMap().size());
		int diff = 0;
		int minmaster = Integer.MAX_VALUE;
		int maxmaster = Integer.MIN_VALUE;
		int lastmaster = -1;
		int lastring = -1;
		for(Entry<Integer, Integer> e: alignment.getMap().entrySet()){
			if(lastmaster!=-1){
				
				if(e.getKey() != lastmaster+1){
					System.out.println("skipped a master ring "+(e.getKey()-lastmaster-1));
				}
				
			}
			if(lastring!=-1){
				
				if(e.getValue() != lastring+1){
					System.out.println("skipped a tree ring "+(e.getKey()-lastring-1));
				}
				
			}
			lastmaster = e.getKey();
			lastring = e.getValue();
			System.out.println(master.getYear(e.getKey())+" "+(e.getValue()));
			
			diff += e.getValue()-e.getKey();
			if(e.getKey() > maxmaster)maxmaster = e.getKey();
			if(e.getKey() < minmaster)minmaster = e.getKey();
		}
		double trouble = (double)diff/(double)alignment.getMap().size();
		
		System.out.println("This tree was born in year "+(master.getYear(minmaster))+" and died in year "+(master.getYear(maxmaster))+" min master "+minmaster+" max master "+maxmaster);
		System.out.println(master.getYear(0));
		//AlignmentPanel.showAlignment(master,series,alignment);
		
	}

	/**
	 * @param args
	 */
	public static void main2(String[] args) {
		String input = "./Series2.txt";
		try {
			ArrayList<Integer> years = new ArrayList<Integer>();
			ArrayList<Double> master = new ArrayList<Double>();
			ArrayList<Double> series1 = new ArrayList<Double>();
			ArrayList<Double> series2 = new ArrayList<Double>();
			ArrayList<Double> series3 = new ArrayList<Double>();
			BufferedReader br = new BufferedReader(new FileReader(input));
			System.out.println(br.readLine());
			System.out.println(br.readLine());
			String str = null;
			while((str = br.readLine()) != null){
				String[] line = str.split("\\s");
				if(line.length > 0 && !line[0].isEmpty()){
					years.add(Integer.parseInt(line[0]));
					master.add(Double.parseDouble(line[1]));
				}
				if(line.length > 3 && !line[3].isEmpty())
					series1.add(Double.parseDouble(line[3]));
				if(line.length > 5 && !line[5].isEmpty())
					series2.add(Double.parseDouble(line[5]));
				if(line.length > 6 && !line[6].isEmpty())
					series3.add(Double.parseDouble(line[6]));
			}
			
			double[] masterseries = new double[master.size()];
			for(int ii = 0; ii < master.size(); ii++){
				masterseries[ii] = master.get(ii);
				
			}
			int masteroffset = years.get(0);
			MasterSeries M = new MasterSeries(masterseries,years.get(0));//FourierTesting.highPassFilter(.01, masterseries, years.get(0));
			System.out.println(M.toString());
			series1 = series3;
			double[] series = new double[series1.size()];
			for(int ii = 0; ii < series1.size(); ii++){
				series[ii] = series1.get(ii);
			}
			//series = TreeRingSeries.fitAndNormalize(series);
			TreeRingSeries trs = new TreeRingSeries(series);//FourierTesting.highPassFilter(.01, series);
			System.out.println("Filtered tree rings\n"+trs.toString());
			new CrossDater(trs,M);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args){
		MasterSeries M = MasterSeries.getMasterSeries(new File("./datasets/master.txt"));
		TreeRingSeries trs = TreeRingSeries.getTreeRingSeries(new File("./datasets/treeunknown.txt"));
		new CrossDater(trs,M);
		
	}

}
