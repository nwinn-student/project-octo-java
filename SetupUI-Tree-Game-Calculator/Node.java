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
import java.time.Instant;
import java.util.ArrayList;
/**
 * The Node class has Connector objects attached to it, responding to clicks, 
 * hovers, scrolls, and drags to increase functionality.
 *
 * @author Noah Winn
 * @version 6/3/2024
 */


public class Node extends JPanel implements MouseListener,MouseMotionListener,MouseWheelListener
{
    // instance variables
    private Instant uniqueID = Instant.now();
    private String name = "Enter name here..";
    private String type = "Select type..";
    private Node parentNode = this; // Will only not be this when it has been changed
    private Instant connNodeID = parentNode.getUniqueID();
    private List<String> formulas = null;
    
    private int screenX = 0;
    private int screenY = 0;
    private int myX = 0;
    private int myY = 0;
    
    private double currentZoom = 1;
    private final double maxZoom = 5;
    private final double minZoom = .25;
    private GroupSelector select = null;
    
    private final Border blackBorder = BorderFactory.createLineBorder(Color.BLACK);
    private final Border redBorder = BorderFactory.createLineBorder(Color.RED,3);
    
    private Connector top = null;
    private Connector bottom = null;
    
    private List<Node> childrenNodes = null;
    
    private EditPopupMenu menu = null;
    private ActionManager actions = null;
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
        this.setToolTipText(this.getName());
        
        
    }
    public Instant getUniqueID(){return uniqueID;}
    public void setUniqueID(Instant uniqueID){this.uniqueID = uniqueID;}
    
    public Node getParentNode(){return parentNode;}
    public void setParentNode(Node parentNode){
        if(getDescendants() != null){
            for(Node elem : getDescendants()){
                if(elem.getUniqueID() == parentNode.getUniqueID()){
                    System.out.println("Broken, a loop occurred.");
                    return;
                }
            }
        }
        System.out.println("Parent:"+parentNode);
        this.parentNode = parentNode;
        parentNode.addChild(this);
        // Update stuff?
        this.updateConnections();
    }
    public Instant getConnectedNodeID(){return connNodeID;}
    public void setConnectedNodeID(Instant connNodeID){this.connNodeID = connNodeID;}
    public String getName(){return name;}
    public void setName(String name){
        this.name = name;
        this.setToolTipText(this.getName());
    }
    public String getType(){return type;}
    public void setType(String type){this.type = type;}
    public List<String> getFormulas(){return formulas;}
    public void appendFormulas(String form){formulas.add(form);}
    public void setFormulas(List<String> formulas){this.formulas = formulas;}
    
    public void setPanel(GroupSelector select, EditPopupMenu menu){
        this.select = select;
        this.actions = select.getActions();
        this.menu = menu;
        top = new Connector("Top", select, this);
        top.updatePosition();
        top.updatePosition();
        bottom = new Connector("Bottom", select, this);
        bottom.updatePosition();
        bottom.updatePosition();
            
        select.add(top);
        select.add(bottom);
    }
    public void updateConnectionPosition(){
        top.updateConnectionPosition();
        bottom.updateConnectionPosition();
    }
    public void removeConnections(){
        if(childrenNodes != null){
            for(Node elem : childrenNodes){
                elem.removeConnections();
            }
        }
        top.removeConnections();
        select.remove(top);
        bottom.removeConnections();
        select.remove(bottom);
    }
    public void updateConnections(){
        top.setConnParentNode(this.parentNode);
        bottom.setConnParentNode(this.parentNode);
    }
    public Connector getClosestConnector(Connector conn){
        if(top.getLocation().distance(conn.getLocation()) > 
            bottom.getLocation().distance(conn.getLocation())){
            return bottom;
        } else {
            return top;
        }
    }
    public void addChild(Node child){
        if(childrenNodes == null){
            childrenNodes = new ArrayList<>();
        }
        System.out.println("Added"+child.toString());
        childrenNodes.add(child);
    }
    public void removeChild(Node child){
        if(childrenNodes == null) {return;}
        childrenNodes.remove(child);
    }
    public List<Node> getChildren(){
        return childrenNodes;
    }
    private List<Node> getDescNode(Node node, List<Node> desc){
        if(desc == null){
            desc = new ArrayList<>();
        }
        if(node.getChildren() == null){
            return desc;
        }
        for(Node elem : node.getChildren()){
            //String na = elem.getName();
            desc.add(elem);
            if(elem.getChildren() != null && !elem.getChildren().isEmpty()){
                getDescNode(elem, desc);
            }
        }
        if(desc.size() == 0){
            return desc;
        }
        
        return desc;
    }
    public List<Node> getDescendants(){
        return getDescNode(this, null);
    }
    /**
     * Zooms in and out in accordance with mouse scroll wheel
     */
    public void mouseWheelMoved(MouseWheelEvent e){
        if(e.getWheelRotation() > 0){
            if(currentZoom > minZoom){
                currentZoom -= .05;
            }
        } else {
            if(currentZoom < maxZoom){
                currentZoom += .05;
            }
        }
        this.setSize(getZoomedSize(getX(),getY()));
        top.updatePosition();
        top.updatePosition();
        bottom.updatePosition();
        bottom.updatePosition();
        if(childrenNodes != null){
            for(Node elem : childrenNodes){
                elem.updateConnectionPosition();
            }
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
            top.updatePosition();
            bottom.updatePosition();
            if(childrenNodes != null){
                for(Node elem : childrenNodes){
                    elem.updateConnectionPosition();
                }
            }
            menu.setVisible(false);
            select.repaint();
        }
    } 
    @Override
    public void mouseExited(MouseEvent e){}
    @Override
    public void mouseEntered(MouseEvent e){}
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
                this.setBorder(blackBorder);
                this.setForeground(Color.black);
            }
            else{
                this.setBorder(redBorder);
                this.setForeground(Color.red);
            }
            if(menu != null){
                menu.setChosen(null);
                menu.setVisible(false);
            }
        } else if (e.getButton() == MouseEvent.BUTTON2){
            //System.out.println("Middle button clicked");
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            //System.out.println("Right button clicked");
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
            this.setBorder(redBorder);
            this.setForeground(Color.red);
            menu.setLocation(e.getXOnScreen(), e.getYOnScreen());
            menu.setChosen(this);
            menu.setVisible(true);
        } 
        this.repaint();
    }
    @Override
    public void mouseMoved(MouseEvent e){}
    public void shiftLocation(int deltaX, int deltaY){
        
        this.setLocation(this.myX + deltaX, this.myY + deltaY);
        top.updatePosition();
        bottom.updatePosition();
        if(childrenNodes != null){
            for(Node elem : childrenNodes){
                elem.updateConnectionPosition();
            }
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
    @Override
    public String toString(){
        return "Node["+uniqueID+","+name+","+type+","+connNodeID+","+getX()+","+getY()+","+getWidth()+","+getHeight()+","+formulas+"]";
    }
}