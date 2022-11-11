public class ClosingBracket implements Token{
    private char bracket;

    public ClosingBracket() {
        this.bracket=')';
    }

    public String get(){
        return String.valueOf(bracket);
    }

    public void out(){ System.out.print( bracket );}
}
