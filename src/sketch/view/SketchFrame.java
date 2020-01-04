package sketch.view;



import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFileChooser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;


import sketch.geo.gPoint;;


public class SketchFrame extends JFrame {

	private static final long serialVersionUID = 1L;


	
	private JPanel jContentPane = null;  //  @jve:decl-index=0:visual-constraint="34,18"
	private JMenuBar jJMenuBar = null;
	private JMenu jMenuFile = null;
	private JMenuItem jMenuItemOpen = null;
	private JMenuItem jMenuItemNew = null;
	private JMenu jMenuOption = null;
	private JMenu jMenuAbout = null;
	private JMenuItem jMenuItemHelp = null;
	private JMenu jMenuView = null;
	private JMenu jMenuParse = null;
	private JPanel jPanelState = null;
	private JPanel jPanelInfo = null;
	private SketchPanel jPanelDraw = null;
	private JLabel jLabel = null;
	private JLabel jlSketchID = null;
	private JLabel jlSketcherID = null;
	private JLabel jlStrokeNo = null;
	private JLabel jSketchID = null;
	private JLabel jSketcherID = null;
	private JLabel jStrokeNo = null;
	private JCheckBoxMenuItem jCheckBoxMenuItemStroke = null;
	private JCheckBoxMenuItem jCheckBoxMenuItemToken = null;
	private JCheckBoxMenuItem jCheckBoxMenuItemSymbol = null;
	//added by daisy
	private JCheckBoxMenuItem jCheckBoxMenuItemSegment = null;
	private JCheckBoxMenuItem jCheckBoxMenuItemResample = null;
	private JMenuItem jMenuItemViewNext = null;
	private JMenuItem jMenuItemParseResample = null;
	private JMenuItem jMenuItemParseSegment = null;
	private JMenuItem jMenuItemDelete = null;

	private JMenuItem jMenuItemSave = null;

	private JCheckBoxMenuItem jCheckBoxMenuItemDraw = null;

	private JMenuItem jMenuItemExit = null;

	private JMenuItem jMenuItemML = null;
	
	// variable used to indicate the current position while reading
	private int indexOfDocument = 0;
	private int userID = -1;
	// current file, for viewNext
	private File file = null;
	
	private int noSample = 0;

	/**
	 * This is the default constructor
	 */
	public SketchFrame() {
		super();
		//System.out.println("Welcome to use SampleCollector!");

		initialize();

	}
	
	private SketchFrame getThis() {
		return this;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
	
		this.setLocation(120, 80);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(800,600);
		this.setJMenuBar(getJJMenuBar());  
		this.setContentPane(getJContentPane());  
		this.setTitle("Welcome to SampleCollector!");
	}
	
	
	
	private void clear() {
		
		getSketchPanel().setDrawable(true);
		getJCheckBoxMenuItemStroke().setSelected(true);
		getJCheckBoxMenuItemResample().setSelected(false);
		getThis().repaint();
	}
	

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());

			jContentPane.setSize(new java.awt.Dimension(283,144));
			jContentPane.add(getSketchPanel(), java.awt.BorderLayout.CENTER);
			jContentPane.add(getJPanelState(), java.awt.BorderLayout.SOUTH);
			jContentPane.add(getJPanelInfo(), java.awt.BorderLayout.WEST);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jCheckBoxMenuItemStroke	
	 * 	
	 * @return javax.swing.JCheckBoxMenuItem	
	 */
	private JCheckBoxMenuItem getJCheckBoxMenuItemStroke() {
		if (jCheckBoxMenuItemStroke == null) {
			jCheckBoxMenuItemStroke = new JCheckBoxMenuItem();
			jCheckBoxMenuItemStroke.setText("View Stroke");
			jCheckBoxMenuItemStroke.setMnemonic(java.awt.event.KeyEvent.VK_K);
			jCheckBoxMenuItemStroke.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.CTRL_MASK));
			jCheckBoxMenuItemStroke.setSelected(true);
			jCheckBoxMenuItemStroke.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//System.out.println("change view stroke"); 
					boolean b = getJCheckBoxMenuItemStroke().isSelected();
//					sketchView.setDrawStroke(b);
					
					//TODO: set draw stroke
//					jLabel.setText(sketchView.getStateString());
					getThis().repaint();
					
				}
			});
		}
		return jCheckBoxMenuItemStroke;
	}

	/**
	 * This method initializes jCheckBoxMenuItemToken	
	 * 	
	 * @return javax.swing.JCheckBoxMenuItem	
	 */
	
	
	private JCheckBoxMenuItem getJCheckBoxMenuItemToken() {
		if (jCheckBoxMenuItemToken == null) {
			jCheckBoxMenuItemToken = new JCheckBoxMenuItem();
			jCheckBoxMenuItemToken.setText("View Token");
			jCheckBoxMenuItemToken.setMnemonic(java.awt.event.KeyEvent.VK_T);
			jCheckBoxMenuItemToken.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.CTRL_MASK));
			jCheckBoxMenuItemToken.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
//					System.out.println("change view token"); 
					boolean b = getJCheckBoxMenuItemToken().isSelected();
//					sketchView.setDrawToken(b);
					/*
					HMMSegmentor seg = HMMSegmentor.getInstance();
					double[] o = {-0.6217419896746907,-0.7832221257570273,3.305419396682769};

					System.out.println("the pdf of state8 is: " + seg.testObservationParsing(8, o));
					System.out.println("the pdf of state21 is: " + seg.testObservationParsing(21, o));
					System.out.println("selected/prefered = " + 
							seg.testObservationParsing(8, o)/seg.testObservationParsing(21, o));
					*/
					getThis().repaint();
				}
			});
		}
		return jCheckBoxMenuItemToken;
	}

	/**
	 * This method initializes jCheckBoxMenuItemSymbol	
	 * 	
	 * @return javax.swing.JCheckBoxMenuItem	
	 */
	private JCheckBoxMenuItem getJCheckBoxMenuItemSymbol() {
		if (jCheckBoxMenuItemSymbol == null) {
			jCheckBoxMenuItemSymbol = new JCheckBoxMenuItem();
			jCheckBoxMenuItemSymbol.setText("View Symbol");
			jCheckBoxMenuItemSymbol.setMnemonic(java.awt.event.KeyEvent.VK_S);
			jCheckBoxMenuItemSymbol.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.CTRL_MASK));
			jCheckBoxMenuItemSymbol.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
//					System.out.println("change view symbol"); 
					boolean b = getJCheckBoxMenuItemSymbol().isSelected();
//					sketchView.setDrawSymbol(b);
					
					//TODO: set draw complicated symbols
//					jLabel.setText(sketchView.getStateString());
					getThis().repaint();
				}
			});
		}
		return jCheckBoxMenuItemSymbol;
	}
	/**
	 * This method initialize jCheckBoxMenuItemSegment
	 * 
	 * @return javax.swing.jCheckBoxMenuItem
	 */
	private JCheckBoxMenuItem getJCheckBoxMenuItemSegment() {
		if(jCheckBoxMenuItemSegment == null) {
			jCheckBoxMenuItemSegment = new JCheckBoxMenuItem();
			jCheckBoxMenuItemSegment.setText("View Segmentation");
			jCheckBoxMenuItemSegment.setMnemonic(java.awt.event.KeyEvent.VK_M);
			jCheckBoxMenuItemSegment.setSelected(true);

			jCheckBoxMenuItemSegment.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, ActionEvent.CTRL_MASK));
			jCheckBoxMenuItemSegment.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					boolean b = getJCheckBoxMenuItemSegment().isSelected();
				
					getThis().repaint();
				}
			});
		}
		return jCheckBoxMenuItemSegment;
	}
	
	/**
	 * This method initialize jCheckBoxMenuItemResample
	 * 
	 * @return javax.swing.jCheckBoxMenuItem
	 */
	private JCheckBoxMenuItem getJCheckBoxMenuItemResample() {
		if(jCheckBoxMenuItemResample == null) {
			jCheckBoxMenuItemResample = new JCheckBoxMenuItem();
			jCheckBoxMenuItemResample.setText("View Resample");
			jCheckBoxMenuItemResample.setMnemonic(java.awt.event.KeyEvent.VK_P);
			jCheckBoxMenuItemResample.setSelected(true);
			
			jCheckBoxMenuItemResample.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, ActionEvent.CTRL_MASK));
			
			jCheckBoxMenuItemResample.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					boolean b = getJCheckBoxMenuItemResample().isSelected();
					//System.out.print("in SketchFrame.getJCheckBoxMenuItemResample...");
					//System.out.print("the b value is : "+b);
					//System.out.println();
					
					
					repaint();
				}
			});
		}
		return jCheckBoxMenuItemResample;
	}
	
	private JCheckBoxMenuItem hhh(){
		if(jCheckBoxMenuItemResample == null) {
			jCheckBoxMenuItemResample = new JCheckBoxMenuItem();
			jCheckBoxMenuItemResample.setText("clear");
			jCheckBoxMenuItemResample.setSelected(true);
			jCheckBoxMenuItemResample.addActionListener(new java.awt.event.ActionListener() {
			
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					//clear();
					repaint();
				}
			});
			
		}
	
		return jCheckBoxMenuItemResample;
	}
	
	
	
	/**
	 * This method initializes jJMenuBar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */
	
	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.add(getJMenuFile());  
			jJMenuBar.add(getJMenuView());
			//jJMenuBar.add(getJMenuKB());
			jJMenuBar.add(getJMenuOption());
			jJMenuBar.add(getJMenuAbout());
			jJMenuBar.add(getJMenuParse());
		}
		return jJMenuBar;
	}

	/**
	 * This method initializes jMenuFile	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getJMenuFile() {
		if (jMenuFile == null) {
			jMenuFile = new JMenu();
			jMenuFile.setText("File");
			jMenuFile.setMnemonic(java.awt.event.KeyEvent.VK_F);
			jMenuFile.add(getJMenuItemNew());
			//jMenuFile.add(getJMenuItemOpen());

			jMenuFile.addSeparator();

			jMenuFile.addSeparator();

		}
		return jMenuFile;
	}

	/**
	 * This method initializes jMenuItemOpen	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	
	
	

	
	 /**
	   * Return the extension portion of the file's name .
	   *
	   * @see #getExtension
	   */
	  private String getExtension(File f) {
	      return (f != null) ? getExtension(f.getName()) : "";
	  }

	  private String getExtension(String filename) {
	      return getExtension(filename, "");
	  }

	  private String getExtension(String filename, String defExt) {
	      if ((filename != null) && (filename.length() > 0)) {
	          int i = filename.lastIndexOf('.');

	          if ((i > 0) && (i < (filename.length() - 1))) {
	              return filename.substring(i + 1);
	          }
	      }
	      return defExt;
	  }
	
	/**
	 * This method initializes jMenuItemNew	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItemNew() {
		if (jMenuItemNew == null) {
			jMenuItemNew = new JMenuItem();
			jMenuItemNew.setText("New");
			jMenuItemNew.setMnemonic(java.awt.event.KeyEvent.VK_N);
			jMenuItemNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		}
		jMenuItemNew.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {

			getSketchPanel().clear();
			//System.out.println("getJCheckBoxMenuItemResample().isSelected: " + getJCheckBoxMenuItemResample().isSelected());
			repaint(); // needed

			}				
		});
		return jMenuItemNew;
	}

	/**
	 * This method initializes jMenuKB	
	 * 	
	 * @return javax.swing.JMenu	
	 
	private JMenu getJMenuKB() {
		if (jMenuKB == null) {
			jMenuKB = new JMenu();
			jMenuKB.setText("KB");
			jMenuKB.setMnemonic(java.awt.event.KeyEvent.VK_K);
		}
		return jMenuKB;
	}*/

	/**
	 * This method initializes jMenuOption	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getJMenuOption() {
		if (jMenuOption == null) {
			jMenuOption = new JMenu();
			jMenuOption.setText("Option");
			jMenuOption.setMnemonic(java.awt.event.KeyEvent.VK_O);
		}
		return jMenuOption;
	}

	/**
	 * This method initializes jMenuAbout	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getJMenuAbout() {
		if (jMenuAbout == null) {
			jMenuAbout = new JMenu();
			jMenuAbout.setText("Help");
			jMenuAbout.setMnemonic(java.awt.event.KeyEvent.VK_H);
			jMenuAbout.add(getJMenuItemHelp());
		}
		return jMenuAbout;
	}

	/**
	 * This method initializes jMenuItemHelp	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItemHelp() {
		if (jMenuItemHelp == null) {
			jMenuItemHelp = new JMenuItem();
			jMenuItemHelp.setText("About");
			jMenuItemHelp.setMnemonic(java.awt.event.KeyEvent.VK_A);
			jMenuItemHelp.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
//					System.out.println("actionPerformed()");
					
				}
			});
		}
		return jMenuItemHelp;
	}

	/**
	 * This method initializes jMenuView	
	 * 	
	 * @return javax.swing.JMenu	
	 */

	private JMenu getJMenuView() {
		if (jMenuView == null) {
			jMenuView = new JMenu();
			jMenuView.setText("Document");
			jMenuView.setMnemonic(java.awt.event.KeyEvent.VK_D);
			jMenuView.add(getJCheckBoxMenuItemSegment());
			jMenuView.add(getJCheckBoxMenuItemResample());
			
			//jMenuView.add(hhh());
			jMenuView.addSeparator();
			
			
			
		}
		return jMenuView;
	}
	
	
	private JMenu getJMenuParse() {
		if (jMenuParse == null) {
			jMenuParse = new JMenu();
			jMenuParse.setText("Parse");
			jMenuParse.setMnemonic(java.awt.event.KeyEvent.VK_P);

		}
		return jMenuParse;
	}
	/**
	 * This method initializes jPanelState	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelState() {
		if (jPanelState == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(java.awt.FlowLayout.LEFT);
			flowLayout.setVgap(2);
			flowLayout.setHgap(2);
			jLabel = new JLabel();
//			jLabel.setText(sketchView.getStateString());
			jPanelState = new JPanel();
			jPanelState.setLayout(flowLayout);
			jPanelState.add(jLabel, null);
		}
		return jPanelState;
	}
	
	
	//锟斤拷叩锟斤拷锟较�
	private JPanel getJPanelInfo() {
		if(jPanelInfo == null) {
			//FlowLayout flowLayout = new FlowLayout();
			//flowLayout.setAlignment(java.awt.FlowLayout.LEFT);
			jlSketchID = new JLabel("SketchID:");
			jlSketcherID = new JLabel("SketcherID:");
			jlStrokeNo = new JLabel("StrokeNo:");
			jSketchID = new JLabel("null");
			jSketcherID = new JLabel("null");
			jStrokeNo = new JLabel("null");
			jPanelInfo = new JPanel();
			jPanelInfo.setLayout(new GridLayout(5,1,2,2));
			jPanelInfo.add(jlSketchID);
			jPanelInfo.add(jSketchID);
			jPanelInfo.add(jlSketcherID);
			jPanelInfo.add(jSketcherID);
			jPanelInfo.add(jlStrokeNo);
			jPanelInfo.add(jStrokeNo);
		}
		return jPanelInfo;
	}
	
	private void setSketchID(int id) {
		jSketchID.setText(Integer.toString(id));
	}
	private void setSketcherID(int id) {
		jSketcherID.setText(Integer.toString(id));
	}
	
	private void setStrokeNo(int no) {
		jStrokeNo.setText(Integer.toString(no));
	}

	/**
	 * This method initializes jPanelDraw	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private SketchPanel getSketchPanel() {
		if (jPanelDraw == null) {
			jPanelDraw = new SketchPanel(getThis());
		}
		return jPanelDraw;
	}

	
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
			new SketchFrame().setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	
}