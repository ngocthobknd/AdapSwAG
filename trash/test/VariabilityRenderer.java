package test;


import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

public class VariabilityRenderer implements TreeCellRenderer {

	private JCheckBox leafRenderer = new JCheckBox();
	private JLabel label = new JLabel();
	protected JCheckBox getLeafRenderer() {
		return leafRenderer;
	}
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		// TODO Auto-generated method stub
		VariabilityNode node = (VariabilityNode)value;
		leafRenderer.setText(node.getText());
		leafRenderer.setSelected(node.isSelected());
		
		return leafRenderer;
	}
	

}
