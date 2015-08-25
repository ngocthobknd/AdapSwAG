package main;

import generation.productArchitecture.ACMEGeneration;
import generation.productArchitecture.FractalGeneration;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.TitledBorder;

import org.ow2.fractal.f4e.fractal.Binding;
import org.ow2.fractal.f4e.fractal.Component;
import org.ow2.fractal.f4e.fractal.Definition;

import product.gui.ProductArchitectureGUI;
import product.gui.ProductArchitectureGUI_temp;
import ACME.Attachment;
import ACME.ComponentInstance;
import base.acme.implement.ACMEImpl;
import base.api.BaseArchitectureService;
import base.fractalADL.implement.FractalADLImpl;
import base.gui.BaseArchitectureGUI;
import resolution.gui.ResolutionModelGUI;
import test.ProductGeneration;
import variability.gui.VariabilityModelGUI;
import verification.ResolutionModelVerification;
import cvl.*;

public class MainGUI {

	private JFrame frame;
	String variabilityModelFileName = "//home//DiskD//Dropbox//workspace//Process//model//fractal2//model.cvl";
	String resolutionModelFileName = "//home//DiskD//Dropbox//workspace//Process//model//fractal2//resolution.cvl";
	String baseModelFileName =  "//home//DiskD//Dropbox//workspace//Process//model//fractal2//architecture.fractal";
	String productModelFileName = "";
	
	/**
	 * Launch the application.
	 */
	private ProductArchitectureGUI product;
	private BaseArchitectureGUI baseModel ;
	private VariabilityModelGUI variabilityModel;
	private ResolutionModelGUI resolutionModel;
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
		frame.setTitle("Adptive software architecture generation");
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
		   
		baseModel = new BaseArchitectureGUI(baseModelFileName);
		panel_1.add(baseModel);
	    
	    final JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Adaptive architecture", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPane_2.setRightComponent(panel_2);
		panel_2.setLayout(new GridLayout(0, 1, 0, 0));
		
		product = new ProductArchitectureGUI(productModelFileName);
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
				baseModelFileName = baseModel.getFileName();
				int i = baseModelFileName.lastIndexOf("/");
				String header = baseModelFileName.substring(0, i);
				String footer =  baseModelFileName.substring(i + 1);
				String productModelFileName = header + "/" + "generated" + footer;
//				ProductGeneration generateProduct = new ProductGeneration(baseModel.getComponentList(),
//						baseModel.getBindingList(), 
//						variabilityModel.getVSpecList(), 
//						variabilityModel.getVariationPointList(),
//						resolutionModel.getVSpecResolutionList());
//				generateProduct.createProductModel(baseModel.getArchitectureDefinition(), productModelFileName);
				ArrayList<VSpec> vSpecList = variabilityModel.getVSpecList(); 
				ArrayList<VariationPoint> vpList =	variabilityModel.getVariationPointList();
				ArrayList<VSpecResolution> vSpecResolutionList = resolutionModel.getVSpecResolutionList();

				BaseArchitectureService baseArchitectureService = baseModel.baseArchitecture;
				if (baseArchitectureService instanceof FractalADLImpl) {
					Definition definition = baseModel.getDefinition();
					ArrayList<Component> sourceComponentList = baseModel.getParentComponentList(definition);
					ArrayList<Binding> sourceBindingList = baseModel.getParentBindingList(definition);
					
					FractalGeneration fractalGeneration = new FractalGeneration(vSpecList, 
							vpList, 
							vSpecResolutionList, 
							sourceComponentList, 
							sourceBindingList); 
					
					fractalGeneration.create(productModelFileName, definition.getName());
				} else if (baseArchitectureService instanceof ACMEImpl) {
					ACME.System sys = baseModel.getACMESystem();
					ArrayList<ComponentInstance> sourceComponentList = baseModel.getParentComponentList(sys);
					ArrayList<ACME.Connector> connectorList = baseModel.getParentConnectorList(sys);
					ArrayList<Attachment> attachmentList = baseModel.getParentAttchmentList(sys);
					ArrayList<ACME.Binding> sourceBindingList = baseModel.getParentBindingList(sys);
					
					ACMEGeneration acme = new ACMEGeneration(vSpecList, 
							vpList, 
							vSpecResolutionList, 
							sourceComponentList, 
							connectorList,
							attachmentList, 
							sourceBindingList);
					acme.create(productModelFileName, sys.getName());
				}
				product.setText(productModelFileName);
				product.txtModelcvl.setText(productModelFileName);
				
				//System.out.println("2:"+baseModel.getComponentList().size());	
			}
		});
		pnCtrl.add(btnNewButton_2);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
	}
}
