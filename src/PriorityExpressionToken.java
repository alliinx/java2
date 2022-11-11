public class PriorityExpressionToken implements Token{
    private char expression;

    public PriorityExpressionToken(char exp) {
        this.expression=exp;
    }

    public char getExpresion(){
        return expression;
    }

    public void out(){ System.out.print( expression );}

}
