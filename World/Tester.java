
/**
 * Write a description of class Tester here.
 *
 * @author Noah Winn
 * @version 6/21/2023
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.PrintWriter;
public class Tester
{
    public static void main(String args[]) throws Exception{
        double a;
        int count = 0;
        File inputFile = new File("monsterList.txt");
        FileInputStream fis = new FileInputStream(inputFile);
        Scanner in = new Scanner(inputFile);
        //in.useDelimiter("[^A-Za-z]+");
        
        byte[] byteAr = new byte[(int)inputFile.length()];
        fis.read(byteAr);
        String s = new String(byteAr);
        String[] wordsCount = s.split("\n");
        
        
        String[] locations = new String[wordsCount.length];
        int[] negatedPositions = new int[wordsCount.length];
        int coord = 0;
        
        for(int i = 0; i < wordsCount.length; i++){
            String[] temp = wordsCount[i].split(" ");
            wordsCount[i] = temp[0];
            if(wordsCount[i].equals("*")){
                negatedPositions[coord] = i;
                negatedPositions[coord+1] = i+1;
                locations[coord] = wordsCount[i+1];
                coord += 1;
                count-=2; //removes locations
            }
            count++; //counts the amount of words within a file
        }
        
        String[] text = new String[count]; //holds a max of count words
        System.out.println(count);
        for(int v = 0; v < wordsCount.length; v++){
            if(wordsCount[v].equals("*")){
                v+=2;
            }
            
            if(v < count){
                text[v] = wordsCount[v]; //adds the word into the text string[]
                System.out.println(v+": "+text[v]);
            }
        }
        
        PrintWriter out = new PrintWriter("output.txt");
        Monster[] b = new Monster[count]; 
        
        for(int v= 0; v <= 50; v++){
             a = Math.random();
             b[v] = new Monster(a);
             out.println("Region "+v+":\n"+b[v].listOfMobs(text, count-1)+"\n");
         }
        out.close();
        in.close();        
    }
}
