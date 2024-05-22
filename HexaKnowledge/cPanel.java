
/**
 * Write a description of class Panel here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.awt.*;
import javax.swing.*;
public class cPanel extends JPanel{
    /**
     * Constructor for objects of class cPanel
     */
    public cPanel(){
        this.setPreferredSize(new Dimension(1000,500)); //dimension of panel and frame    
    }
    /**
     * Runs when JFrame's size is modified?
     */
    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        int randx; //will hold a random x coord
        int randy; //will hold a random y coord
        int num = 50; //1 v num
        
        int[] xcoords = new int[num+1]; //holds all x coordinates 
        int[] ycoords = new int[num+1]; //holds all y coordinates
        
        boolean DEBOUNCEx = false;
        boolean DEBOUNCEy = false;
        
        g2.setStroke(new BasicStroke(6));
        g2.drawLine(0,0,0,0);
        xcoords[0] = 0;
        ycoords[0] = 0;
        g2.setStroke(new BasicStroke(3));
        
        //gets all x coordinates, randomized
        for(int i = 1; i < num+1; i++){
            randx = (int) (Math.random() * 1000);
            //no replications
            for(int x: xcoords){ 
                if(x == randx){
                    DEBOUNCEx = true;
                    break;
                }
            }
            if(DEBOUNCEx == true){
                DEBOUNCEx = false;
                i--;
            }
            else{
                xcoords[i] = randx;
            }
        }
        
        //gets all y coordinates and plots the points, randomized
        for(int v = 1; v < num+1; v++){
            randy = (int) (Math.random() * 500);
            //no replications
            for(int x: ycoords){
                if(x == randy){
                    DEBOUNCEy = true;
                    break;
                }
            }
            if(DEBOUNCEy == true){
                DEBOUNCEy = false;
                v--;
            }
            else{
                ycoords[v] = randy;
                g2.drawLine(xcoords[v],ycoords[v],xcoords[v],ycoords[v]); //plots a point

            }
        }

        double dist[][] = new double[num+1][num+1];
        // [row][column]
        g2.setStroke(new BasicStroke(1));
        
        //distance from point (x,y) to any point
        for(int i = 0; i < num+1; i++){
            for(int v = 0; v < num+1; v++){
                //dist from point to all others
                dist[i][v] = Math.sqrt(Math.pow((xcoords[i]-xcoords[v]),2)+Math.pow((ycoords[i]-ycoords[v]),2));
            }
        }
        
        double max = Math.sqrt(Math.pow(1000,2)+Math.pow(500,2)); //largest possible value
        double temp = max;  //holds distance between
        int holder = 0; //used for holding final vector
        boolean DEB1 = false;   //debounces
        boolean DEB2 = false;   //debounces
        
        //origin to shortest distance, remove origin vector, point to shortest point, repeat
        int coL[] = new int[num+1]; //initial vectors
        int holders[] = new int[num+1]; //final vectors
        
        //calculates the shortest distances for each, removing the previous shortests
        for(int i = 0; i < num+1; i++){
            for(int v = 0; v < num+1; v++){
                if(dist[i][v] < temp && dist[i][v] > 0 && v != 0){
                    
                    
                    //if 1 -> 9, then 9 !-> 1
                    //if i -> v, then v !-> i
                    
                    //ensure no repeats for v
                    for(int vec = 0; vec < num+1; vec++){
                        if(coL[vec] != 0 && holders[vec] != 0){
                            if(holders[vec] == v){
                                DEB2 = true;
                                break;
                            }
                            if(v == coL[vec]){
                                DEB1 = true;
                                //System.out.println("Held: "+holders[vec]+" Current: "+v);
                                break;
                            }
                            
                        }
                    }
                    //basically what is above but less efficient
                    // for(int y: holders){
                        // if(y == v || DEB1){
                            // DEB2 = true;
                            // break;
                        // }
                        // for(int x: coL){
                            // if(v == x){
                                // DEB1 = true;
                                
                                // break;
                            // }
                        // }
                        
                    // }
                    if(DEB1 == true || DEB2 == true){
                        DEB1 = false;
                        DEB2 = false;
                    }
                    else{
                        temp = dist[i][v];
                        holder = v;
                    }
                }
                //uncomment this if testing
                //System.out.println("i: "+i+" v: "+v+" dist: "+dist[i][v]);
            }
            temp = max;
            coL[i] = i;
            holders[i] = holder;
            g2.drawLine(xcoords[i],ycoords[i],xcoords[holder],ycoords[holder]);
            
            //uncomment this if testing
            //System.out.println("Taken: "+holder+" Final: "+dist[i][holder]);
            //System.out.println("Line drawn from vector "+i+" to vector "+holders[i]);
            //holder = 0;
        }
    }
}
