package baseArchitecture.gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.ow2.fractal.f4e.fractal.Attribute;
import org.ow2.fractal.f4e.fractal.Binding;
import org.ow2.fractal.f4e.fractal.Component;
import org.ow2.fractal.f4e.fractal.Definition;
import org.ow2.fractal.f4e.fractal.FractalPackage;
import org.ow2.fractal.f4e.fractal.Interface;


@SuppressWarnings("serial")
public class ProductArchitecture extends JPanel {
	ArrayList<ComponentShape> rectList = new ArrayList<ComponentShape>();
	ArrayList<ConnectionShape> connectionList = new ArrayList<ConnectionShape>();
	MovingAdapter ma = new MovingAdapter();
	ComponentShape cp;
	int x=100,y=100;
	int componentNumber;
	ArrayList<String> listAttributes = new ArrayList<String>();
	//EList<RealizationComponent> realizationCompponentList;

	String productFileName = "model//product.fractal";
	public ProductArchitecture(String productFileName) {
		this.loadModel(productFileName);
		addMouseMotionListener(ma);
		addMouseListener(ma);
	}
	public void loadModel(String productFileName) {
		FractalPackage.eINSTANCE.eClass();
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		try {
			//registry extent part of model file ex: *.variability
			reg.getExtensionToFactoryMap().put("fractal", new XMIResourceFactoryImpl());
		} catch (Exception e){
		}
		ResourceSet resourceSet = new ResourceSetImpl();
		URI uri = URI.createFileURI(productFileName);
		
		Resource resource = resourceSet.getResource(uri, true);
		//get root of variability model 
		Definition definition = (Definition) resource.getContents().get(0);
		EList<Component> compponentList = definition.getSubComponents();
		
		for (int i = 0; i < compponentList.size(); i++) {
			EList<Interface> interfaces = compponentList.get(i).getInterfaces();
			int requestNumber = 0 , offerNumber = 0;
			for (int j = 0; j < interfaces.size(); j++) {
				//System.out.println(interfaces.get(j).getRole().name());
				if (interfaces.get(j).getRole().name().equals("CLIENT") || interfaces.get(j).getRole().name() == null) requestNumber += 1;
				if (interfaces.get(j).getRole().name().equals("SERVER")) offerNumber += 1;
			}
			
			EList<Attribute> attributeList = compponentList.get(i).getAttributesController().getAttributes();
			Random randomGenerator = new Random(); 
			x = randomGenerator.nextInt(500);
			y = randomGenerator.nextInt(300);
			//System.out.println(requestNumber +","+offerNumber);
			String cnt = "";
			try {
				if (compponentList.get(i).getContent().getClass_() != null) cnt = compponentList.get(i).getContent().getClass_();
			} catch (Exception e) {
				
			}
			cp = new ComponentShape(x, y, 100, 60, requestNumber, offerNumber, compponentList.get(i).getName(), attributeList, cnt);
			rectList.add(cp);
		}
		//realizationCompponentList = definition.getRealizationComponents();
		
//		for (int i = 0; i < realizationCompponentList.size(); i++){
//			
//			Random randomGenerator = new Random(); 
//			x = randomGenerator.nextInt(500);
//			y = randomGenerator.nextInt(300);
//			
//			cp = new ComponentShape(x, y, 100, 60, 0, 0, realizationCompponentList.get(i).getName(), null, "");
//			//System.out.println("s");
//			rectList.add(cp);
//		}
		
		this.componentNumber = compponentList.size();
		EList<Binding> bindingList = definition.getBindings();
		for (int i = 0; i < bindingList.size(); i++) {
			String client = bindingList.get(i).getClient();
			Interface clientInterface =  bindingList.get(i).getClientInterface();
			
			//get order of interface in component
			int clientInterfaceOrder = requestInterfaceOrder(compponentList, client, clientInterface .getName());
			
			//System.out.println(clientInterfaceOrder + clientInterface .getName());
			
			String server = bindingList.get(i).getServer();
			Interface serverInterface =  bindingList.get(i).getServerInterface();
			int serverInterfaceOrder = offerInterfaceOrder(compponentList, server, serverInterface .getName());
			//if (serverInterfaceOrder > 0) {
				ConnectionShape cs = new ConnectionShape(client, clientInterface, clientInterfaceOrder-1, server, serverInterface, serverInterfaceOrder-1);
				connectionList.add(cs);
			//} else System.out.println("can not find server interface");
		}
		
	}
	Component getComponent(EList<Component> compponentList, String componentName) {
		Component component = null;
		for (int i = 0; i < compponentList.size(); i++) {
			if (compponentList.get(i).getName().equals(componentName)) return compponentList.get(i); 
		}
		return component;
	}
	public int  requestInterfaceOrder(EList<Component> compponentList, String componentName, String requestInterface) {
		int i = 0;
		Component c = getComponent(compponentList, componentName);
		EList<Interface> listInterface = c.getInterfaces();
		for (int j = 0; j < listInterface.size(); j++) {
			if ((listInterface.get(j).getRole().name().equals("CLIENT") ) && 
					listInterface.get(j).getName().equals(requestInterface)) {
				i++;
				return i;
			}
		}
		return i;
	}
	public int  offerInterfaceOrder(EList<Component> compponentList, String componentName, String offerInterface) {
		int i = 0;
		Component c = getComponent(compponentList, componentName);
		EList<Interface> listInterface = c.getInterfaces();
		for (int j = 0; j < listInterface.size(); j++) {
			if ((listInterface.get(j).getRole().name().equals("SERVER")) &&
					listInterface.get(j).getName().equals(offerInterface)) {
				i++;
				return i;
			}
		}
		return i;
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
		    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setColor(new Color(0, 0, 200));
		/*
		 * read components list and paint them in graphic 
		 */
		for (int j = 0; j < rectList.size(); j++) {
			//draw rect
			g2d.draw(rectList.get(j).getRect());
			
			
			g2d.drawString(rectList.get(j).getName(), rectList.get(j).getRect().x + 30, rectList.get(j).getRect().y + 10);
			if (rectList.get(j).getContent() != "") 
				g2d.drawString("Content= "+rectList.get(j).getContent(), rectList.get(j).getRect().x, rectList.get(j).getRect().y + 20);
			if (rectList.get(j).getAttributes() != null)    
			for (int i = 0; i < rectList.get(j).getAttributes().size(); i++) {
				g2d.drawString(rectList.get(j).getAttributes().get(i).getName() + "=" + rectList.get(j).getAttributes().get(i).getValue(), rectList.get(j).getRect().x , rectList.get(j).getRect().y + 10 + 10*(i+2));
			}
			
			
		    
			
			//draw arc
			for (int i = 0; i < rectList.get(j).getRequestList().size(); i++) {
		    	g2d.draw(rectList.get(j).getRequestList().get(i));
		    	g2d.draw(rectList.get(j).getLineArcList().get(i));
		    }
			//draw interface -circle
		    for (int i = 0; i < rectList.get(j).getCircleList().size(); i++) {
		        g2d.draw(rectList.get(j).getCircleList().get(i));
		    	g2d.draw(rectList.get(j).getLineCircleList().get(i));
		    }
		}
		
		/*
		 * read connection list and paint in graphic
		 */
		for (int i = 0; i < connectionList.size(); i++) {
			ConnectionShape cs = connectionList.get(i);
			String client = cs.getClient();
			String server = cs.getServer();
			//System.out.println(client+":"+server);
			int x1 = 0, y1 = 0, x2 = 0, y2 = 0;
			
			for (int j = 0; j < componentNumber; j++) {
				if (rectList.get(j).getName().equals(client)) {
					x1 = (int) rectList.get(j).getRequestList().get(cs.getInterfaceClientOrder()).x;
					y1 = (int) rectList.get(j).getRequestList().get(cs.getInterfaceClientOrder()).y + 7;
				}
				if (rectList.get(j).getName().equals(server)) {
					x2 = (int) rectList.get(j).getCircleList().get(cs.getInterfaceServerOrder()).x;
					y2 = (int) rectList.get(j).getCircleList().get(cs.getInterfaceServerOrder()).y + 7;
				}
			}
			g2d.drawLine(x1, y1, x2, y2);
		}
		

		/*
		 * read realization component and draw extend line
		 * 
		 */
//		for (int i = 0; i < realizationCompponentList.size(); i++) {
//			
//			int x1 = 0, y1 = 0, x2 = 0, y2 = 0;
//			String component = realizationCompponentList.get(i).getRealizes().getName();
//			String realizationComponentName = realizationCompponentList.get(i).getName();
//			for (int j = 0; j < rectList.size(); j++) {
//				if (rectList.get(j).getName().equals(realizationComponentName)) {
//					x1 = (int) rectList.get(j).getRect().x + 50;
//					y1 = (int) rectList.get(j).getRect().y + 0;
//				}
//				if (rectList.get(j).getName().equals(component)) {
//					x2 = (int) rectList.get(j).getRect().x + 50;
//					y2 = (int) rectList.get(j).getRect().y + 60;
//				}
//			}
//			Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
//			g2d.setStroke(dashed);
//			g2d.drawLine(x1, y1, x2, y2);
//			
//		}
  }
  public Dimension getPreferredSize() {
      return new Dimension(3000, 1000);
  }
  
  	class MovingAdapter extends MouseAdapter {
  		private int x;
  		private int y;
  		public void mousePressed(MouseEvent e) {
  			x = e.getX();
  			y = e.getY();
  	}
    public void mouseDragged(MouseEvent e) {
    	int dx = e.getX() - x;
    	int dy = e.getY() - y;
    	/*
    	 * set new points for components and 
    	 * repaint them
    	 */
    	for (int j = 0; j < rectList.size(); j++) {
    		if (rectList.get(j).getRect().getBounds2D().contains(x, y)) {
    			rectList.get(j).getRect().x += dx;
    			rectList.get(j).getRect().y += dy;
    			for (int i = 0; i < rectList.get(j).getRequestList().size(); i++) {
    				//set new point to arcs
    				rectList.get(j).getRequestList().get(i).x += dx;
    				rectList.get(j).getRequestList().get(i).y += dy;
    				// set new line between arc and rect
    				rectList.get(j).getLineArcList().get(i).x1 +=dx;
    				rectList.get(j).getLineArcList().get(i).x2 +=dx;
    				rectList.get(j).getLineArcList().get(i).y1 +=dy;
    				rectList.get(j).getLineArcList().get(i).y2 +=dy;
    			}
    			for (int i = 0; i < rectList.get(j).getCircleList().size(); i++) {
    				//set new circle
    				rectList.get(j).getCircleList().get(i).x +=dx;
    				rectList.get(j).getCircleList().get(i).y +=dy;
    				// set new line between circle and rect
    				rectList.get(j).getLineCircleList().get(i).x1 +=dx;
    				rectList.get(j).getLineCircleList().get(i).x2 +=dx;
    				rectList.get(j).getLineCircleList().get(i).y1 +=dy;
    				rectList.get(j).getLineCircleList().get(i).y2 +=dy;
    			}
    			repaint();
				}  
    	}
    	x += dx;
    	y += dy;
    }
  }
  public static void main(String[] args) {
    JFrame frame = new JFrame("Moving");
    ProductArchitecture m = new ProductArchitecture("model//product.fractal");
    m.setDoubleBuffered(true);
    
    JScrollPane scrollPane = new JScrollPane(m);
    frame.add(scrollPane);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(500, 500);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}