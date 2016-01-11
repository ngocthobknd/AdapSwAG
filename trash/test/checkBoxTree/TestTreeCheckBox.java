package test.checkBoxTree;



import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EventObject;



import javax.swing.AbstractCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.UIManager;


import javax.swing.event.ChangeEvent;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;

import javax.swing.tree.DefaultTreeModel;

import javax.swing.tree.TreeCellRenderer;

import test.checkBoxTree.CheckNodeTreeExample.NodeSelectionListener;










public class TestTreeCheckBox {
	JTree tree;
	JSplitPane splitPane;
	public TestTreeCheckBox(){
		JCheckBox jchBox = new JCheckBox();
		jchBox = null;
	

		Icon iconsch=new ImageIcon(TestTreeCheckBox.class.getResource("..\\graphic\\icon\\computer.gif"));
		JLabel lb = new JLabel("root", iconsch, SwingConstants.RIGHT);
		DefaultMutableTreeNode root=  new DefaultMutableTreeNode(new TreeNode(lb,jchBox,false));
		
		
		
		
		int i = 0;
		Icon icon = new ImageIcon(TestTreeCheckBox.class.getResource("..\\graphic\\icon\\group.gif"));
		JLabel lb1 = new JLabel("leaf1", icon, SwingConstants.RIGHT);

		JCheckBox jchBox1 = new JCheckBox();
		jchBox1.setEnabled(true);
		DefaultMutableTreeNode vpNode =  new DefaultMutableTreeNode(new TreeNode(lb1,jchBox1, false));
		DefaultMutableTreeNode vpNode5 =  new DefaultMutableTreeNode(new TreeNode(lb1,jchBox1, false));
		// add variation point to tree root
		
		
		
	
		Icon icon2 = new ImageIcon(TestTreeCheckBox.class.getResource("..\\graphic\\icon\\group.gif"));
		JLabel lb2 = new JLabel("leaf2", icon2, SwingConstants.RIGHT);
		JCheckBox jchBox2 = new JCheckBox();
		DefaultMutableTreeNode vpNode2 = new DefaultMutableTreeNode(new TreeNode(lb2,null, false));
		// add variation point to tree root
		Icon icon3 = new ImageIcon(TestTreeCheckBox.class.getResource("..\\graphic\\icon\\group.gif"));
		JLabel lb3 = new JLabel("leaf3", icon3, SwingConstants.RIGHT);
		JCheckBox jchBox3 = new JCheckBox();
		DefaultMutableTreeNode vpNode3 = new DefaultMutableTreeNode(new TreeNode(lb3,jchBox3, true));
		DefaultMutableTreeNode vpNode4 = new DefaultMutableTreeNode(new TreeNode(lb3,jchBox3, true));

		vpNode2.add(vpNode);
		vpNode2.add(vpNode5);
		root.add(vpNode2);
		root.add(vpNode3);
		root.add(vpNode4);
		
		DefaultTreeModel model = new DefaultTreeModel(root);
		tree = new JTree(model);
				
		// action to expand and collapse
		
		tree.addTreeWillExpandListener(new TreeWillExpandListener()	{
			public void treeWillExpand(TreeExpansionEvent e)  throws ExpandVetoException { 
				
			}
			public void treeWillCollapse(TreeExpansionEvent e)	{
				
			}
		});

	          
		TreeNodeCellRenderer1 renderer1 = new TreeNodeCellRenderer1();
    	tree.setCellRenderer(renderer1); 
        tree.putClientProperty("JTree.lineStyle", "Angled");

 //       tree.addMouseListener(new NodeSelectionListener1(tree));


        tree.setCellEditor(new CheckNodeEditor(tree));
        tree.setEditable(true);

        
    	JFrame fr = new JFrame();
		
		fr.add(tree);
		fr.setSize(400,450);
		fr.setVisible(true);
		
    }
	
	public static void main(String arg[]) {
		
		new TestTreeCheckBox();
		
	}
}
/*class NodeSelectionListener1 extends MouseAdapter {
    JTree tree;
    
    NodeSelectionListener1(JTree tree) {
      this.tree = tree;
    }
    
    public void mouseClicked(MouseEvent e) {
      int x = e.getX();
      int y = e.getY();
      int row = tree.getRowForLocation(x, y);
      System.out.println(row);
      
      TreePath  path = tree.getPathForRow(row);
      //TreePath  path = tree.getSelectionPath();
      System.out.println(path.toString());
    		  
      if (path != null) {
    	System.out.println(row+1);
    	Object node = (Object)path.getLastPathComponent();
    	
    	
    	
    	boolean isSelected = ! (node.isSelected());
        node.setSelected(isSelected);
        if ( isSelected) {
        	tree.expandPath(path);
	    } else {
	        tree.collapsePath(path);
	    }
        System.out.println(node.getJLabel().getText());
        
        // I need revalidate if node is root.  but why?
        if (row == 0) {
          tree.revalidate();
          tree.repaint();
        }
      }
    }
  }
*/



class TreeNodeCellRenderer1 implements TreeCellRenderer {
	TreeNode pTreeNode; int i = 0;
    JLabel label;
    JCheckBox checkBox;
    boolean boo;
    Color selectionBorderColor, selectionForeground, selectionBackground,
    textForeground, textBackground;
    
	public TreeNodeCellRenderer1(){
		 pTreeNode = new TreeNode();
		 pTreeNode.setLayout(new FlowLayout());
		 selectionBorderColor = UIManager.getColor("Tree.selectionBorderColor");
		    selectionForeground = UIManager.getColor("Tree.selectionForeground");
		    selectionBackground = UIManager.getColor("Tree.selectionBackground");
		    textForeground = UIManager.getColor("Tree.textForeground");
		    textBackground = UIManager.getColor("Tree.textBackground");
	}
	 protected TreeNode getLeafRenderer() {
		    return pTreeNode;
		  }
	public Component getTreeCellRendererComponent(JTree tree,
										            Object value,
										            boolean selected,
										            boolean expanded,
										            boolean leaf,
										            int row,
										            boolean hasFocus) 
	{
		
		System.out.println("getTreeCellRendererComponent");
		pTreeNode.removeAll();
		pTreeNode.setBackground(null);
		//pTreeNode.setSelected(false);
		pTreeNode.setEnabled(tree.isEditable());
		if(selected){
			pTreeNode.setBackground(selectionBackground);
			//pTreeNode.setForeground(selectionForeground);
			
		}
		/*DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
		TreeNode icondata1 = (TreeNode)node.getUserObject();
		
		if (icondata1.getjCheckBox() != null) {
			
			icondata1.getjCheckBox().setSelected(icondata1.getSelected());
			panel.setSelected(icondata1.getSelected());
			
			panel.add(icondata1.getjCheckBox());
			panel.setjCheckBox(icondata1.getjCheckBox());
		}*/
		   
		/*panel.add(icondata1.getjLabel());
		panel.setjLabel(icondata1.getjLabel());*/
		
		if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
	        Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
	        if (userObject instanceof TreeNode) {
	        	TreeNode node1 = (TreeNode) userObject;
	        	if (node1.getjCheckBox() != null) {
	        		checkBox = node1.getjCheckBox();
	        		
	        		checkBox.setSelected(node1.getSelected());
					pTreeNode.setSelected(node1.getSelected());
					
					
		        	pTreeNode.add(checkBox);
		        	pTreeNode.setjCheckBox(node1.getjCheckBox());
	        	}
	        	pTreeNode.add(node1.getjLabel());
	    		pTreeNode.setjLabel(node1.getjLabel());
	        }
	        
		}
		
		
		return (TreeNode)pTreeNode;
			
	}
	
}
class CheckNodeEditor extends AbstractCellEditor implements TreeCellEditor{
	TreeNodeCellRenderer1 renderer = new TreeNodeCellRenderer1();
	ChangeEvent changEvent =  null;
	JTree tree;
	TreeNode pn ;
	public CheckNodeEditor(JTree tree) {
		// TODO Auto-generated constructor stub
		this.tree = tree;
	}
	@Override
	public Object getCellEditorValue() {
		// TODO Auto-generated method stub
		pn= renderer.getLeafRenderer();
		TreeNode ic = new TreeNode(pn.getjLabel(),pn.getjCheckBox(),pn.getSelected());
	    System.out.println("getCellEditorValue");

		return ic;
	}
	public boolean isCellEditable(EventObject event) {
	    boolean returnValue = false;
	    if (event instanceof MouseEvent) {
	        MouseEvent mouseEvent = (MouseEvent) event;
	        TreePath path = tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
	        if (path != null) {
	          Object node = path.getLastPathComponent();
	          if ((node != null) && (node instanceof DefaultMutableTreeNode)) {
	            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
	            Object userObject = treeNode.getUserObject();
	            returnValue = ((treeNode.isLeaf()) && (userObject instanceof TreeNode));
	          }
	        }
	      }

	      return returnValue;
	}

	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean isSelected, boolean expanded, boolean leaf, int row) {
		// TODO Auto-generated method stub
		Component editor = (TreeNode)renderer.getTreeCellRendererComponent(tree, value,
		        true, expanded, leaf, row, true);
			
		ItemListener itemListener = new ItemListener() {
		        public void itemStateChanged(ItemEvent itemEvent) {
		          
		        	System.out.println("Chon");
		        	//System.out.println(pn.getjLabel().getText()+"Item");
		       //   if (pn.getSelected() == false)
		        	  pn.setSelected(true);
		        //  else pn.setSelected(false);
		        	System.out.println(pn.getjLabel().getText());
		        }
		     };
		    if (editor instanceof JPanel) {
		    	(((TreeNode) editor).getjCheckBox()).addItemListener(itemListener);
		      
		    	((JPanel) editor).revalidate();
		    }
		    
		    return editor;
		 
	}
	
}
class TreeNode extends JPanel {
	JLabel   jLabel;
	JCheckBox jCheckBox;
	boolean selected;
	
	public TreeNode(){
		
	}
	public TreeNode(JLabel jLabel, JCheckBox jCheckBox, boolean selected)	{
	
		this.jLabel = jLabel;
		this.jCheckBox = jCheckBox;
		this.selected = selected;
		
	}
	
		
	public JLabel getjLabel() {
		return jLabel;
	}

	public void setjLabel(JLabel jLabel) {
		this.jLabel = jLabel;
	}

	public JCheckBox getjCheckBox() {
		return jCheckBox;
	}

	public void setjCheckBox(JCheckBox jCheckBox) {
		this.jCheckBox = jCheckBox;
	}

	
	public boolean getSelected() {
		return selected;
	}
	public void setSelected(boolean select) {
		selected = select;
	}
	 public boolean isSelected() {
		    return selected;
	}
	
}


