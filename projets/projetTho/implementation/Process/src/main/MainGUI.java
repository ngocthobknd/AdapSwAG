package main;

import generation.productArchitecture.ProductGeneration;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.TitledBorder;

import org.ow2.fractal.f4e.fractal.Definition;

import resolution.gui.ResolutionModelGUI;
import variability.gui.VariabilityModelGUI;
import verification.ResolutionModelVerification;
import architecture.gui.BaseArchitectureGUI;

public class MainGUI {

	private JFrame frame;
	String variabilityModelFileName = "model//primitive//model.cvl";
	String resolutionModelFileName = "model//primitive//resolution.cvl";
	String baseModelFileName =  "model//composite2//architecture.fractal";
	String productModelFileName = "model//composite2//product.fractal";
	/**
	 * Launch the application.
	 */
	
	BaseArchitectureGUI product, baseModel ;
	VariabilityModelGUI variabilityModel;
	ResolutionModelGUI resolutionModel;
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
	@SuppressWarnings({ })
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(0, 0, 1920, 1080);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(1, 1, 0, 0));
		
		final JSplitPane splitPane = new JSplitPane();
		splitPane.setContinuousLayout(true);
		splitPane.setResizeWeight(0.5);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		frame.getContentPane().add(splitPane);
		
		final JPanel pnBase = new JPanel();
		splitPane.setRightComponent(pnBase);
		pnBase.setLayout(new BorderLayout(0, 0));
		
		final JSplitPane splitPane_2 = new JSplitPane();
		splitPane_2.setResizeWeight(0.7);
		pnBase.add(splitPane_2, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Base model", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPane_2.setLeftComponent(panel_1);
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));
		   
	    //BaseArchitecture m = new BaseArchitecture();
		baseModel = new BaseArchitectureGUI(baseModelFileName);
		panel_1.add(baseModel);
		//scrollPane.setViewportView(m);
	    baseModel.setDoubleBuffered(true);
	    
	    final JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Adaptive architecture", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPane_2.setRightComponent(panel_2);
		panel_2.setLayout(new GridLayout(0, 1, 0, 0));
		
		//final ProductArchitecture pa = new ProductArchitecture();
		
		//final ProductArchitecture prA = new ProductArchitecture();
		product = new BaseArchitectureGUI(productModelFileName);
		panel_2.add(product);
	
		
		JPanel pnVmodel = new JPanel();
		splitPane.setLeftComponent(pnVmodel);
		pnVmodel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setResizeWeight(0.3);
		pnVmodel.add(splitPane_1);
		
		JPanel pnVSpec = new JPanel();
		pnVSpec.setBorder(new TitledBorder(null, "Variability model", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPane_1.setLeftComponent(pnVSpec);
		pnVSpec.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel pnTree = new JPanel();
		pnVSpec.add(pnTree);
		pnTree.setLayout(new BorderLayout(0, 0));

		
		variabilityModel = new VariabilityModelGUI(variabilityModelFileName);
		pnTree.add(variabilityModel);
		
		
		JPanel pnResolutionTree = new JPanel();
		pnResolutionTree.setBorder(new TitledBorder(null, "Resolution tree", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPane_1.setRightComponent(pnResolutionTree);
		pnResolutionTree.setLayout(new BorderLayout(0, 0));
		
		JPanel pnRTree = new JPanel();
		pnRTree.setBorder(null);
		pnResolutionTree.add(pnRTree, BorderLayout.CENTER);
		pnRTree.setLayout(new BorderLayout(0, 0));
		
		//ResolutionTree resolvedTree = new ResolutionTree(resolutionModelFileName); 
		resolutionModel = new ResolutionModelGUI(resolutionModelFileName); 
		pnRTree.add(resolutionModel, BorderLayout.CENTER);
		
		JPanel pnCtrl = new JPanel();
		pnCtrl.setBorder(new TitledBorder(null, "Control", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnResolutionTree.add(pnCtrl, BorderLayout.SOUTH);
		pnCtrl.setLayout(new GridLayout(0, 2, 0, 0));
		
		JButton btnVerifyConsistency = new JButton("Verify RM");
		btnVerifyConsistency.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int mc = JOptionPane.WARNING_MESSAGE;
				ResolutionModelVerification rmv = new 
						ResolutionModelVerification(variabilityModel.getVSpecList(), 
								resolutionModel.getVSpecResolutionList());
				
				System.out.println("1."+variabilityModel.getVSpecList().size());
				System.out.println("2."+resolutionModel.getVSpecResolutionList().size());
				
				if (rmv.verifyRM()) {
					JOptionPane.showMessageDialog (null, "Verification OK",
							"Verification", mc);
				} else JOptionPane.showMessageDialog (null, "Errors: "+rmv.messageAlert,
						"Verification", mc);
				System.out.println("Result of verification: "+
						(new ResolutionModelVerification(variabilityModel.getVSpecList(), resolutionModel.getVSpecResolutionList())).verifyRM());
			}
		});
		pnCtrl.add(btnVerifyConsistency);
		
		JButton btnNewButton_2 = new JButton("Generate adaptative architecture");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent paramActionEvent) {
				//GeneratingProductArchitecture generateProduct = new GeneratingProductArchitecture(variabilityModelFileName, resolutionModelFileName, baseModelFileName, productModelFileName);
				//Definition definition = baseModel.getArchitectureDefinition();
				ProductGeneration generateProduct = new ProductGeneration(baseModel.getComponentList(),
						baseModel.getBindingList(), 
						variabilityModel.getVSpecList(), 
						variabilityModel.getVariationPointList(),
						resolutionModel.getVSpecResolutionList());
				
				String baseModelFileName =  baseModel.baseModelFileName;
				int i = baseModelFileName.lastIndexOf("/");
				String header = baseModelFileName.substring(0, i);
				String footer =  baseModelFileName.substring(i + 1);
				String productModelFileName = header + "//" + "generated" + footer;
				
				
				generateProduct.createProductModel(baseModel.getArchitectureDefinition(), productModelFileName);
				product.setText(productModelFileName);
				//prA.loadModel();
				//prA.revalidate();
				
				panel_2.revalidate();
				splitPane_2.revalidate();
				pnBase.revalidate();
				splitPane.revalidate();
				
			}
		});
		pnCtrl.add(btnNewButton_2);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
	}
}
