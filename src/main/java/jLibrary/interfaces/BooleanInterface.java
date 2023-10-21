package jLibrary.interfaces;

import jLibrary.exceptions.WrongExecutionTypeException;
import jLibrary.typeEnumerable.ObjectTypes;

public interface BooleanInterface extends JObjectBaseInterface {

    // region set to functions
    Object setTo(boolean inputValue);

    // endregion

    // region primitive value access

    default boolean booleanValue() throws WrongExecutionTypeException{
        if(isBoolean()){
            return (Boolean)getBaseValue();
        }
        throw new WrongExecutionTypeException(getType().name(), ObjectTypes.Boolean.name());
    }

    // endregion

    // region primitive equals function

    /**
     * Compares the given inputValue with this Object.
     * @param inputValue The value to be compared to this Object.
     * @return Returns True if the inputValue is equal to this Object, else it returns False.
     */
    default boolean equals(boolean inputValue) {

        if(isBoolean()) {
            return booleanValue() == inputValue;
        }
        return false;
    }

    // endregion

    // region operations using BooleanInterfaces

    default boolean or(BooleanInterface value) throws WrongExecutionTypeException {
        boolean thisValue = this.booleanValue();
        boolean thatValue = value.booleanValue();
        return thisValue || thatValue;
    }

    default boolean and(BooleanInterface value) throws WrongExecutionTypeException {
        boolean thisValue = this.booleanValue();
        boolean thatValue = value.booleanValue();
        return thisValue && thatValue;
    }

    // endregion

    // region operations using booleans

    default boolean or(Boolean value) throws WrongExecutionTypeException {
        return this.booleanValue() || value;
    }

    default boolean and(Boolean value) throws WrongExecutionTypeException {
        return this.booleanValue() && value;
    }

    // endregion
}
