
/**
 * Write a description of class ModelFromCSV here.
 *
 * @author Noah Winn
 * @version (a version number or a date)
 */
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;  
import java.io.FileReader;  
import java.io.IOException;  
import java.io.File;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.Collection;

public class ModelFromCSV
{
    private List<List<Double>> original_X;
    private List<Double> original_Y;
    private double[][] dataX;
    private double[] dataY;
    private String model;
    
    private char[] alph = {
        'a','b','c','d','e','f','g','h','i','j','k','l'
    };
    /**
     * Constructor for objects of class ModelFromCSV
     */
    public ModelFromCSV(){
        //
    }
    
    /**
     * Constructor for objects of class ModelFromCSV
     * 
     * @param file the csv file that holds the data you want to get a model from
     */
    public ModelFromCSV(File file) throws IOException{
        //File file = new File("yourfilehere");
        //Verify that it is a csv file.
        String[] extension = file.getPath().split("\\.");
        
        if(!extension[extension.length-1].equals("csv") && !extension[extension.length-1].equals("CSV"))
            return;
        extension = null;

        //Source: https://docs.oracle.com/javase/8/docs/api/java/lang/Double.html#valueOf-java.lang.String-
        final String Digits     = "(\\p{Digit}+)";
        final String HexDigits  = "(\\p{XDigit}+)";
        // an exponent is 'e' or 'E' followed by an optionally
        // signed decimal integer.
        final String Exp        = "[eE][+-]?"+Digits;
        final String fpRegex    =
              ("[\\x00-\\x20]*"+  // Optional leading "whitespace"
               "[+-]?(" + // Optional sign character
               "NaN|" +           // "NaN" string
               "Infinity|" +      // "Infinity" string
        
               // A decimal floating-point string representing a finite positive
               // number without a leading sign has at most five basic pieces:
               // Digits . Digits ExponentPart FloatTypeSuffix
               //
               // Since this method allows integer-only strings as input
               // in addition to strings of floating-point literals, the
               // two sub-patterns below are simplifications of the grammar
               // productions from section 3.10.2 of
               // The Java Language Specification.
        
               // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
               "((("+Digits+"(\\.)?("+Digits+"?)("+Exp+")?)|"+
        
               // . Digits ExponentPart_opt FloatTypeSuffix_opt
               "(\\.("+Digits+")("+Exp+")?)|"+
        
               // Hexadecimal strings
               "((" +
                // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
                "(0[xX]" + HexDigits + "(\\.)?)|" +
        
                // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
                "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +
        
                ")[pP][+-]?" + Digits + "))" +
               "[fFdD]?))" +
               "[\\x00-\\x20]*");// Optional trailing "whitespace"
        
        //Scan 1 line to check the number of columns
        
        // Now we can actually create the double arrays.
        
        
        //Scan through the whole file, change all NaN or empty into 0
        
        
        //Could just use List
        List<List<Double>> x = new ArrayList<List<Double>>();
        //x.add(new ArrayList<Double>());
        //x.get(0).add(1.0);
        List<Double> y = new ArrayList<Double>();
        
        //Still need to scan once, to do a column check, then continue
        Scanner scan = new Scanner(file);
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            String[] field = line.split(","); //since CSV
            if(x.isEmpty()){
  
                for(int i = 0; i < field.length-1; i++){
                    x.add(new ArrayList<Double>());
                    //for testing ONLY
                    //x.get(i).add((double)i); 
                }
                
            }
            //check to see if any field are "" or null or NaN or infinity
            for(int i = 0; i < field.length-1; i++){
                //here, gotta not let text pass
                
                if(field[i] == null || field[i] == "" || !Pattern.matches(fpRegex, field[i])){
                     x.get(i).add(null);
                }
                else{
                    x.get(i).add(Double.valueOf(field[i]));
                }    
            }
                
            String temp = field[field.length-1]; // speed up referencing
            //System.out.println(!Pattern.matches(fpRegex, temp));
            //System.out.println(temp);
            if(temp == null || temp == "" || !Pattern.matches(fpRegex, temp))
                y.add(null);
            else
                //still need to do more checking...
                y.add(Double.valueOf(temp));
            temp = null;
        
        }
        scan.close();
    
        //System.out.println("ArrayList : " + y.toString());
        this.original_X = x;
        this.original_Y = y;
        x = null;
        y = null;
    }
    
    private double beta;

    private double[] weights;

    private double learningRate = 0.001d;

    private int epochs;
    public double predict(double[] inputs) {
        if (inputs == null || inputs.length <= 0) {
            return 3;
        }

        double result = 0d;
        for (int i = 0; i < inputs.length; i++) {
            result = inputs[i] * weights[i] + result;
        }

        result = result + beta;

        return result;
    }

    
    /**
     * Source:
     * https://medium.com/analytics-vidhya/linear-regression-from-scratch-in-java-dc7aead2ba04
     * 
     * 
     */
    public void train(double[][] trainData, double[] result) {
  
        if (trainData == null || trainData.length <= 0) {
            throw new RuntimeException("Input data can not be null");
        }
        epochs = 10000;
        weights = new double[trainData[0].length];
        
        //System.out.println(trainData[0].length);
        //System.out.println(trainData.length);
        double prevmse = 0d;
        
        // Stochastic Gradient descent
        for (int e = 0; e < epochs; e++) {
            double mse = 0d;
            for (int i = 0; i < trainData.length; i++) {
                double[] tempInput = trainData[i];
                
                double predictedValue = predict(tempInput);
                
                double error = predictedValue - result[i];
                //System.out.println("i: "+i);
                mse = error * error + mse;
    
                for (int j = 0; j < weights.length; j++) {
                    //System.out.println("j: "+j);
                    weights[j] = weights[j] - learningRate * error * tempInput[j];
    
                }
                beta = beta - learningRate * error;
    
            }
    
            mse = (Math.sqrt(mse)) / trainData.length;
            
            if(prevmse == mse){
                System.out.println("Epoch: "+e);
                System.out.println(" MSE " + mse + " Weights " + Arrays.toString(weights) + " Beta " + beta);
                break;
            }
                
            prevmse = mse;
        }

        String linModel = "";
        for(int i = 0; i < weights.length; i++){
            
            linModel += alph[i]+" * "+weights[i]+" + ";
        }
        linModel += beta;
        System.out.println(linModel);
        this.model = linModel;
    }
    
    public String findModel(){
        MathDefinitions mdef = new MathDefinitions();
        
        //mdef.Correlation(original_X.get(0), original_Y, "","");
        // use product 2 for _,_ , _,log , log,_ , log,log
        //System.out.println(mdef.product(List.of("","log"), 2).getClass().getName());
        
        int SIZE = original_X.size() + 1; // CURRENTLY: 2, change to 3+ if multivariable
        // maybe??? bro im so confused
        
        List<Collection<String>> loginator = mdef.product(List.of("","log"), SIZE);
        //Object[] d = mdef.product(List.of("","log"), 2).toArray();
        double[] correlations = new double[loginator.size()];
        //double[] newCorrelations = new double[loginator.size()];
        
        List<List<Double>> dataX = new ArrayList<List<Double>>();
        List<List<Double>> dataY = new ArrayList<List<Double>>();
        
        for(int i = 0; i < loginator.size(); i++){
            Object[] miniLog = loginator.get(i).toArray();
            double a = mdef.Correlation(original_X.get(0), original_Y, (String)miniLog[0],(String)miniLog[1]);
            correlations[i] = a;
            System.out.println(a);
            //dataX.add(new ArrayList<Double>());
            dataX.add(mdef.getDataX());
            //dataY.add(new ArrayList<Double>());
            dataY.add(mdef.getDataY());
            
            //double corr2 = 0;
        
            //int start = 9 * dataX.get(i).size() / 10;
        
            //List<Double> xz = new ArrayList<Double>();
            //List<Double> yz = new ArrayList<Double>();
            
            //for(int v = start; v < dataX.get(i).size(); v++){
            //    xz.add(dataX.get(i).get(v));
            //    yz.add(dataY.get(i).get(v));
            //}
            //if(xz.size() > 3)
            //    newCorrelations[i] = mdef.Correlation(xz, yz,(String)miniLog[0],(String)miniLog[1]);
            //Limits calculations, 0.5 or less usually means not enough data or
            // the dataset needs to be tested using another method}
            
            //dataX.add(mdef.getDataX());
            //dataY.add(mdef.getDataY());
            
            
            miniLog = null;
        }
        //loginator = null;
        double[] maxCorr = mdef.max(mdef.abs(correlations));
        if(maxCorr[0] < 0.5){
            System.out.println("We must adjust the approach");
        }
        System.out.println("Max Correlation: "+correlations[(int)maxCorr[1]]);
        System.out.println(loginator.get((int)maxCorr[1]).toString());
        System.out.println(dataX.get((int)(maxCorr[1])).toString());
        System.out.println(dataY.get((int)(maxCorr[1])).toString());
        
        // SIZE - 1 since we added 1 for Y
        double[][] dx = new double[dataX.get((int)(maxCorr[1])).size()][SIZE - 1];
        int i = 0;
        for(double x : dataX.get((int)(maxCorr[1]))) {
            //Issue is that it won't get the other x values
            dx[i++][0] = x;
        }
        

        double[] dy = new double[dataY.get((int)(maxCorr[1])).size()];
        int v = 0;
        for(double y : dataY.get((int)(maxCorr[1]))) {
            dy[v++] = y;
        }
        
        train(dx,dy);
        //extract the weights
        double[] w = this.weights;
        //since we are looking for the highest order, 
        
        //beta is the constant
        
        // use product n for ??
        return "";
    }
    private static void trainModel(){
        double[][] trainSet = {{20,11},{16,5},{19.8,8},{18.4,15},{17.1,18},{15.5,20}};
        double[] result = {88.6,71.6,93.3,84.3,80.6,75.2};
        ModelFromCSV f = new ModelFromCSV();
        f.train(trainSet, result);
    }
 
    
    public static void main(String[] args){
        //trainModel();
        double[][] d = {
            {0,-53,-6,3,0,3,6,0,3},{}
        };
        double[] e = {
            0,-13,-6,3,2,6,6,0,3
        };
        File cv = new File("data-points.csv");
        try{
            ModelFromCSV f = new ModelFromCSV(cv);
            f.findModel();
        }
        catch(Exception l){
            System.out.println(l);
        }
        
        
        
        
        //System.out.println(d[0]);
    }
}
