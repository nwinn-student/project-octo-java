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
 * Write a description of class EditPopupMenu here.
 *
 * @author Noah Winn
 * @version 6/2/2024
 */
public class EditPopupMenu extends JPopupMenu implements ActionListener
{
    private JPopupMenu menuBar = null;
    private GroupSelector pan = null;
    private Border blackBorder = BorderFactory.createLineBorder(Color.BLACK);
    private EditView edit = null;
    private Node edittedNode = null;
    /**
     * Constructor for objects of class EditPopupMenu
     */
    public EditPopupMenu(){}
    
    public void createPopupMenu(GroupSelector pan){
        this.pan = pan;
        
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
    public void setChosen(Node edittedNode){
        this.edittedNode = edittedNode;
    }
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
            List<Component> frameElements = Arrays.asList(pan.getComponents());
            if(frameElements.size() < 4096){
                for(Component elem : frameElements){
                    if(elem.getForeground() == Color.red){
                        elem.setForeground(Color.black);
                        Node p = (Node) elem;
                        p.setBorder(blackBorder);
                        Node copy = new Node();
                        copy.setBounds(p.getBounds());
                        int shiftX = copy.getX() + (int)copy.getSize().getWidth()/5;
                        int shiftY = copy.getY() + (int)copy.getSize().getHeight()/5;
                        copy.setLocation(shiftX, shiftY);
                        copy.setPanel(pan, this);
                        copy.updateZoom();
                        pan.add(copy);
                    }
                }
            }
            pan.repaint();
        }
        if(e.getActionCommand() == "Copy"){
            List<Component> frameElements = Arrays.asList(pan.getComponents());
            //Clear clipboard.txt
            try{
                PrintWriter out = new PrintWriter("clipboard.txt");
                // Don't want to take up too much memory
                if(frameElements.size() < 4096){
                    for(Component elem : frameElements){
                        if(elem.getForeground() == Color.red){
                            Node p = (Node) elem;
                            p.setBorder(blackBorder);
                            p.setForeground(Color.black);
                            out.println(elem);
                        }
                    }
                }
                pan.repaint();
                out.close();
            }
            catch(Exception a){}
        }
        if(e.getActionCommand() == "Cut"){
            List<Component> frameElements = Arrays.asList(pan.getComponents());
            //Clear clipboard.txt
            try{
                PrintWriter out = new PrintWriter("clipboard.txt");
                if(frameElements.size() < 4096){
                    for(Component elem : frameElements){
                        if(elem.getForeground() == Color.red){
                            Node p = (Node) elem;
                            p.setBorder(blackBorder);
                            p.setForeground(Color.black);
                            out.println(elem);
                            p.removeConnections();
                            pan.remove(elem);
                        }
                    }
                }
                pan.repaint();
                out.close();
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
                        //System.out.println(line);
                        
                        String[] crd = line.split(",");
                        p.setUniqueID(Instant.parse(crd[0]));
                        p.setName(crd[1]);
                        // Look through current elements to see if any have the same ID
                        List<Component> frameElements = Arrays.asList(pan.getComponents());
                        if(!Instant.parse(crd[0]).equals(Instant.parse(crd[3])) 
                            && frameElements.size() < 4096) {
                            boolean none = true;
                            for(Component elem : frameElements){
                                if(elem.getClass().equals(Node.class)){
                                    if(((Node)elem).getUniqueID().equals(Instant.parse(crd[3]))){
                                        none = false;
                                        p.setParentNode((Node)elem);
                                    }
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
                        pan.add(p);
                    }
                    else if(line.substring(0,i).equals("Connector")){
                        
                    }
                }
                scan.close();
                for(Node nod : connCheck){
                    List<Component> frameElements = Arrays.asList(pan.getComponents());
                    for(Component elem : frameElements){
                        if(elem.getClass().equals(Node.class)){
                            Node z = (Node)elem;
                            if(z.getUniqueID().equals(nod.getConnectedNodeID())){
                                nod.setParentNode(z);
                            }
                        }
                    }
                }
                pan.repaint();
                
            }
            catch(Exception a){
                
            }
        }
        if(e.getActionCommand() == "Remove Nodes"){
            //DELETES selected nodes
            List<Component> frameElements = Arrays.asList(pan.getComponents());
            for(Component elem : frameElements){
                if(elem.getForeground() == Color.red){
                    Node p = (Node) elem;
                    p.removeConnections();
                    pan.remove(elem);
                }
            }
            pan.repaint();
        }
        this.setVisible(false);
    }
}
