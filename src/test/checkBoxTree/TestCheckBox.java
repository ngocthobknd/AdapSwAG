package test.checkBoxTree;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class TestCheckBox {

	/**
	 * @param args
	 */
	JCheckBox ch;
	JPanel pn;
	public TestCheckBox(){
		pn = new JPanel();
		
		ImageIcon igc = new ImageIcon(TestCheckBox.class.getResource("..\\graphic\\icon\\group.gif"));
		ch = new JCheckBox();
		pn.add(ch);
		pn.add(new JLabel("Leaf",igc,SwingConstants.LEFT));
		
	
		
		
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame fr = new JFrame();
		fr.setSize(200, 300);
		TestCheckBox h = new TestCheckBox();
		fr.add(h.pn);
		fr.setVisible(true);
		
		
		

	}

}
