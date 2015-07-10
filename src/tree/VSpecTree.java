package tree;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.lang.model.element.PackageElement;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import cvl.ObjectExistence;
import cvl.ObjectHandle;
import cvl.ObjectSubstitution;
import cvl.ParametricSlotAssignment;
import cvl.VPackage;
import cvl.VPackageable;
import cvl.VSpec;
import cvl.Variable;
import cvl.VariationPoint;
import cvl.cvlPackage;
public class VSpecTree extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTree tree ;
	private Resource resource;
	public ArrayList<VariationPoint> VPList = new ArrayList<>();
	public DefaultListModel listModel = new DefaultListModel(); //variation points in JList 
	/*
	 * procedure for getting all nodes in a VSpec
	 */
	public Node getNode(VSpec vSpec) {
		String type = vSpec.getClass().getSimpleName().substring(0, vSpec.getClass().getSimpleName().length()-4);
		String groupMultiplicity;
		if (vSpec.getGroupMultiplicity() != null) {
			groupMultiplicity = "[" + vSpec.getGroupMultiplicity().getLower() +","+vSpec.getGroupMultiplicity().getUpper() + "]" ;
			System.out.println(groupMultiplicity);
		} else groupMultiplicity = ""; 
		Node node = new Node(vSpec.getName(),type, groupMultiplicity, "","",true);
		
		if (vSpec.getChild().size() > 0)
			for (int i=0; i<vSpec.getChild().size(); i++) {
				VSpec vSpecChild = vSpec.getChild().get(i); 
				
				node.add(getNode(vSpecChild));
			}
		return node;
	}
	public VSpecTree() {
		cvlPackage.eINSTANCE.eClass();
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		try {
			//registry extent part of model file ex: *.variability
			reg.getExtensionToFactoryMap().put("cvl", new XMIResourceFactoryImpl());
		} catch (Exception e){
		}
		ResourceSet resourceSet = new ResourceSetImpl();
		URI uri = URI.createFileURI("model//model.cvl");
		resource = resourceSet.getResource(uri, true);
		//get root of variability model 
		VPackage vPackage = (VPackage) resource.getContents().get(0);
		EList<VPackageable> packageElement = vPackage.getPackageElement();
		
		VSpec vSpec = (VSpec) packageElement.get(0);
		//System.out.println(vSpec.toString());
		Node root = getNode(vSpec); 
	    tree = new JTree(root);
		tree.putClientProperty("JTree.lineStyle", "Angled");
		NodeRenderer renderer = new NodeRenderer();
		tree.setCellRenderer(renderer); 
		expandAllNodes(tree, 0, tree.getRowCount());
//		NodeEditor nodeEditor = new NodeEditor(tree);
//		tree.setCellEditor(nodeEditor);
//		tree.setEditable(true);
		tree.addMouseListener(new NodeSelectionListener(tree));
		
		JScrollPane scrollPane = new JScrollPane(tree);
		//scrollPane.setViewportView(tree);
		setLayout(new GridLayout(1, 1, 0, 0));
		add(scrollPane);
		
		//list variation points
		for (int i = 1; i < packageElement.size(); i++) {
			VariationPoint vp = (VariationPoint)packageElement.get(i);
			VPList.add(vp);
			if (vp instanceof ObjectExistence) {
				ObjectHandle oObject = ((ObjectExistence) vp).getOptionalObject();
				listModel.addElement(vp.getName()+":ObjectExistence ("+vp.getBindingVSpec().getName()+" -> "+oObject.getMOFRef()+")");
			} 
			else if (vp instanceof ParametricSlotAssignment) {
				ObjectHandle oObject = ((ParametricSlotAssignment) vp).getSlotOwner();
				listModel.addElement(vp.getName()+":ParametricSlotAssignment ("+vp.getBindingVSpec().getName()+" -> "+oObject.getMOFRef()+")");
			}
			else if (vp instanceof ObjectSubstitution) {
				ObjectHandle oPlacementObject = ((ObjectSubstitution) vp).getPlacementObject();
				ObjectHandle oReplacementObject = ((ObjectSubstitution) vp).getReplacementObject();
				listModel.addElement(vp.getName()+":ObjectSubstitution ("+vp.getBindingVSpec().getName()+" -> "+oPlacementObject.getMOFRef()+
						","+oReplacementObject.getMOFRef()+")");
		
			}
		}
	}
	private void expandAllNodes(JTree tree, int startingIndex, int rowCount){
	    for(int i=startingIndex;i<rowCount;++i){
	        tree.expandRow(i);
	    }

	    if(tree.getRowCount()!=rowCount){
	        expandAllNodes(tree,rowCount, tree.getRowCount());
	    }
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame fr = new JFrame();
		//fr.setLayout(null);
		// add panel which contains tree to frame
		VSpecTree frTree = new VSpecTree();
		fr.add(frTree);
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
      TreePath  path1 = tree.getSelectionPath();
      System.out.println(path1.toString());
    }
  }

class NodeRenderer extends JPanel implements TreeCellRenderer {
	private JCheckBox chk_leafRenderer = new JCheckBox();
	private JTextField txt_leafRenderer = new JTextField("       ");
	private JLabel lbl_Node = new JLabel();

	public NodeRenderer() {
		add(lbl_Node);
	}
	protected JCheckBox getChkBoxRenderer() {
		return chk_leafRenderer;
	}
//	protected JTextField getTxtRenderer() {
//		return txt_leafRenderer;
//	}
//	protected JPanel getJPanel(){
//		return this;
//	}
	protected int getTypeRenderer() {
		return 0;
	}
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, 
			boolean expanded, boolean leaf, int row, boolean hasFocus) {
		Node node = (Node)value;
		if (leaf) {
				lbl_Node.setText(node.getName()+":"+node.type + node.getGroupMultiplicity());
				chk_leafRenderer.setSelected(node.selected);
				chk_leafRenderer.setEnabled(true);
				//this.add(chk_leafRenderer);
				return this;
		} else {
				
				lbl_Node.setText(node.getName()+":"+node.type+ node.getGroupMultiplicity());
				chk_leafRenderer.setSelected(node.selected);
				chk_leafRenderer.setEnabled(true);
				//this.add(chk_leafRenderer);
				return this;
			
		}
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
class Node extends DefaultMutableTreeNode {
	String name;
	String type;
	String groupMultiplicity;
	String resolutionTime;
	String availabilityTime;
	boolean selected;
	public Node(String name, String type, boolean selected) {
		this.name = name;
		this.selected = selected;
		this.type = type;
	}
	
	public Node(String name, String type, String groupMultiplicity, String resolutionTime, String availabilityTime, boolean selected) {
		this.name = name;
		this.selected = selected;
		this.type = type;
		this.groupMultiplicity = groupMultiplicity;
		this.resolutionTime = resolutionTime;
		this.availabilityTime = availabilityTime;
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
class NamedVector extends Vector {
	String name;
	public NamedVector(String name) {
		this.name = name;
	}
	public NamedVector(String name, Object elements[]) {
		this.name = name;
		for (int i = 0, n = elements.length; i < n; i++) {
			add(elements[i]);
		}
	}
	public String toString() {
		return "[" + name + "]";
	}
}
