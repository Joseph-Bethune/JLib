package jLibrary.exceptions;

public class FailedParseException extends RuntimeException{
    protected String failingString;

    public FailedParseException(){
        super();
    }

    public FailedParseException(String str){
        super(str);
    }

    // region getter

    /**
     * Getter function for the string that failed to parse
     * @return string that failed to parse
     */
    public String getFailingString(){
        return failingString;
    }

    // endregion
}
