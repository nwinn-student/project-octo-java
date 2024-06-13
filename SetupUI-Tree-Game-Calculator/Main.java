
/**
 * The Main class's primary purpose is to create and initialize 
 * the Frame object.
 *
 * @author Noah Winn
 * @version 6/12/2024
 */
import javax.swing.SwingUtilities;
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                Frame fram = new Frame();
            }
        });
        
    }
}
