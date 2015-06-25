package architecture;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
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

class Component extends Rectangle {
	Rectangle rect;
	ArrayList<Arc2D.Float> arcList = new ArrayList<Arc2D.Float>();
	ArrayList<Line2D.Float> lineArcList = new ArrayList<Line2D.Float>();
	ArrayList<Ellipse2D.Float> circleList = new ArrayList<Ellipse2D.Float>();
	ArrayList<Line2D.Float> lineCircleList = new ArrayList<Line2D.Float>();
	int arcNumber=0, circleNumber=0;
	public int x, y;
	int width, height;
	String name;
	public Component(int x, int y, int width, int height, int arcNumber, int circleNumber, String name) {
		/*
		 * x,y are position of component
		 * 
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
		this.arcNumber = arcNumber;
		this.circleNumber = circleNumber;
		this.name = name;
		rect = new Rectangle(x+35, y, width, height);
		for (int i = 0; i < arcNumber; i++) {
			Arc2D.Float arc = new Arc2D.Float(x+width+55, y + (height/(arcNumber+1))*(i+1)-7,15, 15,90, 180, 0);
			arcList.add(arc);
			
			Line2D.Float line = new Line2D.Float(x+width+35, y + (height/(arcNumber+1))*(i+1), x+width+55,y + (height/(arcNumber+1))*(i+1));
			lineArcList.add(line);
		}
		for (int j = 0; j < circleNumber; j++) {
			Ellipse2D.Float ellipse = new Ellipse2D.Float(x, y + (height/(circleNumber+1))*(j+1) - 7, 15, 15);
			circleList.add(ellipse);
			
			Line2D.Float line = new Line2D.Float(x+15, y + (height/(circleNumber+1))*(j+1), x+35,y + (height/(circleNumber+1))*(j+1));
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

	public ArrayList<Arc2D.Float> getArcList() {
		return arcList;
	}

	public void setArcList(ArrayList<Arc2D.Float> arcList) {
		this.arcList = arcList;
	}

	public ArrayList<Line2D.Float> getLineArcList() {
		return lineArcList;
	}

	public void setLineArcList(ArrayList<Line2D.Float> lineArcList) {
		this.lineArcList = lineArcList;
	}

	public ArrayList<Ellipse2D.Float> getCircleList() {
		return circleList;
	}

	public void setCircleList(ArrayList<Ellipse2D.Float> circleList) {
		this.circleList = circleList;
	}

	public ArrayList<Line2D.Float> getLineCircleList() {
		return lineCircleList;
	}

	public void setLineCircleList(ArrayList<Line2D.Float> lineCircleList) {
		this.lineCircleList = lineCircleList;
	}

	public int getArcNumber() {
		return arcNumber;
	}

	public void setArcNumber(int arcNumber) {
		this.arcNumber = arcNumber;
	}

	public int getCircleNumber() {
		return circleNumber;
	}

	public void setCircleNumber(int circleNumber) {
		this.circleNumber = circleNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	


}
public class BaseArchitecture extends JPanel {
  
  ArrayList<Component> rectList = new ArrayList<Component>();
  MovingAdapter ma = new MovingAdapter();
  Component cp;
  int x=100,y=100;
  int componentNumber = 3;
  public BaseArchitecture() {
	  
	  for (int i = 0; i < componentNumber; i++) {
		  cp = new Component(x + i*50, y+i*50, 100, 60, 1, 1, "name");
		  rectList.add(cp);
	  }
	  addMouseMotionListener(ma);
	  addMouseListener(ma);
  }
  public void paint(Graphics g) {
    super.paint(g);

    Graphics2D g2d = (Graphics2D) g;

    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    g2d.setColor(new Color(0, 0, 200));
    
    
    for (int j = 0; j < componentNumber; j++) {
    	g2d.draw(rectList.get(j).getRect());
    	g2d.drawString(rectList.get(j).getName(), rectList.get(j).getRect().x + 10, rectList.get(j).getRect().y + 10);
        for (int i = 0; i < rectList.get(j).getArcList().size(); i++) {
        	System.out.println(rectList.get(j).getRect().x+","+rectList.get(j).getRect().y 
    	    		  +","+rectList.get(j).width+","+rectList.get(j).height);
        	System.out.println("repaint");
        	
        	g2d.draw(rectList.get(j).getArcList().get(i));
        	g2d.draw(rectList.get(j).getLineArcList().get(i));
        }
        for (int i = 0; i < rectList.get(j).getCircleList().size(); i++) {
            g2d.draw(rectList.get(j).getCircleList().get(i));
        	g2d.draw(rectList.get(j).getLineCircleList().get(i));
        }
    }
    
    //connect from 1 to 2
    int x1 = (int) rectList.get(0).getArcList().get(0).x;
    int y1 = (int) rectList.get(0).getArcList().get(0).y + 7;
    int x2 = (int) rectList.get(1).getCircleList().get(0).x;
    int y2 = (int) rectList.get(1).getCircleList().get(0).y + 7;
    g2d.drawLine(x1, y1, x2, y2);
    
    x1 = (int) rectList.get(1).getArcList().get(0).x;
    y1 = (int) rectList.get(1).getArcList().get(0).y + 7;
    x2 = (int) rectList.get(2).getCircleList().get(0).x;
    y2 = (int) rectList.get(2).getCircleList().get(0).y + 7;
    g2d.drawLine(x1, y1, x2, y2);
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
      for (int j = 0; j < componentNumber; j++) {
	 	  if (rectList.get(j).getRect().getBounds2D().contains(x, y)) {
	 		 rectList.get(j).getRect().x += dx;
	 		 rectList.get(j).getRect().y += dy;
			 for (int i = 0; i < rectList.get(j).getArcList().size(); i++) {
					//set new point to arcs
					rectList.get(j).getArcList().get(i).x += dx;
					rectList.get(j).getArcList().get(i).y += dy;
					
					rectList.get(j).getLineArcList().get(i).x1 +=dx;
					rectList.get(j).getLineArcList().get(i).x2 +=dx;
					rectList.get(j).getLineArcList().get(i).y1 +=dy;
					rectList.get(j).getLineArcList().get(i).y2 +=dy;
			}
			for (int i = 0; i < cp.getCircleList().size(); i++) {
					rectList.get(j).getCircleList().get(i).x +=dx;
					rectList.get(j).getCircleList().get(i).y +=dy;
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
    frame.add(m);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(500, 500);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}