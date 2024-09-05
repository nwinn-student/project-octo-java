import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Component;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import java.awt.Point;
import java.util.List;
import java.util.Arrays;


/**
 * The Connector class has JPanel objects attached to it, responding to clicks 
 * and drags to increase functionality and make it easier for the user to understand 
 * the connections.
 *
 * @author Noah Winn
 * @version 9/5/2024
 */


public class Connector extends JPanel implements MouseListener,MouseMotionListener
{
    // instance variables
    private int screenX = 0;
    private int screenY = 0;
    private int myX = 0;
    private int myY = 0;
    
    private double currentZoom = 1;
    private double maxZoom = 5;
    private double minZoom = .25;
    private boolean isHighlighted = false;
    private GroupSelector select;
    private String pos = "";
    private Node parentNode = null;
    
    private Border blackBorder = BorderFactory.createLineBorder(Color.BLACK);
    private Border redBorder = BorderFactory.createLineBorder(Color.RED,3);
    private JPanel panVert = new JPanel();
    private JPanel panHori = new JPanel();
    private Point point = new Point(getX(), getY());
    private boolean isConnected = false;
    private boolean isVisiblyConnected = false;
    private Node connParentNode = null;
    
    /**
     * Constructor for objects of class Panel
     */
    public Connector(String pos, GroupSelector select, Node parentNode){
        this.pos = pos;
        this.select = select;
        this.parentNode = parentNode;
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setBackground(Color.black);
        this.setLayout(null);
        this.setBorder(blackBorder);
        this.setForeground(Color.black);
        this.setBounds(0, 0,5,5);
        this.setVisible(true);
        panVert.setVisible(false);
        panHori.setVisible(false);
        panVert.setLayout(null);
        panVert.setBackground(Color.blue);
        panHori.setLayout(null);
        panHori.setBackground(new Color(135,135,255));
        select.add(panVert);
        select.add(panHori);
    }
    public Node getParentNode(){return parentNode;}
    public boolean isVisiblyConnected(){return isVisiblyConnected;}
    public Rectangle getHorizontal(){return panHori.getBounds();}
    public void setConnParentNode(Node connParentNode){
        this.connParentNode = connParentNode;
        isConnected = true;
        if(connParentNode != parentNode){
            updateConnectionPosition();
        }
    }
    public void removeConnected(){
        isConnected = false;
        connParentNode = null;
        panVert.setVisible(false);
        panHori.setVisible(false);
    }
    public void updatePosition(){
        int height = (int) this.parentNode.getSize().getHeight();
        int width = (int) this.parentNode.getSize().getWidth();
        int positionX = (int) this.parentNode.getLocation().getX();
        int positionY = (int) this.parentNode.getLocation().getY();

        int curHeight = (int) this.getSize().getHeight();
        int curWidth = (int) this.getSize().getWidth();
        if(pos.equals("Top")){
            this.setBounds((width-curWidth)/2 + positionX,positionY-height/10, height/10, width/10);
            if(connParentNode != null && connParentNode != parentNode)
                connectToPoint();
        } else if (pos.equals("Bottom")){
            this.setBounds((width-curWidth)/2 + positionX,height-curHeight + positionY+height/10, height/10, width/10);
            if(connParentNode != null && connParentNode != parentNode)
                connectToPoint();
        }
        select.repaint();
    }
    public void updateConnectionPosition(){        
        if(connParentNode != null && connParentNode != parentNode){
            point = connParentNode.getClosestConnector(this).getLocation();
            connectToPoint();
        } else {
            panVert.setVisible(false);
            panHori.setVisible(false);
        }
    }
    public void removeConnections(){
        select.remove(panHori);
        select.remove(panVert);
        select.repaint();
    }
    /**
     * Initializes the position of the node.
     */
    @Override
    public void mousePressed(MouseEvent e){
        screenX = e.getXOnScreen(); screenY = e.getYOnScreen();
        myX = getX(); myY = getY();
    }
    /**
     * Moves the JPanels and resizes them to be at the cursor.
     */
    @Override
    public void mouseDragged(MouseEvent e){
        isConnected=false;
        int deltaX = e.getXOnScreen() - screenX;
        int deltaY = e.getYOnScreen() - screenY;
        int h = (int)this.getSize().getHeight();
        int w = (int)this.getSize().getWidth();
        myX = getX();
        myY = getY();
        // if dragged down right
        if(deltaX > 0 && pos.equals("Bottom") && deltaY > 0){
            panVert.setBounds(myX, myY, h, w+deltaY);
            panHori.setBounds(myX, myY+deltaY, h + deltaX, w);
        }
        // if dragged down left
        else if(deltaX < 0 && pos.equals("Bottom") && deltaY > 0){
            panVert.setBounds(myX, myY, h, w+deltaY);
            panHori.setBounds(myX+deltaX, myY+deltaY, h - deltaX, w);
        }
        // if dragged up right
        else if(deltaX > 0 && pos.equals("Top") && deltaY < 0){
            panVert.setBounds(myX, myY+deltaY, h, w-deltaY);
            panHori.setBounds(myX, myY+deltaY, h + deltaX, w);
        }
        // if dragged up left
        else if(deltaX < 0 && pos.equals("Top") && deltaY < 0){
            panVert.setBounds(myX, myY+deltaY, h, w-deltaY);
            panHori.setBounds(myX+deltaX, myY+deltaY, h - deltaX, w);
        }
        else{
            //panVert.setVisible(false);
            //panHori.setVisible(false);
        }
        if(!panVert.isVisible() || !panHori.isVisible()){
            panVert.setVisible(true);
            panHori.setVisible(true);            
        }
        point = new Point(getX(), getY());
        //point.setLocation(myX+deltaX, myY+deltaY);
        //isConnected = false;
        select.repaint();
    }
    @Override
    public void mouseExited(MouseEvent e){}
    @Override
    public void mouseEntered(MouseEvent e){}
    /**
     * Connects the Node touched by the Connector to the parent Node.
     */
    @Override
    public void mouseReleased(MouseEvent e){
        if (connParentNode != null){
            connParentNode.removeChild(this.parentNode);
        }
        if(!isConnected){
            // Look at distance, if too far away then remove
            // SNAP TO the nearest panel connector within x distance, NOT DONE, currently just if they overlap
            for(Node elem : select.getNodes()){
                if(fallsInside(panHori.getBounds(), elem.getBounds()) || fallsInside(panHori.getBounds(), elem.getTop().getBounds())
                    || fallsInside(panHori.getBounds(), elem.getBottom().getBounds())){
                    
                        connParentNode = elem;
                        this.parentNode.setParentNode(connParentNode);
                        // may not happen if there are descendants and one of the parentNode's descendants is itself
                        if(this.parentNode.getParentNode() == connParentNode){
                            isConnected = true;
                        }
                        break;
                }
            }
            if(!isConnected){
                panVert.setVisible(false);
                panHori.setVisible(false);
            }
            select.repaint();
        }
    }
    @Override
    public void mouseClicked(MouseEvent e){}
    @Override
    public void mouseMoved(MouseEvent e){}
    /**
     * Whether a rectangle intersects with another rectangle, order doesn't matter.
     * @param a, the first Rectangle
     * @param b, the second Rectangle
     * @returns boolean, whether the Rectangles are connected or not.
     */
    private boolean fallsInside(Rectangle a, Rectangle b){
        if(a.getBounds().intersects(b.getBounds())){
            return true;
        }
        return false;
    }
    /**
     * Connects the JPanels to a predetermined point
     */
    public void connectToPoint(){
        if(pos.equals("Top")){
            int h = (int)this.getSize().getHeight();
            int w = (int)this.getSize().getWidth();
            myX = getX();
            myY = getY();
            int pointX = (int)point.getX()-myX; // + means left, - means right
            int pointY = (int)point.getY()-myY; // + means up, - means down
            panVert.setBounds(myX, myY+pointY, w, -pointY);
            if(pointX > 0 && -pointY > 0)
                panHori.setBounds(myX, myY+pointY, Math.abs(pointX), h); // myX for right
            else if(pointX < 0 && -pointY > 0)
                panHori.setBounds((int)point.getX(), myY+pointY, Math.abs(pointX), h);
            else if(pointX < 0 && pointY > 0){
                // WORKING HERE, good for to the right, need another if for to the left, also gotta make it go size 0 when needed
                panVert.setBounds((int)point.getX(), myY+(int)(connParentNode.getSize().getHeight()+h), Math.abs(pointX), h); // Get the Bottom connector to connect to the parent
                panHori.setBounds(myX+pointX, myY+pointY, w, (int)(connParentNode.getSize().getHeight()+h)-pointY);
                // Issue is that it'll cause confusion due to coloring, may need to swap??
                // Dark blue will go horizontal with light blue going down.
            } else if(pointX > 0 && pointY > 0){
                panVert.setBounds(myX+w, myY+(int)(connParentNode.getSize().getHeight()+h), Math.abs(pointX), h); // Get the Bottom connector to connect to the parent
                panHori.setBounds(myX+pointX, myY+pointY, w, (int)(connParentNode.getSize().getHeight()+h)-pointY);
            } else if(pointX == 0 && pointY != 0){
                // 0's, need to work on
                panHori.setBounds(myX,myY-pointY, w, Math.abs(pointY));
                panVert.setBounds(myX,myY,0,0);
                System.out.println("Zero'd");
            }
            isVisiblyConnected = true;
            if(parentNode.getBottom().isVisiblyConnected()){
                panHori.setSize(0,0);
            }
            if(panHori.getSize().getHeight() <= 0){
                panVert.setSize(0,0);
                isVisiblyConnected = false;
            }
        } else if (pos.equals("Bottom")){
            int h = (int)this.getSize().getHeight();
            int w = (int)this.getSize().getWidth();
            myX = getX();
            myY = getY();
            int pointX = (int)point.getX()-myX; // + means left, - means right
            int pointY = (int)point.getY()-myY; // + means up, - means down
            
            panVert.setBounds(myX, myY+h, w, pointY);
            if(pointX > 0 && pointY > 0)
                panHori.setBounds(myX, myY+pointY, Math.abs(pointX), h);
            else if(pointX < 0 && pointY > 0)
                panHori.setBounds((int)point.getX(), myY+pointY, Math.abs(pointX), h);
            else{
                // somehow check if the top's panHori is within the node or not
                
                if(pointX > 0 && !parentNode.getTop().isVisiblyConnected()){
                    System.out.println(">"+pointX+" "+pointY);
                } else if(pointX < 0 && !parentNode.getTop().isVisiblyConnected()){
                    System.out.println("< "+pointX+" "+pointY);
                }
                if(parentNode.getTop().getHorizontal().intersects(parentNode.getBounds()) || parentNode.getTop().getHorizontal().intersects(connParentNode.getBounds())){
                    //System.out.println("Top's panHori is within ParentNode.");
                }
                panHori.setBounds(myX, 0, 0, 0); // Get the Top connector to connect to the parent
            }
            isVisiblyConnected = true;
            if(panHori.getSize().getHeight() <= 0){
                panVert.setSize(0,0);
                isVisiblyConnected = false;
            }
        }
        if(!point.equals(new Point(getX(),getY())) && connParentNode != parentNode){
            panVert.setVisible(true);
            panHori.setVisible(true);
        }
    }
}
