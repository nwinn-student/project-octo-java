
/**
 * Write a description of class Monster here.
 *
 * @author Noah Winn
 * @version 6/21/2023
 */
public class Monster
{
    private double a;
    /**
     * Constructor
     */
    public Monster(){
    }
    /**
     * 
     */
    public Monster(double a){
        this.a = a;
    }
    /**
     * 
     */
    public double getA(){
        return this.a;
    }
    /**
     * 
     */
    public void setA(double a){
        this.a = a;
    }
    /**
     * 
     */
    public String listOfMobs(String[] list, int count){
        //gets different sections of the random number
        int t = (int) ((this.a * count)) * 3; //gets first 2 numbers after 0.
        int s = (int) (((this.a*count - t)* count)) * 3; //gets second 2 numbs
        int v = (int) ((((this.a*count - t)*count - s)* count)) * 3; //gets third 2 numbs
        //different approach for other 3
        int t2 = t*2;
        int s2 = s*2;
        int v2 = v*2;
        //ensures the randum numbers are not over count
        t = Math.abs(t%count);
        s = Math.abs(s%count);
        v = Math.abs(v%count);
        t2 = Math.abs(t2%count);
        s2 = Math.abs(s2%count);
        v2 = Math.abs(v2%count);
        
        //split by location, some locations can be paired, so specify with if statements.
        //possibly have tester check whether a certain character appears and log the word
        //after it (location), then send the list of locations here
        
        String text = "1:\t"+list[t]+"\n2:\t"+list[s]+"\n3:\t"+list[v]+"\n4:\t"+list[t2]+"\n5:\t"+list[s2]+"\n6:\t"+list[v2];
        return text;
    }
    /**
     * 
     */
    public String listOfVariants(String[] list, int count){
        int t = (int) ((this.a * count)); //gets first 2 numbers after 0.
        int s = (int) (((this.a*count - t)* count)); //gets second 2 numbs
        int v = (int) ((((this.a*count - t)*count - s)* count)); //gets third 2 numbs
        //different approach for other
        int t2 = t*2;
        //ensures the randum numbers are not over count
        t = t%count;
        s = s%count;
        v = v%count;
        t2 = t2%count;
        
        String text = "1:\t"+list[t]+"\n2:\t"+list[s]+"\n3:\t"+list[v]+"\n4:\t"+list[t2];
        return text;
    }
}
