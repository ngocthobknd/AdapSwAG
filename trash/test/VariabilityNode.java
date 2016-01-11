package test;

import javax.swing.tree.DefaultMutableTreeNode;

public class VariabilityNode extends DefaultMutableTreeNode{
	
	String text;
	boolean selected;
	
	public VariabilityNode(String text) {
		this.text = text;
	}
	public VariabilityNode(String text, boolean selected) {
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