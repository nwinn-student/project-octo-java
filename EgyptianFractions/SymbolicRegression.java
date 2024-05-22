
/**
 * Write a description of class SymbolicRegression here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

public class SymbolicRegression
{
    
    public SymbolicRegression()
    {
        // initialise instance variables
        
    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public String symbolicRegression(int[] operands, int numOperators, int numOperands, double condition)
    {
        
        String result = "Undetermined Function"; //output**, default value
        String[] holderO = {"+","-","*","/"}; //storage in case custom operators
        String[] o = {"+", "-","*","/"};
        String[] rand = new String[numOperators];
        int[] randOp = new int[numOperands];
        int[] indexHolder = {
            Math.max(numOperators,numOperands),Math.max(numOperators,numOperands),
            Math.max(numOperators,numOperands),Math.max(numOperators,numOperands)
        }; 
        Object[] res = {0,0,0,0,0,0,0,0,0,0,0};
        for(int i = 0; i < 1000; i++){
            //different starters
            //rand
            for(int j = 0; j < numOperators; j++){
                rand[j] = o[(int)(o.length*Math.random())];
            }
            for(int j = 0; j < numOperands; j++){
                int index = (int)(operands.length*Math.random());
                int count = 0;
                
                if(index != indexHolder[0] && index != indexHolder[1] && index != indexHolder[2] && index != indexHolder[3]){
                    randOp[j] = operands[index];
                    indexHolder[j] = index;
                }
                else{
                    while(index == indexHolder[0] || index == indexHolder[1] || index == indexHolder[2] || index == indexHolder[3]){
                        index = count;
                        count++;
                    }
                    count = 0;
                    randOp[j] = operands[index];
                    indexHolder[j] = index;
                }
            }
            indexHolder[0] = Math.max(numOperators,numOperands);
            indexHolder[1] = Math.max(numOperators,numOperands);
            indexHolder[2] = Math.max(numOperators,numOperands);
            indexHolder[3] = Math.max(numOperators,numOperands);
                
            //System.out.println(randOp[0]+rand[0]+randOp[1]+rand[1]+randOp[2]+rand[2]+randOp[3]);
            
            //highly custom, would need a for loop in practice
            res[0] = eval(randOp[0]+rand[0]+randOp[1]+rand[1]+randOp[2]+rand[2]+randOp[3]);
            res[1] = eval(randOp[0]+rand[0]+"("+randOp[1]+rand[1]+randOp[2]+rand[2]+randOp[3]+")");
            res[2] = eval("("+randOp[0]+rand[0]+randOp[1]+")"+rand[1]+"("+randOp[2]+rand[2]+randOp[3]+")");
            res[3] = eval("("+randOp[0]+rand[0]+randOp[1]+rand[1]+randOp[2]+")"+rand[2]+randOp[3]);
            res[4] = eval(randOp[0]+rand[0]+randOp[1]+rand[1]+"("+randOp[2]+rand[2]+randOp[3]+")");
            res[5] = eval(randOp[0]+rand[0]+"("+randOp[1]+rand[1]+randOp[2]+")"+rand[2]+randOp[3]);
            res[6] = eval("("+randOp[0]+rand[0]+randOp[1]+")"+rand[1]+randOp[2]+rand[2]+randOp[3]);
            res[7] = eval("(("+randOp[0]+rand[0]+randOp[1]+")"+rand[1]+randOp[2]+")"+rand[2]+randOp[3]);
            res[8] = eval("("+randOp[0]+rand[0]+"("+randOp[1]+rand[1]+randOp[2]+"))"+rand[2]+randOp[3]);
            res[9] = eval(randOp[0]+rand[0]+"(("+randOp[1]+rand[1]+randOp[2]+")"+rand[2]+randOp[3]+")");
            res[10] = eval(randOp[0]+rand[0]+"("+randOp[1]+rand[1]+"("+randOp[2]+rand[2]+randOp[3]+"))");
            
            //System.out.println(res[6].equals((Object) condition));
            //will need a for loop in practice
            // a o b o c o d
            if(res[0].equals((Object)condition)){
                result = randOp[0]+rand[0]+randOp[1]+rand[1]+randOp[2]+rand[2]+randOp[3];
                break;
            }
            // a o (b o c o d)
            if(res[1].equals((Object)condition)){
                result = randOp[0]+rand[0]+"("+randOp[1]+rand[1]+randOp[2]+rand[2]+randOp[3]+")";
                break;
            }
            // (a o b) o (c o d)
            if(res[2].equals((Object)condition)){
                result = "("+randOp[0]+rand[0]+randOp[1]+")"+rand[1]+"("+randOp[2]+rand[2]+randOp[3]+")";
                break;
            }
            // (a o b o c) o d
            if(res[3].equals((Object)condition)){
                result = "("+randOp[0]+rand[0]+randOp[1]+rand[1]+randOp[2]+")"+rand[2]+randOp[3];
                break;
            }
            // a o b o (c o d)
            if(res[4].equals((Object)condition)){
                result = randOp[0]+rand[0]+randOp[1]+rand[1]+"("+randOp[2]+rand[2]+randOp[3]+")";
                break;
            }
            // a o (b o c) o d
            if(res[5].equals((Object)condition)){
                result = randOp[0]+rand[0]+"("+randOp[1]+rand[1]+randOp[2]+")"+rand[2]+randOp[3];
                break;
            }
            // (a o b) o c o d
            if(res[6].equals((Object)condition)){
                result = "("+randOp[0]+rand[0]+randOp[1]+")"+rand[1]+randOp[2]+rand[2]+randOp[3];
                break;
            }
            // ((a o b) o c) o d
            if(res[7].equals((Object)condition)){
                result = "(("+randOp[0]+rand[0]+randOp[1]+")"+rand[1]+randOp[2]+")"+rand[2]+randOp[3];
                break;
            }
            // (a o (b o c)) o d
            if(res[8].equals((Object)condition)){
                result = "("+randOp[0]+rand[0]+"("+randOp[1]+rand[1]+randOp[2]+"))"+rand[2]+randOp[3];
                break;
            }
            // a o ((b o c) o d)
            if(res[9].equals((Object)condition)){
                result = randOp[0]+rand[0]+"(("+randOp[1]+rand[1]+randOp[2]+")"+rand[2]+randOp[3]+")";
                break;
            }
            // a o (b o (c o d))
            if(res[10].equals((Object)condition)){
                result = randOp[0]+rand[0]+"("+randOp[1]+rand[1]+"("+randOp[2]+rand[2]+randOp[3]+"))";
                break;
            }
            // there are more probably...
        }
        return result+" = "+condition;
    }
    //Provided by Boann on StackOverflow, 
    //https://stackoverflow.com/questions/3422673/how-to-evaluate-a-math-expression-given-in-string-form
    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;
            
            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }
            
            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }
            
            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }
            
            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)` | number
            //        | functionName `(` expression `)` | functionName factor
            //        | factor `^` factor
            
            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }
            
            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }
            
            double parseFactor() {
                if (eat('+')) return +parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus
                
                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    if (!eat(')')) throw new RuntimeException("Missing ')'");
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    if (eat('(')) {
                        x = parseExpression();
                        if (!eat(')')) throw new RuntimeException("Missing ')' after argument to " + func);
                    } else {
                        x = parseFactor();
                    }
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }
                
                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation
                
                return x;
            }
        }.parse();
    }
}
