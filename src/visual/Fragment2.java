package visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import dataStructures.RingSeries;

public class Fragment2 {
	private RingSeries the_fragment;
	private int x;
	private int y;
	private int width;
	private int height;
	private int[] probeXs;

	private static final Color bluish = new Color(0,0,255);
	private static final Color reddish = new Color(200,50,0);
	
	public Fragment2(RingSeries frag0){
		the_fragment = frag0;
		probeXs = new int[frag0.numRings()];
	}
	


	public double getLength(){
		return the_fragment.getTotalRadius();
	}

	public int getProbeX(int index){
		return probeXs[index];
		
	}
	
	public void setFrame(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		for (int ii=0; ii<the_fragment.numRings(); ii++){
			
			probeXs[ii] = x + (int)((the_fragment.getRadius(ii) * width)/the_fragment.getTotalRadius());
			System.out.println("total radius "+the_fragment.getTotalRadius()+" and radius at this ring "+the_fragment.getRadius(ii)+" resulting in an x of "+probeXs[ii]);
		}
	}

	public void paint(Graphics2D brush){
		Color oldc = brush.getColor();
		
		brush.setColor(bluish);
		brush.setColor(Color.black);
		brush.fillRect(x, y+(height/2-2), width, 2);
		
		brush.setColor(reddish);
		for (int ii=0; ii<the_fragment.numRings(); ii++){
			double start = y-4;
//				System.out.println(tag_colors[jj]);
//				System.out.println("start "+start+" height "+h + " tag "+jj+" probability "+tagp[jj]);
				
				brush.fillRect(probeXs[ii]-1, (int)Math.round(start), 3, (int)Math.round(height));
				
		}
		brush.setColor(oldc);
	}
	
	public static void showTreeRings(FragmentPanel f){
		JFrame frame = new JFrame("Tree Ring");
		frame.add(f);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	public class FragmentPanel extends JPanel{
		Fragment2 _frag;
		public FragmentPanel(Fragment2 f){
			super();
			_frag= f;
			this.setBackground(Color.white/**new Color(0xDF,0xFD,0xFF)**/);
			Dimension dim = new Dimension(1680,100);
			this.setSize(dim);
			this.setPreferredSize(dim);
		}
		
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			Graphics2D brush = (Graphics2D)g;
			_frag.paint(brush);
		}
	}

}
