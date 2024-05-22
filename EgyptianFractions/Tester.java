
/**
 * Write a description of class Tester here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.math.BigInteger;
import java.math.BigDecimal;
public class Tester
{
    public static void main(String[] args){
        double x = 854297;
        double y = 7951368;
        BigInteger k = BigDecimal.valueOf(x).toBigInteger();
        BigInteger j = BigDecimal.valueOf(y).toBigInteger();
        EgyptianFraction frac = new EgyptianFraction();
        System.out.println("EF Result:"+frac.recursiveFraction(k,j,0));
        System.out.println("Actual Result:"+x/y);
        
        int[] op = {2,8,4,7};
        String[] operators = {"+","-","*","/"};
        int condition = 3;
        SymbolicRegression regy = new SymbolicRegression();
        System.out.println("Input: "+op[0]+","+op[1]+","+op[2]+","+op[3]+"\t Result: "+condition);
        System.out.println(regy.symbolicRegression(op,3,4,condition));
        
        final int N = 32;
        final int N2 = (N * (N - 1) / 2);
        final double STEP = 0.05;
        //entire set
        double[] xval = new double[N];
        double[] yval = new double[N];
        double[] r_y = new double[N2];
        ThieleInterpolation thiele = new ThieleInterpolation();
        //Fills in x and y values
        for(int i = 0; i < N; i++){
            xval[i] = i*STEP;
            yval[i] = Math.sin(xval[i]);
        }
        //Default for r
        for(int i = 0; i < N2; i++){
            r_y[i] = Double.NaN;
        }
        
        //Should output PI since sin^-1 1/2 = pi/6
        System.out.printf("Obtained: %16.14f%n",6*thiele.thieleValue(yval, xval, r_y, 0.5, 0));
        System.out.println("Actual Result: "+Math.PI);
        
        //System.out.println(eval("6/(1-5/7)"));
        
        //Should output the rational function of the input data
        //training set
        
        //testing set
        
        double p_value = (xval[0]-xval[1]) / (yval[0] - yval[1]);
        //test with 1,2,3,4,5,6,7,8,9,10 values
        for(int i = 0; i < 5; i++){
            //System.out.println(thiele.thieleFunction(xval, yval, r_y, i, ""));
            //System.out.println(p_value);
        }
        
        //Should output the polynomial function of the input data
        PolynomialInterpolation poly = new PolynomialInterpolation();
        //training set
        
        //testing set
        
        //test with 5 values
        //System.out.println(poly.polynomialFunction(xval,yval,"",5));
        //Expected: 
    }
    
}
