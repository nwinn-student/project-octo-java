
/**
 * Write a description of class Frame here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.awt.*;
import javax.swing.*;
public class cFrame extends JFrame{
    cPanel pan;
    /**
     * Constructor for objects of class cFrame
     */
    public cFrame(){
        pan = new cPanel();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   //allows the frame to close when x is pushed
        this.setLayout(new GridLayout());
        this.add(pan);  //adds the panel to the frame
        this.pack();
        this.setLocationRelativeTo(null);   //centered frame to screen
        this.setVisible(true);  //visible frame
    }
    
    

    
}
