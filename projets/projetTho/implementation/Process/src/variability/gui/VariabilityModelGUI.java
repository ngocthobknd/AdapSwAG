package variability.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import variability.api.VariabilityModelService;
import variability.implement.VariabilityModel;
import cvl.ObjectExistence;
import cvl.ObjectHandle;
import cvl.ObjectSubstitution;
import cvl.ParametricSlotAssignment;
import cvl.VPackage;
import cvl.VSpec;
import cvl.VariationPoint;
import cvl.Choice;



@SuppressWarnings("serial")
public class VariabilityModelGUI extends JPanel {
	
	//public String newFileName;
	
	
	private JTree tree;
	@SuppressWarnings("rawtypes")
	private JList list;
	
	private VariabilityModelService vty;
	private VPackage vPackage;
	String variabilityModelFileName;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public VariabilityModelGUI(String cvlFileName) {
		// TODO Auto-generated constructor stub
		//this.newFileName = cvlFileName;
		this.variabilityModelFileName = cvlFileName;
		setLayout(new BorderLayout());
		JPanel pnLoad = new JPanel();
		pnLoad.setLayout(new BorderLayout());
		
		JLabel lblNewLabel = new JLabel("CVL File");
		pnLoad.add(lblNewLabel, BorderLayout.WEST);
		
		final JTextField txtModelcvl = new JTextField();
		String tm = cvlFileName.replaceAll("//", "/");
		txtModelcvl.setText(tm);
		pnLoad.add(txtModelcvl, BorderLayout.CENTER);
		txtModelcvl.setColumns(30);
		
		JButton btnNewButton_1 = new JButton("Load");
		
		pnLoad.add(btnNewButton_1, BorderLayout.EAST);
		
		add(pnLoad, BorderLayout.NORTH);
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.7);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		vty = new VariabilityModel();
		vPackage = vty.getVPackage(cvlFileName);
		Node root = getNode(vty.getVSpecTreeRoot(vPackage));
		
		tree = new JTree(root);
		tree.putClientProperty("JTree.lineStyle", "Angled");
		NodeRenderer renderer = new NodeRenderer();
		tree.setCellRenderer(renderer); 
		expandAllNodes(tree, 0, tree.getRowCount());
		tree.addMouseListener(new NodeSelectionListener(tree));
		JScrollPane scrollPane = new JScrollPane(tree);
		scrollPane.setBorder(new TitledBorder(null, "VSpec tree", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPane.setTopComponent(scrollPane);
	
		//List VP in GUI
		
		ArrayList<VariationPoint> vpList = vty.getVariationPointList(vPackage);
		DefaultListModel vpInJList = this.getVPListModel(vpList); 
		
		list = new JList(vpInJList);
		list.setBorder(new TitledBorder(null, "Variation Point", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPane.setBottomComponent(list);
		add(splitPane, BorderLayout.CENTER);
		
		
		btnNewButton_1.addActionListener(new ActionListener() {
	        @Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
	        	JFileChooser fc = new JFileChooser();
	        	File fi = new File("/home/DiskD/Dropbox/workspace/Process/model");
	            fc.setCurrentDirectory(fi);
	            
	            int returnVal = fc.showOpenDialog(VariabilityModelGUI.this);
	        	if (returnVal == JFileChooser.APPROVE_OPTION) {
	        		File file = fc.getSelectedFile();
	        		String newFileName = file.getAbsolutePath();
	        		txtModelcvl.setText(newFileName);
	        		
	        		vPackage = vty.getVPackage(newFileName);
	        		TreeModel treeModel = new DefaultTreeModel(getNode(vty.getVSpecTreeRoot(vPackage)));
	        		tree.setModel(treeModel);
	        		expandAllNodes(tree, 0, tree.getRowCount());
	        		
	        		ArrayList<VariationPoint> vpList = vty.getVariationPointList(vPackage);
	        		DefaultListModel vpInJList = getVPListModel(vpList); 
	        		list.setModel(vpInJList);
	 	        	 
	        	} else {
	        		//log.append("Open command cancelled by user." + newline);
	        	}
	        	
			}
	      });
	}
	
	public VPackage getVPackage() {
		// TODO Auto-generated method stub
		return vty.getVPackage(variabilityModelFileName);
	}
	public VSpec getVSpecTreeRoot() {
		// TODO Auto-generated method stub
		return vty.getVSpecTreeRoot(vPackage);
	}
	public ArrayList<VSpec> getVSpecList() {
		// TODO Auto-generated method stub
		return vty.getVSpecList(vPackage);
	}
	public ArrayList<VariationPoint> getVariationPointList() {
		// TODO Auto-generated method stub
		return vty.getVariationPointList(vPackage);
	}
	public Node getNode(VSpec vSpec) {
		String type = vSpec.getClass().getSimpleName().substring(0, vSpec.getClass().getSimpleName().length()-4);
		String groupMultiplicity;
		String availabilityTime;
		String defaultResolution;
		if (vSpec.getAvailabilityTime().getName().equals("default")) {
			availabilityTime = "runtime";
		} else availabilityTime = vSpec.getAvailabilityTime().getName(); 
		if (vSpec.getGroupMultiplicity() != null) {
			groupMultiplicity = "[" + vSpec.getGroupMultiplicity().getLower() +","+vSpec.getGroupMultiplicity().getUpper() + "]" ;
		} else groupMultiplicity = "";
		if (vSpec instanceof Choice) {
			defaultResolution = ""+((Choice)vSpec).isDefaultResolution();
		} else defaultResolution = "";
		Node node = new Node(vSpec.getName(),type, groupMultiplicity, "", availabilityTime, defaultResolution, true);
		if (vSpec.getChild().size() > 0)
			for (int i=0; i<vSpec.getChild().size(); i++) {
				VSpec vSpecChild = vSpec.getChild().get(i); 
				node.add(getNode(vSpecChild));
			}
		return node;
	}
	private void expandAllNodes(JTree tree, int startingIndex, int rowCount){
	    for(int i=startingIndex;i<rowCount;++i){
	        tree.expandRow(i);
	    }

	    if(tree.getRowCount()!=rowCount){
	        expandAllNodes(tree,rowCount, tree.getRowCount());
	    }
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DefaultListModel getVPListModel(ArrayList<VariationPoint> vpList) {
		DefaultListModel vpInJList = new DefaultListModel(); 
		for (int i = 1; i < vpList.size(); i++) {
			//System.out.println(packageElement.size());
			VariationPoint vp = vpList.get(i);
			if (vp instanceof ObjectExistence) {
				ObjectHandle oObject = ((ObjectExistence) vp).getOptionalObject();
				vpInJList.addElement(vp.getName() + ":ObjectExistence (" + vp.getBindingVSpec().getName()+" -> " + oObject.getMOFRef()+")");
			} 
			else if (vp instanceof ParametricSlotAssignment) {
				ObjectHandle oObject = ((ParametricSlotAssignment) vp).getSlotOwner();
				vpInJList.addElement(vp.getName() + ":ParametricSlotAssignment (" + vp.getBindingVSpec().getName() + " -> "+oObject.getMOFRef() + ")");
			}
			else if (vp instanceof ObjectSubstitution) {
				ObjectHandle oPlacementObject = ((ObjectSubstitution) vp).getPlacementObject();
				ObjectHandle oReplacementObject = ((ObjectSubstitution) vp).getReplacementObject();
				vpInJList.addElement(vp.getName()+":ObjectSubstitution (" + vp.getBindingVSpec().getName()+" -> " + oPlacementObject.getMOFRef() +
						"," + oReplacementObject.getMOFRef() + ")");
		
			}
		}
		return vpInJList;
	}

	public static void main(String arg[]) {
		JFrame fr = new JFrame();
		VariabilityModelGUI frTree = new VariabilityModelGUI("model//primitive//model.cvl");
		fr.setLayout(new BorderLayout());
		//frTree.setLayout(new BorderLayout());
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.add(frTree,BorderLayout.CENTER);
		fr.setSize(400,450);
		fr.setVisible(true);
	}
	
}
class NodeSelectionListener extends MouseAdapter {
    JTree tree;
    
    NodeSelectionListener(JTree tree) {
      this.tree = tree;
    }
    
    public void mouseClicked(MouseEvent e) {
    	
    	int x = e.getX();
    	int y = e.getY();
    	int row = tree.getRowForLocation(x, y);
    	TreePath  path = tree.getPathForRow(row);
    	if (path != null) {
    		Node node = (Node)path.getLastPathComponent();
	        boolean isSelected = ! (node.isSelected());
		    node.setSelected(isSelected);
		    if(node.isSelected()) ((Node)path.getPathComponent(path.getPathCount()-1)).isSelected();		    
		    ((DefaultTreeModel) tree.getModel()).nodeChanged(node);
		    
    	}
    }
}

@SuppressWarnings("serial")
class NodeRenderer extends JPanel implements TreeCellRenderer {
	private JCheckBox chk_leafRenderer = new JCheckBox();
	private JLabel lbl_Node = new JLabel();

	public NodeRenderer() {
		add(lbl_Node);
	}
	protected JCheckBox getChkBoxRenderer() {
		return chk_leafRenderer;
	}
	protected int getTypeRenderer() {
		return 0;
	}
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, 
			boolean expanded, boolean leaf, int row, boolean hasFocus) {
		Node node = (Node)value;
		if (leaf) {
				this.setBackground(Color.WHITE);
				if (node.defaultResolution.equals("true"))
				lbl_Node.setText(node.getName()+":"+node.type + node.getGroupMultiplicity() + ":" 
									+ node.availabilityTime 
									+ ":"+node.defaultResolution);
				else lbl_Node.setText(node.getName()+":"+node.type + node.getGroupMultiplicity() + ":" + node.availabilityTime);
				chk_leafRenderer.setSelected(node.selected);
				chk_leafRenderer.setEnabled(true);
				//this.add(chk_leafRenderer);
				return this;
		} 
		else {
				this.setBackground(Color.WHITE);
				//lbl_Node.setText(node.getName()+":"+node.type+ node.getGroupMultiplicity()+ ":" + node.availabilityTime);
				if (node.defaultResolution.equals("true"))
					lbl_Node.setText(node.getName()+":"+node.type + node.getGroupMultiplicity() + ":" 
										+ node.availabilityTime 
										+ ":"+node.defaultResolution);
					else lbl_Node.setText(node.getName()+":"+node.type + node.getGroupMultiplicity() + ":" + node.availabilityTime);
				chk_leafRenderer.setSelected(node.selected);
				chk_leafRenderer.setEnabled(true);
				//this.add(chk_leafRenderer);
				return this;
			
		}
		//return null;
	}
}
class VariationPointClass {
	String name;
	String type;
	String vSpec;
	String refMOF1;
	String refMOF2;
	public VariationPointClass(String name, String type, String vSpec,
			String refMOF1, String refMOF2) {
		super();
		this.name = name;
		this.type = type;
		this.vSpec = vSpec;
		this.refMOF1 = refMOF1;
		this.refMOF2 = refMOF2;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getvSpec() {
		return vSpec;
	}
	public void setvSpec(String vSpec) {
		this.vSpec = vSpec;
	}
	public String getRefMOF1() {
		return refMOF1;
	}
	public void setRefMOF1(String refMOF1) {
		this.refMOF1 = refMOF1;
	}
	public String getRefMOF2() {
		return refMOF2;
	}
	public void setRefMOF2(String refMOF2) {
		this.refMOF2 = refMOF2;
	}
	
}
@SuppressWarnings("serial")
class Node extends DefaultMutableTreeNode {
	String name;
	String type;
	String groupMultiplicity;
	String resolutionTime;
	String availabilityTime;
	String defaultResolution;
	boolean selected;
	public Node(String name, String type, boolean selected) {
		this.name = name;
		this.selected = selected;
		this.type = type;
	}
	
	public Node(String name, String type, String groupMultiplicity, 
			String resolutionTime, String availabilityTime, 
			String defaultResolution,
			boolean selected) {
		this.name = name;
		this.selected = selected;
		this.type = type;
		this.groupMultiplicity = groupMultiplicity;
		this.resolutionTime = resolutionTime;
		this.availabilityTime = availabilityTime;
		this.defaultResolution = defaultResolution;
	}
		
	public String getDefaultResolution() {
		return defaultResolution;
	}

	public void setDefaultResolution(String defaultResolution) {
		this.defaultResolution = defaultResolution;
	}

	public String getGroupMultiplicity() {
		return groupMultiplicity;
	}
	public void setGroupMultiplicity(String groupMultiplicity) {
		this.groupMultiplicity = groupMultiplicity;
	}
	public String getResolutionTime() {
		return resolutionTime;
	}
	public void setResolutionTime(String resolutionTime) {
		this.resolutionTime = resolutionTime;
	}
	public String getAvailabilityTime() {
		return availabilityTime;
	}
	public void setAvailabilityTime(String availabilityTime) {
		this.availabilityTime = availabilityTime;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean newValue) {
		selected = newValue;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		return getClass().getName() + "[" + name + "/" + selected +"/" + type + "]";
	}
}
//class NamedVector extends Vector {
//	String name;
//	public NamedVector(String name) {
//		this.name = name;
//	}
//	public NamedVector(String name, Object elements[]) {
//		this.name = name;
//		for (int i = 0, n = elements.length; i < n; i++) {
//			add(elements[i]);
//		}
//	}
//	public String toString() {
//		return "[" + name + "]";
//	}
//}