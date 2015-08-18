package product.gui;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import base.api.BaseArchitectureService;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.JLabel;
import javax.swing.JButton;

import toJava.fractal2Java.JavaGeneration;

public class ProductArchitectureGUI extends JPanel {

	public JTextArea edit;
	public JTextField txtModelcvl;
	
	public BaseArchitectureService baseArchitecture;
	public String baseModelFileName;
	private JTextField textField;
	/**
	 * Create the panel.
	 */
	public ProductArchitectureGUI(String file) {
		setLayout(new BorderLayout());
		
		setLayout(new BorderLayout());
		JPanel pnLoad = new JPanel();
		pnLoad.setLayout(new BorderLayout());
		
		JLabel lblNewLabel = new JLabel("File");
		pnLoad.add(lblNewLabel, BorderLayout.WEST);
		
		txtModelcvl = new JTextField();
		String tm = file.replaceAll("//", "/");
		txtModelcvl.setText(tm);
		txtModelcvl.setText("architecture.fractal");
		pnLoad.add(txtModelcvl, BorderLayout.CENTER);
		//txtModelcvl.setColumns();
		JButton btnNewButton_1 = new JButton("Load");
		pnLoad.add(btnNewButton_1, BorderLayout.EAST);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
	        	File fi = new File("/home/DiskD/Dropbox/workspace/Process/model");
	            fc.setCurrentDirectory(fi);
	            
	        	int returnVal = fc.showOpenDialog(ProductArchitectureGUI.this);
	        	if (returnVal == JFileChooser.APPROVE_OPTION) {
	        		File file = fc.getSelectedFile();
	        		String newFileName = file.getAbsolutePath();
	        		txtModelcvl.setText(newFileName);
	        		baseModelFileName = newFileName;
//	        		String ext = newFileName.substring(newFileName.lastIndexOf(".")+1);
//	        		if (ext.equals("fractal")) {
//	        			baseArchitecture = new FractalADLImpl();
//	        			definition = baseArchitecture.getArchitectureDefinition(baseModelFileName);
//	        		} else if (ext.equals("acme")) {
//	        			baseArchitecture = new ACMEImpl();
//	        			system = baseArchitecture.getACMESystem(baseModelFileName);
//	        		}
	        		setText(newFileName);
	             } else {
	                 //log.append("Open command cancelled by user." + newline);
	             }
			}
			
		});
		add(pnLoad, BorderLayout.NORTH);
		edit = new JTextArea();
		setText(file);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(edit);
		add(scrollPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("To Java Code");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int mc = JOptionPane.WARNING_MESSAGE;
				
				String fileName = txtModelcvl.getText().replaceAll("/", "//");
				//
				System.out.println(fileName);
				new JavaGeneration(fileName);
				JOptionPane.showMessageDialog (null, "Generate Java code",
						"Verification", mc);
			}
		});
		panel.add(btnNewButton);

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
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		JFrame frame = new JFrame("Moving");
		ProductArchitectureGUI m = new ProductArchitectureGUI("model//composite2//architecture.fractal");
		//		m.setDoubleBuffered(true);
		frame.getContentPane().add(m);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
