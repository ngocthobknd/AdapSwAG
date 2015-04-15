package test.main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.ReadModel;
import graphic.*;
public class GUI {
	public TreeContainer frTree;
	public static JFrame fr;
	String str_file="My.variability";
	public GUI() {
		fr = new JFrame();
		frTree = new TreeContainer();
		fr.add(frTree);
		fr.setSize(400,450);
		fr.setVisible(true);
	}
	public static void main(String [] arg){
		new GUI();
	}
}