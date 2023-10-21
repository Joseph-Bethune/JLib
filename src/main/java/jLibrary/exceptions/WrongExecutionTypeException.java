package jLibrary.exceptions;

import jLibrary.miscFunctions.MiscFunctions;
import jLibrary.typeEnumerable.ObjectTypes;

import java.util.Arrays;

/**
 * Custom exception class created when a function is used on or by the wrong type of object.
 * @author Joseph Bethune
 */
public class WrongExecutionTypeException extends RuntimeException{
    private final String currentType;
    private final String[] supportedTypes;

    private static String generateMessage(String currentType, String[] supportedTypes){
        StringBuilder temp = new StringBuilder();
        temp.append("Objects of type ").append(currentType).append(" cannot execute this method.");
        temp.append(" The acceptable type are ");
        temp.append(
                MiscFunctions.concatenateStrings(
                        supportedTypes,
                        true,
                        false));
        temp.append(".");

        return temp.toString();
    }

    private static String generateMessage(String currentType, String supportedTypes){
        StringBuilder temp = new StringBuilder();
        temp.append("Objects of type ").append(currentType).append(" cannot execute this method.");
        temp.append(" An acceptable type would be ").append(supportedTypes).append(".");

        return temp.toString();
    }

    private WrongExecutionTypeException(){
        currentType = "";
        supportedTypes = new String[0];
    }

    public WrongExecutionTypeException(String currentType, String supportedType){
        super(generateMessage(currentType, supportedType));

        this.currentType = currentType;
        this.supportedTypes = new String[]{supportedType};
    }

    public WrongExecutionTypeException(String currentType, String[] supportedTypes){
        super(generateMessage(currentType, supportedTypes));
        this.currentType = currentType;
        this.supportedTypes = supportedTypes;
    }

    public WrongExecutionTypeException(ObjectTypes currentType, ObjectTypes supportedType){
        super(generateMessage(currentType.name(), supportedType.name()));

        this.currentType = currentType.name();
        this.supportedTypes = new String[]{supportedType.name()};
    }

    public WrongExecutionTypeException(ObjectTypes currentType, ObjectTypes[] supportedTypes){
        super(generateMessage(
                currentType.name(),
                Arrays.stream(supportedTypes).map(ObjectTypes::name).toList().toArray(new String[0])
        ));
        this.currentType = currentType.name();
        String[] newTypes = MiscFunctions.toStringArray(supportedTypes);
        this.supportedTypes = newTypes;
    }

    public WrongExecutionTypeException(String currentType, ObjectTypes supportedType){
        super(generateMessage(currentType, supportedType.name()));

        this.currentType = currentType;
        this.supportedTypes = new String[]{supportedType.name()};
    }

    public WrongExecutionTypeException(String currentType, ObjectTypes[] supportedTypes){
        super(generateMessage(
                currentType,
                Arrays.stream(supportedTypes).map(ObjectTypes::name).toList().toArray(new String[0])
        ));
        this.currentType = currentType;
        String[] newTypes = MiscFunctions.toStringArray(supportedTypes);
        this.supportedTypes = newTypes;
    }

    public WrongExecutionTypeException(Class<?> currentType, Class<?>[] supportedTypes){
        super(generateMessage(
                currentType.getTypeName(),
                Arrays.stream(supportedTypes).map(Class::getTypeName).toList().toArray(new String[0])
        ));
        this.currentType = currentType.getTypeName();
        String[] newTypes = MiscFunctions.toStringArray(supportedTypes);
        this.supportedTypes = newTypes;
    }

    public String[] getSupportedTypes(){
        return supportedTypes;
    }

    public String getCurrentType(){
        return currentType;
    }
}
