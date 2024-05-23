
/**
 * Write a description of class Frame here.
 *
 * @author Noah Winn
 * @version 5/23/2024
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.EventListener;
import javax.swing.event.EventListenerList;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

public class Frame extends JFrame implements ActionListener{
    // instance variables
    JMenuBar menuBar;
    
    JMenu fileMenu, editMenu, viewMenu, testMenu, nodeMenu, helpMenu;
    
    //FileMenu Items
    JMenuItem newItem, loadItem, 
        loadNewItem, saveItem, saveAsItem,
    //exporting diagram to PDF, PNG, or JPEG, and can go from diagram to TXT.
        exportItem, 
        exitItem;
    
    //EditMenu Items
    JMenuItem undoItem, redoItem, cutItem, copyItem, pasteItem,
        findItem, settingsItem;
    
    //ViewMenu Items
    JMenuItem fitItem, zoomInItem, zoomOutItem, zoomScaleItem, 
        viewItem, //May have multiple views, 1 for formulas and 1 for tree
        tutorialItem;
    
    //TestMenu Items: Ensures that the formulas OR/AND tree work properly.
    JMenuItem verifyItem, sampleFormulaItem, sampleTreeItem;
    
    //NodeMenu
    JMenuItem addNodeItem,removeNodeItem, //DELETE and BACKSPACE
        resetNodesItem;
    
    //HelpMenu Items
    JMenuItem componentItem, documentationItem, aboutItem;
    private GroupSelector pan3;
    private JButton createNode;
    JFileChooser chooser;
    private UndoManager undoManager = new UndoManager();
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
        //this.setLayout(null);
        this.setBounds(0,0,750,750);
        //this.setSize(750,750);
        this.setResizable(true);
        this.getContentPane().setBackground(new Color(200,255,255));
        
        pan3 = new GroupSelector(this);
        pan3.setLayout(null);
        pan3.setFocusable(false);
        
        //Creates menu components
        MenuBar menu = new MenuBar();
        menu.createMenuBar(this, pan3);
        
        
        
        
        //pan3.setBounds(0,0,300,300);
        //JScrollPane scroll = new JScrollPane(pan3);
        //scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        //JToolBar toolBar = new JToolBar();
        
        //createNode = new JButton("Create Node");
        //createNode.addActionListener(this);
        //createNode.setFocusable(false);
        
        //toolBar.add(createNode);
        ToolBar tool = new ToolBar();
        tool.createToolBar(this, pan3);
        
        this.add(pan3);
        //this.add(toolBar, BorderLayout.PAGE_START);
        
        this.setVisible(true);
        //this.pack();
        this.setLocationRelativeTo(null);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == createNode){
            
        }
    }
    
    //need to check tho
    // From https://stackoverflow.com/questions/3571223
    private String getFileExtension(File file){
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".") + 1;
        if(lastIndexOf == -1){
            return "";
        }
        return name.substring(lastIndexOf);
    }
    
    
}