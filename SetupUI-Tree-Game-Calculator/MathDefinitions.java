import java.util.List;
import java.util.*;
import java.util.LinkedHashSet;

/**
 * Write a description of class MathDefinitions here.
 *
 * @author Noah Winn
 * @version (a version number or a date)
 */
public class MathDefinitions
{
    private List<Double> dataX;
    private List<Double> dataY;

    /**
     * Constructor for objects of class MathDefinitions
     */
    public MathDefinitions()
    {
        
    }

    public List<Double> getDataX(){
        return this.dataX;
    }
    
    public List<Double> getDataY(){
        return this.dataY;
    }
    
    public double[] minus(double[] first, double[] second){
        //First - Second
        if(first == null || second == null){
            double[] i = {};
            return i;
        }
        if(first.length != second.length){
            double[] i = {};
            return i;
        }
        for(int i = 0; i < first.length; i++){
            first[i] = first[i] - second[i];
        }
        
        return first;
    }
    
    public int[] max(int[] x){
        int max[] = {-1024,0};
        for(int i = 0; i < x.length; i++){
            if(x[i] > max[0]){
                max[0] = Math.max(x[i], max[0]);
                max[1] = i;
            }
        }
        return max;
    }
    public double[] max(double[] x){
        double max[] = {-2e10+1,0};
        for(int i = 0; i < x.length; i++){
            if(x[i] > max[0]){
                max[0] = Math.max(x[i], max[0]);
                max[1] = i;
            }
        }
        return max;
    }
    
    public int[] abs(int[] x){
        for(int i = 0; i < x.length; i++){
            x[i] = Math.abs(x[i]);
        }
        return x;
    }
    public double[] abs(double[] x){
        for(int i = 0; i < x.length; i++){
            x[i] = Math.abs(x[i]);
        }
        return x;
    }
    
    public int sign(int x){
        return x/Math.abs(x);
    }
    public int sign(double x){
        return (int)x / (int)Math.abs(x);
    }
    
    public int unit(int x){
        return (1+sign(x))/2;
    }
    public int unit(double x){
        return (1+sign(x))/2;
    }
    
    public double sigmoid(int x){
        return sigmoid((double)x);
    }
    public double sigmoid(double x){
        return 1/(1+Math.exp(-x));
    }
    
    public double tanh(int x){
        return tanh((double)x);
    }
    public double tanh(double x){
        return (Math.exp(x)-Math.exp(-x))/(Math.exp(x)+Math.exp(-x));
    }
    
    public double relu(int x){
        return Math.max(x, 0);
    }
    public double relu(double x){
        return Math.max(x, 0);
    }
    
    public double gelu(int x){
        return gelu((double)x);
    }
    public double gelu(double x){
        return 0.5*x*(1+erf(x/Math.sqrt(2)));
    }
    
    public double silu(int x){
        return silu((double)x);
    }
    public double silu(double x){
        return x / (1 + Math.exp(-x));
    }
    
    public double erf(int x){
        return erf((double)x);
    }
    public double erf(double x){
        double y = 2*x / Math.sqrt(Math.PI) - 2*x*x*x / 3*Math.sqrt(Math.PI);
        y += Math.pow(x,5)/5*Math.sqrt(Math.PI); //low accuracy
        return y;
    }
    
    public double softplus(int x){
        return softplus((double)x);
    }
    public double softplus(double x){
        return Math.log(1+Math.exp(x));
    }
    
    public double gaussian(int x){
        return gaussian((double)x);
    }
    public double gaussian(double x){
        return Math.exp(-Math.pow(x,2));
    }
    
    public double mish(int x){
        return mish((double)x);
    }
    public double mish(double x){
        return x*tanh(softplus(x));
    }
    
    /**
     * Source:
     *  https://stackoverflow.com/questions/63433335
     * Finds all possibilities based on the given inputs
     * 
     */
    public <T> List<Collection<T>> product(Collection<T> a, int r){
        List<Collection<T>> result = Collections.nCopies(1,Collections.emptyList());
        for(Collection<T> pool : Collections.nCopies(r, new LinkedHashSet<>(a))){
            List<Collection<T>> temp = new ArrayList<>();
            for(Collection<T> x: result){
                for(T y: pool){
                    Collection<T> z = new ArrayList<>(x);
                    z.add(y);
                    temp.add(z);
                }
            }
            result = temp;
        }
        return result;
    }
    
    
    /**
     * 
     * 
     */
    public void gradDescent(){
        //allow for multivariable
        
    }
    
    /**
     * Modifies the array to not result in errors
     * 
     * @params
     * @params
     * @return 
     */
    public void prepareData(List<Double> dataX, List<Double> dataY, String modifierX, String modifierY){
        if(dataX == null || dataX.size() == 0){
            return;
        }
        if(dataY == null || dataY.size() == 0){
            return;
        }
        if(dataX.size() != dataY.size()){
            return;
        }
        
        double xValue = 0;
        double yValue = 0;
        List<Double> hopefulDataX = new ArrayList<Double>();
        List<Double> hopefulDataY = new ArrayList<Double>();
        //Check each element within the array to ensure that 
        for(int i = 0; i < dataX.size(); i++){
            try{
                // From: https://en.wikipedia.org/wiki/Activation_function
                if(modifierX == "")
                    xValue = dataX.get(i);
                else if(modifierX == "log")
                    xValue = Math.log(dataX.get(i));
                
                if(modifierY == "")
                    yValue = dataY.get(i);
                else if(modifierY == "log")
                    yValue = Math.log(dataY.get(i));
                    
                //NaN
                if((xValue != xValue) || (yValue != yValue))
                    throw new Exception();
                if(Double.isInfinite(xValue) || Double.isInfinite(yValue))
                    throw new Exception();
                hopefulDataX.add(xValue);
                hopefulDataY.add(yValue);
            }
            catch(Exception e){
                
            }
        }
        
        
        this.dataX = hopefulDataX;
        this.dataY = hopefulDataY;
        
    }
    
    
    /**
     * Provided by: Dmitry Bychenko
     * https://stackoverflow.com/questions/28428365/how-to-find-correlation-between-two-integer-arrays-in-java
     * 
     * Calculates the correlation between two datasets.
     * 
     * @param   xs  list of x values
     * @param   ys  list of y values
     * @return  the correlation between x and y
     * @return 
     */
    public double Correlation(List<Double> xs, List<Double> ys, String modx, String mody) {
        if(xs == null || xs.size() == 0){
            return 3; //xs is null
        }
        if(ys == null || ys.size() == 0){
            return 3; //xs is null
        }
        if(xs.size() != ys.size()){
            return 3; //array size doesn't match
        }
        
        prepareData(xs, ys, modx, mody);
        
        xs = null;
        ys = null;
        xs = this.dataX;
        ys = this.dataY;
        
        
        
        double corr = calcCorr(xs, ys);
        
        
        return corr;
    }
    
    public double calcCorr(List<Double> xs, List<Double> ys){
        if(xs == null || xs.size() == 0){
            return 3; //xs is null
        }
        if(ys == null || ys.size() == 0){
            return 3; //xs is null
        }
        if(xs.size() != ys.size()){
            return 3; //array size doesn't match
        }

        
        double sx = 0.0;
        double sy = 0.0;
        double sxx = 0.0;
        double syy = 0.0;
        double sxy = 0.0;
    
        int n = xs.size();
    
        for(int i = 0; i < n; ++i) {
          double x = xs.get(i);
          double y = ys.get(i);
    
          sx += x;
          sy += y;
          sxx += x * x;
          syy += y * y;
          sxy += x * y;
        }
    
        // covariation
        double cov = sxy / n - sx * sy / n / n;
        // standard error of x
        double sigmax = Math.sqrt(sxx / n -  sx * sx / n / n);
        // standard error of y
        double sigmay = Math.sqrt(syy / n -  sy * sy / n / n);
    
        
        return cov / sigmax / sigmay;
    }
    
    
}
