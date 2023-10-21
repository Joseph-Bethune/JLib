package jLibrary.exceptions;

import jLibrary.miscFunctions.MiscFunctions;

/**
 * Custom exception class created to capture unrecognized symbols
 * @author Joseph Bethune
 */
public class UnrecognizedSymbolsException extends RuntimeException{

    private String wholeString;
    private String[] unrecognizedSymbols;

    // constructors

    /**
     * Constructor overload used to capture an array of unrecognized symbols
     */
    public UnrecognizedSymbolsException(CharSequence wholeString, CharSequence[] unrecognized_symbols){
        super(MiscFunctions.concatenateStrings(unrecognized_symbols, true, true) +
                ((unrecognized_symbols.length == 1) ? " is an unrecognized symbol" : " are unrecognized symbols in " + wholeString));
        unrecognizedSymbols = MiscFunctions.toStringArray(unrecognized_symbols);
        this.wholeString = new StringBuilder(wholeString).toString();
    }

    /**
     * Constructor overload used to capture a single unrecognized symbol
     */
    public UnrecognizedSymbolsException(String wholeString, String unrecognized_symbol){
        super("\"" + unrecognized_symbol + "\" is an unrecognized symbol in "  + wholeString);
        unrecognizedSymbols = new String[]{unrecognized_symbol};
        this.wholeString = wholeString;
    }

    /**
     * Restricted default constructor overload
     */
    private UnrecognizedSymbolsException(){
        super();
        unrecognizedSymbols = new String[0];
    }

    //

    /**
     * Getter function for the unrecognized symbols
     * @return string array of the unrecognized symbols stored in the class
     */
    public String[] getUnrecognizedSymbols(){
        return this.unrecognizedSymbols;
    }
}
