package visual;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import dataStructures.MapAlignment;
import dataStructures.RingSeries;

public class AlignmentPanel2 extends JPanel{

	private MapAlignment the_alignment;
	private RingSeries ring_two;
	private RingSeries ring_one;

	public AlignmentPanel2(RingSeries ring1, RingSeries ring2, MapAlignment alignment){
		super();
		this.setPreferredSize(new Dimension(1600,400));
		ring_one = ring1;
		ring_two = ring2;
		the_alignment = alignment;
		this.repaint();
	}
	
	protected void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		
		
		
	}
	
}
