
/**
 * Write a description of class J here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.PrintWriter;
public class DocumentPlot
{
    public static void main(String[] args) throws FileNotFoundException{
        double rand;
        double base;
        double dist;
        double pi = 3.14159;
        PrintWriter out = new PrintWriter("output.txt.txt");
        for(int i = 0; i <= 200; i++){
            for(int v = 0; v <= 200; v++){
                rand = Math.random();
                dist = Math.sqrt(Math.pow(i, 2) + Math.pow(v, 2)) * .01;
                base = (2-dist)/Math.sqrt(pi*2) - Math.pow((2-dist), 3)/(24*Math.sqrt(pi*2)) + Math.pow((2-dist), 5)/(640*Math.sqrt(pi*2));
                if(rand < base){
                    out.printf("x");
                }
                else{
                    out.printf(" ");
                }
            }
            out.println();
        }
        out.close();
    }
}
