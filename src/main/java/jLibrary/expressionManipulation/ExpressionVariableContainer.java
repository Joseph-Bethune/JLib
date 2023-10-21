package jLibrary.expressionManipulation;

import jLibrary.exceptions.WrongExecutionTypeException;
import jLibrary.JObject;
import jLibrary.typeEnumerable.ObjectTypes;

import java.util.ArrayList;
import java.util.Map;

public class ExpressionVariableContainer {

    public static final String className = "ExpressionVariableContainer";

    final JObject source;

    // region constructors

    private ExpressionVariableContainer(){
        source = null;
    }

    public ExpressionVariableContainer(JObject expressionJObject) throws WrongExecutionTypeException {
        source = expressionJObject;
        checkAndThrowWrongTypeError();
    }

    // endregion

    // region common error function

    private void checkAndThrowWrongTypeError() throws WrongExecutionTypeException {
        if(!source.getType().isExpression()) {
            throw new WrongExecutionTypeException(source.getTypeName(), ObjectTypes.Expression);
        }
    }

    // endregion

    // region source sub object

    private JObject getTargetSubObject(){
        return source.getValue(ExpressionObject._Variables);
    }

    // endregion

    // region common functions

    public ExpressionVariableObject getVariableObject(CharSequence variableName){
        return new ExpressionVariableObject(source, variableName);
    }

    public ExpressionVariableObject[] getAllVariableObjects(){
        ArrayList<ExpressionVariableObject> vars = new ArrayList<>();

        JObject variableRoot = getTargetSubObject();
        for(Map.Entry<String, JObject> entry : variableRoot.entrySet()){

            vars.add(new ExpressionVariableObject(source, entry.getKey()));
        }

        return vars.toArray(new ExpressionVariableObject[0]);
    }

    /**
     * Returns the names of each variable in the expression as a string array.
     */
    public String[] getExpressionVariableNames() {
        ArrayList<String> output = new ArrayList<>();
        JObject variableSub = getTargetSubObject();
        if(variableSub != null && variableSub.length() > 0){
            for(Map.Entry<String, JObject> entry : variableSub.entrySet()){
                output.add(entry.getKey());
            }
        }
        else{
            throw new RuntimeException("Expression has no variables but it still unresolved.");
        }
        return output.toArray(new String[0]);
    }

    public boolean isMissingVariableValues() {
        for(ExpressionVariableObject ele : getAllVariableObjects()){
            if(!ele.isAssigned()){
                return true;
            }
        }
        return false;
    }

    public String[] getNamesOfUnassignedVariables() {
        ArrayList<String> unassignedVariables = new ArrayList<>();

        for(ExpressionVariableObject ele : getAllVariableObjects()){
            if(!ele.isAssigned() || ele.getValue() == null){
                unassignedVariables.add(ele.getVariableName());
            }
        }
        return unassignedVariables.toArray(new String[0]);
    }

    // endregion

    // region getter/setter for variable values

    /**
     * Assigns the given value to the expression's variable.
     * @param variableName The target variable's name. This is the variable that will be assigned to.
     * @param newValue The new value to be assigned.
     */
    public void setExpressionVariableValue(String variableName, JObject newValue) {

        JObject variableRoot = getTargetSubObject();
        if(variableRoot != null){
            if(variableRoot.containsKey(variableName)){
                new ExpressionVariableObject(source, variableName).setValue(newValue);
            }
        }
        else{
            throw new RuntimeException("Expression has no variables but it still unresolved.");
        }
    }

    /**
     * Assigns multiple values to the corresponding variables. The method extracts whatever JObject is assigned to the
     * newValues object (or null if none can be found) at the key matching a variable name and assigns it to the
     * corresponding variable within the calling instance.
     * @param newValues The values to be assigned.
     */
    public void setExpressionVariableValues(JObject newValues) throws WrongExecutionTypeException {
        checkAndThrowWrongTypeError();

        for(Map.Entry<String, JObject> entry : newValues.entrySet()){
            this.setExpressionVariableValue(entry.getKey(), entry.getValue());
        }
    }

    public JObject getExpressionVariableValue(String variableName){
        checkAndThrowWrongTypeError();

        return new ExpressionVariableObject(source, variableName).getValue();
    }

    // endregion

    // region to string functions

    public String toDebugString(){
        return source.getValue(ExpressionObject._Variables).toShortDebugString();
    }

    public String toString(){
        return toDebugString();
    }

    // endregion
}
