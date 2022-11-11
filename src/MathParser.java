
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class MathParser {
    private final List<Character> digits = Arrays.asList( '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' );
    private final List<Character> expressions = Arrays.asList( '+', '-', '*', '/' );
    private final List<Character> brackets = Arrays.asList( ')', '(' );


    double parse(String expression) {
        var tokens = new ArrayList<Token>( );
        for (int i = 0; i < expression.length( ); i++) {
            var character = expression.charAt( i );
            if ( digits.contains( character ) ) {
                String number = "";
                do {
                    character = expression.charAt( i );
                    number += character;
                    i++;
                } while ( i < expression.length( ) && digits.contains( expression.charAt( i ) ) );
                tokens.add( new NumberToken( number ) );
                i--;
                continue;
            } else if ( expressions.contains( character ) ) {
                if ( character == '/' || character == '*' )
                    tokens.add( new PriorityExpressionToken( character ) );
                else
                    tokens.add( new ExpressionToken( character ) );
                continue;
            } else if ( brackets.contains( character ) ) {
                if ( character == ')' )
                    tokens.add( new ClosingBracket( ) );
                else
                    tokens.add( new OpeningBracket( ) );
                continue;
            }
        }
        //System.out.println( tokens );
        return calculate( tokens );
    }

    private int findExpression(List<Token> tokens) {
        for (int i = 0; i < tokens.size( ); i++) {
            if ( tokens.get( i ) instanceof ExpressionToken )
                return i;
        }
        return -1;
    }

    List<Token> calculatePriority(List<Token> tokens) {
        List<Token> newTokens = new ArrayList<>( );
        for (int i = 0; i < tokens.size( ) - 1; i++) {
            Token token = tokens.get( i );
            if ( token instanceof PriorityExpressionToken ) {
                if ( tokens.get( i - 1 ) instanceof NumberToken && tokens.get( i + 1 ) instanceof NumberToken ) {
                    NumberToken numberToken;
                    if ( ((PriorityExpressionToken) token).getExpresion( ) == '/' ) {
                        numberToken = new NumberToken( ((NumberToken) tokens.get( i - 1 )).getValue( )
                                /
                                ((NumberToken) tokens.get( i + 1 )).getValue( ) );
                    } else {
                        numberToken = new NumberToken( ((NumberToken) tokens.get( i - 1 )).getValue( )
                                *
                                ((NumberToken) tokens.get( i + 1 )).getValue( ) );
                    }

                    tokens.remove( i - 1 );
                    tokens.add( i - 1, numberToken);
                    tokens.remove( i );
                    tokens.remove( i );
                    return calculatePriority( tokens );
                }
            }
        }
        return tokens;
    }

    List<Token> calculateLowPriority(List<Token> tokens) {
        List<Token> newTokens = new ArrayList<>( );
        for (int i = 0; i < tokens.size( ) - 1; i++) {
            Token token = tokens.get( i );
            if ( token instanceof ExpressionToken ) {
                if ( tokens.get( i - 1 ) instanceof NumberToken && tokens.get( i + 1 ) instanceof NumberToken ) {
                    NumberToken numberToken;
                    if ( ((ExpressionToken) token).getExpresion( ) == '+' ) {
                        numberToken = new NumberToken( ((NumberToken) tokens.get( i - 1 )).getValue( )
                                +
                                ((NumberToken) tokens.get( i + 1 )).getValue( ) );
                    } else {
                        numberToken = new NumberToken( ((NumberToken) tokens.get( i - 1 )).getValue( )
                                -
                                ((NumberToken) tokens.get( i + 1 )).getValue( ) );
                    }

                    tokens.remove( i - 1 );
                    tokens.add( i - 1, numberToken );
                    tokens.remove( i );
                    tokens.remove( i );
                    return calculateLowPriority( tokens );
                }
            }
        }
        return tokens;
    }

    private int findOpeningBracket(List<Token> tokens) {
        for (int i = 0; i < tokens.size( ); i++)
            if ( tokens.get( i ) instanceof OpeningBracket )
                return i;
        return -1;
    }

    private int findClosingBracket(List<Token> tokens, int from) {
        int ind = -1;
        for (int i = from + 1; i < tokens.size( ); i++)
            if ( tokens.get( i ) instanceof ClosingBracket )
                ind = i;
        return ind;
    }

    private List<Token> calculateInBrackets(List<Token> tokens) {
        var tmpTokes = new ArrayList<>( tokens );
        //System.out.println( tokens );
        int l = findOpeningBracket( tmpTokes );
        int r = findClosingBracket( tmpTokes, l );
        if ( (l == -1 && r != -1) || (l != -1 && r == -1))
            throw new IllegalArgumentException( "Error with amount of brackets" );
        if ( l == -1 || r == -1 )
            return tokens;
        var inBrackets = new ArrayList<>( tmpTokes.subList( l + 1, r ) );
        double value = calculate( inBrackets );
        int k = r - l + 1;
        while ( k > 0 ) {
            tmpTokes.remove( l );
            k--;
        }
        tmpTokes.add( l, new NumberToken(value) );
        //System.out.println( "tk aft calk" );

        //System.out.println( tmpTokes );
        return tmpTokes;
    }

    private double calculate(List<Token> tokens) {
        //System.out.println( tokens );
        tokens = calculateInBrackets( tokens );
        tokens = calculatePriority( tokens );
        tokens = calculateLowPriority( tokens );
        if ( tokens.size( ) != 1 )
            throw  new IllegalArgumentException( "Error in expression" );
        return ((NumberToken) tokens.get( 0 )).getValue( );
    }
}
