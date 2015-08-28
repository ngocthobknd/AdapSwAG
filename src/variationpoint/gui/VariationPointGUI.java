package variationpoint.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import variationpoint.api.VariationPointService;
import variationpoint.implement.VariationPointImpl;
import cvl.ObjectExistence;
import cvl.ObjectHandle;
import cvl.ObjectSubstitution;
import cvl.ParametricSlotAssignment;
import cvl.VPackage;
import cvl.VariationPoint;

public class VariationPointGUI extends JPanel {

	private String variabilityModelFileName;
	@SuppressWarnings("rawtypes")
	private JList list;
	
	private VariationPointService vty;
	private VPackage vPackage;
	public VariationPointGUI(String VPFileName) {
		// TODO Auto-generated constructor stub
		// TODO Auto-generated constructor stub
				//this.newFileName = cvlFileName;
				this.variabilityModelFileName = VPFileName;
				setLayout(new BorderLayout());
				JPanel pnLoad = new JPanel();
				pnLoad.setLayout(new BorderLayout());
				
				JLabel lblNewLabel = new JLabel("Variation Point File");
				pnLoad.add(lblNewLabel, BorderLayout.WEST);
				
				final JTextField txtModelcvl = new JTextField();
				String tm = VPFileName.replaceAll("//", "/");
				txtModelcvl.setText(tm);
				pnLoad.add(txtModelcvl, BorderLayout.CENTER);
				txtModelcvl.setColumns(30);
				
				JButton btnNewButton_1 = new JButton("Load");
				
				pnLoad.add(btnNewButton_1, BorderLayout.EAST);
				
				add(pnLoad, BorderLayout.NORTH);
				
				//List VP in GUI
				vty = new VariationPointImpl();
				vPackage = vty.getVPackage(VPFileName);
				ArrayList<VariationPoint> vpList = vty.getVariationPointList(vPackage);
				DefaultListModel vpInJList = this.getVPListModel(vpList); 
				
				list = new JList(vpInJList);
				list.setBorder(new TitledBorder(null, "Variation Point", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				add(list, BorderLayout.CENTER);
				
				
				btnNewButton_1.addActionListener(new ActionListener() {
			      					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
			        	JFileChooser fc = new JFileChooser();
			        	File fi = new File("/home/DiskD/Dropbox/workspace/Process/model");
			            fc.setCurrentDirectory(fi);
			            int returnVal = fc.showOpenDialog(VariationPointGUI.this);
			        	if (returnVal == JFileChooser.APPROVE_OPTION) {
			        		File file = fc.getSelectedFile();
			        		String newFileName = file.getAbsolutePath();
			        		txtModelcvl.setText(newFileName);
			        		vPackage = vty.getVPackage(newFileName);
			        		ArrayList<VariationPoint> vpList = vty.getVariationPointList(vPackage);
			        		DefaultListModel vpInJList = getVPListModel(vpList); 
			        		list.setModel(vpInJList);
			 	        	 
			        	} else {
			        		//log.append("Open command cancelled by user." + newline);
			        	}
			        	
					}
			      });
	}
	public VPackage getVPackage() {
		// TODO Auto-generated method stub
		return vty.getVPackage(variabilityModelFileName);
	}
	public ArrayList<VariationPoint> getVariationPointList() {
		// TODO Auto-generated method stub
		return vty.getVariationPointList(vPackage);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DefaultListModel getVPListModel(ArrayList<VariationPoint> vpList) {
		DefaultListModel vpInJList = new DefaultListModel(); 
		for (int i = 0; i < vpList.size(); i++) {
			//System.out.println(packageElement.size());
			VariationPoint vp = vpList.get(i);
			if (vp instanceof ObjectExistence) {
				ObjectHandle oObject = ((ObjectExistence) vp).getOptionalObject();
				vpInJList.addElement(vp.getName() + ":ObjectExistence (" + vp.getBindingVSpec().getName()+" -> " + oObject.getMOFRef()+")");
			} 
			else if (vp instanceof ParametricSlotAssignment) {
				ObjectHandle oObject = ((ParametricSlotAssignment) vp).getSlotOwner();
				vpInJList.addElement(vp.getName() + ":ParametricSlotAssignment (" + vp.getBindingVSpec().getName() + " -> "+oObject.getMOFRef() + ")");
			}
			else if (vp instanceof ObjectSubstitution) {
				ObjectHandle oPlacementObject = ((ObjectSubstitution) vp).getPlacementObject();
				ObjectHandle oReplacementObject = ((ObjectSubstitution) vp).getReplacementObject();
				vpInJList.addElement(vp.getName()+":ObjectSubstitution (" + vp.getBindingVSpec().getName()+" -> " + oPlacementObject.getMOFRef() +
						"," + oReplacementObject.getMOFRef() + ")");
		
			}
		}
		return vpInJList;
	}
	public static void main(String arg[]) {
		JFrame fr = new JFrame();
		VariationPointGUI frTree = new VariationPointGUI("model//acme//variationpoint.cvl");
		fr.setLayout(new BorderLayout());
		//frTree.setLayout(new BorderLayout());
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.add(frTree,BorderLayout.CENTER);
		fr.setSize(400,450);
		fr.setVisible(true);
	}
}
