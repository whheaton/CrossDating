package visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

import dataStructures.MasterSeries;

/***
 * This class is the visual representation for the Master tree ring series
 * 
 * @author wheaton
 *
 */
public class MasterPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -434475099544065140L;
	private MasterSeries the_master;
	private int the_max;
	private double ring_mean;
	private double ring_sd;
	private int x_last;
	private int y_last;
	private int x_offset;
	private static double max_sd = 2.5;

	public MasterPanel(MasterSeries master){
		super();
		the_master = master;
		x_offset = 0;
		this.setPreferredSize(new Dimension(1600,50));
		this.setBackground(Color.LIGHT_GRAY);
		addMouseMotionListener(this);
		addMouseListener(this);
		addMouseWheelListener(this);
		the_max = 3200;
		ring_mean = (double)the_max/(double)the_master.numRings();
		ring_sd = ring_mean/max_sd;
		//System.out.println("ring mean "+ring_mean+", ring sd = "+ring_sd+" the max "+the_max+" num rings "+master.numRings());
		//repaint();
	}


	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g); 
		Graphics2D g2 = (Graphics2D)g;
		Color oldcolor = g2.getColor();
		int y1 = 10;
		int y2 = 30;
		//this.setPreferredSize(new Dimension(this.getParent().getWidth(),50));
		int last = 0;
		for(int ii = 0; ii < the_master.numRings(); ii++){
			//System.out.println("ring "+ii+" "+the_master.getRing(ii));
			if(the_master.getRing(ii) < -max_sd){
				g2.setColor(Color.red);
				g2.drawLine(last+1+x_offset, y1, last+1+x_offset, y2);
				last++;
				g2.setColor(oldcolor);
			}
			else if(the_master.getRing(ii) > max_sd){
				g2.setColor(Color.red);
				int x1 = (int)(last + ring_mean+ring_sd*max_sd)+x_offset;
				last = x1;
				g2.drawLine(x1, y1, x1, y2);
				g2.setColor(oldcolor);
			}
			else{
				
				int x1 = (int)(last + ring_mean + ring_sd*the_master.getRing(ii));
			
				g2.setColor(Color.blue);
				g2.drawLine(x1+x_offset, y1, x1+x_offset, y2);
				if(x1-last > 27){
					g2.drawString(Integer.toString(the_master.getYear(ii-1)), last+x_offset-13, y1);
				}
				last = x1;
				g2.setColor(oldcolor);
			}
		}
		g2.setColor(oldcolor);
		g2.drawLine(x_offset, 20, last+x_offset, 20);
		
	}


	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent arg0) {

		x_last = arg0.getX();
		
		
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseDragged(MouseEvent arg0) {
		int x = arg0.getX();
		int y = arg0.getY();
		
		x_offset += x - x_last;
		//System.out.println(x_offset);
		
		x_last = x;
		y_last = y;
		this.repaint();
		
	}


	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int rotation = e.getWheelRotation();
		//System.out.println(rotation);
		int change = the_max - Math.max(100, (int)((double)the_max * (double)(100-rotation)/(double)100.0));
		double percent = (double)(e.getX()-x_offset)/(double)the_max;
		x_offset += (int)change*percent;
		the_max = Math.max(100, (int)((double)the_max * (double)(100-rotation)/(double)100.0));
		ring_mean = (double)the_max/(double)the_master.numRings();
		ring_sd = (double)ring_mean/(double)max_sd;
		//System.out.println(the_max+" "+ring_mean+" "+ring_sd);
		repaint();
		
	}


}
