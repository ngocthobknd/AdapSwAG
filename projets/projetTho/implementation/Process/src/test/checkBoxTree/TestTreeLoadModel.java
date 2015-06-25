package test.checkBoxTree;

import javax.swing.JFrame;
import javax.swing.UIManager;

import test.FeaturesTree;
import model.ReadModel;

public class TestTreeLoadModel {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 try {
		        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		 } catch (Exception evt) {}
		 
		ReadModel rd = new ReadModel("");
		JFrame fr = new JFrame();
		
		//FeaturesTree  vtyGUI= new FeaturesTree();
		//fr.add(vtyGUI);
		fr.setSize(200,250);
		fr.setVisible(true);
		
	}

}
