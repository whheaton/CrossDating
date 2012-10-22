package visual;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dataStructures.MasterSeries;
import dataStructures.TreeRingSeries;

/**
 * Outer frame for the main graphical interaction with this project
 * 
 * @author wheaton
 *
 */
public class MainGUI extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7082273473753797654L;
	
	private String master_file;
	private String series_file;
	private Component parent_component;
	private String current_directory;
	private MasterSeries master_series;
	private TreeRingSeries tree_series;
	
	
	public MainGUI(String directory){
		super();
		current_directory = directory;
		parent_component = this;
		this.addWindowListener(new ExitListener());
		
		JPanel content = new JPanel(new BorderLayout());
		this.setContentPane(content);
		JPanel loadpanel = new JPanel(new BorderLayout());
		content.add(loadpanel,BorderLayout.NORTH);

		JPanel loadmasterpanel = new JPanel(new BorderLayout());
		JPanel loadseriespanel = new JPanel(new BorderLayout());
//		loadmasterpanel.setLayout(new FlowLayout(FlowLayout.LEFT));
//		loadseriespanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		loadpanel.add(loadmasterpanel,BorderLayout.NORTH);
		loadpanel.add(loadseriespanel,BorderLayout.SOUTH);
		loadpanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel masterlabel = new JLabel("Load master series: ");
		JLabel serieslabel = new JLabel("Load tree rings series: ");
		loadpanel.setPreferredSize(new Dimension(800,80));
		JTextField mastertext = new JTextField();
		mastertext.setEditable(false);
		mastertext.setPreferredSize(new Dimension(500,20));
		JTextField seriestext = new JTextField();
		seriestext.setEditable(false);
		seriestext.setPreferredSize(new Dimension(500,20));   
		JButton masterbrowse = new JButton("Browse");
		
		JButton seriesbrowse = new JButton("Browse");
		
		loadmasterpanel.add(masterlabel,BorderLayout.WEST);
		loadmasterpanel.add(mastertext,BorderLayout.CENTER);
		loadmasterpanel.add(masterbrowse,BorderLayout.EAST);
		loadseriespanel.add(serieslabel,BorderLayout.WEST);
		loadseriespanel.add(seriestext,BorderLayout.CENTER);
		loadseriespanel.add(seriesbrowse,BorderLayout.EAST);
		
		
		JButton alignbutton = new JButton("Align");
		alignbutton.addActionListener(new AlignActionListener());
		
		
		
		
		JPanel superringpanel = new JPanel(new BorderLayout());
		JPanel ringpanel = new JPanel();
		content.add(superringpanel,BorderLayout.SOUTH);
		ringpanel.setPreferredSize(new Dimension(1600,200));
		JPanel seriespanel = new JPanel();
		seriespanel.setPreferredSize(new Dimension(1600,100));
		JPanel masterpanel = new JPanel();
		masterpanel.setPreferredSize(new Dimension(1600,100));
		ringpanel.add(masterpanel);
		ringpanel.add(seriespanel);
		masterbrowse.addActionListener(new BrowseActionListener(master_file, mastertext, masterpanel, true));
		seriesbrowse.addActionListener(new BrowseActionListener(series_file, seriestext, seriespanel, false));
		superringpanel.add(ringpanel,BorderLayout.NORTH);
		
		this.pack();
		this.setVisible(true);
		this.setResizable(false);
	}
	
	private class BrowseActionListener implements ActionListener{

		private String my_file;
		private JTextField my_text;
		private JPanel my_panel;
		private boolean is_master;
		
		
		private BrowseActionListener(String file, JTextField text, JPanel panel, boolean master){
			my_file = file;
			my_text = text;
			my_panel = panel;
			is_master = master;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			File f = loadChooser(current_directory,parent_component);
			if(f == null) return;
			my_file = f.getAbsolutePath();
			my_text.setText(my_file);
			my_panel.removeAll();
			if(is_master){
				MasterSeries ms = MasterSeries.getMasterSeries(f);
				master_series = ms;
				MasterPanel mp = new MasterPanel(ms);
				my_panel.add(mp);
				my_panel.validate();
				mp.repaint();
				my_panel.repaint();
				
			}
			else{
				TreeRingSeries trs = TreeRingSeries.getTreeRingSeries(f);
				tree_series = trs;
				SeriesPanel sp = new SeriesPanel(trs);
				my_panel.add(sp);
				my_panel.validate();
				my_panel.repaint();
			}
			my_text.repaint();
		}
		
	}
	
	private static File loadChooser(String dir, Component parent){
		//Create a file chooser
		final JFileChooser fc = new JFileChooser(dir);
		fc.setMultiSelectionEnabled(false);
		//In response to a button click:
		int returnVal = fc.showOpenDialog(parent);
		if(returnVal == JFileChooser.APPROVE_OPTION){
			
			File f = fc.getSelectedFile();
			return f;
		}
		else return null;
		
	}
	
	private class AlignActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(master_series != null && tree_series != null){
				
				
			}
			
		}
		
	}
	

	
	
	/**
	 * 
	 * @param args - 1 argument is the base directory you wish to start from
	 */
	public static void main(String[] args){
		File f = new File(args[0]);
		if(f.exists()){
			new MainGUI(args[0]);
		}
		else{
			new MainGUI("./");
		}
	}
	
	
	
}
