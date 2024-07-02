
/**
 * The ToolBar class sets up a collection of buttons, such as
 * copy, cut, create, delete, and paste, that can be used to directly impact
 * the screen.
 *
 * @author Noah Winn
 * @version 6/25/2024
 */
import javax.swing.JToolBar;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JComponent;
import java.awt.Component;
import java.awt.Color;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.File;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.time.Instant;
public class ToolBar implements ActionListener{
    private JToolBar toolBar = null;
    private Frame fram = null;
    private GroupSelector pan = null;
    private EditPopupMenu menu = null;
    private ActionManager actions = null;
    private Integer nodeIndex = 0;
    private Border blackBorder = BorderFactory.createLineBorder(Color.BLACK);

    private JButton createNode = null;
    /**
     * Constructor for objects of class ToolBar
     */
    public ToolBar(){}
    
    public void createToolBar(Frame fram, GroupSelector pan, EditPopupMenu menu){
        this.fram = fram;
        this.actions = fram.getActions();
        this.pan = pan;
        this.menu = menu;
        toolBar = new JToolBar("Toolbar");
        addButton("Create Node", toolBar, "Spawns in a new node at 0,0.");
        addButton("Edit Node", toolBar, "..."); // no clue yet
        addButton("Delete Node", toolBar, "Use Delete to use Delete Node, removes all of the currently selected nodes.");
        addButton("Run", toolBar, "..."); // Displays whether or not the inputs will work with Tree-Calculator
        toolBar.addSeparator();
        addButton("Copy", toolBar, "Use Control-C to use Copy, copies the currently selected Nodes.");
        addButton("Cut",toolBar, "Use Control-X to use Cut, copies and removes the currently selected Nodes.");
        addButton("Paste", toolBar, "Use Control-V to use Paste, pastes in the previously copied or cut nodes.");
        toolBar.getAccessibleContext().setAccessibleName("Tool Bar");
        fram.add(toolBar, BorderLayout.PAGE_START);
    }
    /**
     * Used to make JButtons easier to create.
     * 
     * @param title, the name of the JButton object to be created
     * @param parent, the JToolBar to add the JButton object to
     * @param key, the keyboard key necessary to activate the button w/o clicking
     * @param description, for screen readers to provide extra information
     */
    public void addButton(String name, JToolBar parent, String description){
        JButton button = new JButton(name);
        button.addActionListener(this);
        button.setFocusable(false);
        button.getAccessibleContext().setAccessibleName(name);
        button.getAccessibleContext().setAccessibleDescription(description);
        parent.add(button);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        menu.setVisible(false);
        if(e.getActionCommand() == "Create Node"){
            Node p = new Node();
            p.setPanel(pan, menu);
            p.setName(nodeIndex.toString());
            nodeIndex++;
            actions.addUndoAbleAction("MKS"+p.toString());
            pan.add(p);
            pan.repaint();
        } else if (e.getActionCommand() == "Connect Nodes"){
            //CONNECTS selected nodes, remove?
            // Was originally thinking of having the user select a node, 
            // then press connect nodes and have the next selection become connected
            List<Component> frameElements = Arrays.asList(pan.getComponents());
            List<Component> cElements = new ArrayList<>();
            if(frameElements.size() < 4096){
                for(Component elem : frameElements){
                    if(elem.getForeground() == Color.red){
                        //Selected nodes have been obtained now connect them all, prob not this loop tho
                        cElements.add(elem);
                    }
                }
                //actions.addUndoAbleAction("CNM"+cElements.toString());
            }
            
            pan.repaint();
        } else if (e.getActionCommand() == "Delete Node"){
            List<Component> frameElements = Arrays.asList(pan.getComponents());
            List<String> cElements = new ArrayList<>();
            for(Component elem : frameElements){
                if(elem.getForeground() == Color.red){
                    Node p = (Node) elem;
                    cElements.add(p.toString());
                }
            }
            for(Component elem : frameElements){
                if(elem.getForeground() == Color.red){
                    ((Node)elem).removeConnections();
                    pan.remove(elem);
                }
            }
            if(!cElements.isEmpty())
                actions.addUndoAbleAction("DLM"+cElements);
            pan.repaint();
        }
        else if(e.getActionCommand() == "Copy"){
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
        else if(e.getActionCommand() == "Cut"){
            List<Component> frameElements = Arrays.asList(pan.getComponents());
            List<String> cElements = new ArrayList<>();
            try{
                PrintWriter out = new PrintWriter("clipboard.txt");
                if(frameElements.size() < 4096){
                    for(Component elem : frameElements){
                        if(elem.getForeground() == Color.red){
                            cElements.add(((Node)elem).toString());
                            out.println(elem);
                        }
                    }
                    for(Component elem : frameElements){
                        if(elem.getForeground() == Color.red){
                            Node p = (Node) elem;
                            p.setBorder(blackBorder);
                            p.setForeground(Color.black);
                            p.removeConnections();
                            pan.remove(elem);
                        }
                    }
                }
                out.close();
                if(!cElements.isEmpty())
                    actions.addUndoAbleAction("DLM"+cElements);                
                pan.repaint();
            }
            catch(Exception a){}
        }
        else if(e.getActionCommand() == "Paste"){
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
                        p.setPanel(pan, menu);
                        line = line.substring(i+1, line.length()-1);
                        
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
                        p.updateConnectionPosition();
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
                // add Action.
            }
            catch(Exception a){
                System.out.println(a);
            }
        }
    }
    
}
