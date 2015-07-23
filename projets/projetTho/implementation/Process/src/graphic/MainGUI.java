package graphic;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import transformation.GeneratingProductArchitecture;
import tree.ResolutionTree;
import tree.VSpecTree;
import verification.ResolutionModelVerification;
import architecture.BaseArchitecture;
import architecture.ProductArchitecture;

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
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
		   
	    BaseArchitecture m = new BaseArchitecture();
	    //scrollPane.setViewportView(m);
	    m.setDoubleBuffered(true);
	    JScrollPane scrollPane = new JScrollPane(m);
	    panel_1.add(scrollPane);
	    
	    final JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Adaptive architecture", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPane_2.setRightComponent(panel_2);
		panel_2.setLayout(new GridLayout(0, 1, 0, 0));
		
		//final ProductArchitecture pa = new ProductArchitecture();
		
		final ProductArchitecture prA = new ProductArchitecture();
		final JScrollPane scrollPane_2 = new JScrollPane(prA);
		panel_2.add(scrollPane_2);
	
		
		JPanel pnVmodel = new JPanel();
		splitPane.setLeftComponent(pnVmodel);
		pnVmodel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setResizeWeight(0.3);
		pnVmodel.add(splitPane_1);
		
		JPanel pnVSpec = new JPanel();
		pnVSpec.setBorder(new TitledBorder(null, "VSpec tree", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPane_1.setLeftComponent(pnVSpec);
		pnVSpec.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel pnTree = new JPanel();
		pnVSpec.add(pnTree);
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
		pnControl.setBorder(new TitledBorder(null, "Variation Points", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnVSpec.add(pnControl);
		pnControl.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		pnControl.add(scrollPane_1, BorderLayout.CENTER);
		DefaultListModel model = tree.vpInJList;
		JList list = new JList(model);
		
		
		scrollPane_1.setViewportView(list);
		
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
		
		JButton btnVerifyConsistency = new JButton("Verify RM");
		btnVerifyConsistency.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ResolutionTree rs;
				VSpecTree vSpec;
				rs = new ResolutionTree();
				vSpec = new VSpecTree();
				int mc = JOptionPane.WARNING_MESSAGE;
				ResolutionModelVerification rmv = new ResolutionModelVerification(vSpec.vSpecList, rs.resolutionList);
				if (rmv.verifyRM()) {
					JOptionPane.showMessageDialog (null, "Verification OK",
							"Verification", mc);
				} else JOptionPane.showMessageDialog (null, "Errors: "+rmv.messageAlert,
						"Verification", mc);
				System.out.println("Result of verification: "+new ResolutionModelVerification(vSpec.vSpecList, rs.resolutionList).verifyRM());
			}
		});
		pnCtrl.add(btnVerifyConsistency);
		
		JButton btnNewButton_2 = new JButton("Generate adaptative architecture");
		btnNewButton_2.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent paramActionEvent) {
				GeneratingProductArchitecture generateProduct = new GeneratingProductArchitecture();
				generateProduct.createProductModel(generateProduct.readArchitecture(""));
				
				prA.loadModel();
				prA.revalidate();
				scrollPane_2.revalidate();
				
				
				panel_2.revalidate();
				splitPane_2.revalidate();
				pnBase.revalidate();
				splitPane.revalidate();
				frame.dispose();
				MainGUI window = new MainGUI();
				window.frame.setVisible(true);
				
			}
		});
		pnCtrl.add(btnNewButton_2);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
	}
}
