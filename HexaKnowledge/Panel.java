
/**
 * Write a description of class Panel here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.awt.*;
import javax.swing.*;
public class Panel extends JPanel{
    /**
     * Constructor for objects of class Panel
     */
    public Panel(){
        this.setPreferredSize(new Dimension(4000,4000));
    }
    
    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        double rand;
        double base;
        double dist;
        double pi = 3.14159;
        
        //paint spraybrush?
        for(int i = 0; i <= 4000; i++){
            for(int v = 0; v <= 4000; v++){
                rand = Math.random();
                dist = Math.sqrt(Math.pow(i, 2) + Math.pow(v, 2)) * .00275;
                base = (2-dist)/Math.sqrt(pi*2) - Math.pow((2-dist), 3)/(24*Math.sqrt(pi*2)) + Math.pow((2-dist), 5)/(640*Math.sqrt(pi*2));
                if(rand < base){
                    g2.drawLine(i,v,i,v);                    
                }
                else{
                    //bro idk
                }
            }
            //if there are a certain number of points within a region make a line from the end of that region to the
            //beginning of the next closest region?
        }
        
        
    }
}
