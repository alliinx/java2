public class ExpressionToken implements Token{
    private char expression;

    public ExpressionToken(char exp) {
        this.expression=exp;
    }

    public char getExpresion(){
        return expression;
    }

    public void out(){ System.out.print( expression );}
}
