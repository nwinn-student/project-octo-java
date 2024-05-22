import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.PrintWriter;

import java.lang.Math;
/**
 * Write a description of class Main here.
 *
 * @author Noah Winn
 * @version 3/4/2024
 */
public class GenerateModel{
    public static void main(String[] args) throws FileNotFoundException{
        File modelFile = new File("modelSample.txt");
        
        Scanner modelScan = new Scanner(modelFile);
        PrintWriter modelOut;
        
        Scanner scan;
        PrintWriter out;
        
        boolean isGenerateModel = true;
        boolean isSentModel = false;
        
        //Generates the models, only once**
        while(modelScan.hasNextLine()){
            modelScan.nextLine();
            isGenerateModel = false;
            break;
        }
        modelScan.close();
        
        if(isGenerateModel){
            modelOut = new PrintWriter("modelSample.txt");
            String inp = "";
            
            final int AMOUNT_PER_FAMILY = 10;
            
            //0: Linear, 1: Polynomial, 2: Rational, 3: Irrational,
            //4: Exponential, 5: Logarithmic
            for(int modelFamily = 0; modelFamily < 6; modelFamily++){
                for(int v = 0; v < AMOUNT_PER_FAMILY; v++){
                    if(modelFamily == 0){
                        double a = Math.round(Math.random() * 20 - 10);
                        double b = Math.round(Math.random() * 20 - 10);
                        inp = ""+a+"*x"+"+"+b;
                    }
                    if(modelFamily == 1){
                        int size = (int)(Math.random() * 5 + 1);
                        inp = "";
                        for(int k = 0; k < size; k++){
                            double a = Math.round(Math.random() * 20 - 10);
                            inp += ""+a+"*x^"+k;
                            
                            if(k+1 == size) break;
                            inp += "+";
                        }
                    }
                    if(modelFamily == 2){
                        int sizeTop = (int)(Math.random() * 3 +1);
                        int sizeBot = (int)(Math.random() * 3 +1);
                        inp = "";
                        inp += "(";
                        for(int k = 0; k < sizeTop; k++){
                            double a = Math.round(Math.random() * 20 - 10);
                            inp += ""+a+"*x^"+k;
                            if(k+1 == sizeTop) break;
                            inp += "+";
                        }
                        inp += ")/(";
                        for(int k = 0; k < sizeBot; k++){
                            double a = Math.round(Math.random() * 20 - 10);
                            inp += ""+a+"*x^"+k;
                            if(k+1 == sizeBot) break;
                            inp += "+";
                        }
                        inp += ")";
                    }
                    if(modelFamily == 3){
                         int size = (int)(Math.random() * 3 + 1);
                        inp = "";
                        for(int k = 0; k < size; k++){
                            double a = Math.round(Math.random() * 20 - 10);
                            double b = (double)(Math.round((Math.random()+1) * 200))/200;
                            inp += ""+a+"*x^"+b;
                            
                            if(k+1 == size) break;
                            inp += "+";
                        }
                    }
                    if(modelFamily == 4){
                         int size = (int)(Math.random() * 3 + 1);
                        inp = "";
                        for(int k = 0; k < size; k++){
                            double a = Math.round(Math.random() * 20 - 10);
                            double b = Math.round(Math.random() * 10);
                            inp += ""+a+"*log("+b+"x)";
                            
                            if(k+1 == size) break;
                            inp += "+";
                        }
                    }
                    if(modelFamily == 5){
                        int size = (int)(Math.random() * 5 + 1);
                        inp = "";
                        for(int k = 0; k < size; k++){
                            double a = Math.round(Math.random() * 20 - 10);
                            double b = Math.round(Math.random() * 9 + 1);
                            inp += ""+a+"*"+b+"^x";
                            
                            if(k+1 == size) break;
                            inp += "+";
                        }
                    }
                    modelOut.println(modelFamily+":"+v+".\t"+inp);
                }
            }
            modelOut.close();
        }
        
        
        //Asks which model the dataset will be created for
        scan = new Scanner(System.in);
        System.out.println("Enter model number: Updates data-points.cvs.");
        //scan.next()
        
        scan.close();
        
        
        //Creates the dataset
        if(isSentModel){
            out = new PrintWriter("data-points.csv");
            
            
        
            out.close();
        }
    }
}
