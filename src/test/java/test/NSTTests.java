package test;

import jLibrary.JString;
import jLibrary.miscFunctions.MiscFunctions;
import jLibrary.nestedStringTree.NestedStringTree_Brackets;

import static jLibrary.miscFunctions.MiscFunctions.*;

public class NSTTests {

    static void treeConstructionTests(){
        String[] inputs = new String[]{
                "",
                "{{hi}{help}}",
                "{{hi, help}{1,2,3}}",
                "{\"bob \", \" name\"}",
                "bob,alex,hope"
        };

        print();
        print("Tree construction tests");
        for(String input : inputs){
            print();
            print("input string : ");
            print(input);

            NestedStringTree_Brackets tree = new NestedStringTree_Brackets(input);

            if(tree.isTreeBuilt()){
                print();
                print("reconstructed string");
                print(tree.rebuildString());
                print();
                print("opening brackets : ");
                print(JString.concatenateStrings(tree.getOpeningBrackets(), " ", null, null));
                print("closing brackets : ");
                print(JString.concatenateStrings(tree.getClosingBrackets(), " ", null, null));
                print("leaf debug strings: ");
                for(NestedStringTree_Brackets.Leaf leaf : tree.getAllLeafs()) {
                    print();
                    print(leaf.toDebugString());
                }
                print();
                print("tree debug string : ");
                print();
                print(tree.toDebugString());
            }
            else {
                print();
                print("construction error : ");
                print(tree.getConstructionErrorDescription());
            }
        }
    }

    public static void main(String[] args){
        //MiscFunctions.setDebugMode(true);
        treeConstructionTests();
        MiscFunctions.setDebugMode(false);
    }
}
