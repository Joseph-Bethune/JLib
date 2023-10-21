package jLibrary;

import jLibrary.exceptions.FailedNumericParseException;
import jLibrary.exceptions.WrongExecutionTypeException;
import jLibrary.interfaces.NumberInterface;
import jLibrary.interfaces.JObjectBaseInterface;
import jLibrary.miscFunctions.MiscFunctions;
import jLibrary.typeEnumerable.ObjectTypes;

import static jLibrary.miscFunctions.MiscFunctions.endsWith;
import static jLibrary.miscFunctions.MiscFunctions.print;

/**
 * This class is used to store numeric values.
 * @author Joseph Bethune
 */
public class JNumber extends Number implements Comparable<Number>, NumberInterface {

    // number bit sizes
    //
    // integral
    //
    // byte 8 bit
    // short 16 bit
    // int 32 bit
    // long 64 bit
    //
    // floating point
    //
    // float 32 bit
    // double 64 bit

    private ObjectTypes type;
    private java.lang.Number numberVal;

    // region static conversion functions

    public static JNumber convertToJNumber(Object input){
        nullExceptionCheck(input, "input");
        if(input instanceof JNumber iv){
            return iv;
        }
        else{
            return new JNumber(input);
        }
    }

    // endregion

    // region exception prefabs

    private static IllegalArgumentException getException_IllegalArgument(CharSequence variableName){
        return new IllegalArgumentException(variableName+" is not an acceptable type");
    }

    private static void nullExceptionCheck(Object var, String name){
        if(var == null){
            throw new NullPointerException("variable " + name + " cannot be null");
        }
    }

    private static void illegalArgumentCheck_number(JObjectBaseInterface arg, String argName){
        if(!arg.isNumber()){
            throw new IllegalArgumentException(argName + " must be a numeric type.");
        }
    }

    private static void illegalArgumentCheck_numberOrString(JObjectBaseInterface arg, String argName){
        if(!arg.isNumber()){
            throw new IllegalArgumentException(argName + " must be a numeric or string type.");
        }
    }

    // endregion

    // region constructors

    public JNumber() {
        clear();
    }

    public JNumber(byte newByte) {
        setTo(newByte);
    }

    public JNumber(short newShort) {
        setTo(newShort);
    }

    public JNumber(int newInteger) {
        setTo(newInteger);
    }

    public JNumber(long newLong) {
        setTo(newLong);
    }

    public JNumber(float newFloat) {
        setTo(newFloat);
    }

    public JNumber(double newDouble) {
        setTo(newDouble);
    }

    public JNumber(Object newValue) throws FailedNumericParseException {
        setTo(newValue);
    }

    // endregion

    // region base value retrieval

    @Override
    public Object getBaseValue() {
        return getClonedNumber();
    }

    // endregion

    // region type functions

    public ObjectTypes getType(){
        return type;
    }

    // endregion

    // region get Number

    public Number numberValue(){
        return numberVal;
    }

    public JNumber jNumberValue(){
        return this;
    }

    // endregion

    // region whole number test

    public boolean isWholeNumber(){
        return !hasDecimalComponent();
    }

    public boolean hasDecimalComponent(){

        if(isDouble()){
            return ((doubleValue() % 1.0d) != 0);
        }
        else if(isFloat()){
            return ((floatValue() % 1.0f) != 0);
        }

        return false;
    }

    // endregion

    // region clear function

    public void clear() {
        type = ObjectTypes.Byte;
        numberVal = (byte) 0;
    }

    // endregion

    // region clone functions

    public static Number getClonedNumber(Number value){
        ObjectTypes type = MiscFunctions.getType(value);
        switch (type){
            case Byte->{
                return value.byteValue();
            }
            case Short->{
                return value.shortValue();
            }
            case Integer ->{
                return value.intValue();
            }
            case Long->{
                return value.longValue();
            }
            case Float->{
                return value.floatValue();
            }
            default->{
                return value.doubleValue();
            }
        }
    }

    public Number getClonedNumber(){
        return getClonedNumber(this);
    }

    public JNumber clone(){

        JNumber output = new JNumber();

        output.type = this.type;

        output.numberVal = getClonedNumber();

        return output;
    }

    // endregion

    // region setTo functions

    public JNumber setTo(byte newValue) {

        type = ObjectTypes.Byte;
        numberVal = newValue;
        return this;
    }

    public JNumber setTo(short newValue) {

        type = ObjectTypes.Short;
        numberVal = newValue;
        return this;
    }

    public JNumber setTo(int newValue) {

        type = ObjectTypes.Integer;
        numberVal = newValue;
        return this;
    }

    public JNumber setTo(long newValue) {

        type = ObjectTypes.Long;
        numberVal = newValue;
        return this;
    }

    public JNumber setTo(float newValue) {

        type = ObjectTypes.Float;
        numberVal = newValue;
        return this;
    }

    public JNumber setTo(double newValue) {

        type = ObjectTypes.Double;
        numberVal = newValue;
        return this;
    }

    public JNumber setTo(Object newValue) throws FailedNumericParseException {

        if(newValue == null){
            clear();
            return this;
        }

        boolean alreadyShrunk = false;

        if(newValue instanceof JNumber nv){

            type = nv.type;
            numberVal = nv.getClonedNumber();
        }
        else if(newValue instanceof Number nv){
            if(nv instanceof Byte nv2){
                setTo(nv2.byteValue());
            }
            else if(nv instanceof Short nv2){
                setTo(nv2.shortValue());
            }
            else if(nv instanceof Integer nv2){
                setTo(nv2.intValue());
            }
            else if(nv instanceof Long nv2){
                setTo(nv2.longValue());
            }
            else if(nv instanceof Float nv2){
                setTo(nv2.floatValue());
            }
            else if(nv instanceof Double nv2){
                setTo(nv2.doubleValue());
            }
            else{
                throw new RuntimeException("Unknown number type.");
            }
        }
        else if(newValue instanceof CharSequence s){
            if(!tryParse(s)) {
                throw new FailedNumericParseException(s);
            }
        }
        else if(newValue instanceof NumberInterface io){
            clear();

            if(io.isNumber()){
                setTo(io.jNumberValue());
            }
            else{
                throw new RuntimeException("Cannot set value from non numeric objects");
            }
        }else{
            throw new RuntimeException("Cannot set value from this type of object.");
        }
        if(!alreadyShrunk) {
            shrinkToSmallestDataType();
        }
        return this;
    }

    // endregion

    // region equals functions

    public boolean equals(byte val){
        return this.byteValue() == val;
    }

    public boolean equals(short val){
        return this.shortValue() == val;
    }

    public boolean equals(int val){
        return this.intValue() == val;
    }

    public boolean equals(long val){
        return this.longValue() == val;
    }

    public boolean equals(float val){
        return this.floatValue() == val;
    }

    public boolean equals(double val){
        return this.doubleValue() == val;
    }

    @Override
    public boolean equals(Object inputValue){

        if(inputValue == null){
            return false;
        }

        if(inputValue == this){
            return true;
        }

        if(inputValue instanceof Byte val){
            return byteValue() == val;
        }
        else if(inputValue instanceof Short val){
            return shortValue() == val;
        }
        else if(inputValue instanceof Integer val){
            return intValue() == val;
        }
        else if(inputValue instanceof Long val){
            return longValue() == val;
        }
        else if(inputValue instanceof Float val){
            return floatValue() == val;
        }
        else if(inputValue instanceof Double val){
            return doubleValue() == val;
        }
        else if(inputValue instanceof NumberInterface iv && iv.isNumber()){
            return equals(iv.numberValue());
        }
        else {
            return false;
        }
    }

    // endregion

    // region hash code

    /**
     * Generates a hash code for the JString
     * @return int : returns a hash code for the JString
     */
    @Override
    public int hashCode() {

        return numberVal.hashCode();
    }

    // endregion

    // region can cast functions

    public static boolean isTypeSmallerThenOrEqualToType(ObjectTypes type, ObjectTypes typeToFitInto){
        if(!type.isNumber()){
            throw new WrongExecutionTypeException(type, ObjectTypes.Number);
        }

        if(typeToFitInto == null){
            throw new NullPointerException("typeToFitInto must not be null");
        }

        if(type == typeToFitInto){
            return true;
        }

        if(typeToFitInto == ObjectTypes.Long){
            switch (type){
                case Integer:
                case Short :
                case Byte :
                    return true;
            }
        }
        else if(typeToFitInto == ObjectTypes.Integer){
            switch (type){
                case Short :
                case Byte :
                    return true;
            }
        }
        else if(typeToFitInto == ObjectTypes.Short){
            return type == ObjectTypes.Byte;
        }
        else if(typeToFitInto == ObjectTypes.Float){
            return true;
        }
        else if(typeToFitInto == ObjectTypes.Double){
            return true;
        }

        return false;
    }

    public static boolean canFitIntoAByte(NumberInterface num){
        if(isTypeSmallerThenOrEqualToType(num.getType(), ObjectTypes.Byte)){
            return true;
        }

        switch (num.getType()) {
            case Short -> {
                short val = num.shortValue();
                if (val < (short) Byte.MIN_VALUE || val > (short) Byte.MAX_VALUE) {
                    return false;
                }
            }
            case Integer -> {
                int val = num.intValue();
                if (val < (int) Byte.MIN_VALUE || val > (int) Byte.MAX_VALUE) {
                    return false;
                }
            }
            case Long -> {
                long val = num.longValue();
                if (val < (long) Byte.MIN_VALUE || val > (long) Byte.MAX_VALUE) {
                    return false;
                }
            }
            case Float -> {
                if (num.hasDecimalComponent()) {
                    return false;
                } else {
                    float val = num.floatValue();
                    if (val < (float) Byte.MIN_VALUE || val > (float) Byte.MAX_VALUE) {
                        return false;
                    }
                }
            }
            case Double -> {
                if (num.hasDecimalComponent()) {
                    return false;
                } else {
                    double val = num.doubleValue();
                    if (val < (double) Byte.MIN_VALUE || val > (double) Byte.MAX_VALUE) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public static boolean canFitIntoAShort(NumberInterface num){
        if(isTypeSmallerThenOrEqualToType(num.getType(), ObjectTypes.Short)){
            return true;
        }

        switch (num.getType()) {
            case Integer -> {
                int val = num.intValue();
                if (val < (int) Short.MIN_VALUE || val > (int) Short.MAX_VALUE) {
                    return false;
                }
            }
            case Long -> {
                long val = num.longValue();
                if (val < (long) Short.MIN_VALUE || val > (long) Short.MAX_VALUE) {
                    return false;
                }
            }
            case Float -> {
                if (num.hasDecimalComponent()) {
                    return false;
                } else {
                    float val = num.floatValue();
                    if (val < (float) Short.MIN_VALUE || val > (float) Short.MAX_VALUE) {
                        return false;
                    }
                }
            }
            case Double -> {
                if (num.hasDecimalComponent()) {
                    return false;
                } else {
                    double val = num.doubleValue();
                    if (val < (double) Short.MIN_VALUE || val > (double) Short.MAX_VALUE) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public static boolean canFitIntoAInteger(NumberInterface num){
        if(isTypeSmallerThenOrEqualToType(num.getType(), ObjectTypes.Integer)){
            return true;
        }

        switch (num.getType()) {
            case Long -> {
                long val = num.longValue();
                if (val < (long) Integer.MIN_VALUE || val > (long) Integer.MAX_VALUE) {
                    return false;
                }
            }
            case Float -> {
                if (num.hasDecimalComponent()) {
                    return false;
                } else {
                    float val = num.floatValue();
                    if (val < (float) Integer.MIN_VALUE || val > (float) Integer.MAX_VALUE) {
                        return false;
                    }
                }
            }
            case Double -> {
                if (num.hasDecimalComponent()) {
                    return false;
                } else {
                    double val = num.doubleValue();
                    if (val < (double) Integer.MIN_VALUE || val > (double) Integer.MAX_VALUE) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public static boolean canFitIntoALong(NumberInterface num){
        if(isTypeSmallerThenOrEqualToType(num.getType(), ObjectTypes.Long)){
            return true;
        }

        switch (num.getType()) {
            case Float -> {
                if (num.hasDecimalComponent()) {
                    return false;
                } else {
                    float val = num.floatValue();
                    if (val < Long.MIN_VALUE || val > Long.MAX_VALUE) {
                        return false;
                    }
                }
            }
            case Double -> {
                if(!num.hasDecimalComponent()){
                    double val = num.doubleValue();
                    if(val < Long.MIN_VALUE || val > Long.MAX_VALUE){
                        return false;
                    }
                }
                else{
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean canFitIntoAFloat(NumberInterface num){
        if(isTypeSmallerThenOrEqualToType(num.getType(), ObjectTypes.Float)){
            return true;
        }

        switch (num.getType()) {
            case Integer -> {
                int val = num.intValue();
                if (val < Float.MIN_VALUE || val > Float.MAX_VALUE) {
                    return false;
                }
            }
            case Long -> {
                long val = num.longValue();
                if (val < Float.MIN_VALUE || val > Float.MAX_VALUE) {
                    return false;
                }
            }
            case Double -> {
                double val = num.doubleValue();
                if (val < Float.MIN_VALUE || val > Float.MAX_VALUE) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean canFitIntoADouble(NumberInterface num){

        /*

        if(isTypeSmallerThenOrEqualToType(num, ObjectTypes.Double)){
            return true;
        }

        switch (num.getType()) {
            case Long -> {
                long val = num.longValue();
                if (val < Double.MIN_VALUE || val > Double.MAX_VALUE) {
                    return false;
                }
            }
        }

        //*/

        return true;
    }

    // endregion

    // region getting smallest common cast

    public static ObjectTypes getSmallestCommonCastType(ObjectTypes type1, ObjectTypes type2){
        if(!type1.isNumber()){
            throw new WrongExecutionTypeException(type1, ObjectTypes.Number);
        }

        if(!type2.isNumber()){
            throw new WrongExecutionTypeException(type2, ObjectTypes.Number);
        }

        if(type1 == type2){
            return type1;
        }

        switch (type1){
            case Double -> {
                switch (type2) {
                    case Long, Float, Integer, Short, Byte -> {
                        return type1;
                    }
                }
            }
            case Float -> {
                switch (type2) {
                    case Long, Integer, Short, Byte -> {
                        return type1;
                    }
                }
            }
            case Long -> {
                switch (type2) {
                    case Integer, Short, Byte -> {
                        return type1;
                    }
                }
            }
            case Integer -> {
                switch (type2) {
                    case Short, Byte -> {
                        return type1;
                    }
                }
            }
            case Short -> {
                if (type2 == ObjectTypes.Byte) {
                    return type1;
                }
            }
        }

        return type2;
    }

    public ObjectTypes getSmallestCommonCastType(NumberInterface num2){
        return getSmallestCommonCastType(this.getType(), num2.getType());
    }

    // endregion

    // region shrinking to smaller types

    public JNumber shrinkToSmallestDataType(){

        return NumberInterface.super.shrinkToSmallestDataType().jNumberValue();
    }

    public static JNumber shrinkToSmallestDataType(Number input){
        return convertToJNumber(input).shrinkToSmallestDataType();
    }

    // endregion

    // region Comparable implementation

    @Override
    public int compareTo(Number o) {
        if(o instanceof JNumber no){
            ObjectTypes commonType = getSmallestCommonCastType(this.getType(), no.getType());

            switch (commonType){
                case Double -> {
                    double val1 = this.doubleValue();
                    double val2 = no.doubleValue();
                    return Double.compare(val2, val1);
                }
                case Float -> {
                    float val1 = this.floatValue();
                    float val2 = no.floatValue();
                    return Float.compare(val2, val1);
                }
                case Long -> {
                    long val1 = this.longValue();
                    long val2 = no.longValue();
                    return Long.compare(val2, val1);
                }
                case Integer -> {
                    int val1 = this.intValue();
                    int val2 = no.intValue();
                    return Integer.compare(val2, val1);
                }
                case Short -> {
                    short val1 = this.shortValue();
                    short val2 = no.shortValue();
                    return Short.compare(val2, val1);
                }
                case Byte -> {
                    byte val1 = this.byteValue();
                    byte val2 = no.byteValue();
                    return Byte.compare(val2, val1);
                }
            }
        }
        else{
            return compareTo(new JNumber(o));
        }
        throw getException_IllegalArgument("o");
    }

    // endregion

    // region Number implementation

    @Override
    public byte byteValue(){
        return numberVal.byteValue();
    }

    @Override
    public short shortValue(){
        return numberVal.shortValue();
    }

    @Override
    public int intValue() {
        return numberVal.intValue();
    }

    @Override
    public long longValue() {
        return numberVal.longValue();
    }

    @Override
    public float floatValue() {
        return numberVal.floatValue();
    }

    @Override
    public double doubleValue() {
        return numberVal.doubleValue();
    }

    // endregion

    // region operations between this and another JNumbers
    // this is the core of all numeric operations
    // all number interface operations lead back to here

    private JNumber add_JNumber(JNumber num2){
        nullExceptionCheck(num2, "num2");

        ObjectTypes commonType = getSmallestCommonCastType(this.getType(), num2.getType());
        JNumber output = new JNumber();
        switch (commonType){
            case Byte ->{
                output.setTo(this.shortValue() + num2.shortValue());
            }
            case Short ->{
                output.setTo(this.intValue() + num2.intValue());
            }
            case Integer, Long ->{
                output.setTo(this.longValue() + num2.longValue());
            }
            default ->{
                output.setTo(this.doubleValue() + num2.doubleValue());
            }
        }
        output.shrinkToSmallestDataType();
        return output;
    }

    private JNumber subtract_JNumber(JNumber num2){
        nullExceptionCheck(num2, "num2");

        ObjectTypes type = JNumber.getSmallestCommonCastType(this.getType(), num2.getType());
        JNumber output = new JNumber();
        switch (type){
            case Byte->{
                output.setTo(this.byteValue() - num2.byteValue());
            }
            case Short->{
                output.setTo(this.shortValue() - num2.shortValue());
            }
            case Integer->{
                output.setTo(this.intValue() - num2.intValue());
            }
            case Long->{
                output.setTo(this.longValue() - num2.longValue());
            }
            case Float->{
                output.setTo(this.floatValue() - num2.floatValue());
            }
            default->{
                output.setTo(this.doubleValue() - num2.doubleValue());
            }
        }

        output.shrinkToSmallestDataType();

        return output;
    }

    private JNumber multiply_JNumber(JNumber num2){
        nullExceptionCheck(num2, "num2");

        ObjectTypes type = JNumber.getSmallestCommonCastType(this.getType(), num2.getType());
        JNumber output = new JNumber();

        switch (type){
            case Byte,Short,Integer,Long->{
                output.setTo(this.longValue() * num2.longValue());
            }
            default->{
                output.setTo(this.doubleValue() * num2.doubleValue());
            }
        }

        output.shrinkToSmallestDataType();

        return output;
    }

    private JNumber divide_JNumber(JNumber num2) throws IllegalArgumentException {
        nullExceptionCheck(num2, "num2");

        if(num2.doubleValue() == 0.0){
            throw new IllegalArgumentException("Cannot divide by zero.");
        }

        ObjectTypes type = JNumber.getSmallestCommonCastType(this.getType(), num2.getType());
        JNumber output = new JNumber();

        switch (type){
            case Byte, Short, Integer, Float ->{
                output.setTo(this.floatValue() / num2.floatValue());
            }
            default->{
                output.setTo(this.doubleValue() / num2.doubleValue());
            }
        }

        output.shrinkToSmallestDataType();

        return output;
    }

    private JNumber power_JNumber(JNumber num2){
        nullExceptionCheck(num2, "num2");

        JNumber output = new JNumber(Math.pow(this.doubleValue(), num2.doubleValue()));

        output.shrinkToSmallestDataType();

        return output;
    }

    private JNumber modulo_JNumber(JNumber num2) {
        nullExceptionCheck(num2, "num2");

        ObjectTypes type = JNumber.getSmallestCommonCastType(this.getType(), num2.getType());
        JNumber output = new JNumber();
        switch (type){
            case Byte,Short->{
                output.setTo(this.floatValue() % num2.floatValue());
            }
            default -> {
                output.setTo(this.doubleValue() % num2.doubleValue());
            }
        }

        output.shrinkToSmallestDataType();

        return output;
    }

    private  boolean equalTo_JNumber(JNumber num2){
        return this.equals(num2);
    }

    private boolean notEqualTo_JNumber(JNumber num2){
        return !equalTo_JNumber(num2);
    }

    private boolean greaterThanOrEqualTo_JNumber(JNumber num2) {
        nullExceptionCheck(num2, "num2");

        ObjectTypes type = JNumber.getSmallestCommonCastType(this.getType(), num2.getType());
        switch (type){
            case Byte->{
                return this.byteValue() >= num2.byteValue();
            }
            case Short->{
                return this.shortValue() >= num2.shortValue();
            }
            case Integer->{
                return this.intValue() >= num2.intValue();
            }
            case Long->{
                return this.longValue() >= num2.longValue();
            }
            case Float->{
                return this.floatValue() >= num2.floatValue();
            }
            case Double->{
                return this.doubleValue() >= num2.doubleValue();
            }
        }

        return false;
    }

    private boolean lessThanOrEqualTo_JNumber(JNumber num2) {
        nullExceptionCheck(num2, "num2");

        ObjectTypes type = JNumber.getSmallestCommonCastType(this.getType(), num2.getType());
        switch (type){
            case Byte->{
                return this.byteValue() <= num2.byteValue();
            }
            case Short->{
                return this.shortValue() <= num2.shortValue();
            }
            case Integer->{
                return this.intValue() <= num2.intValue();
            }
            case Long->{
                return this.longValue() <= num2.longValue();
            }
            case Float->{
                return this.floatValue() <= num2.floatValue();
            }
            case Double->{
                return this.doubleValue() <= num2.doubleValue();
            }
        }
        return false;
    }

    private boolean greaterThan_JNumber(JNumber num2) {
        nullExceptionCheck(num2, "num2");

        ObjectTypes type = JNumber.getSmallestCommonCastType(this.getType(), num2.getType());
        switch (type){
            case Byte -> {
                return this.byteValue() > num2.byteValue();
            }
            case Short->{
                return this.shortValue() > num2.shortValue();
            }
            case Integer->{
                return this.intValue() > num2.intValue();
            }
            case Long->{
                return this.longValue() > num2.longValue();
            }
            case Float->{
                return this.floatValue() > num2.floatValue();
            }
            case Double->{
                return this.doubleValue() > num2.doubleValue();
            }
        }

        return false;
    }

    private boolean lessThan_JNumber(JNumber num2) {
        nullExceptionCheck(num2, "num2");

        ObjectTypes type = JNumber.getSmallestCommonCastType(this.getType(), num2.getType());
        switch (type) {
            case Byte -> {
                return this.byteValue() < num2.byteValue();
            }
            case Short -> {
                return this.shortValue() < num2.shortValue();
            }
            case Integer -> {
                return this.intValue() < num2.intValue();
            }
            case Long -> {
                return this.longValue() < num2.longValue();
            }
            case Float -> {
                return this.floatValue() < num2.floatValue();
            }
            case Double -> {
                return this.doubleValue() < num2.doubleValue();
            }
        }
        return false;
    }

    // endregion

    // region static operations between numbers

    public static JNumber add(Number num1, Number num2){
        nullExceptionCheck(num1, "num1");

        return convertToJNumber(num1).add_JNumber(convertToJNumber(num2));
    }

    public static JNumber subtract(Number num1, Number num2){
        nullExceptionCheck(num1, "num1");

        return convertToJNumber(num1).subtract_JNumber(convertToJNumber(num2));
    }

    public static JNumber multiply(Number num1, Number num2){
        nullExceptionCheck(num1, "num1");

        return convertToJNumber(num1).multiply_JNumber(convertToJNumber(num2));
    }

    public static JNumber divide(Number num1, Number num2){
        nullExceptionCheck(num1, "num1");

        return convertToJNumber(num1).divide_JNumber(convertToJNumber(num2));
    }

    public static JNumber power(Number num1, Number num2){
        nullExceptionCheck(num1, "num1");

        return convertToJNumber(num1).power_JNumber(convertToJNumber(num2));
    }

    public static JNumber modulo(Number num1, Number num2){
        nullExceptionCheck(num1, "num1");

        return convertToJNumber(num1).modulo_JNumber(convertToJNumber(num2));
    }

    public static boolean equalTo(Number num1, Number num2){

        nullExceptionCheck(num1, "num1");

        return convertToJNumber(num1).equalTo_JNumber(convertToJNumber(num2));
    }

    public static boolean notEqualTo(Number num1, Number num2){

        return !equalTo(num1, num2);
    }

    public static boolean greaterThanOrEqualTo(Number num1, Number num2){
        nullExceptionCheck(num1, "num1");

        return convertToJNumber(num1).greaterThanOrEqualTo_JNumber(convertToJNumber(num2));
    }

    public static boolean lessThanOrEqualTo(Number num1, Number num2){
        nullExceptionCheck(num1, "num1");

        return convertToJNumber(num1).lessThanOrEqualTo_JNumber(convertToJNumber(num2));
    }

    public static boolean greaterThan(Number num1, Number num2){
        nullExceptionCheck(num1, "num1");

        return convertToJNumber(num1).greaterThan_JNumber(convertToJNumber(num2));
    }

    public static boolean lessThan(Number num1, Number num2){
        nullExceptionCheck(num1, "num1");

        return convertToJNumber(num1).lessThan_JNumber(convertToJNumber(num2));
    }

    // endregion

    // region to / from string

    public boolean tryParse(CharSequence inputString){
        try {
            byte val = Byte.parseByte(inputString.toString());
            setTo(val);
            return true;
        } catch (Exception e) {
            // do nothing
        }

        try {
            short val = Short.parseShort(inputString.toString());
            setTo(val);
            return true;
        } catch (Exception e) {
            // do nothing
        }

        try {
            int val = Integer.parseInt(inputString.toString());
            setTo(val);
            return true;
        } catch (Exception e) {
            // do nothing
        }

        try {
            long val = Long.parseLong(inputString.toString());
            setTo(val);
            return true;
        } catch (Exception e) {
            // do nothing
        }

        try {
            float val = Float.parseFloat(inputString.toString());
            setTo(val);
            return true;
        } catch (Exception e) {
            // do nothing
        }

        try {
            double val = Double.parseDouble(inputString.toString());
            setTo(val);
            return true;
        } catch (Exception e) {
            // do nothing
        }
        return false;
    }

    public static JNumber createFromString(CharSequence inputString){
        JNumber output = new JNumber();
        if(output.tryParse(inputString)){
            return output;
        }
        return null;
    }

    public JNumber fromString(CharSequence inputString) {
        if(tryParse(inputString)){
            // do nothing
        }
        return this;
    }

    @Override
    public String toString() {
        return String.valueOf(numberVal);
    }

    public String toBinaryString(){
        if(isIntegralNumber() || isWholeNumber()){
            return Long.toBinaryString(longValue());
        }
        else{
            return Long.toBinaryString(Math.round(doubleValue()));
        }
    }

    // endregion

    // region debug string

    public String toDebugString(){
        StringBuilder s = new StringBuilder();

        s.append("{");

        s.append(JString.getQuotedJString("type")).append(" : ").append(JString.getQuotedJString(type)).append(", ");

        s.append(JString.getQuotedJString("numberVal")).append(" : ").append(numberVal);

        s.append("}");

        return s.toString();
    }

    // endregion
}

