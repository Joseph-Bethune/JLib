package test;

import jLibrary.JPrimitive;
import jLibrary.miscFunctions.MiscFunctions;

import static jLibrary.miscFunctions.MiscFunctions.print;

public class JPrimitiveTests {

    private static Object[] generateTestValues(){
        Number val = Short.MAX_VALUE + 5;
        val = val.floatValue();
        Object[] testValues = {
                5, 5L, 5.5d, 5.5f, 5d, 5f, val, true, false, "bob", "\"bob\"", "-", "["
        };
        return testValues;
    }

    private static void simpleObjectConstructionTests(){
        Object[] testValues = generateTestValues();

        print();
        print("Simple Object Construction test");
        for(Object testValue : testValues){
            print("_____New Test_____");
            print();
            print("Input value : ");
            print(testValue);
            print();
            print("Input value type : ");
            print(MiscFunctions.getType(testValue));
            print();

            JPrimitive obj = new JPrimitive(testValue);

            print("Created Object : ");
            print(obj.toString());

            print();
            print("Debug String : ");
            print(obj.toDebugString());
        }
    }

    private static void addTest(){
        Object[] testValues = generateTestValues();

        print();
        print("Add tests");
        for(Object testValue1 : testValues) {
            for (Object testValue2 : testValues) {
                print();
                print("_____New Test_____");
                print();
                print("Test Value 1 : ");
                print(testValue1);
                print();
                print("Test Value 2 : ");
                print(testValue2);
                print();

                JPrimitive value1 = new JPrimitive(testValue1);
                print("Object 1 : ");
                print(value1.toDebugString());
                print();

                try {

                    JPrimitive obj = value1.add(testValue2);

                    print("Created Object : ");
                    print(obj.toString());

                    print();
                    print("Debug String : ");
                    print(obj.toDebugString());
                }
                catch (Exception e){
                    print(e);
                    print(e.getStackTrace());
                }
            }
        }
    }

    public static void main(String[] args) {

        MiscFunctions.setDebugMode(false);

        //simpleObjectConstructionTests();
        addTest();

        MiscFunctions.setDebugMode(false);
    }
}
