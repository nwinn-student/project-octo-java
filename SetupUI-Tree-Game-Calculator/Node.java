import java.awt.event.*;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Container;
import java.awt.Component;
import javax.swing.border.Border;
import javax.swing.BorderFactory;

/**
 * The Node class has Connector objects attached to it, responding to clicks, 
 * hovers, scrolls, and drags to increase functionality.
 *
 * @author Noah Winn
 * @version 5/24/2024
 */


public class Node extends JPanel implements MouseListener,MouseMotionListener,MouseWheelListener, FocusListener
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
    
    private EditPopupMenu menu;
    //private KeyboardFocusManager focusManager =
    //        KeyboardFocusManager.getCurrentKeyboardFocusManager();
    /**
     * Constructor for objects of class Panel
     */
    public Node(){
        //Source: https://stackoverflow.com/questions/874360
        addMouseListener(this);
        
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        addFocusListener(this);
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
    public void setPanel(GroupSelector select, EditPopupMenu menu){
        this.select = select;
        this.menu = menu;
    }
    public void removeConnections(){
        if(top != null){
            top.removeConnections();
            select.remove(top);
            bottom.removeConnections();
            select.remove(bottom);
            //select.remove(top);
            //select.remove(top);
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
        //System.out.println(e.getX());
        this.setSize(getZoomedSize(getX(),getY()));
        // Change getX to be the center and getY to be the center
        //int x = getDistanceFromCenter("X", e.getX());
        //int y = getDistanceFromCenter("Y", e.getY());
        //int x = getX() - /20;
        //int y = getY() - e.getY()/20;
        //this.setLocation(x, y);
        if(top != null){
            top.setPosition("Top", select, this);
            top.setPosition("Top", select, this);
            //left.setPosition("Left", select, this);
            bottom.setPosition("Bottom", select, this);
            bottom.setPosition("Bottom", select, this);
            //right.setPosition("Right", select, this);
        }
        select.repaint();
    }
    public void mousePressed(MouseEvent e){
        screenX = e.getXOnScreen();
        screenY = e.getYOnScreen();
                
        myX = getX();
        myY = getY();
        
        // SELECT THE NODE PRESSED -- STOPPED HERE
        //selector = new JPanel();
        //selector.setBackground(Color.blue);
        //selector.setBounds(myX,myY,20,20);
        //selector.setOpaque(true);
        //this.add(selector);
        //this.requestFocusInWindow();
    }
    @Override
    public void mouseDragged(MouseEvent e){
        if(e.getButton() == 0){
            int deltaX = e.getXOnScreen() - screenX;
            int deltaY = e.getYOnScreen() - screenY;
                    
            setLocation(myX + deltaX, myY + deltaY);
            
            // Move other selected pieces, should this item be selected?
            if(top != null){
                top.setPosition("Top", select, this);
                //left.setPosition("Left", select, this);
                bottom.setPosition("Bottom", select, this);
                //right.setPosition("Right", select, this);
            }
            menu.setVisible(false);
            select.repaint();
        }
        
    } 
    @Override
    public void mouseExited(MouseEvent e){
        //select.remove(top);
        //select.remove(left);
        //select.remove(bottom);
        //select.remove(right);
        select.repaint();
        
    }
    @Override
    public void mouseEntered(MouseEvent e){
        if(top == null){
            top = new Connector();
            top.setPosition("Top", select, this);
            top.setPosition("Top", select, this);
            //left = new Connector();
            bottom = new Connector();
            bottom.setPosition("Bottom", select, this);
            bottom.setPosition("Bottom", select, this);
            //right = new Connector();
            
            select.add(top);
            //select.add(left);
            select.add(bottom);
            //select.add(right);
            select.repaint();
        }
        //top.setPosition("Top", select, this);
        //left.setPosition("Left", select, this);
        //bottom.setPosition("Bottom", select, this);
        //right.setPosition("Right", select, this);
        
        //select.add(top);
        //select.add(left);
        //select.add(bottom);
        //select.add(right);
        //select.repaint();
    }
    @Override
    public void mouseReleased(MouseEvent e){}
    @Override
    public void mouseClicked(MouseEvent e){
        if (e.getButton() == MouseEvent.BUTTON1){
            //System.out.println("Left button clicked");
            //System.out.println(getForeground().equals(Color.red));
            // make sure it is left click
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
    @Override
    public void focusLost(FocusEvent e){}
    @Override
    public void focusGained(FocusEvent e){}
    
    private Dimension getZoomedSize(int x,int y) {
        
        this.setVisible(false);
        this.setVisible(true);
        this.setLocation(x, y);
        return new Dimension((int)(currentZoom * 50), (int)(currentZoom * 50));
    }
    private int getDistanceFromCenter(String type, int position){
        if(type.equals("X")){
            //System.out.println(getCenter(type)+" "+position);
            return getCenter(type) - position/2;
        }
        else if(type.equals("Y")){
            return getCenter(type) - position/2;
        }
        return 0;
    }
    private int getCenter(String type){
        if(type.equals("X")){
            return (int)this.getBounds().getCenterX();
        }
        else if(type.equals("Y")){
            return (int)this.getBounds().getCenterY();
        }
        return 0;
    }
    public void updateZoom(){
        
        currentZoom = this.getSize().getHeight()/50;
    }
}
