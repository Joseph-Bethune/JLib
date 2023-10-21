package jLibrary.typeEnumerable;

public enum ObjectTypes {
    Null, Boolean, Byte, Short, Integer, Long, Float, Double, String, Number, List, Set, Dictionary,
    Expression, Operator, Symbol, Bracket, Unknown;

    public String toString(){
        return name();
    }

    public static ObjectTypes fromString(CharSequence input){
        for(ObjectTypes type : ObjectTypes.values()){
            if(type.name().equalsIgnoreCase(input.toString())){
                return type;
            }
        }
        return Unknown;
    }

    public boolean isNull(){
        return this == Null;
    }

    public boolean isBoolean(){
        return this == Boolean;
    }

    public boolean isByte() {
        return this == Byte;
    }

    public boolean isShort() {
        return this == Short;
    }

    public boolean isInteger() {
        return this == Integer;
    }

    public boolean isLong() {
        return this == Long;
    }

    public boolean isFloat() {
        return this == Float;
    }

    public boolean isDouble() {
        return this == Double;
    }

    public boolean isString(){
        return this == String;
    }

    public boolean isNumber(){
        return (this == Number || isByte() || isShort() || isInteger() || isLong() || isFloat() || isDouble());
    }

    public boolean isList(){
        return this == List;
    }

    public boolean isSet(){
        return this == Set;
    }

    public boolean isDictionary(){
        return this == Dictionary;
    }

    public boolean isExpression(){
        return this == Expression;
    }

    public boolean isOperator(){
        return this == Operator;
    }

    public boolean isSymbol(){
        return (this == Symbol || isOperator() || isBracket());
    }

    public boolean isBracket(){
        return this == Bracket;
    }

    public boolean isPrimitive(){
        return (isBoolean() || isNumber() || isString() || isSymbol());
    }

    public boolean isIntegralNumber(){
        return (isByte() || isShort() || isInteger() || isLong());
    }

    public boolean isFloatingPointNumber(){
        return (isFloat() || isDouble());
    }

    public boolean isUnknown(){
        return this == Unknown;
    }
}
