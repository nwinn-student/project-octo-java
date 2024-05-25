
/**
 * The Frame class has a MenuBar, ToolBar, and GroupSelector,
 * which in turn have their own functionality.
 * 
 * The Frame class is planned to not be a fixed size, 750x750, but instead be
 * 100% height and 50% width of the screen, updating when the screen display size
 * changes, it will still be resizable.
 *
 * @author Noah Winn
 * @version 5/24/2024
 */

import java.awt.Color;
import javax.swing.JFrame;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;

public class Frame extends JFrame implements WindowListener{
    // instance variables
    private EditPopupMenu popupMenu = new EditPopupMenu();
    /**
     * Constructor for objects of class Frame
     */
    public Frame(){
        
    }
    
    /**
     * An example of a method - replace this comment with your own
     */
    public void initializeFrame(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setFocusable(false);
        this.setBounds(0,0,750,750);
        this.setResizable(true);
        this.getContentPane().setBackground(new Color(200,255,255));
        this.addWindowListener(this);
        
        GroupSelector pan3 = new GroupSelector(this, popupMenu);
        pan3.setLayout(null);
        pan3.setFocusable(false);
        
        //Creates menu components
        MenuBar menu = new MenuBar();
        menu.createMenuBar(this, pan3, popupMenu);
        
        ToolBar tool = new ToolBar();
        tool.createToolBar(this, pan3, popupMenu);
        
        this.add(pan3);        
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
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