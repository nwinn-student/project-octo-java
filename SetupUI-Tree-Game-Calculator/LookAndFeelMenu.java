
/**
 * Write a description of class n here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.*;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JMenu;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

//From Notepad provided by javapoint.
//https://www.javatpoint.com/notepad
class LookAndFeelMenu{
    
    public static void createLookAndFeelMenuItem(JMenu jmenu,Component cmp){
        final UIManager.LookAndFeelInfo[] infos=UIManager.getInstalledLookAndFeels();
        
        JRadioButtonMenuItem rbm[]=new JRadioButtonMenuItem[infos.length];
        ButtonGroup bg=new ButtonGroup();
        JMenu tmp=new JMenu("Change Look and Feel");
        tmp.setMnemonic('C');
        for(int i=0; i<infos.length; i++){
            rbm[i]=new JRadioButtonMenuItem(infos[i].getName());
            rbm[i].setMnemonic(infos[i].getName().charAt(0));
            tmp.add(rbm[i]);
            bg.add(rbm[i]);
            rbm[i].addActionListener(new LookAndFeelMenuListener(infos[i].getClassName(),cmp));
        }
        
        rbm[0].setSelected(true);
        jmenu.add(tmp);
    
    }
    
    }
class LookAndFeelMenuListener implements ActionListener{
    String classname;
    Component jf;
    /////////////////////
    LookAndFeelMenuListener(String cln,Component jf){
        this.jf=jf;
        classname=new String(cln);
    }
    /////////////////////
    public void actionPerformed(ActionEvent ev){
        try{
            UIManager.setLookAndFeel(classname);
            SwingUtilities.updateComponentTreeUI(jf);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    ///////////////////////////////
}
