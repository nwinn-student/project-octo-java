import javax.swing.JInternalFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Write a description of class EditView here.
 *
 * @author Noah Winn
 * @version 6/25/2024
 */
public class EditView extends JInternalFrame implements ActionListener, MouseListener{
    private GroupSelector pan = null;
    private JTextField header = null;
    private JTextField type = null;
    private JTextField connectedTo = null;
    private JTextField childrenTo = null;
    private JButton formulaAdder = null;
    private JButton submit = null;
    
    private Node node = null;
    /**
     * Constructor for objects of class EditView
     */
    public EditView(GroupSelector pan){
        super("Edit View", true, true, true, true);
        this.setSize((int)pan.getSize().getWidth()/4,(int)pan.getSize().getHeight());
        this.pan = pan;
        this.setLayout(null);
        this.setLocation((int)pan.getSize().getWidth()-(int)pan.getSize().getWidth()/4, 0);
        this.addMouseListener(this);
        JLabel headerLabel = new JLabel("Name:");
        headerLabel.setBounds(0,0,getWidth()/3,getHeight()/15);
        header = new JTextField("Enter name here..");
        header.setToolTipText("Name");
        header.setBounds(getWidth()/3,0,2*getWidth()/3,getHeight()/15);
        header.setFocusable(false);
        JSeparator sep = new JSeparator();
        sep.setBounds(0,getHeight()/15, getWidth(), getHeight());
        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setBounds(0,getHeight()/15,getWidth()/3,getHeight()/15);
        type = new JTextField("Enter type here..");
        type.setBounds(getWidth()/3,getHeight()/15,2*getWidth()/3,getHeight()/15);
        type.setFocusable(false);
        type.setToolTipText("Type");
        JSeparator sep2 = new JSeparator();
        sep2.setBounds(0,2*getHeight()/15, getWidth(), getHeight());
        JLabel connectedLabel = new JLabel("Parent:");
        connectedLabel.setBounds(0,2*getHeight()/15, getWidth(), getHeight()/15);
        connectedTo = new JTextField("Enter node name here..");
        connectedTo.setBounds(getWidth()/3,2*getHeight()/15, 2*getWidth()/3, getHeight()/15);
        connectedTo.setEditable(false);
        connectedTo.setFocusable(false);
        JLabel childrenLabel = new JLabel("Children:");
        childrenLabel.setBounds(0,getHeight()/5, getWidth(), getHeight()/15);
        childrenTo = new JTextField("[null]");
        childrenTo.setBounds(getWidth()/3, getHeight()/5, 2*getWidth()/3, getHeight()/15);
        childrenTo.setEditable(false);
        childrenTo.setFocusable(false);
        formulaAdder = new JButton("Add Formula");
        formulaAdder.setBounds(getWidth()/4, 4*getHeight()/15, getWidth()/2, getHeight()/15);
        formulaAdder.addActionListener(this);
        formulaAdder.setFocusable(false);
        submit = new JButton("Submit");
        submit.setBounds(getWidth()/4, getHeight()/2, getWidth()/2, getHeight()/15);
        submit.addActionListener(this);
        submit.setFocusable(false);
        this.add(headerLabel);
        this.add(header);
        this.add(sep);
        this.add(typeLabel);
        this.add(type);
        this.add(sep2);
        this.add(connectedLabel);
        this.add(connectedTo);
        this.add(childrenLabel);
        this.add(childrenTo);
        //this.add(formulaAdder);
        this.add(submit);
    }
    public void resetLocation(){
        this.setSize((int)pan.getSize().getWidth()/4,(int)pan.getSize().getHeight());
        this.setLocation((int)pan.getSize().getWidth()-(int)pan.getSize().getWidth()/4, 0);
    }
    public void editNode(Node node){
        this.node = node;
        header.setText(node.getName());
        type.setText(node.getType());
        if(node.getUniqueID() == node.getParentNode().getUniqueID()) {
            connectedTo.setText("this");
            if(node.getChildren() == null || node.getChildren().isEmpty()){
                childrenTo.setText("[null]");
            } else{
                String children = "";
                for(Node elem : node.getChildren()){
                    children += "["+elem.getName()+"]";
                }
                childrenTo.setText(children);
                String desc = "";
                for(Node elem : node.getDescendants()){
                    desc += "["+elem.getName()+"]";
                }
                System.out.println(desc);
            }
        } else {
            connectedTo.setText(node.getParentNode().getName());
            if(node.getChildren() == null || node.getChildren().isEmpty()){
                childrenTo.setText("[null]");
            } else{
                String children = "";
                for(Node elem : node.getChildren()){
                    children += "["+elem.getName()+"]";
                }
                childrenTo.setText(children);
                String desc = "";
                for(Node elem : node.getDescendants()){
                    desc += "["+elem.getName()+"]";
                }
                System.out.println(desc);

            }
        }
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand() == "Add Formula"){
            
        } else if(e.getActionCommand() == "Submit"){
            node.setName(header.getText());
            node.setType(type.getText());
            // Maybe allow connectedTo nodes to be changed?
            
            //System.out.println(node);
        }
    }
    /**
     * Sets the elements within the EditView to focusable
     */
    @Override
    public void mouseEntered(MouseEvent e){
        header.setFocusable(true);
        type.setFocusable(true);
        connectedTo.setFocusable(true);
    }
    /**
     * Sets the elements within the EditView to unfocusable, when the cursor leaves
     */
    @Override
    public void mouseExited(MouseEvent e){
        if(!isWithin(e)){
            header.setFocusable(false);
            type.setFocusable(false);
            connectedTo.setFocusable(false);
        }
    }
    @Override
    public void mouseClicked(MouseEvent e){}
    @Override
    public void mousePressed(MouseEvent e){}
    @Override
    public void mouseReleased(MouseEvent e){}
    /**
     * Whether the mouse is within the EditView object or not
     */
    public boolean isWithin(MouseEvent e){
        if(e.getX() < 0 || e.getX() >= this.getWidth()){
            return false;
        }
        if(e.getY() < 0 || e.getY() >= this.getHeight()){
            return false;
        }
        return true;
    }
}
