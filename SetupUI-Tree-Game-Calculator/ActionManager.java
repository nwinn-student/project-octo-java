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
 * @version 9/5/2024
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
    private void updateLists(){
        if(undoAbleList.size() > MAX_SIZE){
            // no need for Deque due to the small size of the stack
            undoAbleList.removeElementAt(MAX_SIZE);
        }
    }
    /**
     * Undoes the previous action
     */
    public void undo(){
        if(canUndo()){
            redoAbleList.push(undoAbleList.pop());
            doAction(redoAbleList.peek(), true);
        }
    }
    /**
     * Redoes the undone action
     */
    public void redo(){
        if(canRedo()){
            undoAbleList.push(redoAbleList.pop());
            doAction(undoAbleList.peek(), false);
        }
    }
    /**
     * Adds an action that can be undone/redone.
     */
    public void addUndoableAction(String action){
        undoAbleList.push(action);
        updateLists();
    }
    private void doAction(String action, boolean isUndo){
        /*
         * Similar to pasting, we will look at the action and 
         *  determine what it was, most likely it will be Node 
         *   creation, duplication, final position/size after 
         *    mouseRelease, or editting (name, connectedTo, desc, or formulas).
         * Nodes have their own uniqueIDs, so we look for that first.
         */
        if(action.substring(0,3).equals("MKS")){
            // Remove / Add
            makeNodeAction(action, isUndo);
        } else if (action.substring(0,3).equals("DLM")) {
            // Add / Remove
            deleteNodeAction(action, isUndo);
        } else if (action.substring(0,3).equals("MOV")){
            // Move back / Move forward
            moveNodeAction(action, isUndo);
        } else if(action.substring(0,3).equals("EDT")){
            // Edit back / Edit forward
        }
    }
    //Make the node(s)
    private void makeNodeAction(String action, boolean isUndo){
        if(isUndo){
            deleteNodeAction(action, !isUndo);
            return;
        }
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
                
                //System.out.println(crd[0] + " : " + crd[3]);
                if(!Instant.parse(crd[0]).equals(Instant.parse(crd[3]))) {
                    boolean none = true;
                    for(Node node : undoAblePanel.getNodes()){
                        if(node.getUniqueID().equals(Instant.parse(crd[3]))){
                            none=false;
                            p.setParentNode(node);
                            // maybe break? since there could be multiple
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
                p.updateZoom();
                p.updateConnectionPosition();
                undoAblePanel.add(p);
            }
        }
        for(Node conn : connCheck){
            for(Node elem : undoAblePanel.getNodes()){
                if(elem.getUniqueID().equals(conn.getConnectedNodeID())){
                    conn.setParentNode(elem);
                }
            }
        }
    }
    // Removes the node(s)
    private void deleteNodeAction(String action, boolean isUndo){
        if(isUndo){
            makeNodeAction(action, !isUndo);
            return;
        }
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
            Node[] frameElements = undoAblePanel.getNodes().toArray(new Node[undoAblePanel.getNodes().size()]);
            for(Node node : frameElements){
                if(node.getUniqueID().equals(Instant.parse(crd[0]))){
                    undoAblePanel.remove(node); // will error?
                }
            }
        }
    }
    private void moveNodeAction(String action, boolean isUndo){
        if(isUndo){
        
        } else {
        
        }
    }
    private void editNodeAction(String action, boolean isUndo){
        if(isUndo){
        
        } else {
        
        }
    }
}
