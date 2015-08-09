package test;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class MainFrame extends JPanel{
	private JTextArea textArea;
	private JButton btn;   
	public MainFrame() {
		btn = new JButton("Click Me!");
		textArea = new JTextArea();
        setLayout(new BorderLayout());
        add(btn, BorderLayout.SOUTH);
		add(new JScrollPane(textArea), BorderLayout.CENTER);
   }
   public static void main(String arg[]){
	   	JFrame fr =new JFrame("Hello World");
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.setLayout(new BorderLayout());
		MainFrame textPanel;
        
      
       	textPanel = new MainFrame();
      
   
       	fr.add(textPanel, BorderLayout.CENTER);
       	fr.setVisible(true);	
		fr.setSize(600, 500);
       	
       	
   }
}
