package jLibrary.interfaces;

import jLibrary.JNumber;
import jLibrary.JString;
import jLibrary.exceptions.WrongExecutionTypeException;
import jLibrary.exceptions.WrongInputTypeException;
import jLibrary.miscFunctions.MiscFunctions;
import jLibrary.typeEnumerable.ObjectTypes;

import java.util.ArrayList;
import java.util.List;

public interface NumberInterface extends JObjectBaseInterface {
   
    // region type functions

    /**
     * Object type query.
     * @return Returns true if the object is of the requested type, or else will return false.
     */
    default boolean isByte(){
        return getType().isByte();
    }

    /**
     * Object type query.
     * @return Returns true if the object is of the requested type, or else will return false.
     */
    default boolean isShort(){
        return getType().isShort();
    }

    /**
     * Object type query.
     * @return Returns true if the object is of the requested type, or else will return false.
     */
    default boolean isInteger(){
        return getType().isInteger();
    }

    /**
     * Object type query.
     * @return Returns true if the object is of the requested type, or else will return false.
     */
    default boolean isLong(){
        return getType().isLong();
    }

    /**
     * Object type query.
     * @return Returns true if the object is of the requested type, or else will return false.
     */
    default boolean isFloat(){
        return getType().isFloat();
    }

    /**
     * Object type query.
     * @return Returns true if the object is of the requested type, or else will return false.
     */
    default boolean isDouble(){
        return getType().isDouble();
    }

    /**
     * Object type query.
     * @return Returns true if the object is of the requested type, or else will return false.
     */
    default boolean isIntegralNumber(){
        return getType().isIntegralNumber();
    }

    /**
     * Object type query.
     * @return Returns true if the object is of the requested type, or else will return false.
     */
    default boolean isFloatingPointNumber(){
        return getType().isFloatingPointNumber();
    }
    
    // endregion

    // region value access0

    default JNumber jNumberValue() throws WrongExecutionTypeException{
        if(isNumber()){
            return JNumber.convertToJNumber(getBaseValue());
        }
        throw MiscFunctions.getWrongExecutionTypeException_forNumbers(getType());
    }

    default Number numberValue() throws WrongExecutionTypeException {
        if(isNumber()){
            return (Number)getBaseValue();
        }
        throw MiscFunctions.getWrongExecutionTypeException_forNumbers(getType());
    }

    // endregion
    
    // region casting

    default byte byteValue() throws WrongExecutionTypeException {
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return numberValue().byteValue();
    }

    default short shortValue() throws WrongExecutionTypeException {
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return numberValue().shortValue();
    }

    default int intValue() throws WrongExecutionTypeException {
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return numberValue().intValue();
    }

    default long longValue() throws WrongExecutionTypeException {
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return numberValue().longValue();
    }

    default float floatValue() throws WrongExecutionTypeException {
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return numberValue().floatValue();
    }

    default double doubleValue() throws WrongExecutionTypeException {
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return numberValue().doubleValue();
    }
    
    // endregion

    // region whole number functions

    default boolean isWholeNumber(){
        if(isNumber()) {
            return !hasDecimalComponent();
        }
        return false;
    }

    default boolean hasDecimalComponent(){

        if(isNumber()) {
            if (isDouble()) {
                return ((doubleValue() % 1.0d) != 0);
            } else if (isFloat()) {
                return ((floatValue() % 1.0f) != 0);
            }
        }

        return false;
    }

    // endregion

    // region can-cast functions

    default boolean canCastToByte(){
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return JNumber.canFitIntoAByte(this);
    }

    default boolean canCastToShort(){
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return JNumber.canFitIntoAShort(this);
    }

    default boolean canCastToInteger(){
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return JNumber.canFitIntoAInteger(this);
    }

    default boolean canCastToLong(){
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return JNumber.canFitIntoALong(this);
    }

    default boolean canCastToFloat(){
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return JNumber.canFitIntoAFloat(this);
    }

    default boolean canCastToDouble(){
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return JNumber.canFitIntoADouble(this);
    }

    // endregion

    // region setTo functions, contains declaration only methods

    Object setTo(byte inputValue);

    Object setTo(short inputValue);

    Object setTo(int inputValue);

    Object setTo(long inputValue);

    Object setTo(float inputValue);

    Object setTo(double inputValue);

    // endregion

    // region shrinking to smaller types

    default NumberInterface shrinkToSmallestDataType(){

        MiscFunctions.checkIfCanExecuteNumericMethods(this);

        boolean canGoSmaller = true;
        boolean recastDoubles = false;
        Number val = numberValue();
        ObjectTypes valType = MiscFunctions.getType(val);

        while (canGoSmaller){

            if(valType.isDouble()){
                if(hasDecimalComponent()) {
                    if(recastDoubles) {
                        if (canCastToFloat()) {
                            val = floatValue();
                            valType = ObjectTypes.Float;
                        } else {
                            canGoSmaller = false;
                        }
                    }
                    else{
                        canGoSmaller = false;
                    }
                }
                else {
                    if(canCastToInteger()) {
                        val = intValue();
                        valType = ObjectTypes.Integer;
                    }
                    else{
                        canGoSmaller = false;
                    }
                }
            }
            else if(valType.isFloat()){
                if(hasDecimalComponent()){
                    canGoSmaller = false;
                }
                else{
                    if(canCastToShort()){
                        val = shortValue();
                        valType = ObjectTypes.Short;
                    }
                    else{
                        canGoSmaller = false;
                    }
                }
            }
            else if(valType.isLong()){
                if(canCastToInteger()){
                    val = intValue();
                    valType = ObjectTypes.Integer;
                }
                else {
                    canGoSmaller = false;
                }
            }
            else if(valType.isInteger()){
                if(canCastToShort()){
                    val = shortValue();
                    valType = ObjectTypes.Short;
                }
                else {
                    canGoSmaller = false;
                }
            }
            else if(valType.isShort()){
                if(canCastToByte()){
                    val = byteValue();
                    valType = ObjectTypes.Byte;
                }
                canGoSmaller = false;
            }
            else if(valType.isByte()){
                canGoSmaller = false;
            }
        }

        if(valType == ObjectTypes.Byte){
            setTo(val.byteValue());
        }
        else if(valType == ObjectTypes.Short){
            setTo(val.shortValue());
        }
        else if(valType == ObjectTypes.Integer){
            setTo(val.intValue());
        }
        else if(valType == ObjectTypes.Long){
            setTo(val.longValue());
        }
        else if(valType == ObjectTypes.Float){
            setTo(val.floatValue());
        }
        else {
            setTo(val.doubleValue());
        }

        return this;
    }

    // endregion

    // region primitive equals overload

    default boolean equals(byte val){
        if(isNumber()){
            return byteValue() == val;
        }
        return false;
    }

    default boolean equals(short val){
        if(isNumber()){
            return shortValue() == val;
        }
        return false;
    }

    default boolean equals(int val){
        if(isNumber()){
            return intValue() == val;
        }
        return false;
    }

    default boolean equals(long val){
        if(isNumber()){
            return longValue() == val;
        }
        return false;
    }

    default boolean equals(float val){
        if(isNumber()){
            return floatValue() == val;
        }
        return false;
    }

    default boolean equals(double val){
        if(isNumber()){
            return doubleValue() == val;
        }
        return false;
    }

    // endregion

    // region add operations

    default Object add(Object obj2) {
        boolean thisIsNumber = isNumber();
        boolean thisIsString = getType().isString();

        if(obj2 == null){
           if(thisIsNumber){
               return numberValue();
           }
           else{
               return toString();
           }
        }

        boolean otherIsNumber = false;
        JNumber otherAsNumber = null;
        boolean otherIsString = false;
        String otherAsString = null;

        if(obj2 instanceof NumberInterface var){
            if(var.isNumber()){
                otherAsNumber = var.jNumberValue();
                otherIsNumber = true;
            }
            else if(var.getType().isString()){
                otherIsString = true;
                otherAsString = var.toString();
            }
            else{
                ArrayList<String> temp = new ArrayList<>(List.of(MiscFunctions.getNumberTypeNames()));
                temp.add(ObjectTypes.String.name());
                throw new WrongInputTypeException(
                        var.getType().name(),
                        temp.toArray(new String[0]));
            }
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
        boolean oneIsString = thisIsString || otherIsString;

        if(oneIsString){
            if(!otherIsString){
                otherAsString = otherAsNumber.toString();
            }
            return this.toString() + otherAsString;
        }
        else if(bothAreNumbers){
            JNumber temp = JNumber.add(jNumberValue(), otherAsNumber);
            temp.shrinkToSmallestDataType();
            return temp.numberValue();
        }
        else{
            return null;
        }
    }

    // endregion

    // region numeric operations using NumberInterfaces

    default Number subtract(NumberInterface num2) {
        if(!isNumber()){
            throw new WrongExecutionTypeException(this.getType().name(), ObjectTypes.Number.name());
        }

        if(!num2.isNumber()){
            throw new WrongInputTypeException(num2.getType().name(), ObjectTypes.Number.name());
        }

        JNumber temp = JNumber.subtract(jNumberValue(), num2.jNumberValue());
        temp.shrinkToSmallestDataType();
        return temp.numberValue();
    }

    default Number multiply(NumberInterface num2) {
        if(!isNumber()){
            throw new WrongExecutionTypeException(this.getType().name(), ObjectTypes.Number.name());
        }

        if(!num2.isNumber()){
            throw new WrongInputTypeException(num2.getType().name(), ObjectTypes.Number.name());
        }

        JNumber temp = JNumber.multiply(jNumberValue(), num2.jNumberValue());
        temp.shrinkToSmallestDataType();
        return temp.numberValue();
    }

    default Number divide(NumberInterface num2) {
        if(!isNumber()){
            throw new WrongExecutionTypeException(this.getType().name(), ObjectTypes.Number.name());
        }

        if(!num2.isNumber()){
            throw new WrongInputTypeException(num2.getType().name(), ObjectTypes.Number.name());
        }

        JNumber temp = JNumber.divide(jNumberValue(), num2.jNumberValue());
        temp.shrinkToSmallestDataType();
        return temp.numberValue();
    }

    default Number power(NumberInterface num2) {
        if(!isNumber()){
            throw new WrongExecutionTypeException(this.getType().name(), ObjectTypes.Number.name());
        }

        if(!num2.isNumber()){
            throw new WrongInputTypeException(num2.getType().name(), ObjectTypes.Number.name());
        }

        JNumber temp = JNumber.power(jNumberValue(), num2.jNumberValue());
        temp.shrinkToSmallestDataType();
        return temp.numberValue();
    }

    default Number modulo(NumberInterface num2) {
        if(!isNumber()){
            throw new WrongExecutionTypeException(this.getType().name(), ObjectTypes.Number.name());
        }

        if(!num2.isNumber()){
            throw new WrongInputTypeException(num2.getType().name(), ObjectTypes.Number.name());
        }

        JNumber temp = JNumber.modulo(jNumberValue(), num2.jNumberValue());
        temp.shrinkToSmallestDataType();
        return temp.numberValue();
    }

    default boolean equalTo(NumberInterface num2) {
        if(!isNumber()){
            throw new WrongExecutionTypeException(this.getType().name(), ObjectTypes.Number.name());
        }

        if(!num2.isNumber()){
            throw new WrongInputTypeException(num2.getType().name(), ObjectTypes.Number.name());
        }

        return JNumber.equalTo(jNumberValue(), num2.jNumberValue());
    }

    default boolean notEqualTo(NumberInterface num2) {
        return !equalTo(num2);
    }

    default boolean greaterThanOrEqualTo(NumberInterface num2) {
        if(!isNumber()){
            throw new WrongExecutionTypeException(this.getType().name(), ObjectTypes.Number.name());
        }

        if(!num2.isNumber()){
            throw new WrongInputTypeException(num2.getType().name(), ObjectTypes.Number.name());
        }

        return JNumber.greaterThanOrEqualTo(jNumberValue(), num2.jNumberValue());
    }

    default boolean lessThanOrEqualTo(NumberInterface num2) {
        if(!isNumber()){
            throw new WrongExecutionTypeException(this.getType().name(), ObjectTypes.Number.name());
        }

        if(!num2.isNumber()){
            throw new WrongInputTypeException(num2.getType().name(), ObjectTypes.Number.name());
        }

        return JNumber.lessThanOrEqualTo(jNumberValue(), num2.jNumberValue());
    }

    default boolean greaterThan(NumberInterface num2) {
        if(!isNumber()){
            throw new WrongExecutionTypeException(this.getType().name(), ObjectTypes.Number.name());
        }

        if(!num2.isNumber()){
            throw new WrongInputTypeException(num2.getType().name(), ObjectTypes.Number.name());
        }

        return JNumber.greaterThan(jNumberValue(), num2.jNumberValue());
    }

    default boolean lessThan(NumberInterface num2) {
        if(!isNumber()){
            throw new WrongExecutionTypeException(this.getType().name(), ObjectTypes.Number.name());
        }

        if(!num2.isNumber()){
            throw new WrongInputTypeException(num2.getType().name(), ObjectTypes.Number.name());
        }

        return JNumber.lessThan(jNumberValue(), num2.jNumberValue());
    }

    // endregion

    // region numeric operations using numbers

    default Number subtract(Number num2) {
        if(isNumber()) {
            return JNumber.subtract(jNumberValue(), JNumber.convertToJNumber(num2));
        }
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return null;
    }

    default Number multiply(Number num2) {
        if(isNumber()) {
            return JNumber.multiply(jNumberValue(), JNumber.convertToJNumber(num2));
        }
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return null;
    }

    default Number divide(Number num2) {
        if(isNumber()) {
            return JNumber.divide(jNumberValue(), JNumber.convertToJNumber(num2));
        }
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return null;
    }

    default Number power(Number num2) {
        if(isNumber()) {
            return JNumber.power(jNumberValue(), JNumber.convertToJNumber(num2));
        }
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return null;
    }

    default Number modulo(Number num2) {
        if(isNumber()) {
            return JNumber.modulo(jNumberValue(), JNumber.convertToJNumber(num2));
        }
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return null;
    }

    default boolean equalTo(Number num2) {
        if(isNumber()) {
            return JNumber.equalTo(jNumberValue(), JNumber.convertToJNumber(num2));
        }
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return false;
    }

    default boolean notEqualTo(Number num2) {
        if(isNumber()) {
            return JNumber.notEqualTo(jNumberValue(), JNumber.convertToJNumber(num2));
        }
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return false;
    }

    default boolean greaterThanOrEqualTo(Number num2) {
        if(isNumber()) {
            return JNumber.greaterThanOrEqualTo(jNumberValue(), JNumber.convertToJNumber(num2));
        }
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return false;
    }

    default boolean lessThanOrEqualTo(Number num2) {
        if(isNumber()) {
            return JNumber.lessThanOrEqualTo(jNumberValue(), JNumber.convertToJNumber(num2));
        }
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return false;
    }

    default boolean greaterThan(Number num2) {
        if(isNumber()) {
            return JNumber.greaterThan(jNumberValue(), JNumber.convertToJNumber(num2));
        }
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return false;
    }

    default boolean lessThan(Number num2) {
        if(isNumber()) {
            return JNumber.lessThan(jNumberValue(), JNumber.convertToJNumber(num2));
        }
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return false;
    }

    // endregion

    // region numeric operations using jNumbers

    default Number subtract(JNumber num2) {
        if(isNumber()) {
            return JNumber.subtract(jNumberValue(), num2);
        }
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return null;
    }

    default Number multiply(JNumber num2) {
        if(isNumber()) {
            return JNumber.multiply(jNumberValue(), num2);
        }
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return null;
    }

    default Number divide(JNumber num2) {
        if(isNumber()) {
            return JNumber.divide(jNumberValue(), num2);
        }
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return null;
    }

    default Number power(JNumber num2) {
        if(isNumber()) {
            return JNumber.power(jNumberValue(), num2);
        }
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return null;
    }

    default Number modulo(JNumber num2) {
        if(isNumber()) {
            return JNumber.modulo(jNumberValue(), num2);
        }
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return null;
    }

    default boolean equalTo(JNumber num2) {
        if(isNumber()) {
            return jNumberValue().equals(num2);
        }
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return false;
    }

    default boolean notEqualTo(JNumber num2) {
        if(isNumber()) {
            return  !jNumberValue().equals(num2);
        }
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return false;
    }

    default boolean greaterThanOrEqualTo(JNumber num2) {
        if(isNumber()) {
            return JNumber.greaterThanOrEqualTo(jNumberValue(), num2);
        }
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return false;
    }

    default boolean lessThanOrEqualTo(JNumber num2) {
        if(isNumber()) {
            return JNumber.lessThanOrEqualTo(jNumberValue(), num2);
        }
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return false;
    }

    default boolean greaterThan(JNumber num2) {
        if(isNumber()) {
            return JNumber.greaterThan(jNumberValue(), num2);
        }
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return false;
    }

    default boolean lessThan(JNumber num2) {
        if(isNumber()) {
            return JNumber.lessThan(jNumberValue(), num2);
        }
        MiscFunctions.checkIfCanExecuteNumericMethods(this);
        return false;
    }

    // endregion
}
