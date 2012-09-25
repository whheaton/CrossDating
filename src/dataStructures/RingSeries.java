package dataStructures;

public abstract class RingSeries {

	public abstract double getRing(int index);
	public abstract double getRadius(int index);
	public abstract int length();
	public abstract int numRings();
	public abstract double[] getRings();
	public double getTotalRadius() {
		return this.getRadius(this.length()-1);
	}
	
}
