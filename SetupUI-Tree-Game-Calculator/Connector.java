import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
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
 * @version 5/25/2024
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
    private Node pan = null;
    
    private Border blackBorder = BorderFactory.createLineBorder(Color.BLACK);
    private Border redBorder = BorderFactory.createLineBorder(Color.RED,3);
    private JPanel panVert = null;
    private JPanel panHori = null;
    private Point point = null;
    private boolean isConnected = false;
    private Node connectedTo = null;
    
    //private KeyboardFocusManager focusManager =
    //        KeyboardFocusManager.getCurrentKeyboardFocusManager();
    /**
     * Constructor for objects of class Panel
     */
    public Connector(String pos, GroupSelector select, Node pan){
        //Source: https://stackoverflow.com/questions/874360
        this.pos = pos;
        this.select = select;
        this.pan = pan;
        
        this.addMouseListener(this);
        
        this.addMouseMotionListener(this);
        this.setBackground(Color.black);
        this.setLayout(null);
        this.setBorder(blackBorder);
        this.setForeground(Color.black);
        this.setBounds(0, 0,5,5);
        this.setVisible(true);
        //this.setName("Connector");
        //this.setRequestFocusEnabled(true);
    }
    public Node getParentNode(){
        return pan;
    }
    public void removeConnected(){
        isConnected = false;
        connectedTo = null;
        panVert.setVisible(false);
        panHori.setVisible(false);
    }
    public void updatePosition(){
        int height = (int) this.pan.getSize().getHeight();
        int width = (int) this.pan.getSize().getWidth();
        int positionX = (int) this.pan.getLocation().getX();
        int positionY = (int) this.pan.getLocation().getY();

        int curHeight = (int) this.getSize().getHeight();
        int curWidth = (int) this.getSize().getWidth();
        if(pos.equals("Top")){
            this.setBounds((width-curWidth)/2 + positionX,positionY-height/10, height/10, width/10);
            if(panHori != null){
                connectToPoint();
            }
        }
        else if(pos.equals("Left")){
            //this.setBounds(positionX-width/10,(height-curHeight)/2 + positionY, height/10, width/10);
        }
        else if(pos.equals("Right")){
            //this.setBounds(width-curWidth + positionX + width/10,(height-curHeight)/2 + positionY, height/10, width/10);
        }
        else if(pos.equals("Bottom")){
            this.setBounds((width-curWidth)/2 + positionX,height-curHeight + positionY+height/10, height/10, width/10);
            if(panHori != null){
                connectToPoint();
            }
        }
        
        //this.setBounds(height, width, 5,5);
        select.repaint();
        //this.repaint();
    }
    public void updateConnectionPosition(){
        //System.out.println("Updating Connection...");
        point = connectedTo.getClosestConnector(this).getLocation();
        connectToPoint();
    }
    public void removeConnections(){
        if(panHori != null){
            select.remove(panHori);
        }
        if(panVert != null){
            select.remove(panVert);
        }
        select.repaint();
    }
    /**
     * Zooms in and out in accordance with mouse scroll wheel
     */
    public void mouseWheelMoved(MouseWheelEvent e){
    }
    public void mousePressed(MouseEvent e){
        screenX = e.getXOnScreen();
        screenY = e.getYOnScreen();
                
        myX = getX();
        myY = getY();
        if(panVert == null){
            panVert = new JPanel();
            panHori = new JPanel();
            point = new Point(screenX, screenY);
            panVert.setLayout(null);
            panVert.setBackground(Color.blue);
            //panVert.setBounds(myX, myY,10,10);
            panVert.setVisible(true);
            panHori.setLayout(null);
            panHori.setBackground(Color.blue);
            //panHori.setBounds(myX, myY,10,10);
            panHori.setVisible(true);
        
            select.add(panVert);
            select.add(panHori);
            //select.repaint();
        }
        
        
        
    }
    @Override
    public void mouseDragged(MouseEvent e){
        isConnected=false;
        int deltaX = e.getXOnScreen() - screenX;
        int deltaY = e.getYOnScreen() - screenY;
        int h = (int)this.getSize().getHeight();
        int w = (int)this.getSize().getWidth();
        myX = getX();
        myY = getY();
        panVert.setVisible(true);
        panHori.setVisible(true);
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
            panVert.setVisible(false);
            panHori.setVisible(false);
        }
        point.setLocation(myX+deltaX, myY+deltaY);
        //isConnected = false;
        select.repaint();
        
        
    } 
    @Override
    public void mouseExited(MouseEvent e){
        
    }
    @Override
    public void mouseEntered(MouseEvent e){
        
    }
    @Override
    public void mouseReleased(MouseEvent e){
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
                                connectedTo = ((Connector) elem).getParentNode();
                            } else {
                                connectedTo = (Node) elem;
                            }
                            connectedTo.setConnectedTo(this);
                            updateConnectionPosition();
                            isConnected = true;
                            panVert.setVisible(true);
                            panHori.setVisible(true);
                            break;
                        }
                    }
                }
            }
            if(!isConnected){
                if(connectedTo != null){
                    connectedTo.setConnectedTo(null);
                }
                panVert.setVisible(false);
                panHori.setVisible(false);
            }
            select.repaint();
        }
    }
    @Override
    public void mouseClicked(MouseEvent e){
        
    }
    @Override
    public void mouseMoved(MouseEvent e){}
    private boolean fallsInside(Rectangle a, Rectangle b){
        if(a.getBounds().intersects(b.getBounds())){
            return true;
        }
        return false;
    }
    private void connectToPoint(){
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
        }
        else if(pos.equals("Bottom")){
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
            else
                panHori.setBounds(myX, 0, 0, 0);
                
        }
        
    }
}
