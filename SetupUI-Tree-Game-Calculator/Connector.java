import java.awt.event.*;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Container;
import java.awt.Component;
import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JViewport;
import java.awt.Point;
import javax.swing.SwingUtilities;
import java.awt.Graphics2D;
import java.awt.KeyboardFocusManager;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.JPopupMenu;

/**
 * Write a description of class Connector here.
 *
 * @author Noah Winn
 * @version 5/22/2024
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
    private String pos;
    
    private Border blackBorder = BorderFactory.createLineBorder(Color.BLACK);
    private Border redBorder = BorderFactory.createLineBorder(Color.RED,3);
    private JPanel panVert;
    private JPanel panHori;
    private boolean isConnected = false;
    
    //private KeyboardFocusManager focusManager =
    //        KeyboardFocusManager.getCurrentKeyboardFocusManager();
    /**
     * Constructor for objects of class Panel
     */
    public Connector(){
        //Source: https://stackoverflow.com/questions/874360
        addMouseListener(this);
        
        addMouseMotionListener(this);
        this.setBackground(Color.black);
        this.setLayout(null);
        this.setBorder(blackBorder);
        this.setForeground(Color.black);
        this.setBounds(0, 0,5,5);
        this.setVisible(true);
        //this.setName("Connector");
        //this.setRequestFocusEnabled(true);
    }
    public void setPosition(String pos, GroupSelector select, Panel pan){
        this.pos = pos;
        this.select = select;
        int height = (int) pan.getSize().getHeight();
        int width = (int) pan.getSize().getWidth();
        int positionX = (int) pan.getLocation().getX();
        int positionY = (int) pan.getLocation().getY();

        int curHeight = (int) this.getSize().getHeight();
        int curWidth = (int) this.getSize().getWidth();
        if(pos.equals("Top")){
            this.setBounds((width-curWidth)/2 + positionX,positionY-height/10, height/10, width/10);
        }
        else if(pos.equals("Left")){
            //this.setBounds(positionX-width/10,(height-curHeight)/2 + positionY, height/10, width/10);
        }
        else if(pos.equals("Right")){
            //this.setBounds(width-curWidth + positionX + width/10,(height-curHeight)/2 + positionY, height/10, width/10);
        }
        else if(pos.equals("Bottom")){
            this.setBounds((width-curWidth)/2 + positionX,height-curHeight + positionY+height/10, height/10, width/10);
        }
        //this.setBounds(height, width, 5,5);
        select.repaint();
        //this.repaint();
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
        }
        
        panVert.setLayout(null);
        panVert.setBackground(Color.blue);
        panVert.setBounds(myX, myY,10,10);
        panVert.setVisible(true);
        panHori.setLayout(null);
        panHori.setBackground(Color.blue);
        panHori.setBounds(myX, myY,10,10);
        panHori.setVisible(true);
        
        select.add(panVert);
        select.add(panHori);
        select.repaint();
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
        if(!isConnected){
            // Look at distance, if too far away then remove
            // SNAP TO the nearest panel connector within x distance
            Component[] c = select.getComponents();
            for(int i=0; i < c.length; i++){
                    //not done!! work on later
                    if(c[i].getClass().equals(Panel.class) || c[i].getClass().equals(Connector.class)){
                        if(fallsInside(this.getBounds(),c[i].getBounds())){
                            //Change foreground and border back to black
                            JPanel p = (JPanel) c[i];
                            p.setBorder(redBorder);
                            p.setForeground(Color.red);
                            isConnected = true;
                            break;
                        }
                        else{
                            JPanel p = (JPanel) c[i];
                            p.setBorder(blackBorder);
                            p.setForeground(Color.black);
                            this.removeConnections();
                        }
                    }
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
    
}
