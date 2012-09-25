package dataStructures;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MapAlignment {

	private TreeMap<Integer,Integer> the_alignment;
	private int the_score;
	
	public MapAlignment(TreeMap<Integer,Integer> alignment, int score){
		the_alignment = alignment;
		the_score = score;
	}

	public Map<Integer,Integer> getMap() {
		return the_alignment;
	}
	
	public int getScore(){
		return the_score;
	}
	
	public int get(int ii){
		return the_alignment.get(ii);
	}

	public long numMatches() {
		return the_alignment.size();
	}
	
}
