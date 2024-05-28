import java.awt.event.*;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Component;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import java.util.List;
import java.util.Arrays;

/**
 * The Node class has Connector objects attached to it, responding to clicks, 
 * hovers, scrolls, and drags to increase functionality.
 *
 * @author Noah Winn
 * @version 5/25/2024
 */


public class Node extends JPanel implements MouseListener,MouseMotionListener,MouseWheelListener
{
    // instance variables
    private int screenX = 0;
    private int screenY = 0;
    private int myX = 0;
    private int myY = 0;
    
    private double currentZoom = 1;
    private final double maxZoom = 5;
    private final double minZoom = .25;
    private GroupSelector select;
    private final Border blackBorder = BorderFactory.createLineBorder(Color.BLACK);
    private final Border redBorder = BorderFactory.createLineBorder(Color.RED,3);
    
    private Connector top;
    private Connector left;
    private Connector bottom;
    private Connector right;
    
    private Connector connectedTo;
    
    private EditPopupMenu menu;
    /**
     * Constructor for objects of class Panel
     */
    public Node(){
        this.addMouseListener(this);
        
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
        this.setPreferredSize(new Dimension(50,50));
        this.setMaximumSize(new Dimension(5000,5000));
        this.setMinimumSize(new Dimension(25,25));
        this.setBackground(Color.green);
        this.setLayout(null);
        this.setOpaque(true);
        this.setBorder(blackBorder);
        this.setForeground(Color.black);
        this.setBounds(0, 0,50,50);
        this.setVisible(true);
        this.setEnabled(true);
        this.setFocusable(true);
        //this.setName("Panel");
        //this.setRequestFocusEnabled(true);
    }
    public Connector getConnectedTo(){
        return this.connectedTo;
    }
    public void setConnectedTo(Connector connectedTo){
        this.connectedTo = connectedTo;
    }
    public Connector getClosestConnector(Connector conn){
        if(top.getLocation().distance(conn.getLocation()) > 
            bottom.getLocation().distance(conn.getLocation())){
            return bottom;
        } else {
            return top;
        }
    }
    public void setPanel(GroupSelector select, EditPopupMenu menu){
        this.select = select;
        this.menu = menu;
    }
    public void removeConnections(){
        if(connectedTo != null){
            connectedTo.removeConnected();
        }
        if(top != null){
            
            top.removeConnections();
            select.remove(top);
            bottom.removeConnections();
            select.remove(bottom);
            
        }
    }
    /**
     * Zooms in and out in accordance with mouse scroll wheel
     */
    public void mouseWheelMoved(MouseWheelEvent e){
        if(e.getWheelRotation() > 0){
            if(currentZoom > minZoom){
                currentZoom -= .05;
            }
        }
        else{
            if(currentZoom < maxZoom){
                currentZoom += .05;
            }
        }
        this.setSize(getZoomedSize(getX(),getY()));
        if(top != null){
            top.updatePosition();
            top.updatePosition();
            bottom.updatePosition();
            bottom.updatePosition();
        }
        if(connectedTo != null){
            connectedTo.updateConnectionPosition();
        }
        select.repaint();
    }
    public void mousePressed(MouseEvent e){
        screenX = e.getXOnScreen();
        screenY = e.getYOnScreen();
                
        myX = getX();
        myY = getY();
    }
    @Override
    public void mouseDragged(MouseEvent e){
        if(e.getButton() == 0){
            int deltaX = e.getXOnScreen() - screenX;
            int deltaY = e.getYOnScreen() - screenY;
                    
            setLocation(myX + deltaX, myY + deltaY);
            
            
            if(this.getForeground() == Color.red){
                // Move other selected pieces, should this item be selected?
                List<Component> frameElements = Arrays.asList(select.getComponents());
                if(frameElements.size() < 4096){
                    for(Component elem : frameElements){
                        if(elem.getForeground() == Color.red && elem != this){
                            Node p = (Node) elem;
                            p.shiftLocation(deltaX, deltaY);
                        }
                    }
                }
            } else {
                List<Component> frameElements = Arrays.asList(select.getComponents());
                if(frameElements.size() < 4096){
                    for(Component elem : frameElements){
                        if(elem.getClass().equals(Node.class)){
                            Node p = (Node) elem;
                            p.setBorder(blackBorder);
                            elem.setForeground(Color.black);
                        }
                    }
                }
            }
            if(top != null){
                top.updatePosition();
                bottom.updatePosition();
            }
            if(connectedTo != null){
                connectedTo.updateConnectionPosition();
            }
            menu.setVisible(false);
            select.repaint();
        }
    } 
    @Override
    public void mouseExited(MouseEvent e){
        select.repaint();
    }
    @Override
    public void mouseEntered(MouseEvent e){
        if(top == null){
            top = new Connector("Top", select, this);
            top.updatePosition();
            top.updatePosition();
            bottom = new Connector("Bottom", select, this);
            bottom.updatePosition();
            bottom.updatePosition();
            
            select.add(top);
            select.add(bottom);
            select.repaint();
        }
    }
    @Override
    public void mouseReleased(MouseEvent e){
        myX = getX();
        myY = getY();
        if(this.getForeground() == Color.red){
                // Move other selected pieces, should this item be selected?
                List<Component> frameElements = Arrays.asList(select.getComponents());
                if(frameElements.size() < 4096){
                    for(Component elem : frameElements){
                        if(elem.getForeground() == Color.red && elem != this){
                            Node p = (Node) elem;
                            p.updateLocation();
                        }
                    }
                }
            }
    }
    @Override
    public void mouseClicked(MouseEvent e){
        if (e.getButton() == MouseEvent.BUTTON1){
            if(getForeground().equals(Color.red)){
                setBorder(blackBorder);
                this.setForeground(Color.black);
            }
            else{
                setBorder(redBorder);
                this.setForeground(Color.red);
            }
            if(menu != null){
                menu.setVisible(false);
            }
        } else if (e.getButton() == MouseEvent.BUTTON2){
            //System.out.println("Middle button clicked");
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            //System.out.println("Right button clicked");
            int deltaX = e.getXOnScreen() - screenX;
            int deltaY = e.getYOnScreen() - screenY;
                
            menu.setLocation(e.getXOnScreen(), e.getYOnScreen());
            menu.setVisible(true);
        } 
        this.repaint();
    }
    @Override
    public void mouseMoved(MouseEvent e){}
    public void shiftLocation(int deltaX, int deltaY){
        
        this.setLocation(this.myX + deltaX, this.myY + deltaY);
        if(top != null){
            top.updatePosition();
            bottom.updatePosition();
        }
        if(connectedTo != null){
            connectedTo.updateConnectionPosition();
        }
        menu.setVisible(false);
    }
    public void updateLocation(){
        myX = getX();
        myY = getY();
    }
    private Dimension getZoomedSize(int x,int y) {
        this.setVisible(false);
        this.setVisible(true);
        this.setLocation(x, y);
        return new Dimension((int)(currentZoom * 50), (int)(currentZoom * 50));
    }
    public void updateZoom(){
        currentZoom = this.getSize().getHeight()/50;
    }
}
