package dataStructures;

public class TreeRingSeries extends RingSeries{

	private double[] the_radii;
	private double[] raw_rings;
	private double[] the_rings;
	
	public TreeRingSeries(double[] rings){
		the_radii = rings;
		raw_rings = new double[rings.length];
		for(int ii = 0; ii < rings.length; ii++){
			raw_rings[ii] = rings[ii];
			
		}
		fitAndNormalize();
		for(int ii = 1; ii < the_radii.length; ii++){
			//System.out.println(the_radii[ii]);
			the_radii[ii] = the_radii[ii] + the_radii[ii-1];
		}
	}
	
	public TreeRingSeries(double[] rings, boolean normalized) {
		the_radii = rings;
		raw_rings = new double[rings.length];
		the_rings = new double[rings.length];
		for(int ii = 0; ii < rings.length; ii++){
			raw_rings[ii] = rings[ii];
			
		}
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
		variance/=(the_radii.length-1);
		double std = Math.sqrt(variance);
		for(int ii = 0; ii < the_radii.length; ii++){
			the_radii[ii] = (the_radii[ii]-mean)/std;
			the_rings[ii] = the_radii[ii];
		}
		for(int ii = 1; ii < the_radii.length; ii++){
			the_radii[ii] = the_radii[ii]+the_radii[ii-1];
		}
	}

	private void fitAndNormalize(){
		
		double[] x = new double[the_radii.length];
		double[] y = new double[the_radii.length];
		for(int ii = 1; ii<=the_radii.length; ii++){
			x[ii-1] = ii;
			y[ii-1] = Math.log(the_radii[ii-1]);
		}
		
		
		double[] function = regression(x,y);
		
		double mean = 0.0;
		for(int ii = 0; ii < the_radii.length; ii++){
			mean+=the_radii[ii];
		}
		mean/=the_radii.length;
		System.out.println("new regression normalization y= e^("+function[1]+" + "+function[0]+"x)");
		for(int ii = 0; ii < the_radii.length; ii++){
			the_radii[ii] = (the_radii[ii]-(Math.exp(function[0]*(ii+1)+function[1])))/mean;//the_radii[ii];//(Math.exp(function[0]*(Math.log(ii+1))+function[1]));
			System.out.println(the_radii[ii]);
		}
		the_rings = the_radii;
	}
	
	public static double[] fitAndNormalize(double[] rings){
		double[] x = new double[rings.length];
		double[] y = new double[rings.length];
		for(int ii = 1; ii<=rings.length; ii++){
			x[ii-1] = Math.log(ii);
			y[ii-1] = Math.log(rings[ii-1]);
		}
		
		
		double[] function = regression(x,y);
		
		double mean = 0.0;
		for(int ii = 0; ii < rings.length; ii++){
			mean+=rings[ii];
		}
		mean/=rings.length;
		System.out.println("new regression normalization y= e^("+function[1]+" + "+function[0]+"x)");
		for(int ii = 0; ii < rings.length; ii++){
			rings[ii] = (rings[ii]-(Math.exp(function[0]*(Math.log(ii+1))+function[1])))/mean;//rings[ii];//(Math.exp(function[0]*(Math.log(ii+1))+function[1]));
			System.out.println(rings[ii]);
		}
		return rings;
	}

	@Override
	public double getRing(int index) {
		return the_radii[index]-(index > 0?the_radii[index-1]:0);
	}
	
	public double getRawRing(int index){
		return raw_rings[index];
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
		return raw_rings.length;
	}

	@Override
	public double[] getRings() {
		// TODO Auto-generated method stub
		return the_rings;
	}

	
	public String toString(){
		String s = "Tree ring series\n";
		for(int ii = 0; ii < this.numRings(); ii++){
			s+=this.getRing(ii)+"\n";
		}
		return s;
	}
	
	public static double[] regression(double[] x, double[] y){
		double sumx = 0;
		double sumy = 0;
		double sumx2 = 0;
		int n = 0;
		for(int ii = 0; ii < x.length; ii++){
			sumx += x[ii];
			sumx2 += x[ii]*x[ii];
			sumy += y[ii];
			n++;
		}

		double xbar = sumx / n;
		double ybar = sumy / n;

		// second pass: compute summary statistics
		//		double[] beta1a = new double[x.length];
		double xxbar = 0.0, yybar = 0.0, xybar = 0.0;
		for (int i = 0; i < n; i++) {
			xxbar += (x[i] - xbar) * (x[i] - xbar);
			yybar += (y[i] - ybar) * (y[i] - ybar);
			xybar += (x[i] - xbar) * (y[i] - ybar);
			//beta1a[i] = (x[i] - xbar) * (y[i] - ybar);
		}

		//		Arrays.sort(beta1a);
		//		double maxbeta1 = beta1a[beta1a.length/2];
		//
		//		for (int i = 0; i < n; i++) {
		//			if((x[i] - xbar) * (y[i] - ybar) < maxbeta1){
		//				xxbar += (x[i] - xbar) * (x[i] - xbar);
		//				yybar += (y[i] - ybar) * (y[i] - ybar);
		//				xybar += (x[i] - xbar) * (y[i] - ybar);
		//			}
		//		}

		double beta1 = xybar / xxbar;
		double beta0 = ybar - beta1 * xbar;

		// print results
		//System.out.println("y   = " + beta1 + " * x + " + beta0);

		// analyze results
		int df = n - 2;
		double rss = 0.0;      // residual sum of squares
		double ssr = 0.0;      // regression sum of squares
		for (int i = 0; i < n; i++) {
			double fit = beta1*x[i] + beta0;
			rss += (fit - y[i]) * (fit - y[i]);
			ssr += (fit - ybar) * (fit - ybar);
		}
		double R2    = ssr / yybar;
		double svar  = rss / df;
		double svar1 = svar / xxbar;
		double svar0 = svar/n + xbar*xbar*svar1;
		//		System.out.println("R^2                 = " + R2);
		//		System.out.println("std error of beta_1 = " + Math.sqrt(svar1));
		//		System.out.println("std error of beta_0 = " + Math.sqrt(svar0));
		svar0 = svar * sumx2 / (n * xxbar);
		//		System.out.println("std error of beta_0 = " + Math.sqrt(svar0));
		//
		//		System.out.println("SSTO = " + yybar);
		//		System.out.println("SSE  = " + rss);
		//		System.out.println("SSR  = " + ssr);

		return new double[]{beta1,beta0,R2};
	}
	
}
