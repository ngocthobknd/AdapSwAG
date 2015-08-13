package base.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.ow2.fractal.f4e.fractal.Binding;
import org.ow2.fractal.f4e.fractal.Component;
import org.ow2.fractal.f4e.fractal.Definition;

import ACME.Attachment;
import ACME.ComponentInstance;
import ACME.Connector;
import base.acme.implement.ACMEImpl;
import base.api.BaseArchitectureService;
import base.fractalADL.implement.FractalADLImpl;

@SuppressWarnings("serial")
public class BaseArchitectureGUI extends JPanel{

	/**
	 * 
	 */
	public JTextArea edit;
	public JTextField txtModelcvl;
	
	public BaseArchitectureService baseArchitecture;
	private Definition definition;
	public String baseModelFileName;
	
	private ACME.System system;
	
	public BaseArchitectureGUI(String file) {
		// TODO Auto-generated constructor stub
		this.baseModelFileName = file;
		setLayout(new BorderLayout());
		
		setLayout(new BorderLayout());
		JPanel pnLoad = new JPanel();
		pnLoad.setLayout(new BorderLayout());
		
		JLabel lblNewLabel = new JLabel("File");
		pnLoad.add(lblNewLabel, BorderLayout.WEST);
		
		txtModelcvl = new JTextField();
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
	            
	        	int returnVal = fc.showOpenDialog(BaseArchitectureGUI.this);
	        	if (returnVal == JFileChooser.APPROVE_OPTION) {
	        		File file = fc.getSelectedFile();
	        		String newFileName = file.getAbsolutePath();
	        		txtModelcvl.setText(newFileName);
	        		baseModelFileName = newFileName;
	        		String ext = newFileName.substring(newFileName.lastIndexOf(".")+1);
	        		if (ext.equals("fractal")) {
	        			baseArchitecture = new FractalADLImpl();
	        			definition = baseArchitecture.getArchitectureDefinition(baseModelFileName);
	        		} else if (ext.equals("acme")) {
	        			baseArchitecture = new ACMEImpl();
	        			system = baseArchitecture.getACMESystem(baseModelFileName);
	        		}
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
		String ext = baseModelFileName.substring(baseModelFileName.lastIndexOf(".")+1);
		if (ext.equals("fractal")) {
			baseArchitecture = new FractalADLImpl();
			definition = baseArchitecture.getArchitectureDefinition(baseModelFileName);
		} else if (ext.equals("acme")) {
			baseArchitecture = new ACMEImpl();
			system = baseArchitecture.getACMESystem(baseModelFileName);
		}
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
	
	public String getFileName() {
		return this.baseModelFileName;
	}
	//for Fractal ADL
	public Definition getDefinition() {
		return this.definition;				
	}
//	public Definition getArchitectureDefinition() {
//		// TODO Auto-generated method stub
//		return baseArchitecture.getArchitectureDefinition(baseModelFileName);
//	}
	public ArrayList<Component> getComponentList(Definition definition) {
		// TODO Auto-generated method stub
		return baseArchitecture.getComponentList(definition);
	}
	public ArrayList<Component> getParentComponentList(Definition definition) {
		// TODO Auto-generated method stub
		return baseArchitecture.getParentComponentList(definition);
	}
	public ArrayList<Binding> getBindingList(Definition definition) {
		// TODO Auto-generated method stub
		return baseArchitecture.getBindingList(definition);
	}
	public ArrayList<Binding> getParentBindingList(Definition definition) {
		// TODO Auto-generated method stub
		return baseArchitecture.getParentBindingList(definition);
	}
	
	//for ACME
	public ACME.System getSystem() {
		return this.system;
	}
	public ACME.System getACMESystem() {
		return baseArchitecture.getACMESystem(baseModelFileName);
	}
	public ArrayList<ComponentInstance> getParentComponentList(ACME.System sys) {
		return baseArchitecture.getParentComponentList(system);
	}
	public ArrayList<Connector> getParentConnectorList(ACME.System sys) {
		return baseArchitecture.getParentConnectorList(sys);
	}
	public ArrayList<Attachment> getParentAttchmentList(ACME.System sys) {
		return baseArchitecture.getParentAttchmentList(sys);
				
	}
	public ArrayList<ACME.Binding> getParentBindingList(ACME.System sys) {
		return baseArchitecture.getParentBindingList(sys);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		JFrame frame = new JFrame("Moving");
		BaseArchitectureGUI m = new BaseArchitectureGUI("model//composite2//architecture.fractal");
		//		m.setDoubleBuffered(true);
		frame.add(m);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
}