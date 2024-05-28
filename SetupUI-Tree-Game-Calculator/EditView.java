import javax.swing.JInternalFrame;

/**
 * Write a description of class EditView here.
 *
 * @author Noah Winn
 * @version 5/27/2024
 */
public class EditView extends JInternalFrame {
    private GroupSelector pan = null;
    /**
     * Constructor for objects of class EditView
     */
    public EditView(GroupSelector pan){
        super("Edit View", true, true, true, true);
        this.setVisible(true); //necessary as of 1.3
        this.setSize((int)pan.getSize().getWidth()/4,(int)pan.getSize().getHeight());
        this.pan = pan;

        this.setLocation((int)pan.getSize().getWidth()-(int)pan.getSize().getWidth()/4, 0);
        pan.add(this);
        try {
            this.setSelected(true);
        } catch (java.beans.PropertyVetoException a) {}
    }
}
