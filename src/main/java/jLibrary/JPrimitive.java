package jLibrary;

import jLibrary.exceptions.WrongInputTypeException;
import jLibrary.interfaces.BooleanInterface;
import jLibrary.interfaces.NumberInterface;
import jLibrary.interfaces.StringInterface;
import jLibrary.miscFunctions.MiscFunctions;
import jLibrary.typeEnumerable.ObjectTypes;

import java.util.ArrayList;
import java.util.List;

import static jLibrary.miscFunctions.MiscFunctions.print;

/**
 * This class is used to parse "simple" strings.
 * <br>
 * A "simple" string contains no operators, brackets or punctuation.
 * @author Joseph Bethune
 */
public class JPrimitive implements NumberInterface, BooleanInterface, StringInterface {

    private Object value;
    private ObjectTypes type;

    // region constructors

    public JPrimitive() {
        clear();
    }

    public JPrimitive(boolean inputValue){
        setTo(inputValue);
    }

    public JPrimitive(char inputValue){
        setTo(inputValue);
    }

    public JPrimitive(byte inputValue){
        setTo(inputValue);
    }

    public JPrimitive(short inputValue){
        setTo(inputValue);
    }

    public JPrimitive(int inputValue){
        setTo(inputValue);
    }

    public JPrimitive(long inputValue){
        setTo(inputValue);
    }

    public JPrimitive(float inputValue){
        setTo(inputValue);
    }

    public JPrimitive(double inputValue){
        setTo(inputValue);
    }

    public JPrimitive(Object inputValue){
        setTo(inputValue);
    }

    // endregion

    // region clear and clone functions

    public void clear(){
        type = ObjectTypes.Null;

        value = null;
    }

    // endregion

    // region clone functions

    public JPrimitive clone(){

        JPrimitive output = new JPrimitive();

        output.type = this.type;
        output.value = getClonedValue();

        return output;
    }

    public Object getClonedValue(){
        if(isString() || isSymbol()){
            return toJString().clone();
        }
        else if(isNumber()){
            return JNumber.getClonedNumber(numberValue());
        }
        else if(isBoolean()){
            return booleanValue();
        }
        return value;
    }

    // endregion

    // region base value

    @Override
    public Object getBaseValue() {
        return value;
    }

    // endregion

    // region set to functions

    public JPrimitive setTo(boolean inputValue){
        clear();
        type = ObjectTypes.Boolean;
        value = inputValue;
        return this;
    }

    public JPrimitive setTo(char inputValue){
        clear();
        type = ObjectTypes.String;
        value = new JString(inputValue);
        return this;
    }

    public JPrimitive setTo(byte inputValue){
        clear();
        type = ObjectTypes.Byte;
        value = inputValue;
        return this;
    }

    public JPrimitive setTo(short inputValue){
        clear();
        type = ObjectTypes.Short;
        value = inputValue;
        return this;
    }

    public JPrimitive setTo(int inputValue){
        clear();
        type = ObjectTypes.Integer;
        value = inputValue;
        return this;
    }

    public JPrimitive setTo(long inputValue){
        clear();
        type = ObjectTypes.Long;
        value = inputValue;
        return this;
    }

    public JPrimitive setTo(float inputValue){
        clear();
        type = ObjectTypes.Float;
        value = inputValue;
        return this;
    }

    public JPrimitive setTo(double inputValue){
        clear();
        type = ObjectTypes.Double;
        value = inputValue;
        return this;
    }

    public JPrimitive setTo(Object inputValue){

        if(inputValue == null){
            clear();
            return this;
        }

        boolean alreadyShrunk = false;

        if(inputValue instanceof Number iv){
            JNumber temp = JNumber.convertToJNumber(iv);
            value = temp.getClonedNumber();
            type = temp.getType();
            alreadyShrunk = true;
        }
        else if(inputValue instanceof Boolean val){
            setTo(val.booleanValue());
        }
        else if(inputValue instanceof JString js){
            fromString(js);
        }
        else if(inputValue instanceof Character io){
            fromString(new JString(io));
        }
        else if(inputValue instanceof CharSequence io){
            fromString(new JString(io));
        }
        else if(inputValue instanceof JPrimitive io){
            this.type = io.type;
            value = io.getClonedValue();
        }
        else if(inputValue instanceof JObject io){

            if(io.isPrimitive() || io.isSymbol()){
                setTo(io.jPrimitiveValue());
            }
            else{
                throw new RuntimeException("Cannot set value from non primitive JObjects.");
            }
        }
        else if(inputValue instanceof NumberInterface io){
            if(io.isNumber()){
                setTo(io.numberValue());
            }
        }
        else if(inputValue instanceof BooleanInterface io){
            if(io.isBoolean()){
                setTo(io.booleanValue());
            }
        }
        else if(inputValue instanceof StringInterface io){
            if(io.isString()){
                fromString(io.toJString());
            }
        }
        else{
            throw new RuntimeException("Cannot set value from this type of object.");
        }

        if(isNumber()){
            if(!alreadyShrunk) {
                shrinkToSmallestDataType();
            }
        }

        return this;
    }

    // endregion

    // region equals functions

    @Override
    public boolean equals(Object inputValue){

        if(inputValue == null){
            return this.isNull();
        }

        if(inputValue == this){
            return true;
        }

        if(inputValue instanceof CharSequence iv){
            return equals(new JPrimitive(iv));
        }
        else if(inputValue instanceof Character iv){
            return equals(new JPrimitive(iv));
        }
        else if(inputValue instanceof JNumber iv){
            return equals(new JPrimitive(iv));
        }
        else if(inputValue instanceof JPrimitive io){

            if(!this.type.equals(io.type)){
                return false;
            }

            if(isBoolean()){
                return this.booleanValue() == io.booleanValue();
            }
            else if(isNumber()){
                return this.jNumberValue().equals(io.jNumberValue());
            }
            else{
                return this.toJString().equals(io.toJString());
            }
        }
        else if(inputValue instanceof JObject io){

            if(io.isPrimitive()){
                return equals(io.jPrimitiveValue());
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    // endregion

    // region get type name

    public String getTypeName(){
        return type.toString();
    }

    public ObjectTypes getType(){
        return type;
    }

    // endregion

    // region static operator functions

    public static JPrimitive add(JPrimitive var1, JPrimitive var2){

        MiscFunctions.nullCheck(var1, "var1");
        MiscFunctions.nullCheck(var2, "var2");
        return new JPrimitive(var1.add(var2));
    }

    public static JPrimitive subtract(JPrimitive var1, JPrimitive var2){
        MiscFunctions.nullCheck(var1, "var1");
        MiscFunctions.nullCheck(var2, "var2");
        return new JPrimitive(var1.subtract(var2));
    }

    public static JPrimitive multiply(JPrimitive var1, JPrimitive var2){
        MiscFunctions.nullCheck(var1, "var1");
        MiscFunctions.nullCheck(var2, "var2");
        return new JPrimitive(var1.multiply(var2));
    }

    public static JPrimitive divide(JPrimitive var1, JPrimitive var2){

        MiscFunctions.nullCheck(var1, "var1");
        MiscFunctions.nullCheck(var2, "var2");

        return new JPrimitive(var1.divide(var2));
    }

    public static JPrimitive power(JPrimitive var1, JPrimitive var2){
        MiscFunctions.nullCheck(var1, "var1");
        MiscFunctions.nullCheck(var2, "var2");

        return new JPrimitive(var1.power(var2));
    }

    public static JPrimitive modulo(JPrimitive var1, JPrimitive var2){
        MiscFunctions.nullCheck(var1, "var1");
        MiscFunctions.nullCheck(var2, "var2");

        return new JPrimitive(var1.modulo(var2));
    }

    public static JPrimitive greaterThanOrEqualTo(JPrimitive var1, JPrimitive var2){
        MiscFunctions.nullCheck(var1, "var1");
        MiscFunctions.nullCheck(var2, "var2");

        return new JPrimitive(var1.greaterThanOrEqualTo(var2));
    }

    public static JPrimitive lessThanOrEqualTo(JPrimitive var1, JPrimitive var2){
        MiscFunctions.nullCheck(var1, "var1");
        MiscFunctions.nullCheck(var2, "var2");

        return new JPrimitive(var1.lessThanOrEqualTo(var2));
    }

    public static JPrimitive greaterThan(JPrimitive var1, JPrimitive var2){
        MiscFunctions.nullCheck(var1, "var1");
        MiscFunctions.nullCheck(var2, "var2");

        return new JPrimitive(var1.greaterThan(var2));
    }

    public static JPrimitive lessThan(JPrimitive var1, JPrimitive var2){
        MiscFunctions.nullCheck(var1, "var1");
        MiscFunctions.nullCheck(var2, "var2");

        return new JPrimitive(var1.lessThan(var2));
    }

    public static JPrimitive equalTo(JPrimitive var1, JPrimitive var2){
        MiscFunctions.nullCheck(var1, "var1");
        MiscFunctions.nullCheck(var2, "var2");

        return new JPrimitive(var1.equalTo(var2));
    }

    public static JPrimitive notEqualTo(JPrimitive var1, JPrimitive var2){
        MiscFunctions.nullCheck(var1, "var1");
        MiscFunctions.nullCheck(var2, "var2");

        return new JPrimitive(var1.notEqualTo(var2));
    }

    public static JPrimitive or(JPrimitive var1, JPrimitive var2){
        MiscFunctions.nullCheck(var1, "var1");
        MiscFunctions.nullCheck(var2, "var2");

        return new JPrimitive(var1.booleanValue() || var2.booleanValue());
    }

    public static JPrimitive and(JPrimitive var1, JPrimitive var2){
        MiscFunctions.nullCheck(var1, "var1");
        MiscFunctions.nullCheck(var2, "var2");

        return new JPrimitive(var1.booleanValue() && var2.booleanValue());
    }

    // endregion

    // region add operations

    public JPrimitive add(Object obj2) {
        boolean thisIsNumber = isNumber();
        boolean thisIsString = isString();

        if(obj2 == null){
            if(isString()){
                return this.add("null");
            }
            return this;
        }

        if(thisIsString){
            return new JPrimitive(this.toJString().add(obj2));
        }

        boolean otherIsNumber = false;
        JNumber otherAsNumber = null;
        boolean otherIsString = false;
        JString otherAsString = null;

        if(obj2 instanceof JString var){
            otherIsString = true;
            otherAsString = var;
        }
        else if(obj2 instanceof NumberInterface var){
            if(var.isNumber()){
                otherAsNumber = var.jNumberValue();
                otherIsNumber = true;
            }
            else if(var.isString()){
                otherIsString = true;
                otherAsString = new JString(var);
            }
            else{
                ArrayList<String> temp = new ArrayList<>(List.of(MiscFunctions.getNumberTypeNames()));
                temp.add(ObjectTypes.String.name());
                throw new WrongInputTypeException(
                        var.getType().name(),
                        temp.toArray(new String[0]));
            }
        }
        else if(obj2 instanceof StringInterface var){
            return add(new JString(var));
        }
        else if(obj2 instanceof CharSequence str){
            return add(new JString(str));
        }
        else if(obj2 instanceof Number num){
            return add(new JNumber(num));
        }
        else{
            ArrayList<String> temp = new ArrayList<>(List.of(MiscFunctions.getNumberTypeNames()));
            temp.add(ObjectTypes.String.name());
            throw new WrongInputTypeException(
                    obj2.getClass().getTypeName(),
                    temp.toArray(new String[0]));
        }

        boolean bothAreNumbers = thisIsNumber && otherIsNumber;

        if(otherIsString){
            return new JPrimitive(this.toJString().add(otherAsString));
        }
        else if(bothAreNumbers){
            JNumber temp = JNumber.add(jNumberValue(), otherAsNumber);
            temp.shrinkToSmallestDataType();
            return new JPrimitive(temp.numberValue());
        }
        else{
            throw new RuntimeException("In order to perform that add() method, both this value and the other value " +
                    "have to be numbers or one has to be string.");
        }
    }

    // endregion

    // region value retrieval

    public String getOperatorValue(){
        if(isOperator()) {
            return value.toString();
        }
        return null;
    }

    // endregion

    // region isQuotedString function

    public boolean isNonQuotedString(){
        if(isString()){
            return !toJString().hasQuotes();
        }
        return false;
    }

    // endregion

    // region toJString functions

    @Override
    public JString toJString(){
        if(this.value instanceof JString js) {
            return js;
        }
        else{
            return new JString(value.toString());
        }
    }

    // endregion

    // region to string functions

    @Override
    public String toString(){
        if (isBoolean() || isNumber() || isSymbol()) {
            return value.toString();
        }
        else if(isString()) {
            return toString(false);
        }
        else {
            return null;
        }
    }

    public String toString(boolean includeQuotationMarks){
        if(isString()) {
            JString sb = toJString();
            boolean hasDoubleQuotes = sb.doMatchedBracketsEncapsulateEntireString("\"", "\"");
            boolean hasSingleQuotes = sb.doMatchedBracketsEncapsulateEntireString("'", "'");

            if(hasSingleQuotes || hasDoubleQuotes) {
                if(includeQuotationMarks){
                    return sb.toString();
                }
                else{
                    return sb.getSubSequence_asJString(1, sb.length()-1).toString();
                }
            }
            else{
                if(includeQuotationMarks) {
                    StringBuilder temp = new StringBuilder("\"");
                    temp.append(sb).append("\"");
                    return temp.toString();
                }
                else{
                    return sb.toString();
                }
            }
        }
        else {
            return toString();
        }
    }

    // endregion

    // region from string functions

    public JPrimitive fromString(CharSequence inputString){
        clear();

        if(inputString == null){
            return this;
        }

        JString inputValue = new JString(inputString);

        // numeric parse
        JNumber temp = JNumber.createFromString(inputValue);

        if(temp != null){
            type = temp.getType();
            value = temp;
            return this;
        }

        // bool parse
        if (inputValue.equalsIgnoreCase("true") || inputValue.equalsIgnoreCase("yes")) {
            value = true;
            type = ObjectTypes.Boolean;
            return this;
        } else if (inputValue.equalsIgnoreCase("false") || inputValue.equalsIgnoreCase("no")) {
            value = false;
            type = ObjectTypes.Boolean;
            return this;
        }

        // operator parsing
        if(MiscFunctions.doesStringArrayContain(MiscFunctions.getAllOperators(), inputValue)){
            value = inputValue;
            type = ObjectTypes.Operator;
            return this;
        }

        // bracket parsing
        if(MiscFunctions.doesStringArrayContain(MiscFunctions.getOpeningBrackets(), inputValue)){
            value = inputValue;
            type = ObjectTypes.Bracket;
            return this;
        }

        if(MiscFunctions.doesStringArrayContain(MiscFunctions.getClosingBrackets(), inputValue)){
            value = inputValue;
            type = ObjectTypes.Bracket;
            return this;
        }

        // symbol parsing
        if(MiscFunctions.doesStringArrayContain(MiscFunctions.getAllRecognizedSymbols(), inputValue)){
            value = inputValue;
            type = ObjectTypes.Symbol;
            return this;
        }

        // no parsing possible
        value = inputValue;
        type = ObjectTypes.String;
        return this;
    }

    public static JPrimitive createFromString(CharSequence inputValue){
        JPrimitive output = new JPrimitive();
        output.fromString(inputValue);
        return output;
    }

    // endregion

    // region debug string

    public String toDebugString(){
        StringBuilder s = new StringBuilder();

        s.append("{");

        s.append(JString.getQuotedJString("type")).append(" : ").append(type.toString());

        s.append(", ");
        s.append(JString.getQuotedJString("value")).append(" : ");
        if(isBoolean() || isNumber() || isSymbol()){
            s.append(value);
        }
        else if(isString()) {
            s.append(toJString().toDebugString());
        }

        s.append("}");

        return s.toString();
    }

    // endregion
}

