
/**
 * Write a description of class PolynomialInterpolation here.
 *
 * @author Noah Winn
 * @version (a version number or a date)
 */
public class PolynomialInterpolation
{
    // instance variables - replace the example below with your own

    /**
     * Constructor for objects of class PolynomialInterpolation
     */
    public PolynomialInterpolation()
    {
        // initialise instance variables
    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public double polynomialValue(double[] x, double[] y)
    {
        // put your code here
        return 0;
    }
    
    public String polynomialFunction(double[] x, double[] y, String result,int numIter){
        if(numIter == 0)  return result+" end.";
        String numerator = "";
        
        double denomConst = 1;
        String denom = "";
        for(int i = 0; i < numIter; i++){
            for(int j = 0; j < numIter; j++){
                if(i != j){
                    numerator += "(x - "+x[j]+")";
                    if(j == numIter - 1){
                        numerator += y[i];
                    }
                    denomConst *= x[i] - x[j];
                }
            }
            result += numerator + "/"+denomConst + "\n";
            numerator = "";
            denomConst = 1;
            if(i != numIter - 1)    result+=" + ";
        }
        //return polynomialFunction(x,y,result,numIter-1);
        return result;
    }
}
