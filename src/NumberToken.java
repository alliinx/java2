public class NumberToken implements Token {
    private double digit;

    public NumberToken(String number) {
        this.digit= Double.parseDouble(number);
    }

    public NumberToken(double number) {
        this.digit=number;
    }

    public double getValue(){
        return digit;
    }

    public void out(){ System.out.print( digit );}
}
