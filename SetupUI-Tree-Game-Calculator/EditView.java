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
 * @version 6/2/2024
 */
public class EditView extends JInternalFrame implements ActionListener, MouseListener{
    private GroupSelector pan = null;
    private JTextField header = null;
    private JTextField type = null;
    private JTextField connectedTo = null;
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
        headerLabel.setBounds(0,0,this.getWidth()/3,this.getHeight()/15);
        header = new JTextField("Enter name here..");
        header.setToolTipText("Name");
        header.setBounds(this.getWidth()/3,0,2*this.getWidth()/3,this.getHeight()/15);
        header.setFocusable(false);
        JSeparator sep = new JSeparator();
        sep.setBounds(0,this.getHeight()/15, this.getWidth(), this.getHeight());
        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setBounds(0,this.getHeight()/15,this.getWidth()/3,this.getHeight()/15);
        type = new JTextField("Enter type here..");
        type.setBounds(this.getWidth()/3,this.getHeight()/15,2*this.getWidth()/3,this.getHeight()/15);
        type.setFocusable(false);
        type.setToolTipText("Type");
        JSeparator sep2 = new JSeparator();
        sep2.setBounds(0,2*this.getHeight()/15, this.getWidth(), this.getHeight());
        JLabel connectedLabel = new JLabel("Parent:");
        connectedLabel.setBounds(0,2*this.getHeight()/15, this.getWidth(), this.getHeight()/15);
        connectedTo = new JTextField("Enter node name here..");
        connectedTo.setBounds(this.getWidth()/3,2*this.getHeight()/15, 2*this.getWidth()/3, this.getHeight()/15);
        connectedTo.setEditable(false);
        connectedTo.setFocusable(false);
        formulaAdder = new JButton("Add Formula");
        formulaAdder.setBounds(this.getWidth()/4, this.getHeight()/5, this.getWidth()/2, this.getHeight()/15);
        formulaAdder.addActionListener(this);
        formulaAdder.setFocusable(false);
        submit = new JButton("Submit");
        submit.setBounds(this.getWidth()/4, this.getHeight()/2, this.getWidth()/2, this.getHeight()/15);
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
        } else {
            connectedTo.setText(node.getParentNode().getName());
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
    @Override
    public void mouseEntered(MouseEvent e){
        header.setFocusable(true);
        type.setFocusable(true);
        connectedTo.setFocusable(true);
    }
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
    public boolean isWithin(MouseEvent e){
        // work on later
        if(e.getX() < 0 || e.getX() >= this.getWidth()){
            return false;
        }
        if(e.getY() < 0 || e.getY() >= this.getHeight()){
            return false;
        }
        return true;
    }
}
