package dataStructures;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MasterSeries extends RingSeries {

	private double[] the_radii;
	private double[] the_rings;
	private int year_offset;
	
	public static MasterSeries getMasterSeries(File f){
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String str;
			ArrayList<Double> rings = new ArrayList<Double>();
			int startyear = -1;
			br.readLine(); // header
			while((str = br.readLine()) != null){
				String[] line = str.split("\\s");
				System.out.println(str);
				if(startyear == -1){
					startyear = Integer.parseInt(line[0]);
					
				}
				rings.add(Double.parseDouble(line[1]));
			}
			double[] ringsarray = new double[rings.size()];
			for(int ii = 0; ii < ringsarray.length; ii++){
				ringsarray[ii] = rings.get(ii);
			}
			return new MasterSeries(ringsarray,startyear);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public MasterSeries(double[] rings, int startyear) {
		the_rings = new double[rings.length];
		the_radii = rings;
		for(int ii = 1; ii < rings.length; ii++){
			the_rings[ii]= rings[ii];
			the_radii[ii] = the_radii[ii]+the_radii[ii-1];
		}
		year_offset = startyear;
	}
	
	public MasterSeries(double[] rings, int startyear, boolean hpf){
		the_radii = rings;
		the_rings = new double[rings.length];
		//fitAndNormalize();
		double mean = 0.0;
		for(int ii = 0; ii < the_radii.length; ii++){
			//System.out.println(the_radii[ii]);
			mean += the_radii[ii];
			//the_radii[ii] = the_radii[ii] + the_radii[ii-1];
			
		}
		mean/=the_radii.length;
		double variance = 0.0;
		for(int ii = 0; ii < the_radii.length; ii++){
			variance += Math.pow(the_radii[ii]-mean,2);
		}
		variance /= (the_radii.length-1);
		double std = Math.sqrt(variance);
		for(int ii = 0; ii < the_radii.length; ii++){
			the_radii[ii] = (the_radii[ii]-mean)/std;
			the_rings[ii] = the_radii[ii];
		}
		for(int ii = 1; ii < the_radii.length; ii++){
			the_radii[ii] = the_radii[ii]+the_radii[ii-1];
		}
		year_offset = startyear;
	}

	@Override
	public double getRing(int index) {
		return the_radii[index]-(index-1>=0?the_radii[index-1]:0);
	}
	
	public int getYear(int index){
		return index + year_offset;
	}

	@Override
	public int length() {
		return the_radii.length;
	}

	@Override
	public double getRadius(int index) {
		return the_radii[index];
	}

	@Override
	public int numRings() {
		// TODO Auto-generated method stub
		return the_rings.length;
	}

	@Override
	public double[] getRings() {
		return the_rings;
	}
	
	
	public String toString(){
		String s = "Master series\n";
		for(int ii = 0; ii < this.numRings(); ii++){
			s+=this.getRing(ii)+"\n";
		}
		return s;
	}
	
	
}
