
/**
 * Write a description of class Frame here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.awt.*;
import javax.swing.*;
public class Frame extends JFrame{
    Panel pan;
    /**
     * Constructor for objects of class Frame
     */
    public Frame(){ 
        pan = new Panel();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridLayout());
        this.add(pan);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }    
}
