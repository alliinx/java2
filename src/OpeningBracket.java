public class OpeningBracket implements Token{
    private char bracket;

    public OpeningBracket() {
        this.bracket='(';
    }

    public String get(){
        return String.valueOf(bracket);
    }

    public void out(){ System.out.print( bracket );}
}
