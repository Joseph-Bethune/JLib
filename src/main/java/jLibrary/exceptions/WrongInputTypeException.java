package jLibrary.exceptions;

import jLibrary.miscFunctions.MiscFunctions;
import jLibrary.typeEnumerable.ObjectTypes;

import java.util.Arrays;

public class WrongInputTypeException extends IllegalArgumentException{
    private final String currentType;
    private final String[] supportedTypes;

    private static String generateMessage(String currentType, String[] supportedTypes){
        StringBuilder temp = new StringBuilder();
        temp.append("Objects of type ").append(currentType).append(" cannot be accepted by this method.");
        temp.append(" The acceptable types are ");
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
        temp.append("Objects of type ").append(currentType).append(" cannot be accepted by this method.");
        temp.append(" An acceptable type would be ").append(supportedTypes).append(".");

        return temp.toString();
    }

    private WrongInputTypeException(){
        currentType = "";
        supportedTypes = new String[0];
    }

    public WrongInputTypeException(String currentType, String supportedType){
        super(generateMessage(currentType, supportedType));

        this.currentType = currentType;
        this.supportedTypes = new String[]{supportedType};
    }

    public WrongInputTypeException(String currentType, String[] supportedTypes){
        super(generateMessage(currentType, supportedTypes));
        this.currentType = currentType;
        this.supportedTypes = supportedTypes;
    }

    public WrongInputTypeException(ObjectTypes currentType, ObjectTypes supportedType){
        super(generateMessage(currentType.name(), supportedType.name()));

        this.currentType = currentType.name();
        this.supportedTypes = new String[]{supportedType.name()};
    }

    public WrongInputTypeException(ObjectTypes currentType, ObjectTypes[] supportedTypes){
        super(generateMessage(currentType.name(), Arrays.stream(supportedTypes).map(
                Enum::name).toList().toArray(new String[0])
        ));
        this.currentType = currentType.name();
        this.supportedTypes = MiscFunctions.toStringArray(supportedTypes);
    }

    public WrongInputTypeException(String currentType, ObjectTypes supportedType){
        super(generateMessage(currentType, supportedType.name()));

        this.currentType = currentType;
        this.supportedTypes = new String[]{supportedType.name()};
    }

    public WrongInputTypeException(String currentType, ObjectTypes[] supportedTypes){
        super(generateMessage(
                currentType,
                Arrays.stream(supportedTypes).map(Enum::name).toList().toArray(new String[0])
        ));
        this.currentType = currentType;
        String[] newTypes = MiscFunctions.toStringArray(supportedTypes);
        this.supportedTypes = newTypes;
    }

    public WrongInputTypeException(Class<?> currentType, Class<?>[] supportedTypes){
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
