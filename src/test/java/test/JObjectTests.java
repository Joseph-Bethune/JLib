package test;

import jLibrary.JPrimitive;
import jLibrary.exceptions.UnassignedVariableException;
import jLibrary.JObject;
import jLibrary.JString;
import jLibrary.miscFunctions.MiscFunctions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.function.Predicate;

import static jLibrary.miscFunctions.MiscFunctions.*;
import static org.assertj.core.api.Assertions.*;

public class JObjectTests {

    private static Object[] generateTestValues(){
        Number val = Short.MAX_VALUE + 5;
        val = val.floatValue();
        return new Object[] {
                5, 5L, 5.5d, 5.5f, 5d, 5f, val, true, false, "bob", "\"bob\"", "-", "["
        };
    }

    private static void simpleObjectConstructionTests(){
        Number val = Short.MAX_VALUE + 5;
        val = val.floatValue();
        Object[] testValues = new Object[]{
            5, 5L, 5.5d, 5.5f, 5d, 5f, val, true, false, "bob", "\"bob\""
        };

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

            JObject obj = new JObject(testValue);

            print("Created Object : ");
            print(obj.toString());

            print();
            print("Debug String : ");
            print(obj.toDebugString());
        }
    }

    private static void complexObjectConstructionTest(){

        Object[] testItems = new Object[]{
                //"'var'",
                //"\"7,8,   9\"",
                //"true",
                //"1"
                //"[0, 1, 2, 3, 4]",
                //"0, 1, 2, 3, 4",
                //"(0, 1, 2, 3, 4)",
                //["\"1, 2, 3\",\"4, 5, 6\"]",
                //"(\"1, 2, 3\",\"4, 5, 6\")",
                //"\"1, 2, 3\",\"4, 5, 6\"",
                //"[[0,1,2][3,4,5]]",
                //"((0,1,2)(3,4,5))",
                //"{\"stringValue\":\"7,8,   9\"}",
                //"{className:\"newClass\", listValue:[[0,1,2][3,4,5]], setValue:((0,1,2)(3,4,5)), stringValue:\"7,8,   9\", numericValue:5}",
                //"{\"ClassName\":\"newClass\", \"listValue\":[[0,1,2][3,4,5]], \"setValue\":((0,1,2)(3,4,5)), \"stringValue\":\"7,8,   9\", \"numericValue\":5}"
                //"{"+JObject.ParameterName_ClassName+":\"newClass\", \"listValue\":[[0,1,2][3,4,5]], \"setValue\":((0,1,2)(3,4,5)), \"stringValue\":\"7,8,   9\", nullValue:nuLL, \"numericValue\":5}",
                //"{\"className\":\"newClass\", \"listValue\":[[0,1,2][3,4,5]], \"setValue\":((0,1,2)(3,4,5)), \"stringValue\":\"7,8,   9\", \"numericValue\":5}{\"className\":\"newClass\", \"listValue\":[[0,1,2][3,4,5]], \"setValue\":((0,1,2)(3,4,5)), \"stringValue\":\"7,8,   9\", \"numericValue\":5}",
                //"{className:\"newClass\", listValue:[[0,1,2][3,4,5]], setValue:((0,1,2)(3,4,5)), stringValue:\"7,8,   9\", numericValue:5}{className:\"newClass\", listValue:[[0,1,2][3,4,5]], setValue:((0,1,2)(3,4,5)), stringValue:\"7,8,   9\", numericValue:5}",
                //"'listValue', 'numericValue', 'className', 'stringValue'",
                //"('listValue', 'numericValue', 'className', 'stringValue')",
                //"[[false, false, false][false, false, false][false, false, false]]",
                //"[(True && (!False || !true)), false, false]",
                "((5*4+4), 3, 4)",
                //"((5*4+4), 3, 4)jj",
                //"ff((5*4+4), 3, 4)jj",
                //"('listValue'+'listValue', 'numericValue', 'className', 'stringValue')",
                //"'5+2', '3', '4', '6'",
                //"5+2, 3, 4, 6"
                //"\"My name's bob.\""
                //"\"My name\"+\"'\"+\"s bob.\""
                //"temp.z.[temp.a]+temp.b.[temp.[temp.d]]"
            };

        //testItems = new Object[]{"{className:\"Inventory\", parts:[{className:\"OutSourced\", min:1, max:3, price:3, companyName:\"TireCorp\", name:\"Tire\", id:0, stock:2}, {className:\"InHouse\", min:1, machineId:1, max:3, price:3, name:\"Spoke\", id:1, stock:2}, {className:\"InHouse\", min:1, machineId:2, max:3, price:3, name:\"Hub\", id:2, stock:2}, {className:\"InHouse\", min:1, machineId:3, max:3, price:3, name:\"Rim\", id:3, stock:2}], nextProductId:1, nextPartId:4, products:[{className:\"Product\", min:1, max:3, price:10, name:\"Wheel\", id:0, stock:2, associatedPartIds:[0, 1, 2, 3]}]}"};
        //testItems = new Object[]{"{className:\"Inventory\", parts:[], nextProductId:1, nextPartId:4, products:[]}"};

        print();
        print("Object Construction Tests");
        print();
        for(Object testItem : testItems){

            JObject obj = new JObject();

            print();
            print("_____New Test____");
            print();
            print("input value : ");
            print(testItem);
            print();

            try{
                obj.fromString(testItem.toString());

                print("from string complete");
                print("object : ");
                print(obj);
                print();
                print("object debug string : ");
                print(obj.toDebugString());
            }
            catch (Exception e){
                print(e);
                print(e.getStackTrace());
            }
        }
    }

    private static void objectConstructionTest2(){

        String testItem;

        // testItem = "'var'";
        //testItem = "{\"className\":\"newClass\", \"listValue\":[[0,1,2][3,4,5]], \"stringValue\":\"7,8,   9\", \"numericValue\":5}";
        //testItem = "{\"tupleValue\":(0,1,2,3,4), \"listValue\":[[0,1,2][3,4,5]], \"stringValue\":\"7,8,   9\"}{\"className\":\"newClass\", \"listValue\":[[0,1,2][3,4,5]], \"stringValue\":\"7,8,   9\"}";
        //testItem = "'listValue', 'numericValue', 'className', 'stringValue'";
        //testItem = "('listValue', 'numericValue', 'className', 'stringValue')";
        // testItem = "0, 1, 2, 3, 4";
        //testItem = "[[false, false, false][false, false, false][false, false, false]]";
        //testItem = "[(True && (!False || !true)), false, false]";
        //testItem = "((5*4+4), 3, 4)";
        //testItem = "((5*4+4), 3, 4)jj";
        //testItem = "ff((5*4+4), 3, 4)jj";
        testItem = "('listValue'+'listValue', 'numericValue', 'className', 'stringValue')";

        print("test:");
        print();
        print("input string : ");
        print();
        print(testItem);
        print();
        print("object to string : ");
        print();
        JObject object;
        try {
            object = new JObject(testItem);

            print(object);

            print("class : " + object.getTypeName());

            JObject[] parameterObjects = object.getValues();
            if(parameterObjects != null) {
                print();
                print("parameter fields names");
                for(String s : object.getKeys())
                {
                    print();
                    print(s);
                }

                print();
                print("parameter objects");
                for (JObject subObject : object.getValues()) {
                    print();
                    print(subObject);
                }
            }
            else
            {
                print(object);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void equalsTests(){

        var testValue = 5;

        var obj = new JObject(5);

        print("Equals test : ");
        print();
        print("obj 1 : ");
        print(obj);
        print();
        print("test value : ");
        print(testValue);
        print();
        print("result : ");
        print(obj.equals(testValue));
    }

    public static void cloneTests(){
        String testString = "";

        testString = "5.5";

        JObject obj1 = new JObject(testString);
        JObject obj2 = obj1.clone();

        print("obj 1");
        print(obj1);
        print();
        print("obj 2");
        print(obj2);
        print("object 1 == object 2 : "+obj1.equals(obj2));

        obj1 = new JObject("6.6");

        print();
        print("post change tests");
        print("obj 1");
        print(obj1);
        print();
        print("obj 2");
        print(obj2);
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

                JObject value1 = new JObject(testValue1);
                print("Object 1 : ");
                print(value1.toDebugString());
                print();

                try {

                    JObject obj = value1.add(testValue2);

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

    public static void duplicateTests(){

        String testValue = "";

        testValue = "9, 5,5,6,7,8";
        //testValue = "[5,6,7][5,6,7]";
        //testValue = "string, string, temp, bob";

        JObject obj1 = new JObject(testValue);
        JObject obj2 = obj1.clone();
        obj2.removeDuplicates();

        print("Remove duplicate test");
        print();
        print("obj 1");
        print();
        print(obj1);
        print();
        print("obj 2");
        print();
        print(obj2);
    }

    public static void sortTests(){
        String testValue = "";

        testValue = "9, 5,5,6,7,8";
        //testValue = "[5,6,7][5,6,7]";
        testValue = "string, temp, bob";
        //testValue = "(9, 5,5,6,7,8)";

        JObject obj1 = new JObject(testValue);
        JObject obj2 = null;

        print("sort test");
        print();
        print("input string");
        print();
        print(testValue);
        print();
        print("raw");
        print();
        print(obj1);
        print();
        print("ascending");
        obj2 = obj1.sort();
        print();
        print(obj2);
        print();
        print("descending, sort then revers");
        obj2 = obj1.sort().reverseOrder();
        print();
        print(obj2);
        print();
        print("descending, descending order");
        obj2 = obj1.sort(false);
        print();
        print(obj2);
        print();
        print("comparator");
        obj2 = obj1.sort(java.util.Comparator.reverseOrder());
        print();
        print(obj2);
    }

    public static void containsTests(){

        String testItem;
        String searchItem;

        // testItem = "'var'";
        //testItem = "{\"className\":\"newClass\", \"listValue\":[[0,1,2][3,4,5]], \"stringValue\":\"7,8,   9\", \"numericValue\":5}";
        //testItem = "{\"tupleValue\":(0,1,2,3,4), \"listValue\":[[0,1,2][3,4,5]], \"stringValue\":\"7,8,   9\"}{\"className\":\"newClass\", \"listValue\":[[0,1,2][3,4,5]], \"stringValue\":\"7,8,   9\"}";
        //testItem = "'listValue', 'numericValue', 'className', 'stringValue'";
        //testItem = "('listValue', 'numericValue', 'className', 'stringValue')";
        testItem = "0, 1, 2, 3, 4";
        //testItem = "[[false, false, false][false, false, false][false, false, false]]";
        //testItem = "[(True && (!False || !true)), false, false]";
        //testItem = "((5*4+4), 3, 4)";
        //testItem = "((5*4+4), 3, 4)jj";
        //testItem = "ff((5*4+4), 3, 4)jj";
        //testItem = "('listValue'+'listValue', 'numericValue', 'className', 'stringValue')";

        searchItem = "3";

        JObject obj = new JObject(testItem);

        print("contains test");
        print();
        print("test value : ");
        print(obj);
        print();
        print("search value : ");
        print(searchItem);
        print();
        print("contains search value? : ");
        print(obj.containsValue(searchItem));
        print();
        print("contains key 3? : ");
        print(obj.containsKey(3));
    }

    public static void containsKeyTest(){
        Object[] testValues = {
                "value",
                new String[]{"value", "temp"}
        };

        JObject target = JObject.createEmptyDictionary("value", "temp");

        // should be all true
        for(Object testValue : testValues){
            assertThat(target.containsKey(testValue)).isTrue();
            if(testValue instanceof String var){
                assertThat(target.containsEveryKey(var)).isTrue();
            }
            else if(testValue instanceof String[] var){
                assertThat(target.containsEveryKey(var)).isTrue();
            }
        }
        assertThat(target.containsAnyKey("blarg", "value", "temp")).isTrue();

        // should be false
        assertThat(target.containsKey("blarg")).isFalse();
        assertThat(target.containsEveryKey("blarg", "value", "temp")).isFalse();

        print("Tests complete");
    }

    public static void keyRemovalTest(){

        String testItem;
        String keyList;

        testItem = "{\"className\":\"newClass\", \"numericValue\":5, \"listValue\":[[0,1,2][3,4,5]], \"stringValue\":\"7,8,   9\"}";
        //testItem = "{\"tupleValue\":(0,1,2,3,4), \"listValue\":[[0,1,2][3,4,5]], \"stringValue\":\"7,8,   9\"}{\"className\":\"newClass\", \"listValue\":[[0,1,2][3,4,5]], \"stringValue\":\"7,8,   9\"}";
        //testItem = "3, 5, 0, 1, 2, 3, 4";

        //keyList = "5,3";
        //keyList = "bob";
        keyList = "listValue, numericValue";

        JObject obj = new JObject(testItem);
        JObject keyListObj = new JObject(keyList);

        print();
        print("key removal test");
        print();
        print("input obj : ");
        print(obj);
        print();
        print("keys to remove : ");
        print(keyListObj);
        print();

        JObject newObj = obj.clone().removeSubObjectsByKeys(keyListObj.toStringArray());

        print("result : ");
        print();
        print("new object : ");
        print(newObj);
        print();
        print("remaining keys");
        print(newObj.getKeys());
        print();
    }

    public static void toArrayTests(){
        //var testItem = "t, f, f, t";
        //var testItem = "blarg, dad, mom, bob";
        var testItem = "1, 2, 3, 4";

        JObject obj =  new JObject(testItem);

        print("To array tests");
        print();
        print("test value : ");
        print(testItem);
        print();
        print("object : ");
        print(obj);
        print();
        print("string array : ");
        print(obj.toStringArray());
        print();
        print("bool array : ");
        print(obj.toBooleanArray());
        print();
        print("int array : ");
        print(obj.toIntegerArray());
        print();
        print("float array : ");
        print(obj.toFloatArray());
        print();
        print("double array : ");
        print(obj.toDoubleArray());
        print();

        //
        obj.remove(0);
        obj.insert(0, 4);
        print(obj);
    }

    public static void prependTests(){

        var testData = new String[]{
                "[]",
                "1, 2, 3, 4",
                "blarg, dad, mom, bob",
                "t, f, f, t"
        };

        print("Prepend tests");
        print();
        for(String s : testData){
            print("test data : ");
            print(s);
            print();

            JObject obj =  new JObject(s);

            print("original list : ");
            print(obj);
            print();

            var newData = new String[]{
                    "0","1","2","3","4","5"
            };

            for(String x : newData){
                print("prepending object : ");
                print(x);
                print();

                obj.prepend(x);
            }

            print("final list : ");
            print(obj);
            print();
        }
    }

    public static void appendTests(){
        var testData = new String[]{
                "[]",
                "1, 2, 3, 4",
                "blarg, dad, mom, bob",
                "t, f, f, t"};

        print("Append tests");
        print();
        for(String s : testData){
            print("test data : ");
            print(s);
            print();

            JObject obj =  new JObject(s);

            print("original list : ");
            print(obj);
            print();

            var newData = new String[]{
                    "0","1","2","3","4","5"
            };

            for(String x : newData){
                print("appending object : ");
                print(x);
                print();

                obj.append(x);
            }

            print("final list : ");
            print(obj);
            print();
        }
    }

    public static void listAddTests(){
        var testData = new String[]{
                "[]",
                "1, 2, 3, 4",
                "blarg, dad, mom, bob",
                "t, f, f, t"};

        print("Add tests tests");
        print();
        for(int testCode = 0; testCode < 3; testCode++){
            print("!! new test type !!");
            print();

            if(testCode == 0){
                print("prepend tests");
                print();
            }
            else if(testCode == 1){
                print("insert tests");
                print();
            }
            else{
                print("append tests");
                print();
            }

            for(String s : testData){
                print("!! new test data !!");
                print();
                print("test data : ");
                print(s);
                print();

                JObject obj =  new JObject(s);

                print("original list : ");
                print(obj);
                print();

                var newData = new String[]{
                        "0","1","2","3","4","5"
                };

                for(String x : newData){
                    if(testCode == 0){
                        print("prepending object : ");
                    }
                    else if(testCode == 1){
                        print("inserting object at start: ");
                    }
                    else{
                        print("appending object : ");
                    }
                    print(x);
                    print();

                    if(testCode == 0){
                        obj = obj.convertSetToList().prepend(x).convertListToSet();
                    }
                    else if(testCode == 1){
                        obj = obj.convertSetToList().insert(0, x).convertListToSet();
                    }
                    else{
                        obj = obj.convertSetToList().append(x).convertListToSet();
                    }
                }

                print("final list : ");
                print(obj);
                print();
            }
        }
    }

    public static void methodChainingTests(){
        var testData = "blarg, dad, mom, bob";

        JObject obj = new JObject(testData);

        obj = obj.convertSetToList().append(0).append(1).append(2).append(3).append(4).append(5).convertListToSet();

        print(obj);
    }

    public static void combineTests(){
        var initialData = new String[]{
                "{\"temp\":\"temp\", \"blarg\":\"blarg\"}",
                "[0,1,2,3]"
        };
        var newData = new String[]{
                "{\"temp\":\"newTemp\", \"blarg\":\"newBlarg\", \"vec\":\"newVec\"}",
                "[4,5,6,7]"
        };

        print("combine tests : ");
        print();
        for(String iniData : initialData){
            for(String newDataStr : newData){

                JObject obj1 = new JObject(iniData);
                JObject obj2 = new JObject(newDataStr);

                print("new test : ");
                print();

                print("obj 1 : ");
                print(obj1);
                print();

                print("obj 2 : ");
                print(obj2);
                print();

                try {
                    obj1.combine(obj2);
                }
                catch (Exception e){
                    print("!!!! ERROR !!!!");
                    print(e.toString());
                    print(e.getStackTrace());
                    print();
                }

                print("result: ");
                print(obj1);
                print();
            }
        }
    }

    public static void popTest(){
        var testData = new String[]{
                //"[]",
                //"1, 2, 3, 4",
                //"blarg, dad, mom, bob",
                "true, false, false, true"
        };

        boolean[] flag = {false, true};

        print("Pop tests");
        print();
        for(String s : testData){
            for(boolean popFromBeginning : flag){
                print("test data : ");
                print(s);
                print();

                JObject obj =  new JObject(s);

                print("original list : ");
                print(obj);
                print();

                print("popping from beginning : ");
                print(popFromBeginning);
                print();

                boolean keepGoing = true;
                while (keepGoing){
                    print("popping value...");
                    print();
                    JObject poppedValue = obj.pop(popFromBeginning);
                    if(poppedValue != null) {
                        print("popped value");
                        print(poppedValue);
                        print();
                        print("remaining list");
                        print(obj);
                    }
                    else{
                        print("list is empty");
                        print();
                        keepGoing = false;
                    }
                }

                print("final list : ");
                print(obj);
                print();
            }
        }
    }

    public static void setAndListConversionTests(){
        var constructionValues = new String[]{
                "(0,1,1,2,2,3,4,5)",
                "[0,1,1,2,2,3,4,5]"
        };

        print();
        print("Set to-from List conversion tests");
        print();

        for(String constructionValue : constructionValues) {
            print("!!!! New Test Iteration !!!!");
            print("construction value : ");
            print(constructionValue);
            print();

            JObject originalObject = new JObject(constructionValue);

            print("original object");
            print(originalObject);
            print();

            JObject newObject = originalObject.clone().convertSetToList().removeDuplicates().convertListToSet();

            print("new object");
            print(newObject);
            print();
        }
    }

    public static void predicateTest(){
        Predicate<JObject> isNumber = new Predicate<JObject>() {
            @Override
            public boolean test(JObject jObject) {

                boolean var = jObject.isNumber();

                print("Testing :" + jObject);
                print("result : " + var);
                print();
                return var;
            }
        };

        var testItem = "blarg, dad, mom, bob, 7, 5.2, 3, tom";

        JObject obj =  new JObject(testItem);
        JObject output = obj.clone().removeIfNot(isNumber);

        print("predicate tests");
        print();
        print("test value : ");
        print(testItem);
        print();
        print("object : ");
        print(obj);
        print();
        print("output : ");
        print(output);
        print();
        if(false) {
            print("input debugged : ");
            print(obj.toDebugString());
            print();
            print("output debugged : ");
            //print(output.toDebugString());
            print();
        }
    }

    public static void hashTests(){
        Object[] testItems = new Object[]{
                "'var'",
                "5",
                "true",
                "{\"stringValue\":\"7,8,   9\"}",
                "{\""+JObject.ParameterName_ClassName+"\":\"newClass\", \"listValue\":[[0,1,2][3,4,5]], \"setValue\":((0,1,2)(3,4,5)), \"stringValue\":\"7,8,   9\", \"numericValue\":5}",
                //"{"+JObject.ParameterName_ClassName+":\"newClass\", listValue:[[0,1,2][3,4,5]], setValue:((0,1,2)(3,4,5)), stringValue:\"7,8,   9\", numericValue:5}",
                //"{"+JObject.ParameterName_ClassName+":\"newClass\", \"listValue\":[[0,1,2][3,4,5]], \"setValue\":((0,1,2)(3,4,5)), \"stringValue\":\"7,8,   9\", \"numericValue\":5}{\"className\":\"newClass\", \"listValue\":[[0,1,2][3,4,5]], \"setValue\":((0,1,2)(3,4,5)), \"stringValue\":\"7,8,   9\", \"numericValue\":5}",
                //"{"+JObject.ParameterName_ClassName+":\"newClass\", listValue:[[0,1,2][3,4,5]], setValue:((0,1,2)(3,4,5)), stringValue:\"7,8,   9\", numericValue:5}{className:\"newClass\", listValue:[[0,1,2][3,4,5]], setValue:((0,1,2)(3,4,5)), stringValue:\"7,8,   9\", numericValue:5}",
                //"'listValue', 'numericValue', 'className', 'stringValue'",
                //"('listValue', 'numericValue', 'className', 'stringValue')",
                //"0, 1, 2, 3, 4",
                //"[[false, false, false][false, false, false][false, false, false]]",
                //"[(True && (!False || !true)), false, false]",
                //"((5*4+4), 3, 4)",
                //"((5*4+4), 3, 4)",
                //"((5*4+4), 3, 4)",
                //"('listValue'+'listValue', 'numericValue', 'className', 'stringValue')",
                //new String[]{"5+2", "3", "4", "6"}
        };

        for(Object testItem1 : testItems){
            for(Object testItem2 : testItems){
                JObject obj1 = new JObject(testItem1);
                JObject obj2 = new JObject(testItem2);
                boolean sameSourceObject = testItem1 == testItem2;

                print();
                print("_____New Test Iteration_____");
                print();
                print("Object 1: " + obj1);
                print();
                print("Object 2: " + obj2);
                print();
                print("Objects are built from the same value : " + sameSourceObject);
                print();
                print("Object references are the same: " + (obj1 == obj2));
                print();
                print("Objects contain the same values: " + obj1.equals(obj2));
                print();

                int hash1 = obj1.hashCode();
                int hash2 = obj2.hashCode();
                print("Object 1 hash code: " + hash1);
                print();
                print("Object 2 hash code: " + hash2);
                if(sameSourceObject) {
                    if (hash1 != hash2) {
                        throw new RuntimeException("Test Failed : hashes are not equal");
                    }
                }
            }
        }
    }

    public static void expressionTests(){
        Object[] testItems = new Object[]{
                //"("
                //"(5+3)+5 + temp.z"
                //"6+1"
                //"\"var\"",
                //"'var'",
                //"5",
                //"true",
                //"+"
                //">="
                //"false || !no"
                //"[2,]"
                //"[2,5+1]"
                //"(2,)"
                //"(1)(2)(3)"
                //"((1),(1),(1))"
                //"(5x+3y+temp.z)*(1+temp)"
                //"(temp.a.b.c.d && true) && (temp.b || true)"
                //"92.52temp + -.5 -0.5"
                //"[\"temp\", temp]"
                //"[temp, temp]"
                //"{'numberValues':[temp, temp+1, temp+2]}",
                //"{\"numberValues\":[temp, temp+1, temp+2]}"
                //"{\"object1\"    : {\"numberValues\":[temp, temp+1, temp+2]}}"
                //"{\"stringValue\":\"7,8,   9\"}",
                //"{'stringValue':\"bob's name\"}",
                //"[[0,1,2][3,4,5]]"
                //"{"+JString.getQuotedJString(JObject.ParameterName_ClassName)+":\"NewClass\", \"listValue\":[[0,1,2][3,4,5]]}"
                //"{"+JString.getQuotedJString(JObject.ParameterName_ClassName)+":'NewClass', 'listValue':[[0,1,2][3,4,5]]}"
                //"{"+JString.getQuotedJString(JObject.ParameterName_ClassName)+":\"newClass\", \"listValue\":[[0,1,2][3,4,5]], \"setValue\":((0,1,2)(3,4,5)), \"stringValue\":\"7,8,   9\", \"numericValue\":5}",
                //"{"+JObject.ParameterName_ClassName+":\"newClass\", listValue:[[0,1,2][3,4,5]], setValue:((0,1,2)(3,4,5)), stringValue:\"7,8,   9\", numericValue:5}{className:\"newClass\", listValue:[[0,1,2][3,4,5]], setValue:((0,1,2)(3,4,5)), stringValue:\"7,8,   9\", numericValue:5}",
                //"'listValue', 'numericValue', 'className', 'stringValue'",
                //"('listValue', 'numericValue', 'className', 'stringValue')",
                //"0, 1, 2, 3, 4",
                //"[[false, false, false][false, false, false][false, false, false]]",
                //"[(True && (!False || !true)), false, false]",
                //"((5*4+4), 3, 4)",
                //"((5*4+4), 3, 4)",
                //"((5*4+4), 3, 4)",
                //"('listValue'+'listValue', 'numericValue', 'className', 'stringValue')",
                //new String[]{"5+2", "3", "4", "6"}
                "temp.z.[temp.a]+temp.b.[temp.[temp.d]]"
        };

        for(Object testItem : testItems){


            print("_____New Test Iteration_____");
            print();
            print("Test value : ");
            print(testItem);
            print();
            print("Constructing expression : ");
            JObject obj1 = new JObject(testItem);
            print("Expression construction complete");
            print();
            print("Object 1: ");
            print(obj1);
            print();
            print("Object 1 Debug string : ");
            print(obj1.toShortDebugString());
            print();

            if(!obj1.getType().isExpression()){
                continue;
            }
            print("Evaluating expression before assigning variables : ");
            try {
                print(obj1.getExpressionObject().evaluateExpression());
            }
            catch (UnassignedVariableException e){
                print(e.toString());
            }
            String[] variables = MiscFunctions.toStringArray(obj1.getExpressionVariableContainer().getExpressionVariableNames());
            JObject newValues = JObject.createEmptyDictionary(variables);
            for(String var : variables){
                newValues.setValue(var, 1);
            }
            print();
            print("assigning values to variables : ");
            print(newValues);
            print();
            print("post value assignment : ");
            obj1.getExpressionVariableContainer().setExpressionVariableValues(newValues);
            print("whole object : ");
            print(obj1);
            print();
            print("variable container : ");
            print(obj1.getExpressionVariableContainer().toString());
            print();
            print("Object 1 Debug post assignment string : ");
            print(obj1.toShortDebugString());
            print();
            print("Evaluating expression after assigning variables : ");
            JObject output = null;
            try {
                output = obj1.getExpressionObject().evaluateExpression();
                print("Evaluation complete");
                print();
                print("output : ");
                print(output);
                print();
                print("Debug string");
                print(output.toShortDebugString());
            }
            catch (UnassignedVariableException e){
                print(e.toString());
            }
        }
    }

    static void jsonTest(){
        String[] testValues = {
                "[{\"customerList\":[{\"firstName\":\"Chloe\",\"lastName\":\"Alex\",\"id\":11,\"name\":\"Chloe Alex\",\"_links\":{\"self\":{\"href\":\"http://localhost:54853/customers/11\"},\"customers\":{\"href\":\"http://localhost:54853/customers\"}}}]},{\"first\":{\"href\":\"http://localhost:54853/customers?page=0&size=1&sort=lastName,asc\"},\"self\":{\"href\":\"http://localhost:54853/customers?page=0&size=1&sort=lastName,asc\"},\"next\":{\"href\":\"http://localhost:54853/customers?page=1&size=1&sort=lastName,asc\"},\"last\":{\"href\":\"http://localhost:54853/customers?page=155&size=1&sort=lastName,asc\"}},{\"size\":1,\"totalElements\":156,\"totalPages\":156,\"number\":0}]"
        };

        for(String testValue : testValues){
            print();
            print("New test");
            print();
            print("test value: ");
            print();
            print(testValue);
            JSONArray jsonArr = new JSONArray(testValue);
            print();
            print("json array: ");
            print();
            print(jsonArr);
            print();
            print("j object construction : ");
            print();
            JObject jObject = new JObject(jsonArr);
            print(jObject.toIndentedString());
            print();
            print(jObject.toDebugString());
        }
    }

    public static void main(String[] args) {

        MiscFunctions.setDebugMode(false);

        //simpleObjectConstructionTests();
        //complexObjectConstructionTest();
        //addTest();
        //cloneTests();
        //duplicateTests();
        // sortTests();
        // containsTests();
        //keyRemovalTest();
        // equalsTests();
        // toArrayTests();
        // prependTests();
        // appendTests();
        //popTest();
        // setAndListConversionTests();
        // addTests();
        //methodChainingTests();
        //combineTests();
        //predicateTest();
        //hashTests();
        //expressionTests();
        //jsonTest();
        containsKeyTest();

        MiscFunctions.setDebugMode(false);
    }
}