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
 * and drags to increase functionality.
 *
 * @author Noah Winn
 * @version 6/25/2024
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
    public Node getParentNode(){
        return parentNode;
    }
    public void setConnParentNode(Node connParentNode){
        this.connParentNode = connParentNode;
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
            connectToPoint();
        } else if (pos.equals("Bottom")){
            this.setBounds((width-curWidth)/2 + positionX,height-curHeight + positionY+height/10, height/10, width/10);
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
        panVert.setVisible(true);
        panHori.setVisible(true);
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
        //isConnected = true; // fixed for testing
        if(!isConnected){
            // Look at distance, if too far away then remove
            // SNAP TO the nearest panel connector within x distance
            List<Component> frameElements = Arrays.asList(select.getComponents());
            if(frameElements.size() < 4096){
                for(Component elem : frameElements){
                    if(elem.getClass().equals(Node.class) || elem.getClass().equals(Connector.class)){
                        if(fallsInside(panHori.getBounds(),elem.getBounds())){                            
                            if(elem.getClass().equals(Connector.class)){
                                connParentNode = ((Connector) elem).getParentNode();
                            } else {
                                connParentNode = (Node) elem;
                            }
                            this.parentNode.setParentNode(connParentNode);
                            if(this.parentNode.getParentNode() == connParentNode){
                                updateConnectionPosition();
                                isConnected = true;
                                //panVert.setVisible(true);
                                //panHori.setVisible(true);
                            }
                            break;
                        }
                    }
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
            else{
                panHori.setBounds(myX, 0, 0, h);
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
                panHori.setBounds(myX, 0, 0, 0);
            }
        }
        if(!point.equals(new Point(0,0)) && connParentNode != parentNode){
            panVert.setVisible(true);
            panHori.setVisible(true);
        }
    }
}
