package treeRingAlignment;

import dataStructures.MapAlignment;
import dataStructures.RingSeries;
import dataStructures.TreeRingSeries;

public interface Aligner {

	public MapAlignment align(RingSeries a, RingSeries b);
}
