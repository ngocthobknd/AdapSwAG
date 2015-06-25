package graphic;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.FlowLayout;

import javax.swing.JSplitPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import javax.swing.JTree;
import javax.swing.JScrollPane;

import tree.ResolutionTree;
import tree.VSpecTree;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JEditorPane;

import architecture.BaseArchitecture;

import java.awt.Canvas;

public class MainGUI {

	private JFrame frame;
	private JTextField txtModelcvl;
	private JTextField txtResolutioncvl;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI window = new MainGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public MainGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 920, 718);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(1, 1, 0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.2);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		frame.getContentPane().add(splitPane);
		
		JPanel pnModel = new JPanel();
		splitPane.setRightComponent(pnModel);
		pnModel.setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane_2 = new JSplitPane();
		splitPane_2.setResizeWeight(0.8);
		pnModel.add(splitPane_2, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Base model", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPane_2.setLeftComponent(panel_1);
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));
		
		 BaseArchitecture m = new BaseArchitecture();
		 m.setDoubleBuffered(true);
		 panel_1.add(m); 
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Adaptive architecture", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPane_2.setRightComponent(panel_2);
		panel_2.setLayout(new GridLayout(0, 1, 0, 0));
		
		
		
		
		
		JPanel pnBase = new JPanel();
		splitPane.setLeftComponent(pnBase);
		pnBase.setLayout(new GridLayout(0, 1, 0, 0));
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setResizeWeight(0.3);
		pnBase.add(splitPane_1);
		
		JPanel pnVSpec = new JPanel();
		pnVSpec.setBorder(new TitledBorder(null, "VSpec tree", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPane_1.setLeftComponent(pnVSpec);
		pnVSpec.setLayout(new BorderLayout(0, 0));
		
		JPanel pnTree = new JPanel();
		pnVSpec.add(pnTree, BorderLayout.CENTER);
		pnTree.setLayout(new BorderLayout(0, 0));
	
	//	JTree tree = new JTree();
		VSpecTree tree = new VSpecTree(); 
		pnTree.add(tree);
		
		JPanel pnLoad = new JPanel();
		pnTree.add(pnLoad, BorderLayout.NORTH);
		pnLoad.setLayout(new GridLayout(0, 3, 0, 0));
		
		JLabel lblNewLabel = new JLabel("Variability model");
		pnLoad.add(lblNewLabel);
		
		txtModelcvl = new JTextField();
		txtModelcvl.setText("model.cvl");
		pnLoad.add(txtModelcvl);
		txtModelcvl.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("Load");
		pnLoad.add(btnNewButton_1);
		
		JPanel pnControl = new JPanel();
		pnControl.setBorder(new TitledBorder(null, "Control", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnVSpec.add(pnControl, BorderLayout.SOUTH);
		pnControl.setLayout(new BorderLayout(0, 0));
		
		JButton btnNewButton = new JButton("Deduce a base model");
		pnControl.add(btnNewButton);
		
		JPanel pnResolutionTree = new JPanel();
		pnResolutionTree.setBorder(new TitledBorder(null, "Resolution tree", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPane_1.setRightComponent(pnResolutionTree);
		pnResolutionTree.setLayout(new BorderLayout(0, 0));
		
		JPanel pnRTree = new JPanel();
		pnRTree.setBorder(null);
		pnResolutionTree.add(pnRTree, BorderLayout.CENTER);
		pnRTree.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		pnRTree.add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(0, 3, 0, 0));
		
		JLabel lblResolutionModel = new JLabel("Resolution model");
		panel.add(lblResolutionModel);
		
		txtResolutioncvl = new JTextField();
		txtResolutioncvl.setText("resolution.cvl");
		panel.add(txtResolutioncvl);
		txtResolutioncvl.setColumns(10);
		
		JButton btnLoad = new JButton("Load");
		panel.add(btnLoad);
		
		ResolutionTree resolvedTree = new ResolutionTree(); 
		
		pnRTree.add(resolvedTree, BorderLayout.CENTER);
		
		JPanel pnCtrl = new JPanel();
		pnCtrl.setBorder(new TitledBorder(null, "Control", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnResolutionTree.add(pnCtrl, BorderLayout.SOUTH);
		pnCtrl.setLayout(new GridLayout(0, 2, 0, 0));
		
		JButton btnVerifyConsistency = new JButton("Verify consistency");
		pnCtrl.add(btnVerifyConsistency);
		
		JButton btnNewButton_2 = new JButton("Generate adaptative architecture");
		pnCtrl.add(btnNewButton_2);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
	}
}
