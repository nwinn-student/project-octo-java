import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.ArrayList;

import java.awt.*;
import java.awt.event.*;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.KeyStroke;
import java.util.List;
import java.util.Arrays;
import java.time.Instant;
/**
 * The MenuBar class sets up a collection of buttons, such as
 * copy, cut, create, delete, and paste, that can be used to directly impact
 * the screen.
 *
 * @author Noah Winn
 * @version 5/31/2024
 */
public class MenuBar implements ActionListener
{
    private JMenuBar menuBar = null;
    private Frame fram = null;
    private GroupSelector pan = null;
    private EditPopupMenu menu = null;
    private ActionManager actions = null;
    
    private JMenu fileMenu, editMenu, viewMenu, testMenu, nodeMenu, helpMenu = null;
    
    private Border blackBorder = BorderFactory.createLineBorder(Color.BLACK);

    /**
     * Constructor for objects of class MenuBar
     */
    public MenuBar(){}

    public void createMenuBar(Frame fram, GroupSelector pan, EditPopupMenu menu){
        this.fram = fram;
        this.actions = fram.getActions();
        this.pan = pan;
        this.menu = menu;
        menuBar = new JMenuBar();
        
        fileMenu = addMenu("File", menuBar, KeyEvent.VK_F); //ALT-F
        editMenu = addMenu("Edit", menuBar, KeyEvent.VK_E); //ALT-E
        viewMenu = addMenu("View", menuBar, KeyEvent.VK_V); //ALT-V
        testMenu = addMenu("Test", menuBar, KeyEvent.VK_T); //ALT-T
        nodeMenu = addMenu("Node", menuBar, KeyEvent.VK_N); //ALT-N
        helpMenu = addMenu("Help", menuBar, KeyEvent.VK_H); //ALT-H
        
        LookAndFeelMenu.createLookAndFeelMenuItem(viewMenu,fram);
        
        addMenuItem("New", fileMenu, KeyEvent.VK_N); // CTRL N
        addMenuItem("Open", fileMenu, KeyEvent.VK_O); // CTRL O
        //addMenuItem("Load in new window", fileMenu, 0);
        addMenuItem("Save", fileMenu, KeyEvent.VK_S); // CTRL S
        addMenuItem("Save as..", fileMenu, 0); 
        addMenuItem("Export", fileMenu, 0);
        //addMenuItem("Print", fileMenu, KeyEvent.VK_P); // CTRL P
        addMenuItem("Exit", fileMenu, KeyEvent.VK_Q); // CTRL Q
        
        addMenuItem("Undo", editMenu, KeyEvent.VK_Z); // CTRL Z
        addMenuItem("Redo", editMenu, KeyEvent.VK_Y); // CTRL Y
        addMenuItem("Duplicate", editMenu, KeyEvent.VK_D); //CTRL D
        addMenuItem("Copy", editMenu, KeyEvent.VK_C); // CTRL C
        addMenuItem("Cut", editMenu, KeyEvent.VK_X); // CTRL X
        addMenuItem("Paste", editMenu, KeyEvent.VK_V); // CTRL V
        addMenuItem("Find", editMenu, KeyEvent.VK_F); // CTRL F
        addMenuItem("Settings", editMenu, KeyEvent.VK_COMMA); // CTRL ,
        
        addMenuItem("Fit Window", viewMenu, 0);
        addMenuItem("Zoom In", viewMenu, KeyEvent.VK_PLUS);
        addMenuItem("Zoom Out", viewMenu, KeyEvent.VK_MINUS);
        addMenuItem("Set Scaling..", viewMenu, 0);
        addMenuItem("View as..", viewMenu, 0);
        addMenuItem("Tutorial", viewMenu, 0); //goes thru it
        
        addMenuItem("Verify", testMenu, 0); //checks formulas, or trees
        addMenuItem("Display Sample Formula", testMenu, 0);
        addMenuItem("Display Sample Tree", testMenu, 0);
        
        addMenuItem("Add Game Node", nodeMenu, 0);
        addMenuItem("Remove Game Node", nodeMenu, KeyEvent.VK_DELETE);
        addMenuItem("Remove All Node", nodeMenu, 0);
        
        addMenuItem("Components", helpMenu, 0);
        addMenuItem("Documentation", helpMenu, 0);
        addMenuItem("About", helpMenu, 0);
        
        fram.setJMenuBar(menuBar);
    }
    
    /**
     * Constructor for objects of class Frame
     */
    private JMenu addMenu(String title, JMenuBar parent, int key){
        JMenu menu = new JMenu(title);
        if(key != 0){
             //Key is not undefined
            menu.setMnemonic(key);
        }
        parent.add(menu);
        return menu;
    }
    /**
     * Constructor for objects of class Frame
     */
    private void addMenuItem(String title, JMenu parent, int key){
        JMenuItem menuItem = new JMenuItem(title);
        if(key != 0){
            if(title.equals("Remove Game Node")){
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
    
    // From https://stackoverflow.com/questions/3571223
    private String getFileExtension(File file){
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".") + 1;
        if(lastIndexOf == -1){
            return "";
        }
        return name.substring(lastIndexOf);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        menu.setVisible(false);
        if(e.getActionCommand() == "New"){
            //Open up a new frame
            
            //Create a new file, if the file exists then ask if the user wants
            //to delete the existing file using another frame
            
        }
        if(e.getActionCommand() == "Open"){
            JFileChooser fileChooser = new JFileChooser();
            int response = fileChooser.showOpenDialog(null);
            if(response == JFileChooser.APPROVE_OPTION){
                File file = fileChooser.getSelectedFile();
                
                //Open the file
                try{
                    //Must be .txt extension.
                    Scanner scan = new Scanner(file);
                    
                    //Go through the contents of the file
                    while(scan.hasNextLine()){
                        String line = scan.nextLine();
                        String[] field = line.split("    "); //or \t
                        
                        //Sets up the UI
                        
                    }
                }
                catch(FileNotFoundException err){
                    System.out.println(err);
                }
                
                System.out.println(file);
            }
            
        }
        if(e.getActionCommand() == "Save"){
            JFileChooser fileChooser = new JFileChooser();
            // Find the previous file loaded***
            
            fileChooser.setSelectedFile(new File("."));
            // Does not always work after the LookandFeelMenu has been adjusted
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files(*.txt)","txt"));
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                //Force the file extension to be .TXT
                
                //Save the file
                try{
                    Scanner scan = new Scanner(file);
                    
                    //Update the contents of the file???? to what tho???
                    while(scan.hasNextLine()){
                        String line = scan.nextLine();
                        String[] field = line.split("    "); //or \t
                        
                    }
                }
                catch(FileNotFoundException err){
                    System.out.println("The system cannot find the file specified.\nAttempting to create a file...");
                    
                    // Should the file not be found, we must create it
                    try{
                        PrintWriter out = new PrintWriter(file.toString() + ".txt");
                        
                        //Add the contents***
                        
                        
                        out.close();
                    }
                    catch(FileNotFoundException newerr){
                        System.out.println("Failed to create a file.\n"+newerr);
                    }
                    
                }
                
                System.out.println(file);
            }
        }
        if(e.getActionCommand() == "Save as.."){
            JFileChooser fileChooser = new JFileChooser();
            // Find the previous file loaded***
            
            fileChooser.setSelectedFile(new File("."));
            // Does not always work after the LookandFeelMenu has been adjusted
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files(*.txt)","txt"));
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                //Force the file extension to be .TXT
                
                //Save the file
                try{
                    Scanner scan = new Scanner(file);
                    
                    //Update the contents of the file???? to what tho???
                    while(scan.hasNextLine()){
                        String line = scan.nextLine();
                        String[] field = line.split("    "); //or \t
                        
                    }
                }
                catch(FileNotFoundException err){
                    System.out.println("The system cannot find the file specified.\nAttempting to create a file...");
                    
                    // Should the file not be found, we must create it
                    try{
                        PrintWriter out = new PrintWriter(file.toString() + ".txt");
                        
                        //Add the contents***
                        
                        
                        out.close();
                    }
                    catch(FileNotFoundException newerr){
                        System.out.println("Failed to create a file.\n"+newerr);
                    }
                    
                }
                System.out.println(file);
            }
        }
        if(e.getActionCommand() == "Export"){}
        if(e.getActionCommand() == "Exit"){
            //Ask if they want to save
            System.exit(0);
        }
        
        if(e.getActionCommand() == "Undo"){
            //USE: https://github.com/hneemann/Digital/tree/master/src/main/java/de/neemann/digital/undo
            //https://stackoverflow.com/questions/24433089/jtextarea-settext-undomanager
            try{
                //undoManager.undo();
            }
            catch(Exception a){}
        }
        if(e.getActionCommand() == "Redo"){
            //https://stackoverflow.com/questions/24433089/jtextarea-settext-undomanager
            try{
                //undoManager.redo();
            }
            catch(Exception a){}
        }
        if(e.getActionCommand() == "Duplicate"){
            List<Component> frameElements = Arrays.asList(pan.getComponents());
            if(frameElements.size() < 4096){
                for(Component elem : frameElements){
                    if(elem.getForeground() == Color.red){
                        elem.setForeground(Color.black);
                        Node p = (Node) elem;
                        p.setBorder(blackBorder);
                        Node copy = new Node();
                        copy.setBounds(p.getBounds());
                        int shiftX = copy.getX() + (int)copy.getSize().getWidth()/5;
                        int shiftY = copy.getY() + (int)copy.getSize().getHeight()/5;
                        copy.setLocation(shiftX, shiftY);
                        copy.setPanel(pan, menu);
                        copy.updateZoom();
                        pan.add(copy);
                    }
                }
            }
            fram.repaint();
        }
        if(e.getActionCommand() == "Copy"){
            List<Component> frameElements = Arrays.asList(pan.getComponents());
            //Clear clipboard.txt
            try{
                PrintWriter out = new PrintWriter("clipboard.txt");
                // Don't want to take up too much memory
                if(frameElements.size() < 4096){
                    for(Component elem : frameElements){
                        if(elem.getForeground() == Color.red){
                            Node p = (Node) elem;
                            p.setBorder(blackBorder);
                            p.setForeground(Color.black);
                            out.println(elem);
                        }
                    }
                }
                fram.repaint();
                out.close();
            }
            catch(Exception a){}
        }
        if(e.getActionCommand() == "Cut"){
            List<Component> frameElements = Arrays.asList(pan.getComponents());
            //Clear clipboard.txt
            try{
                PrintWriter out = new PrintWriter("clipboard.txt");
                if(frameElements.size() < 4096){
                    for(Component elem : frameElements){
                        if(elem.getForeground() == Color.red){
                            Node p = (Node) elem;
                            p.setBorder(blackBorder);
                            p.setForeground(Color.black);
                            out.println(elem);
                            p.removeConnections();
                            pan.remove(elem);
                        }
                    }
                }
                fram.repaint();
                out.close();
            }
            catch(Exception a){}
        }
        if(e.getActionCommand() == "Paste"){
            //Create c[i] and add it to pan
            try{
                Scanner scan = new Scanner(new File("clipboard.txt"));
                List<Node> connCheck = new ArrayList<>();
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
                        p.setPanel(pan, menu);
                        line = line.substring(i+1, line.length()-1);
                        //System.out.println(line);
                        
                        String[] crd = line.split(",");
                        p.setUniqueID(Instant.parse(crd[0]));
                        p.setName(crd[1]);
                        // Look through current elements to see if any have the same ID
                        List<Component> frameElements = Arrays.asList(pan.getComponents());
                        if(!Instant.parse(crd[0]).equals(Instant.parse(crd[3])) 
                            && frameElements.size() < 4096) {
                            boolean none = true;
                            for(Component elem : frameElements){
                                if(elem.getClass().equals(Node.class)){
                                    if(((Node)elem).getUniqueID().equals(Instant.parse(crd[3]))){
                                        none = false;
                                        p.setConnectedNode((Node)elem);
                                    }
                                }
                            }
                            
                            if(none){
                                p.setConnectedNodeID(Instant.parse(crd[3]));
                                connCheck.add(p);
                            }
                        }
                        
                        p.setBounds(
                            Integer.parseInt(crd[4]),Integer.parseInt(crd[5]),
                            Integer.parseInt(crd[6]),Integer.parseInt(crd[7])
                            );
                        //Formulas...
                        
                        
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
                scan.close();
                for(Node nod : connCheck){
                    List<Component> frameElements = Arrays.asList(pan.getComponents());
                    for(Component elem : frameElements){
                        if(elem.getClass().equals(Node.class)){
                            Node z = (Node)elem;
                            if(z.getUniqueID().equals(nod.getConnectedNodeID())){
                                nod.setConnectedNode(z);
                            }
                        }
                    }
                }
                fram.repaint();
            }
            catch(Exception a){}
        }
        if(e.getActionCommand() == "Find"){}
        if(e.getActionCommand() == "Settings"){}
        
        if(e.getActionCommand() == "Fit Window"){}
        if(e.getActionCommand() == "Zoom In"){}
        if(e.getActionCommand() == "Zoom Out"){}
        if(e.getActionCommand() == "Set Scaling.."){}
        if(e.getActionCommand() == "View As.."){}
        if(e.getActionCommand() == "Tutorial"){}
        
        if(e.getActionCommand() == "Verify"){}
        if(e.getActionCommand() == "Display Sample Formula"){}
        if(e.getActionCommand() == "Display Sample Tree"){}
        
        if(e.getActionCommand() == "Add Game Node"){
            //System.out.println("Creating Node");
            Node p = new Node();
            p.setPanel(pan, menu);
            pan.add(p);
            fram.repaint();
            fram.repaint();
        }
        if(e.getActionCommand() == "Remove Game Node"){
            List<Component> frameElements = Arrays.asList(pan.getComponents());
            for(Component elem : frameElements){
                if(elem.getForeground() == Color.red){
                    Node p = (Node) elem;
                    p.removeConnections();
                    pan.remove(elem);
                }
            }
            fram.repaint();
        }
        if(e.getActionCommand() == "Remove All Node"){
            List<Component> frameElements = Arrays.asList(pan.getComponents());
            for(Component elem : frameElements){
                pan.remove(elem);
            }
            fram.repaint();
        }
        
        if(e.getActionCommand() == "Components"){}
        if(e.getActionCommand() == "Documentation"){}
        if(e.getActionCommand() == "About"){}
    }
}


