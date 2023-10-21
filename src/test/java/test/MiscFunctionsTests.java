package test;

import jLibrary.miscFunctions.MiscFunctions;

import static jLibrary.miscFunctions.MiscFunctions.*;
import static jLibrary.miscFunctions.MiscFunctions.print;

public class MiscFunctionsTests {

    static void stringBuilderTrimTest(){
        StringBuilder[] testValues = new StringBuilder[]{
                new StringBuilder("      "),
                new StringBuilder("   trim value   "),
                new StringBuilder("     trim value "),
                new StringBuilder(" trim value     ")
        };

        for(StringBuilder testValue : testValues){
            print("New Test");
            print("Test Value : ");
            print(testValue);
            print();

            StringBuilder output = trim(testValue);

            print("input : ");
            print("\""+testValue+"\"");
            print("Output : ");
            print("\""+output+"\"");
            print();
        }
    }

    static void stringBuilderConcatStringTests(){
        StringBuilder[] testValues = new StringBuilder[]{
                new StringBuilder("value1"),
                new StringBuilder("value2"),
                new StringBuilder("value2")
        };

        for(StringBuilder testValue : testValues){
            print("New Test");
            print("Test Value : ");
            print(testValue);
            print();

            StringBuilder output = concatenateStrings(testValues, ", ", null, null);

            print("Output : ");
            print(output);
            print();
        }
    }

    static void stringBuilderMatchTests(){
        StringBuilder[] set1 = new StringBuilder[]{
                new StringBuilder("blarg"),
                new StringBuilder("blarg")
        };
        StringBuilder[] set2 = new StringBuilder[]{
                new StringBuilder("blarg"),
                new StringBuilder("BLARG")
        };

        print("String Builder Match tests");
        for(int flag = 0; flag < 2; flag += 1) {
            for (StringBuilder s1 : set1) {
                for (StringBuilder s2 : set2) {
                    boolean ignoreCase = flag == 1;
                    print();
                    print("SB 1 : " + s1);
                    print("SB 2 : " + s2);
                    print("Ignore case : " + ignoreCase);
                    //print("Match : " + stringBuildersEquals(s1, s2, ignoreCase));
                }
            }
        }
    }

    static void stringBuilderSubstringTest(){
        StringBuilder test = new StringBuilder("0123456789");

        print(stringBuilderSubstring(test, 1, 10));
    }

    static void startsWithEndsWithTest(){

        print("starts with and ends with tests for string builders");

        StringBuilder[] wholeStrings = new StringBuilder[]{
                new StringBuilder("<0>blarg<1>"),
                new StringBuilder("<1>blarg<0>")
        };
        StringBuilder[] openings = new StringBuilder[]{
                new StringBuilder("<0>")
        };
        StringBuilder[] closings = new StringBuilder[]{
                new StringBuilder("<1>")
        };

        for(StringBuilder wholeString : wholeStrings){
            print();
            print("Whole string");
            print(wholeString);
            print();

            for(int x = 0; x < openings.length; x += 1){
                print("opening");
                print(openings[x]);
                print("starts with opening");
                print(startsWith(wholeString, openings[x]));
                print();
                print("closing");
                print(closings[x]);
                print("ends with closing");
                print(endsWith(wholeString, closings[x]));
                print();
            }
        }

    }

    static void stringBuilderReplaceStringTest(){
        StringBuilder[] wholeStrings = new StringBuilder[]{
                new StringBuilder("<0>blarg<1>"),
                new StringBuilder("<1>blarg<0>")
        };
        StringBuilder[] subs = new StringBuilder[]{
                new StringBuilder("<0>blarg<1>"),
                new StringBuilder("<1>blarg<0>"),
                new StringBuilder("blarg"),
                new StringBuilder("<0>"),
                new StringBuilder("<1>")
        };
        StringBuilder newString = new StringBuilder("newVal");

        for(StringBuilder ws : wholeStrings){
            for(StringBuilder sub : subs){
                StringBuilder output = replaceFirstOccurrence(ws, sub, newString);

                print();
                print("New Test");
                print();
                print("Whole String");
                print(ws);
                print();
                print("String to replace");
                print(sub);
                print();
                print("replacement string");
                print(newString);
                print();
                print("Output");
                print(output);
            }
        }
    }

    static void stringBuilderSubstringCountTest(){
        StringBuilder[] wholeStrings = new StringBuilder[]{
                new StringBuilder("<0>blarg<1>"),
                new StringBuilder("<1>blarg<0>")
        };
        StringBuilder[] subs = new StringBuilder[]{
                new StringBuilder("<0>blarg<1>"),
                new StringBuilder("<1>blarg<0>"),
                new StringBuilder("blarg"),
                new StringBuilder("<0>"),
                new StringBuilder("<1>"),
                new StringBuilder("<"),
                new StringBuilder(">")
        };

        for(StringBuilder ws : wholeStrings){
            for(StringBuilder sub : subs){
                int count = getSubstringCount(ws, sub);

                print();
                print("New Test");
                print();
                print("Whole String");
                print(ws);
                print();
                print("String to search for");
                print(sub);
                print();
                print("count");
                print(count);
            }
        }
    }

    static void containsTests(){
        StringBuilder[] wholeStrings = new StringBuilder[]{
                new StringBuilder("<0>blarg<1>"),
                new StringBuilder("<1>blarg<0>")
        };
        StringBuilder[] subs = new StringBuilder[]{
                new StringBuilder("<0>blarg<1>"),
                new StringBuilder("<1>blarg<0>"),
                new StringBuilder("blarg"),
                new StringBuilder("<0>"),
                new StringBuilder("<1>"),
                new StringBuilder("<"),
                new StringBuilder(">")
        };

        print();
        print("Contains tests");

        for(StringBuilder ws : wholeStrings){
            for(StringBuilder sub : subs){
                boolean output = doesStringContain(ws, sub);

                print();
                print("New Test");
                print();
                print("Whole String");
                print(ws);
                print();
                print("String to search for");
                print(sub);
                print();
                print("output");
                print(output);
            }
        }
    }

    public static void main(String[] args) {

        MiscFunctions.setDebugMode(true);

        //stringBuilderTrimTest();
        // stringBuilderConcatStringTests();
        //stringBuilderMatchTests();
        //stringBuilderSubstringTest();
        //startsWithEndsWithTest();
        //stringBuilderReplaceStringTest();
        //stringBuilderSubstringCountTest();
        //containsTests();

        MiscFunctions.setDebugMode(false);
    }
}
