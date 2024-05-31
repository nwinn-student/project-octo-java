import java.util.Stack;
import java.time.Instant;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

/**
 * Much like javax.swing.undo.UndoManager, except it allows for custom
 * edits to be used.
 *
 * @author Noah Winn
 * @version 5/31/2024
 */
public class ActionManager {
    private final short MAX_SIZE = 512;
    private Stack<String> undoAbleList = new Stack();
    private Stack<String> redoAbleList = new Stack();
    /**
     * Constructor for objects of class ActionManager
     */
    public ActionManager() {}
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
    private void doAction(String action){
        /*
         * Similar to pasting, we will look at the action and 
         *  determine what it was, most likely it will be Node 
         *   creation, duplication, final position/size after 
         *    mouseRelease, or editting (name, connectedTo, desc, or formulas).
         * Nodes have their own uniqueIDs, so we look for that first.
         */
        
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
            doAction(redoAbleList.peek());
        }
    }
    public void redo(){
        if(canRedo()){
            undoAbleList.push(redoAbleList.pop());
            doAction(undoAbleList.peek());
        }
    }
    public void addUndoAbleAction(String action){
        undoAbleList.push(action);
        updateLists();
    }
}
