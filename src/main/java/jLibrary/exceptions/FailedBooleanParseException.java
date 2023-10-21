package jLibrary.exceptions;

public class FailedBooleanParseException extends FailedParseException{

    public FailedBooleanParseException(CharSequence value){
        super("Failed to parse " + value.toString() + " into a boolean.");
        failingString = value.toString();
    }
}
