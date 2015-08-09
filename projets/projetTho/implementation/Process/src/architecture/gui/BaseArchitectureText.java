package architecture.gui;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.ow2.fractal.f4e.fractal.Binding;
import org.ow2.fractal.f4e.fractal.Component;

@SuppressWarnings("serial")
public class BaseArchitectureText extends JPanel {

	/**
	 * 
	 */
	public JTextArea edit;
	
	ArrayList<Component> sourceComponentList = new ArrayList<Component>();
	ArrayList<Binding> sourcebindingList = new ArrayList<Binding>();
	
	String baseModelFileName;
	public BaseArchitectureText(String file) {
		// TODO Auto-generated constructor stub
		setLayout(new BorderLayout());
		edit = new JTextArea();
		setText(file);
        add(edit, BorderLayout.CENTER);
	}
	public void setText(String baseArchitectureFileName) {
		try
        {
			 FileReader reader = new FileReader(baseArchitectureFileName);
             BufferedReader br = new BufferedReader(reader);
             edit.read( br, null );
             br.close();
             edit.requestFocus();
        }
        catch(Exception e2) {}
 	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		JFrame frame = new JFrame("Moving");
		BaseArchitectureText m = new BaseArchitectureText("model//composite2//architecture.fractal");
	
		//		m.setDoubleBuffered(true);
		JScrollPane scrollPane = new JScrollPane(m);
		frame.add(scrollPane);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}