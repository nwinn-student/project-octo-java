
/**
 * The ToolBar class sets up a collection of buttons, such as
 * copy, cut, create, delete, and paste, that can be used to directly impact
 * the screen.
 *
 * @author Noah Winn
 * @version 6/5/2024
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
        addButton("Create Node", toolBar);
        addButton("Connect Nodes", toolBar);
        addButton("Edit Node", toolBar);
        addButton("Delete Node", toolBar);
        addButton("Run", toolBar);
        toolBar.addSeparator();
        addButton("Copy", toolBar);
        addButton("Cut",toolBar);
        addButton("Paste", toolBar);
        toolBar.getAccessibleContext().setAccessibleName("Tool Bar");
        fram.add(toolBar, BorderLayout.PAGE_START);
    }
    
    public void addButton(String name, JToolBar parent){
        JButton button = new JButton(name);
        button.addActionListener(this);
        button.setFocusable(false);
        button.getAccessibleContext().setAccessibleName(name);
        parent.add(button);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        menu.setVisible(false);
        if(e.getActionCommand() == "Create Node"){
            //System.out.println("Creating Node");
            Node p = new Node();
            p.setPanel(pan, menu);
            p.setName(nodeIndex.toString());
            nodeIndex++;
            actions.addUndoAbleAction("MKS"+p.toString());
            pan.add(p);
            fram.repaint();
        }
        else if(e.getActionCommand() == "Connect Nodes"){
            //CONNECTS selected nodes, remove?
            List<Component> frameElements = Arrays.asList(pan.getComponents());
            List<Component> cElements = new ArrayList<>();
            if(frameElements.size() < 4096){
                for(Component elem : frameElements){
                    if(elem.getForeground() == Color.red){
                        //Selected nodes have been obtained now connect them all, prob not this loop tho
                        cElements.add(elem);
                    }
                }
                actions.addUndoAbleAction("CNM"+cElements.toString());
            }
            
            fram.repaint();
        }
        else if(e.getActionCommand() == "Edit Node"){
            //EDITS selected nodes, remove?
            List<Component> frameElements = Arrays.asList(pan.getComponents());
            List<Component> cElements = new ArrayList<>();
            if(frameElements.size() < 4096){
                for(Component elem : frameElements){
                    if(elem.getForeground() == Color.red){
                        //Selected nodes have been obtained now open editor UI
                        //for each one?
                        cElements.add(elem);
                    }
                }
                // to state before edit
                actions.addUndoAbleAction("EDM"+cElements.toString());
            }
            fram.repaint();
        }
        else if(e.getActionCommand() == "Delete Node"){
            List<Component> frameElements = Arrays.asList(pan.getComponents());
            List<Component> cElements = new ArrayList<>();
            for(Component elem : frameElements){
                if(elem.getForeground() == Color.red){
                    Node p = (Node) elem;
                    cElements.add(p);
                    p.removeConnections();
                    pan.remove(elem);
                }
            }
            actions.addUndoAbleAction("DLM"+cElements);
            fram.repaint();
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
                fram.repaint();
                out.close();
            }
            catch(Exception a){}
        }
        else if(e.getActionCommand() == "Cut"){
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
                fram.repaint();
                out.close();
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
                fram.repaint();
            }
            catch(Exception a){}
        }
    }
    
}
