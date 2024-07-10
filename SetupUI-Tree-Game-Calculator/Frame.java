import java.awt.Color;
import javax.swing.JFrame;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.AWTEvent;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
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
 * resize and reposition itself when the screenSize has changed, example, from 1920x1080 
 * to 800x600.
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
 * @version 7/9/2024
 */

public class Frame extends JFrame implements WindowListener, ComponentListener{
    // instance variables
    private Frame fram = this;
    private Dimension screenSize = null;
    private Dimension frameSize = null;
    private Point frameLocation = null;
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
     * Called by the constructors to initialize the Frame properly with the necessary
     * features.
     */
    protected void initializeFrame(){
        //this.setTitle(); // For later when user can open files and probably in MenuBar instead?
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int inset = (int)(screenSize.width/12.5);
        this.setSize(screenSize.width - inset,screenSize.height - inset);
        frameSize = this.getSize();
        inset = 0;
        this.setResizable(true);
        this.getContentPane().setBackground(new Color(200,255,255));
        this.addWindowListener(this);
        this.addComponentListener(this);
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
        frameLocation = fram.getLocation();
        // I want it to be able to start at the size of the currentScreen
        // then adjusted by the user, and moved by the user, for multi-monitors
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener(){
            @Override
            public void eventDispatched(AWTEvent event){
                if(screenSize.width != Toolkit.getDefaultToolkit().getScreenSize().width || screenSize.height != Toolkit.getDefaultToolkit().getScreenSize().height){
                    Dimension newSize = Toolkit.getDefaultToolkit().getScreenSize();
                    if(fram.isUndecorated() || fram.getExtendedState() == JFrame.MAXIMIZED_BOTH || 
                        fram.getExtendedState() == JFrame.MAXIMIZED_HORIZ || fram.getExtendedState() == JFrame.MAXIMIZED_VERT){
                        // if the state is horiz or vert maxed then when screen size becomes smaller it will become max both.
                        frameSize = new Dimension(newSize.width*frameSize.width/screenSize.width,newSize.height*frameSize.height/screenSize.height);
                        frameLocation = new Point(newSize.width*frameLocation.x/screenSize.width,newSize.height*frameLocation.y/screenSize.height);
                        screenSize = newSize;
                        fram.repaint(); //Change this section to repaint() only certain segments
                        return;
                    }
                    fram.setBounds(
                        newSize.width*frameLocation.x/screenSize.width,
                        newSize.height*frameLocation.y/screenSize.height,
                        newSize.width*frameSize.width/screenSize.width,
                        newSize.height*frameSize.height/screenSize.height
                    );
                    fram.repaint(); //Change this section to repaint() only certain segments
                    frameSize = fram.getSize();
                    frameLocation = fram.getLocation();
                    screenSize = newSize;
                    return;
                }
            }
        }, AWTEvent.PAINT_EVENT_MASK);
    }
    /**
     * Retrieves the ActionManager object, which is used for undoing and redoing actions.
     * @return the actionManager for this frame
     */
    public ActionManager getActions(){return actions;}
    /**
     * Hides the popupMenu when it is visible, activates whenever the window is clicked
     * out of.
     */
    @Override
    public void windowDeactivated(WindowEvent w){
        popupMenu.setVisible(false);
    }
    @Override
    public void windowActivated(WindowEvent w){}
    @Override
    public void windowDeiconified(WindowEvent w){}
    /**
     * Hides the popupMenu when it is visible, activates whenever the window is 
     * turned into an icon.
     */
    @Override
    public void windowIconified(WindowEvent w){
        popupMenu.setVisible(false);
    }
    @Override
    public void windowClosed(WindowEvent w){}
    /**
     * Will save the Frame location and size, not done yet, activates whenever the 
     * window is closing, when you have pressed the X button, but not CTRL-Q, will
     * have to do that separately or adjust how CTRL-Q operates.
     */
    @Override
    public void windowClosing(WindowEvent w){
        //System.out.println("Frame is closing, proceeding to operate HistoricReader in order to save settings");
    }
    @Override
    public void windowOpened(WindowEvent w){}
    /**
     * Fires before eventDispatched sometimes, which can cause size conflicts when using Scale, at least in Windows.
     * In order to stop this from occurring, the contents were moved to eventDispatched.  
     * Also, coordinates always are at 0,0 when Windows scales, so that made it easier to fix.
     */
    @Override
    public void componentResized(ComponentEvent c){
        if(fram.isUndecorated() || fram.getExtendedState() == JFrame.MAXIMIZED_BOTH || 
            fram.getExtendedState() == JFrame.MAXIMIZED_HORIZ || fram.getExtendedState() == JFrame.MAXIMIZED_VERT){
            
            return;
        }
        if(Toolkit.getDefaultToolkit().getScreenSize().equals(screenSize)){
            if(fram.getLocation().equals(new Point(0,0))){
                return;
            }
            frameSize = fram.getSize();
        }
    }
    /**
     * Relocates the Frame object whenever it moves, should be fine, unlike componentResized, hopefully.
     * Ensured that a component cannot be at 0,0.
     */
    @Override
    public void componentMoved(ComponentEvent c){
        if(fram.isUndecorated() || fram.getExtendedState() == JFrame.MAXIMIZED_BOTH || 
            fram.getExtendedState() == JFrame.MAXIMIZED_HORIZ || fram.getExtendedState() == JFrame.MAXIMIZED_VERT){
            
            return;
        }
        if(Toolkit.getDefaultToolkit().getScreenSize().equals(screenSize)){
            if(fram.getLocation().equals(new Point(0,0))){
                frameLocation = new Point(1,1); // just so 0,0 doesn't exist, hopefully
                return;
            }
            frameLocation = fram.getLocation();
        }
    }
    @Override
    public void componentHidden(ComponentEvent c){}
    @Override
    public void componentShown(ComponentEvent c){}
    /**
     * Updates the screensize, only used by MenuBar when fullscreening.
     */
    public void updateScreensize(){
        fram.setSize(frameSize);
        fram.setLocation(frameLocation);
    }
}
