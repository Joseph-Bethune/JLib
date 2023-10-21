package jLibrary.expressionManipulation;

import jLibrary.exceptions.WrongExecutionTypeException;
import jLibrary.JObject;
import jLibrary.typeEnumerable.ObjectTypes;

public class ExpressionVariableObject {
    public static final String className = "ExpressionVariableObject";
    public static final String _Value = "value";
    public static final String _OriginalSubstring = "originalSubstring";
    public static final String _ModifiedSubstring = "modifiedSubstring";
    public static final String _AllNestedVariables = "allNestedVariables";
    public static final String _RootNestedVariables = "rootNestedVariables";
    public static final String _IsAssigned = "isAssigned";

    final JObject source;
    final String varName;

    // region constructors

    private ExpressionVariableObject(){
        source = null;
        varName = null;
    }

    public ExpressionVariableObject(JObject expressionObject, CharSequence variableName){
        source = expressionObject;
        varName = variableName.toString();
        checkAndThrowWrongTypeError();
    }

    // endregion

    // region common error function

    private void checkAndThrowWrongTypeError() throws WrongExecutionTypeException {
        if(!source.getType().isExpression()) {
            throw new WrongExecutionTypeException(source.getTypeName(), ObjectTypes.Expression);
        }

        JObject temp = source.getValue(ExpressionObject._Variables);
        if(!temp.containsKey(varName)){
            throw new RuntimeException("Expression does not have a the variable " + varName);
        }
    }

    // endregion

    // region source sub object

    private JObject getTargetSubObject(){
        JObject temp = source.getValue(ExpressionObject._Variables);
        if(temp.containsKey(varName)){
            return temp.getValue(varName);
        }
        throw new RuntimeException("Expression does not have a the variable " + varName);
    }

    // endregion

    // region variable name

    public String getVariableName(){
        return varName;
    }

    // endregion

    // region variable value

    public JObject getValue(){
        return getTargetSubObject().getValue(_Value);
    }

    public void setValue(JObject newValue){
        if(newValue != null) {
            getTargetSubObject().setValue(_Value, newValue);
            setAssigned(true);
        }
        else{
            setAssigned(false);
        }
    }

    // endregion

    // region variable string : original and modified

    public String getOriginalSubstring(){
        return getTargetSubObject().getValue(_OriginalSubstring).toString();
    }

    public String getModifiedSubstring(){
        return getTargetSubObject().getValue(_ModifiedSubstring).toString();
    }

    public void setModifiedSubstring(CharSequence newValue){
        getTargetSubObject().setValue(_ModifiedSubstring, JObject.createStringJObject(newValue));
    }

    // endregion

    // region nested variables : root and all

    public String[] getAllNestedVariableNames(){
        return getTargetSubObject().getValue(_AllNestedVariables).toStringArray();
    }

    public String[] getRootNestedVariableNames(){
        return getTargetSubObject().getValue(_RootNestedVariables).toStringArray();
    }

    // endregion

    // region isAssigned flag

    public boolean isAssigned(){
        return getTargetSubObject().getValue(_IsAssigned).booleanValue();
    }

    public void setAssigned(boolean newValue){
        getTargetSubObject().setValue(_IsAssigned, new JObject(newValue));
    }

    // endregion

    // region to string functions

    public String toDebugString(){
        return source.getValue(ExpressionObject._Variables).getValue(varName).toShortDebugString();
    }

    public String toString(){
        return toDebugString();
    }

    // endregion
}
