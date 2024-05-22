
/**
 * Write a description of class ThieleInterpolation here.
 *
 * @author Noah Winn
 * @version (a version number or a date)
 */
public class ThieleInterpolation
{
    // instance variables - replace the example below with your own
    //https://github.com/jklappert/FireFly
    final private int N = 32;
    final private int N2 = (N * (N - 1) / 2);
    private String[] numerator = new String[N];
    private String[] denomenator = new String[N];
    /**
     * Constructor for objects of class ThieleInterpolation
     */
    public ThieleInterpolation()
    {
        // initialise instance variables
    }

    public double rho(double[] x, double[] y, double[] r, int i, int n){
        if(n < 0)   return 0;
        if(n == 0){  
            //numerator[i] += y[i];
            //System.out.println(y[i]);
            return y[i];
        }
        int idx = (N - 1 - n) * (N - 2) / 2 + i;
        if(r[idx] != r[idx]){
            r[idx] = (x[i] - x[i+n]) / (rho(x,y,r,i,n-1) - rho(x,y,r,i+1,n-1)) + rho(x,y,r,i+1,n-2);
        }
        //denomenator[i] += r[idx];
        //System.out.println(r[idx]);
        return r[idx];
    }
    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public double thieleValue(double[] x, double[] y, double[] r, double xin, int n)
    {
        // put your code here
        if(n > N - 1){
            return 1;
        }
        return rho(x,y,r,0,n) - rho(x,y,r,0,n-2) + (xin-x[n])/thieleValue(x,y,r,xin,n+1);
    }
    
    public String thieleFunction(double[] x, double[] y, double[] r, int numIterations, String output){
        //p_1(x,x_1) = p_1(x_1,x_2) + (x - x_2) / (p_2(x,x_1,x_2)-y_1)
        //Need to find p_1(x_1,x_2)
        if(numIterations == 0){
            return output;
        }
        
        output += rho(x,y,r,0,numIterations)+"x\n";
        
        return thieleFunction(x,y,r,numIterations-1, output);
    }
}
