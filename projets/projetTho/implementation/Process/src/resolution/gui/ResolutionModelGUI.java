package resolution.gui;

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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import cvl.ChoiceResolution;
import cvl.VInstance;
import cvl.VPackage;
import cvl.VSpecResolution;
import cvl.VariableValueAssignment;
import cvl.VariationPoint;
import resolution.api.ResolutionModelService;
import resolution.implement.ResolutionModel;





@SuppressWarnings("serial")
public class ResolutionModelGUI extends JPanel{
	
	private JTree  tree;
	private ResolutionModelService resolutionModel;
	private VSpecResolution vSpecResolutionRoot;
	public ResolutionModelGUI(String resolutionModelFileName) {
		setLayout(new BorderLayout());
		JPanel pnLoad = new JPanel();
		pnLoad.setLayout(new BorderLayout());
		
		JLabel lblNewLabel = new JLabel("CVL File");
		pnLoad.add(lblNewLabel, BorderLayout.WEST);
		
		final JTextField txtModelcvl = new JTextField();
		String tm = resolutionModelFileName.replaceAll("//", "/");
		txtModelcvl.setText(tm);
		pnLoad.add(txtModelcvl, BorderLayout.CENTER);
		txtModelcvl.setColumns(30);
		
		JButton btnNewButton_1 = new JButton("Load");
		
		pnLoad.add(btnNewButton_1, BorderLayout.EAST);
		add(pnLoad, BorderLayout.NORTH);
		
		resolutionModel = new ResolutionModel();
		vSpecResolutionRoot = resolutionModel.getVSpecResolutionRoot(resolutionModelFileName);
		
		ResolutionNode root = getNode(vSpecResolutionRoot); 
		tree = new JTree(root);
		tree.putClientProperty("JTree.lineStyle", "Angled");

		ResolutionNodeRenderer renderer = new ResolutionNodeRenderer();
		tree.setCellRenderer(renderer); 
		expandAllNodes(tree, 0, tree.getRowCount());
		tree.addMouseListener(new ResolutionNodeSelectionListener(tree));
		
		JScrollPane scrollPane = new JScrollPane(tree);
		
		add(scrollPane, BorderLayout.CENTER);
		btnNewButton_1.addActionListener(new ActionListener() {
	        @Override
			public void actionPerformed(ActionEvent arg0) {
	        	// TODO Auto-generated method stub
	        	JFileChooser fc = new JFileChooser();
	        	File fi = new File("/home/DiskD/Dropbox/workspace/Process/model");
	            fc.setCurrentDirectory(fi);
	            
	        	int returnVal = fc.showOpenDialog(ResolutionModelGUI.this);
	        	if (returnVal == JFileChooser.APPROVE_OPTION) {
	        		File file = fc.getSelectedFile();
	        		String newFileName = file.getAbsolutePath();
	        		txtModelcvl.setText(newFileName);
	 	        	//ResolutionModelService resolutionModel = new ResolutionModel();
	 	        	vSpecResolutionRoot = resolutionModel.getVSpecResolutionRoot(newFileName);
	 	        	TreeModel treeModel = new DefaultTreeModel(getNode(vSpecResolutionRoot));
	 	        	tree.setModel(treeModel);
	 	        	expandAllNodes(tree, 0, tree.getRowCount());
	 	        	 
	 	        	 
	             } else {
	                 //log.append("Open command cancelled by user." + newline);
	             }
	        	
			}
	      });
	}
	public ArrayList<VSpecResolution> getVSpecResolutionList(){
		return resolutionModel.getVSPecResolutionList(vSpecResolutionRoot);
	}
	public ResolutionNode getNode(VSpecResolution vSpecResolutionRoot) {
		String resolved = null;
		String type = vSpecResolutionRoot.getClass().getSimpleName().substring(0, vSpecResolutionRoot.getClass().getSimpleName().length()-4);
		//System.out.println(type);
		if (vSpecResolutionRoot instanceof ChoiceResolution) {
			resolved = ""+((ChoiceResolution) vSpecResolutionRoot).isDecision();
		}else if (vSpecResolutionRoot  instanceof VariableValueAssignment) {
			resolved = ((VariableValueAssignment)vSpecResolutionRoot).getValue();
		} else {
			resolved = ""+((VInstance)vSpecResolutionRoot).getNumber();
		}
		ResolutionNode node = new ResolutionNode(vSpecResolutionRoot.getName(),type, resolved,true);
		if (vSpecResolutionRoot.getChild().size() > 0)
			for (int i=0; i<vSpecResolutionRoot.getChild().size(); i++) {
				VSpecResolution vSpecChild = vSpecResolutionRoot.getChild().get(i); 
				node.add(getNode(vSpecChild));
				//listNode.add(getNode(vSpecChild).getResolved());
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

	public static void main(String[] args) {
		JFrame fr = new JFrame();
		ResolutionModelGUI frTree = new ResolutionModelGUI("model//composite2//resolution.cvl");
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.add(frTree);
		fr.setSize(400,450);
		fr.setVisible(true);
	}
}
class ResolutionNodeSelectionListener extends MouseAdapter {
    JTree tree;
    
    ResolutionNodeSelectionListener(JTree tree) {
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
      //System.out.println(path1.toString());
    }
  }

class ResolutionNodeRenderer extends JPanel implements TreeCellRenderer {
	private JCheckBox chk_leafRenderer = new JCheckBox();
	private JTextField txt_leafRenderer = new JTextField("       ");
	private JLabel lbl_Node = new JLabel();

	public ResolutionNodeRenderer() {
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
				this.setBackground(Color.WHITE);

				lbl_Node.setText(node.getName()+":"+node.type + ":" +node.getResolved());
				chk_leafRenderer.setSelected(node.selected);
				chk_leafRenderer.setEnabled(true);
				//this.add(chk_leafRenderer);
				return this;
		} else {
				this.setBackground(Color.WHITE);

				lbl_Node.setText(node.getName()+":"+node.type+ ":" +node.getResolved());
				chk_leafRenderer.setSelected(node.selected);
				chk_leafRenderer.setEnabled(true);
				//this.add(chk_leafRenderer);
				return this;
			
		}
	}

}
@SuppressWarnings("serial")
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
@SuppressWarnings("serial")
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