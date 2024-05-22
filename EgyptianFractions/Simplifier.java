
/**
 * Write a description of class Simplifier here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Simplifier
{
    // instance variables - replace the example below with your own
    private int x;

    /**
     * Constructor for objects of class Simplifier
     */
    public Simplifier()
    {
        // initialise instance variables
        x = 0;
    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public String fullFunc(String y)
    {
        // put your code here
        return x + y;
    }
    public String radicalExpr(String y){
        String sqrt = "sqrt(";
        //if nested in sqrt(x + sqrt(x^2-1)) -> sqrt((x+1)/2)+sqrt((x-1)/2)
        //sqrt(A+Bsqrt(q)) = 1/(2(Bq+d))(4qrt(4q(Bq+d)^2)+4qrt(4q(Bq+d)^2))^3)
        //where d^2 = B^2q^2-A^2q
        return "";
    }
}
