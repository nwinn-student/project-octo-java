
/**
 * The ToolBar class sets up a collection of buttons, such as
 * copy, cut, create, delete, and paste, that can be used to directly impact
 * the screen.
 *
 * @author Noah Winn
 * @version 5/23/2024
 */
import javax.swing.JToolBar;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JComponent;
import java.awt.Component;
import java.awt.Color;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.File;
import javax.swing.border.Border;
import javax.swing.BorderFactory;

public class ToolBar implements ActionListener
{
    private JToolBar toolBar;
    private Frame fram;
    private GroupSelector pan;
    private Border blackBorder = BorderFactory.createLineBorder(Color.BLACK);

    private JButton createNode;
    /**
     * Constructor for objects of class ToolBar
     */
    public ToolBar(){}
    
    public void createToolBar(Frame fram, GroupSelector pan){
        this.fram = fram;
        this.pan = pan;
        toolBar = new JToolBar();
        addButton("Create Node", toolBar);
        addButton("Connect Nodes", toolBar);
        addButton("Edit Node", toolBar);
        addButton("Delete Node", toolBar);
        addButton("Run", toolBar);
        toolBar.addSeparator();
        addButton("Copy", toolBar);
        addButton("Cut",toolBar);
        addButton("Paste", toolBar);
        fram.add(toolBar, BorderLayout.PAGE_START);
    }
    
    public void addButton(String name, JToolBar parent){
        JButton button = new JButton(name);
        button.addActionListener(this);
        button.setFocusable(false);
        parent.add(button);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand() == "Create Node"){
            //System.out.println("Creating Node");
            Node p = new Node();
            p.setPanel(pan);
            pan.add(p);
            fram.repaint();
        }
        else if(e.getActionCommand() == "Connect Nodes"){
            //CONNECTS selected nodes
            Component[] c = pan.getComponents();
            for(int i=0; i < c.length; i++){
                if(c[i].getForeground() == Color.red){
                    //Selected nodes have been obtained now connect them all, prob not this loop tho
                }
            }
            fram.repaint();
        }
        else if(e.getActionCommand() == "Edit Node"){
            //EDITS selected nodes
            Component[] c = pan.getComponents();
            for(int i=0; i < c.length; i++){
                if(c[i].getForeground() == Color.red){
                    //Selected nodes have been obtained now open editor UI
                    //for each one.
                }
            }
            fram.repaint();
        }
        else if(e.getActionCommand() == "Delete Node"){
            //DELETES selected nodes
            Component[] c = pan.getComponents();
            for(int i=0; i < c.length; i++){
                if(c[i].getForeground() == Color.red){
                    pan.remove(c[i]);
                }
            }
            fram.repaint();
        }
        else if(e.getActionCommand() == "Copy"){
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
                fram.repaint();
                
                out.close();
            }
            catch(Exception a){
                
            }
        }
        else if(e.getActionCommand() == "Cut"){
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
                fram.repaint();
                
                out.close();
            }
            catch(Exception a){
                
            }
        }
        else if(e.getActionCommand() == "Paste"){
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
                        p.setPanel(pan);
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
                fram.repaint();
                
                scan.close();
            }
            catch(Exception a){
                
            }
        }
    }
    
}
