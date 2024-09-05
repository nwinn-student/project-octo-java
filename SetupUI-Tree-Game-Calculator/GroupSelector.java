import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Component;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import java.util.List;
import java.util.Arrays;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * The GroupSelector class has JPanel objects attached to it, responding to clicks 
 * and drags to increase functionality.
 * The main use of the GroupSelector class is to store and "select" a collection
 * of JPanel type objects in order to manipulate or hold them.
 *
 * @author Noah Winn
 * @version 9/4/2024
 */
public class GroupSelector extends JPanel implements MouseListener,MouseMotionListener, KeyListener{
    // instance variables
    private int screenX = 0;
    private int screenY = 0;
    private int myX = 0;
    private int myY = 0;
    
    private Frame fram = null;
    private EditPopupMenu menu = null;
    private ActionManager actions = null;
    
    private boolean isHighlighted = false;
    private Border blackBorder = BorderFactory.createLineBorder(Color.BLACK);
    private Border redBorder = BorderFactory.createLineBorder(Color.RED,3);
    
    private JPanel rect = null;
    private List<Node> selectedNodes = new ArrayList<>();
    private List<Node> nodes = new ArrayList<>();
    private Integer nodeDefaultIndex = 0;
    /**
     * Constructor for objects of class GroupSelector
     * 
     * @param fram, the Frame object the GroupSelector object should be set up for
     * @param menu, the EditPopupMenu object the GroupSelector object should moderate
     */
    public GroupSelector(Frame fram, EditPopupMenu menu){
        super();
        this.fram = fram;
        this.actions = fram.getActions();
        this.menu = menu;
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        this.setLayout(null);
        this.setOpaque(true);
        this.setPreferredSize(new Dimension(500,500));
        if(rect == null){
            rect = new JPanel();
            this.add(rect);
        }
        this.setLocation(0,0);
        this.setFocusable(true);
        menu.createPopupMenu(this, actions);
    }
    public Integer getNodeIndex(){
        return nodeDefaultIndex;
    }
    public void incrementNodeIndex(){
        nodeDefaultIndex++;
        if(nodeDefaultIndex > Math.pow(2,15)) nodeDefaultIndex = 0;
    }
    public List<Node> getSelected(){
        if(selectedNodes.size() > 4096){
            selectedNodes.clear(); //?
            return selectedNodes;
        } else{
            return selectedNodes;
        }
    }
    public void addSelected(Node node){
        if(selectedNodes.size() > 4096) return;
        node.setForeground(Color.red);
        node.setBorder(redBorder);
        selectedNodes.add(node);
    }
    public void removeSelected(Node node){
        node.setForeground(Color.black);
        node.setBorder(blackBorder);
        selectedNodes.remove(node);
    }
    public void clearSelected(){
        for(Node elem : selectedNodes){
            elem.setForeground(Color.black);
            elem.setBorder(blackBorder);
        }
        selectedNodes.clear();
    }
    public void sweepSelected(){
        // clears selected and removes the nodes
        for(Node elem : selectedNodes){
            remove(elem);
        }
        selectedNodes.clear();
    }
    public List<Node> getNodes(){
        return nodes;
    }
    public Component add(Node node){
        nodes.add(node);
        return super.add(node);
    }
    public void remove(Node node){
        nodes.remove(node);
        node.removeConnections();
        // expected for node to be removed from selected
        super.remove(node);
    }
    public void clearNodes(){
        for(Node node : nodes){
            node.removeConnections();
        }
        nodes.clear();
    }
    /**
     * Retrieves the ActionManager object, which is used for undoing and redoing actions.
     * @return the actionManager from the parent frame
     */
    public ActionManager getActions(){return actions;}
    /**
     * Displays a box for selecting nodes.
     */
    @Override
    public void mousePressed(MouseEvent e){
        screenX = e.getXOnScreen();
        screenY = e.getYOnScreen();
                
        myX = e.getX();
        myY = e.getY();
        
        //Start Rectangle
        rect.setBounds(myX,myY,1, 1);
        rect.setBackground(new Color(255,0,0,125));
        rect.setBorder(redBorder);
        rect.setVisible(true);
        this.repaint();
    }
    /**
     * Increases the size of the box from initial click position to current click
     * selecting all nodes within.
     */
    @Override
    public void mouseDragged(MouseEvent e){
         // Middle Click and any combination of clicks other than left xor right
        if(e.getModifiersEx() != 4096 && e.getModifiersEx() != 1024){
            rect.setVisible(false);
            return;
        }
        int x = myX-e.getX();
        int y = myY-e.getY();
        //Setup Rectangle, fr so cool, issue if y or x is 0, idk if this is fine
        if(x <= 0 && y <= 0){
            rect.setBounds(myX,myY,Math.abs(x), Math.abs(y));
        } else if(x < 0 && y > 0){
            rect.setBounds(myX,myY-y,Math.abs(x), y);
        } else if(x > 0 && y < 0){
            rect.setBounds(myX-x,myY,x, Math.abs(y));
        } else if(x > 0 && y > 0){
            rect.setBounds(myX-x,myY-y,x, y);
        }
        //Highlight all nodes within rectangle
        for(Node elem : nodes){
            if(fallsInside(rect.getBounds(), elem.getBounds())){
                addSelected(elem);
            } else {
                removeSelected(elem);
            }
        }
        menu.setVisible(false);
        //this.repaint(); // maybe useless
    }
    @Override
    public void mouseExited(MouseEvent e){}
    @Override
    public void mouseEntered(MouseEvent e){}
    /**
     * Hides the selector box OR shows a popup menu.
     */
    @Override
    public void mouseReleased(MouseEvent e){
        rect.setVisible(false);
        if(e.getButton() == MouseEvent.BUTTON3){
            // Right click them all.
            menu.setLocation(e.getXOnScreen(), e.getYOnScreen());
            menu.setVisible(true);
        }
        this.repaint();
    }
    /**
     * Refreshes the popup menu and the selected elements.
     */
    @Override
    public void mouseClicked(MouseEvent e){
        if (e.getButton() == MouseEvent.BUTTON1){
            clearSelected();
            menu.setVisible(false);
        }
        if(e.getButton() == MouseEvent.BUTTON3){            
            clearSelected();
            menu.setLocation(e.getXOnScreen(), e.getYOnScreen());
            menu.setVisible(true);
        }
        menu.setChosen(null);
        this.repaint();
    }
    @Override
    public void mouseMoved(MouseEvent e){}
    @Override
    public void keyReleased(KeyEvent e){
        // check if a node is selected, if not then cycle through the nodes, and instead call keyReleased on Node
        // tab should be the key used to cycle, but I will use arrow keys (it'll go to closest and highlight it some color)
        // if a node is selected, then arrow keys can move the nodes around
        if(e.getKeyCode() == 40){
            // down
            if(selectedNodes.size() > 0){
                
            } else {
                // node below
            }
        } else if(e.getKeyCode() == 39){
            //right
        } else if(e.getKeyCode() == 38){
            //up
        } else if(e.getKeyCode() == 37){
            //left   
        } else if(e.getKeyCode() == 10){
            // select the current node, pressed "Enter"
        }
        //System.out.println(e);
        //System.out.println(selectedNodes);
    }
    @Override
    public void keyPressed(KeyEvent e){}
    @Override
    public void keyTyped(KeyEvent e){}
    /**
     * Whether a rectangle intersects with another rectangle, order doesn't matter.
     * @param a, the first Rectangle
     * @param b, the second Rectangle
     * @returns boolean, whether the Rectangles are connected or not.
     */
    private boolean fallsInside(Rectangle a, Rectangle b){
        return a.getBounds().intersects(b.getBounds());
    }
}
