package jLibrary.exceptions;

/**
 * Custom exception class created when a string fails to parse
 * @author Joseph Bethune
 */
public class FailedNumericParseException extends FailedParseException{



    // region constructors

    /**
     * Constructor overload used to capture the string that failed to parse
     */
    public FailedNumericParseException(CharSequence error_string){
        super("Failed to parse " + error_string.toString() + " into a number.");
        failingString = error_string.toString();
    }

    /**
     * Restricted default constructor overload
     */
    private FailedNumericParseException(){
        super();
        failingString = "";
    }

    // endregion
}
