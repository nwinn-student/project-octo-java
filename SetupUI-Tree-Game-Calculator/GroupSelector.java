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

/**
 * Write a description of class GroupSelector here.
 *
 * @author Noah Winn
 * @version 5/23/2024
 */


public class GroupSelector extends JPanel implements MouseListener,MouseMotionListener
{
    // instance variables
    private int screenX = 0;
    private int screenY = 0;
    private int myX = 0;
    private int myY = 0;
    
    private Frame fram;
    private boolean isHighlighted = false;
    private JPanel selector;
    private Border blackBorder = BorderFactory.createLineBorder(Color.BLACK);
    private Border redBorder = BorderFactory.createLineBorder(Color.RED,3);
    
    private JPanel rect;
    //private KeyboardFocusManager focusManager =
    //        KeyboardFocusManager.getCurrentKeyboardFocusManager();
    /**
     * Constructor for objects of class Panel
     */
    public GroupSelector(Frame fram){
        this.fram = fram;
        
        //Source: https://stackoverflow.com/questions/874360
        addMouseListener(this);
        
        addMouseMotionListener(this);
        
        this.setLayout(null);
        this.setOpaque(true);
        this.setPreferredSize(new Dimension(500,500));
        
        this.setLocation(0,0);
        System.out.println(getX());
        System.out.println(getY());
        //this.setRequestFocusEnabled(true);
    }
    
    public void mousePressed(MouseEvent e){
        screenX = e.getXOnScreen();
        screenY = e.getYOnScreen();
                
        myX = e.getX();
        myY = e.getY();
        
        //Start Rectangle
        rect = new JPanel();
        rect.setBounds(myX,myY,1, 1);
        rect.setBackground(new Color(255,0,0,125));
        rect.setBorder(redBorder);
        
        this.add(rect);
        fram.repaint();
    }
    @Override
    public void mouseDragged(MouseEvent e){
        int x = myX-e.getX();
        int y = myY-e.getY();
        //Setup Rectangle, fr so cool
        if(x < 0 && y < 0){
            rect.setBounds(myX,myY,Math.abs(x), Math.abs(y));
        }
        if(x < 0 && y > 0){
            rect.setBounds(myX,myY-y,Math.abs(x), y);
        }
        if(x > 0 && y < 0){
            rect.setBounds(myX-x,myY,x, Math.abs(y));
        }
        if(x > 0 && y > 0){
            rect.setBounds(myX-x,myY-y,x, y);
        }
        //Highlight all within rectangle
        Component[] c = this.getComponents();
        for(int i=0; i < c.length; i++){
            if(c[i] != rect){
                if(c[i].getClass().equals(Panel.class)){
                    if(fallsInside(rect.getBounds(),c[i].getBounds())){
                        //Change foreground and border back to black
                        JPanel p = (JPanel) c[i];
                        p.setBorder(redBorder);
                        p.setForeground(Color.red);
                    }
                    else{
                        JPanel p = (JPanel) c[i];
                        p.setBorder(blackBorder);
                        p.setForeground(Color.black);
                    }
                }
            }
        }
        
        //System.out.println("Added JPanel");
        //System.out.println(rect);
        fram.repaint();
    } 
    @Override
    public void mouseExited(MouseEvent e){}
    @Override
    public void mouseEntered(MouseEvent e){}
    @Override
    public void mouseReleased(MouseEvent e){
        this.remove(rect);
        if(e.getButton() == MouseEvent.BUTTON2){
            // Right click them all.
        }
        fram.repaint();
    }
    @Override
    public void mouseClicked(MouseEvent e){
        //if(isHighlighted) setBorder(blackBorder);
        //else setBorder(redBorder);
        //isHighlighted=!isHighlighted;
        if (e.getButton() == MouseEvent.BUTTON1){
            Component[] c = this.getComponents();
            for(int i=0; i < c.length; i++){
                //System.out.println(Panel.class);
                if(c[i].getClass().equals(Panel.class)){
                    Panel p = (Panel) c[i];
                    p.setBorder(blackBorder);
                    p.setForeground(Color.black);
                }
                
            }
        }
        fram.repaint();
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
