package jLibrary.expressionManipulation;

import jLibrary.exceptions.UnassignedVariableException;
import jLibrary.exceptions.UnrecognizedSymbolsException;
import jLibrary.exceptions.WrongExecutionTypeException;
import jLibrary.JObject;
import jLibrary.JString;
import jLibrary.typeEnumerable.ObjectTypes;

import java.util.ArrayList;

import static jLibrary.miscFunctions.MiscFunctions.print;

/**
 * Helper class for manipulating expression JObjects. Must be "linked" to an expression JObject to function.
 */
public class ExpressionObject {

    public static final String className = "Expression";
    public static final String _Variables = "Variables";
    public static final String _ExpressionString = "RawExpressionString";

    final JObject source;

    // region constructor

    private ExpressionObject(){
        source = null;
    }

    public ExpressionObject(JObject expressionJObject) throws WrongExecutionTypeException{
        source = expressionJObject;
        checkAndThrowWrongTypeError();
    }

    // endregion

    // region common exception function

    private void checkAndThrowWrongTypeError() throws WrongExecutionTypeException {
        if(!source.getType().isExpression()) {
            throw new WrongExecutionTypeException(source.getTypeName(), ObjectTypes.Expression);
        }
    }

    // endregion

    // region variable container access

    public ExpressionVariableContainer getVariableContainer(){
        return new ExpressionVariableContainer(source);
    }

    // endregion

    // region expression string access

    private String getRawExpressionString(){
        JObject output = source.getValue(ExpressionObject._ExpressionString);
        if(output != null && output.isString()){
            return output.toString();
        }
        throw new RuntimeException("Could not find expression string.");
    }

    public String getExpressionString(){
        return getRawExpressionString();
    }

    public String getExpressionString(boolean replaceVariables){
        if(replaceVariables){
            JString workingString = source.getValue(ExpressionObject._ExpressionString).toJString();

            ExpressionVariableObject[] variableObjectsArray = new ExpressionVariableContainer(this.source).getAllVariableObjects();

            //orders variables by name length, longest first
            ArrayList<ExpressionVariableObject> variableObjects = new ArrayList<>();
            for(ExpressionVariableObject newElement : variableObjectsArray){
                if(variableObjects.size() == 0){
                    variableObjects.add(newElement);
                }
                else{
                    boolean added = false;
                    for(int insertionPoint = 0; insertionPoint < variableObjects.size() && !added; insertionPoint += 1){
                        ExpressionVariableObject objectInList = variableObjects.get(insertionPoint);
                        if(newElement.varName.length() >= objectInList.varName.length()){
                            variableObjects.add(insertionPoint, newElement);
                            added = true;
                        }
                    }
                    if(!added) {
                        variableObjects.add(newElement);
                    }
                }
            }

            // replaces variable names within the working string with their values
            for(ExpressionVariableObject var : variableObjects){
                workingString.replaceEach(var.varName, var.getValue().toString());
            }

            return workingString.toString(false);
        }
        else{
            return source.getValue(ExpressionObject._ExpressionString).toString();
        }
    }

    // endregion

    // region common functions

    /**
     * Returns the names of each variable in the expression as a string array.
     */
    public String[] getExpressionVariableNames() throws WrongExecutionTypeException {
        return new ExpressionVariableContainer(this.source).getExpressionVariableNames();
    }

    public boolean isMissingVariableValues() throws WrongExecutionTypeException {
        return new ExpressionVariableContainer(this.source).isMissingVariableValues();
    }

    public String[] getNamesOfUnassignedVariables() throws WrongExecutionTypeException {
        return new ExpressionVariableContainer(this.source).getNamesOfUnassignedVariables();
    }

    /**
     * Assigns the given value to the expression's variable.
     * @param variableName The target variable's name. This is the variable that will be assigned to.
     * @param newValue The new value to be assigned.
     */
    public void setExpressionVariableValue(String variableName, JObject newValue)
            throws WrongExecutionTypeException {

        new ExpressionVariableContainer(this.source).setExpressionVariableValue(variableName, newValue);
    }

    /**
     * Assigns multiple values to the corresponding variables. The method extracts whatever JObject is assigned to the
     * newValues object (or null if none can be found) at the key matching a variable name and assigns it to the
     * corresponding variable within the calling instance.
     * @param newValues The values to be assigned.
     */
    public void setExpressionVariableValues(JObject newValues) throws WrongExecutionTypeException {
        new ExpressionVariableContainer(this.source).setExpressionVariableValues(newValues);
    }

    public JObject getExpressionVariableValue(String variableName){
        return new ExpressionVariableContainer(this.source).getExpressionVariableValue(variableName);
    }

    /**
     * If all variables have been assigned, then this method will replace each instance of the variable name within
     * the expression with the corresponding value, then evaluates the expression. If the expression evaluates correctly,
     * the result is saved into a new JObject, which is then returned by the method. The original, calling instance will
     * not be altered by this method.
     */
    public JObject evaluateExpression()
            throws WrongExecutionTypeException, UnassignedVariableException, UnrecognizedSymbolsException, IllegalArgumentException{

        checkAndThrowWrongTypeError();

        String[] unassignedVariables = getVariableContainer().getNamesOfUnassignedVariables();
        if(unassignedVariables.length > 0){
            throw new UnassignedVariableException(
                    getRawExpressionString(),
                    unassignedVariables);
        }

        String newExpressionString = getExpressionString(true);

        return new JObject(newExpressionString);
    }

    // endregion

    // region to string

    public String toDebugString(){
        return source.toShortDebugString();
    }

    public String toString(){
        return toDebugString();
    }

    // endregion
}
