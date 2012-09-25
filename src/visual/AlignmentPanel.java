package visual;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import dataStructures.MapAlignment;
import dataStructures.RingSeries;


/**
 * A class for displaying an Alignment
 * 
 * 
 * @author Peter
 * @author wheaton changed to use MapAlignment, not tested yet
 *
 */
public class AlignmentPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Fragment2 fragment_a;
	private Fragment2 fragment_b;
	private RingSeries map_a;
	private RingSeries map_b;
	private MapAlignment my_matching;
	private Color[] tag_colors;
	private int num_tags;


	/**
	 * Constructor initializing and displaying
	 * @param frag0
	 * @param frag1
	 * @param line
	 */
	public AlignmentPanel(RingSeries frag0, RingSeries frag1, MapAlignment line){
		map_a = frag0;
		map_b = frag1;
		
		fragment_a = new Fragment2(frag0);
		fragment_b = new Fragment2(frag1);
		my_matching = line;
		
		
		
		this.setBackground(Color.white/**new Color(0xDF,0xFD,0xFF)**/);
		Dimension dim = new Dimension(1680,100);
		this.setSize(dim);
		this.setPreferredSize(dim);

		this.addComponentListener(new ResizeListener());
		resize();
	}

	
	/**
	 * Paint method
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D brush = (Graphics2D)g;
		fragment_a.paint(brush);
		fragment_b.paint(brush);
		Map<Integer, Integer> mapAlignment = my_matching.getMap();
		System.out.println("num matches "+my_matching.getMap().size());
		for(Integer ii : mapAlignment.keySet()){
			int j = mapAlignment.get(ii);
			int xA = fragment_a.getProbeX(ii);
			int xB = fragment_b.getProbeX(j);
			Color oldcolor = g.getColor();
			g.setColor(Color.black);
			g.drawLine(xA, this.getHeight()/4, xB, 3*this.getHeight()/4);
			g.setColor(oldcolor);
		}
	}

	/**
	 * to handle resize events, not sure if this works with the new mapalignment change
	 */
	private void resize(){
		final int margin = 20;

		int width = getWidth();
		int height = getHeight();
		long offset = 0;
		Map<Integer,Integer> mapalignment = my_matching.getMap();
		for(Integer ii : mapalignment.keySet()){
			offset += map_b.getRadius(my_matching.get(ii)) - map_a.getRadius(ii);
		}
		//for (int ii=0; ii<my_matching.numMatches(); ii++){
		//	offset += map_b.getProbeLocation(my_matching.get(ii)) - map_a.getProbeLocation(my_matching.get(ii)[0]);
		//}
		offset /= my_matching.numMatches();
		//		System.out.println(offset);

		double scale = (offset < 0 ? fragment_b.getLength() - offset
				: fragment_a.getLength() + offset);
		if (offset < 0){
			fragment_a.setFrame(margin,margin,(int)((width - 2*margin)*fragment_a.getLength()/scale),height/2-2*margin);
			fragment_b.setFrame((int)((width - 2*margin)*-offset/scale)+margin, height/2+margin, (int)((width - 2*margin)*fragment_b.getLength()/scale), height/2-2*margin);
		}else{
			fragment_a.setFrame((int)((width - 2*margin)*offset/scale)+margin, margin, (int)((width - 2*margin)*fragment_a.getLength()/scale), height/2-2*margin);
			fragment_b.setFrame(margin, height/2+margin,(int)((width - 2*margin)*fragment_b.getLength()/scale),height/2-2*margin);
		}
	}

	/**
	 * resize listener class
	 * 
	 * @author Peter
	 *
	 */
	private class ResizeListener extends ComponentAdapter{
		public void componentResized(ComponentEvent e) {
			resize();
		}
	}

	/**
	 * Displays the alignment
	 * @param frag0
	 * @param frag1
	 * @param matching
	 */
	public static void showAlignment(RingSeries frag0, RingSeries frag1, MapAlignment matching){
		JFrame frame = new JFrame("Alignment");
		frame.add(new AlignmentPanel(frag0,frag1,matching));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

}