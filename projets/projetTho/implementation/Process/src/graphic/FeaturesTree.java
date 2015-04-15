package graphic;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractCellEditor;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import model.ReadModel;
import model.WriteModel;

import org.eclipse.emf.common.util.EList;

import test.main.GUI;
import utils.StringHandle;

import eu.telecombretagne.variability.Association;
import eu.telecombretagne.variability.DynamicVariationPoint;
import eu.telecombretagne.variability.StaticVariationPoint;
import eu.telecombretagne.variability.Variability;
import eu.telecombretagne.variability.Variant;
import eu.telecombretagne.variability.VariationPoint;

public class FeaturesTree extends JPanel{
	public JTree tree;
	JSplitPane splitPane;
	JLabel label;
	JCheckBox jchBox = new JCheckBox();
	JTextField  loadTextField;
	//list selected variants
	List <String> selectedVariants = new ArrayList<String>();
	String file;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public FeaturesTree(String inputFile) {
		
    	/* 
    	 * init an object that play the role to read a model file.
    	 * thanks to this object. We can return variability EClass that contains variation points and variants.  
    	 */
		
		ReadModel rd = new ReadModel(inputFile);
		Variability variability = rd.variability;
		EList <VariationPoint> variationPoints = variability.getVariationPoints();
		//int i = 0;
		Vector v_variationPoints = new NamedVector("Variability Model");
		for (VariationPoint vp : variationPoints) {
			EList <Association> associations = vp.getAssociationType();
			// get association type name
			String vpType = associations.get(0).eClass().getName();
			Vector v_variants = new Vector();
			for (Association ass : associations) {
				// get variant of variation point
				EList <Variant> variants = ass.getVariants();
				for (Variant v : variants) {
					if (v.getName() != null){
						CheckBoxNode chk = new CheckBoxNode(v.getName(), false);
						v_variants.addElement(chk);
					}
				}				
			}
			Vector va = new NamedVector(vp.getName()+"/"+vpType,v_variants.toArray());
			if (vp instanceof DynamicVariationPoint) 
				va = new NamedVector(vp.getName()+"(dynamic)"+"/"+vpType,v_variants.toArray());
			if (vp instanceof StaticVariationPoint) 
				va = new NamedVector(vp.getName()+"(static)"+"/"+vpType,v_variants.toArray());
			
			v_variationPoints.addElement(va);
		}
		Object root[] = {v_variationPoints}; //root contains all variation points of variability model
		
		/*
		 * use a JTree to represent variability model by GUI
		 * each leaf node of tree is a CheckBox 
		 * and a variation point is a JLabel  
		 */
		tree = new JTree(root);
		// action to expand and collapse
		tree.addTreeWillExpandListener(new TreeWillExpandListener()	{
			public void treeWillExpand(TreeExpansionEvent e)  throws ExpandVetoException { 
				
			}
			public void treeWillCollapse(TreeExpansionEvent e)	{
				
			}
		});
		tree.putClientProperty("JTree.lineStyle", "Angled");
		CheckBoxNodeRenderer renderer = new CheckBoxNodeRenderer();
		tree.setCellRenderer(renderer); 
    	//tree.setShowsRootHandles(true);
        tree.setCellEditor(new CheckBoxNodeEditor(tree));
        tree.setEditable(true);
    	for (int j = 0; j < tree.getRowCount(); j++) {
            tree.expandRow(j);
    	}
        JScrollPane scrollPane = new JScrollPane(tree);
    	setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
 	}
	
	public static void main(String arg[]) {
		JFrame fr = new JFrame();
		// add panel which contains tree to frame
		FeaturesTree frTree = new FeaturesTree("");
		fr.add(frTree);
		fr.setSize(400,450);
		fr.setVisible(true);
	}
}
class CheckBoxNodeRenderer implements TreeCellRenderer {
	private JCheckBox leafRenderer = new JCheckBox();
	private JLabel label = new JLabel();
	private DefaultTreeCellRenderer nonLeafRenderer = new DefaultTreeCellRenderer();

	Color selectionBorderColor, selectionForeground, selectionBackground, textForeground, textBackground;

	protected JCheckBox getLeafRenderer() {
		return leafRenderer;
	}
	public CheckBoxNodeRenderer() {
		Font fontValue;
		fontValue = UIManager.getFont("Tree.font");
		if (fontValue != null) {
				leafRenderer.setFont(fontValue);
		}

		selectionBorderColor = UIManager.getColor("Tree.selectionBorderColor");
		selectionForeground = UIManager.getColor("Tree.selectionForeground");
		selectionBackground = UIManager.getColor("Tree.selectionBackground");
		textForeground = UIManager.getColor("Tree.textForeground");
		textBackground = UIManager.getColor("Tree.textBackground");
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, 
									boolean expanded, boolean leaf, int row, boolean hasFocus) {

		Component returnValue;
		String stringValue = tree.convertValueToText(value, selected, expanded, leaf, row, false);
		if (leaf && (!stringValue.equals(null)) ) {
			leafRenderer.setText(stringValue);
			leafRenderer.setSelected(false);
			leafRenderer.setEnabled(tree.isEnabled());
	
			if (selected) {
				leafRenderer.setForeground(selectionForeground);
				leafRenderer.setBackground(selectionBackground);
			} else {
				leafRenderer.setForeground(textForeground);
				leafRenderer.setBackground(textBackground);
			}
	
			if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
				Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
				
				if ((userObject instanceof CheckBoxNode)) {
					
					CheckBoxNode node = (CheckBoxNode) userObject;
					leafRenderer.setText(node.getText());
					leafRenderer.setSelected(node.isSelected());
				}
			}
			returnValue = leafRenderer;
		} else {
			label.setText(stringValue);
			if (row == 0){
				label.setIcon(new ImageIcon(FeaturesTree.class.getResource("..\\icons\\computer.gif")));
			}  else label.setIcon(new ImageIcon(FeaturesTree.class.getResource("..\\icons\\group.gif")));
			returnValue = label;      
		}
		return returnValue;
	}
}
class CheckBoxNodeEditor extends AbstractCellEditor implements TreeCellEditor {
	CheckBoxNodeRenderer renderer = new CheckBoxNodeRenderer();
	ChangeEvent changeEvent = null;
	JTree tree;
	public CheckBoxNodeEditor(JTree tree) {
		this.tree = tree;
	}
	public Object getCellEditorValue() {
		JCheckBox checkbox = renderer.getLeafRenderer();
		CheckBoxNode checkBoxNode = new CheckBoxNode(checkbox.getText(),checkbox.isSelected());
		System.out.println(checkBoxNode.getText());
		return checkBoxNode;
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
					returnValue = ((treeNode.isLeaf()) && (userObject instanceof CheckBoxNode));
				}
			}
		}
		
		return returnValue;
	}
 
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row) {
		Component editor = renderer.getTreeCellRendererComponent(tree, value, true, expanded, leaf, row, true);
		// editor always selected / focused
		ItemListener itemListener = new ItemListener() {
			public void itemStateChanged(ItemEvent itemEvent) {
				if (stopCellEditing()) {
					fireEditingStopped();
				}
			}
		};
		if (editor instanceof JCheckBox) {
			((JCheckBox) editor).addItemListener(itemListener);
		}
		return editor;
	}
}

class CheckBoxNode extends DefaultMutableTreeNode{
	String text;
	boolean selected;
	public CheckBoxNode(String text, boolean selected) {
		this.text = text;
		this.selected = selected;
	}

	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean newValue) {
		selected = newValue;
	}
	public String getText() {
		return text;
	}
	public void setText(String newValue) {
		text = newValue;
	}

	public String toString() {
		return getClass().getName() + "[" + text + "/" + selected + "]";
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