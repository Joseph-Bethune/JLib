package jLibrary.miscFunctions;

import jLibrary.*;
import jLibrary.exceptions.FailedBooleanParseException;
import jLibrary.exceptions.FailedParseException;
import jLibrary.exceptions.WrongExecutionTypeException;
import jLibrary.exceptions.WrongInputTypeException;
import jLibrary.interfaces.JObjectBaseInterface;
import jLibrary.typeEnumerable.ObjectTypes;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This class stores miscellaneous static functions necessary for the library.
 * @author Joseph Bethune
 */
public final class MiscFunctions {

    // region debug mode

    static boolean debugMode = false;

    /**
     * Getter for the static debugMode boolean variable
     * @return boolean static debugMode variable
     */
    public static boolean isInDebugMode(){
        return debugMode;
    }

    /**
     * Setter for the static debugMode boolean variable
     * @param debug_mode the new value for the static debugMode boolean variable
     */
    public static void setDebugMode(boolean debug_mode){
        debugMode = debug_mode;
    }

    // endregion

    // region printing to console

    /**
     * Prints a blank line to the console
     */
    public static void print(){
        System.out.println();
    }

    /**
     *  Attempts to execute the toString() function on the given object
     *  <br>
     *  Prints the output to the console as a whole line
     * @param input the object to be converted to a string and printed to the console
     */
	public static <t> void print(t input){
        if(input == null){
            System.out.print("null");
        }
        else if(input instanceof String)
        {
            System.out.println(input);
        }
        else if(input instanceof JString var){
            System.out.println(var);
        }
        else if(input instanceof JObject var){
            System.out.println(var);
        }
        else if(input instanceof String[]){
            for(String s : (String[]) input){
                print(s);
            }
        }
        else if(input instanceof boolean[]){
            for(boolean s : (boolean[]) input){
                print(s);
            }
        }
        else if(input instanceof int[]){
            for(int s : (int[]) input){
                print(s);
            }
        }
        else if(input instanceof float[]){
            for(float s : (float[]) input){
                print(s);
            }
        }
        else if(input instanceof double[]){
            for(double s : (double[]) input){
                print(s);
            }
        }
        else if(input instanceof char[]){
            System.out.println((char[])input);
        }
        else if(input instanceof Object[]){
            for(Object s : (Object[]) input){
                print(s);
            }
        }
        else
        {
            try {
                System.out.println(input);
            }
            catch (Exception e){
                System.out.println("!!! couldn't print object of class "+input.getClass().getName()+" !!!");
                System.out.println(e);
            }
        }
    }

    // endregion

    // region Working with Strings and StringBuilders

    // region string constants

    public static final String Null = "Null";

    public static String[] getAllOperators(){
        return new String[]{"+", "-", "*", "/", "^", "||", "&&", "<", "<=", ">", ">=",
                "!=", "==", "!", "%"};
    }

    public static String[] getOpeningBrackets(){
        return new String[]{
                "'", "\"", "[", "{", "("
        };
    }

    public static String[] getClosingBrackets(){
        return new String[]{
                "'", "\"", "]", "}", ")"
        };
    }

    public static String[] getAllRecognizedSymbols(){
        return new String[]{" ",
                "\"", "'", "[","]","{", "}", "(", ")",
                "+", "-", "*", "/", "^", "||", "&&", "<", "<=", ">", ">=",
                "!=", "==", "!", "%", ".", ",", ":", Null};
    }

    // endregion

    // region simple string method

    public static boolean isSimpleString(JString inputString){

        if(inputString.doMatchedBracketsEncapsulateEntireString("\"", "\"")){
            return true;
        }

        String[] specialSymbols = getAllRecognizedSymbols();

        for(String symbol : specialSymbols){
            if(inputString.equals(symbol)){
                return true;
            }
        }

        if(inputString.contains(specialSymbols)){
            return false;
        }

        return true;
    }

    // endregion

    // region to character array

    public static <t>char[] toCharArray(char str) throws NullPointerException {
        return new char[]{str};
    }

    public static <t>char[] toCharArray(t str) throws NullPointerException {
        if(str == null){
            throw new NullPointerException("The input value is null");
        }

        if(str instanceof char[] val) {
            return val;
        } else if (str instanceof Character val) {
            return new char[]{val};
        } else if (str instanceof JString s) {
            return s.toCharArray();
        } else if (str instanceof String s) {
            return s.toCharArray();
        } else if (str instanceof CharSequence s) {
            char[] output = new char[s.length()];
            for (int x = 0; x < output.length; x += 1) {
                output[x] = s.charAt(x);
            }
            return output;
        } else if (str instanceof Number s) {
            return s.toString().toCharArray();
        } else if(str instanceof JPrimitive iv){
            return toCharArray(iv.toJString());
        }
        else {
            return JString.valueOf(str).toCharArray();
        }
    }

    // endregion

    // region to string array

    public static <t>String[] toStringArray(t input){
        if (input == null){
            return null;
        }

        if(input instanceof Character var){
            return new String[]{var.toString()};
        }
        else if(input instanceof CharSequence var){
            return new String[]{var.toString()};
        }
        else if(input instanceof char[] var){
            return toStringArray(new JString(var));
        }
        else if(input instanceof CharSequence[] var){
            if(var.length > 0){
                String[] output = new String[var.length];
                for(int x = 0; x < var.length; x+= 1){
                    output[x] = var[x].toString();
                }
                return output;
            }
            else{
                return new String[0];
            }
        }
        else if(input instanceof JPrimitive iv){
            return new String[]{iv.toString()};
        }
        else if(input instanceof JPrimitive[] var){
            if(var.length > 0){
                String[] output = new String[var.length];
                for(int x = 0; x < var.length; x+= 1){
                    output[x] = var[x].toString();
                }
                return output;
            }
            else{
                return new String[0];
            }
        }
        else if(input instanceof ObjectTypes var){
            return new String[]{var.toString()};
        }
        else if(input instanceof ObjectTypes[] var){
            if(var.length > 0){
                String[] output = new String[var.length];
                for(int x = 0; x < var.length; x+= 1){
                    output[x] = var[x].toString();
                }
                return output;
            }
            else{
                return new String[0];
            }
        }
        else if(input instanceof Collection<?> var){
            if(var.size() > 0){
                String[] output = new String[var.size()];
                Object[] elements = var.stream().toArray();
                int counter = 0;
                for(Object element : elements){
                    output[counter] = element.toString();
                    counter += 1;
                }

                return output;
            }
            else{
                return new String[0];
            }
        }
        else if(input instanceof Class<?>[] var){
            String[] output = new String[var.length];
            for(int x = 0; x < output.length; x+= 1){
                output[x] = var[x].getTypeName();
            }
            return output;
        }
        return null;
    }

    // endregion

    // region to string builder array

    public static <t>StringBuilder[] toStringBuilderArray(t input){

        if (input == null){
            return null;
        }

        if(input instanceof Character var){
            return new StringBuilder[]{new StringBuilder(var)};
        }
        else if(input instanceof CharSequence var){
            return new StringBuilder[]{new StringBuilder(var)};
        }
        else if(input instanceof char[] var){
            return toStringBuilderArray(new JString(var));
        }
        else if(input instanceof CharSequence[] var){
            if(var.length > 0){
                StringBuilder[] output = new StringBuilder[var.length];
                for(int x = 0; x < var.length; x+= 1){
                    output[x] = new StringBuilder(var[x]);
                }
                return output;
            }
            else{
                return new StringBuilder[0];
            }
        }
        return null;
    }

    // endregion

    // region Converting from char[]

    /**
     * Creates a new StringBuilder object from the given character array
     * @param input char[] supplying the source of the characters for the output
     * @return Returns a string builder object made of the input character array
     */
    public static StringBuilder fromCharArray(char[] input){
        StringBuilder output = new StringBuilder(input.length);

        output.append(input);

        return output;
    }

    public static StringBuilder[] fromCharArray(char[][] input){
        StringBuilder[] output = new StringBuilder[input.length];
        for(int x = 0; x < input.length; x += 1){
            output[x] = fromCharArray(input[x]);
        }

        return output;
    }

    // endregion

    // region substring

    /**
     * Extracts characters from the string builder and places them in a new string builder object.
     * @param str The string builder object to extract characters from
     * @param startIndex The index where extraction will begin in the str variable
     * @param endIndex The index where extraction will end in the str variable
     * @return Returns StringBuilder containing the extracted characters
     */
    public static StringBuilder stringBuilderSubstring(StringBuilder str, int startIndex, int endIndex){
        char[] src = stringBuilderSubstring_toCharArray(str, startIndex, endIndex);
        StringBuilder output = new StringBuilder(src.length);
        output.append(src);
        return output;
    }

    /**
     * Extracts characters from the string builder and places them in a character array.
     * @param str The string builder object to extract characters from
     * @param startIndex The index where extraction will begin in the str variable
     * @param endIndex The index where extraction will end in the str variable
     * @return Returns char[] containing the extracted characters
     */
    public static char[] stringBuilderSubstring_toCharArray(StringBuilder str, int startIndex, int endIndex){
        if(startIndex < 0){
            throw new IndexOutOfBoundsException("Start index must be greater then or equal to zero.");
        }

        if(endIndex > str.length()){
            throw new IndexOutOfBoundsException(
                    "The end index must be less then the length of the input string");
        }

        if(endIndex < startIndex){
            throw new IndexOutOfBoundsException(
                    "The start index must be less then the end index");
        }

        if(endIndex == startIndex){
            return new char[0];
        }
        char[] output = new char[endIndex-startIndex];

        str.getChars(startIndex, endIndex, output, 0);

        return output;
    }

    // endregion

    // region trim

    /**
     *  Removes leading and trailing white spaces from a StringBuilder object's contents
     *  <br>
     * @param str The StringBuilder to be modified
     * @return Returns the modified StringBuilder object
     */
    public static StringBuilder trim(StringBuilder str){

        char[] chars = JString.toCharArray(str);
        int startIndex = -1;
        int endIndex = -1;
        int counter = 0;
        boolean startIndexFound = false;
        boolean endIndexFound = false;

        while (!(startIndexFound && endIndexFound) && counter < chars.length){

            if(!startIndexFound){
                if(chars[counter] != ' '){
                    startIndex = counter;
                    startIndexFound = true;
                }
            }

            if(!endIndexFound){
                int index = chars.length-(counter+1);
                if(chars[index] != ' '){
                    endIndex = index+1;
                    endIndexFound = true;
                }
            }

            counter += 1;
        }

        // print("start index : " + startIndex + ", end index : " + endIndex);

        if(startIndex >= endIndex){
            return new StringBuilder();
        }

        return new StringBuilder(str.subSequence(startIndex, endIndex));
    }

    // endregion

    // region tab string

    /**
     *  Generates a StringBuilder object containing a series of tab spaces
     *  <br>
     * @param tabCount int : number of tab spaces to be placed into the output
     * @return Returns StringBuilder containing tab spaces
     */
    public static StringBuilder getTabString(int tabCount){
        StringBuilder output = new StringBuilder();
        for(int x = 0; x < tabCount; x += 1){
            output.append("\t");
        }
        return output;
    }

    // endregion

    // region empty string check

    /**
     *  Determines if the given string is empty or null
     * @param str the string in question
     * @return true if the string is empty or null, otherwise false
     */
    public static boolean isEmptyString(CharSequence str){
        if (str == null) {
            return true;
        }

        return new JString(str).trim().isEmpty();
    }

    // endregion

    // region char array equal functions

    public static char[] getLowerCaseLetters(){
        return new char[]{
                'a','b','c','d','e','f','g','h','i','j','k',
                'l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'
        };
    }

    public static char[] getUpperCaseLetters(){
        return new char[]{
                'A','B','C','D','E','F','G','H','I','J','K',
                'L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
        };
    }

    /**
     * Determines if the given char primitives match
     * @param c1 first char
     * @param c2 second char
     * @param ignoreCase flag controlling weather or not case is a factor
     * @return Returns true if the 2 char values match, otherwise: false
     */
    public static boolean charEquals(char c1, char c2, boolean ignoreCase){
        if(ignoreCase){
            return c1 == c2;
        }

        if(c1 == c2){
            return true;
        }

        char[] lc_letters = getLowerCaseLetters();

        char[] uc_letters = getUpperCaseLetters();

        int c1_index = -1;
        int c2_index = -1;
        int tempIndex = -1;

        // looks for first character in lower case character array
        tempIndex = indexOf(lc_letters, c1);
        if(tempIndex != -1){
            c1_index = tempIndex;
        }

        // looks for second character in lower case character array
        tempIndex = indexOf(lc_letters, c2);
        if(tempIndex != -1){
            c2_index = tempIndex;
        }

        // looks for first character in upper case character array
        if(c1_index == -1) {
            tempIndex = indexOf(uc_letters, c1);
            if(tempIndex != -1){
                c1_index = tempIndex;
            }
        }

        // looks for second character in upper case character array
        if(c2_index == -1) {
            tempIndex = indexOf(uc_letters, c2);
            if(tempIndex != -1){
                c2_index = tempIndex;
            }
        }

        return c1_index == c2_index && c1_index != -1;
    }

    /**
     * Determines if the given char[] arrays match
     * @param charSet1 first char array
     * @param charSet2 second char array
     * @param ignoreCase flag controlling weather or not case is a factor
     * @return Returns true if the 2 char[] values match, otherwise: false
     */
    private static boolean charArrayEquals_prv(char[] charSet1, char[] charSet2, boolean ignoreCase){
        if(charSet1 == charSet2){
            return true;
        }

        if(charSet1.length != charSet2.length){
            return false;
        }

        if(!ignoreCase){
            for(int x = 0; x < charSet1.length; x += 1){
                if(charSet1[x] != charSet2[x]){
                    return false;
                }
            }
            return true;
        }

        char[] lc_letters = getLowerCaseLetters();

        char[] uc_letters = getUpperCaseLetters();

        for(int counter = 0; counter < charSet1.length; counter += 1){
            char c1 = charSet1[counter];
            char c2 = charSet2[counter];

            if(c1==c2){
                continue;
            }
            else{
                int c1_index = -1;
                int c2_index = -1;
                int tempIndex = -1;

                // looks for first character in lower case character array
                tempIndex = indexOf(lc_letters, c1);
                if(tempIndex != -1){
                    c1_index = tempIndex;
                }

                // looks for second character in lower case character array
                tempIndex = indexOf(lc_letters, c2);
                if(tempIndex != -1){
                    c2_index = tempIndex;
                }

                // looks for first character in upper case character array
                if(c1_index == -1) {
                    tempIndex = indexOf(uc_letters, c1);
                    if(tempIndex != -1){
                        c1_index = tempIndex;
                    }
                }

                // looks for second character in upper case character array
                if(c2_index == -1) {
                    tempIndex = indexOf(uc_letters, c2);
                    if(tempIndex != -1){
                        c2_index = tempIndex;
                    }
                }

                if(c1_index == -1 ||
                        c2_index == -1 ||
                        c1_index != c2_index){
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Determines if the given char[][] arrays match
     * @param charSetSet1 first 2 dimensional char array
     * @param charSetSet2 second 2 dimensional char array
     * @param ignoreCase flag controlling weather or not case is a factor
     * @return Returns true if the 2 char[] values match, otherwise: false
     */
    private static boolean charArrayArrayEquals_prv(char[][] charSetSet1, char[][] charSetSet2, boolean ignoreCase){
        if(charSetSet1 == charSetSet2){
            return true;
        }

        if(charSetSet1.length != charSetSet2.length){
            return false;
        }

        if(!ignoreCase) {
            for (int charSetIndex = 0; charSetIndex < charSetSet1.length; charSetIndex += 1) {
                char[] charSet1 = charSetSet1[charSetIndex];
                char[] charSet2 = charSetSet2[charSetIndex];
                if(charSet1.length != charSet2.length){
                    return false;
                }
                for(int charIndex = 0; charIndex < charSet1.length; charIndex += 1){
                    if(charSet1[charIndex] != charSet2[charIndex]) {
                        return false;
                    }
                }
            }
            return true;
        }

        char[] lc_letters = getLowerCaseLetters();

        char[] uc_letters = getUpperCaseLetters();

        for (int charSetIndex = 0; charSetIndex < charSetSet1.length; charSetIndex += 1) {
            char[] charSet1 = charSetSet1[charSetIndex];
            char[] charSet2 = charSetSet2[charSetIndex];
            if(charSet1.length != charSet2.length){
                return false;
            }

            for(int charIndex = 0; charIndex < charSet1.length; charIndex += 1){
                char c1 = charSet1[charIndex];
                char c2 = charSet2[charIndex];

                if(c1!=c2){
                    int c1_index = -1;
                    int c2_index = -1;
                    int tempIndex = -1;

                    // looks for first character in lower case character array
                    tempIndex = indexOf(lc_letters, c1);
                    if(tempIndex != -1){
                        c1_index = tempIndex;
                    }

                    // looks for second character in lower case character array
                    tempIndex = indexOf(lc_letters, c2);
                    if(tempIndex != -1){
                        c2_index = tempIndex;
                    }

                    // looks for first character in upper case character array
                    if(c1_index == -1) {
                        tempIndex = indexOf(uc_letters, c1);
                        if(tempIndex != -1){
                            c1_index = tempIndex;
                        }
                    }

                    // looks for second character in upper case character array
                    if(c2_index == -1) {
                        tempIndex = indexOf(uc_letters, c2);
                        if(tempIndex != -1){
                            c2_index = tempIndex;
                        }
                    }

                    if(c1_index == -1 ||
                            c2_index == -1 ||
                            c1_index != c2_index){
                        return false;
                    }
                }
            }
        }

        return true;
    }

    // endregion

    // region equals functions

    /**
     * Determines if two "string like objects" are equal to each other.
     * @param string1 The first object.
     * @param string2 The second object.
     * @param ignoreCase The flag that determines if case will be ignored.
     * @return Returns true if both strings are a match, or else false.
     */
    public static <t1, t2>boolean stringsAreEqual(t1 string1, t2 string2, boolean ignoreCase){
        if(string1 == null && string2 != null){
            return false;
        }

        if(string1 != null && string2 == null){
            return false;
        }

        char[] cs1 = toCharArray(string1);
        char[] cs2 = toCharArray(string2);

        if(cs1.length != cs2.length){
            return false;
        }

        if(cs1.length == 0){
            return true;
        }

        return charArrayEquals_prv(cs1, cs2, ignoreCase);
    }

    /**
     * Determines if two "string like objects" are equal to each other.
     * Differences in lower and upper case characters will not be ignored.
     * @param string1 The first object.
     * @param string2 The second object.
     * @return Returns true if both strings are a match, or else false.
     */
    public static <t1, t2>boolean stringsAreEqual(t1 string1, t2 string2){
        return stringsAreEqual(string1, string2, false);
    }

    /**
     *  Determines if the given string arrays are equal
     * @param var1 the first string array
     * @param var2 the first string array
     * @return true if the string arrays are the same length, contain the same values and are in the same order
     */
    public static boolean stringArrayEquals(CharSequence[] var1, CharSequence[] var2){
        if((var1 == null) != (var2 == null)) {
            return false;
        }

        if(var1 == null){
            return true;
        }

        if(var1.length != var2.length){
            return false;
        }

        for(int x = 0; x < var1.length; x ++){
            if(!var1[x].toString().equals(var2[x].toString())){
                return false;
            }
        }

        return true;
    }

    // endregion

    // region search functions

    private static int linearSearch(char[] charSet, char searchValue, int startPoint){
        if(charSet.length < 1){
            return -1;
        }

        if(charSet.length == 1){
            if(charSet[0] == searchValue){
                return 0;
            }
            else{
                return -1;
            }
        }

        // short circuit, immediately checks if the start point is the answer
        if(charSet[startPoint] == searchValue){
            return startPoint;
        }

        for(int x = startPoint; x < charSet.length; x += 1){
            if (charSet[x] == searchValue) {
                return x;
            }
        }

        return -1;
    }

    static int bidirectionalLinearSearch(char[] charSet, char searchValue, int startPoint){
        if(charSet.length < 1){
            return -1;
        }

        if(charSet.length == 1){
            if(charSet[0] == searchValue){
                return 0;
            }
        }

        // short circuit, immediately checks if the start point is the answer
        if(charSet[startPoint] == searchValue){
            return startPoint;
        }

        int secondaryIndex = -1;
        for(int x = 0; x < charSet.length; x += 1){
            if(x >= startPoint) {
                if (charSet[x] == searchValue) {
                    return x;
                }
            }

            secondaryIndex = (charSet.length-1) - x;

            if(secondaryIndex >= startPoint){
                if(charSet[secondaryIndex] == searchValue){
                    return secondaryIndex;
                }
            }
        }

        return -1;
    }

    public static int indexOf(char[] charSet, char searchValue, int startPoint){

        return linearSearch(charSet, searchValue, startPoint);
    }

    public static int indexOf(char[] charSet, char searchValue){
        return indexOf(charSet, searchValue, 0);
    }

    public static int indexOf(char[] charSet, char[] searchValue, int startPoint){

        if(charSet.length < 1 || searchValue.length < 1){
            return -1;
        }

        if(searchValue.length == 1){
            return indexOf(charSet, searchValue[0], startPoint);
        }

        if(searchValue.length > charSet.length){
            return -1;
        }

        if(searchValue.length == charSet.length){
            boolean matchEliminated = false;
            boolean done = false;
            int counter = startPoint;
            while(!matchEliminated && !done){
                if(counter >= searchValue.length){
                    done = true;
                    continue;
                }
                if(counter >= charSet.length ||
                        charSet[counter] != searchValue[counter]){
                    matchEliminated = true;
                }
                else{
                    counter += 1;
                }
            }

            if(!matchEliminated){
                return 0;
            }
            return -1;
        }

        boolean found = false;

        while (!found){
            int pot_output = indexOf(charSet, searchValue[0], startPoint);

            if(pot_output == -1){
                return -1;
            }

            boolean matchEliminated = false;
            boolean done = false;
            int counter = 1;
            while(!matchEliminated && !done){
                if(counter >= searchValue.length){
                    done = true;
                    continue;
                }
                if(pot_output + counter >= charSet.length ||
                        charSet[pot_output + counter] != searchValue[counter]){

                    matchEliminated = true;
                }
                else{
                    counter += 1;
                }
            }

            if(!matchEliminated){
                return pot_output;
            }
            else{
                startPoint += 1;
            }
        }

        return -1;
    }

    public static int indexOf(char[] charSet, char[] searchValue){
        return indexOf(charSet, searchValue, 0);
    }

    public static <t,v>int indexOf(t wholeString, v searchValue, int startPoint){

        return indexOf(toCharArray(wholeString), toCharArray(searchValue), startPoint);
    }

    public static <t,v>int indexOf(t wholeString, v searchValue){

        if(wholeString instanceof CharSequence[] arr){
            int counter = 0;
            for(CharSequence ele : arr){
                if(JString.toJString(ele).equals(searchValue)){
                    return counter;
                }
                counter += 1;
            }
            return -1;
        }
        else {
            return indexOf(toCharArray(wholeString), toCharArray(searchValue), 0);
        }
    }

    static int indexOf_LinearSearch(char[] charSet, char[] searchValue, int startPoint){

        if(charSet.length < 1 || searchValue.length < 1){
            return -1;
        }

        if(searchValue.length == 1){
            return indexOf(charSet, searchValue[0], startPoint);
        }

        if(searchValue.length > charSet.length){
            return -1;
        }

        if(searchValue.length == charSet.length){
            boolean matchEliminated = false;
            boolean done = false;
            int counter = startPoint;
            while(!matchEliminated && !done){
                if(counter >= searchValue.length){
                    done = true;
                    continue;
                }
                if(counter >= charSet.length ||
                        charSet[counter] != searchValue[counter]){
                    matchEliminated = true;
                }
                else{
                    counter += 1;
                }
            }

            if(!matchEliminated){
                return 0;
            }
            return -1;
        }

        boolean found = false;

        while (!found){
            int pot_output = linearSearch(charSet, searchValue[0], startPoint);

            if(pot_output == -1){
                return -1;
            }

            boolean matchEliminated = false;
            boolean done = false;
            int counter = 1;
            while(!matchEliminated && !done){
                if(counter >= searchValue.length){
                    done = true;
                    continue;
                }
                if(pot_output + counter >= charSet.length ||
                        charSet[pot_output + counter] != searchValue[counter]){

                    matchEliminated = true;
                }
                else{
                    counter += 1;
                }
            }

            if(!matchEliminated){
                return pot_output;
            }
            else{
                startPoint += 1;
            }
        }

        return -1;
    }

    static int indexOf_LinearSearch(char[] charSet, char[] searchValue){
        return indexOf_LinearSearch(charSet, searchValue, 0);
    }

    static int indexOf_LinearSearch(StringBuilder wholeString, StringBuilder searchValue, int startPoint){
        return indexOf_LinearSearch(JString.toCharArray(wholeString), JString.toCharArray(searchValue), startPoint);
    }

    static int indexOf_LinearSearch(StringBuilder wholeString, StringBuilder searchValue){
        return indexOf_LinearSearch(wholeString, searchValue, 0);
    }

    // endregion

    // region String Concatenation

    /**
     *  Combines a string array into a single string builder object
     * @param strings the array of strings to be combined
     * @param separator the string that is placed in between each of the input strings in the output
     * @param openingBracket the string placed between each of the input strings in the output
     * @param closingBracket the string placed between each of the input strings in the output
     * @return single combined string builder object in the following format :<br> (openingBracket)(strings[0])(closingBracket)(separator)(openingBracket)(strings[1])(closingBracket)(separator)...
     */
    public static <t>StringBuilder concatenateStrings(
            t[] strings, String separator, String openingBracket, String closingBracket) {

        StringBuilder sb = new StringBuilder();

        boolean firstItem = true;

        for(t s : strings) {

            if(!firstItem) {
                if(separator != null) {
                    if (separator.length() > 0) {
                        sb.append(separator);
                    }
                }
            }

            if(openingBracket != null && closingBracket != null){
                sb.append(openingBracket);
            }

            sb.append(s);

            if(openingBracket != null && closingBracket != null){
                sb.append(closingBracket);
            }

            if(firstItem)
            {
                firstItem = false;
            }
        }

        return sb;
    }

    /**
     * Combines a string array into a single string
     * @param strings the array of strings to be combined
     * @param addCommaBetweenEachElement boolean controlling comma presence in the output
     * @param addQuotationMarksAroundEachElement boolean controlling quotation mark presence in the output
     * @return a single combined string with/without commas between each element and with/without quotation marks around each element
     */
    public static StringBuilder concatenateStrings(
            CharSequence[] strings, boolean addCommaBetweenEachElement,
            boolean addQuotationMarksAroundEachElement){

        String commaString = null;
        String openingBracketString = null;
        String closingBracketString = null;
        if(addCommaBetweenEachElement){
            commaString = ", ";
        }
        if (addQuotationMarksAroundEachElement) {
            openingBracketString = "\"";
            closingBracketString = "\"";
        }
        return concatenateStrings(strings, commaString, openingBracketString, closingBracketString);
    }

    /**
     * Combines a string array into a single string builder object
     */
    public static StringBuilder concatenateStrings(CharSequence[] strings){
        return concatenateStrings(strings, null, null, null);
    }

    // endregion

    // region Replace in String

    public static StringBuilder replaceFirstOccurrence(
            StringBuilder wholeString, StringBuilder substringToReplace,
            StringBuilder newValue, int startPoint){

        if(wholeString.isEmpty() || substringToReplace.isEmpty()){
            return wholeString;
        }

        int index = indexOf_LinearSearch(wholeString, substringToReplace, startPoint);

        if(index != -1){
            StringBuilder output = new StringBuilder();

            StringBuilder preSub = stringBuilderSubstring(wholeString, 0, index);
            StringBuilder postSub = stringBuilderSubstring(wholeString,
                    index+substringToReplace.length(), wholeString.length());

            output = output.append(preSub).append(newValue).append(postSub);
            return output;
        }

        return wholeString;
    }

    public static StringBuilder replaceFirstOccurrence(
            StringBuilder wholeString, StringBuilder substringToReplace,
            StringBuilder newValue){

        return replaceFirstOccurrence(wholeString, substringToReplace, newValue, 0);
    }

    public static StringBuilder replaceFirstOccurrence(
            StringBuilder wholeString, StringBuilder substringToReplace, String newValue){

        return replaceFirstOccurrence(
                wholeString,
                substringToReplace,
                new StringBuilder(newValue));
    }

    public static StringBuilder replaceFirstOccurrence(
            StringBuilder wholeString, StringBuilder substringToReplace, String newValue, int startPoint){

        return replaceFirstOccurrence(
                wholeString,
                substringToReplace,
                new StringBuilder(newValue),
                startPoint);
    }

    public static StringBuilder replaceFirstOccurrence(
            StringBuilder wholeString, String substringToReplace, String newValue, int startPoint){

        return replaceFirstOccurrence(
                wholeString,
                new StringBuilder(substringToReplace),
                new StringBuilder(newValue),
                startPoint);
    }

    public static StringBuilder replaceFirstOccurrence(
            StringBuilder wholeString, String substringToReplace, String newValue){

        return replaceFirstOccurrence(
                wholeString,
                new StringBuilder(substringToReplace),
                new StringBuilder(newValue),
                0);
    }

    public static StringBuilder replaceFirstOccurrence(
            String wholeString, String substringToReplace, String newValue, int startPoint){

        return replaceFirstOccurrence(
                new StringBuilder(wholeString),
                new StringBuilder(substringToReplace),
                new StringBuilder(newValue),
                startPoint);
    }

    public static StringBuilder replaceFirstOccurrence(
            String wholeString, String substringToReplace, String newValue){

        return replaceFirstOccurrence(
                new StringBuilder(wholeString),
                new StringBuilder(substringToReplace),
                new StringBuilder(newValue),
                0);
    }

    // endregion

    // region Substring Count

    public static int getSubstringCount(JString[] strings, CharSequence searchString){
        int output = 0;
        for(JString s : strings){
            if(s.equals(searchString)){
                output += 1;
            }
        }
        return output;
    }

    /**
     *  Counts the number of times the search string appears in a StringBuilder object
     * @param wholeString the StringBuilder to search through
     * @param searchString the string to search for
     * @return the number of times (as an integer) that the search string appears in the whole string
     */
    public static int getSubstringCount(StringBuilder wholeString, StringBuilder searchString){
        if (wholeString == null || searchString == null)
        {
            return 0;
        }

        int stringLength = wholeString.length();
        int substringLength = searchString.length();

        if (substringLength < 1){
            return 0;
        }

        if(stringLength < 1){
            return 0;
        }

        if (substringLength > stringLength){
            return 0;
        }

        int outputValue = 0;
        StringBuilder modString = new StringBuilder(wholeString);
        int index = indexOf(modString, searchString);

        while(index != -1){
            modString = replaceFirstOccurrence(modString, searchString, "", index);
            outputValue += 1;
            index = indexOf(modString, searchString);
        }

        return outputValue;
    }

    /**
     *  Counts the number of times the search string appears in a string
     * @param wholeString the string to search through
     * @param searchString the string to search for
     * @return the number of times (as an integer) that the search string appears in the whole string
     */
    public static int getSubstringCount(String wholeString, String searchString){
        return getSubstringCount(new StringBuilder(wholeString), new StringBuilder(searchString));
    }

    /**
     *  Counts the number of times the search string appears in a StringBuilder object
     * @param wholeString the StringBuilder to search through
     * @param searchString the string to search for
     * @return the number of times (as an integer) that the search string appears in the whole string
     */
    public static int getSubstringCount(StringBuilder wholeString, String searchString){
        return getSubstringCount(wholeString, new StringBuilder(searchString));
    }

    // endregion

    // region Contains functions

    public static boolean doesStringContain(StringBuilder wholeString, StringBuilder substring){
        return indexOf(wholeString, substring) != -1;
    }

    // region contains any instance

    /**
     * Determines whether a string contains any instance of the given substrings
     * @param wholeString the string to search through
     * @param substrings the strings to search for
     * @return returns true if the whole string contains any element of the substrings, or else false
     */
    public static boolean doesStringContainAnyInstance(JString wholeString, CharSequence[] substrings){
        for (CharSequence substring : substrings) {
            if (wholeString.contains(substring)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines whether a string contains any instance of the given substrings
     * @param wholeString the string to search through
     * @param substrings the strings to search for
     * @return returns true if the whole string contains any element of the substrings, or else false
     */
    public static boolean doesStringContainAnyInstance(String wholeString, String[] substrings){
        for (String substring : substrings) {
            if (wholeString.contains(substring)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines whether a string contains any instance of the given substrings
     * @param wholeString the string to search through
     * @param substrings the strings to search for
     * @return returns true if the whole string contains any element of the substrings, or else false
     */
    public static boolean doesStringContainAnyInstance(StringBuilder wholeString, String[] substrings){
        for (String substring : substrings) {
            if (wholeString.indexOf(substring) != -1) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines whether a string contains any instance of the given substrings
     * @param wholeString the string to search through
     * @param substrings the strings to search for
     * @return returns true if the whole string contains any element of the substrings, or else false
     */
    public static boolean doesStringContainAnyInstance(StringBuilder wholeString, StringBuilder[] substrings){
        for (StringBuilder substring : substrings) {
            if (doesStringContain(wholeString, substring)) {
                return true;
            }
        }

        return false;
    }

    // endregion

    // region contains every instance

    /**
     * Determines whether a string contains every instance of the given substrings
     * @param wholeString the string to search through
     * @param substrings the strings to search for
     * @return returns true if the whole string contains every element of the substrings, or else false
     */
    public static boolean doesStringContainEveryInstance(String wholeString, String[] substrings){
        for (String substring : substrings) {
            if (!wholeString.contains(substring)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Determines whether a string contains every instance of the given substrings
     * @param wholeString the string to search through
     * @param substrings the strings to search for
     * @return returns true if the whole string contains every element of the substrings, or else false
     */
    public static boolean doesStringContainEveryInstance(StringBuilder wholeString, String[] substrings){
        for (String substring : substrings) {
            if (wholeString.indexOf(substring) == -1) {
                return false;
            }
        }

        return true;
    }

    /**
     * Determines whether a string contains every instance of the given substrings
     * @param wholeString the string to search through
     * @param substrings the strings to search for
     * @return returns true if the whole string contains every element of the substrings, or else false
     */
    public static boolean doesStringContainEveryInstance(StringBuilder wholeString, StringBuilder[] substrings){
        for (StringBuilder substring : substrings) {
            if (!doesStringContain(wholeString, substring)) {
                return false;
            }
        }

        return true;
    }

    // endregion

    // region checking a set

    /**
     * Determines whether a string array contains any instance of the given substring
     * @param stringSet the string set to search through
     * @param valueToSearchFor the string to search for
     * @return returns true if stringSet contains an instance of valueToSearchFor, or else false
     */
    public static boolean doesStringArrayContain(CharSequence[] stringSet, CharSequence valueToSearchFor) {

        JString vs = new JString(valueToSearchFor);
        for(CharSequence ele : stringSet) {

            JString s = new JString(ele);
            if(s.equals(vs)) {

                return true;
            }
        }
        return false;
    }

    // endregion

    // endregion

    // region StringBuilder Starts with and ends with functions

    public static boolean startsWith(StringBuilder wholeString, StringBuilder substring){

        if(wholeString.isEmpty() || substring.isEmpty()){
            return false;
        }

        char[] ws = JString.toCharArray(wholeString);
        char[] ss = JString.toCharArray(substring);

        if(ws.length < 1 || ws.length < ss.length){
            return false;
        }

        for(int x = 0; x < ss.length; x += 1){
            if(ws[x] != ss[x]){
                return false;
            }
        }

        return true;
    }

    public static boolean startsWith(StringBuilder wholeString, String substring){
        return startsWith(wholeString, new StringBuilder(substring));
    }

    public static boolean endsWith(StringBuilder wholeString, StringBuilder substring){
        if(wholeString.isEmpty() || substring.isEmpty()){
            return false;
        }

        char[] ws = JString.toCharArray(wholeString);
        char[] ss = JString.toCharArray(substring);

        if(ws.length < 1 || ws.length < ss.length){
            return false;
        }

        for(int x = 0; x < ss.length; x += 1){
            char ws_char = ws[((ws.length-1) - x)];
            char ss_char = ss[((ss.length-1) - x)];
            if(ws_char != ss_char){
                return false;
            }
        }

        return true;
    }

    public static boolean endsWith(StringBuilder wholeString, String substring){
        return endsWith(wholeString, new StringBuilder(substring));
    }

    // endregion

    // region index of matching brackets

    /**
     * Finds the index of the matching bracket in a given string
     * @param wholeString the given string to search through
     * @param openingBracket the opening bracket paired with the closing bracket
     * @param closingBracket the closing bracket paired with the opening bracket
     * @param bracketLocation the index of a(n) opening/closing bracket whose mate you are trying to find
     * @return the integer index of the other half of the opening-closing bracket pair
     */
    public static int getIndexOfMatchingBracket(StringBuilder wholeString,
                                                StringBuilder openingBracket, StringBuilder closingBracket,
                                                int bracketLocation){
        if (wholeString == null || openingBracket == null || closingBracket == null)
        {
            return -1;
        }

        int stringLength = wholeString.length();
        int openingBracketLength = openingBracket.length();
        int closingBracketLength = closingBracket.length();

        if (openingBracketLength < 1 || closingBracketLength < 1){
            return -1;
        }

        if(stringLength < 1){
            return -1;
        }

        if (openingBracketLength > stringLength || closingBracketLength > stringLength){
            return -1;
        }

        if (getSubstringCount(wholeString, openingBracket) != getSubstringCount(wholeString, closingBracket)){
            return -1;
        }

        if (bracketLocation >= stringLength)
        {
            return -1;
        }

        if (stringsAreEqual(openingBracket, closingBracket)){
            return wholeString.indexOf(closingBracket.toString(), closingBracketLength + bracketLocation);
        }
        else{
            StringBuilder incrementer;
            StringBuilder deIncrementer;
            int incrementerLength;
            int deIncrementerLength;
            boolean lookingForOpening = false;

            if (stringsAreEqual(
                    stringBuilderSubstring(
                            wholeString, bracketLocation, bracketLocation+openingBracketLength), openingBracket)){
                incrementer = openingBracket;
                deIncrementer = closingBracket;
            }
            else if (stringsAreEqual(
                    stringBuilderSubstring(
                            wholeString, bracketLocation, bracketLocation+closingBracketLength), closingBracket)){
                incrementer = closingBracket;
                deIncrementer = openingBracket;
                lookingForOpening = true;
            }
            else
            {
                return -1;
            }

            int counter = 1;
            incrementerLength = incrementer.length();
            deIncrementerLength = deIncrementer.length();

            if(!lookingForOpening) {
                for (int x = bracketLocation + incrementerLength; x < stringLength; x += 1) {
                    StringBuilder substring = stringBuilderSubstring(wholeString, x, x + incrementerLength);
                    if (stringsAreEqual(substring, incrementer, false)) {
                        counter += 1;
                    }

                    substring = stringBuilderSubstring(wholeString, x, x + deIncrementerLength);
                    if (stringsAreEqual(substring, deIncrementer, false)) {
                        counter -= 1;
                    }

                    if (counter == 0) {
                        return x;
                    }
                }
            }
            else
            {
                for (int x = bracketLocation-incrementerLength; x >= 0; x -= 1) {
                    StringBuilder substring = stringBuilderSubstring(wholeString, x, x + incrementerLength);
                    if (stringsAreEqual(substring, incrementer, false)) {
                        counter += 1;
                    }

                    substring = stringBuilderSubstring(wholeString, x, x + deIncrementerLength);
                    if (stringsAreEqual(substring, deIncrementer, false)) {
                        counter -= 1;
                    }

                    if (counter == 0) {
                        return x;
                    }
                }
            }
        }

        return -1;
    }

    /**
     * Finds the index of the matching bracket in a given string
     * @param wholeString the given string to search through
     * @param openingBracket the opening bracket paired with the closing bracket
     * @param closingBracket the closing bracket paired with the opening bracket
     * @param bracketLocation the index of a(n) opening/closing bracket whose mate you are trying to find
     * @return the integer index of the other half of the opening-closing bracket pair
     */
    public static int getIndexOfMatchingBracket(String wholeString,
                                                String openingBracket, String closingBracket,
                                                int bracketLocation){
        return getIndexOfMatchingBracket(
                new StringBuilder(wholeString),
                new StringBuilder(openingBracket),
                new StringBuilder(closingBracket),
                bracketLocation);
    }

    public static int getIndexOfMatchingBracket(JString[] strings,
                                                CharSequence openingBracket, CharSequence closingBracket,
                                                int bracketLocation){
        if (strings == null || openingBracket == null || closingBracket == null || strings.length <= 1)
        {
            return -1;
        }

        int stringLength = strings.length;
        int openingBracketLength = openingBracket.length();
        int closingBracketLength = closingBracket.length();

        if (openingBracketLength < 1 || closingBracketLength < 1){
            return -1;
        }

        if (getSubstringCount(strings, openingBracket) != getSubstringCount(strings, closingBracket)){
            return -1;
        }

        if (bracketLocation >= stringLength)
        {
            return -1;
        }

        if (stringsAreEqual(openingBracket, closingBracket)){
            for(int x = bracketLocation+1; x < strings.length; x += 1){
                if(strings[x].equals(closingBracket)){
                    return x;
                }
            }
        }
        else{
            String incrementer;
            String deIncrementer;

            if(strings[bracketLocation].equals(openingBracket)){
                incrementer = openingBracket.toString();
                deIncrementer = closingBracket.toString();
            }
            else if(strings[bracketLocation].equals(closingBracket)){
                incrementer = closingBracket.toString();
                deIncrementer = openingBracket.toString();
            }
            else{
                return -1;
            }

            int counter = 0;
            for(int x = bracketLocation; x < strings.length; x += 1){
                JString ele = strings[x];
                if(ele.equals(incrementer)){
                    if(x != bracketLocation){
                        counter += 1;
                    }
                }
                else if(ele.equals(deIncrementer)){

                    if(counter == 0){
                        return x;
                    }
                    counter -=1;
                }
            }
        }

        return -1;
    }

    // endregion

    // region string encapsulated by brackets

    /**
     * Determines whether the contents of a StringBuilder object begins with the opening bracket and ends with the closing bracket
     * @param wholeString the string in question
     * @param openingBracket the sub string the whole string must start with in order for the function to return true
     * @param closingBracket the sub string the whole string must end with in order for the function to return true
     * @return returns true if the whole string begins and ends with the opening and closing brackets, respectively
     */
    public static boolean isStringEncapsulatedByBrackets(
            StringBuilder wholeString, StringBuilder openingBracket, StringBuilder closingBracket){
        if (wholeString == null || openingBracket == null || closingBracket == null || wholeString.length() < 1)
        {
            return false;
        }

        if(!startsWith(wholeString, openingBracket) || !endsWith(wholeString, closingBracket)){
            return false;
        }

        int stringLength = wholeString.length();
        int openingBracketLength = openingBracket.length();
        int closingBracketLength = closingBracket.length();

        if (openingBracketLength < 1 || closingBracketLength < 1){
            return false;
        }

        if (openingBracketLength > stringLength || closingBracketLength > stringLength){
            return false;
        }

        if (getSubstringCount(wholeString, openingBracket) != getSubstringCount(wholeString, closingBracket)){
            return false;
        }

        return (stringLength - closingBracketLength) == getIndexOfMatchingBracket(
                wholeString, openingBracket, closingBracket, 0);
    }


    public static boolean isStringEncapsulatedByBrackets(StringBuilder wholeString,
                                                         StringBuilder openingBracket, String closingBracket){
        return isStringEncapsulatedByBrackets(
                wholeString,
                openingBracket,
                new StringBuilder(closingBracket)
        );
    }

    public static boolean isStringEncapsulatedByBrackets(StringBuilder wholeString,
                                                         String openingBracket, String closingBracket){
        return isStringEncapsulatedByBrackets(
                wholeString,
                new StringBuilder(openingBracket),
                new StringBuilder(closingBracket)
        );
    }

    /**
     * Determines whether a string begins with the opening bracket and ends with the closing bracket
     * @param wholeString the string in question
     * @param openingBracket the sub string the whole string must start with in order for the function to return true
     * @param closingBracket the sub string the whole string must end with in order for the function to return true
     * @return returns true if the whole string begins and ends with the opening and closing brackets, respectively
     */
    public static boolean isStringEncapsulatedByBrackets(String wholeString,
                                                         String openingBracket, String closingBracket){
        return isStringEncapsulatedByBrackets(
                new StringBuilder(wholeString),
                new StringBuilder(openingBracket),
                new StringBuilder(closingBracket)
        );
    }

    // endregion

    // region extract substring via brackets

    /**
     * Copies all substrings that start and end in the opening and closing brackets (respectively) into an array
     * @param wholeString the string the function searches through for the substring extraction
     * @param openingBracket the string the substring must start with in order to be selected
     * @param closingBracket the string the substring must end with in order to be selected
     * @param keepBracketsInOutput controls whether the output strings contain the opening and closing brackets
     * @return string array of all the selected substrings copied out of the whole string
     */
    public static StringBuilder[] extractSubstringViaBrackets(
            StringBuilder wholeString,
            StringBuilder openingBracket,
            StringBuilder closingBracket,
            boolean keepBracketsInOutput){


        if (wholeString == null){
            return new StringBuilder[0];
        }

        if (openingBracket == null){
            return new StringBuilder[0];
        }

        if (closingBracket == null){
            return new StringBuilder[0];
        }

        ArrayList<StringBuilder> output = new ArrayList<>();

        boolean continueLoop = true;
        StringBuilder workingString = new StringBuilder(wholeString);

        while(continueLoop)
        {
            int openingBracketIndex = indexOf(workingString, openingBracket);

            if (openingBracketIndex == -1)
            {
                continueLoop = false;
                continue;
            }

            int closingBracketIndex = getIndexOfMatchingBracket(
                    workingString, openingBracket, closingBracket, openingBracketIndex);

            if (closingBracketIndex == -1)
            {
                continueLoop = false;
                continue;
            }

            // extracted substring without brackets
            StringBuilder extractedSubstring = stringBuilderSubstring(workingString,
                    openingBracketIndex + openingBracket.length(), closingBracketIndex);

            StringBuilder substringToAddToList = extractedSubstring;

            // extracted substring with brackets
            extractedSubstring = new StringBuilder(openingBracket).append(extractedSubstring).append(closingBracket);

            if (keepBracketsInOutput) {
                substringToAddToList = extractedSubstring;
            }

            output.add(substringToAddToList);
            workingString = replaceFirstOccurrence(
                    workingString, extractedSubstring, new StringBuilder(), openingBracketIndex);
        }

        return output.toArray(new StringBuilder[0]);
    }

    /**
     * Copies all substrings that start and end in the opening and closing brackets (respectively) into an array
     * @param wholeString the string the function searches through for the substring extraction
     * @param openingBracket the string the substring must start with in order to be selected
     * @param closingBracket the string the substring must end with in order to be selected
     * @param keepBracketsInOutput controls whether the output strings contain the opening and closing brackets
     * @return string array of all the selected substrings copied out of the whole string
     */
    public static StringBuilder[] extractSubstringViaBrackets(
            StringBuilder wholeString,
            String openingBracket,
            String closingBracket,
            boolean keepBracketsInOutput){


        return extractSubstringViaBrackets(
                wholeString,
                new StringBuilder(openingBracket),
                new StringBuilder(closingBracket),
                keepBracketsInOutput);
    }

    /**
     * Copies all substrings that start and end in the opening and closing brackets (respectively) into an array
     * @param wholeString the string the function searches through for the substring extraction
     * @param openingBracket the string the substring must start with in order to be selected
     * @param closingBracket the string the substring must end with in order to be selected
     * @param keepBracketsInOutput controls whether the output strings contain the opening and closing brackets
     * @return string array of all the selected substrings copied out of the whole string
     */
    public static StringBuilder[] extractSubstringViaBrackets(
            String wholeString,
            String openingBracket,
            String closingBracket,
            boolean keepBracketsInOutput){

        return extractSubstringViaBrackets(
                new StringBuilder(wholeString),
                new StringBuilder(openingBracket),
                new StringBuilder(closingBracket),
                keepBracketsInOutput);
    }

    // endregion

    // endregion

    // region multi type check

    public static void multiTypeCheck(Object targetObject, Class<?>[] acceptableClasses){
        for(Class<?> acceptableClass : acceptableClasses){
            if(acceptableClass.isInstance(targetObject)){
                return;
            }
        }
        throw new WrongExecutionTypeException(targetObject.getClass(), acceptableClasses);
    }

    public static void checkIfCanExecuteNumericMethods(JObjectBaseInterface targetObject)
    throws WrongExecutionTypeException {
        if(!targetObject.getType().isNumber()) {
            throw getWrongExecutionTypeException_forNumbers(targetObject.getTypeName());
        }
    }

    public static WrongExecutionTypeException getWrongExecutionTypeException_forNumbers(String type){
        return new WrongExecutionTypeException(type, getNumberTypeNames());
    }

    public static WrongExecutionTypeException getWrongExecutionTypeException_forNumbers(ObjectTypes type){
        return new WrongExecutionTypeException(type.name(), getNumberTypeNames());
    }

    public static WrongInputTypeException getWrongInputTypeException_forNumbers(String type){
        return new WrongInputTypeException(type, getNumberTypeNames());
    }

    public static WrongInputTypeException getWrongInputTypeException_forNumbers(ObjectTypes type){
        return new WrongInputTypeException(type.name(), getNumberTypeNames());
    }

    public static void nullCheck(Object targetObj, String name) throws NullPointerException {
        if(targetObj == null){
            throw new NullPointerException(name + " is null");
        }
    }

    // endregion

    // region getting type

    public static ObjectTypes getType(Object input){
        if(input instanceof JObjectBaseInterface var){
            return var.getType();
        }
        else if(input instanceof Number) {
            if (input instanceof Byte) {
                return ObjectTypes.Byte;
            } else if (input instanceof Short) {
                return ObjectTypes.Short;
            } else if (input instanceof Integer) {
                return ObjectTypes.Integer;
            } else if (input instanceof Long) {
                return ObjectTypes.Long;
            } else if (input instanceof Float) {
                return ObjectTypes.Float;
            } else {
                return ObjectTypes.Double;
            }
        }
        else if(input instanceof CharSequence){
            return ObjectTypes.String;
        }
        else if(input instanceof Boolean){
            return ObjectTypes.Boolean;
        }
        else{
            return ObjectTypes.Unknown;
        }
    }

    public static String[] getNumberTypeNames(){
        ArrayList<String> temp = new ArrayList<>();

        temp.add(ObjectTypes.Byte.name());
        temp.add(ObjectTypes.Short.name());
        temp.add(ObjectTypes.Integer.name());
        temp.add(ObjectTypes.Long.name());
        temp.add(ObjectTypes.Float.name());
        temp.add(ObjectTypes.Double.name());
        temp.add(ObjectTypes.Number.name());

        return temp.toArray(new String[0]);
    }

    public static String[] getNonPrimitiveTypeNames(){
        ArrayList<String> temp = new ArrayList<>();

        temp.add(ObjectTypes.List.name());
        temp.add(ObjectTypes.Set.name());
        temp.add(ObjectTypes.Dictionary.name());
        temp.add(ObjectTypes.Expression.name());
        temp.add("or any other non-primitive type");

        return temp.toArray(new String[0]);
    }

    // endregion

    // region parsing

    public static boolean booleanParse(CharSequence str) throws FailedParseException {
        // bool parse
        if (str.toString().equalsIgnoreCase("true") || str.toString().equalsIgnoreCase("yes")) {
            return true;
        } else if (str.toString().equalsIgnoreCase("false") || str.toString().equalsIgnoreCase("no")) {
            return false;
        }
        throw new FailedBooleanParseException(str);
    }

    // endregion

    // region convert to JObject base interface

    private static JObjectBaseInterface convertToJObjectBaseInterface(Object var){
        if(var instanceof JObjectBaseInterface val){
            return val;
        }
        else if (var instanceof Number val){
            return JNumber.convertToJNumber(val);
        }
        else if(var instanceof CharSequence val){
            return JString.convertToJString(val);
        }
        else if(var instanceof Boolean val){
            return new JPrimitive(val);
        }
        throw new RuntimeException("Couldn't convert object of type " + var.getClass().getTypeName() +
                " into a JObjectBaseInterface class.");
    }

    // endregion

}
