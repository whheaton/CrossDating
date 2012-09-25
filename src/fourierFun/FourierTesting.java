package fourierFun;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.math.complex.Complex;
import org.apache.commons.math.transform.FastFourierTransformer;

import crossDating.CrossDater;
import dataStructures.MasterSeries;
import dataStructures.TreeRingSeries;

public class FourierTesting {

	
	private static double time_unit=1;
	private static FastFourierTransformer _fft = new FastFourierTransformer();

	public static void main(String[] args){
		String input = "./Series2.txt";
		TreeRingSeries tree = null;
		MasterSeries ms = null;
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
				//System.out.println(years.get(years.size()-1)+"\t"+master.get(master.size()-1)+"\t"+(line.length>3?series1.get(series1.size()-1):"")+"\t"+(line.length>4?series2.get(series2.size()-1):""));
			}
			
			double[] masterseries = new double[master.size()];
			for(int ii = 0; ii < master.size(); ii++){
				masterseries[ii] = master.get(ii);
				
			}
			int masteroffset = years.get(0);
			MasterSeries M = FourierTesting.highPassFilter(.1, masterseries, masteroffset);
			System.out.println(M.toString());
			System.out.println();
			ms = M;
			double[] series = new double[series3.size()];
			for(int ii = 0; ii < series3.size(); ii++){
				series[ii] = series3.get(ii);
			}
			series = TreeRingSeries.fitAndNormalize(series);
			for(int ii = 0; ii < series.length; ii++){
				System.out.println(series[ii]+"\n");
			}
			TreeRingSeries trs = highPassFilter(.1,series);
			tree = trs;
			System.out.println(trs.toString());
		//	new CrossDater(trs,M);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Complex[] timeseries = new Complex[ms.length()];
		for(int ii = 0; ii < timeseries.length; ii++){
			timeseries[ii] = new Complex(ms.getRing(ii),0);
		}
		
		
		System.out.println("frequency array length "+timeseries.length);
		timeseries = padArray(timeseries);
		writeToFile("/Users/wheaton/Rworkspaces/NabPlotR/timeseries.txt",timeseries);
		FastFourierTransformer fft = new FastFourierTransformer();
		
		
		Complex[] frequency = fft.transform(timeseries);
		System.out.println("frequency array length "+frequency.length);
		writeToFile("/Users/wheaton/Rworkspaces/NabPlotR/frequency.txt",frequency);
		frequency = highPassFilter(time_unit,.1,frequency);
		
		
		
		
		Complex[] timeinverse = fft.inversetransform(frequency);
		writeToFile("/Users/wheaton/Rworkspaces/NabPlotR/timeseries2.txt", timeinverse);
		
	}
	
	public static TreeRingSeries highPassFilter(double freq, double[] rings){
		int l = rings.length;
		rings = padArray(rings);
		Complex[] f = _fft.transform(rings);
		f = highPassFilter(1,.1,f);
		Complex[] d = _fft.inversetransform(f);
		double[] r = new double[l];
		for(int ii = 0; ii < r.length; ii++){
			r[ii] = d[ii].getReal();
		}
		return new TreeRingSeries(r,true);
	}
	
	public static MasterSeries highPassFilter(double freq, double[] rings, int year){
		int l = rings.length;
		rings = padArray(rings);
		Complex[] f = _fft.transform(rings);
		f = highPassFilter(1,.1,f);
		Complex[] d = _fft.inversetransform(f);
		double[] r = new double[l];
		for(int ii = 0; ii < r.length; ii++){
			r[ii] = d[ii].getReal();
		}
		return new MasterSeries(r,year, true);
	}

	private static Complex[] highPassFilter(double timeunit, double freq, Complex[] frequency) {
		int range = (int)Math.ceil(frequency.length*timeunit*freq);
		for(int ii = 0; ii <= Math.min(frequency.length-1, range); ii++){
			frequency[ii] = new Complex(0,0);
			frequency[frequency.length-1-ii] = new Complex(0,0);
		}
		return frequency;
	}
	
	private static double[] padArray(double[] d){
		int size = 2;
		while(size < d.length)size*=2;
		double[] dd = new double[size];
		for(int ii = 0; ii < dd.length; ii++){
			dd[ii] = (d.length > ii?d[ii]:0);
		}
		return dd;
	}

	private static Complex[] padArray(Complex[] timeseries) {
		int size = 2;
		while(size < timeseries.length){
			size*=2;
		}
		Complex[] toReturn = new Complex[size];
		for(int ii = 0; ii < size; ii++){
			toReturn[ii] = (timeseries.length>ii?timeseries[ii]:new Complex(0,0));
		}
		return toReturn;
	}

	private static void writeToFile(String string,Complex[] d) {
		try {
			BufferedWriter br = new BufferedWriter(new FileWriter(string));
			br.write("unit\tdata\n");
			for(int ii = 0; ii < d.length; ii++){
				br.write(time_unit*ii+"\t"+d[ii].getReal()+"\n");
				
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
