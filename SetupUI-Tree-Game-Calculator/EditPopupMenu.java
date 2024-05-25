import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;
import java.awt.Event;
import java.awt.event.KeyEvent;
import java.awt.Component;
import java.awt.Color;
import java.io.PrintWriter;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import java.util.Scanner;
import java.io.File;

/**
 * Write a description of class EditPopupMenu here.
 *
 * @author Noah Winn
 * @version 5/24/2024
 */
public class EditPopupMenu extends JPopupMenu implements ActionListener
{
    private JPopupMenu menuBar;
    private GroupSelector pan;
    private Border blackBorder = BorderFactory.createLineBorder(Color.BLACK);

    /**
     * Constructor for objects of class EditPopupMenu
     */
    public EditPopupMenu(){}
    
    public void createPopupMenu(GroupSelector pan){
        this.pan = pan;
        
        addMenuItem("Edit View", this, 0);
        this.addSeparator();
        addMenuItem("Duplicate", this, KeyEvent.VK_D);
        addMenuItem("Copy", this, KeyEvent.VK_C); // CTRL C
        addMenuItem("Cut", this, KeyEvent.VK_X); // CTRL X
        addMenuItem("Paste", this, KeyEvent.VK_V); // CTRL V
        this.addSeparator();
        addMenuItem("Remove Nodes", this, KeyEvent.VK_DELETE);
        this.setVisible(false);
    }
    private void addMenuItem(String title, JPopupMenu parent, int key){
        JMenuItem menuItem = new JMenuItem(title);
        if(key != 0){
            if(title.equals("Remove Nodes")){
               menuItem.setAccelerator(KeyStroke.getKeyStroke(key, 0));
            }
            else{
                 //Key is not undefined, will always require CTRL
                menuItem.setAccelerator(KeyStroke.getKeyStroke(key, Event.CTRL_MASK));
            }
        }
        menuItem.addActionListener(this);
        parent.add(menuItem);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand() == "Edit View"){
            // Create a new frame or tabbedPane?
        }
        if(e.getActionCommand() == "Duplicate"){
            Component[] c = pan.getComponents();
            for(int i=0; i < c.length; i++){
                if(c[i].getForeground() == Color.red){
                    //Change foreground and border back to black
                    c[i].setForeground(Color.black);
                    Node p = (Node) c[i];
                    p.setBorder(blackBorder);
                    //Duplicate c[i]
                    //Panel copy = (Panel) cloneSwingComponent(c[i]);
                    Node copy = new Node();
                    copy.setBounds(p.getBounds());
                    //Shift
                    int shiftX = copy.getX() + (int)copy.getSize().getWidth()/5;
                    int shiftY = copy.getY() + (int)copy.getSize().getHeight()/5;
                    copy.setLocation(shiftX, shiftY);
                    copy.setPanel(pan, this);
                    copy.updateZoom();
                    //System.out.println(copy);
                    pan.add(copy);
                    
                }
            }
            pan.repaint();
        }
        if(e.getActionCommand() == "Copy"){
            Component[] c = pan.getComponents();
            //Clear clipboard.txt
            try{
                PrintWriter out = new PrintWriter("clipboard.txt");
                for(int i=0; i < c.length; i++){
                    if(c[i].getForeground() == Color.red){
                        //Copy c[i], meaning we place it in clipboard.txt
                        //Change foreground and border back to black
                        Node p = (Node) c[i];
                        p.setBorder(blackBorder);
                        p.setForeground(Color.black);
                        out.println(c[i]);
                        
                    }
                }
                pan.repaint();
                
                out.close();
            }
            catch(Exception a){
                
            }
        }
        if(e.getActionCommand() == "Cut"){
            Component[] c = pan.getComponents();
            //Clear clipboard.txt
            try{
                PrintWriter out = new PrintWriter("clipboard.txt");
                for(int i=0; i < c.length; i++){
                    if(c[i].getForeground() == Color.red){
                        //Copy c[i], meaning we place it in clipboard.txt
                        //Change foreground and border back to black
                        Node p = (Node) c[i];
                        p.setBorder(blackBorder);
                        p.setForeground(Color.black);
                        //c[i].list();
                        out.println(c[i]);
                        //Remove c[i]
                        p.removeConnections();
                        pan.remove(c[i]);
                    }
                }
                pan.repaint();
                
                out.close();
            }
            catch(Exception a){
                
            }
        }
        if(e.getActionCommand() == "Paste"){
            try{
                Scanner scan = new Scanner(new File("clipboard.txt"));
                while(scan.hasNextLine()){
                    String line = scan.nextLine();
                    char[] field = line.toCharArray();
                    int i;
                    for(i = 0; i < field.length; i++){
                        if(field[i] == '['){
                            break;
                        }
                    }
                    // EXPECT EITHER "Panel" or "Connector"
                    if(line.substring(0,i).equals("Node")){
                        Node p = new Node();
                        p.setPanel(pan, this);
                        line = line.substring(i+2, line.length()-1);
                        //System.out.println(line);
                        
                        String[] crd = line.split(",");
                        String[] siz = crd[2].split("x");
                        p.setBounds(
                            Integer.parseInt(crd[0]),Integer.parseInt(crd[1]),
                            Integer.parseInt(siz[0]),Integer.parseInt(siz[1])
                            );
                        //Shift
                        int shiftX = p.getX() + (int)p.getSize().getWidth()/5;
                        int shiftY = p.getY() + (int)p.getSize().getHeight()/5;
                        p.setLocation(shiftX, shiftY);
                        p.updateZoom();
                        pan.add(p);
                    }
                    else if(line.substring(0,i).equals("Connector")){
                        
                    }
                }
                pan.repaint();
                
                scan.close();
            }
            catch(Exception a){
                
            }
        }
        if(e.getActionCommand() == "Remove Nodes"){
            //DELETES selected nodes
            Component[] c = pan.getComponents();
            for(int i=0; i < c.length; i++){
                if(c[i].getForeground() == Color.red){
                    pan.remove(c[i]);
                }
            }
            pan.repaint();
        }
    }
}
