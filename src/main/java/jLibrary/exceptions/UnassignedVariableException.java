package jLibrary.exceptions;

import jLibrary.miscFunctions.MiscFunctions;

public class UnassignedVariableException extends RuntimeException{
    String[] variables;
    String expression;

    private UnassignedVariableException(){

    }

    public UnassignedVariableException(String expression, String[] variables){
        super("There are unassigned variables in the expression \"" + expression + "\": " +
                MiscFunctions.concatenateStrings(variables, true, false)
        + ".");
        this.variables = new String[variables.length];
        System.arraycopy(variables, 0, this.variables, 0, variables.length);
        this.expression = expression;
    }

    public String[] getVariables(){
        return variables;
    }
}
