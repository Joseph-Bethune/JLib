package test;

import jLibrary.JObject;
import jLibrary.miscFunctions.MiscFunctions;
import jLibrary.nestedStringTree.NestedStringTree_Brackets;

import static jLibrary.miscFunctions.MiscFunctions.*;

public class JSONTests {
    public static void jsonTest(){
        String[] testValues = new String[]{
                //"5",
                //"5.5",
                //"false",
                //"string",
                //"null",
                //"[0,1,2,3,4,5]",
                //"(0,1,2,3,4,5)",
                //"((0,1,2,3,4,5),(6,7,8,9,10,11))",
                //"[5,false, bob]",
                //"(5, false, bob)",
                //"[0,1,2,3,4,5]",
                //"{num:5,boolean:true,string:bob,list:[52, true, dill], set:(52, true, dill)}",
                //"{"+JObject.ParameterName_ClassName+":newClass,num:5,boolean:true,string:bob,list:[52, true, dill], set:(52, true, dill)}",
                "[{"+JObject.ParameterName_ClassName+":newClass,num:5,boolean:true,string:bob,list:[52, true, dill], set:(52, true, dill)}," +
                        "{"+JObject.ParameterName_ClassName+":newClass,num:5,boolean:true,string:ross,list:[52, true, dill], set:(52, true, dill)}]"
        };
        for(String testValue : testValues){
            print();
            print("______NEW TEST VALUE______");
            print("Test value");
            print(testValue);

            JObject jObject = new JObject(testValue);
            JObject output = null;
            print();
            print("JObject");
            print(jObject);
            if(false) {
                print();
                print("debug mode");
                print(jObject.toDebugString());
            }

            Object json = jObject.toJSONElement();
            print();
            print("JSON element");
            print(json);

            json = jObject.toJSONObject();
            print();
            print("JSON object");
            print(json);

            output = new JObject(json);
            print();
            print("JObject post conversion");
            print(output);
        }
    }

    public static void treeTest(){
        String s = "[5,false, bob]";
        NestedStringTree_Brackets tree = new NestedStringTree_Brackets(s);
        print(tree.toDebugString());
        print();
        print(tree.rebuildString());
    }

    public static void main(String[] args){
        MiscFunctions.setDebugMode(false);
        jsonTest();
        //treeTest();
        MiscFunctions.setDebugMode(false);
    }
}
