package architecture;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.ow2.fractal.f4e.fractal.Binding;
import org.ow2.fractal.f4e.fractal.Component;
import org.ow2.fractal.f4e.fractal.Definition;
import org.ow2.fractal.f4e.fractal.FractalPackage;
import org.ow2.fractal.f4e.fractal.Interface;

class ComponentShape extends Rectangle {
	/*
	 * define a component 
	 */
	Rectangle rect;
	ArrayList<Arc2D.Float> requestList = new ArrayList<Arc2D.Float>();
	ArrayList<Line2D.Float> lineArcList = new ArrayList<Line2D.Float>(); //line connect from request to component
	ArrayList<Ellipse2D.Float> serviceList = new ArrayList<Ellipse2D.Float>();
	ArrayList<Line2D.Float> lineCircleList = new ArrayList<Line2D.Float>(); //line connect from service to component
	int requestNumber=0, offerNumber=0;
	public int x, y;
	int width, height;
	String name;
	public ComponentShape(int x, int y, int width, int height, int requestNumber, int offerNumber, String name) {
		/*
		 * x,y are position of component
		 * arcNumber is request number of component
		 * circleNumber is interface number of component
		 * name is name of component 
		 */
		this.x = x;
		this.y = y;
		
		width = 100;
		height = 60 ;
		this.width = width;
		this.height = height;
		this.requestNumber = requestNumber;
		this.offerNumber = offerNumber;
		this.name = name;
		rect = new Rectangle(x+35, y, width, height);
		for (int i = 0; i < requestNumber; i++) {
			Arc2D.Float arc = new Arc2D.Float(x+width+55, y + (height/(requestNumber+1))*(i+1)-7,15, 15,90, 180, 0);
			requestList.add(arc);
			Line2D.Float line = new Line2D.Float(x+width+35, y + (height/(requestNumber+1))*(i+1), x+width+55,y + (height/(requestNumber+1))*(i+1));
			lineArcList.add(line);
		}
		for (int j = 0; j < offerNumber; j++) {
			Ellipse2D.Float ellipse = new Ellipse2D.Float(x, y + (height/(offerNumber+1))*(j+1) - 7, 15, 15);
			serviceList.add(ellipse);

			Line2D.Float line = new Line2D.Float(x+15, y + (height/(offerNumber+1))*(j+1), x+35,y + (height/(offerNumber+1))*(j+1));
			lineCircleList.add(line);
		}
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Rectangle getRect() {
		return rect;
	}

	public void setRect(Rectangle rect) {
		this.rect = rect;
	}

	public ArrayList<Arc2D.Float> getRequestList() {
		return requestList;
	}

	public void setArcList(ArrayList<Arc2D.Float> requestList) {
		this.requestList = requestList;
	}

	public ArrayList<Line2D.Float> getLineArcList() {
		return lineArcList;
	}

	public void setLineArcList(ArrayList<Line2D.Float> lineArcList) {
		this.lineArcList = lineArcList;
	}

	public ArrayList<Ellipse2D.Float> getCircleList() {
		return serviceList;
	}

	public void setCircleList(ArrayList<Ellipse2D.Float> circleList) {
		this.serviceList = circleList;
	}

	public ArrayList<Line2D.Float> getLineCircleList() {
		return lineCircleList;
	}

	public void setLineCircleList(ArrayList<Line2D.Float> lineCircleList) {
		this.lineCircleList = lineCircleList;
	}

	public int getRequestNumber() {
		return requestNumber;
	}

	public void setArcNumber(int arcNumber) {
		this.requestNumber = arcNumber;
	}

	public int getServiceNumber() {
		return offerNumber;
	}

	public void setServiceNumber(int circleNumber) {
		this.offerNumber = circleNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
class ConnectionShape {
	String client;
	Interface clientInterface;
	int interfaceClientOrder;
	String server;
	Interface serverInterface;
	int interfaceServerOrder;
	public ConnectionShape(String client, Interface clientInterface, int interfaceClientOrder,
			String server, Interface serverInterface, int interfaceServerOrder) {
		super();
		this.client = client;
		this.clientInterface = clientInterface;
		this.interfaceClientOrder = interfaceClientOrder;
		this.server = server;
		this.serverInterface = serverInterface;
		this.interfaceServerOrder = interfaceServerOrder;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public Interface getClientInterface() {
		return clientInterface;
	}
	public void setClientInterface(Interface clientInterface) {
		this.clientInterface = clientInterface;
	}
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public Interface getServerInterface() {
		return serverInterface;
	}
	public void setServerInterface(Interface serverInterface) {
		this.serverInterface = serverInterface;
	}
	public int getInterfaceClientOrder() {
		return interfaceClientOrder;
	}
	public void setInterfaceClientOrder(int interfaceClientOrder) {
		this.interfaceClientOrder = interfaceClientOrder;
	}
	public int getInterfaceServerOrder() {
		return interfaceServerOrder;
	}
	public void setInterfaceServerOrder(int interfaceServerOrder) {
		this.interfaceServerOrder = interfaceServerOrder;
	}
	
	
}

public class BaseArchitecture extends JPanel {
	ArrayList<ComponentShape> rectList = new ArrayList<ComponentShape>();
	ArrayList<ConnectionShape> connectionList = new ArrayList<ConnectionShape>();
	MovingAdapter ma = new MovingAdapter();
	ComponentShape cp;
	int x=100,y=100;
	int componentNumber;
	public BaseArchitecture() {
		/*
	     * declare components list
	     */
		FractalPackage.eINSTANCE.eClass();
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		try {
			//registry extent part of model file ex: *.variability
			reg.getExtensionToFactoryMap().put("fractal", new XMIResourceFactoryImpl());
		} catch (Exception e){
		}
		ResourceSet resourceSet = new ResourceSetImpl();
		URI uri = URI.createFileURI("model//architecture.fractal");
		Resource resource = resourceSet.getResource(uri, true);
		//get root of variability model 
		Definition definition = (Definition) resource.getContents().get(0);
		//System.out.println(definition.toString());
		EList<Component> compponentList = definition.getSubComponents();
		this.componentNumber = compponentList.size();
		for (int i = 0; i < compponentList.size(); i++) {
			EList<Interface> interfaces = compponentList.get(i).getInterfaces();
			int requestNumber = 0 , offerNumber = 0;
			for (int j = 0; j < interfaces.size(); j++) {
				//System.out.println(interfaces.get(j).getRole().name());
				if (interfaces.get(j).getRole().name().equals("CLIENT") || interfaces.get(j).getRole().name() == null) requestNumber += 1;
				if (interfaces.get(j).getRole().name().equals("SERVER")) offerNumber += 1;
			}
			//System.out.println(requestNumber +","+offerNumber);
			cp = new ComponentShape(x + i*150, y+i*80, 100, 60, requestNumber, offerNumber, compponentList.get(i).getName());
			rectList.add(cp);
		}
		
		EList<Binding> bindingList = definition.getBindings();
		for (int i = 0; i < bindingList.size(); i++) {
			String client = bindingList.get(i).getClient();
			Interface clientInterface =  bindingList.get(i).getClientInterface();
			
			//get order of interface in component
			int clientInterfaceOrder = requestInterfaceOrder(compponentList, client, clientInterface .getName());
			
			System.out.println(clientInterfaceOrder + clientInterface .getName());
			
			String server = bindingList.get(i).getServer();
			Interface serverInterface =  bindingList.get(i).getServerInterface();
			int serverInterfaceOrder = offerInterfaceOrder(compponentList, server, serverInterface .getName());
			
			ConnectionShape cs = new ConnectionShape(client, clientInterface, clientInterfaceOrder, server, serverInterface, serverInterfaceOrder);
			connectionList.add(cs);
		}
		addMouseMotionListener(ma);
		addMouseListener(ma);
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
				i = j;
				System.out.println(i);
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
				i = j;
				System.out.println(i);
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
		for (int j = 0; j < componentNumber; j++) {
			g2d.draw(rectList.get(j).getRect());
			g2d.drawString(rectList.get(j).getName(), rectList.get(j).getRect().x + 10, rectList.get(j).getRect().y + 10);
		    for (int i = 0; i < rectList.get(j).getRequestList().size(); i++) {
		    	g2d.draw(rectList.get(j).getRequestList().get(i));
		    	g2d.draw(rectList.get(j).getLineArcList().get(i));
		    }
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
			System.out.println(client+":"+server);
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
		
//		//connect from 1 to 2
//		int y1 = (int) rectList.get(0).getRequestList().get(0).y + 7;
//		int x2 = (int) rectList.get(1).getCircleList().get(0).x;
//		int y2 = (int) rectList.get(1).getCircleList().get(0).y + 7;
//		g2d.drawLine(x1, y1, x2, y2);
//		//connect from 2 to 3
//		x1 = (int) rectList.get(1).getRequestList().get(0).x;
//		y1 = (int) rectList.get(1).getRequestList().get(0).y + 7;
//		x2 = (int) rectList.get(2).getCircleList().get(0).x;
//		y2 = (int) rectList.get(2).getCircleList().get(0).y + 7;
//		g2d.drawLine(x1, y1, x2, y2);
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
    BaseArchitecture m = new BaseArchitecture();
    m.setDoubleBuffered(true);
    
    JScrollPane scrollPane = new JScrollPane(m);
    frame.add(scrollPane);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(500, 500);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}