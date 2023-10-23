package jLibrary;

import jLibrary.exceptions.WrongInputTypeException;
import jLibrary.expressionManipulation.ExpressionObject;
import jLibrary.expressionManipulation.ExpressionVariableContainer;
import jLibrary.expressionManipulation.ExpressionVariableObject;
import jLibrary.interfaces.*;
import jLibrary.miscFunctions.MiscFunctions;
import jLibrary.nestedStringTree.NestedStringTree_Brackets;
import jLibrary.exceptions.UnrecognizedSymbolsException;
import jLibrary.exceptions.WrongExecutionTypeException;
import jLibrary.interfaces.functional.JObjectExecutionPredicate;
import jLibrary.typeEnumerable.ObjectTypes;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static jLibrary.miscFunctions.MiscFunctions.*;

/**
 * This class is the root of the jLib library.
 * <br>
 * It is meant to be a general purpose data storage class that allows for easy conversion of
 * complex and simple data to and from strings.
 * <br>
 * <br>
 * Giving a primitive data type(string, boolean, doubles, floats, etc) to the constructor or setTo() functions
 * will create a "primitive" JObject of the appropriate type (Number, Boolean or String).
 * <br>
 * The JObject will always attempt to parse and or evaluate any strings not enclosed within quotation marks.
 * <br>
 * <br>
 * Giving a string of data separated by commas will create a JObject list containing the given elements
 * ,each of which will also be converted to JObjects.
 * <br>
 * ex: "5, 6, bob, True" will create the list [5, 6, bob, true]
 * <br>
 * <br>
 * Giving an array of primitive values or strings will create a JObject list of those values. The JObject will attempt
 * to evaluate any strings it is given unless the strings are encapsulated in quotation marks.
 * <br>
 * <br>
 * Giving a string of data separated by commas and encapsulated within [] brackets will create a JObject list
 * containing the given elements ,each of which will also be converted to JObjects.
 * <br>
 * ex: "[5, 6, bob, True]" will create the list [5, 6, bob, true]
 * <br>
 * <br>
 * Giving a string of data separated by commas and encapsulated within () parentheses will create a JObject set
 * containing the given elements ,each of which will also be converted to JObjects.
 * <br>
 * Sets are immutable, they cannot have elements added to them or removed from them. Additionally,
 * elements within the set are also immutable. Attempting to retrieve them will only yield a copy of the element,
 * not the element itself.
 * <br>
 * ex: "(5, 6, bob, True)" will create the set (5, 6, bob, true)
 * <br>
 * <br>
 * Special note: if comma separated values are given as a string with no brackets,
 * the JObject will default to being a list.
 * <br>
 * <br>
 * Giving a string of data encapsulated by {} brackets attempts to create a dictionary JObject.
 * <br>
 * A dictionary is a group of key-value pairs.
 * <br>
 * Keys must be strings and there can be no duplicate keys.
 * <br>
 * All values are JObjects.
 * <br>
 * ex: "{name:bob, weight:5}" will create a dictionary containing a JObject representing the string "bob" assigned
 * to the key "name" and a JObject representing the number 5 assigned to the key "weight".
 * <br>
 * Notice that each key-value pair is separated by a comma.
 * <br>
 * <br>
 * Special Note: If you assign a string value to the key "className" in a dictionary, the getClassName()
 * function will return the given string. Otherwise, the class name will be "Dictionary".
 * @author Joseph Bethune
 */
public class JObject
        implements Iterable<JObject>, Comparable<JObject>, JObjectConvertible,
        NumberInterface, BooleanInterface, StringInterface, java.util.Map<String, JObject> {

    private static class JObjectHashMap {

        private HashMap<String, JObject> subObjects;

        public JObjectHashMap(){
            subObjects = new HashMap<>();
        }

        public JObjectHashMap clone(){
            JObjectHashMap output = new JObjectHashMap();

            for(Map.Entry<String, JObject> entry : subObjects.entrySet()){
                output.subObjects.put(entry.getKey(), entry.getValue().clone());
            }

            return output;
        }

        public void clear(){
            subObjects.clear();
        }

        public HashMap<String, JObject> getHashMap(){
            return subObjects;
        }

        public void setHashMap(HashMap<String, JObject> newMap){
            subObjects = newMap;
        }

        @Override
        public String toString(){
            if(subObjects.size() == 0){
                return "Null";
            }
            else{
                StringBuilder s = new StringBuilder("{");
                boolean first = true;
                for(Map.Entry<String, JObject> entry : subObjects.entrySet()){
                    if(first){
                        s.append(", ");
                    }
                    s.append(entry.getKey()).append(" : ").append(entry.getKey());
                    if(first){
                        first = false;
                    }
                }
                s.append("}");
                return s.toString();
            }
        }
    }

    public static final String ParameterName_ClassName = "ClassName";
    public static final String ParameterName_value = "Value";

    protected String typeName;
    protected Object value;

    //region constructors

    /**
     * Default constructor : creates an empty JObject with a class name of Null
     */
    public JObject() {
        clear();
    }

    /**
     * Constructor overload : creates JObject to store a boolean value
     */
    public JObject(boolean inputValue){
        setTo(inputValue);
    }

    /**
     * Constructor overload : creates JObject to store a boolean value
     */
    public JObject(char inputValue){
        setTo(inputValue);
    }

    /**
     * Constructor overload : creates JObject to store a byte value
     */
    public JObject(byte inputValue){
        setTo(inputValue);
    }

    /**
     * Constructor overload : creates JObject to store a short value
     */
    public JObject(short inputValue){
        setTo(inputValue);
    }

    /**
     * Constructor overload : creates JObject to store an integer value
     */
    public JObject(int inputValue){
        setTo(inputValue);
    }

    /**
     * Constructor overload : creates JObject to store a long value
     */
    public JObject(long inputValue){
        setTo(inputValue);
    }

    /**
     * Constructor overload : creates JObject to store a float value
     */
    public JObject(float inputValue){
        setTo(inputValue);
    }

    /**
     * Constructor overload : creates JObject to store a double value
     */
    public JObject(double inputValue){
        setTo(inputValue);
    }

    /**
     * Constructor overload : creates JObject from a variety of different types
     */
    public JObject(Object inputValue) throws UnrecognizedSymbolsException, IllegalArgumentException{

        if(inputValue == null) {
            clear();
            return;
        }

        try {
            setTo(inputValue);
        }
        catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Received an un-usable type during Instantiation : " + inputValue +
                    ", input type : " + inputValue.getClass().getName());
        }
    }

    // endregion

    // region clear functions

    /**
     * Resets the JObject to an empty object with the built-in type of Null.
     */
    public void clear() {
        typeName = ObjectTypes.Null.toString();
        value = null;
    }

    // endregion

    // region set to empty default type functions

    /**
     * Converts this JObject into a string composed of the given value.
     * @return Returns the modified JObject for method chaining.
     */
    public JObject setToStringJObject(CharSequence value){

        this.typeName = ObjectTypes.String.name();
        this.value = new JString(value);

        return this;
    }

    /**
     * Converts this JObject into a string.
     * @return Returns the modified JObject for method chaining.
     */
    public JObject setToStringJObject(){
        return setToStringJObject(null);
    }

    /**
     * Converts this JObject into an empty list.
     * @return Returns the modified JObject for method chaining.
     */
    public JObject setToEmptyList(){
        setTypeName(ObjectTypes.List.name());
        resetSubObjects();
        return this;
    }

    /**
     * Converts this JObject into an empty set.
     * @return Returns the modified JObject for method chaining.
     */
    public JObject setToEmptySet(){
        setTypeName(ObjectTypes.Set.name());
        resetSubObjects();
        return this;
    }

    /**
     * Converts this JObject into an empty dictionary.
     * @return Returns the modified JObject for method chaining.
     */
    public JObject setToEmptyDictionary(){
        setTypeName(ObjectTypes.Dictionary.name());
        resetSubObjects();
        return this;
    }

    /**
     * Converts this JObject into an empty dictionary, containing the given keys. There will be no
     * values stored at these keys.
     * @return Returns the modified JObject for method chaining.
     */
    public JObject setToEmptyDictionary(CharSequence... keys){
        setToEmptyDictionary();

        if(keys == null || keys.length < 1){
            return this;
        }

        for (CharSequence key : keys) {
            getSubObjects().put(key.toString(), null);
        }
        return this;
    }

    // endregion

    // region static create empty default type functions

    /**
     * Creates a new string JObject composed of the given value.
     * @return Returns the created JObject.
     */
    public static JObject createStringJObject(CharSequence value){
        JObject output = new JObject();
        output.setToStringJObject(value);

        return output;
    }

    /**
     * Creates a new empty string JObject.
     * @return Returns the created JObject.
     */
    public static JObject createStringJObject(){
        JObject output = new JObject();
        output.setToStringJObject(null);

        return output;
    }

    /**
     * Creates a new empty list JObject.
     * @return Returns the created JObject.
     */
    public static JObject createEmptyList(){
        JObject output = new JObject();
        output.setToEmptyList();
        return output;
    }

    /**
     * Creates a new empty set JObject.
     * @return Returns the created JObject.
     */
    public static JObject createEmptySet(){
        JObject output = new JObject();
        output.setToEmptySet();
        return output;
    }

    /**
     * Creates a new empty dictionary JObject.
     * @return Returns the created JObject.
     */
    public static JObject createEmptyDictionary(){
        JObject output = new JObject();
        output.setToEmptyDictionary();
        return output;
    }

    /**
     * Creates a new empty dictionary JObject containing the given keys. There will be no values stored at these keys.
     * @return Returns the created JObject.
     */
    public static JObject createEmptyDictionary(CharSequence... keys){
        JObject output = new JObject();
        output.setToEmptyDictionary(keys);
        return output;
    }

    /**
     * Creates an empty dictionary JObject containing the given keys.
     * The created object will have the designated class name.
     * @param className The class name to be assigned to the output object.
     * @param keys The parameter names in the output object.
     * @return Returns the created dictionary JObject.
     */
    public static JObject createEmptyClassJObject(String className, String... keys)
            throws WrongExecutionTypeException, UnrecognizedSymbolsException, IllegalArgumentException {
        JObject output = createEmptyDictionary(keys);

        output.typeName = className;

        return output;
    }

    // endregion

    // region static to JObject conversion

    /**
     * Creates a new JObject out of the given value if it is not already a JObject; otherwise: this method returns
     * the given value unchanged.
     */
    public static JObject convertToJObject(Object value){
        if(value instanceof JObject nv){
            return nv;
        }
        else{
            return new JObject(value);
        }
    }

    // endregion

    // region map methods

    @Override
    public boolean isEmpty() {
        if(value == null){
            return true;
        }

        if(isPrimitive()){
            return true;
        }

        if(value instanceof JObjectHashMap map){
            return map.getHashMap().isEmpty();
        }

        return true;
    }

    @Override
    public boolean containsKey(Object key) {
        if(isPrimitive()){
            return false;
        }

        if(key == null){
            return false;
        }

        if(key.getClass().isArray()){
            if(key instanceof Object[] keys){
                for(Object keyElement : keys){
                    if(containsKey(keyElement)){
                        return true;
                    }
                }
            }
        }
        else{
            HashMap<String, JObject> map = getSubObjects();
            if(map != null){
                return map.containsKey(key);
            }
        }

        return false;
    }

    @Override
    public JObject get(Object key) {
        if(isPrimitive()){
            return null;
        }

        return getValue(key);
    }

    @Override
    public JObject put(String key, JObject value) {
        if(isPrimitive()){
            return null;
        }

        JObject previousValue = getValue(key);
        setValue(key, value);
        return previousValue;
    }

    @Override
    public void putAll(Map<? extends String, ? extends JObject> m) {
        HashMap<String, JObject> map = getSubObjects();
        if(map != null){

            map.putAll(m);
        }
    }

    @Override
    public Set<String> keySet() {
        if(isPrimitive()){
            return Collections.emptySet();
        }

        HashMap<String, JObject> map = getSubObjects();
        if(map != null){
            return map.keySet();
        }
        return Collections.emptySet();
    }

    @Override
    public Set<Entry<String, JObject>> entrySet() {
        if(isPrimitive()){
            return Collections.emptySet();
        }

        HashMap<String, JObject> map = getSubObjects();
        if(map != null){

            return map.entrySet();
        }
        return Collections.emptySet();
    }

    @Override
    public ArrayList<JObject> values() {
        if(isPrimitive()){
            return new ArrayList<>();
        }
        else{
            HashMap<String, JObject> map = getSubObjects();
            if(map != null){

                return new ArrayList<>(map.values());
            }
            throw new RuntimeException("Values are missing");
        }
    }

    // endregion

    // region clone

    private Object getClonedValue(){
        if(this.isBoolean()){
            return ((Boolean) value);
        }
        else if(this.isNumber()){
            return jNumberValue().clone().numberValue();
        }
        else if(this.isString() || this.isSymbol()){
            return ((JString) value).clone();
        }
        else {
            return getSubObjects().clone();
        }
    }

    /**
     * Creates a deep copy clone of the JObject. All sub objects (including sub JObjects) are cloned recursively.
     * <br>
     * The resulting JObject will be an exact duplicate to the original, but will contain no references back
     * to the original.
     * <br>
     * Chainable Function.
     * @return JObject a clone of the JObject that called this function.
     */
    public JObject clone(){

        JObject output = new JObject();

        output.typeName = this.typeName;

        if(this.isPrimitive() || this.isSymbol()){
            output.value = this.getClonedValue();
        }
        else{
            output.value = this.getSubObjectsContainer().clone();
        }

        return output;
    }

    /**
     * Clones each of this JObject's sub-objects and returns the clones inside an array.
     * If the sub-object value is null, this method will return an empty array.
     * @return JObject array containing clones of all of this JObject's sub-objects, or an empty array.
     */
    public JObject[] getClonedChildren(){
        if(getSubObjectsContainer() != null) {
            return getSubObjectsContainer().clone().getHashMap().values().toArray(new JObject[0]);
        }
        else{
            return new JObject[0];
        }
    }

    // endregion

    // region base value

    @Override
    public Object getBaseValue() {
        return value;
    }

    // endregion
    
    // region subObjects functions
    
    private HashMap<String, JObject> getSubObjects() {
        JObjectHashMap temp = getSubObjectsContainer();
        if(temp != null){
            return temp.getHashMap();
        }
        return null;
    }

    private JObjectHashMap getSubObjectsContainer() {
        if(value == null){
            if(!isPrimitive()){
                resetSubObjects();
            }
        }

        if (value != null && value instanceof JObjectHashMap var) {
            return var;
        }
        return null;
    }
    
    private void resetSubObjects() {
        value = new JObjectHashMap();
    }
    
    // endregion

    // region setTo functions

    // region primitive setTo()

    /**
     * Converts the calling JObject into a boolean JObject representing the given value.
     * <br>
     * Chainable Function.
     * @param inputValue The value the JObject is to represent.
     * @return Returns the modified JObject.
     */
    public JObject setTo(boolean inputValue){

        clear();
        typeName = ObjectTypes.Boolean.toString();
        value = inputValue;

        return this;
    }

    /**
     * Converts the calling JObject into a boolean JObject representing the given value.
     * <br>
     * Chainable Function.
     * @param inputValue The value the JObject is to represent.
     * @return Returns the modified JObject.
     */
    public JObject setTo(char inputValue){

        clear();
        typeName = ObjectTypes.String.toString();
        value = new JString(inputValue);

        return this;
    }

    /**
     * Converts the calling JObject into a number JObject representing the given value.
     * <br>
     * Chainable Function.
     * @param inputValue The value the JObject is to represent.
     * @return Returns the modified JObject.
     */
    public JObject setTo(byte inputValue){

        clear();
        typeName = ObjectTypes.Byte.toString();
        value = inputValue;

        return this;
    }

    /**
     * Converts the calling JObject into a number JObject representing the given value.
     * <br>
     * Chainable Function.
     * @param inputValue The value the JObject is to represent.
     * @return Returns the modified JObject.
     */
    public JObject setTo(short inputValue){

        clear();
        typeName = ObjectTypes.Short.toString();
        value = inputValue;

        return this;
    }

    /**
     * Converts the calling JObject into a number JObject representing the given value.
     * <br>
     * Chainable Function.
     * @param inputValue The value the JObject is to represent.
     * @return Returns the modified JObject.
     */
    public JObject setTo(int inputValue){

        clear();
        typeName = ObjectTypes.Integer.name();
        value = inputValue;

        return this;
    }

    /**
     * Converts the calling JObject into a number JObject representing the given value.
     * <br>
     * Chainable Function.
     * @param inputValue The value the JObject is to represent.
     * @return Returns the modified JObject.
     */
    public JObject setTo(long inputValue){

        clear();
        typeName = ObjectTypes.Long.toString();
        value = inputValue;

        return this;
    }

    /**
     * Converts the calling JObject into a number JObject representing the given value.
     * <br>
     * Chainable Function.
     * @param inputValue The value the JObject is to represent.
     * @return Returns the modified JObject.
     */
    public JObject setTo(float inputValue){

        clear();
        typeName = ObjectTypes.Float.toString();
        value = inputValue;

        return this;
    }

    /**
     * Converts the calling JObject into a number JObject representing the given value.
     * <br>
     * Chainable Function.
     * @param inputValue The value the JObject is to represent.
     * @return Returns the modified JObject.
     */
    public JObject setTo(double inputValue){

        clear();
        typeName = ObjectTypes.Double.toString();
        value = inputValue;

        return this;
    }

    // endregion

    /**
     * Converts the calling JObject into a JObject representing the given value.
     * <br>
     * Chainable Function.
     * @param inputValue The value the JObject is to represent.
     * @return Returns the modified JObject.
     */
    public JObject setTo(Object inputValue) throws UnrecognizedSymbolsException, IllegalArgumentException{

        if (inputValue == null) {

            clear();
            return this;
        }
        if(inputValue == this){
            return this;
        }

        boolean alreadyShrunk = false;

        if(inputValue instanceof JString iv){

            fromString(iv);
        }
        else if(inputValue instanceof JPrimitive iv){
            this.typeName = iv.getTypeName();
            this.value = iv.getClonedValue();
            return this;
        }
        else if (inputValue instanceof CharSequence iv) {

            fromString(JString.toJString(iv));
        }
        else if (inputValue instanceof Character iv) {

            fromString(JString.toJString(iv));
        }
        else if(inputValue instanceof JNumber iv) {

            clear();
            typeName = iv.getType().name();
            value = iv.getClonedNumber();
        }
        else if(inputValue instanceof Number iv){
            setTo(new JNumber(iv));
            alreadyShrunk = true;
        }
        else if(inputValue instanceof Boolean iv){
            setTo(iv.booleanValue());
        }
        else if(inputValue instanceof JObject srcObj) {

            clear();
            if (srcObj.isNull()){

                return this;
            }

            this.typeName = srcObj.typeName;
            if (srcObj.isPrimitive() || srcObj.isSymbol()) {
                this.value = srcObj.getClonedValue();
            }
            else if (srcObj.getSubObjects() != null){
                this.resetSubObjects();
                for(Map.Entry<String, JObject> entry : srcObj.getSubObjects().entrySet()){
                    JObject valueBase = entry.getValue();
                    if(valueBase != null) {
                        JObject subObjClone = valueBase.clone();
                        this.setValue(entry.getKey(), subObjClone);
                    }
                }
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
        else if(inputValue instanceof ConvertibleToJObject var){

            clear();
            setTo(var.toJObject());
        }
        else if(inputValue instanceof JSONObject var){
            return setTo(var.toMap());
        }
        else if(inputValue instanceof boolean[] arr){

            clear();
            typeName = ObjectTypes.List.name();
            for(int x = 0; x < arr.length; x += 1){

                JObject jObj = new JObject(arr[x]);
                this.setValue(x, jObj);
            }
        }
        else if(inputValue instanceof byte[] arr){

            clear();
            typeName = ObjectTypes.List.name();
            for(int x = 0; x < arr.length; x += 1){

                JObject jObj = new JObject(arr[x]);
                this.setValue(x, jObj);
            }
        }
        else if(inputValue instanceof short[] arr){

            clear();
            typeName = ObjectTypes.List.name();
            for(int x = 0; x < arr.length; x += 1){

                JObject jObj = new JObject(arr[x]);
                this.setValue(x, jObj);
            }
        }
        else if(inputValue instanceof int[] arr){

            clear();
            typeName = ObjectTypes.List.name();
            for(int x = 0; x < arr.length; x += 1){

                JObject jObj = new JObject(arr[x]);
                this.setValue(x, jObj);
            }
        }
        else if(inputValue instanceof long[] arr){

            clear();
            typeName = ObjectTypes.List.name();
            for(int x = 0; x < arr.length; x += 1){

                JObject jObj = new JObject(arr[x]);
                this.setValue(x, jObj);
            }
        }
        else if(inputValue instanceof float[] arr){

            clear();
            typeName = ObjectTypes.List.name();
            for(int x = 0; x < arr.length; x += 1){

                JObject jObj = new JObject(arr[x]);
                this.setValue(x, jObj);
            }
        }
        else if(inputValue instanceof double[] arr){

            clear();
            typeName = ObjectTypes.List.name();
            for(int x = 0; x < arr.length; x += 1){

                JObject jObj = new JObject(arr[x]);
                this.setValue(x, jObj);
            }
        }
        else if(inputValue instanceof Object[] arr){

            clear();
            typeName = ObjectTypes.List.name();
            for(int x = 0; x < arr.length; x += 1){

                JObject jObj = convertToJObject(arr[x]);
                this.setValue(x, jObj);
            }
        }
        else if(inputValue instanceof Collection<?> collection){
            clear();
            typeName = ObjectTypes.List.name();
            Object[] elements = collection.toArray();
            for(int x = 0; x < elements.length; x += 1){
                JObject jObj = null;

                try{
                    jObj = convertToJObject(elements[x]);
                }
                catch (Exception e){
                    print(e);
                    throw e;
                }

                try {
                    this.setValue(x, jObj);
                }
                catch (Exception e){
                    print(e);
                    throw e;
                }
            }
        }
        else if(inputValue instanceof Iterable<?> collection){
            clear();
            typeName = ObjectTypes.List.name();
            int counter = 0;
            for(Object ele : collection){
                JObject jObj = null;

                try{
                    jObj = convertToJObject(ele);
                }
                catch (Exception e){
                    print(e);
                    throw e;
                }

                try {
                    this.setValue(counter, jObj);
                    counter += 1;
                }
                catch (Exception e){
                    print(e);
                    throw e;
                }
            }
        }
        else if(inputValue instanceof Map<?,?> map){
            clear();
            typeName = ObjectTypes.Dictionary.name();
            for(Map.Entry<?,?> entry: map.entrySet()){

                JObject key = convertToJObject(entry.getKey());

                if(!key.isString() && !key.isIntegralNumber()){
                    throw new IllegalArgumentException("The key must be a string or a whole number.");
                }

                JObject value = null;

                try{
                    value = convertToJObject(entry.getValue());
                }
                catch (UnrecognizedSymbolsException e){
                    if(entry.getValue().getClass().getSimpleName().equalsIgnoreCase("String")){
                        value = JObject.createStringJObject(entry.getValue().toString());
                    }
                    else{
                        print(e);
                        throw e;
                    }
                }
                catch (Exception e){
                    print(e);
                    throw e;
                }

                setValue(key.toString(), value);
            }
        }
        else {

            throw new IllegalArgumentException("Received an un-usable data type during setTo()");
        }

        if(isNumber()){
            if(!alreadyShrunk) {
                shrinkToSmallestDataType();
            }
        }

        return this;
    }

    // endregion

    // region equals

    /**
     * Compares the given inputValue with this JObject.
     * @param inputValue The value to be compared to this JObject.
     * @return Returns True if the inputValue is equal to this JObject, else it returns False.
     */
    @Override
    public boolean equals(Object inputValue) {

        if(inputValue == this){
            return true;
        }

        if(inputValue == null){
            return isNull();
        }

        if (inputValue instanceof String) {
            return inputValue.equals(toString(false));
        }
        else if(inputValue instanceof JNumber io) {

            try{
                if(isNumber()){
                    return jNumberValue().equals(io);
                }
            }
            catch (WrongExecutionTypeException e){
                return false;
            }

            return false;
        }
        else if(inputValue instanceof JObject io)
        {
            if(this.isNull() && io.isNull()){
                return true;
            }

            if(this.isNull() != io.isNull()){
                return false;
            }

            if(!this.getTypeName().equals(io.getTypeName())){
                return false;
            }

            if(this.isPrimitive()){
                return this.value.equals(io.value);
            }

            if(this.size() != io.size()){
                return false;
            }

            if(this.size() > 0){
                String[] theseKeys = this.getKeys();
                String[] thoseKeys = io.getKeys();
                if(!MiscFunctions.stringArrayEquals(theseKeys, thoseKeys)){
                    return false;
                }

                for (String theseKey : theseKeys) {
                    JObject thisSubObject = this.getValue(theseKey);
                    JObject thatSubObject = io.getValue(theseKey);

                    boolean thisIsNull = thisSubObject == null || thisSubObject.isNull();
                    boolean thatIsNull = thatSubObject == null || thatSubObject.isNull();

                    if (thisIsNull && thatIsNull) {
                        continue;
                    }

                    if (thisIsNull != thatIsNull) {
                        return false;
                    }

                    if (!thisSubObject.equals(thatSubObject)) {
                        return false;
                    }
                }
            }

            return true;
        }
        else {
            return false;
        }
    }

    // endregion

    // region add operations

    /**
     * "Adds" obj2 to this JObject. This method takes performs different operations depending on the type of the
     * instance that it is being executed on and the type of obj2. This will either perform a string concatenation or
     * a numeric addition. If both operands are numbers, then it will perform a numeric addition. If any of the operands
     * are a string, then it will perform a string concatenation.
     * @param obj2 The value to be "added" to this JObject.
     * @return Returns the result of the "addition" as a new JObject after it has been modified.
     */
    public JObject add(Object obj2) {
        boolean thisIsNumber = isNumber();
        boolean thisIsString = isString();

        if(obj2 == null){
            if(isString()){
                return this.add("null");
            }
            return this;
        }

        if(thisIsString){
            return JObject.createStringJObject(this.toJString().add(obj2));
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
            return JObject.createStringJObject(this.toJString().add(otherAsString));
        }
        else if(bothAreNumbers){
            JNumber temp = JNumber.add(jNumberValue(), otherAsNumber);
            temp.shrinkToSmallestDataType();
            return new JObject(temp.numberValue());
        }
        else{
            throw new RuntimeException("In order to perform that add() method, both this value and the other value " +
                    "have to be numbers or one has to be string.");
        }
    }

    // endregion

    // region hash code

    /**
     * Generates a hash code for the JObject
      * @return int : returns a hash code for the JObject
     */
    @Override
    public int hashCode() {

        if(isPrimitive()){
            return Objects.hash(typeName, value.hashCode());
        }
        else{
            return Objects.hash(typeName, getSubObjects());
        }
    }

    // endregion

    // region length and size

    /**
     * Gives the number of objects contained within this JObject.
     * @return Returns the number of sub-objects inside this JObject.
     */
    public int size() {
        if(isPrimitive()) {
            throw new RuntimeException("Cannot retrieve size from primitive JObjects.");
        }
        else{
            if(this.getSubObjects() == null) {
                return 0;
            }
            else{
                return this.getSubObjects().size();
            }
        }
    }

    /**
     * Gives the character length or element count of this JObject.
     * @return If this JObject is a string, then this function will return the length of that String.
     * <br>
     * If this JObject is not a primitive, then it returns the number of sub objects contained within this JObject.
     */
    public int length() {
        if (isPrimitive()) {
            if (isString()){
                return ((JString) value).length();
            }

            throw new RuntimeException("Can only get the length from a primitive JObject if it is a string.");
        }
        else {
            return size();
        }
    }

    // endregion

    // region to array functions

    /**
     * Creates a boolean array from lists, sets or primitives. If this JObject is a primitive of the correct type,
     * then a single element array containing this JObject as a string is returned.
     * @return boolean array
     * @throws WrongExecutionTypeException Throws a wrong type exception if this JObject isn't a boolean or
     * a list/set of booleans.
     */
    public boolean[] toBooleanArray() throws WrongExecutionTypeException {
        if(!getType().isList() && !getType().isSet() && !isPrimitive()){
            throw new WrongExecutionTypeException(getTypeName(),
                    new String[]{ObjectTypes.List.name(), ObjectTypes.Set.name(), ObjectTypes.Boolean.name()});
        }

        if(isPrimitive()){
            if(isBoolean()){
                return new boolean[]{this.booleanValue()};
            }
            else{
                throw new WrongExecutionTypeException(getTypeName(),
                        new String[]{ObjectTypes.List.name(), ObjectTypes.Set.name(), ObjectTypes.Boolean.name()});
            }
        }
        else {
            int counter = 0;
            boolean[] output = new boolean[size()];
            for(JObject subObj : this){
                output[counter] = subObj.booleanValue();
                counter += 1;
            }
            return output;
        }
    }

    /**
     * Creates an integer array from lists, sets or primitives. If this JObject is a primitive of the correct type,
     * then a single element array containing this JObject as an integer is returned.
     * @return boolean array
     * @throws WrongExecutionTypeException Throws a wrong type exception if this JObject isn't a Number or
     * a list/set of Numbers.
     */
    public int[] toIntegerArray() throws WrongExecutionTypeException {
        if(!getType().isList() && !getType().isSet() && !isPrimitive()){
            throw new WrongExecutionTypeException(getTypeName(),
                    new String[]{ObjectTypes.List.name(), ObjectTypes.Set.name(), ObjectTypes.Number.name()});
        }

        if(isPrimitive()){
            if(isInteger()){
                return new int[]{this.intValue()};
            }
            else{
                throw new WrongExecutionTypeException(getTypeName(),
                        new String[]{ObjectTypes.List.name(), ObjectTypes.Set.name(), ObjectTypes.Number.name()});
            }
        }
        else {
            int counter = 0;
            int[] output = new int[size()];
            for(JObject subObj : this){
                output[counter] = subObj.intValue();
                counter += 1;
            }
            return output;
        }
    }

    /**
     * Creates a float array from lists, sets or primitives. If this JObject is a primitive of the correct type,
     * then a single element array containing this JObject as a float is returned.
     * @return boolean array
     * @throws WrongExecutionTypeException Throws a wrong type exception if this JObject isn't a Number or
     * a list/set of Numbers.
     */
    public float[] toFloatArray() throws WrongExecutionTypeException {
        if(!getType().isList() && !getType().isSet() && !isPrimitive()){
            throw new WrongExecutionTypeException(getTypeName(),
                    new String[]{ObjectTypes.List.name(), ObjectTypes.Set.name(), ObjectTypes.Number.name()});
        }

        if(isPrimitive()){
            if(isNumber()){
                return new float[]{this.floatValue()};
            }
            else{
                throw new WrongExecutionTypeException(getTypeName(),
                        new String[]{ObjectTypes.List.name(), ObjectTypes.Set.name(), ObjectTypes.Number.name()});
            }
        }
        else {
            int counter = 0;
            float[] output = new float[size()];
            for(JObject subObj : this){
                output[counter] = subObj.floatValue();
                counter += 1;
            }
            return output;
        }
    }

    /**
     * Creates a double array from lists, sets or primitives. If this JObject is a primitive of the correct type,
     * then a single element array containing this JObject as a double is returned.
     * @return boolean array
     * @throws WrongExecutionTypeException Throws a wrong type exception if this JObject isn't a Number or
     * a list/set of Numbers.
     */
    public double[] toDoubleArray() throws WrongExecutionTypeException {
        if(!getType().isList() && !getType().isSet() && !isPrimitive()){
            throw new WrongExecutionTypeException(getTypeName(),
                    new String[]{ObjectTypes.List.name(), ObjectTypes.Set.name(), ObjectTypes.Number.name()});
        }

        if(isPrimitive()){
            if(isNumber()){
                return new double[]{this.doubleValue()};
            }
            else{
                throw new WrongExecutionTypeException(getTypeName(),
                        new String[]{ObjectTypes.List.name(), ObjectTypes.Set.name(), ObjectTypes.Number.name()});
            }
        }
        else {
            int counter = 0;
            double[] output = new double[size()];
            for(JObject subObj : this){
                output[counter] = subObj.doubleValue();
                counter += 1;
            }
            return output;
        }
    }

    /**
     * Creates a string array from lists, sets or primitives. If this JObject is a primitive of the correct type,
     * then a single element array containing this JObject as a string is returned.
     * @param includeQuotesOnArrayElements boolean value to determine if each element in the array will be encapsulated
     * by quotation marks.
     * @return String array
     */
    public String[] toStringArray(boolean includeQuotesOnArrayElements){
        if(!getType().isList() && !getType().isSet() && !isPrimitive()){
            throw new WrongExecutionTypeException(getTypeName(),
                    new ObjectTypes[]{ObjectTypes.List, ObjectTypes.Set,
                            ObjectTypes.Number, ObjectTypes.Boolean, ObjectTypes.String});
        }

        if(isPrimitive()){
            return new String[]{this.toString(includeQuotesOnArrayElements)};
        }
        else {
            ArrayList<String> output = new ArrayList<>();
            for (JObject subObj : this) {
                output.add(subObj.toString(includeQuotesOnArrayElements));
            }

            return output.toArray(new String[0]);
        }
    }

    /**
     * Creates a string array from lists, sets or primitives. Each element will NOT have quotation marks.
     * If this JObject is a primitive of the correct type, then a single element array containing this
     * JObject as a string is returned.
     * @return String array
     */
    public String[] toStringArray(){
        return toStringArray(false);
    }

    /**
     * Creates a JObject array from containing all of this object's sub objects. If this JObject is a primitive,
     * then a single element array containing this JObject is returned instead.
     * @return JObject array
     */
    public JObject[] toJObjectArray(){
        if(isPrimitive()){
            return new JObject[]{this};
        }
        else{
            return getValues();
        }
    }

    // endregion

    // region dictionary helper functions

    /**
     * Returns the sub objects stored inside this JObject as an array of JObjects. If this JObject
     * has no sub objects, then an empty array is returned.
     * @return JObject array
     */
    public JObject[] getValues(){
        if(!isPrimitive()) {
            if(getType().isSet()){
                return getClonedChildren();
            }
            else {
                return getSubObjects().values().toArray(new JObject[0]);
            }
        }
        return new JObject[0];
    }

    // endregion

    // region JConvertible interface implementations

    @Override
    public JObject toJObject() {
        return this;
    }

    @Override
    public boolean fromJObject(JObject source) {
        setTo(source);
        return true;
    }

    /**
     * Pulls data from source JObject and places it within the outputRef object.
     * @param source This function will pull data from the source to populate the output.
     * @param outputRef A non-null instance of the output type will be needed to receive the data.
     * @return This function returns a modified instance of the outputRef, after it has been populated with data.
     * @param <T> This function can handle any instance that implements the JObjectConvertible interface.
     */
    public static <T extends JObjectConvertible>T createObjectFromJObject(JObject source, T outputRef){
        if(outputRef != null) {
            boolean valid = false;
            try {
                valid = outputRef.fromJObject(source);
            } catch (Exception e) {
                return null;
            }
            if (valid) {
                return outputRef;
            }
        }
        return null;
    }

    // endregion

    // region parameter names

    /**
     * Gives the key names as a String Array.
     * @return String Array of the sub object key names.
     */
    public String[] getKeys(){
        if(size() > 0) {
            return getSubObjects().keySet().toArray(new String[0]);
        }
        return new String[0];
    }

    // endregion

    // region retrieving the values of sub objects

    /**
     * Returns the JObject that is referenced as a sub-object under the given parameter name.
     * Set typed JObjects will yield a deep copy of the sub-object rather than the object itself.
     * @param parameterName Object : the parameter name of the sub-object. Strings are preferred.
     */
    public JObject getValue(Object parameterName){
        if(isPrimitive()) {
            return null;
        }

        String key = null;
        if(parameterName instanceof CharSequence var){
            key = var.toString();
        }
        else{
            try{
                key = parameterName.toString();
            }
            catch (Exception e){
                throw new RuntimeException("Couldn't extract string from parameterName.");
            }
        }

        if(getType().isSet()) {
            try {
                return getSubObjects().getOrDefault(key, null).clone();
            }
            catch (NullPointerException var){
                return null;
            }
        }
        else {
            return getSubObjects().getOrDefault(key, null);
        }
    }

    /**
     * Returns the JObject that is referenced as a sub-object under the given index.
     * Set typed JObjects will yield a deep copy of the sub-object rather than the object itself.
     * @param index int : the parameter name of the sub-object as an integer
     */
    public JObject getValue(int index){
        return getValue(Integer.toString(index));
    }

    /**
     * Returns the JObject that is referenced as a sub-object under the given index.
     * Set typed JObjects will yield a deep copy of the sub-object rather than the object itself.
     * @param index long : the parameter name of the sub-object as an integer
     */
    public JObject getValue(long index){
        return getValue(Long.toString(index));
    }

    // endregion

    // region setting the values of sub objects

    private JObject setValue_bypassSetLock(String parameterName, JObject newValue)
            throws UnrecognizedSymbolsException, IllegalArgumentException, WrongExecutionTypeException
    {
        if(getSubObjectsContainer() == null){
            resetSubObjects();
        }

        // assigns the value to the class name if the parameter name matches
        if(parameterName.equals(ParameterName_ClassName)){
            if(newValue.isString()){
                setTypeName(newValue.toString());
                return this;
            }
            else{
                throw new RuntimeException("The new class name must be a string.");
            }
        }

        if(newValue != null && !newValue.isNull()) {
            getSubObjects().put(parameterName, newValue);
        }
        else {
            getSubObjects().put(parameterName, null);
        }
        return this;
    }

    private JObject setValue_prv(Object parameterName, JObject newValue)
            throws UnrecognizedSymbolsException, IllegalArgumentException, WrongExecutionTypeException
    {

        String key = null;
        if(parameterName instanceof CharSequence var){
            key = var.toString();
        }
        else{
            try{
                key = parameterName.toString();
            }
            catch (Exception e){
                throw new RuntimeException("Couldn't extract string from parameterName.");
            }
        }

        if(getType().isSet()){
            throw new RuntimeException("Sets cannot be modified.");
        }

        if(isPrimitive()){
            throw new WrongExecutionTypeException(getTypeName(), "Any non primitive excluding sets");
        }

        return setValue_bypassSetLock(key, newValue);
    }

    /**
     * Places the newValue within this JObject as a sub object under the given parameter name.
     * <br>
     * Chainable method.
     * @return Returns this JObject after it has been modified.
     */
    public JObject setValue(Object parameterName, Object newValue)
            throws UnrecognizedSymbolsException, IllegalArgumentException, WrongExecutionTypeException {
        return setValue_prv(parameterName, convertToJObject(newValue));
    }

    /**
     * Places the newValue within this JObject as a sub object under at the given index.
     * <br>
     * Chainable method.
     * @return Returns this JObject after it has been modified.
     */
    public JObject setValue(int index, Object newValue)
            throws UnrecognizedSymbolsException, IllegalArgumentException, WrongExecutionTypeException
    {
        if(getType().isList()) {
            return setValue_prv(Integer.toString(index) , convertToJObject(newValue));
        }
        else{
            throw new WrongExecutionTypeException(getTypeName(), ObjectTypes.List.name());
        }
    }

    /**
     * Places the newValue within this JObject as a sub object under at the given index.
     * <br>
     * Chainable method.
     * @return Returns this JObject after it has been modified.
     */
    public JObject setValue(long index, Object newValue)
            throws UnrecognizedSymbolsException, IllegalArgumentException, WrongExecutionTypeException
    {
        if(getType().isList()) {
            return setValue_prv(Long.toString(index) , convertToJObject(newValue));
        }
        else{
            throw new WrongExecutionTypeException(getTypeName(), ObjectTypes.List.name());
        }
    }

    // endregion

    // region class name

    public JObject setTypeName(String newTypeName){
        typeName = newTypeName;

        return this;
    }

    @Override
    public String getTypeName() {
        return typeName;
    }

    @Override
    public ObjectTypes getType() {
        return ObjectTypes.fromString(typeName);
    }
    
    // endregion

    // region adding objects to collection

    // region adding an object at the beginning of the collection

    /**
     * Adds the newObject to the beginning of a collection.
     */
    public JObject prepend(Object newObject) throws WrongExecutionTypeException, UnrecognizedSymbolsException{
        if(!getType().isList()){
            throw new WrongExecutionTypeException(typeName, ObjectTypes.List.name());
        }

        JObject tempObject;

        if(newObject == null) {
            tempObject = new JObject();
        }
        else {
            tempObject = new JObject(newObject);
        }

        int newSize = size() + 1;
        JObject[] values = new JObject[newSize];
        values[0] = tempObject;
        for(int x = 1; x < newSize; x += 1){
            values[x] = getValue(x-1);
        }

        for(int x = 0; x < newSize; x += 1){
            setValue(x, values[x]);
        }

        return this;
    }

    /**
     * Adds the newObject to the beginning of a collection.
     */
    public JObject prepend(char newObject) throws WrongExecutionTypeException, UnrecognizedSymbolsException{
        return prepend(new JObject(newObject));
    }

    /**
     * Adds the newObject to the beginning of a collection.
     */
    public JObject prepend(boolean newObject) throws WrongExecutionTypeException, UnrecognizedSymbolsException{
        return prepend(new JObject(newObject));
    }

    /**
     * Adds the newObject to the beginning of a collection.
     */
    public JObject prepend(byte newObject) throws WrongExecutionTypeException, UnrecognizedSymbolsException{
        return prepend(new JObject(newObject));
    }

    /**
     * Adds the newObject to the beginning of a collection.
     */
    public JObject prepend(short newObject) throws WrongExecutionTypeException, UnrecognizedSymbolsException{
        return prepend(new JObject(newObject));
    }

    /**
     * Adds the newObject to the beginning of a collection.
     */
    public JObject prepend(int newObject) throws WrongExecutionTypeException, UnrecognizedSymbolsException{
        return prepend(new JObject(newObject));
    }

    /**
     * Adds the newObject to the beginning of a collection.
     */
    public JObject prepend(long newObject) throws WrongExecutionTypeException, UnrecognizedSymbolsException{
        return prepend(new JObject(newObject));
    }

    /**
     * Adds the newObject to the beginning of a collection.
     */
    public JObject prepend(float newObject) throws WrongExecutionTypeException, UnrecognizedSymbolsException{
        return prepend(new JObject(newObject));
    }

    /**
     * Adds the newObject to the beginning of a collection.
     */
    public JObject prepend(double newObject) throws WrongExecutionTypeException, UnrecognizedSymbolsException{
        return prepend(new JObject(newObject));
    }

    // endregion

    // region adding an object at end the of collection

    /**
     * Adds the newObject to the end of a collection.
     */
    public JObject append(Object inputObject) throws WrongExecutionTypeException, UnrecognizedSymbolsException{

        if(!getType().isList()){
            throw new WrongExecutionTypeException(typeName, ObjectTypes.List.name());
        }

        JObject tempObject;

        if(inputObject == null) {
            tempObject = new JObject();
        }
        else {
            tempObject = new JObject(inputObject);
        }

        int newIndex = size();
        getSubObjects().put(Integer.toString(newIndex), tempObject);

        return this;
    }

    /**
     * Adds the newObject to the end of a collection.
     */
    public JObject append(boolean newObject) throws WrongExecutionTypeException, UnrecognizedSymbolsException{
        return append(new JObject(newObject));
    }

    /**
     * Adds the newObject to the end of a collection.
     */
    public JObject append(char newObject) throws WrongExecutionTypeException, UnrecognizedSymbolsException{
        return append(new JObject(newObject));
    }

    /**
     * Adds the newObject to the end of a collection.
     */
    public JObject append(byte newObject) throws WrongExecutionTypeException, UnrecognizedSymbolsException{
        return append(new JObject(newObject));
    }

    /**
     * Adds the newObject to the end of a collection.
     */
    public JObject append(short newObject) throws WrongExecutionTypeException, UnrecognizedSymbolsException{
        return append(new JObject(newObject));
    }

    /**
     * Adds the newObject to the end of a collection.
     */
    public JObject append(int newObject) throws WrongExecutionTypeException, UnrecognizedSymbolsException{
        return append(new JObject(newObject));
    }

    /**
     * Adds the newObject to the end of a collection.
     */
    public JObject append(long newObject) throws WrongExecutionTypeException, UnrecognizedSymbolsException{
        return append(new JObject(newObject));
    }

    /**
     * Adds the newObject to the end of a collection.
     */
    public JObject append(float newObject) throws WrongExecutionTypeException, UnrecognizedSymbolsException{
        return append(new JObject(newObject));
    }

    /**
     * Adds the newObject to the end of a collection.
     */
    public JObject append(double newObject) throws WrongExecutionTypeException, UnrecognizedSymbolsException{
        return append(new JObject(newObject));
    }

    // endregion

    // region add an object in the collection at a specified point

    public JObject insert(Object location, Object newObject) throws WrongExecutionTypeException, UnrecognizedSymbolsException{
        if(!getType().isList()){
            throw new WrongExecutionTypeException(typeName, ObjectTypes.List.name());
        }

        // processes location input to get index
        int locationIntVal;
        if (location == null)
        {
            throw new NullPointerException("The input given for location is null.");
        }

        if(location instanceof JObject locationObject){
            if (locationObject.isWholeNumber())
            {
                locationIntVal = locationObject.intValue();
            }
            else
            {
                throw new RuntimeException(
                        "Can only accept integers for the location, this is a "+locationObject.getTypeName()+".");
            }
        }
        else if(location instanceof String locationObject){
            JObject newLoc = new JObject(locationObject);
            return this.insert(newLoc, newObject);
        }
        else{
            throw new RuntimeException("The value given for the insert location must be an convertible to an integer");
        }

        // processes newObject

        JObject objectToAssign;
        if (newObject == null)
        {
            objectToAssign = null;
        }
        else if(newObject instanceof JObject var) {
            objectToAssign = var;
        }
        else {
            objectToAssign = new JObject(newObject);
        }

        // actual appending
        int newSize = size() + 1;

        for(int x = locationIntVal; x < newSize; x += 1)
        {
            String paramName = Integer.toString(x);
            JObject pulledObject = getValue(x);
            getSubObjects().put(paramName, objectToAssign);

            objectToAssign = pulledObject;
        }

        return this;
    }

    public JObject insert(int location, Object newObject) throws WrongExecutionTypeException, UnrecognizedSymbolsException{
        return insert(new JObject(location), convertToJObject(newObject));
    }

    public JObject insert(int location, double newObject) throws WrongExecutionTypeException, UnrecognizedSymbolsException{
        return insert(new JObject(location), new JObject(newObject));
    }

    public JObject insert(int location, float newObject) throws WrongExecutionTypeException, UnrecognizedSymbolsException{
        return insert(new JObject(location), new JObject(newObject));
    }

    public JObject insert(int location, long newObject) throws WrongExecutionTypeException, UnrecognizedSymbolsException{
        return insert(new JObject(location), new JObject(newObject));
    }

    public JObject insert(int location, int newObject) throws WrongExecutionTypeException, UnrecognizedSymbolsException{
        return insert(new JObject(location), new JObject(newObject));
    }

    public JObject insert(int location, short newObject) throws WrongExecutionTypeException, UnrecognizedSymbolsException{
        return insert(new JObject(location), new JObject(newObject));
    }

    public JObject insert(int location, byte newObject) throws WrongExecutionTypeException, UnrecognizedSymbolsException{
        return insert(new JObject(location), new JObject(newObject));
    }

    public JObject insert(int location, char newObject) throws WrongExecutionTypeException, UnrecognizedSymbolsException{
        return insert(new JObject(location), new JObject(newObject));
    }

    public JObject insert(int location, boolean newObject) throws WrongExecutionTypeException, UnrecognizedSymbolsException{
        return insert(new JObject(location), new JObject(newObject));
    }

    // endregion

    // endregion

    // region removing objects from collection

    public JObject remove(Object key) throws WrongExecutionTypeException, UnrecognizedSymbolsException{

        if(getType().isSet()) {
            throw new RuntimeException("Cannot remove elements from a Set.");
        }

        if (key == null) {
            throw new NullPointerException("Null received for Key.");
        }

        JObject keyObj = convertToJObject(key);

        if(keyObj.isNull()){
            throw new RuntimeException("Couldn't determine the type for the index.");
        }

        if(getType().isList())
        {
            int indexNumber = -1;

            if (keyObj.isNumber()) {
                indexNumber = keyObj.intValue();
            }

            String indexString = Integer.toString(indexNumber);

            if(!getSubObjects().containsKey(indexString)) {
                return null;
            }

            JObject output = getSubObjects().getOrDefault(indexString, null);

            int counter = indexNumber;
            for(int x = indexNumber+1; x < getSubObjects().size(); x += 1)
            {
                int oldKey = x;
                int newKey = counter;

                JObject parameterObject = getValue(oldKey);

                setValue(newKey, parameterObject);

                counter += 1;
            }

            getSubObjects().remove(Long.toString(counter));

            return output;
        }
        else {
            return getSubObjects().remove(keyObj.toString(false));
        }
    }

    public JObject remove(int index) throws WrongExecutionTypeException {

        return remove(new JObject(index));
    }

    /**
     * Removes an object from the beginning of a list and returns the removed item.
     * @return Returns the removed object.
     * @throws WrongExecutionTypeException This method throws this error if executed on anything other than a list.
     */
    public JObject popFromBeginning() throws WrongExecutionTypeException {
        if(!getType().isList()){
            throw new WrongExecutionTypeException(typeName, ObjectTypes.List.name());
        }

        if(size() < 1){
            return null;
        }

        JObject[] values = toJObjectArray();
        JObject output = values[0];

        resetSubObjects();

        for(int x = 1; x < values.length; x += 1){
            setValue(x-1, values[x]);
        }

        return output;
    }

    /**
     * Removes an object from the end of a list and returns the removed item.
     * @return Returns the removed object.
     * @throws WrongExecutionTypeException This method throws this error if executed on anything other than a list.
     */
    public JObject popFromEnd() throws WrongExecutionTypeException {
        if(!getType().isList()){
            throw new WrongExecutionTypeException(typeName, ObjectTypes.List.name());
        }

        if(size() < 1){
            return null;
        }

        JObject[] values = toJObjectArray();
        JObject output = values[values.length-1];

        resetSubObjects();

        for(int x = 0; x < values.length-1; x += 1){
            setValue(x, values[x]);
        }

        return output;
    }

    /**
     * Removes an object from a list and returns the removed item.
     * @param fromBeginning Boolean flag controlling weather the object is removed from
     *                      the beginning or the end of the list.
     * @return Returns the removed object.
     * @throws WrongExecutionTypeException This method throws this error if executed on anything other than a list.
     */
    public JObject pop(boolean fromBeginning) throws WrongExecutionTypeException {
        if(fromBeginning){
            return popFromBeginning();
        }
        else{
            return popFromEnd();
        }
    }

    /**
     * Removes an object from the beginning of a list and returns the removed item.
     * @return Returns the removed object.
     * @throws WrongExecutionTypeException This method throws this error if executed on anything other than a list.
     */
    public JObject pop() throws WrongExecutionTypeException {
        return popFromBeginning();
    }

    // endregion

    // region removing duplicate objects from collection

    private void removeDuplicates_prv(){
        ArrayList<JObject> newListElements = new ArrayList<>();

        for(JObject elementInOldList : this){
            boolean duplicateFound = false;
            for(int x = 0; x < newListElements.size() && !duplicateFound; x += 1){
                JObject elementInNewList = newListElements.get(x);
                if(elementInNewList.equals(elementInOldList)){
                    duplicateFound = true;
                }
            }

            if(!duplicateFound){
                newListElements.add(elementInOldList);
            }
        }

        resetSubObjects();

        for(int x = 0 ;x < newListElements.size(); x += 1){
            getSubObjects().put(Integer.toString(x), newListElements.get(x));
        }
    }

    /**
     * Removes all duplicate items from a list.
     * <br>
     * This method is chainable.
     * @return Returns this JObject after it has been modified.
     * @throws WrongExecutionTypeException This method will throw this error if executed on anything
     *                                     other than a list JObject.
     */
    public JObject removeDuplicates() throws WrongExecutionTypeException {

        if (!this.getType().isList()){
            throw new WrongExecutionTypeException(typeName, ObjectTypes.List.name());
        }

        removeDuplicates_prv();

        return this;
    }

    // endregion

    // region list-Set conversions

    /**
     * Converts this object into a list if it is a set.
     * @return Returns the calling JObject after ths conversion is complete.
     */
    public JObject convertSetToList() throws WrongExecutionTypeException {

        if(getType().isSet()) {
            this.typeName = ObjectTypes.List.toString();
        }
        return this;
    }

    /**
     * Converts this object into a set if it is a list.
     * @return Returns the calling JObject after ths conversion is complete.
     */
    public JObject convertListToSet() throws WrongExecutionTypeException {

        if(getType().isList()) {
            this.typeName = ObjectTypes.Set.toString();
        }
        return this;
    }

    /**
     * Converts all sets contained in this JObject into lists.
     * @return Returns the calling JObject after ths conversion is complete.
     */
    private JObject convertSetToList_recursive(){
        JObjectExecutionPredicate<JObject> conversion = new JObjectExecutionPredicate<JObject>() {
            @Override
            public JObject execute(JObject input) {
                if(input.getType().isSet()){
                    return input.convertSetToList();
                }
                return input;
            }
        };
        return this.executeRecursively(conversion);
    }

    /**
     * Converts all lists contained in this JObject into sets.
     * @return Returns the calling JObject after ths conversion is complete.
     */
    private JObject convertListToSet_recursive(){
        JObjectExecutionPredicate<JObject> conversion = new JObjectExecutionPredicate<JObject>() {
            @Override
            public JObject execute(JObject input) {
                if(input.getType().isList()){
                    return input.convertListToSet();
                }
                return input;
            }
        };
        return this.executeRecursively(conversion);
    }

    // endregion

    // region combining object collections

    /**
     * Combines JObject Dictionaries and JObject Lists with other JObjects of the same type (Dictionaries can only
     * be combined with Dictionaries and Lists can only be combined with Lists). If the 2 dictionaries have duplicate
     * keys then the old dictionary is updated with the values from the new dictionary.
     * <br>
     * This method is chainable.
     * @return Returns the original JObject updated with the new values.
     * @param newObject The JObject containing the new values to be added.
     */
    public JObject combine(Object newObject) throws WrongExecutionTypeException, UnrecognizedSymbolsException{

        JObject newJObject = null;
        if(newObject instanceof JObject var){
            newJObject = var;
        }
        else{
            newJObject = new JObject(newObject);
        }

        if(getType().isDictionary()){
            if(!newJObject.getType().isDictionary()){
                throw new RuntimeException("The new value must be a dictionary.");
            }

            String[] newObjKeys = newJObject.getKeys();

            for(String key : newObjKeys){
                JObject newSubObj = newJObject.getValue(key);
                getSubObjects().put(key, newSubObj);
            }
        }
        else if(getType().isList()){
            if(!newJObject.getType().isList()){
                throw new RuntimeException("The new value must be a list.");
            }

            int newIndex = size();

            JObject[] newValues = newJObject.getValues();
            for (JObject newValue : newValues) {
                getSubObjects().put(Integer.toString(newIndex), newValue);
                newIndex++;
            }
        }
        else{
            throw new WrongExecutionTypeException(typeName,
                    new ObjectTypes[]{
                            ObjectTypes.Dictionary, ObjectTypes.List});
        }

        return this;
    }

    // endregion

    // region sorting

    // region basic sorting

    public JObject sort(boolean ascendingOrder) throws WrongExecutionTypeException {
        if (!getType().isList()){
            throw new WrongExecutionTypeException(getTypeName(), ObjectTypes.List.name());
        }

        JObject[] subObjects = this.getValues();

        Vector<String> subObjectsStrings = new Vector<>();

        for(JObject obj : subObjects){
            subObjectsStrings.add(obj.toString());
        }

        if(!ascendingOrder) {
            Collections.sort(subObjectsStrings, java.util.Comparator.reverseOrder());
        }
        else{
            Collections.sort(subObjectsStrings);
        }

        HashMap<String, JObject> newSubObjects = new HashMap<>();

        for(int x = 0; x < subObjectsStrings.size(); x += 1){
            newSubObjects.put(Integer.toString(x), new JObject(subObjectsStrings.get(x)));
        }

        this.getSubObjectsContainer().setHashMap(newSubObjects);

        return this;
    }

    public JObject sort() throws WrongExecutionTypeException {
        return this.sort(true);
    }

    // endregion

    // region sorting-reverse order

    /**
     * Reverses the order of elements in JObject Lists.
     * <br>
     * This function only works on lists.
     * <br>
     * This function is chainable.
     * @return Returns the JObject list after the order of its elements has been reversed.
     */
    public JObject reverseOrder() throws WrongExecutionTypeException {
        if (!getType().isList()){
            throw new WrongExecutionTypeException(getTypeName(), ObjectTypes.List.name());
        }

        // copies the sub objects from of the JObject
        JObject[] subObjects = this.getValues();

        // finds the halfway point
        int halfWay = 0;
        if ((subObjects.length % 2) == 0){
            halfWay = subObjects.length/2;
        }
        else{
            halfWay = ((subObjects.length-1)/2);
        }

        // reverses order of sub objects by swapping elements at the end with elements near the start
        int counter = subObjects.length-1;
        for(int x = 0; x < halfWay; x++){

            // extracts element near the end of the array
            JObject temp = subObjects[counter];

            // copies element near the start into the position of the previously extracted element
            subObjects[counter] = subObjects[x];

            // copies extracted element into the position near the start
            subObjects[x] = temp;

            // increments end position
            counter--;
        }

        // clears the current sub objects away
        resetSubObjects();

        // re-inserts sub objects back into JObject
        counter = 0;
        for(JObject obj : subObjects){
            this.getSubObjects().put(Integer.toString(counter), obj);
            counter++;
        }

        return this;
    }

    // endregion

    // region comparator sort

    public JObject sort(Comparator sortComparator) throws WrongExecutionTypeException {
        if (!getType().isList()){
            throw new WrongExecutionTypeException(getTypeName(), ObjectTypes.List.name());
        }

        JObject[] subObjects = this.getValues();

        Vector<String> subObjectsStrings = new Vector<>();

        for(JObject obj : subObjects){
            subObjectsStrings.add(obj.toString());
        }

        subObjectsStrings.sort(sortComparator);

        HashMap<String, JObject> newSubObjects = new HashMap<>();

        for(int x = 0; x < subObjectsStrings.size(); x += 1){
            newSubObjects.put(Integer.toString(x), new JObject(subObjectsStrings.get(x)));
        }

        this.getSubObjectsContainer().setHashMap(newSubObjects);

        return this;
    }

    // endregion

    // endregion

    // region contains value

    /**
     * Tests weather the value is contained within this JObject. If this JObject is a non-primitive type
     * (list, set, dictionary, etc..), this method checks the contained sub objects for a match. If this JObject is a
     * string JObject, then it checks weather or not the value's string from is contained within.
     * @param value value whose presence in this JObject is to be tested
     * @return Returns true if the value is contained within this object.
     */
    @Override
    public boolean containsValue(Object value){
        if(isPrimitive()){
            if(isString()){
                return toJString().contains(value.toString());
            }
            return false;
        }

        for(JObject obj : getValues()) {
            if(obj.equals(value)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Tests weather the value is contained within this JObject. If this JObject is a non-primitive type
     * (list, set, dictionary, etc..), this method checks the contained sub objects for a match. If this JObject is a
     * string JObject, then it checks weather or not the value's string from is contained within.
     * @param value value whose presence in this JObject is to be tested
     * @return Returns true if the value is contained within this object.
     */
    public boolean containsValue(double value){
        if(isPrimitive()){
            if(isString()){
                return toJString().contains(Double.toString(value));
            }
            return false;
        }

        for(JObject obj : getValues()) {
            if(obj.equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests weather the value is contained within this JObject. If this JObject is a non-primitive type
     * (list, set, dictionary, etc..), this method checks the contained sub objects for a match. If this JObject is a
     * string JObject, then it checks weather or not the value's string from is contained within.
     * @param value value whose presence in this JObject is to be tested
     * @return Returns true if the value is contained within this object.
     */
    public boolean containsValue(float value){
        if(isPrimitive()){
            if(isString()){
                return toJString().contains(Float.toString(value));
            }
            return false;
        }

        for(JObject obj : getValues()) {
            if(obj.equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests weather the value is contained within this JObject. If this JObject is a non-primitive type
     * (list, set, dictionary, etc..), this method checks the contained sub objects for a match. If this JObject is a
     * string JObject, then it checks weather or not the value's string from is contained within.
     * @param value value whose presence in this JObject is to be tested
     * @return Returns true if the value is contained within this object.
     */
    public boolean containsValue(long value){
        if(isPrimitive()){
            if(isString()){
                return toJString().contains(Long.toString(value));
            }
            return false;
        }

        for(JObject obj : getValues()) {
            if(obj.equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests weather the value is contained within this JObject. If this JObject is a non-primitive type
     * (list, set, dictionary, etc..), this method checks the contained sub objects for a match. If this JObject is a
     * string JObject, then it checks weather or not the value's string from is contained within.
     * @param value value whose presence in this JObject is to be tested
     * @return Returns true if the value is contained within this object.
     */
    public boolean containsValue(int value){
        if(isPrimitive()){
            if(isString()){
                return toJString().contains(Integer.toString(value));
            }
            return false;
        }

        for(JObject obj : getValues()) {
            if(obj.equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests weather the value is contained within this JObject. If this JObject is a non-primitive type
     * (list, set, dictionary, etc..), this method checks the contained sub objects for a match. If this JObject is a
     * string JObject, then it checks weather or not the value's string from is contained within.
     * @param value value whose presence in this JObject is to be tested
     * @return Returns true if the value is contained within this object.
     */
    public boolean containsValue(short value){
        if(isPrimitive()){
            if(isString()){
                return toJString().contains(Short.toString(value));
            }
            return false;
        }

        for(JObject obj : getValues()) {
            if(obj.equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests weather the value is contained within this JObject. If this JObject is a non-primitive type
     * (list, set, dictionary, etc..), this method checks the contained sub objects for a match. If this JObject is a
     * string JObject, then it checks weather or not the value's string from is contained within.
     * @param value value whose presence in this JObject is to be tested
     * @return Returns true if the value is contained within this object.
     */
    public boolean containsValue(byte value){
        if(isPrimitive()){
            if(isString()){
                return toJString().contains(Byte.toString(value));
            }
            return false;
        }

        for(JObject obj : getValues()) {
            if(obj.equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests weather the value is contained within this JObject. If this JObject is a non-primitive type
     * (list, set, dictionary, etc..), this method checks the contained sub objects for a match. If this JObject is a
     * string JObject, then it checks weather or not the value's string from is contained within.
     * @param value value whose presence in this JObject is to be tested
     * @return Returns true if the value is contained within this object.
     */
    public boolean containsValue(boolean value){
        if(isPrimitive()){
            if(isString()){
                return toJString().contains(Boolean.toString(value));
            }
            return false;
        }

        for(JObject obj : getValues()) {
            if(obj.equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests weather the value is contained within this JObject. If this JObject is a non-primitive type
     * (list, set, dictionary, etc..), this method checks the contained sub objects for a match. If this JObject is a
     * string JObject, then it checks weather or not the value's string from is contained within.
     * @param value value whose presence in this JObject is to be tested
     * @return Returns true if the value is contained within this object.
     */
    public boolean containsValue(char value){
        if(isPrimitive()){
            if(isString()){
                return toJString().contains(value);
            }
            return false;
        }

        for(JObject obj : getValues()) {
            if(obj.equals(value)) {
                return true;
            }
        }
        return false;
    }

    // endregion

    // region contains key

    /**
     * Returns true if this object contains a value mapping for the given key.
     * @param key The key to be tested.
     */
    public boolean containsKey(int key){
        return containsKey(Integer.toString(key));
    }

    /**
     * Returns true if this object contains a value mapping for the given key.
     * @param key The key to be tested.
     */
    public boolean containsKey(long key){
        return containsKey(Long.toString(key));
    }

    /**
     * Tests if the given object contains every instance of the given keys.
     * @param keys The list of keys to be tested for.
     * @return Returns false if any of the keys are missing, or else true.
     */
    public boolean containsEveryKey(Object... keys){
        for(Object key : keys){
            if(!containsKey(key)){
                return false;
            }
        }
        return true;
    }

    /**
     * Tests if the given object contains any instance of the given keys.
     * @param keys The list of keys to be tested for.
     * @return Returns true if any of the keys are present, or else false.
     */
    public boolean containsAnyKey(Object... keys){
        return containsKey(keys);
    }

    // endregion

    // region remove sub objects by keys

    public JObject removeSubObjectsByKeys(String... keys){
        if(getType().isSet()){
            throw new RuntimeException("Sets cannot be modified.");
        }

        if(size() > 0) {
            for (String key : keys) {
                getSubObjects().remove(key);
            }
        }
        return this;
    }

    public JObject removeAllSubObjectByKeysExcept(String... keys){

        if(getType().isSet()){
            throw new RuntimeException("Sets cannot be modified.");
        }

        HashMap<String, JObject> newSubObjects = new HashMap<>();

        if(size() > 0) {
            for (String key : keys) {
                if (getSubObjects().containsKey(key)) {
                    JObject subObject = getSubObjects().getOrDefault(key, null);
                    if (subObject.isNull()) {
                        subObject = null;
                    }
                    newSubObjects.put(key, subObject);
                }
            }
        }

        getSubObjectsContainer().setHashMap(newSubObjects);

        return this;
    }

    // endregion

    // region casting shortcuts

    public JPrimitive jPrimitiveValue() throws WrongExecutionTypeException {
        if(isPrimitive() || isSymbol()){
            JPrimitive output = new JPrimitive();
            if(isNumber()){
                output.setTo(jNumberValue());
            }
            else if(isBoolean()){
                output.setTo(booleanValue());
            }
            else{
                output.setTo(toJString());
            }
            return output;
        }
        throw new WrongExecutionTypeException(getTypeName(), new String[]{
                ObjectTypes.Number.name(),
                ObjectTypes.String.name(),
                ObjectTypes.Boolean.name(),
                ObjectTypes.Symbol.name(),
                ObjectTypes.Operator.name()
        });
    }

    // endregion

    //region iterator function overrides

    /**
     * Returns an iterator of JObjects contained within this object.
     * <br>
     * If this object is a JObject of a primitive type, then the iterator will contain no elements.
     * <br>
     * If this object is a list or a set, then it will iterate over the objects in order of their index.
     * <br>
     * If this object is a set, then this will iterator over deep copies of each of the elements
     * contained within this object.
     * <br>
     * If this object is any other type, of non-primitive JObject, then the order of sub-objects iterated over
     * will be random.
     */
    @Override
    public Iterator<JObject> iterator(){

        ArrayList<JObject> children = new ArrayList<>();
        if(isList() || isSet()){
            for(int x = 0; x < size(); x += 1){
                children.add(this.getValue(x));
            }
        }
        else if(isPrimitive()){
            // do nothing, empty list
        }
        else{
            children.addAll(List.of(this.getValues()));
        }

        return new Iterator<JObject>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size();
            }

            @Override
            public JObject next() {
                return children.get(currentIndex++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    // endregion

    // region comparable function overrides

    @Override
    public int compareTo(JObject o) {
        if(o == null){
            return -1;
        }

        if(o == this) {
            return 0;
        }

        if(o.isNull()) {
            if(isNull()){
                return 0;
            }
            else {
                return -1;
            }
        }

        return this.toString().compareTo(o.toString());
    }

    // endregion

    // region execute function recursively

    /**
     * Executes the process on every JObject within the calling JObject, including all nested JObjects.
     * The first element modified will be the calling JObject.
     * @param process The process to be executed.
     * @return Returns the calling JObject after all processing is complete.
     */
    public JObject executeRecursively(JObjectExecutionPredicate<JObject> process){
        ArrayList<JObject> que = new ArrayList<>();
        que.add(this);
        while(que.size() > 0){
            JObject loopObject = que.get(0);
            que.remove(0);

            loopObject.setTo(process.execute(loopObject.clone()));

            if(!loopObject.isPrimitive() && loopObject.getSubObjects().size() > 0){
                que.addAll(loopObject.getSubObjects().values());
            }
        }

        return this;
    }

    /**
     * Executes the process on the calling JObject's sub objects;
     * @param process The process to be executed.
     * @return Returns the calling JObject after all processing is complete.
     */
    public JObject executeOnSubObjects(JObjectExecutionPredicate<JObject> process){
        if(getType().isSet()){
            throw new WrongExecutionTypeException(getTypeName(), "any JObject that isn't a Set");
        }
        getSubObjects().replaceAll((key, value) -> process.execute(value.clone()));
        return this;
    }

    // endregion

    // region manipulating via predicates

    /**
     * Copies all sub objects from a list or set that meet the condition.
     * @param condition The condition by which the sub objects are identified to be copied.
     * @return Returns an array containing all objects from the list or set that meet the condition.
     */
    public JObject[] copyIf(Predicate<? super JObject> condition){
        if(!getType().isList() && !getType().isSet()){
            throw new WrongExecutionTypeException(
                    getTypeName(),
                    new ObjectTypes[]{ObjectTypes.List, ObjectTypes.Set});
        }

        ArrayList<JObject> output = new ArrayList<>();

        if(getType().isSet()){
            for(JObject ele : this){
                if(condition.test(ele)){
                    output.add(ele.clone());
                }
            }
        }
        else{
            for(JObject ele : this){
                if(condition.test(ele)){
                    output.add(ele);
                }
            }
        }

        return output.toArray(new JObject[0]);
    }

    /**
     * Copies all sub objects from a list or set that do not meet the condition.
     * @param condition The condition by which the sub objects are identified to be excluded.
     * @return Returns an array containing all objects from the list or set that do not meet the condition.
     */
    public JObject[] copyIfNot(Predicate<? super JObject> condition){
        return copyIf(condition.negate());
    }

    /**
     * Removes all sub objects from a list if they meet the condition.
     * @param condition The condition by which the sub objects are identified to be removed.
     * @return Returns the object on which this method was called after it has been modified.
     */
    public JObject removeIf(Predicate<? super JObject> condition){
        if(!getType().isList()){
            throw new WrongExecutionTypeException(getTypeName(), ObjectTypes.List);
        }

        ArrayList<JObject> output = new ArrayList<>();
        for(JObject ele : this){
            if(!condition.test(ele)){
                output.add(ele);
            }
        }

        setTo(output.toArray(new JObject[0]));

        return this;
    }

    /**
     * Removes all sub objects from a list if they do not meet the condition.
     * @param condition The condition by which the sub objects are identified to be kept.
     * @return Returns the object on which this method was called after it has been modified.
     */
    public JObject removeIfNot(Predicate<? super JObject> condition){
        return removeIf(condition.negate());
    }

    // endregion

    // region json functions

    public static final String JSON_BOOLEAN_VALUE_KEY = "boolean_value";
    public static final String JSON_INTEGER_VALUE_KEY = "integer_value";
    public static final String JSON_LONG_VALUE_KEY = "long_value";
    public static final String JSON_DOUBLE_VALUE_KEY = "double_value";
    public static final String JSON_STRING_VALUE_KEY = "string_value";
    public static final String JSON_LIST_VALUE_KEY = "list_value";
    public static final String JSON_SET_VALUE_KEY = "set_value";

    private Object getPrimitiveValueAsObject(){
        if(isDouble()){
            return doubleValue();
        }
        else if(isFloat()){
            return floatValue();
        }
        else if(isLong()){
            return longValue();
        }
        else if(isInteger()){
            return intValue();
        }
        else if(isShort()){
            return shortValue();
        }
        else if(isByte()){
            return byteValue();
        }
        else if(isBoolean()){
            return booleanValue();
        }
        else{
            return toString(false);
        }
    }

    /**
     * Converts the calling JObject into a valid JSON value.
     * <br>
     * If the verbose flag is set false, then only the value will be returned. Primitive values will be converted
     * into the appropriate matching JSON value type, lists and sets will be converted into JSONArrays and all
     * other JObjects will be converted into JSON objects with match key-value pairs.
     * <br>
     * If the verbose flag is set to true, then the function will wrap any primitive values, lists and sets inside
     * a JSON object with a single key-value pair. The key will be the matching JSON_[type]_VALUE_KEY constant for
     * the value. This affect is recursive to the entire JObject and its contents.
     * @param verbose Boolean flag controlling how concise the returned value is.
     * @return Returns a Boolean, Integer, Long, Double, String, JSONArray or JSONObject: depending on the
     * type of the calling JObject and the value of the verbose flag.
     */
    public Object toJSONElement(boolean verbose){
        if(isNull()){
            return JSONObject.NULL;
        }
        else if(isBoolean()){
            Object value = getPrimitiveValueAsObject();
            if(verbose){
                JSONObject output = new JSONObject();
                output.put(JSON_BOOLEAN_VALUE_KEY, value);
                return output;
            }
            else{
                return value;
            }
        }
        else if(isByte() || isShort() || isInteger()){
            Object value = intValue();
            if(verbose){
                JSONObject output = new JSONObject();
                output.put(JSON_INTEGER_VALUE_KEY, value);
                return output;
            }
            else{
                return value;
            }
        }
        else if(isLong()){
            Object value = longValue();
            if(verbose) {
                JSONObject output = new JSONObject();
                output.put(JSON_LONG_VALUE_KEY, value);
                return output;
            }
            else{
                return value;
            }
        }
        else if(isFloat() || isDouble()){
            Object value = jNumberValue().doubleValue();
            if(verbose) {
                JSONObject output = new JSONObject();
                output.put(JSON_DOUBLE_VALUE_KEY, value);
                return output;
            }
            else{
                return value;
            }
        }
        else if(isString()){
            Object value = toString(false);
            if(verbose) {
                JSONObject output = new JSONObject();
                output.put(JSON_STRING_VALUE_KEY, value);
                return output;
            }
            else{
                return value;
            }
        }
        else if(getType().isSet() || getType().isList()) {
            if(verbose){
                JSONObject output = new JSONObject();
                JSONArray value = new JSONArray();

                ArrayList<Object> subObjects = new ArrayList<>();
                for(JObject subObject : this){
                    subObjects.add(subObject.toJSONElement(true));
                }

                value.putAll(subObjects);

                if(getType().isSet()){
                    output.put(JSON_SET_VALUE_KEY, value);
                }
                else{
                    output.put(JSON_LIST_VALUE_KEY, value);
                }

                return output;
            }
            else{
                JSONArray output = new JSONArray();

                ArrayList<Object> subObjects = new ArrayList<>();
                for(JObject subObject : this){
                    subObjects.add(subObject.toJSONElement(false));
                }

                output.putAll(subObjects);

                return output;
            }

        }
        else {
            JSONObject output = new JSONObject();
            if (this.getSubObjects() != null) {
                for (Map.Entry<String, JObject> entry : this.entrySet()) {
                    output.put(entry.getKey(), entry.getValue().toJSONElement(verbose));
                }

                if (!getType().isDictionary() && getTypeName() != null && getTypeName().length() > 0) {
                    output.put(ParameterName_ClassName, this.getTypeName());
                }
            }
            return output;
        }
    }

    /**
     * Overload that defaults to a non-verbose execution.
     * @return Returns a Boolean, Integer, Long, Double, String, JSONArray or JSONObject: depending on the
     * type of the calling JObject.
     */
    public Object toJSONElement(){
        return this.toJSONElement(false);
    }

    /**
     * Creates a new JSONObject from the existing JObject instance.
     * <br>
     * <br>
     * If the JObject is a list, a set or a primitive value, this function will still return a JSONObject,
     * with a single key value pair. The key will be one of the matching JSON_[type]_VALUE_KEY constants.
     * The value will be of the corresponding JSON value type. (Boolean, Integer, Long, Double, String, JSONArray)
     * <br>
     * @return Returns a JSON object with the same corresponding values as the calling JObject instance.
     */
    public JSONObject toJSONObject(){
        JSONObject output = new JSONObject();
        if(isNull()){
            return null;
        }
        else if(isPrimitive()){
            return (JSONObject)this.toJSONElement(true);
        }
        else if(getType().isList() || getType().isSet()) {
            JSONArray value = new JSONArray();

            ArrayList<Object> subObjects = new ArrayList<>();
            for(JObject subObject : this){
                subObjects.add(subObject.toJSONElement(false));
            }
            value.putAll(subObjects);

            if(getType().isList()) {
                output.put(JSON_LIST_VALUE_KEY, value);
            }
            else{
                output.put(JSON_SET_VALUE_KEY, value);
            }
            return output;
        }
        else{
            if(!isPrimitive()) {
                for (Map.Entry<String, JObject> entry : getSubObjects().entrySet()) {
                    if(entry.getValue() != null) {
                        if(entry.getValue().isPrimitive()){
                            output.put(entry.getKey(), entry.getValue().toJSONElement(false));
                        }
                        else if(entry.getValue().getType().isList() || entry.getValue().getType().isSet()){
                            JSONArray value = new JSONArray();

                            ArrayList<Object> subObjects = new ArrayList<>();
                            for(JObject subObject : entry.getValue()){
                                subObjects.add(subObject.toJSONElement(false));
                            }

                            value.putAll(subObjects);
                            output.put(entry.getKey(), value);
                        }
                        else {
                            output.put(entry.getKey(), entry.getValue().toJSONElement());
                        }
                    }
                    else{
                        output.put(entry.getKey(), JSONObject.NULL);
                    }
                }
            }
            if(!getType().isDictionary() && getTypeName() != null && getTypeName().length() > 0){
                output.put(ParameterName_ClassName, getTypeName());
            }
        }
        return output;
    }

    // endregion

    // region static operator functions

    public static JObject add(JObject var1, JObject var2){

        MiscFunctions.nullCheck(var1, "var1");
        MiscFunctions.nullCheck(var2, "var2");
        return new JObject(var1.add(var2));
    }

    public static JObject subtract(JObject var1, JObject var2){
        MiscFunctions.nullCheck(var1, "var1");
        MiscFunctions.nullCheck(var2, "var2");
        return new JObject(var1.subtract(var2));
    }

    public static JObject multiply(JObject var1, JObject var2){
        MiscFunctions.nullCheck(var1, "var1");
        MiscFunctions.nullCheck(var2, "var2");
        return new JObject(var1.multiply(var2));
    }

    public static JObject divide(JObject var1, JObject var2){

        MiscFunctions.nullCheck(var1, "var1");
        MiscFunctions.nullCheck(var2, "var2");

        return new JObject(var1.divide(var2));
    }

    public static JObject power(JObject var1, JObject var2){
        MiscFunctions.nullCheck(var1, "var1");
        MiscFunctions.nullCheck(var2, "var2");

        return new JObject(var1.power(var2));
    }

    public static JObject modulo(JObject var1, JObject var2){
        MiscFunctions.nullCheck(var1, "var1");
        MiscFunctions.nullCheck(var2, "var2");

        return new JObject(var1.modulo(var2));
    }

    public static JObject greaterThenOrEqualTo(JObject var1, JObject var2){
        MiscFunctions.nullCheck(var1, "var1");
        MiscFunctions.nullCheck(var2, "var2");

        return new JObject(var1.greaterThanOrEqualTo(var2));
    }

    public static JObject lessThenOrEqualTo(JObject var1, JObject var2){
        MiscFunctions.nullCheck(var1, "var1");
        MiscFunctions.nullCheck(var2, "var2");

        return new JObject(var1.lessThanOrEqualTo(var2));
    }

    public static JObject greaterThen(JObject var1, JObject var2){
        MiscFunctions.nullCheck(var1, "var1");
        MiscFunctions.nullCheck(var2, "var2");

        return new JObject(var1.greaterThan(var2));
    }

    public static JObject lessThen(JObject var1, JObject var2){
        MiscFunctions.nullCheck(var1, "var1");
        MiscFunctions.nullCheck(var2, "var2");

        return new JObject(var1.lessThan(var2));
    }

    public static JObject equalTo(JObject var1, JObject var2){
        MiscFunctions.nullCheck(var1, "var1");
        MiscFunctions.nullCheck(var2, "var2");

        return new JObject(var1.equalTo(var2));
    }

    public static JObject notEqualTo(JObject var1, JObject var2){
        MiscFunctions.nullCheck(var1, "var1");
        MiscFunctions.nullCheck(var2, "var2");

        return new JObject(var1.notEqualTo(var2));
    }

    public static JObject or(JObject var1, JObject var2){
        MiscFunctions.nullCheck(var1, "var1");
        MiscFunctions.nullCheck(var2, "var2");

        return new JObject(var1.booleanValue() || var2.booleanValue());
    }

    public static JObject and(JObject var1, JObject var2){
        MiscFunctions.nullCheck(var1, "var1");
        MiscFunctions.nullCheck(var2, "var2");

        return new JObject(var1.booleanValue() && var2.booleanValue());
    }

    // endregion

    // region string evaluation

    private static JPrimitive evaluate_core(JString inputString) throws UnrecognizedSymbolsException {

        JString output;

        // region preprocessing

        // region throws an error for incomplete quote sets

        if (inputString.contains("\"")) {
            double count = inputString.getSubstringCount("\"");
            if ((count % 2) != 0) {
                throw new RuntimeException(inputString + " has an unequal number of quotation marks (\").");
            }
        }

        // endregion

        // region throws an error for incomplete parentheses sets

        if (inputString.contains("(") || inputString.contains(")")) {
            int count1 = inputString.getSubstringCount("(");
            int count2 = inputString.getSubstringCount(")");
            if(count1 != count2) {
                throw new RuntimeException(inputString + " has an unequal number of opening and closing parentheses.");
            }
        }

        // endregion

        JString[][] substringsSets = JString.splitStringForExpressionProcessing_includeUnrecognizedSymbols(inputString);
        JString[] substrings_1 = substringsSets[0];
        JString[] unrecognizedSymbols = substringsSets[1];

        // region throws error for unknown symbols

        if(unrecognizedSymbols.length > 0){
            throw new UnrecognizedSymbolsException(inputString.toString(), unrecognizedSymbols);
        }

        // endregion

        // region combines "-" with numbers where appropriate

        ArrayList<JString> substrings_2 = new ArrayList<>();
        boolean skipNext = false;
        for(int x = 0; x < substrings_1.length; x += 1) {
            if(skipNext) {
                skipNext = false;
                continue;
            }

            JString s = substrings_1[x];
            if(s.equals("-")) {
                if((x+1) < substrings_1.length) {
                    JString nextS = substrings_1[x+1];
                    JPrimitive ps = new JPrimitive(nextS);
                    if(ps.isNumber()) {
                        if(x != 0) {
                            JString prevS = substrings_1[x-1];
                            JPrimitive prev_ps = new JPrimitive(prevS);
                            if(prev_ps.isNumber() || prevS.equals(")")) {
                                substrings_2.add(new JString("+"));
                            }
                        }
                        substrings_2.add(s.append(nextS));
                        skipNext = true;
                    }
                }
            }
            else {
                substrings_2.add(s);
            }
        }

        substrings_1 = substrings_2.toArray(new JString[0]);
        substrings_2.clear();

        // endregion

        // region replaces all boolean values with consistently spelled replacements

        for(JString s : substrings_1){

            if (s.equals("t", true) ||
                    s.equals("true", true) ||
                    s.equals("y", true) ||
                    s.equals("yes", true)) {
                substrings_2.add(new JString("true"));
            } else if (s.equals("f", true) ||
                    s.equals("false", true) ||
                    s.equals("n", true) ||
                    s.equals("no", true)) {
                substrings_2.add(new JString("false"));
            }
            else {
                substrings_2.add(s);
            }
        }

        substrings_1 = substrings_2.toArray(new JString[0]);
        substrings_2.clear();

        // endregion

        // region combines ! with booleans where appropriate

        skipNext = false;
        for(int x = 0; x < substrings_1.length; x += 1) {
            if(skipNext) {
                skipNext = false;
                continue;
            }

            JString s = substrings_1[x];
            if(s.equals("!")) {
                if((x+1) < substrings_1.length) {
                    JString nextS = substrings_1[x+1];
                    JPrimitive ps = new JPrimitive(nextS);
                    if(ps.isBoolean()) {
                        boolean newValue = !ps.booleanValue();
                        substrings_2.add(new JString(Boolean.toString(newValue)));
                        skipNext = true;
                    }
                }
            }
            else {
                substrings_2.add(s);
            }
        }

        substrings_1 = substrings_2.toArray(new JString[0]);
        substrings_2.clear();

        // endregion

        // region adds * where appropriate

        for(int x = 0; x < substrings_1.length; x += 1)
        {
            JString s = substrings_1[x];

            if(s.equals("("))
            {
                if((x-1) >= 0)
                {
                    JString prevS = substrings_1[x-1];

                    if(prevS.equals(")"))
                    {
                        substrings_2.add(new JString("*"));
                    }
                    else {
                        JPrimitive ps = new JPrimitive(prevS);
                        if (ps.isNumber()) {
                            substrings_2.add(new JString("*"));
                        }
                    }
                }

                substrings_2.add(s);
            }
            else if(s.equals(")"))
            {
                substrings_2.add(s);

                if((x+1) < substrings_1.length)
                {
                    JString nextS = substrings_1[x+1];

                    JPrimitive ps = new JPrimitive(nextS);
                    if(ps.isNumber())
                    {
                        substrings_2.add(new JString("*"));
                    }
                }
            }
            else
            {
                substrings_2.add(s);
            }
        }

        substrings_1 = substrings_2.toArray(new JString[0]);
        substrings_2.clear();

        // endregion

        // region removes empty and white space strings

        substrings_2 = new ArrayList<>();
        for(JString str : substrings_1){
            str.trim();
            if(str.isBlank() || str.isEmpty()){

            }
            else{
                substrings_2.add(str);
            }
        }
        substrings_1 = substrings_2.toArray(new JString[0]);

        // endregion

        // endregion

        // region solve section

        output = JString.concatenateStrings(substrings_1, " ", null, null);

        JString workingString = output.clone();
        boolean continueLoop = true;
        String[] operations = MiscFunctions.getAllOperators();

        while (continueLoop)
        {
            // region checks if the root working string contains any operations

            if(!MiscFunctions.doesStringContainAnyInstance(workingString, operations) && !MiscFunctions.doesStringContainAnyInstance(workingString, new String[]{"(", ")"})) {
                continueLoop = false;
                continue;
            }

            // endregion

            // region converts the root working string into a nested string tree

            NestedStringTree_Brackets tree = new NestedStringTree_Brackets(
                    new JString(workingString), new JString("("), new JString(")"));

            if(!tree.isTreeBuilt())
            {
                throw new RuntimeException("Couldn't construct string tree for string \""
                        + workingString + "\" : " + tree.getConstructionErrorDescription());
            }

            // endregion

            // region retrieves the first top leaf in the tree for processing

            NestedStringTree_Brackets.Leaf targetLeaf = tree.getTopLeafs()[0];

            // endregion

            // region splits leaf string by operations

            substrings_1 = JString.concatenateStrings(targetLeaf.getPayloadSubstrings()).clone().trim().splitString(operations, true);

            // endregion

            // region recycles leaf if it has no operations but is contained within brackets

            if(substrings_1.length == 1)
            {
                if (targetLeaf.hasBrackets())
                {
                    workingString = workingString.replaceEach(targetLeaf.buildPayloadString(), substrings_1[0]);
                    continue;
                }
            }

            // endregion

            // region combines "-" with the next substring if the next substring is a number

            substrings_2 = new ArrayList<>();
            skipNext = false;
            for (int x = 0; x < substrings_1.length; x += 1) {

                if(skipNext)
                {
                    skipNext = false;
                    continue;
                }

                JString s = substrings_1[x].clone().trim();
                if(!JString.isNullOrEmptyString(s))
                {
                    if(s.equals("-"))
                    {
                        if((x+1) < substrings_1.length)
                        {
                            JString ns = substrings_1[x+1].clone().trim();
                            JPrimitive pns = new JPrimitive(ns);
                            if(pns.isNumber())
                            {
                                substrings_2.add(s.append(ns));
                                skipNext = true;
                            }
                        }
                    }
                    else
                    {
                        substrings_2.add(s);
                    }
                }
            }
            substrings_1 = substrings_2.toArray(new JString[0]);
            substrings_2.clear();

            // endregion

            // region creates a sub working string using the current leaf string, after it has been processed

            boolean continueSubLoop = true;
            JString subWorkingString = JString.concatenateStrings(substrings_1, " ", null, null);
            JString oldSubWorkingString = subWorkingString.clone();

            if(targetLeaf.hasBrackets()){
                oldSubWorkingString = oldSubWorkingString.prepend("( ").append(" )");
            }

            // endregion

            // region solves operations within sub working string

            while (continueSubLoop) {

                if(!MiscFunctions.doesStringContainAnyInstance(subWorkingString.toString(), operations))
                {
                    continueSubLoop = false;
                    continue;
                }

                {
                    String[] splits = new String[]{" ", "\"", "'"};
                    JString[][] brackets = JString.toJStringArray(JString.getStandardBracketPairs());
                    substrings_1 = JString.removeNullOrEmptyStrings(
                            JString.concatenateByBracketPairs(
                                    JString.splitString(
                                            subWorkingString,
                                            splits,
                                            true)
                                    , brackets)
                            , true);
                }

                // region finds operation via precedence and order

                String[] operationSet1 = new String[]{"^"};
                String[] operationSet2 = new String[]{"*", "/", "%"};
                String[] operationSet3 = new String[]{"+", "-"};
                String[] operationSet4 = new String[]{"<", "<=", ">", ">=", "==", "!=", "!", "||", "&&"};

                String[][] orderedOps = new String[][]{
                        operationSet1, operationSet2, operationSet3, operationSet4
                };

                int operationIndex_inSubstrings = -1;
                JString operator = new JString();
                boolean operationFound = false;

                for(int operationSetIndex = 0; operationSetIndex < 4 && !operationFound; operationSetIndex += 1){
                    JString[] opSet = JString.toJStringArray(orderedOps[operationSetIndex]);

                    for(int substringIndex = 0; substringIndex < substrings_1.length && !operationFound; substringIndex++) {
                        JString substring = substrings_1[substringIndex];

                        for (JString operationInSet : opSet) {
                            if (operationInSet.equals(substring)) {
                                operator = operationInSet;
                                operationIndex_inSubstrings = substringIndex;
                                operationFound = true;
                            }
                        }
                    }
                }

                // endregion

                if(operationFound) {

                    JPrimitive operand1 = null, operand2 = null;
                    String operationOutput = "";

                    if(operationIndex_inSubstrings-1 >= 0)
                    {
                        operand1 = new JPrimitive(substrings_1[operationIndex_inSubstrings-1]);
                    }

                    if(operationIndex_inSubstrings+1 < substrings_1.length)
                    {
                        operand2 = new JPrimitive(substrings_1[operationIndex_inSubstrings+1]);
                    }

                    if(operand1 == null)
                    {
                        throw new RuntimeException("Couldn't find first operand for operation " + operator + " in string " + subWorkingString);
                    }

                    if(operand2 == null)
                    {
                        throw new RuntimeException("Couldn't find second operand for operation " + operator + " in string " + subWorkingString);
                    }

                    switch (operator.toString()) {
                        case "^" -> operationOutput = JPrimitive.power(operand1, operand2).toString();
                        case "%" -> operationOutput = JPrimitive.modulo(operand1, operand2).toString();
                        case "*" -> operationOutput = JPrimitive.multiply(operand1, operand2).toString();
                        case "/" -> operationOutput = JPrimitive.divide(operand1, operand2).toString();
                        case "+" -> {
                            JPrimitive tempOutput = JPrimitive.add(operand1, operand2);
                            if (tempOutput.isString()) {
                                operationOutput = tempOutput.toString(true);
                            } else {
                                operationOutput = tempOutput.toString();
                            }
                        }
                        case "-" -> operationOutput = JPrimitive.subtract(operand1, operand2).toString();
                        case ">" -> operationOutput = JPrimitive.greaterThan(operand1, operand2).toString();
                        case ">=" -> operationOutput = JPrimitive.greaterThanOrEqualTo(operand1, operand2).toString();
                        case "<" -> operationOutput = JPrimitive.lessThan(operand1, operand2).toString();
                        case "<=" -> operationOutput = JPrimitive.lessThanOrEqualTo(operand1, operand2).toString();
                        case "!=" -> operationOutput = JPrimitive.notEqualTo(operand1, operand2).toString();
                        case "==" -> operationOutput = JPrimitive.equalTo(operand1, operand2).toString();
                        case "||" -> operationOutput = JPrimitive.or(operand1, operand2).toString();
                        case "&&" -> operationOutput = JPrimitive.and(operand1, operand2).toString();
                    }

                    String stringToReplace = operand1.toString(true) + " " + operator + " " + operand2.toString(true);

                    JString newSubWorkingString = subWorkingString.clone().replaceFirstOccurrence(stringToReplace, operationOutput);

                    if(newSubWorkingString.equals(subWorkingString)){
                        String error = "The new working string (\""+subWorkingString+"\") is the same as the old " +
                                "working string from the input string \"" + inputString + "\"";
                        throw new RuntimeException(error);
                    }
                    else{
                        subWorkingString = newSubWorkingString;
                    }
                }
                else {
                    continueSubLoop = false;
                }
            }

            // endregion

            // region updates the root string

            JString newWorkingString = workingString.clone().replaceEach(oldSubWorkingString, subWorkingString);

            if(newWorkingString.equals(workingString)) {
                continueLoop = false;
            }
            workingString = newWorkingString;

            // endregion
        }

        // endregion

        return new JPrimitive(workingString);
    }

    /**
     * Attempts to evaluate any operators inside a string to reduce it down to its simplest form.
     * @param inputString the string to be simplified/evaluated
     * @return returns an evaluated/simplified version of the string
     */
    private static JPrimitive evaluate(Object inputString) throws UnrecognizedSymbolsException {
        return evaluate_core(JString.toJString(inputString));
    }

    // endregion

    // region expression functions

    // region expression sub object construction

    /**
     * Converts this JObject into an expression JObject. Any unrecognized symbols will be considered as variable names.
     * @return Returns this JObject, after it has been converted into an expression JObject.
     */
    public JObject constructExpressionObject(CharSequence inputString){
        JString[][] allSplits = JString.splitStringForExpressionProcessing_includeUnrecognizedSymbols(
                JString.convertToJString(inputString));
        //JString[] splits = allSplits[0];
        JString[] unrecognizedSymbols = allSplits[1];

        constructExpressionRootObject(inputString, unrecognizedSymbols);
        return this;
    }

    private static JObject constructExpressionVariableObjectContainer(CharSequence[] variableNames){
        JObject output = JObject.createEmptyDictionary();
        output.setTypeName(ExpressionVariableContainer.className);

        for(CharSequence variableName : variableNames){
            JString varName = new JString(variableName);
            // all variable names nested within this variable
            ArrayList<CharSequence> allNestedVariableNames = new ArrayList<>();

            // variable names nested within this variable that are not nested within any of the other nested variables
            ArrayList<CharSequence> firstOrderNestedVariableNames = new ArrayList<>();

            for(CharSequence str1 : variableNames){
                if(varName.contains(str1) && !varName.equals(str1)){
                    allNestedVariableNames.add(str1);
                }
            }

            for(CharSequence str1 : allNestedVariableNames){
                boolean foundWithinAnother = false;
                for(int x = 0; x < allNestedVariableNames.size() && !foundWithinAnother; x += 1){
                    JString temp = new JString(allNestedVariableNames.get(x));
                    if(temp.equals(str1)){
                        continue;
                    }
                    if(temp.contains(str1)){
                        foundWithinAnother = true;
                    }
                }
                if(!foundWithinAnother){
                    firstOrderNestedVariableNames.add(str1);
                }
            }

            JObject subObject = constructExpressionVariableObject(
                    varName,
                    allNestedVariableNames.toArray(new CharSequence[0]),
                    firstOrderNestedVariableNames.toArray(new CharSequence[0]));
            output.getSubObjects().put(variableName.toString(), subObject);
        }

        return output;
    }

    private static JObject constructExpressionVariableObject(
            CharSequence variableName, CharSequence[] nestedVariables, CharSequence[] firstOrderNestedVariables){

        JObject output = JObject.createEmptyDictionary();

        output.setTypeName(ExpressionVariableObject.className);

        output.setValue(ExpressionVariableObject._Value, null);
        output.setValue(ExpressionVariableObject._OriginalSubstring, JObject.createStringJObject(variableName));
        output.setValue(ExpressionVariableObject._ModifiedSubstring, JObject.createStringJObject(variableName));

        JObject nestedVariableList = JObject.createEmptyList();
        if(nestedVariables != null) {
            for (CharSequence str2 : nestedVariables) {
                nestedVariableList.append(JObject.createStringJObject(str2));
            }
        }

        JObject firstOrderNestedVariableList = JObject.createEmptyList();
        if(firstOrderNestedVariables != null) {
            for (CharSequence str2 : firstOrderNestedVariables) {
                firstOrderNestedVariableList.append(JObject.createStringJObject(str2));
            }
        }

        output.setValue(ExpressionVariableObject._AllNestedVariables, nestedVariableList);
        output.setValue(ExpressionVariableObject._RootNestedVariables, firstOrderNestedVariableList);
        output.setValue(ExpressionVariableObject._IsAssigned, new JObject(false));

        return output;
    }

    private void constructExpressionRootObject(CharSequence inputString, CharSequence[] unrecognizedSymbols){

        clear();
        typeName = ObjectTypes.Expression.name();
        resetSubObjects();

        JObject variableContainer = JObject.constructExpressionVariableObjectContainer(unrecognizedSymbols);

        this.setValue(ExpressionObject._Variables, variableContainer);

        JObject expressionSubObject = JObject.createStringJObject(inputString);
        this.setValue(ExpressionObject._ExpressionString, expressionSubObject);
    }

    // endregion

    /**
     * Creates and returns an ExpressionObject linked to this JObject.
     */
    public ExpressionObject getExpressionObject(){
        return new ExpressionObject(this);
    }

    public ExpressionVariableContainer getExpressionVariableContainer(){
        return new ExpressionVariableContainer(this);
    }

    public ExpressionVariableObject getExpressionVariableObject(CharSequence variableName){
        return new ExpressionVariableObject(this, variableName);
    }

    // endregion

    // region from string functions

    private JObject fromString_prv(JString inputString) throws UnrecognizedSymbolsException{
        clear();

        // region null checks

        if(
                inputString == null ||
                        inputString.equals("None", true) ||
                        inputString.equals("Null", true)) {
            return this;
        }

        // endregion

        // region string pre-processing

        String[] operators = MiscFunctions.getAllOperators();
        String[] brackets = new String[]{
                "\"", "'", "[", "]", "{", "}", "(", ")"
        };

        // region attempts to parse simple strings right away, early exit

        if(isSimpleString(inputString)){

            JPrimitive ps = new JPrimitive(inputString);
            this.typeName = ps.getTypeName();
            this.value = ps.getClonedValue();

            return this;
        }

        // endregion

        // region checks if the input string is an operator, early exit

        for(String operator : operators){
            if(inputString.equals(operator)){
                this.typeName = ObjectTypes.Operator.name();
                this.value = new JString(operator);
                return this;
            }
        }

        // endregion

        // region checks if the input string is a bracket, early exit
        for(String bracket : brackets){
            if(inputString.equals(bracket)){
                this.typeName = ObjectTypes.String.name();
                this.value = new JString(bracket);

                return this;
            }
        }

        // endregion

        // region checks if the input string is a symbol, early exit
        {
            JPrimitive temp = new JPrimitive(inputString);
            if(temp.isSymbol()){
                this.typeName = ObjectTypes.Symbol.name();
                this.value = new JString(inputString);
                return this;
            }
        }

        // endregion

        // endregion

        // region checks for unknown symbols

        {
            JString[][] allSplits = JString.splitStringForExpressionProcessing_includeUnrecognizedSymbols(inputString);
            //JString[] splits = allSplits[0];
            JString[] unrecognizedSymbols = allSplits[1];

            if(unrecognizedSymbols.length > 0){

                throw new UnrecognizedSymbolsException(inputString, unrecognizedSymbols);
            }
        }

        // endregion

        // region constructs nested string tree

        NestedStringTree_Brackets tree = new NestedStringTree_Brackets(inputString);

        if(!tree.isTreeBuilt()) {
            throw new RuntimeException("NestedStringTree was not successfully built : " + tree.getConstructionErrorDescription());
        }

        for (NestedStringTree_Brackets.Leaf leaf : tree.getAllLeafs()) {
            if (leaf.hasCommas() && !leaf.hasBrackets()) {
                leaf.setBrackets("[", "]");
            }
        }

        tree.collapseSingleElementObjects();

        // endregion

        // region while loop

        Vector<JObject> queSet1 = new Vector<>();
        Vector<JString> queSet2 = new Vector<>();

        queSet1.add(this);
        queSet2.add(new JString(tree.getRootLeaf().getIdString()));

        boolean continueLoop = true;

        while (continueLoop) {

            JObject loopObject = null;
            JString loopString = null;
            if (queSet1.size() > 0) {
                if (queSet2.size() > 0) {
                    loopObject = queSet1.get(0);
                    loopString = queSet2.get(0);
                    queSet1.remove(0);
                    queSet2.remove(0);
                }
            }

            if(loopObject == null) {
                continueLoop = false;
                continue;
            }

            if(loopString.equalsIgnoreCase("Null")){
                continue;
            }

            NestedStringTree_Brackets.Leaf loopLeaf = tree.getLeaf(loopString.trim());

            boolean useLoopLeaf = false;
            if(loopLeaf != null){
                if(loopLeaf.hasBrackets() || loopLeaf.hasCommas()){
                    useLoopLeaf = true;
                }
            }

            if(!useLoopLeaf && loopLeaf != null){
                loopString = tree.rebuildString(loopLeaf);
            }

            if(useLoopLeaf){

                boolean isList = false;
                if(loopLeaf.hasBrackets() && loopLeaf.getOpeningBracket().equals("[") && loopLeaf.getClosingBracket().equals("]")){
                    isList = true;
                }
                else if(!loopLeaf.hasBrackets() && loopLeaf.hasCommas()){
                    isList = true;
                }

                if (isList) {

                    loopObject.resetSubObjects();

                    loopObject.typeName = ObjectTypes.List.toString();

                    JString[] substrings = JString.splitStringButConcatenateBrackets(
                            JString.concatenateStrings(loopLeaf.getPayloadSubstrings(),
                                    ",", null, null),
                            ",", false);

                    if(substrings.length == 1 && substrings[0].length() == 0){
                        // do nothing, the list is empty
                    }
                    else{
                        int counter = 0;
                        for(JString s : substrings) {

                            if(s.isEmpty() || s.isBlank()){
                                continue;
                            }

                            if(s.equals(",")){
                                continue;
                            }

                            String keyValue = Integer.toString(counter);

                            JObject subObject = new JObject();
                            queSet1.add(subObject);
                            queSet2.add(s);

                            loopObject.getSubObjects().put(keyValue, subObject);
                            counter += 1;
                        }
                    }
                }
                else if (loopLeaf.hasBrackets() && loopLeaf.getOpeningBracket().equals("(") && loopLeaf.getClosingBracket().equals(")")) {

                    JString loopLeafString = tree.rebuildString(loopLeaf);
                    boolean treatAsSet = false;

                    if (loopLeaf.hasCommas()) {
                        treatAsSet = true;
                    }

                    if(!treatAsSet){
                        Long[] childIds = loopLeaf.getChildLeafIds().toArray(new Long[0]);
                        for(int x = 0; x < childIds.length && !treatAsSet; x += 1){
                            Long childId = childIds[x];
                            NestedStringTree_Brackets.Leaf childLeaf = tree.getLeaf(childId);
                            if(childLeaf != null){
                                if(childLeaf.hasCommas()){
                                    treatAsSet = true;
                                }
                            }
                        }
                    }

                    if (treatAsSet) {
                        loopObject.typeName = ObjectTypes.Set.toString();
                        loopObject.resetSubObjects();

                        JString[] substrings = JString.splitStringButConcatenateBrackets(
                                JString.concatenateStrings(
                                        loopLeaf.getPayloadSubstrings(), ",", null, null),
                                ",", false);

                        int counter = 0;
                        for(JString s : substrings) {
                            boolean emptyObject = s.toJString().length() <= 0;

                            if(emptyObject){
                                continue;
                            }

                            if(s.equals(",")){
                                continue;
                            }

                            JObject subObject = new JObject();
                            String keyValue = Integer.toString(counter);

                            queSet1.add(subObject);
                            queSet2.add(s.toJString());

                            loopObject.getSubObjects().put(keyValue, subObject);
                            counter += 1;
                        }
                    }
                    else {
                        JPrimitive evalOutput = JObject.evaluate(loopLeafString);

                        loopObject.setTo(evalOutput);
                    }
                }
                else if (loopLeaf.hasBrackets() && loopLeaf.getOpeningBracket().equals("{") && loopLeaf.getClosingBracket().equals("}")) {

                    loopObject.typeName = ObjectTypes.Dictionary.toString();
                    loopObject.resetSubObjects();

                    JString[] substrings = JString.splitStringButConcatenateBrackets(
                            JString.concatenateStrings(loopLeaf.getPayloadSubstrings()),
                            new String[]{","}, false);

                    for(JString s : substrings){

                        if(JString.isNullOrEmptyString(s) || s.isBlankOrEmpty()){
                            continue;
                        }

                        JString dictionaryElementString = s;

                        NestedStringTree_Brackets.Leaf subLeaf = tree.getLeaf(s.trim());
                        if(subLeaf != null) {

                            dictionaryElementString = JString.concatenateStrings(subLeaf.getPayloadSubstrings());
                        }

                        JString[] subSubstrings = JString.splitStringButConcatenateBrackets(
                                dictionaryElementString,
                                ":", false);

                        if(subSubstrings.length >= 1)
                        {
                            JString keyString = subSubstrings[0].trim().shrinkWrap();

                            boolean continueKeyStringSearchLoop = true;
                            while(continueKeyStringSearchLoop){
                                NestedStringTree_Brackets.Leaf leaf = tree.getLeaf(keyString);
                                if(leaf == null){
                                    continueKeyStringSearchLoop = false;
                                }
                                else{
                                    keyString = JString.concatenateStrings(leaf.getPayloadSubstrings()).trim().shrinkWrap();
                                }
                            }

                            if(JString.doMatchedBracketsEncapsulateEntireString(keyString, "'", "'")){
                                keyString.removeEndCapBrackets("'", "'");
                            }
                            else if(JString.doMatchedBracketsEncapsulateEntireString(keyString, "\"", "\"")){
                                keyString.removeEndCapBrackets("\"", "\"");
                            }

                            JString valueString = new JString("Null");

                            //
                            boolean emptyObject = false;
                            if (subSubstrings.length >=2) {
                                valueString = subSubstrings[1];
                                if(valueString.length() < 1){
                                    emptyObject = true;
                                }
                            }
                            else{
                                emptyObject = true;
                            }

                            //
                            JObject subObject = new JObject();

                            if(!emptyObject){

                                queSet1.add(subObject);
                                queSet2.add(valueString);
                            }

                            loopObject.getSubObjects().put(keyString.toString(), subObject);
                        }
                    }
                }
                else if (loopLeaf.hasBrackets() && loopLeaf.getOpeningBracket().equals("\"") && loopLeaf.getClosingBracket().equals("\"")) {

                    JString primitiveValueString = tree.rebuildString(loopLeaf);

                    if (primitiveValueString.startsWith("\"") && primitiveValueString.endsWith("\"")) {
                        primitiveValueString = primitiveValueString.subString(1, primitiveValueString.length()-1);
                    }

                    loopObject.typeName = ObjectTypes.String.toString();
                    loopObject.value = primitiveValueString;
                }
                else if (loopLeaf.hasBrackets() && loopLeaf.getOpeningBracket().equals("'") && loopLeaf.getClosingBracket().equals("'")) {

                    JString primitiveValueString = tree.rebuildString(loopLeaf);

                    if (primitiveValueString.startsWith("'") && primitiveValueString.endsWith("'")) {
                        primitiveValueString = primitiveValueString.subString(1, primitiveValueString.length()-1);
                    }

                    loopObject.typeName = ObjectTypes.String.toString();
                    loopObject.value = primitiveValueString;
                }
                else {
                    throw new RuntimeException("loop leaf is not null, but this is an un-coded path : 2");
                }
            }
            else{
                // can't find a loop leaf from the given loop string
                if(MiscFunctions.doesStringContainAnyInstance(loopString, operators)) {

                    JString[] splitLoopString = loopString.splitString(operators, true);

                    JString[] tempList =  new JString[splitLoopString.length];
                    for(int x = 0; x < splitLoopString.length; x += 1){
                        NestedStringTree_Brackets.Leaf leaf = tree.getLeaf(splitLoopString[x]);

                        if(leaf != null){
                            tempList[x] = tree.rebuildString(leaf);
                        }
                        else{
                            tempList[x] = splitLoopString[x];
                        }
                    }

                    JString concatString = JString.concatenateStrings(tempList);

                    JPrimitive evalOutput = JObject.evaluate(concatString);

                    loopString = evalOutput.toJString();
                }

                JPrimitive parsedString = new JPrimitive(loopString);

                loopObject.typeName = parsedString.getTypeName();
                loopObject.value = parsedString.getClonedValue();

            } // end of if(loopLeaf != null) else block
        } // end of while loop

        // endregion
        // end of while loop region

        // region removes the className sub object and uses its value as the object's classname if possible
        {
            queSet1.clear();
            queSet1.add(this);
            while (queSet1.size() > 0) {
                JObject loopObject = queSet1.get(0);
                queSet1.remove(0);
                if(loopObject.isPrimitive()){
                    continue;
                }
                JObject paramObj = loopObject.getValue(ParameterName_ClassName);

                if (paramObj != null) {
                    if (paramObj.isString()) {

                        loopObject.setTypeName(paramObj.toString(false));

                        // removes the className from the sub objects
                        loopObject.getSubObjects().remove(ParameterName_ClassName);
                    }
                }

                // adds loop objects sub elements to que
                if (!loopObject.isPrimitive()) {
                    for (JObject subObject : loopObject.getValues()) {
                        if (!subObject.isPrimitive()) {
                            queSet1.add(subObject);
                        }
                    }
                }
            }
        }
        // endregion

        // region performs a series of checks to ensure JSON objects are properly converted to JObjects
        {
            Predicate<JObject> mayBeCollapsable = jObject -> jObject.getType().isDictionary() && jObject.getKeys().length == 1;
            JObjectExecutionPredicate<JObject> collapseJObject = input -> {
                String subKey = input.getKeys()[0];
                switch (subKey) {
                    case JSON_BOOLEAN_VALUE_KEY:
                    case JSON_INTEGER_VALUE_KEY:
                    case JSON_LONG_VALUE_KEY:
                    case JSON_DOUBLE_VALUE_KEY:
                    case JSON_STRING_VALUE_KEY:
                    case JSON_LIST_VALUE_KEY:
                        return input.getValue(subKey);
                    case JSON_SET_VALUE_KEY:
                        JObject newObject = input.getValue(subKey);
                        if (newObject.getType().isList()) {
                            newObject = newObject.convertListToSet();
                        }
                        return newObject;
                }
                return null;
            };
            queSet1.clear();
            queSet1.add(this);
            while (queSet1.size() > 0) {
                JObject loopObject = queSet1.get(0);
                queSet1.remove(0);

                if (loopObject.isPrimitive()) {
                    continue;
                }

                if (mayBeCollapsable.test(loopObject)) {
                    JObject newLoopObject = collapseJObject.execute(loopObject);
                    if (newLoopObject != null) {
                        loopObject.setTo(newLoopObject);
                    }
                }

                if(!loopObject.isPrimitive()) {
                    for (Map.Entry<String, JObject> entry : loopObject.getSubObjects().entrySet()) {
                        JObject subObject = entry.getValue();
                        if (subObject != null) {
                            queSet1.add(subObject);
                        }
                    }
                }
            }
        }
        // endregion

        return this;
    }

    /**
     * Parses a string into a JObject. Attempts to "resolve" any operators found outside the bounds of quotation marks.
     * @param inputString String to be resolved and converted into a JObject.
     * @return Returns a reference to this instance after it has been modified.
     * @throws UnrecognizedSymbolsException Throws this exception if any unrecognized symbols are detected outside the
     * bounds of quotation marks.
     */
    public JObject fromString(CharSequence inputString) throws UnrecognizedSymbolsException{

        return fromString_prv(JString.toJString(inputString));
    }

    // endregion

    // region toJString functions

    @Override
    public JString toJString(){
        if(isString()) {
            return (JString) this.value;
        }
        else if(isNull()){
            return new JString();
        }
        else{
            return new JString(toString());
        }
    }

    // endregion

    // region to string functions

    /**
     * Converts a JObject into a String.
     * @return Returns the JObject in string form.
     */
    @Override
    public String toString() {

        return this.toString(false);
    }

    /**
     * Converts a string JObject into a string with/without quotation marks. If the JObject is not a String, then this
     * function just executes a normal toString() operation.
     * @param includeQuotationMarks Boolean to determine weather the output has quotation marks.
     * @return String JObject in string form, with or without quotation marks.
     */
    public String toString(Boolean includeQuotationMarks) {

        if(isNull()) {
            return typeName;
        }

        if(isString()) {
            return toJString().toString(includeQuotationMarks);
        }

        JString output = new JString();

        if (isPrimitive()){
            output.append(value.toString());
            return output.toString();
        }
        else if(isOperator()){
            output.append((JString) value).toString(false);
            return output.toString();
        }
        else {
            boolean firstElement = true;
            if (!(getType().isDictionary() || getType().isList() || getType().isSet())) {

                output.append(JString.getQuotedJString(ParameterName_ClassName) + " : ").append("\"").append(typeName).append("\"");
                firstElement = false;
            }

            if (size() > 0) {
                if(getType().isList() || getType().isSet()){
                    HashMap<String, JObject> hm = getSubObjects();
                    for(int x = 0; x < size(); x += 1){
                        if (!firstElement) {
                            output.append(", ");
                        }

                        JObject parameterValue = hm.get(String.valueOf(x));

                        if (parameterValue != null) {
                            output.append(parameterValue.toString(true));
                        } else {
                            output.append("Null");
                        }

                        if (firstElement) {
                            firstElement = false;
                        }
                    }
                }
                else {
                    for (Map.Entry<String, JObject> entry : getSubObjects().entrySet()) {
                        if (!firstElement) {
                            output.append(", ");
                        }

                        JObject parameterValue = entry.getValue();

                        if (parameterValue != null) {
                            output.append(parameterValue.toString(true));
                        } else {
                            output.append("Null");
                        }

                        if (firstElement) {
                            firstElement = false;
                        }
                    }
                }
            }

            if (getType().isList()) {
                output.prepend("[").append("]");
            } else if (getType().isSet()) {
                output.prepend("(").append(")");
            } else if (getType().isDictionary()) {
                output.prepend("{").append("}");
            } else if (!isPrimitive()) {
                output.prepend("{").append("}");
            }
        }

        return output.toString();
    }

    // endregion

    // region toIndentedString()

    public String toIndentedString(){
        return toIndentedString(0, false);
    }

    public String toIndentedString(Boolean includeQuotationMarks){
        return toIndentedString(0, includeQuotationMarks);
    }

    public String toIndentedString(int startingIndentation){
        return toIndentedString(startingIndentation, false);
    }

    public String toIndentedString(int startingIndentation, Boolean includeQuotationMarks){
        StringBuilder s = new StringBuilder();

        StringBuilder tabString = getTabString(startingIndentation);

        if(getType().isNull()){
            s.append("Null");
            return s.toString();
        }

        if(getType().isString()){
            s.append(toJString().toString(includeQuotationMarks));
            return s.toString();
        }

        if(isSymbol()){
            s.append(
                ((JString) value).toString(false)
            );
            return s.toString();
        }
        else if (isPrimitive()){
            s.append(value.toString());
            return s.toString();
        }

        String[] bracketStrings = {"{","}"};

        if(getType().isSet()){
            bracketStrings = new String[]{"(", ")"};
        }
        else if(getType().isList()){
            bracketStrings = new String[]{"[", "]"};
        }

        s.append(bracketStrings[0]);
        tabString = getTabString(++startingIndentation);

        // displays the type name if it is not a default type
        if(getType().isUnknown()) {
            s.append("\n").append(tabString).append(JString.getQuotedJString(ParameterName_ClassName) + " : ");
            if (typeName != null) {
                s.append(JString.getQuotedJString(typeName));
            } else {
                s.append("Null");
            }
        }

        // displays the object's contents
        if(size() > 0){
            boolean firstElement = true;

            if(getType().isList() || getType().isSet()){
                HashMap<String, JObject> hm = getSubObjects();
                for(int x = 0; x < size(); x += 1){
                    if(!firstElement){
                        s.append(",");
                    }

                    JObject val = hm.get(String.valueOf(x));

                    if(val != null){
                        if(val.isPrimitive()){
                            s.append("\n").append(tabString);
                            s.append(val.toIndentedString(0));
                        }
                        else {
                            s.append("\n").append(tabString);
                            s.append(val.toIndentedString(startingIndentation));
                        }
                    }
                    else {
                        s.append("Null");
                    }

                    firstElement = false;
                }
            }
            else{
                for(Map.Entry<String, JObject> entry : getSubObjects().entrySet()){
                    if(!firstElement){
                        s.append(",");
                    }

                    s.append("\n").append(tabString).append(JString.getQuotedJString(entry.getKey())).append(" : ");

                    JObject val = entry.getValue();

                    if(val != null){
                        if(val.isPrimitive()){
                            s.append(val.toIndentedString(0));
                        }
                        else {
                            s.append("\n").append(tabString);
                            s.append(val.toIndentedString(startingIndentation));
                        }
                    }
                    else {
                        s.append("Null");
                    }

                    firstElement = false;
                }
            }
        }

        tabString = MiscFunctions.getTabString(--startingIndentation);
        s.append("\n").append(tabString).append(bracketStrings[1]);

        return s.toString();
    }

    // endregion

    // region debug string

    /**
     * Recursively creates a verbose descriptive string of this object and all sub objects, explaining in detail
     * what data this object is storing. The string contains indentations for readability.
     * @return String : returns descriptive string representation of this object.
     */
    public String toDebugString(){
        return toDebugString(0);
    }

    /**
     * Recursively creates a descriptive string of this object and all sub objects, explaining in detail
     * what data this object is storing. The string contains indentations for readability.
     * @return String : returns descriptive string representation of this object.
     */
    public String toShortDebugString(){
        return toShortDebugString(0);
    }

    /**
     * Recursively creates a verbose descriptive string of this object and all sub objects, explaining in detail
     * what data this object is storing. The string contains indentations for readability.
     * @param tabCount int : used to modify the default starting indentation.
     * @return String : returns descriptive string representation of this object.
     */
    public String toDebugString(int tabCount){

        StringBuilder s = new StringBuilder();

        StringBuilder tabString = getTabString(tabCount);

        if(tabCount != 0){
            s.append("\n").append(tabString);
        }

        s.append("{\n");

        tabString = getTabString(++tabCount);
        s.append(tabString);

        s.append(JString.getQuotedJString(ParameterName_ClassName) + " : ");
        if(typeName != null){
            s.append(JString.getQuotedJString(typeName));
        }
        else {
            s.append("Null");
        }

        s.append(", ");
        s.append("\n");
        s.append(tabString);
        s.append(JString.getQuotedJString(ParameterName_value)+" : ");
        if(isPrimitive() || isNull()){
            if(value != null){
                if(isString()){
                    s.append(JString.getQuotedJString(value));
                }
                else {
                    s.append(value.toString());
                }
            }
            else{
                s.append("Null");
            }
        }
        else{
            s.append("\n").append(tabString).append("{");
            tabString = getTabString(++tabCount);
            s.append("\n");
            if(size() > 0){
                boolean first = true;
                for(Map.Entry<String, JObject> entry : getSubObjects().entrySet()){
                    if(!first){
                        s.append(",\n");
                    }
                    s.append(tabString);
                    s.append(JString.getQuotedJString(entry.getKey())).append(" : ");
                    JObject val = entry.getValue();
                    if(val != null){
                        s.append(val.toDebugString(tabCount));
                    }
                    else{
                        s.append("Null");
                    }
                    first = false;
                }
            }

            s.append("\n");
            tabString = MiscFunctions.getTabString(--tabCount);
            s.append(tabString);
            s.append("}");
        }

        tabString = MiscFunctions.getTabString(--tabCount);
        s.append("\n").append(tabString).append("}");

        return s.toString();
    }

    /**
     * Recursively creates a descriptive string of this object and all sub objects, explaining in detail
     * what data this object is storing. The string contains indentations for readability.
     * @return String : returns descriptive string representation of this object.
     */
    public String toShortDebugString(int tabCount){
        StringBuilder s = new StringBuilder();

        StringBuilder tabString = getTabString(tabCount);

        if(tabCount != 0){
            s.append("\n").append(tabString);
        }

        s.append("{\n");

        tabString = getTabString(++tabCount);
        s.append(tabString);

        s.append(JString.getQuotedJString(ParameterName_ClassName) + " : ");
        if(typeName != null){
            s.append(JString.getQuotedJString(typeName));
        }
        else {
            s.append("Null");
        }

        s.append(", ");
        s.append("\n");
        s.append(tabString);
        if(isNull()){
            s.append("Null");
        }
        else {
            s.append("\"Value\" : ");

            if (isPrimitive()) {
                if (value instanceof JString str) {
                    s.append(str.toString(true));
                } else if (value instanceof Number num) {
                    s.append(num);
                } else if (value instanceof Boolean bool) {
                    s.append(bool);
                }
            } else {
                s.append("\n").append(tabString).append("{");
                tabString = getTabString(++tabCount);
                s.append("\n");
                if (size() > 0) {
                    boolean first = true;
                    for (Map.Entry<String, JObject> entry : getSubObjects().entrySet()) {
                        if (!first) {
                            s.append(",\n");
                        }
                        s.append(tabString);
                        s.append(JString.getQuotedJString(entry.getKey())).append(" : ");
                        JObject val = entry.getValue();
                        if (val != null) {
                            if (val.isPrimitive()) {
                                s.append(val.toString(true));
                            } else {
                                s.append(val.toShortDebugString(tabCount));
                            }
                        } else {
                            s.append("Null");
                        }
                        first = false;
                    }
                }

                s.append("\n");
                tabString = MiscFunctions.getTabString(--tabCount);
                s.append(tabString);
                s.append("}");
            }
        }

        tabString = MiscFunctions.getTabString(--tabCount);
        s.append("\n").append(tabString).append("}");

        return s.toString();
    }

    // endregion
}
