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
 * @version 6/30/2024
 */
public class MenuBar implements ActionListener
{
    private JMenuBar menuBar = null;
    private Frame fram = null;
    private GroupSelector pan = null;
    private EditPopupMenu menu = null;
    private ActionManager actions = null;
    private Integer nodeIndex = 0;
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
        
        fileMenu = addMenu("File", menuBar, KeyEvent.VK_F, "Use Alt-F to select File."); //ALT-F
        editMenu = addMenu("Edit", menuBar, KeyEvent.VK_E, "Use Alt-E to select Edit."); //ALT-E
        viewMenu = addMenu("View", menuBar, KeyEvent.VK_V, "Use Alt-V to select View."); //ALT-V
        testMenu = addMenu("Test", menuBar, KeyEvent.VK_T, "Use Alt-T to select Test."); //ALT-T
        nodeMenu = addMenu("Node", menuBar, KeyEvent.VK_N, "Use Alt-N to select Node."); //ALT-N
        helpMenu = addMenu("Help", menuBar, KeyEvent.VK_H, "Use Alt-H to select Help."); //ALT-H
        
        LookAndFeelMenu.createLookAndFeelMenuItem(viewMenu,fram);
        
        addMenuItem("New", fileMenu, KeyEvent.VK_N, "Use Control-N to use New, creates a new file."); // CTRL N
        addMenuItem("Open", fileMenu, KeyEvent.VK_O, "Use Control-O to use Open, opens up a file selector, then the selected file in the current window."); // CTRL O
        addMenuItem("Save", fileMenu, KeyEvent.VK_S, "Use Control-S to use Save, ..."); // CTRL S
        addMenuItem("Save as", fileMenu, 0, "..."); 
        addMenuItem("Export", fileMenu, 0, "...");
        //addMenuItem("Print", fileMenu, KeyEvent.VK_P); // CTRL P
        addMenuItem("Exit", fileMenu, KeyEvent.VK_Q, "Use Control-Q to use Exit, closes the current window."); // CTRL Q
        
        addMenuItem("Undo", editMenu, KeyEvent.VK_Z, "Use Control-Z to use Undo, undoes the previous action."); // CTRL Z
        addMenuItem("Redo", editMenu, KeyEvent.VK_Y, "Use Control-Y to use Redo, redoes the previous undone action."); // CTRL Y
        addMenuItem("Duplicate", editMenu, KeyEvent.VK_D, "Use Control-D to use Duplicate, duplicates the selected nodes."); //CTRL D
        addMenuItem("Copy", editMenu, KeyEvent.VK_C, "Use Control-C to use Copy, copies the currently selected nodes."); // CTRL C
        addMenuItem("Cut", editMenu, KeyEvent.VK_X, "Use Control-X to use Cut, copies and removes the currently selected nodes."); // CTRL X
        addMenuItem("Paste", editMenu, KeyEvent.VK_V, "Use Control-V to use Paste, pastes in the previously copied or cut nodes."); // CTRL V
        addMenuItem("Find", editMenu, KeyEvent.VK_F, "Use Control-F to use Find, ..."); // CTRL F
        addMenuItem("Settings", editMenu, KeyEvent.VK_COMMA, "Use Control-Comma to use Settings, ..."); // CTRL ,
        
        addMenuItem("Fullscreen", viewMenu, KeyEvent.VK_F11, "Use F11 to fullscreen."); // f11, later
        addMenuItem("Zoom In", viewMenu, KeyEvent.VK_PLUS, "Use Control-Plus to use Zoom In, ...");
        addMenuItem("Zoom Out", viewMenu, KeyEvent.VK_MINUS, "Use Control-Minus to use Zoom Out, ...");
        addMenuItem("Set Scaling", viewMenu, 0, "...");
        addMenuItem("View as", viewMenu, 0, "...");
        addMenuItem("Tutorial", viewMenu, 0, "..."); //goes thru it
        
        addMenuItem("Verify", testMenu, 0, "..."); //checks formulas, or trees
        addMenuItem("Display Sample Formula", testMenu, 0, "...");
        addMenuItem("Display Sample Tree", testMenu, 0, "...");
        
        addMenuItem("Add Game Node", nodeMenu, 0, "Creates a new node.");
        addMenuItem("Remove Game Node", nodeMenu, KeyEvent.VK_DELETE, "Use Delete to use Remove Game Node, deletes the currently selected nodes.");
        addMenuItem("Remove All Node", nodeMenu, 0, "Deletes all of the current nodes on the screen.");
        
        addMenuItem("Components", helpMenu, 0, "...");
        addMenuItem("Documentation", helpMenu, 0, "...");
        addMenuItem("About", helpMenu, 0, "...");
        
        fram.setJMenuBar(menuBar);
    }
    
    /**
     * Used to make JMenus easier to create.
     * 
     * @param title, the name of the JMenu object to be created
     * @param parent, the JMenuBar to add the JMenu object to
     * @param key, the keyboard key necessary to activate the button w/o clicking
     * @param description, for screen readers to provide extra information
     */
    private JMenu addMenu(String title, JMenuBar parent, int key, String description){
        JMenu menu = new JMenu(title);
        if(key != 0){
             //Key is not undefined
            menu.setMnemonic(key);
        }
        menu.getAccessibleContext().setAccessibleName(title);
        menu.getAccessibleContext().setAccessibleDescription(description);
        parent.add(menu);
        return menu;
    }
    /**
     * Used to make JMenuItems easier to create.
     * 
     * @param title, the name of the JMenuItem object to be created
     * @param parent, the JMenu to add the JMenuItem object to
     * @param key, the keyboard key necessary to activate the button w/o clicking
     * @param description, for screen readers to provide extra information
     */
    private void addMenuItem(String title, JMenu parent, int key, String description){
        JMenuItem menuItem = new JMenuItem(title);
        if(key != 0){
            if(title.equals("Remove Game Node")){
               menuItem.setAccelerator(KeyStroke.getKeyStroke(key, 0));
            } else if(title.equals("Fullscreen")){
                menuItem.setAccelerator(KeyStroke.getKeyStroke(key, 0));
            } else{
                 //Key is not undefined, will always require CTRL
                menuItem.setAccelerator(KeyStroke.getKeyStroke(key, Event.CTRL_MASK));
            }
        }
        menuItem.addActionListener(this);
        menuItem.getAccessibleContext().setAccessibleName(title);
        menuItem.getAccessibleContext().setAccessibleDescription(description);
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
                } catch(FileNotFoundException err){
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
                } catch(FileNotFoundException err){
                    System.out.println("The system cannot find the file specified.\nAttempting to create a file...");
                    
                    // Should the file not be found, we must create it
                    try{
                        PrintWriter out = new PrintWriter(file.toString() + ".txt");
                        
                        //Add the contents***
                        out.close();
                    } catch(FileNotFoundException newerr){
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
                } catch(FileNotFoundException err){
                    System.out.println("The system cannot find the file specified.\nAttempting to create a file...");
                    
                    // Should the file not be found, we must create it
                    try{
                        PrintWriter out = new PrintWriter(file.toString() + ".txt");
                        
                        //Add the contents***
                        out.close();
                    } catch(FileNotFoundException newerr){
                        System.out.println("Failed to create a file.\n"+newerr);
                    }
                }
                System.out.println(file);
            }
        }
        else if (e.getActionCommand() == "Export"){}
        else if (e.getActionCommand() == "Exit"){
            //Ask if they want to save? Force save if already existing?
            System.exit(0);
        }
        
        else if (e.getActionCommand() == "Undo"){
            try {actions.undo();}
            catch(Exception a) {System.out.println("Error: "+a);}
        } else if (e.getActionCommand() == "Redo"){
            try {actions.redo();}
            catch(Exception a) {System.out.println("Error: "+a);}
        } else if (e.getActionCommand() == "Duplicate"){
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
            pan.repaint();
        } else if (e.getActionCommand() == "Copy"){
            List<Component> frameElements = Arrays.asList(pan.getComponents());
            try{
                PrintWriter out = new PrintWriter("clipboard.txt");
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
                pan.repaint();
                out.close();
            }
            catch(Exception a){}
        } else if (e.getActionCommand() == "Cut"){
            List<Component> frameElements = Arrays.asList(pan.getComponents());
            List<String> cElements = new ArrayList<>();
            try{
                PrintWriter out = new PrintWriter("clipboard.txt");
                if(frameElements.size() < 4096){
                    for(Component elem : frameElements){
                        if(elem.getForeground() == Color.red){
                            cElements.add(((Node)elem).toString());
                            out.println(elem);
                        }
                    }
                    for(Component elem : frameElements){
                        if(elem.getForeground() == Color.red){
                            Node p = (Node) elem;
                            p.setBorder(blackBorder);
                            p.setForeground(Color.black);
                            p.removeConnections();
                            pan.remove(elem);
                        }
                    }
                }
                out.close();
                if(!cElements.isEmpty())
                    actions.addUndoAbleAction("DLM"+cElements);
                pan.repaint();
            }
            catch(Exception a){}
        } else if (e.getActionCommand() == "Paste"){
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
                        
                        String[] crd = line.split(",");
                        p.setUniqueID(Instant.parse(crd[0]));
                        p.setName(crd[1]);

                        List<Component> frameElements = Arrays.asList(pan.getComponents());
                        if(!Instant.parse(crd[0]).equals(Instant.parse(crd[3])) 
                            && frameElements.size() < 4096) {
                            boolean none = true;
                            for(Component elem : frameElements){
                                if(elem.getClass().equals(Node.class)){
                                    if(((Node)elem).getUniqueID().equals(Instant.parse(crd[3]))){
                                        none = false;
                                        p.setParentNode((Node)elem);
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
                        p.updateConnectionPosition();
                        pan.add(p);
                    }
                }
                scan.close();
                for(Node nod : connCheck){
                    List<Component> frameElements = Arrays.asList(pan.getComponents());
                    for(Component elem : frameElements){
                        if(elem.getClass().equals(Node.class)){
                            Node z = (Node)elem;
                            if(z.getUniqueID().equals(nod.getConnectedNodeID())){
                                nod.setParentNode(z);
                            }
                        }
                    }
                }
                pan.repaint();
            } catch(Exception a){
                System.out.println(a);
            }
        }
        else if (e.getActionCommand() == "Find"){}
        else if (e.getActionCommand() == "Settings"){}
        
        else if (e.getActionCommand() == "Fullscreen"){
            if(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().isFullScreenSupported()){
                // There is currently an issue where the user can spam this and the screen flashes... not fun
                fram.dispose();
                fram.setUndecorated(!fram.isUndecorated());
                if(fram.isUndecorated()){
                    GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(fram);
                } else{
                    GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(null);
                    fram.updateScreensize();
                }
                fram.setVisible(true);
                fram.repaint();
                // Wait until the screen has actually properly loaded
            }
        }
        else if (e.getActionCommand() == "Zoom In"){}
        else if (e.getActionCommand() == "Zoom Out"){}
        else if (e.getActionCommand() == "Set Scaling.."){}
        else if (e.getActionCommand() == "View As.."){}
        else if (e.getActionCommand() == "Tutorial"){}
        
        else if (e.getActionCommand() == "Verify"){}
        else if (e.getActionCommand() == "Display Sample Formula"){}
        else if (e.getActionCommand() == "Display Sample Tree"){}
        
        else if (e.getActionCommand() == "Add Game Node"){
            Node p = new Node();
            p.setPanel(pan, menu);
            p.setName("#"+nodeIndex.toString());
            nodeIndex++;
            actions.addUndoAbleAction("MKS"+p.toString());
            pan.add(p);
            pan.repaint();
        } else if (e.getActionCommand() == "Remove Game Node"){
            List<Component> frameElements = Arrays.asList(pan.getComponents());
            List<String> cElements = new ArrayList<>();
            for(Component elem : frameElements){
                if(elem.getForeground() == Color.red){
                    Node p = (Node) elem;
                    cElements.add(p.toString());
                }
            }
            for(Component elem : frameElements){
                if(elem.getForeground() == Color.red){
                    ((Node)elem).removeConnections();
                    pan.remove(elem);
                }
            }
            if(!cElements.isEmpty())
                actions.addUndoAbleAction("DLM"+cElements);
            pan.repaint();
        } else if (e.getActionCommand() == "Remove All Node"){
            List<Component> frameElements = Arrays.asList(pan.getComponents());
            List<String> cElements = new ArrayList<>();
            for(Component elem : frameElements){
                if(elem.getForeground() == Color.red){
                    Node p = (Node) elem;
                    cElements.add(p.toString());
                }
            }
            for(Component elem : frameElements){
                if(elem.getForeground() == Color.red){
                    ((Node)elem).removeConnections();
                    pan.remove(elem);
                }
            }
            if(!cElements.isEmpty())
                actions.addUndoAbleAction("DLM"+cElements);
            pan.repaint();
        }
        
        else if (e.getActionCommand() == "Components"){}
        else if (e.getActionCommand() == "Documentation"){}
        else if (e.getActionCommand() == "About"){}
    }
}


