package jLibrary.interfaces;

import jLibrary.typeEnumerable.ObjectTypes;

public interface JObjectBaseInterface{

    // region base value

    Object getBaseValue();

    // endregion

    // region setTo functions

    Object setTo(Object inputValue);

    // endregion

    // region type name functions

    ObjectTypes getType();

    default String getTypeName(){
        return getType().name();
    }

    default boolean isObjectType(ObjectTypes type){
        return getType() == type;
    }

    // endregion

    // region type questions

    /**
     * Object type query.
     * @return Returns true if the object is of the requested type, or else will return false.
     */
    default boolean isNumber(){
        return getType().isNumber();
    }

    /**
     * Object type query.
     * @return Returns true if the object is of the requested type, or else will return false.
     */
    default boolean isBoolean(){
        return getType().isBoolean();
    }

    /**
     * Object type query.
     * @return Returns true if the object is of the requested type, or else will return false.
     */
    default boolean isString(){
        return getType().isString();
    }

    /**
     * Object type query.
     * @return Returns true if the object is of the requested type, or else will return false.
     */
    default boolean isPrimitive(){
        return getType().isPrimitive();
    }

    /**
     * Object type query.
     * @return Returns true if the object is of the requested type, or else will return false.
     */
    default boolean isOperator(){
        return getType().isOperator();
    }

    /**
     * Object type query.
     * @return Returns true if the object is of the requested type, or else will return false.
     */
    default boolean isSymbol(){
        return getType().isSymbol();
    }

    /**
     * Object type query.
     * @return Returns true if the object is of the requested type, or else will return false.
     */
    default boolean isBracket(){
        return getType().isBracket();
    }

    /**
     * Object type query.
     * @return Returns true if the object is of the requested type, or else will return false.
     */
    default boolean isNull() {
        return getType().isNull();
    }

    /**
     * Object type query.
     * @return Returns true if the object is of the requested type, or else will return false.
     */
    default boolean isList(){
        return getType().isList();
    }

    /**
     * Object type query.
     * @return Returns true if the object is of the requested type, or else will return false.
     */
    default boolean isSet(){
        return getType().isSet();
    }

    /**
     * Object type query.
     * @return Returns true if the object is of the requested type, or else will return false.
     */
    default boolean isDictionary(){
        return getType().isDictionary();
    }

    // endregion

    // region from string functions

    JObjectBaseInterface fromString(CharSequence inputString);

    // endregion

    // region to debug string

    String toDebugString();

    // endregion
}
