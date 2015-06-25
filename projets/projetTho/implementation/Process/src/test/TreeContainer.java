package test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import model.WriteModel;
import utils.StringHandle;

public class TreeContainer extends JPanel {

	public static JTextField loadTextField;
	FeaturesTree frTree;
	List selectedVariants = new ArrayList<String>();
	String inputFile;
	public TreeContainer(){
		JPanel  loadPanel = new JPanel();
		loadPanel.setLayout(new BorderLayout());
		JLabel jlbLoad = new JLabel("Varibility Model ");
		loadPanel.add(jlbLoad,BorderLayout.WEST);
		loadTextField = new JTextField("My.variability");
		loadPanel.add(loadTextField, BorderLayout.CENTER);
		JButton btLoadModel = new JButton("Load");
		loadPanel.add(btLoadModel, BorderLayout.EAST);
		btLoadModel.addActionListener((new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent arg0) {
		    	remove(frTree);
		    	frTree = new FeaturesTree(loadTextField.getText());
		    	add(frTree, BorderLayout.CENTER);
		    	revalidate();
		    }
		}));

		inputFile = loadTextField.getText();
		JPanel jPanel = new JPanel();
		JButton jbtConfigure = new JButton("Configure models");
		jPanel.add(jbtConfigure);
		JButton jbtCheck = new JButton("Check consistency");
		jPanel.add(jbtConfigure);
		jPanel.add(jbtCheck);
		
		jbtConfigure.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent arg0) {
		        TreeModel model = (TreeModel) frTree.tree.getModel();
		        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		        TreeNode subroot = root.getChildAt(0);
		       // CheckBoxNode root = (CheckBoxNode) model.getRoot();
		       
		        System.out.println(subroot.toString());
		       Enumeration e = subroot.children();
		       while (e.hasMoreElements()) {
		    	   TreeNode treenode = (TreeNode)e.nextElement();
		    	   System.out.println(treenode.toString());
		    	   Enumeration e1 = treenode.children();
		    	   while (e1.hasMoreElements()) {
		    		   TreeNode subnode = (TreeNode)e1.nextElement();
		    		   String str[] = StringHandle.separateString(subnode.toString());
		    		   if (str[1].equals("true")) selectedVariants.add(str[0]);
		    	   }
		       }
		       WriteModel wModel = new WriteModel(inputFile, TreeContainer.loadTextField.getText(), selectedVariants);
		    }
		});
	
		
		setLayout(new BorderLayout());
		add(jPanel, BorderLayout.SOUTH);
		add(loadPanel, BorderLayout.NORTH);
		frTree = new FeaturesTree(loadTextField.getText());
		add(frTree, BorderLayout.CENTER);
	}
	

}
