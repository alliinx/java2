import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        MathParser mathParser = new MathParser(  );
        System.out.println(mathParser.parse( "30+3*(2-5*(2-10*3))" ) );
    }

}