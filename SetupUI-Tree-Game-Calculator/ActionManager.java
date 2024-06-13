import java.util.Stack;
import java.time.Instant;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.LinkedList;
import javax.swing.JPanel;
import java.awt.Component;

/**
 * Much like javax.swing.undo.UndoManager, except it allows for custom
 * edits to be used.
 *
 * @author Noah Winn
 * @version 6/7/2024
 */
public class ActionManager {
    private final short MAX_SIZE = 512;
    private Stack<String> undoAbleList = new Stack();
    private Stack<String> redoAbleList = new Stack();
    private GroupSelector undoAblePanel = null;
    private EditPopupMenu popupMenu = null;
    /**
     * Constructor for objects of class ActionManager
     */
    public ActionManager() {}
    public void setPanel(GroupSelector undoAblePanel, EditPopupMenu popupMenu){
        this.undoAblePanel = undoAblePanel;
        this.popupMenu = popupMenu;
    }
    private boolean canUndo(){
        if(undoAbleList.empty()){
            return false;
        }
        return true;
    }
    private boolean canRedo(){
        if(redoAbleList.empty()){
            return false;
        }
        return true;
    }
    private void doAction(String action, boolean isUndo){
        /*
         * Similar to pasting, we will look at the action and 
         *  determine what it was, most likely it will be Node 
         *   creation, duplication, final position/size after 
         *    mouseRelease, or editting (name, connectedTo, desc, or formulas).
         * Nodes have their own uniqueIDs, so we look for that first.
         */
        //System.out.println(action);
        if(isUndo){
            if(action.substring(0,3).equals("MKS")){
                // Removing
                char[] field = action.substring(3).toCharArray();
                int i;
                for(i = 0; i < field.length; i++){
                    if(field[i] == '['){
                        break;
                    }
                }
                action = action.substring(3).substring(i+1, action.substring(3).length()-1);
                String[] crd = action.split(",");
                
                List<Component> frameElements = Arrays.asList(undoAblePanel.getComponents());
                for(Component elem : frameElements){
                    if(elem.getClass().equals(Node.class)){
                        if(((Node)elem).getUniqueID().equals(Instant.parse(crd[0]))){
                            Node p = (Node) elem;
                            p.removeConnections();
                            undoAblePanel.remove(elem);
                        }
                    }
                }
            } else if (action.substring(0,3).equals("DLM")) {
                // Adding
                List<String> comp = Arrays.asList(action.substring(3,action.length()-1).replaceAll("],","] ,").split(" ,"));
                List<Node> connCheck = new ArrayList<>();
                for(String elem : comp){
                    String line = elem.substring(1,elem.length()-1);
                    char[] field = line.toCharArray();
                    int i;
                    for(i = 0; i < field.length; i++){
                        if(field[i] == '['){
                            break;
                        }
                    }
                    if(line.substring(0,i).equals("Node")){
                        Node p = new Node();
                        p.setPanel(undoAblePanel, popupMenu);
                        line = line.substring(i+1);
                        String[] crd = line.split(",");
                        p.setUniqueID(Instant.parse(crd[0]));
                        p.setName(crd[1]);
                        
                        System.out.println(crd[0] + " : " + crd[3]);
                        List<Component> frameElements = Arrays.asList(undoAblePanel.getComponents());
                        if(!Instant.parse(crd[0]).equals(Instant.parse(crd[3])) 
                            && frameElements.size() < 4096) {
                            boolean none = true;
                            for(Component framE : frameElements){
                                if(elem.getClass().equals(Node.class)){
                                    if(((Node)framE).getUniqueID().equals(Instant.parse(crd[3]))){
                                        none = false;
                                        p.setParentNode((Node)framE);
                                    }
                                }
                            } if(none){
                                p.setConnectedNodeID(Instant.parse(crd[3]));
                                connCheck.add(p);
                            }
                        }
                        p.setBounds(
                            Integer.parseInt(crd[4]),Integer.parseInt(crd[5]),
                            Integer.parseInt(crd[6]),Integer.parseInt(crd[7])
                            );
                        //Formulas...
                        p.updateZoom();
                        p.updateConnectionPosition();
                        undoAblePanel.add(p);
                    }
                }
                for(Node conn : connCheck){
                    List<Component> frameElements = Arrays.asList(undoAblePanel.getComponents());
                    for(Component elem : frameElements){
                        if(elem.getClass().equals(Node.class)){
                            Node z = (Node)elem;
                            if(z.getUniqueID().equals(conn.getConnectedNodeID())){
                                conn.setParentNode(z);
                            }
                        }
                    }
                }
            } else if (action.substring(0,3).equals("MOV")){
                // Move back
            }
        
        } else {
            if(action.substring(0,3).equals("MKS")){
                // Adding
                Node connCheck = null;
                char[] field = action.substring(3).toCharArray();
                int i;
                for(i = 0; i < field.length; i++){
                    if(field[i] == '['){
                        break;
                    }
                }
                if(action.substring(3).substring(0,i).equals("Node")){
                    Node p = new Node();
                    p.setPanel(undoAblePanel, popupMenu);
                    action = action.substring(3).substring(i+1, action.substring(3).length()-1);
                    String[] crd = action.split(",");
                    p.setUniqueID(Instant.parse(crd[0]));
                    p.setName(crd[1]);
                    List<Component> frameElements = Arrays.asList(undoAblePanel.getComponents());
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
                        } if(none){
                            p.setConnectedNodeID(Instant.parse(crd[3]));
                            connCheck = p;
                        }
                    }
                    p.setBounds(
                        Integer.parseInt(crd[4]),Integer.parseInt(crd[5]),
                        Integer.parseInt(crd[6]),Integer.parseInt(crd[7])
                        );
                    //Formulas...
                    
                    p.updateZoom();
                    undoAblePanel.add(p);
                }
                if(connCheck != null){
                    List<Component> frameElements = Arrays.asList(undoAblePanel.getComponents());
                    for(Component elem : frameElements){
                        if(elem.getClass().equals(Node.class)){
                            Node z = (Node)elem;
                            if(z.getUniqueID().equals(connCheck.getConnectedNodeID())){
                                connCheck.setParentNode(z);
                            }
                        }
                    }
                }
            } else if (action.substring(0,3).equals("DLM")) {
                // Remove
                List<String> comp = Arrays.asList(action.substring(3,action.length()-1).replaceAll("],","] ,").split(" ,"));
                for(String elem : comp){
                    String line = elem.substring(1,elem.length()-1);
                    char[] field = line.toCharArray();
                    int i;
                    for(i = 0; i < field.length; i++){
                        if(field[i] == '['){
                            break;
                        }
                    }
                    line = line.substring(i+1);
                    String[] crd = line.split(",");
                    List<Component> frameElements = Arrays.asList(undoAblePanel.getComponents());
                    for(Component framE : frameElements){
                        if(framE.getClass().equals(Node.class)){
                            if(((Node)framE).getUniqueID().equals(Instant.parse(crd[0]))){
                                Node p = (Node) framE;
                                p.removeConnections();
                                undoAblePanel.remove(framE);
                            }
                        }
                    }
                }
            } else if (action.substring(0,3).equals("MOV")){
                // Move forward
            }
            
        }
    }
    private void updateLists(){
        if(undoAbleList.size() > MAX_SIZE){
            // no need for Deque due to the small size of the stack
            undoAbleList.removeElementAt(MAX_SIZE);
        }
    }
    public void undo(){
        if(canUndo()){
            redoAbleList.push(undoAbleList.pop());
            doAction(redoAbleList.peek(), true);
        }
        //System.out.println(undoAbleList+" "+redoAbleList);
    }
    public void redo(){
        if(canRedo()){
            undoAbleList.push(redoAbleList.pop());
            doAction(undoAbleList.peek(), false);
        }
        //System.out.println(undoAbleList+" "+redoAbleList);
    }
    public void addUndoAbleAction(String action){
        undoAbleList.push(action);
        updateLists();
    }
}
