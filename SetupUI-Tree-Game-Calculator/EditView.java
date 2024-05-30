import javax.swing.JInternalFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JButton;

/**
 * Write a description of class EditView here.
 *
 * @author Noah Winn
 * @version 5/29/2024
 */
public class EditView extends JInternalFrame {
    private GroupSelector pan = null;
    private JTextField header = null;
    private JTextField desc = null;
    private JTextField connectedTo = null;
    private JButton formulaAdder = null;
    /**
     * Constructor for objects of class EditView
     */
    public EditView(GroupSelector pan){
        super("Edit View", true, true, true, true);
        this.setSize((int)pan.getSize().getWidth()/4,(int)pan.getSize().getHeight());
        this.pan = pan;
        this.setLayout(null);
        this.setLocation((int)pan.getSize().getWidth()-(int)pan.getSize().getWidth()/4, 0);
        try {
            this.setSelected(true);
        } catch (java.beans.PropertyVetoException a) {}
        
        JLabel headerLabel = new JLabel("Node Name:");
        headerLabel.setBounds(0,0,this.getWidth()/3,this.getHeight()/15);
        header = new JTextField("Enter name here..");
        header.setBounds(this.getWidth()/3,0,2*this.getWidth()/3,this.getHeight()/15);
        JSeparator sep = new JSeparator();
        sep.setBounds(0,this.getHeight()/15, this.getWidth(), this.getHeight());
        JLabel descLabel = new JLabel("Description:");
        descLabel.setBounds(0,this.getHeight()/15,this.getWidth()/3,this.getHeight()/15);
        desc = new JTextField("Enter description here..");
        desc.setBounds(this.getWidth()/3,this.getHeight()/15,2*this.getWidth()/3,this.getHeight()/15);
        JSeparator sep2 = new JSeparator();
        sep2.setBounds(0,2*this.getHeight()/15, this.getWidth(), this.getHeight());
        JLabel connectedLabel = new JLabel("Connected To:");
        connectedLabel.setBounds(0,2*this.getHeight()/15, this.getWidth(), this.getHeight()/15);
        connectedTo = new JTextField("Enter node name here..");
        connectedTo.setBounds(this.getWidth()/3,2*this.getHeight()/15, 2*this.getWidth()/3, this.getHeight()/15);
        formulaAdder = new JButton("Add Formula");
        formulaAdder.setBounds(this.getWidth()/4, this.getHeight()/5, this.getWidth()/2, this.getHeight()/15);
        this.add(headerLabel);
        this.add(header);
        this.add(sep);
        this.add(descLabel);
        this.add(desc);
        this.add(sep2);
        this.add(connectedLabel);
        this.add(connectedTo);
        this.add(formulaAdder);
    }
    public void resetLocation(){
        this.setSize((int)pan.getSize().getWidth()/4,(int)pan.getSize().getHeight());
        this.setLocation((int)pan.getSize().getWidth()-(int)pan.getSize().getWidth()/4, 0);
    }
    public void editNode(Node node){
        
    }
}
