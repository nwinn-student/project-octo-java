import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;
import java.awt.Event;
import java.awt.event.KeyEvent;
import java.awt.Component;
import java.awt.Color;
import java.io.PrintWriter;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import java.util.Scanner;
import java.io.File;
import java.util.List;
import java.util.Arrays;
import javax.swing.JInternalFrame;
import java.time.Instant;
import java.util.ArrayList;

/**
 * The EditPopupMenu class's purpose is to create a JPopupMenu, a small
 * window, that appears when a user right-clicks within the bounds of the 
 * GroupSelector object.
 * 
 * The popup menu consists of multiple buttons used to interact with the Node
 * objects, such as Edit View, Duplicate, Copy, Cut, Paste, and Remove Nodes.
 * Edit View shows an EditView object that allows the properties of the nodes
 * to be altered, whereas duplicate doubles the amount of selected nodes, copy
 * places a stringified version of the nodes into a clipboard, cut does the 
 * same as copy but also removes the nodes.  Paste calls upon the clipboard and
 * creates nodes, with remove nodes, well, removing the nodes.
 * 
 *
 * @author Noah Winn
 * @version 9/5/2024
 */
public class EditPopupMenu extends JPopupMenu implements ActionListener
{
    private JPopupMenu menuBar = null;
    private GroupSelector pan = null;
    private ActionManager actions = null;
    private Border blackBorder = BorderFactory.createLineBorder(Color.BLACK);
    private EditView edit = null;
    private Node edittedNode = null;
    /**
     * Constructor for objects of class EditPopupMenu
     */
    public EditPopupMenu(){}
    /**
     * Called by GroupSelector to initialize.
     */
    public void createPopupMenu(GroupSelector pan, ActionManager actions){
        this.pan = pan;
        this.actions = actions;
        addMenuItem("Edit View", this, 0);
        this.addSeparator();
        addMenuItem("Duplicate", this, KeyEvent.VK_D);
        addMenuItem("Copy", this, KeyEvent.VK_C); // CTRL C
        addMenuItem("Cut", this, KeyEvent.VK_X); // CTRL X
        addMenuItem("Paste", this, KeyEvent.VK_V); // CTRL V
        this.addSeparator();
        addMenuItem("Remove Nodes", this, KeyEvent.VK_DELETE);
        this.setVisible(false);
    }
    /**
     * Used to make JMenuItems easier to create.
     * 
     * @param title, the name of the JMenuItem object to be created
     * @param parent, the JPopupMenu to add the JMenuItem object to
     * @param key, the keyboard key necessary to activate the button w/o clicking
     */
    private void addMenuItem(String title, JPopupMenu parent, int key){
        JMenuItem menuItem = new JMenuItem(title);
        if(key != 0){
            if(title.equals("Remove Nodes")){
               menuItem.setAccelerator(KeyStroke.getKeyStroke(key, 0));
            }
            else{
                 //Key is not undefined, will always require CTRL
                menuItem.setAccelerator(KeyStroke.getKeyStroke(key, Event.CTRL_MASK));
            }
        }
        menuItem.addActionListener(this);
        parent.add(menuItem);
    }
    /**
     * @param edittedNode, the node that the EditView object will look at
     */
    public void setChosen(Node edittedNode){
        this.edittedNode = edittedNode;
    }
    /**
     * The actions attached via buttons all perform their own action.
     * 
     * @param e, the ActionEvent performed
     */
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand() == "Edit View"){
            if(edittedNode == null){
                return;
            }
            if(edit == null){
                edit = new EditView(pan);
                pan.add(edit);
            }
            edit.editNode(edittedNode);
            edit.resetLocation();
            edit.setVisible(true);
        }
        if(e.getActionCommand() == "Duplicate"){
            List<String> cElements = new ArrayList<>();
            for(Node elem : pan.getSelected()){
                Node copy = new Node();
                copy.setBounds(elem.getBounds());
                int shiftX = copy.getX() + (int)copy.getSize().getWidth()/5;
                int shiftY = copy.getY() + (int)copy.getSize().getHeight()/5;
                copy.setLocation(shiftX, shiftY);
                // may need to do more here since they bunch up when dragged instead of moving regularly, 
                copy.setPanel(pan, this);
                copy.updateZoom();
                copy.setName("#"+pan.getNodeIndex().toString());
                pan.incrementNodeIndex();
                cElements.add(copy.toString());
                pan.add(copy);
            }
            pan.clearSelected();
            if(!cElements.isEmpty())
                actions.addUndoableAction("MKS"+cElements);
            pan.repaint();
        }
        if(e.getActionCommand() == "Copy"){
            //Clear clipboard.txt
            try{
                PrintWriter out = new PrintWriter("clipboard.txt");
                // Don't want to take up too much memory
                out.println(pan.getSelected());
                out.close();
                pan.clearSelected();
                pan.repaint();
            }
            catch(Exception a){}
        }
        if(e.getActionCommand() == "Cut"){
            try{
                PrintWriter out = new PrintWriter("clipboard.txt");
                out.println(pan.getSelected());
                out.close();
                if(!pan.getSelected().isEmpty())
                    actions.addUndoableAction("DLM"+pan.getSelected());
                pan.sweepSelected();
                pan.repaint();
            }
            catch(Exception a){}
        }
        if(e.getActionCommand() == "Paste"){
            try{
                Scanner scan = new Scanner(new File("clipboard.txt"));
                List<Node> connCheck = new ArrayList<>();
                while(scan.hasNextLine()){
                    String line = scan.nextLine();
                    char[] field = line.toCharArray();
                    int i;
                    for(i = 0; i < field.length; i++){
                        if(field[i] == '['){
                            break;
                        }
                    }
                    // EXPECT EITHER "Panel" or "Connector"
                    if(line.substring(0,i).equals("Node")){
                        Node p = new Node();
                        p.setPanel(pan, this);
                        line = line.substring(i+1, line.length()-1);
                        
                        String[] crd = line.split(",");
                        p.setUniqueID(Instant.parse(crd[0]));
                        p.setName(crd[1]);
                        // Look through current elements to see if any have the same ID
                        List<Component> frameElements = Arrays.asList(pan.getComponents());
                        if(!Instant.parse(crd[0]).equals(Instant.parse(crd[3])) 
                            && frameElements.size() < 4096) {
                            boolean none = true;
                            for(Node elem : pan.getNodes()){
                                if(elem.getUniqueID().equals(Instant.parse(crd[3]))){
                                    none=false;
                                    elem.setParentNode(elem);
                                }
                            }
                            if(none){
                                p.setConnectedNodeID(Instant.parse(crd[3]));
                                connCheck.add(p);
                            }
                        }
                        
                        p.setBounds(
                            Integer.parseInt(crd[4]),Integer.parseInt(crd[5]),
                            Integer.parseInt(crd[6]),Integer.parseInt(crd[7])
                            );
                        //Formulas...
                        
                        
                        //Shift
                        int shiftX = p.getX() + (int)p.getSize().getWidth()/5;
                        int shiftY = p.getY() + (int)p.getSize().getHeight()/5;
                        p.setLocation(shiftX, shiftY);
                        p.updateZoom();
                        p.updateConnectionPosition();
                        pan.add(p);
                    }
                    else if(line.substring(0,i).equals("Connector")){
                        
                    }
                }
                scan.close();
                for(Node nod : connCheck){
                    for(Node elem : pan.getNodes()){
                        if(elem.getUniqueID().equals(nod.getConnectedNodeID())){
                            nod.setParentNode(elem);
                        }
                    }
                }
                pan.repaint();
                // add Action.
            }
            catch(Exception a){
                System.out.println(a);
            }
        }
        if(e.getActionCommand() == "Remove Nodes"){
            //DELETES selected nodes
            if(!pan.getSelected().isEmpty())
                actions.addUndoableAction("DLM"+pan.getSelected());
            pan.sweepSelected();
            pan.repaint();
        }
        this.setVisible(false);
    }
}
