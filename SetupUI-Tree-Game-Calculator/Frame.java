
/**
 * The Frame class has a MenuBar, ToolBar, and GroupSelector,
 * which in turn have their own functionality.
 * 
 * The Frame class is planned update when the screen display size
 * changes, it will still be resizable.
 *
 * @author Noah Winn
 * @version 6/6/2024
 */

import java.awt.Color;
import javax.swing.JFrame;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Frame extends JFrame implements WindowListener{
    // instance variables
    private EditPopupMenu popupMenu = new EditPopupMenu();
    private ActionManager actions = new ActionManager();
    /**
     * Constructor for objects of class Frame
     */
    public Frame(){
        super("Tree Game Calculator Designer");
    }
    /**
     * An example of a method - replace this comment with your own
     */
    public void initializeFrame(){
        //this.setTitle(); // For later when user can open files
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int inset = 120;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(screenSize.width - inset*4,screenSize.height - inset*2);
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
    }
    public ActionManager getActions(){return actions;}
    @Override
    public void windowDeactivated(WindowEvent w){
        popupMenu.setVisible(false);
    }
    @Override
    public void windowActivated(WindowEvent w){}
    @Override
    public void windowDeiconified(WindowEvent w){}
    @Override
    public void windowIconified(WindowEvent w){
        popupMenu.setVisible(false);
    }
    @Override
    public void windowClosed(WindowEvent w){}
    @Override
    public void windowClosing(WindowEvent w){}
    @Override
    public void windowOpened(WindowEvent w){}
}