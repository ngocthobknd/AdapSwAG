package test;



import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreeNode;



public class Test {
   JTree tree;
   VariabilityNode a = null; 
   VariabilityNode b = null; 
   public Test(){
      
       Frame fr = new Frame();
	   a = new VariabilityNode("Variability");
	   b = new VariabilityNode("b", true);
	   a.add(b);
	   a.add(new VariabilityNode("c", false));
	   tree = new JTree(a);
	   fr.add(tree);
	   fr.setSize(300,300);
	   fr.setVisible(true);
   }
   public static void main(String[] args) {
       new Test();
      
      
   }

}