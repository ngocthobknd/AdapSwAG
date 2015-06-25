package tree;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import cvl.ChoiceResolution;
import cvl.VInstance;
import cvl.VPackage;
import cvl.VSpec;
import cvl.VSpecResolution;
import cvl.VariableValueAssignment;
import cvl.cvlPackage;
public class ResolutionTree extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTree tree ;
	private Resource resource;

	public ResolutionNode getNode(VSpecResolution vSpec) {
		String type = vSpec.getClass().getSimpleName().substring(0, vSpec.getClass().getSimpleName().length()-4);
		String resolved = null;
		System.out.println(type);
		if (type.equals("ChoiceResolution")) {
			resolved = ""+((ChoiceResolution) vSpec).isDecision();
		}else if (type.equals("VariableValueAssignment")) {
			resolved = ((VariableValueAssignment)vSpec).getValue();
		} else {
			resolved = ""+((VInstance)vSpec).getNumber();
		}
		

		ResolutionNode node = new ResolutionNode(vSpec.getName(),type, resolved,true);
		if (vSpec.getChild().size() > 0)
			for (int i=0; i<vSpec.getChild().size(); i++) {
				VSpecResolution vSpecChild = vSpec.getChild().get(i); 
				node.add(getNode(vSpecChild));
			}
		return node;
	}
	public ResolutionTree() {
		cvlPackage.eINSTANCE.eClass();
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		try {
			//registry extent part of model file ex: *.variability
			reg.getExtensionToFactoryMap().put("cvl", new XMIResourceFactoryImpl());
		} catch (Exception e){
		}
		ResourceSet resourceSet = new ResourceSetImpl();
		URI uri = URI.createFileURI("resolution.cvl");
		resource = resourceSet.getResource(uri, true);
		//get root of variability model 
		ChoiceResolution vSpec = (ChoiceResolution) resource.getContents().get(0);
		VSpecResolution vSpec1 = (VSpecResolution) vSpec;
		
		ResolutionNode root = getNode(vSpec1); 

		
	    tree = new JTree(root);
		tree.putClientProperty("JTree.lineStyle", "Angled");

		NodeRenderer1 renderer = new NodeRenderer1();
		tree.setCellRenderer(renderer); 
		expandAllNodes(tree, 0, tree.getRowCount());
		tree.addMouseListener(new NodeSelectionListener1(tree));
		
		JScrollPane scrollPane = new JScrollPane(tree);
		//scrollPane.setViewportView(tree);
		setLayout(new GridLayout(1, 1, 0, 0));
		add(scrollPane);
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
		ResolutionTree frTree = new ResolutionTree();
		fr.add(frTree);
		fr.setSize(400,450);
		fr.setVisible(true);
	}
}

class NodeSelectionListener1 extends MouseAdapter {
    JTree tree;
    
    NodeSelectionListener1(JTree tree) {
      this.tree = tree;
    }
    
    public void mouseClicked(MouseEvent e) {
      int x = e.getX();
      int y = e.getY();
      int row = tree.getRowForLocation(x, y);
      TreePath  path = tree.getPathForRow(row);
      if (path != null) {
    	  ResolutionNode node = (ResolutionNode)path.getLastPathComponent();
	        boolean isSelected = ! (node.isSelected());
		    node.setSelected(isSelected);
		    if(node.isSelected()) ((ResolutionNode)path.getPathComponent(path.getPathCount()-1)).isSelected();		    
		    ((DefaultTreeModel) tree.getModel()).nodeChanged(node);
		    
      }
      TreePath  path1 = tree.getSelectionPath();
      System.out.println(path1.toString());
    }
  }

class NodeRenderer1 extends JPanel implements TreeCellRenderer {
	private JCheckBox chk_leafRenderer = new JCheckBox();
	private JTextField txt_leafRenderer = new JTextField("       ");
	private JLabel lbl_Node = new JLabel();

	public NodeRenderer1() {
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
		ResolutionNode node = (ResolutionNode)value;
		if (leaf) {
				lbl_Node.setText(node.getName()+":"+node.type + ":" +node.getResolved());
				chk_leafRenderer.setSelected(node.selected);
				chk_leafRenderer.setEnabled(true);
				//this.add(chk_leafRenderer);
				return this;
		} else {
				
				lbl_Node.setText(node.getName()+":"+node.type+ ":" +node.getResolved());
				chk_leafRenderer.setSelected(node.selected);
				chk_leafRenderer.setEnabled(true);
				//this.add(chk_leafRenderer);
				return this;
			
		}
	}
}

class ResolutionNode extends DefaultMutableTreeNode {
	String name;
	String type;
	String resolved;
	boolean selected;
	public ResolutionNode(String name, String type, boolean selected) {
		this.name = name;
		this.selected = selected;
		this.type = type;
	}
	
	public ResolutionNode(String name, String type, String resolved, boolean selected) {
		this.name = name;
		this.selected = selected;
		this.type = type;
		this.resolved = resolved;
		
	}
		
	
	public String getResolved() {
		return resolved;
	}

	public void setResolved(String resolved) {
		this.resolved = resolved;
	}

	public String getType() {
		return type;
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

