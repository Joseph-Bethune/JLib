package jLibrary;

import jLibrary.interfaces.JObjectBaseInterface;
import jLibrary.interfaces.StringInterface;
import jLibrary.miscFunctions.MiscFunctions;
import jLibrary.typeEnumerable.ObjectTypes;

import java.util.*;
import java.util.function.Consumer;

import static jLibrary.miscFunctions.MiscFunctions.print;

/**
 * This class partially mimics and adds on to the functionality of the standard string, but it does not extend from it.
 */
public class JString implements JObjectBaseInterface,
        java.io.Serializable, Comparable, CharSequence, Iterable<Character>, StringInterface {

    @java.io.Serial
    private static final long serialVersionUID = -6849794470775330710L;

    private char[] chars;
    private int length, capacity;

    // region static conversion functions

    public static JString convertToJString(Object input){
        if(input instanceof JString var){
            return var;
        }
        else if(input instanceof CharSequence var){
            return new JString(var);
        }
        else{
            return new JString(input.toString());
        }
    }

    // endregion

    // region constructors

    public JString(){
        clear();
    }

    public JString(int capacity){
        if(capacity > 10){
            this.capacity = capacity;
            length = 0;
            chars = new char[capacity];
        }
        else {
            clear();
        }
    }

    public JString(char initialValue){
        clear();
        append(initialValue);
    }

    public JString(char... initialValue){
        setTo(initialValue);
    }

    public JString(Object initialValue){
        setTo(initialValue);
    }

    // endregion

    // region set to functions

    public JString setTo(Object value){
        if(value instanceof char[] input){
            return setTo(input);
        }
        else if(value instanceof JString input){
            length = input.length;
            capacity = input.capacity;
            chars = new char[capacity];
            System.arraycopy(input.chars, 0, chars, 0, input.chars.length);
            return this;
        }
        else if(value instanceof Character input){
            return setTo(new char[]{input});
        }
        else if(value instanceof CharSequence input){
            return setTo(toCharArray(input));
        }
        else if(value == null){
            clear();
            return this;
        }
        else if(value instanceof JPrimitive iv){
            return setTo(iv.toJString());
        }
        else if(value instanceof StringInterface iv){
            return setTo(iv.toJString().toCharArray());
        }
        else{
            return setTo(valueOf(value));
        }
    }

    public JString setTo(char value){
        return setTo(new char[]{value});
    }

    public JString setTo(char... value){
        if(value == null || value.length == 0){
            clear();
        }
        else{

            length = value.length;
            capacity = length;
            chars = new char[capacity];
            System.arraycopy(value, 0, chars, 0, value.length);
        }
        return this;
    }

    public JString setTo(boolean value){
        return setTo(valueOf(value));
    }

    public JString setTo(byte value) {
        return setTo(valueOf(value));
    }

    public JString setTo(short value){
        return setTo(valueOf(value));
    }

    public JString setTo(long value){
        return setTo(valueOf(value));
    }

    public JString setTo(int value){
        return setTo(valueOf(value));
    }

    public JString setTo(float value){
        return setTo(valueOf(value));
    }

    public JString setTo(double value){
        return setTo(valueOf(value));
    }

    // endregion

    // region basic class functions

    public void clear(){
        length = 0;
        capacity = 10;
        chars = new char[capacity];
    }

    public JString clone(){
        JString output = new JString();
        output.length = length;
        output.capacity = capacity;
        output.chars = new char[capacity];
        System.arraycopy(chars, 0, output.chars, 0, chars.length);
        return output;
    }

    // endregion

    // region type functions

    @Override
    public ObjectTypes getType() {
        return ObjectTypes.String;
    }

    // endregion

    // region base value function

    @Override
    public Object getBaseValue() {
        return this;
    }

    // endregion

    // region operation overrides

    public JString add(Object obj2){
        if(obj2 == null){
            return clone().append("null");
        }

        return clone().append(obj2.toString());
    }

    // endregion

    // region static value of

    public static JString valueOf(Object value){
        if(value instanceof JString v){
            return v;
        }
        else if(value instanceof char[] v){
            JString output = new JString();
            output.capacity = v.length;
            output.length = v.length;
            output.chars = new char[v.length];
            System.arraycopy(v, 0, output.chars, 0, output.length);
            return output;
        }
        else if(value instanceof Character v){
            JString output = new JString();
            output.length = 1;
            output.capacity = 1;
            output.chars = new char[]{v};
            return output;
        }
        else if(value instanceof CharSequence v){
            return valueOf(toCharArray(v));
        }
        else if(value instanceof Number v){
            return valueOf(v.toString().toCharArray());
        }
        else if(value == null){
            return null;
        }
        else{
            return valueOf(value.toString().toCharArray());
        }
    }

    public static JString valueOf(char value){
        return valueOf(new char[]{value});
    }

    public static JString valueOf(boolean value){
        return valueOf(String.valueOf(value));
    }

    public static JString valueOf(byte value){
        return valueOf(String.valueOf(value));
    }

    public static JString valueOf(short value){
        return valueOf(String.valueOf(value));
    }

    public static JString valueOf(int value){
        return valueOf(String.valueOf(value));
    }

    public static JString valueOf(long value){
        return valueOf(String.valueOf(value));
    }

    public static JString valueOf(float value){
        return valueOf(String.valueOf(value));
    }

    public static JString valueOf(double value){
        return valueOf(String.valueOf(value));
    }

    // endregion

    // region empty string functions

    public static <t>boolean isNullOrEmptyString(t var){
        if(var == null){
            return true;
        }
        if(var instanceof JString str){
            return str.length() < 1;
        }
        else if(var instanceof String str){
            return str.length() < 1;
        }
        else if(var instanceof char[] str){
            return str.length < 1;
        }
        else{
            return isNullOrEmptyString(toCharArray(var));
        }
    }

    public static boolean isNullOrEmptyStringArray(JString[] var){
        if(var == null || var.length < 1){
            return true;
        }

        for(JString sub : var){
            if(!isNullOrEmptyString(sub)){
                return false;
            }
        }

        return true;
    }

    public static JString[] removeNullOrEmptyStrings(JString[] var, boolean alsoRemoveBlankStrings){
        ArrayList<JString> output_1 = new ArrayList<>();
        for(JString sub : var){
            if(!isNullOrEmptyString(sub)){
                if(alsoRemoveBlankStrings){
                    if(!sub.isBlank()){
                        output_1.add(sub);
                    }
                }
                else {
                    output_1.add(sub);
                }
            }
        }

        return output_1.toArray(new JString[0]);
    }

    public static JString[] removeNullOrEmptyStrings(JString[] var){
        return removeNullOrEmptyStrings(var, false);
    }

    /**
     * Returns true if this string only contains white space.
     */
    @Override
    public boolean isBlank(){
        return toString().isBlank();
    }

    /**
     * Returns true if the string is empty or only contains white spaces.
     */
    public boolean isBlankOrEmpty(){
        if(isEmpty()){
            return true;
        }

        return isBlank();
    }

    // endregion

    // region to character array functions

    public static <t>char[] toCharArray(t str) throws NullPointerException {
        return MiscFunctions.toCharArray(str);
    }

    public char[] toCharArray(){
        char[] output = new char[length];

        System.arraycopy(chars, 0, output, 0, output.length);

        return output;
    }

    // endregion

    // region toJString() methods

    /**
     * Converts an instance into a JString. If it is already a JString, this function recasts and returns
     * the unaltered instance.
     * @param str The object to be converted/recast.
     * @return Returns a recast of the original object or a new JString created from it.
     */
    public static JString toJString(Object str){
        if(str instanceof JString var){
            return var;
        }
        else{
            return new JString(str);
        }
    }

    public JString toJString(){
        return this;
    }

    // endregion

    // region to string functions

    public String toString(){
        return String.valueOf(toCharArray());
    }

    public String toString(boolean includeQuotationMarks){
        if(includeQuotationMarks){
            if(!hasQuotes()) {
                return "\"" + toString() + "\"";
            }
        }
        else{
            if(hasQuotes()){
                return JString.removeEndCapBrackets(toString(), "\"", "\"").toString();
            }
        }
        return toString();
    }

    // endregion

    // region to JString array functions

    public static <t>JString[] toJStringArray(t[] input){
        if(input == null){
            return null;
        }
        JString[] output = new JString[input.length];
        for(int x = 0; x < input.length; x+= 1){
            output[x] = new JString(input[x]);
         }
        return output;
    }

    public static <t>JString[][] toJStringArray(t[][] input){
        if(input == null){
            return null;
        }
        JString[][] output = new JString[input.length][];
        for(int x = 0; x < input.length; x+= 1){
            output[x] = toJStringArray(input[x]);
        }
        return output;
    }

    // endregion

    // region equals

    public boolean equalsIgnoreCase(char input){
        return equalsIgnoreCase(new JString(input));
    }

    public boolean equals(Object input){
        if(input == null){
            return false;
        }

        if(input == this){
            return true;
        }

        if(input instanceof JString string){
            if(string.length() != length()){
                return false;
            }

            for(int x = 0; x < length; x += 1){
                if(string.charAt(x) != charAt(x)){
                    return false;
                }
            }

            return true;
        }
        else if(input instanceof CharSequence string){
            return this.equals(new JString(string));
        }
        else if(input instanceof Character string){
            return this.equals(new JString(string));
        }
        else{
            return super.equals(input);
        }
    }

    public boolean equalsIgnoreCase(Object input){
        if(input == null){
            return false;
        }

        if(input == this){
            return true;
        }

        if(input instanceof JString s){
            JString temp = this.clone().toLowerCase();
            return temp.equals(s.toLowerCase());
        }
        else if(input instanceof CharSequence s){
            return equalsIgnoreCase(new JString(s));
        }
        else if(input instanceof Character s){
            return equalsIgnoreCase(new JString(s));
        }
        else{
            return equals(input);
        }
    }

    public boolean equals(Object input, boolean ignoreCase){
        if(ignoreCase){
            return equalsIgnoreCase(input);
        }
        else{
            return equals(input);
        }
    }

    public static boolean areJStringArraysEqual(JString[] set1, JString[] set2, boolean ignoreCase){
        if(set1 == null && set2 != null){
            return false;
        }

        if(set1 != null && set2 == null){
            return false;
        }

        if(set1.length != set2.length){
            return false;
        }

        for(int x= 0; x < set1.length; x += 1){
            if(ignoreCase){
                if(!set1[x].equals(set2[x], true)){
                    return false;
                }
            }
            else{
                if(!set1[x].equals(set2[x])){
                    return false;
                }
            }
        }
        return true;
    }

    // endregion

    // region hash code

    /**
     * Generates a hash code for the JString
     * @return int : returns a hash code for the JString
     */
    @Override
    public int hashCode() {

        return java.util.Arrays.hashCode(chars);
    }

    // endregion

    // region index bounds check

    public boolean isIndexInBounds(int index){

        if(index < 0){
            return false;
        }
        else {
            return index < length;
        }
    }

    private void indexBoundsCheck(String variableName, int index){
        if(index < 0){
            throw getLessThenZeroError(variableName);
        }
        else if(index >= length){
            throw getExceedsLengthError(variableName, index, length, this);
        }
    }

    private static IndexOutOfBoundsException getLessThenZeroError(String variableName){
        return new IndexOutOfBoundsException(variableName + " is less then zero.");
    }

    private static IndexOutOfBoundsException getExceedsLengthError(String variableName, int index, int length, JString src){
        StringBuilder temp = new StringBuilder(variableName);
        temp.append(" ").append(index).append(" exceeds JString's maximum index of ").append(length);
        temp.append(" in string : \"").append(src).append("\"");
        throw new IndexOutOfBoundsException(temp.toString());
    }

    // endregion

    // region sub sequence

    public char[] getSubSequence_asCharArray(int start, int end){

        indexBoundsCheck("start", start);
        if(end < 0){
            throw getLessThenZeroError("end");
        }
        else if(end > length+1){
            throw getExceedsLengthError("end", end, length, this);
        }

        if(start > end){
            throw new IndexOutOfBoundsException("Start index ("+start+") cannot exceed end ("+end+") index.");
        }

        if (end == start) {
            return new char[0];
        }

        int length = end-start;
        char[] output = new char[length];
        System.arraycopy(chars, start, output, 0, length);
        return output;
    }

    public JString getSubSequence_asJString(int start, int end){
        return new JString(getSubSequence_asCharArray(start, end));
    }

    // endregion

    // region CharSequence implementation

    @Override
    public int length() {
        return length;
    }

    @Override
    public char charAt(int index) {

        indexBoundsCheck("index", index);
        return chars[index];
    }

    @Override
    public boolean isEmpty() {
        return length == 0;
    }

    @Override
    public CharSequence subSequence(int start, int end) {

        return getSubSequence_asJString(start, end);
    }

    // endregion

    // region comparable implementation

    @Override
    public int compareTo(Object i) {

        if(i == null){
            return 1;
        }
        else if(i instanceof JString o) {
            if (o == this) {
                return 0;
            }

            if (length() < o.length()) {
                return -1;
            } else if (o.length() == this.length()) {
                if (this.equals(o)) {
                    return 0;
                }

                for (int x = 0; x < length; x += 1) {
                    if (charAt(x) != o.charAt(x)) {
                        Character c1 = charAt(x);
                        Character c2 = o.charAt(x);
                        return c1.compareTo(c2);
                    }
                }

                return 0;
            } else {
                return 1;
            }
        }
        else if(i instanceof String c){
            return this.compareTo(new JString(c));
        }
        else if(i instanceof char[] c){
            return this.compareTo(new JString(c));
        }
        else if(i instanceof CharSequence c){
            return this.compareTo(new JString(c));
        }
        else if(i instanceof Character c){
            return this.compareTo(new JString(c));
        }
        else{
            return 1;
        }
    }

    // endregion

    // region iterable implementation

    @Override
    public Iterator<Character> iterator() {
        return new Iterator<>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < length;
            }

            @Override
            public Character next() {
                return charAt(currentIndex++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public void forEach(Consumer<? super Character> action) {
        Objects.requireNonNull(action);
        for (int i = 0; i < this.length; i++)
            action.accept(charAt(i));
    }

    @Override
    public Spliterator<Character> spliterator() {
        throw new UnsupportedOperationException();
    }

    // endregion

    // region extract string via brackets

    private static JString[] extractSubstringsViaBrackets_prv(
            JString source, JString openingBracket, JString closingBracket, boolean keepBracketsInOutput){

        int[][] indexes = source.getIndexesOfBracketSets(openingBracket, closingBracket);
        int[] openings = indexes[0];
        int[] closings = indexes[1];

        if(openings.length < 1){
            return new JString[0];
        }

        if(openings[0] == -1){
            return new JString[0];
        }

        ArrayList<JString> output = new ArrayList<>();

        for(int pair = 0; pair < openings.length; pair += 1){
            JString payload = source.subString(openings[pair]+openingBracket.length(), closings[pair]);

            if(keepBracketsInOutput){
                payload = openingBracket.clone().append(payload).append(closingBracket);
            }

            output.add(payload);
        }

        return output.toArray(new JString[0]);
    }

    public static JString[] extractSubstringsViaBrackets(
            CharSequence source, CharSequence openingBracket, CharSequence closingBracket, boolean keepBracketsInOutput){
        return extractSubstringsViaBrackets_prv(
                new JString(source),
                new JString(openingBracket),
                new JString(closingBracket),
                keepBracketsInOutput);
    }

    // endregion

    // region capacity

    public JString shrinkWrap(){

        if(length == capacity){
            return this;
        }

        char[] newChars = new char[length];

        System.arraycopy(chars, 0, newChars, 0, length);
        this.chars = newChars;
        capacity = length;

        return this;
    }

    public void expandToCapacity(int newCapacity){
        if(newCapacity > capacity){
            setCapacity(newCapacity);
        }
    }

    private void expandCapacity(){
        expandCapacity(10);
    }

    private void expandCapacity(int expansionAmount){

        setCapacity(capacity+expansionAmount);
    }

    private void setCapacity(int totalCapacity){
        capacity = totalCapacity;

        char[] newChars = new char[capacity];

        for(int x = 0; x < chars.length && x < newChars.length; x += 1){
            newChars[x] = chars[x];
        }
        this.chars = newChars;
    }

    public int capacity(){
        return capacity;
    }

    // endregion

    // region append

    public JString append(char c){

        if(length+1 >= capacity){
            expandCapacity();
        }

        this.chars[length] = c;
        length += 1;
        return this;
    }

    public JString append(boolean val){
        return append(valueOf(val));
    }

    public JString append(int val){
        return append(valueOf(val).toCharArray());
    }

    public JString append(short val){
        return append(valueOf(val).toCharArray());
    }

    public JString append(long val){
        return append(valueOf(val).toCharArray());
    }

    public JString append(float val){
        return append(valueOf(val).toCharArray());
    }

    public JString append(double val){
        return append(valueOf(val).toCharArray());
    }

    public <t> JString append(t value){
        if(value == null){
            return this;
        }
        else if(value instanceof char[] c){
            if(c.length < 1){
                return this;
            }
            if(length+c.length >= capacity){
                if(c.length >= 10) {
                    expandCapacity(c.length * 2);
                }
                else{
                    expandCapacity();
                }
            }

            for (char chars : c) {
                this.chars[length] = chars;
                length += 1;
            }

            return this;
        }
        else{
            return append(toCharArray(value));
        }
    }

    // endregion

    // region prepend

    public JString prepend(char c){
        return prepend(new char[]{c});
    }

    public JString prepend(boolean val){
        return prepend(valueOf(val).toCharArray());
    }

    public JString prepend(short val){
        return prepend(valueOf(val).toCharArray());
    }

    public JString prepend(long val){
        return prepend(valueOf(val).toCharArray());
    }

    public JString prepend(float val){
        return prepend(valueOf(val).toCharArray());
    }

    public JString prepend(double val){
        return prepend(valueOf(val).toCharArray());
    }

    public JString prepend(int val){
        return prepend(valueOf(val).toCharArray());
    }

    public <t> JString prepend(t value){
        if(value == null){
            return this;
        }
        else if(value instanceof char[] newValues){
            if(newValues.length < 1){
                return this;
            }
            int newCapacity = 0;
            int newMinCapacity = length+newValues.length;
            if(newMinCapacity >= capacity){
                if(newValues.length >= 10) {
                    newCapacity = capacity + (newValues.length*2);
                }
                else{
                    newCapacity = capacity + 10;
                }
            }
            else{
                newCapacity = newMinCapacity;
            }

            int newLength = 0;
            char[] newChars = new char[newCapacity];
            System.arraycopy(newValues, 0, newChars, 0, newValues.length);
            newLength += newValues.length;
            System.arraycopy(this.chars, 0, newChars, newLength, length);
            newLength += length;

            this.chars = newChars;
            this.length = newLength;
            this.capacity = newCapacity;

            return this;
        }
        else{
            return prepend(valueOf(value).toCharArray());
        }
    }

    // endregion

    // region to upper / to lower

    private static char toLowerCase(char c){
        return switch (c) {
            case 'A' -> 'a';
            case 'B' -> 'b';
            case 'C' -> 'c';
            case 'D' -> 'd';
            case 'E' -> 'e';
            case 'F' -> 'f';
            case 'G' -> 'g';
            case 'H' -> 'h';
            case 'I' -> 'i';
            case 'J' -> 'j';
            case 'K' -> 'k';
            case 'L' -> 'l';
            case 'M' -> 'm';
            case 'N' -> 'n';
            case 'O' -> 'o';
            case 'P' -> 'p';
            case 'Q' -> 'q';
            case 'R' -> 'r';
            case 'S' -> 's';
            case 'T' -> 't';
            case 'U' -> 'u';
            case 'V' -> 'v';
            case 'W' -> 'w';
            case 'X' -> 'x';
            case 'Y' -> 'y';
            case 'Z' -> 'z';
            default -> c;
        };
    }

    private static char toUpperCase(char c){
        return switch (c) {
            case 'a' -> 'A';
            case 'b' -> 'B';
            case 'c' -> 'C';
            case 'd' -> 'D';
            case 'e' -> 'E';
            case 'f' -> 'F';
            case 'g' -> 'G';
            case 'h' -> 'H';
            case 'i' -> 'I';
            case 'j' -> 'J';
            case 'k' -> 'K';
            case 'l' -> 'L';
            case 'm' -> 'M';
            case 'n' -> 'N';
            case 'o' -> 'O';
            case 'p' -> 'P';
            case 'q' -> 'Q';
            case 'r' -> 'R';
            case 's' -> 'S';
            case 't' -> 'T';
            case 'u' -> 'U';
            case 'v' -> 'V';
            case 'w' -> 'W';
            case 'x' -> 'X';
            case 'y' -> 'Y';
            case 'z' -> 'Z';
            default -> c;
        };
    }

    public JString toLowerCase(){
        for(int x = 0; x <chars.length; x += 1){
            chars[x] = toLowerCase(chars[x]);
        }

        return this;
    }

    public JString toUpperCase(){
        for(int x = 0; x <chars.length; x += 1){
            chars[x] = toUpperCase(chars[x]);
        }

        return this;
    }

    // endregion

    // region static index of

    public static int indexOf(char[] source, char searchValue, int startingIndex)
            throws IndexOutOfBoundsException{
        if(startingIndex < 0){
            throw getLessThenZeroError("startingIndex");
        }

        for(int x = startingIndex; x < source.length; x += 1){
            if(source[x] == searchValue){
                return x;
            }
        }

        return -1;
    }

    public static int indexOf(char[] source, char searchValue)
            throws IndexOutOfBoundsException{
        return indexOf(source, searchValue, 0);
    }

    public static int indexOf(char[] source, char[] searchValue, int startingIndex)
            throws IndexOutOfBoundsException, NullPointerException{
        if(startingIndex < 0){
            throw new IndexOutOfBoundsException("Starting index must be greater then or equal to zero.");
        }

        if(searchValue == null){
            throw new NullPointerException("search value is null.");
        }

        boolean done = false;
        int output = -1;
        while(!done){
            int indexOfFirstChar = indexOf(source, searchValue[0], startingIndex);

            if(indexOfFirstChar == -1){
                done = true;
                continue;
            }
            boolean match = true;
            int counter = indexOfFirstChar;
            for(char srch : searchValue){
                if(counter >= source.length){
                    match = false;
                    break;
                }

                char src = source[counter];
                if(src != srch){
                    match = false;
                    break;
                }
                else{
                    counter += 1;
                }
            }

            if(match){
                output = indexOfFirstChar;
                done = true;
            }
            else{
                startingIndex = counter;
            }
        }

        return output;
    }

    public static int indexOf(char[] source, char[] searchValue)
            throws NullPointerException, IndexOutOfBoundsException{
        return indexOf(source, searchValue, 0);
    }

    public static <t>int indexOf(t source, t searchValue, int startingIndex)
            throws NullPointerException, IndexOutOfBoundsException{

        char[] src = toCharArray(source);
        char[] search = toCharArray(searchValue);

        return indexOf(src, search, startingIndex);
    }

    public static <t>int indexOf(t source, t searchValue)
            throws NullPointerException, IndexOutOfBoundsException{
        return indexOf(source, searchValue, 0);
    }

    // endregion

    // region index of

    public int indexOf(char searchValue, int startingIndex)
            throws NullPointerException, IndexOutOfBoundsException{
        return indexOf(this, searchValue, startingIndex);
    }

    public int indexOf(char searchValue)
            throws NullPointerException, IndexOutOfBoundsException{
        return indexOf(searchValue, 0);
    }

    public <t>int indexOf(t searchValue, int startingIndex)
            throws NullPointerException, IndexOutOfBoundsException{
        return indexOf(this, searchValue, startingIndex);
    }

    public <t>int indexOf(t searchValue)
            throws NullPointerException, IndexOutOfBoundsException{
        return indexOf(searchValue, 0);
    }

    // endregion

    // region static index of each

    public static int[] indexOfEach(char[] source, char[] searchString, int startingPoint){

        ArrayList<Integer> indexes = new ArrayList<>();

        boolean stop = false;
        while(!stop){
            int index = indexOf(source, searchString, startingPoint);

            if(index == -1){
                stop = true;
            }
            else{
                indexes.add(index);
                startingPoint = index + searchString.length;
            }
        }

        if(indexes.size() > 0) {

            int[] output = new int[indexes.size()];
            for (int x = 0; x < output.length; x += 1) {
                output[x] = indexes.get(x);
            }

            return output;
        }
        else{
            return new int[0];
        }
    }

    public static int[] indexOfEach(char[] source, char[] searchString){
        return indexOfEach(source, searchString, 0);
    }

    public static <t>int[] indexOfEach(t source, t searchValue, int startingPoint){

        char[] src = toCharArray(source);

        char[] search = toCharArray(searchValue);

        return indexOfEach(src, search, startingPoint);
    }

    public static <t>int[] indexOfEach(t source, t searchString){
        return indexOfEach(source, searchString, 0);
    }

    // endregion

    // region index of each

    public <t>int[] indexOfEach(t searchString, int startingPoint){
        return indexOfEach(this, searchString, startingPoint);
    }

    public <t>int[] indexOfEach(t searchString){
        return indexOfEach(this, searchString, 0);
    }

    // endregion

    // region last index of

    public int lastIndexOf(char searchValue, int cutOffPoint){
        indexBoundsCheck("cutOffPoint", cutOffPoint);

        for(int x = cutOffPoint; x >= 0; x -= 1){
            if(charAt(x) == searchValue){
                return x;
            }
        }

        return -1;
    }

    public int lastIndexOf(char searchValue){
        return lastIndexOf(searchValue, length-1);
    }

    public <t>int lastIndexOf(t searchValue, int cutOffPoint){
        indexBoundsCheck("cutOffPoint", cutOffPoint);

        if(searchValue == null){
            throw new NullPointerException("search value is null.");
        }

        if(searchValue instanceof char[] val) {
            for (int x = cutOffPoint; x >= 0; x -= 1) {
                if (charAt(x) == val[0]) {

                    int counter = x + 1;
                    boolean found = true;
                    for (int y = 1; y < val.length; y += 1) {
                        if(counter >= this.length){
                            found = false;
                            break;
                        }
                        if (val[y] != charAt(counter)) {
                            found = false;
                            break;
                        }
                        counter += 1;
                    }
                    if (found) {
                        return x;
                    }
                }
            }
            return -1;
        }
        else{
            return lastIndexOf(toCharArray(searchValue), cutOffPoint);
        }
    }

    public <t>int lastIndexOf(t searchValue){
        return lastIndexOf(searchValue, length-1);
    }

    // endregion

    // region contains function

    public boolean contains(char searchValue, int startingIndex){
        return indexOf(searchValue, startingIndex) != -1;
    }

    public boolean contains(char searchValue){
        return contains(searchValue, 0);
    }

    public <t>boolean contains(t searchValue, int startingIndex){
        return indexOf(searchValue, startingIndex) != -1;
    }

    public <t>boolean contains(t searchValue){
        return contains(searchValue, 0);
    }

    public <t>boolean contains(t[] searchValues){
        for(t s : searchValues){
            if(contains(s)){
                return true;
            }
        }
        return false;
    }

    public static boolean doesStringArrayContain(JString[] searchSet, JString searchValue){
        for(JString ele : searchSet){
            if(ele.equals(searchValue)){
                return true;
            }
        }
        return false;
    }

    // endregion

    // region replace each occurrence in string

    public JString replaceEach(char oldChar, char newChar){
        for(int x = 0; x < length; x += 1){
            if(chars[x] == oldChar){
                chars[x] = newChar;
            }
        }

        return this;
    }

    public JString replaceEach(char[] oldSequence, char[] newSequence){

        if(indexOf(oldSequence) == -1){
            return this;
        }

        JString workingString = new JString();
        boolean done = false;
        int startPoint = 0;
        while(!done){
            int index = this.indexOf(oldSequence, startPoint);
            if(index == -1){
                done = true;
                if(startPoint < this.length) {
                    workingString.append(this.getSubSequence_asCharArray(startPoint, this.length));
                }
            }
            else{
                if(index != 0) {
                    char[] subSequence = this.getSubSequence_asCharArray(startPoint, index);
                    if (subSequence.length > 0) {
                        workingString.append(subSequence);
                    }
                }
                if(newSequence != null && newSequence.length > 0) {
                    workingString.append(newSequence);
                }
                startPoint = index+oldSequence.length;
            }
        }

        setTo(workingString);

        return this;
    }

    public <t>JString replaceEach(t oldSequence, t newSequence){
        char[] old = null;

        old = toCharArray(oldSequence);

        char[] nEw;

        try{
            nEw = toCharArray(newSequence);
        }
        catch (NullPointerException e){
            nEw = null;
        }

        return replaceEach(old, nEw);
    }

    // endregion

    // region replace first occurrence in string

    public JString replaceFirstOccurrence(char oldChar, char newChar){
        for(int x = 0; x < length; x += 1){
            if(chars[x] == oldChar){
                chars[x] = newChar;
                break;
            }
        }

        return this;
    }

    public JString replaceFirstOccurrence(char[] oldSequence, char[] newSequence){

        int index = indexOf(oldSequence);
        if(index == -1){
            return this;
        }

        JString workingString = new JString();
        if(index != 0) {
            char[] subSequence = this.getSubSequence_asCharArray(0, index);
            if (subSequence.length > 0) {
                workingString.append(subSequence);
            }
        }
        if(newSequence != null && newSequence.length > 0) {
            workingString.append(newSequence);
        }
        if(index+oldSequence.length < length) {
            char[] substringAfterReplacedString = this.getSubSequence_asCharArray(index + oldSequence.length, length);
            if (substringAfterReplacedString != null && substringAfterReplacedString.length > 0) {
                workingString.append(substringAfterReplacedString);
            }
        }

        setTo(workingString);

        return this;
    }

    public <t>JString replaceFirstOccurrence(t oldSequence, t newSequence){
        char[] old = null;

        old = toCharArray(oldSequence);

        char[] nEw;

        try{
            nEw = toCharArray(newSequence);
        }
        catch (NullPointerException e){
            nEw = null;
        }

        return replaceFirstOccurrence(old, nEw);
    }

    // endregion

    // region remove from string

    public JString remove(char[] sequenceToRemove){
        return this.replaceEach(sequenceToRemove, null);
    }

    public <t>JString remove(t sequenceToRemove){
        return this.replaceEach(sequenceToRemove, null);
    }

    public JString remove(int removalStartPoint, int removalEndPoint){

        if(removalStartPoint > removalEndPoint){
            throw new RuntimeException("The removalEndPoint must be greater or equal to then the removalStartPoint");
        }

        if(removalStartPoint == removalEndPoint){
            return this;
        }

        if(removalStartPoint < 0){
            throw getLessThenZeroError("removalStartPoint");
        }

        if(removalStartPoint > length){
            throw getExceedsLengthError("removalStartPoint", removalStartPoint, length, this);
        }

        char[] newChars = new char[capacity];


        if(removalStartPoint > 0) {
            System.arraycopy(chars, 0, newChars, 0, removalStartPoint);
        }

        int counter = removalStartPoint;
        for(int x = removalEndPoint; x < length && counter < newChars.length; x += 1){
            newChars[counter] = chars[x];
            counter+=1;
        }

        chars = newChars;
        length = counter;

        return this;
    }

    public JString remove_getRemovedSubstring(int removalStartPoint, int removalEndPoint){
        if(removalStartPoint > removalEndPoint){
            throw new RuntimeException("The removalEndPoint must be greater or equal to then the removalStartPoint");
        }

        if(removalStartPoint == removalEndPoint){
            return new JString("");
        }

        if(removalStartPoint < 0){
            throw getLessThenZeroError("removalStartPoint");
        }

        if(removalStartPoint > length){
            throw getExceedsLengthError("removalStartPoint", removalStartPoint, length, this);
        }

        JString output = subString(removalStartPoint, removalEndPoint);

        char[] newChars = new char[capacity];


        if(removalStartPoint > 0) {
            System.arraycopy(chars, 0, newChars, 0, removalStartPoint);
        }

        int counter = removalStartPoint;
        for(int x = removalEndPoint; x < length && counter < newChars.length; x += 1){
            newChars[counter] = chars[x];
            counter+=1;
        }

        chars = newChars;
        length = counter;

        return output;
    }

    // endregion

    // region trim

    public JString trim(){

        if(isBlank()){
            clear();
            return this;
        }

        char[] newChars = new char[capacity];
        int indexOfStart = 0;
        boolean startFound = false;
        int indexOfEnd = -1;
        boolean endFound = false;

        for(int x = 0; x < length && !startFound; x += 1){
            if(chars[x] != ' '){
                indexOfStart = x;
                startFound = true;
            }
        }

        if(!startFound){
            clear();
            return this;
        }

        for (int x = length - 1; x >= indexOfStart && !endFound; x -= 1) {
            if (chars[x] != ' ') {
                indexOfEnd = x;
                endFound = true;
            }
        }

        char[] subSequence = this.getSubSequence_asCharArray(indexOfStart, indexOfEnd+1);
        int newLength = 0;
        for (char c : subSequence) {
            newChars[newLength] = c;
            newLength += 1;
        }

        length = newLength;
        chars = newChars;

        return this;
    }

    // endregion

    // region starts with / ends with

    public <t>boolean startsWith(t openingString){
        if(openingString instanceof char[] s){
            if(s.length > length){
                return false;
            }

            for(int x = 0; x < s.length; x += 1){
                if(s[x] != chars[x]){
                    return false;
                }
            }
            return true;
        }
        else{
            return startsWith(toCharArray(openingString));
        }
    }

    public <t>boolean endsWith(t closingString){
        if(closingString instanceof char[] s){
            if(s.length > length){
                return false;
            }

            int counter = 0;
            for(int x = length-s.length; x < length; x += 1){
                if(s[counter] != chars[x]){
                    return false;
                }
                counter += 1;
            }
            return true;
        }
        else{
            return endsWith(toCharArray(closingString));
        }
    }

    // endregion

    // region has quotes

    public boolean hasQuotes(){
        return startsWith("\"") && endsWith("\"");
    }

    public static JString getQuotedJString(Object input){
        JString output = toJString(input);
        if(!output.hasQuotes()){
            output.prepend("\"").append("\"");
        }
        return output;
    }

    // endregion

    // region static string splitting

    private static JString[] splitString_prv(JString wholeString, JString[] splitStrings, boolean includeSplitStrings){
        if(isNullOrEmptyString(wholeString)){
            return new JString[0];
        }

        if(splitStrings == null || splitStrings.length == 0){
            return new JString[]{wholeString.clone()};
        }

        if(!wholeString.contains(splitStrings)) {
            return new JString[] {
                    wholeString.clone()
            };
        }

        // sorts split strings by element length, with longer elements first
        ArrayList<JString> output = new ArrayList<>();

        int longestLength = 0;
        for(JString s : splitStrings) {
            if (s != null) {
                if (s.length() > longestLength) {
                    longestLength = s.length();
                }
            }
        }

        for(int currentLength = longestLength; currentLength >= 1; currentLength -= 1) {
            for (JString s : splitStrings) {
                if(!isNullOrEmptyString(s)) {
                    if (s.length() >= currentLength) {
                        if(!output.contains(s)){
                            output.add(s);
                        }
                    }
                }
            }
        }

        splitStrings = output.toArray(new JString[0]);

        // actual splitting
        output.clear();
        JString workingString = wholeString.clone();
        boolean continueLoop = true;

        while(continueLoop){

            if(workingString.length() < 1) {
                continueLoop = false;
                continue;
            }

            if(!workingString.contains(splitStrings)){
                if(!workingString.isEmpty()) {
                    output.add(workingString);
                }
                continueLoop = false;
            }

            int splitAt = -1;
            int splitStringLength = -1;

            for (JString splitString : splitStrings) {
                int newSplitIndex = workingString.indexOf(splitString);
                if(newSplitIndex == -1){
                    continue;
                }

                if(splitAt == -1){
                    splitAt = newSplitIndex;
                    splitStringLength = splitString.length();
                    continue;
                }

                if (newSplitIndex < splitAt) {
                    splitAt = newSplitIndex;
                    splitStringLength = splitString.length();
                }
                else if(splitAt == newSplitIndex) {

                    if (splitString.length() > splitStringLength) {
                        //splitAt = newSplitIndex;
                        splitStringLength = splitString.length();
                    }
                }
            }

            if(splitAt != -1) {
                JString pre_splitString = workingString.subString(0, splitAt);
                JString splitString = workingString.subString(
                        splitAt, splitAt + splitStringLength);
                JString post_splitString = new JString();
                if(splitAt+ splitString.length < workingString.length()) {
                    post_splitString = workingString.subString(splitAt + splitString.length(), workingString.length());
                }

                if (pre_splitString.length() > 0) {

                    output.add(pre_splitString);
                }
                if(includeSplitStrings) {
                    output.add(splitString);
                }

                workingString = post_splitString.clone();
            }

        }

        return output.toArray(new JString[0]);
    }

    private static JString[] splitString_prv(JString wholeString, JString splitString, boolean includeSplitStrings){
        return splitString_prv(wholeString, new JString[]{splitString}, includeSplitStrings);
    }

    public static <t>JString[] splitString(t wholeString, t[] splitStrings, boolean includeSplitStrings){
        return splitString_prv(new JString(wholeString), toJStringArray(splitStrings), includeSplitStrings);
    }

    public static <t>JString[] splitString(t wholeString, t splitStrings, boolean includeSplitStrings){
        return splitString_prv(new JString(wholeString), new JString(splitStrings), includeSplitStrings);
    }

    // endregion

    // region splitting string

    public JString[] splitString(CharSequence[] splitStrings, boolean includeSplitStrings){
        return splitString(this, toJStringArray(splitStrings), includeSplitStrings);
    }

    public JString[] splitString(CharSequence splitString, boolean includeSplitStrings){
        return splitString(this, splitString, includeSplitStrings);
    }

    public static char[][] splitCharArrayAtIndex(char[] src, int index){
        if(index < 0){
            throw getLessThenZeroError("removalStartPoint");
        }

        if(index > src.length){
            throw getExceedsLengthError("index", index, src.length, new JString(src));
        }

        if(index == 0){
            return new char[][]{
                    new char[0],
                    src
            };
        }
        else if(index == src.length){
            return new char[][]{
                    src,
                    new char[0]
            };
        }
        else{
            char[] beforeIndex = new char[index];
            char[] afterIndex = new char[src.length-index];

            System.arraycopy(src, 0, beforeIndex, 0, beforeIndex.length);
            System.arraycopy(src, index, afterIndex, 0, afterIndex.length);

            return new char[][]{
                    beforeIndex,
                    afterIndex
            };
        }
    }

    public char[][] splitStringIntoCharArrays(int index){
        return splitCharArrayAtIndex(toCharArray(), index);
    }

    public JString[] splitString(int breakingPoint){
        char[][] splits = splitStringIntoCharArrays(breakingPoint);
        return new JString[]{
                new JString(splits[0]),
                new JString(splits[1])
        };
    }

    // endregion

    // region standard bracket pairs

    public static String[][] getStandardBracketPairs(){
        return new String[][]{
                {"\"", "\""},
                {"'", "'"},
                {"[","]"},
                {"{","}"},
                {"(",")"}
        };
    }

    public static String[][] getQuoteBracketPairs(){
        return new String[][]{
                {"\"", "\""},
                {"'", "'"}
        };
    }

    // endregion

    // region special static string concatenation

    public static JString[] concatenateByBracketPairs(JString[] input, JString[][] bracketSets){
        if(isNullOrEmptyStringArray(input)){
            return new JString[0];
        }

        {
            String errorString = "Each element in the bracket set must be a pair of non whitespace strings";
            for (JString[] pair : bracketSets) {
                if (pair.length != 2) {
                    throw new RuntimeException(errorString);
                }
                for (JString subElement : pair) {
                    if (isNullOrEmptyString(subElement) || subElement.isBlank()) {
                        throw new RuntimeException(errorString);
                    }
                }
            }
        }

        input = removeNullOrEmptyStrings(input);

        ArrayList<JString> outputList = new ArrayList<>();
        JString newOutputElement = null;
        int counter = 0;
        boolean lfm = false; // flag : looking for matching closing bracket
        JString triggeringString = null;
        JString stringToMatch = null;
        int nestingCounter = 0;
        while(counter < input.length){

            JString subString = input[counter];

            // region checks to see if the current string is an opening bracket and/or a closing bracket

            boolean isOpeningBracket = false;
            boolean isClosingBracket = false;
            JString openingBracket = null;
            JString closingBracket = null;
            boolean continueCheckingLoop = true;
            int subCounter = 0;
            while (subCounter < bracketSets.length && continueCheckingLoop){
                if(bracketSets[subCounter][0].equals(subString)){
                    continueCheckingLoop = false;
                    openingBracket = bracketSets[subCounter][0].clone();
                    closingBracket = bracketSets[subCounter][1].clone();
                    isOpeningBracket = true;
                }

                if(bracketSets[subCounter][1].equals(subString)){
                    continueCheckingLoop = false;
                    if(!isOpeningBracket) {
                        openingBracket = bracketSets[subCounter][0].clone();
                        closingBracket = bracketSets[subCounter][1].clone();
                    }
                    isClosingBracket = true;
                }

                subCounter += 1;
            }

            // endregion

            if(isOpeningBracket || isClosingBracket){
                // region ensures that the substring is exclusively a closing bracket or an opening bracket

                if(isOpeningBracket && isClosingBracket){
                    if(lfm){
                        if(stringToMatch.equals(closingBracket) && subString.equals(stringToMatch)) {
                            isOpeningBracket = false;
                        }
                        else{
                            isClosingBracket = false;
                        }
                    }
                    else{
                        isClosingBracket = false;
                    }
                }

                // endregion

                if(lfm){
                    newOutputElement.append(subString.clone());
                }

                if(isOpeningBracket){
                    if(lfm) {
                        if (subString.equals(triggeringString)) {
                            nestingCounter += 1;
                        }
                    }
                    else{
                        lfm = true;
                        triggeringString = subString.clone();
                        stringToMatch = closingBracket.clone();
                        nestingCounter = 0;
                        newOutputElement = new JString(subString.clone());
                    }
                }
                else if(isClosingBracket){
                    if(lfm){
                        if(subString.equals(stringToMatch)){
                            if(nestingCounter == 0){
                                outputList.add(newOutputElement.clone());
                                newOutputElement = null;
                                triggeringString = null;
                                stringToMatch = null;
                                lfm = false;
                            }
                            else {
                                nestingCounter -= 1;
                            }
                        }
                    }
                    else{
                        throw new RuntimeException("string set contains a dangling closing bracket " + subString);
                    }
                }
            }
            else{
                if(lfm){
                    if(subString.length() > 0) {
                        newOutputElement.append(subString.clone());
                    }
                }
                else{
                    outputList.add(subString.clone());
                }
            }

            counter += 1;
        }

        if(lfm){
            throw new RuntimeException("string set contains an unclosed opening bracket \"" + triggeringString + "\"");
        }

        if(!isNullOrEmptyString(newOutputElement)){
            outputList.add(newOutputElement);
            newOutputElement = null;
        }

        return outputList.toArray(new JString[0]);
    }

    public static JString[] concatenateByDots(JString[] inputStrings){
        ArrayList<JString> outputList = new ArrayList<>();

        int counter = 0;
        JString substring = new JString();
        boolean buildingSubstring = false;
        while(counter < inputStrings.length){
            JString ele = inputStrings[counter];
            JString nextEle = null;
            JString nextNextEle = null;
            if(inputStrings.length > counter + 1){
                nextEle = inputStrings[counter + 1];
            }

            if(inputStrings.length > counter + 2){
                nextNextEle = inputStrings[counter + 2];
            }

            if(ele != null && ele.equals(".")){
                if(!buildingSubstring){
                    buildingSubstring = true;
                    substring = ele.clone();
                }
                else{
                    substring.append(ele);
                }

                counter += 1;

                if(nextEle != null){
                    substring.append(nextEle);
                    counter += 1;

                    if(nextEle.equals("[")){
                        int bracketCounter = 0;
                        int stringCounter = 1;
                        boolean found = false;
                        boolean continueLoop = true;
                        while(!found && continueLoop){
                            if((counter + stringCounter) >= inputStrings.length){
                                continueLoop = false;
                                continue;
                            }
                            JString tempEle = inputStrings[counter + stringCounter];
                            if(tempEle == null || tempEle.isEmpty() || tempEle.isBlank()){
                                stringCounter += 1;
                                continue;
                            }

                            if(tempEle.equals("]")){
                                if(bracketCounter == 0){
                                    found = true;
                                    continue;
                                }
                                else{
                                    bracketCounter -= 1;
                                }
                            }
                            else if(tempEle.equals("[")){
                                bracketCounter += 1;
                            }

                            stringCounter += 1;
                        }

                        if(found) {
                            for (int x = 0; x <= stringCounter; x += 1) {
                                substring.append(inputStrings[counter]);
                                counter += 1;
                            }
                        }
                        else{
                            throw new RuntimeException("No closing bracket found");
                        }
                    }
                }
            }
            else if(nextEle != null && nextEle.equals(".")){
                if(!buildingSubstring) {
                    buildingSubstring = true;
                    substring = ele.clone();
                }
                else{
                    substring.append(ele);
                }

                substring.append(nextEle);

                if(nextNextEle != null){
                    substring.append(nextNextEle);
                    counter += 3;
                }
                else{
                    counter += 2;
                }
            }
            else{
                if(buildingSubstring){
                    buildingSubstring = false;
                    outputList.add(substring);
                    substring = null;
                }

                outputList.add(ele);
                counter += 1;
            }
        }

        if(buildingSubstring){
            outputList.add(substring);
            //substring = new JString();
            //buildingSubstring = false;
        }

        return outputList.toArray(new JString[0]);
    }

    public static <t>JString concatenateStrings(t[] strings,
                                             CharSequence separator,
                                             CharSequence openingBracket, CharSequence closingBracket){
        JString output = new JString();

        boolean firstItem = true;

        for(t s : strings) {

            if(!firstItem) {
                if(separator != null) {
                    if (separator.length() > 0) {
                        output.append(separator);
                    }
                }
            }

            if(openingBracket != null && closingBracket != null){
                output.append(openingBracket);
            }

            output.append(s);

            if(openingBracket != null && closingBracket != null){
                output.append(closingBracket);
            }

            if(firstItem)
            {
                firstItem = false;
            }
        }

        return output;
    }

    public static <t>JString concatenateStrings(t[] strings){
        return concatenateStrings(strings, null, null, null);
    }

    // endregion

    // region special static string splitting

    private static JString[] splitStringButConcatenateBrackets_prv(
            JString wholeString,
            JString[] breakingPoints,
            boolean includeSplits){
        if(!wholeString.contains(breakingPoints)){
            return new JString[]{wholeString};
        }

        ArrayList<JString> breakingPointsArr = new ArrayList<>(
                List.of(JString.toJStringArray(new String[]{"\"", "'", "[","]","{", "}", "(", ")"})));
        breakingPointsArr.addAll(List.of(breakingPoints));

        JString[] splits = wholeString.splitString(
                breakingPointsArr.toArray(new JString[0]), true);

        JString[][] bracketPairs = toJStringArray(getStandardBracketPairs());

        JString[] temp = removeNullOrEmptyStrings(
                concatenateByBracketPairs(splits, bracketPairs),
                true);
        ArrayList<JString> tempArr = new ArrayList<>();
        JString currentEle = new JString();
        for(JString ele : temp){
            boolean isBreakingPoint = false;
            for(int x = 0; x < breakingPoints.length && !isBreakingPoint; x += 1){
                JString bp = breakingPoints[x];
                if(ele.equals(bp)){
                    isBreakingPoint = true;
                }
            }
            if(isBreakingPoint){
                tempArr.add(currentEle);
                if(includeSplits){
                    tempArr.add(ele);
                }

                currentEle = new JString();
            }
            else{
                currentEle.append(ele);
            }
        }
        if(currentEle.length() > 0){
            tempArr.add(currentEle);
        }

        return tempArr.toArray(new JString[0]);
    }

    private static JString[] splitStringButConcatenateBrackets_prv(
            JString wholeString, JString breakingPoint,boolean includeSplits){
        return splitStringButConcatenateBrackets_prv(wholeString, new JString[]{breakingPoint}, includeSplits);
    }

    public static JString[] splitStringButConcatenateBrackets(
            CharSequence wholeString, CharSequence breakingPoint, boolean includeSplits){
        return splitStringButConcatenateBrackets_prv(
                JString.convertToJString(wholeString),
                JString.convertToJString(breakingPoint),
                includeSplits);
    }

    public static JString[] splitStringButConcatenateBrackets(
            CharSequence wholeString, CharSequence[] breakingPoints, boolean includeSplits){
        return splitStringButConcatenateBrackets_prv(
                JString.convertToJString(wholeString),
                JString.toJStringArray(breakingPoints),
                includeSplits);
    }

    private static JString[] splitStringOnSpacesButConcatenateBrackets_prv(JString wholeString){
        JString[] splits = wholeString.splitString(
                JString.toJStringArray(new String[]{" ", "\"", "'", "[","]","{", "}", "(", ")"}), true);

        JString[][] bracketPairs = toJStringArray(getStandardBracketPairs());

        JString[] temp = removeNullOrEmptyStrings(
                concatenateByBracketPairs(splits, bracketPairs),
                true);

        return temp;
    }

    /**
     * Attempts to convert the given value to a JString, then splits that string at every space,
     * but ignoring any spaces contained within a bracket set.
     * @param wholeString The object to be subdivided.
     * @return Returns a JString array containing all the substrings that were separated by non-bracketed spaces.
     * Does not include any empty or white space strings.
     */
    public static JString[] splitStringOnSpacesButConcatenateBrackets(Object wholeString){
        return splitStringOnSpacesButConcatenateBrackets_prv(new JString(wholeString));
    }

    private static JString[] splitString_ExtractNumbers_nestedStep_prv(JString wholeString){
        if(JString.isNullOrEmptyString(wholeString)) {
            return new JString[0];
        }

        String[] numberCharacters =
                new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        JString[] splits = wholeString.splitString(numberCharacters, true);

        Vector<JString> set1 = new Vector<>();
        JString sb = new JString();
        boolean buildingNumber = false;

        for(JString s : splits)
        {
            boolean stringIsNumber = s.contains(numberCharacters);

            if(!stringIsNumber) {
                JNumber num = new JNumber();
                if(num.tryParse(s)){
                    stringIsNumber = true;
                }
            }

            if(stringIsNumber) {
                if (!buildingNumber) {
                    sb = new JString();
                    buildingNumber = true;
                }
                sb.append(s);
            }
            else {
                if (buildingNumber) {
                    set1.add(sb);
                    buildingNumber = false;
                }

                set1.add(s);
            }
        }

        if(buildingNumber) {
            set1.add(sb);
        }

        JString[] output = set1.toArray(new JString[0]);
        return output;
    }

    private static JString[] splitString_ExtractNumbers_prv(JString[] srcStrings){
        ArrayList<JString> set1 = new ArrayList<>();
        for(JString ele : srcStrings){
            JString[] splits = splitString_ExtractNumbers_nestedStep_prv(ele);
            set1.addAll(List.of(splits));
        }

        srcStrings = set1.toArray(new JString[0]);
        set1 = new ArrayList<>();
        int counter = 0;
        while(counter < srcStrings.length){
            JPrimitive ele1 = new JPrimitive(srcStrings[counter]);
            JPrimitive ele2 = null;
            JPrimitive ele3 = null;
            JPrimitive ele4 = null;


            if(counter+1 < srcStrings.length){
                ele2 =  new JPrimitive(srcStrings[counter + 1]);
            }

            if(counter+2 < srcStrings.length){
                ele3 =  new JPrimitive(srcStrings[counter + 2]);
            }

            if(counter+3 < srcStrings.length){
                ele4 =  new JPrimitive(srcStrings[counter + 3]);
            }

            if(ele1.isNumber()){
                if(ele2 != null && ele2.equals(".")){
                    if(ele3 != null && ele3.isNumber()){
                        JString temp = ele1.toJString().append(".").append(ele3.toJString());
                        set1.add(temp);
                        counter += 3;
                        continue;
                    }
                }
            }

            if(ele1.equals(".")){
                if(ele2 != null && ele2.isNumber()){
                    JString temp = ele1.toJString().append(ele2.toJString());
                    set1.add(temp);
                    counter += 2;
                    continue;
                }
            }

            if(ele1.equals("-")){
                if(ele2 != null){
                    if(ele2.isNumber()){
                        if(ele3 != null && ele3.equals(".")){
                            if(ele4 != null && ele4.isNumber()){
                                JString temp = ele1.toJString().append(ele2.toJString());
                                temp.append(ele3.toJString()).append(ele4.toJString());
                                set1.add(temp);
                                counter += 4;
                                continue;
                            }
                        }
                    }
                    else if(ele2.equals(".")){
                        if(ele3 != null && ele3.isNumber()){
                            JString temp = ele1.toJString().append(ele2.toJString());
                            temp.append(ele3.toJString());
                            set1.add(temp);
                            counter += 3;
                            continue;
                        }
                    }
                }
            }


            set1.add(ele1.toJString());
            counter += 1;
        }

        return set1.toArray(new JString[0]);
    }

    /**
     * Splits the given string into a string array, using numbers as split points
     * @param input The string to be subdivided
     * @return Returns a JString array of the subdivided input, split around numbers
     */
    public static <t>JString[] splitString_ExtractNumbers(t[] input){
        return splitString_ExtractNumbers_prv(JString.toJStringArray(input));
    }

    // endregion

    // region static unrecognized symbol extraction

    private static JString[] getUnrecognizedSymbols(JString[] strings){
        ArrayList<JString> unrecognizedSymbols = new ArrayList<>();
        ArrayList<JString> nestedElementsQue = new ArrayList<>();
        for(JString ele : strings){
            if(ele.isBlankOrEmpty()){
                continue;
            }

            if(ele.startsWith("\"") && ele.endsWith("\"")){
                continue;
            }

            if(ele.startsWith("'") && ele.endsWith("'")){
                continue;
            }

            JPrimitive ps = new JPrimitive(ele);
            if(ps.isBoolean()){
                continue;
            }

            if(ps.isNumber()){
                continue;
            }

            if(ps.isSymbol()){
                continue;
            }

            if(!unrecognizedSymbols.contains(ele)){
                unrecognizedSymbols.add(ele);
                if(ele.contains("[")){
                    nestedElementsQue.add(ele);
                }
            }
        }

        while(nestedElementsQue.size() > 0){
            JString loopObject = nestedElementsQue.get(0);
            nestedElementsQue.remove(0);
            int[][] indexOfBracketSets = getIndexesOfBracketSets(loopObject, "[", "]");
            if(indexOfBracketSets[0][0] == -1){
                continue;
            }
            JString substring = loopObject.subString(indexOfBracketSets[0][0]+1,indexOfBracketSets[1][0]);
            unrecognizedSymbols.add(substring.clone());

            if(substring.contains("[")){
                nestedElementsQue.add(substring);
            }
        }

        unrecognizedSymbols.sort(new Comparator<JString>() {
            @Override
            public int compare(JString o1, JString o2) {
                if(o1.contains(o2)){
                    return 1;
                }
                else if(o2.contains(o1)){
                    return -1;
                }
                else{
                    return o1.compareTo(o2);
                }
            }
        });

        return unrecognizedSymbols.toArray(new JString[0]);
    }

    // endregion

    // region special string splitting

    public JString[] splitStringOnSpacesButConcatenateBrackets(){
        return splitStringOnSpacesButConcatenateBrackets_prv(this);
    }

    public JString[] splitString_ExtractNumbers(){
        return splitString_ExtractNumbers_prv(new JString[]{this});
    }

    public static JString[] splitStringForExpressionProcessing(JString input){
        JString[] allSymbols = JString.toJStringArray(MiscFunctions.getAllRecognizedSymbols());

        JString[] splits = input.splitString(allSymbols, true);
        splits = JString.splitString_ExtractNumbers(splits);
        splits = JString.concatenateByBracketPairs(splits, JString.toJStringArray(JString.getQuoteBracketPairs()));
        splits = JString.concatenateByDots(splits);
        splits = JString.removeNullOrEmptyStrings(splits, true);

        return splits;
    }

    /**
     * This method splits a string into component parts for expression processing. Operators, symbols, spaces, commas,
     * quoted strings, unrecognized symbols numbers will be separated into their own strings.
     * @param input The input string to undergo splitting.
     * @return Returns 2 JString arrays inside an array of arrays. The first array will contain
     * all the split strings (including unrecognized symbols). The second array will contain all the unrecognized
     * symbols, including any that are nested inside of others.
     */
    public static JString[][] splitStringForExpressionProcessing_includeUnrecognizedSymbols(JString input){
        JString[] allSymbols = JString.toJStringArray(MiscFunctions.getAllRecognizedSymbols());

        JString[] splits = splitStringForExpressionProcessing(input);

        JString[] unrecs = JString.getUnrecognizedSymbols(splits);

        return new JString[][]{
                splits, unrecs
        };
    }

    // endregion

    // region insert in string

    public JString insertIntoString(int insertionPoint, CharSequence newCharacters){
        char[][] chars = splitStringIntoCharArrays(insertionPoint);


        capacity = length + newCharacters.length() + 10;
        this.chars = new char[capacity];
        length = 0;

        append(chars[0]);
        append(newCharacters);
        append(chars[1]);

        return this;
    }

    // endregion

    // region static substring count

    public static int getSubstringCount(char[] source, char[] searchString, int startingPoint){

        return indexOfEach(source, searchString, startingPoint).length;
    }

    public static int getSubstringCount(char[] source, char[] searchString){
        return getSubstringCount(source, searchString, 0);
    }

    public static <t>int getSubstringCount(t source, t searchValue, int startingPoint){

        char[] src = toCharArray(source);

        char[] search = toCharArray(searchValue);

        return getSubstringCount(src, search, startingPoint);
    }

    public static <t>int getSubstringCount(t source, t searchString){
        return getSubstringCount(source, searchString, 0);
    }

    // endregion

    // region substring count

    public <t>int getSubstringCount(t searchString, int startingPoint){
        return getSubstringCount(this, searchString, startingPoint);
    }

    public <t>int getSubstringCount(t searchString){
        return getSubstringCount(this, searchString, 0);
    }

    // endregion

    // region extract substring

    public JString subString(int startIndex, int endIndex){
        return getSubSequence_asJString(startIndex, endIndex);
    }

    // endregion

    // region static get matching bracket pair indexes

    private static void getIndexesOfBracketSets_preChecks(char[] wholeString,
                                                          char[] openingBracket, char[] closingBracket){
        if(wholeString == null){
            throw new NullPointerException("The wholeString value is null.");
        }

        if(openingBracket == null){
            throw new NullPointerException("The openingBracket value is null.");
        }

        if(closingBracket == null){
            throw new NullPointerException("The closingBracket value is null.");
        }

        if(wholeString.length < 1){
            throw new RuntimeException("The wholeString value is empty.");
        }

        if (openingBracket.length < 1){
            throw new RuntimeException("The openingBracket value is empty.");
        }

        if(closingBracket.length < 1){
            throw new RuntimeException("The closingBracket value is empty.");
        }
    }

    private static int[][] getIndexesOfBracketSets_noPreChecks(char[] wholeString,
                                                               char[] openingBracket, char[] closingBracket){

        int[][] defaultValue = new int[][]{
                new int[]{-1},
                new int[]{-1}
        };
        boolean bracketsAreTheSame = MiscFunctions.stringsAreEqual(openingBracket, closingBracket, false);

        if(bracketsAreTheSame){
            int[] bracketLocations = indexOfEach(wholeString, openingBracket);
            if(bracketLocations.length < 1){
                return defaultValue;
            }

            if(((float)bracketLocations.length)%2 != 0){
                throw new RuntimeException("The number of brackets (" + bracketLocations.length + ") " +
                        "must be an even number");
            }

            int halfLength = (int)(((float)bracketLocations.length)/2);

            int[] openingLocations_matched = new int[halfLength];
            int[] closingLocations_matched = new int[halfLength];
            boolean flag = true;
            int openingCounter = 0;
            int closingCounter = 0;
            for(int location : bracketLocations){
                if(flag){
                    openingLocations_matched[openingCounter] = location;
                    openingCounter += 1;
                }
                else{
                    closingLocations_matched[closingCounter] = location;
                    closingCounter += 1;
                }
                flag = !flag;
            }

            return new int[][]{
                    openingLocations_matched,
                    closingLocations_matched
            };
        }
        else{

            int[] openingBracketLocations = indexOfEach(wholeString, openingBracket);
            if(openingBracketLocations.length < 1){
                return defaultValue;
            }

            int[] closingBracketLocations = indexOfEach(wholeString, closingBracket);

            if(closingBracketLocations.length < 1){
                return defaultValue;
            }

            if (openingBracketLocations.length != closingBracketLocations.length){
                throw new RuntimeException("Opening bracket count (" + openingBracketLocations.length + ") " +
                        "and closing bracket count (" + closingBracketLocations.length + ") must be the same. String : \""+new JString(wholeString)+"\"");
            }

            int[] openingBracketsLocations_orderedByMatch = new int[openingBracketLocations.length];
            int[] closingBracketsLocations_orderedByMatch = new int[closingBracketLocations.length];

            int assignmentCounter = 0;
            for (int matchingOpeningIndex : openingBracketLocations) {
                int matchingClosingIndex = -1;
                for(int targetClosingLocation : closingBracketLocations){
                    if(targetClosingLocation < matchingOpeningIndex){
                        continue;
                    }

                    int numberOfOpeningsCounted = 0;
                    int numberOfClosingsCounted = 0;

                    for (int x : openingBracketLocations) {
                        if (x > matchingOpeningIndex && x < targetClosingLocation) {
                            numberOfOpeningsCounted += 1;
                        }
                    }

                    for (int x : closingBracketLocations) {
                        if (x > matchingOpeningIndex && x < targetClosingLocation) {
                            numberOfClosingsCounted += 1;
                        }
                    }

                    if (numberOfOpeningsCounted == numberOfClosingsCounted) {
                        matchingClosingIndex = targetClosingLocation;
                        break;
                    }
                }

                if (matchingClosingIndex != -1) {
                    openingBracketsLocations_orderedByMatch[assignmentCounter] = matchingOpeningIndex;
                    closingBracketsLocations_orderedByMatch[assignmentCounter] = matchingClosingIndex;
                    assignmentCounter += 1;
                } else {
                    throw new RuntimeException("couldn't find a match");
                }
            }

            return new int[][]{
                    openingBracketsLocations_orderedByMatch,
                    closingBracketsLocations_orderedByMatch
            };
        }
    }

    private static int[][] getIndexesOfBracketSets_prv(char[] wholeString,
                                                      char[] openingBracket, char[] closingBracket){
        getIndexesOfBracketSets_preChecks(wholeString, openingBracket, closingBracket);

        return getIndexesOfBracketSets_noPreChecks(wholeString, openingBracket, closingBracket);
    }

    /**
     * Searches through a string and finds all the opening brackets and the matching closing brackets, returning their
     * indexes in nested integer arrays.
     * @param wholeString The root string to search through.
     * @param openingBracket The opening bracket to search for.
     * @param closingBracket The matching closing bracket to search for.
     * @return Returns 2 integer arrays nested inside an outer array of arrays. The first integer array will contain
     * the indexes of the opening brackets, the 2nd array will contain the indexes of the matching closing brackets.
     * Each index in the 2nd array will correspond to an index in the first array.
     */
    public static int[][] getIndexesOfBracketSets(CharSequence wholeString,
                                                  CharSequence openingBracket, CharSequence closingBracket){

        char[] ws = toCharArray(wholeString);

        char[] open = toCharArray(openingBracket);

        char[] close = toCharArray(closingBracket);

        return getIndexesOfBracketSets_prv(ws, open, close);
    }

    // endregion

    // region get matching bracket pair indexes

    /**
     * Searches through a string and finds all the opening brackets and the matching closing brackets, returning their
     * indexes in nested integer arrays.
     * @param openingBracket The opening bracket to search for.
     * @param closingBracket The matching closing bracket to search for.
     * @return Returns 2 integer arrays nested inside an outer array of arrays. The first integer array will contain
     * the indexes of the opening brackets, the 2nd array will contain the indexes of the matching closing brackets.
     * Each index in the 2nd array will correspond to an index in the first array.
     */
    public int[][] getIndexesOfBracketSets(CharSequence openingBracket,CharSequence closingBracket){
        return getIndexesOfBracketSets(this, openingBracket, closingBracket);
    }

    // endregion

    // region static get index of matching bracket

    public static int getIndexOfMatchingBracket(char[] wholeString,
                                                char[] openingBracket, char[] closingBracket, int bracketLocation){
        int[][] indexPairs = null;
        try{
            indexPairs = getIndexesOfBracketSets_prv(wholeString, openingBracket, closingBracket);
        }
        catch (Exception e){
            return -1;
        }

        for(int setIndex = 0; setIndex < indexPairs[0].length; setIndex += 1){
            int[] set = indexPairs[setIndex];
            int opposingSetIndex = 1;
            if(setIndex == 1){
                opposingSetIndex = 0;
            }
            int[] opposingSet = indexPairs[opposingSetIndex];
            for(int locationInSet = 0; locationInSet < set.length; locationInSet += 1){
                if(set[locationInSet] == bracketLocation){
                    return opposingSet[locationInSet];
                }
            }
        }

        return -1;
    }

    public static <t>int getIndexOfMatchingBracket(t wholeString,
                                                   t openingBracket, t closingBracket,
                                                   int bracketLocation){

        char[] ws = toCharArray(wholeString);

        char[] open = toCharArray(openingBracket);

        char[] close = toCharArray(closingBracket);

        return getIndexOfMatchingBracket(ws, open, close, bracketLocation);
    }

    // endregion

    // region get index of matching bracket

    public <t>int getIndexOfMatchingBracket(t openingBracket, t closingBracket, int bracketLocation){
        return getIndexOfMatchingBracket(this, openingBracket, closingBracket, bracketLocation);
    }

    // endregion

    // region static do brackets matched brackets encapsulate entire string

    private static boolean doMatchedBracketsEncapsulateEntireString_prv(char[] wholeString,
                                                                       char[] openingBracket, char[] closingBracket){
        int[][] bracketPairs = null;

        try {
            bracketPairs = getIndexesOfBracketSets_prv(wholeString, openingBracket, closingBracket);

        }
        catch (Exception e){
            return false;
        }

        int endSetIndex = -1;
        for(int x = 0; x < bracketPairs[0].length; x += 1){
            if(bracketPairs[0][x] == 0){
                endSetIndex = x;
            }
        }

        if(endSetIndex == -1){
            return false;
        }

        if(bracketPairs[1][endSetIndex] + closingBracket.length != wholeString.length){
            return false;
        }

        return true;
    }

    public static <t>boolean doMatchedBracketsEncapsulateEntireString(t wholeString,
                                                                      t openingBracket, t closingBracket){
        char[] ws = toCharArray(wholeString);
        char[] open = toCharArray(openingBracket);
        char[] close = toCharArray(closingBracket);

        return doMatchedBracketsEncapsulateEntireString_prv(ws, open,close);
    }

    // endregion

    // region do brackets matched brackets encapsulate entire string

    public <t>boolean doMatchedBracketsEncapsulateEntireString(t openingBracket, t closingBracket){
        return doMatchedBracketsEncapsulateEntireString(this, openingBracket, closingBracket);
    }

    // endregion

    // region static remove end cap brackets

    private static char[] removeEndCapBrackets_returnCharArray(char[] wholeString, char[] openingBracket, char[] closingBracket){
        if(doMatchedBracketsEncapsulateEntireString_prv(wholeString, openingBracket, closingBracket)){
            int startIndex = openingBracket.length;
            int indexIndex = wholeString.length- closingBracket.length;
            char[] output = new char[indexIndex-startIndex];

            System.arraycopy(wholeString, startIndex, output, 0, output.length);

            return output;
        }
        else{
            return wholeString;
        }
    }

    public static <t>JString removeEndCapBrackets(t wholeString, t openingBracket, t closingBracket){
        char[] open = toCharArray(openingBracket);
        char[] close = toCharArray(closingBracket);
        char[] ws = toCharArray(wholeString);
        return new JString(removeEndCapBrackets_returnCharArray(ws, open, close));
    }

    // endregion

    // region from string functions

    @Override
    public JString fromString(CharSequence inputString) {
        return setTo(inputString);
    }

    // endregion

    // region remove end cap brackets

    public <t>JString removeEndCapBrackets(t openingBracket, t closingBracket){
        char[] open = toCharArray(openingBracket);
        char[] close = toCharArray(closingBracket);
        char[] ws = toCharArray();
        setTo(removeEndCapBrackets_returnCharArray(ws, open, close));
        return this;
    }

    // endregion

    // region to debug string

    public String toDebugString(){
        StringBuilder output = new StringBuilder("{");
        output.append(getQuotedJString("Capacity")).append(" : ").append(capacity).append(", ");
        output.append(getQuotedJString("Length")).append(" : ").append(length).append(", ");
        output.append(getQuotedJString("Character Array Length")).append(" : ").append(chars.length).append(", ");
        output.append(getQuotedJString("Character Array")).append(" : ").append(chars);
        output.append("}");

        return output.toString();
    }

    // endregion
}
