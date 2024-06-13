
/**
 * The Frame class's primary purpose is to create a JFrame, a 
 * window if you will, that can display certain information.  The 
 * certain information consists of a PopupMenu, think of something
 * appearing when you right-click, a MenuBar, the File, Edit, and Open
 * that are within most applications, a ToolBar, which makes using 
 * some of the buttons easier and separates itself from the MenuBar
 * in that it is more convient for use by those with vision.  
 * There is less work to find a button or action.  The GroupSelector
 * acts much like the Frame class, as it too serves to display certain
 * information.
 * 
 * The Frame class, upon being initialized, will make itself the 
 * size of the user's computer, not fullscreen, but screen width
 * minus 1/12.5th of it and screen height minus 1/12.5th of it.  This
 * technique is used by LibreWolf and some other applications, although
 * the value itself varies.  The Frame is resizable, with a Cyan 
 * background, and will spawn in the center of the screen.  The Frame will automatically
 * resize itself and repaint itself every 2 seconds should the frame be active (to be done).
 * 
 * When the Frame is clicked out of, the PopupMenu will be hidden, so
 * as to not hinder other applications, and when the Frame is iconified
 * the same will happen.  When the Frame is closing, information regarding
 * the Frame, such as the position and size, alongside settings from MenuBar,
 * will be saved into a text file (to do later).
 * 
 * The Frame class has a MenuBar, ToolBar, and GroupSelector,
 * which in turn have their own functionality, which are described further
 * in their respective files.
 *
 * @author Noah Winn
 * @version 6/12/2024
 */

import java.awt.Color;
import javax.swing.JFrame;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.SwingUtilities;
import java.util.concurrent.TimeUnit;
public class Frame extends JFrame implements WindowListener{
    // instance variables
    private Frame fram = this;
    private boolean isOpen = false;
    private boolean isActive = false;
    private EditPopupMenu popupMenu = new EditPopupMenu();
    private ActionManager actions = new ActionManager();
    /**
     * Constructor for objects of class Frame
     * Titles the Frame class and initializes it.
     */
    public Frame(){
        super("Tree Game Calculator Designer");
        initializeFrame();
    }
    /**
     * An example of a method - replace this comment with your own
     */
    public void initializeFrame(){
        //this.setTitle(); // For later when user can open files and probably in MenuBar instead?
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int inset = (int)(screenSize.width/12.5);
        this.setSize(screenSize.width - inset,screenSize.height - inset);
        inset = 0; screenSize = null;
        this.setResizable(true);
        this.getContentPane().setBackground(new Color(200,255,255));
        this.addWindowListener(this);
        
        GroupSelector pan3 = new GroupSelector(this, popupMenu);
        
        //Creates menu components
        MenuBar menu = new MenuBar();
        menu.createMenuBar(this, pan3, popupMenu);
        
        ToolBar tool = new ToolBar();
        tool.createToolBar(this, pan3, popupMenu);
        
        actions.setPanel(pan3, popupMenu);
        
        this.add(pan3);        
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        isActive = true;
        isOpen = true;
        begin();
    }
    public ActionManager getActions(){return actions;}
    @Override
    public void windowDeactivated(WindowEvent w){
        isActive = false;
        popupMenu.setVisible(false);
    }
    @Override
    public void windowActivated(WindowEvent w){
        isActive = true;
        end();
        begin();
    }
    @Override
    public void windowDeiconified(WindowEvent w){}
    @Override
    public void windowIconified(WindowEvent w){
        isActive = false;
        popupMenu.setVisible(false);
    }
    @Override
    public void windowClosed(WindowEvent w){}
    @Override
    public void windowClosing(WindowEvent w){
        isOpen = false;
        //System.out.println("Frame is closing, proceeding to operate HistoricReader in order to save settings");
    }
    @Override
    public void windowOpened(WindowEvent w){isOpen = true;}
    Thread refresher;
    public void begin(){
        refresher = new Thread()
        {
            @Override
            public void run(){
                while(isOpen && isActive){
                    try{
                        this.sleep(2000); // 2 seconds
                    }
                    catch(InterruptedException ie){
                    }
                    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                    int inset = (int)(screenSize.width/12.5);
                    System.out.println(fram.getX()+" "+fram.getY());
                    // Flawed in that is doesn't stay still when screensize is the same and that if you resize it, everything messes up.
                    fram.setBounds((int)(screenSize.width*fram.getX()/(fram.getWidth()*12.5/11.5)),(int)(screenSize.height*fram.getY()/(fram.getHeight()*12.5/11.5)),screenSize.width - inset,screenSize.height - inset);
                    fram.repaint();
                }
            }
        };
        refresher.start();
    }
    public void end(){
        refresher.stop();
    }
}