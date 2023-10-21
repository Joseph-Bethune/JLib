package jLibrary.interfaces;

import jLibrary.JString;
import jLibrary.exceptions.WrongExecutionTypeException;
import jLibrary.typeEnumerable.ObjectTypes;

public interface StringInterface extends JObjectBaseInterface {

    // region setTo functions

    Object setTo(char inputValue);

    // endregion

    // region primitive value access

    default JString toJString() {
        return new JString(toString());
    }

    // endregion

    // region primitive equals function

    default boolean equalsIgnoreCase(char val){
        if(isString()){
            return toJString().equalsIgnoreCase(val);
        }
        return false;
    }

    /**
     * Compares the given inputValue with this JObject, while ignoring case.
     * @param inputValue The value to be compared to this JObject.
     * @return Returns True if the inputValue is equal to this JObject regardless of case, else it returns False.
     */
    default boolean equalsIgnoreCase(Object inputValue){

        if(isString()){
            return toJString().equalsIgnoreCase(inputValue);
        }
        else{
            return equals(inputValue);
        }
    }

    /**
     * Compares the given inputValue with this Object.
     * @param inputValue The value to be compared to this Object.
     * @return Returns True if the inputValue is equal to this Object, else it returns False.
     */
    default boolean equals(char inputValue){
        if(isString()){
            return toJString().equals(new JString(inputValue));
        }
        return false;
    }

    // endregion

    // region add operations

    default Object add(Object obj2) {
        return toJString().clone().append(obj2.toString());
    }

    // endregion

    // region is blank functions

    /**
     * Returns true if this string only contains white space.
     */
    default boolean isBlank(){
        if(isString()){
            return toJString().isBlank();
        }
        else {
            throw new WrongExecutionTypeException(getType().name(), ObjectTypes.String.name());
        }
    }

    // endregion
}
