/**
 * ExpElem: an element of an expression - either a String (an operator) or a number
 *  An operator (or a named constant) is stored in the operator field.
 *  A number is stored in the value field, and "#" is stored in the operator field.
 */

public class ExpElem{
    public final String operator;
    public final double value;

    /** Construct an Expr given an operator */ 
    public ExpElem(String token){
        operator = token;
        value = Double.NaN;
    }

    /** Construct an ExpElem given a number */ 
    public ExpElem(double v){
        operator = "#";
        value = v;
    }

    /**
     * Check if the element is an operator
     */
    public boolean isOperator(){
        return !"#".equals(operator);
    }

    /**
     * Convert the ExpElem to a String representation
     */
    public String toString(){
        return (operator.equals("#")) ? String.valueOf(value) : operator;
    }


}
