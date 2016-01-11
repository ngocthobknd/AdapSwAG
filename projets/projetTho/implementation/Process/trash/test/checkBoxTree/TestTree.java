package test.checkBoxTree;



import java.awt.Color;
import java.awt.Component;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeCellRenderer;

public class TestTree {
	
	public JTree tree;
	JSplitPane splitPane;
	public TestTree(){
		
		ImageIcon iconsch=new ImageIcon(TestTree.class.getResource("..\\graphic\\icon\\computer.gif"));
		DefaultMutableTreeNode root= new DefaultMutableTreeNode(new NodeData(iconsch,true,"RootFeature"));
		tree = new JTree(root);
		
		
		
		int i = 0;
		ImageIcon icon = new ImageIcon(TestTree.class.getResource("..\\graphic\\icon\\group.gif"));
		
		//DefaultMutableTreeNode vpNode = new DefaultMutableTreeNode(new IconData(icon,i,"Test"));
		DefaultMutableTreeNode vpNode = new DefaultMutableTreeNode(new NodeData(icon,true,"Test"));
		// add variation point to tree root
		
		root.add(vpNode);
	
		
		
		
		
				
		// action to expand and collapse
		tree.addTreeWillExpandListener(new TreeWillExpandListener()	{
			public void treeWillExpand(TreeExpansionEvent e)  throws ExpandVetoException { 
				
			}
			public void treeWillCollapse(TreeExpansionEvent e)	{
				
			}
		});

		
		tree.putClientProperty("JTree.lineStyle", "Angled");
		IconCellRenderer renderer = new	IconCellRenderer();
		// renderer.setLeafIcon(new ImageIcon("help.png"));
    	tree.setCellRenderer(renderer); 
    	tree.setShowsRootHandles(true);
		
    	for (int j = 0; j < tree.getRowCount(); j++) {
            tree.expandRow(j);
    	}
    	
    	//CheckBoxNodeRenderer renderer1 = new CheckBoxNodeRenderer();
    	//tree.setCellRenderer(renderer1);
    	tree.setEditable(false);
    	
    	
	}
	
	public static void main(String arg[]) {
		
		
		
		JFrame fr = new JFrame();
		
		TestTree  vtyGUI= new TestTree();
		fr.add(vtyGUI.tree);
		fr.setSize(400,450);
		fr.setVisible(true);
		
	}
}

class IconCellRenderer extends DefaultTreeCellRenderer implements TreeCellRenderer {

	/**
	 * 
	 */

	private static final long serialVersionUID = -457613821273985841L;
	JCheckBox jchBox = new JCheckBox();
	public IconCellRenderer() {
		// constructor
		
	}
	protected JCheckBox getLeafRenderer() {
		return jchBox;
	}
	
	public Component getTreeCellRendererComponent(JTree tree, 
												Object value, 
												boolean selected, 
												boolean expanded, 
												boolean leaf,
												int row, 
												boolean hasFocus) {
		// Invoke default implementation
		Component result = super.getTreeCellRendererComponent(tree,
			value, selected, expanded, leaf, row, hasFocus);
	
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
		Object obj = node.getUserObject();
		//setText(obj.toString());
	   if (obj instanceof NodeData) {
			NodeData idata = (NodeData)obj;
			setIcon(idata.getIcon());
			//jchBox.setSelected(idata.isSelected());
			
		}
		if (node.isRoot())
					setBackgroundSelectionColor(Color.red);
				else if (node.getChildCount() > 0)
					setBackgroundSelectionColor(Color.blue);
				else if (leaf)
					setBackgroundSelectionColor(Color.green);
		return result;
	}
}
class NodeData {
	protected Icon   m_icon;
	protected boolean   selected;
	protected Object m_data;
	protected int    m_id;

	public NodeData(Icon icon, boolean expandedIcon, String data)	{
		m_icon = icon;
		selected = expandedIcon;
		m_data = data;
	}
	public int getId() {
		return m_id;
	}
	public Icon getIcon() { 
		return m_icon;
	}

	
	public Object getObject()	{ 
		return m_data;
  	}

	public String toString() { 
		return m_data.toString();
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean newValue) {
		selected = newValue;
	}
}

