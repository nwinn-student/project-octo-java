
/**
 * Write a description of class EgyptianFractions here.
 *
 * @author Noah Winn
 * @version (a version number or a date)
 */
import java.math.BigInteger;
import java.math.BigDecimal;
public class EgyptianFraction
{
    // instance variables - replace the example below with your own
    //private int x;
    //private int y;

    /**
     * Constructor for objects of class EgyptianFractions
     */
    public EgyptianFraction()
    {
        // initialise instance variables
        //x = 1;
        //y = 2;
    }
    
    /**
     * Constructor for objects of class EgyptianFractions
     */
    public EgyptianFraction(int x, int y)
    {
        // initialise instance variables
        //this.x = x;
        //this.y = y;
    }
    
    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public String recursiveFraction(BigInteger x, BigInteger y, double frac)
    {
        // put your code here
        double vX = x.doubleValue();
        double vY = y.doubleValue();
        BigInteger j = BigDecimal.valueOf(-vY).toBigInteger();
        BigInteger a = j.mod(x);
        //System.out.println("X Output:"+a);
        double c = vY*Math.ceil(vY/vX);
        //System.out.println("Y Output:"+c);
        if(c >= 1.8*Math.pow(10,308)){
            return frac+"\nThere exist more fractions that are not yet computable.";
        }
        BigInteger k = BigDecimal.valueOf(c).toBigInteger();
        double fraction = 1/(Math.ceil(vY/vX));
        
        frac += fraction;
        System.out.println("Fraction Output:"+1+"/"+Math.ceil(vY/vX));
        if(a.doubleValue() <= 1){
            if(a.doubleValue() != 0){
                System.out.println("Fraction Output:"+a+"/"+c);
                frac += a.doubleValue()/c;
            }
            return ""+frac;
        }
        return recursiveFraction(a,k,frac);
    }
}
