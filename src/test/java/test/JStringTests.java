package test;

import jLibrary.JString;

import static jLibrary.miscFunctions.MiscFunctions.print;

public class JStringTests {

    private static void basicCreationTest(){
        JString string = new JString("Bob");
        System.out.println(string);
        string = new JString(new char[]{'B','o','b'});
        print(string);
        string.append('\'');
        print(string);
        string.append("s name");
        print(string);
        for(char c : string){
            print(c);
        }
    }

    private static void characterAppendTest(){
        JString string = new JString();
        int capacity = string.capacity();
        for(int x = 0; x < 100; x += 1){
            string.append(Integer.toString(x));
            print();
            print("new length : " + string.length());
            if(string.capacity() != capacity){
                capacity = string.capacity();
                print("new capacity : " + capacity);
            }
            print("new string : " + string);
        }
    }

    private static void compareToTest(){
        JString s1 = new JString("aaa");
        JString s2 = new JString("bbb");
        System.out.println(s1.compareTo(s2));

        String s3 = "aaa";
        String s4 = "bbb";
        System.out.println(s1.compareTo(s3));
    }

    private static void toUpperToLowerTest(){
        JString string = new JString("abcedefhijklmnopqrstuvwxyz");
        print(string);
        string.toUpperCase();
        print(string);
        string.toLowerCase();
        print(string.append(string.toUpperCase()).toLowerCase());
    }

    private static void forEachLambdaTest(){
        JString string = new JString("abcedefhijklmnopqrstuvwxyz");
        string.forEach(n ->{print(n);});
    }

    private static void searchTests(){
        JString string = new JString("abcedefhijklmnopqrstuvwxyz");
        String[] searchValues = new String[]{"n", "nop", "tuv", "var", "z", "a"};
        print(string);
        for(String s : searchValues){
            print("search value :"+s);
            print("index of : "+string.indexOf(s));
        }
    }

    private static void replacementTest(){
        JString string = new JString("The name of bob is bob.");
        JString output = string.clone().replaceEach(" bob", " Alex");
        print("Replacement test");
        print("input: ");
        print(string);
        print("output : ");
        print(output);
    }

    private static void trimTest(){
        JString[] strings = new JString[]{
                new JString("   bob   "),
                new JString("bob   "),
                new JString("    bob")};
        print("trim test");
        for(JString string : strings){
            print("original : ");
            print(string);
            print(string.toDebugString());
            print("output : ");
            print(string.trim());
            print(string.toDebugString());
        }
    }

    private static void startsWithEndsWithTest(){

        JString[] strings = new JString[]{
                new JString("bbaaabb"),
                new JString("<<123>>")
        };

        JString[] startStrings = new JString[]{
                new JString("bba"),
                new JString("<<")
        };
        JString[] endStrings = new JString[]{
                new JString("abb"),
                new JString(">>")
        };
        print("starts with and ends with test");
        for(JString string : strings){
            for(JString start : startStrings){
                print();
                print("whole string : " + string);
                print("start string : " + start);
                print("starts with : " + string.startsWith(start));
            }

            for(JString var : endStrings){
                print();
                print("whole string : " + string);
                print("end string : " + var);
                print("ends with : " + string.endsWith(var));
            }
        }
    }

    private static void lastIndexOfTests(){
        JString[] strings = new JString[]{
                new JString("<<123>>")
        };
        JString[] searches = new JString[]{
                new JString("a"),
                new JString("<<"),
                new JString(">>"),
                new JString(">"),
                new JString("23")
        };

        for(JString string : strings) {
            for (JString search : searches) {
                print();
                print("whole string : " + string);
                print("search string : " + search);
                print("last index of : " + string.lastIndexOf(search));
            }
        }
    }

    private static void splitTest_usingJStrings(){
        JString[] testStrings = new JString[]{
                new JString("Robert"),
                new JString("Robert Robert"),
                new JString("\"Robert\" \"Robert\""),
                new JString("\"Word   1\"   \"Word   2\""),
                new JString("post \"{name:bob hope, role: main healer}\""),
                new JString("post {name:bob hope, role: main healer}")
        };

        print("Split test");
        for(JString testString : testStrings){
            print();
            print("whole string : ");
            print(testString.toString(false));
            JString[] subStrings = testString.clone().splitStringOnSpacesButConcatenateBrackets();
            //JString[] subStrings = JString.splitStringOnSpacesButConcatenateBrackets(null);
            print("sub string list root object : ");
            print(subStrings);
            print();
            int counter = 0;
            for(JString sub : subStrings){
                print("string element " + (counter+1) + ": ");
                print(sub.toString());
                print(sub.toDebugString());
                counter += 1;
            }
        }
    }

    private static void insertTest(){
        JString[] strings = new JString[]{
                new JString("Robert")
        };
        JString newString = new JString("1");

        print("insert test");
        for(JString string : strings){
            for(int x = 0; x <= string.length(); x += 1){
                print();
                print("whole string : ");
                print(string);
                print("insertion index : ");
                print(x);
                JString output = string.clone().insertIntoString(x, newString);
                print("output : ");
                print(output);
                print(output.toDebugString());
            }
        }
    }

    private static void removeTest(){
        JString[] strings = new JString[]{
                new JString("Alexander")
        };

        print("remove test");
        for(JString string : strings){
            for(int x = 0; x < string.length(); x += 1){
                for(int y = x; y <= string.length(); y += 1) {
                    print();
                    print("whole string : ");
                    print(string);
                    print("removal start point : ");
                    print(x);
                    print("removal end point : ");
                    print(y);
                    JString output = string.clone();
                    JString substring = output.remove_getRemovedSubstring(x, y);
                    print("sub string removed : ");
                    print(substring);
                    print("output : ");
                    print(output);
                    print(output.toDebugString());
                }
            }
        }
    }

    private static void subStringTest(){
        JString string = new JString("Alexander");
        JString output = string.subString(4, string.length());
        print(string);
        print(output);
    }

    private static void substringCountTest(){
        JString[] strings = new JString[]{
                new JString("bobbobbob")
        };
        JString[] searchStrings = new JString[]{
                new JString("bob")
        };
        print("substring count test");
        for(JString string : strings){
            for(JString searchValue : searchStrings){
                int[] indices = string.indexOfEach(searchValue);
                print();
                print("whole string : ");
                print(string);
                print("search string");
                print(searchValue);
                print("count : ");
                print(indices.length);
                print("indices : ");
                print(indices);
            }
        }
    }

    private static void bracketTest(){
        JString[] strings = new JString[]{
                new JString("<010><010>55555<011><010>55555<011><011>"),
                new JString("1<010><010>55555<011><010>55555<011><011>"),
                new JString("{{55555}{55555}}"),
                new JString("\"\"Bob\"\"Bob\"\"")
        };
        String[][] bracketPairs = new String[][]{
                new String[]{"<010>", "<011>"},
                new String[]{"{", "}"},
                new String[]{"\"", "\""}
        };

        int[] openings = null;
        int[] closings = null;
        print("bracket test");
        int output = 0;

        for(JString string : strings){
            for(String[] bracketPair : bracketPairs){
                String open = bracketPair[0];
                String close = bracketPair[1];
                print();
                print("whole string : ");
                print(string);
                print("opening bracket : ");
                print(open);
                print("closing bracket: ");
                print(close);
                print("encapsulated by brackets : ");
                print(string.doMatchedBracketsEncapsulateEntireString(open, close));
                openings = string.indexOfEach(open);
                closings = string.indexOfEach(close);

                if(true) {
                    print("opening bracket locations : ");
                    print(openings);
                    print("closing bracket locations : ");
                    print(closings);
                }

                for(int location : openings) {
                    print("location : ");
                    print(location);
                    print("output : ");
                    output = string.getIndexOfMatchingBracket(open, close, location);
                    print(output);
                }

                for(int location : closings){
                    print("location : ");
                    print(location);
                    print("output : ");
                    output = string.getIndexOfMatchingBracket(open, close, location);
                    print(output);
                }
            }
        }
    }

    private static void otherTest(){
        int v = (int)'\b';
        char v2 = (char)v;
        print(v);
        print(v2);

        boolean stop = true;
        for(int x = 32; x < 127 && !stop; x += 1){
            try {
                char c = (char) x;
                print();
                print(x);
                print(c);
            }
            catch (Exception e){
                stop = true;
            }
        }
    }

    private static void prependTest(){
        JString string1 = new JString("ander");
        JString string2 = new JString("Alex");
        JString string3 = string1.clone().prepend(string2);

        print(string1);
        print(string1.toDebugString());
        print(string2);
        print(string2.toDebugString());
        print(string3);
        print(string3.toDebugString());
    }

    public static void main(String[] args){

        //basicCreationTest();
        //characterAppendTest();
        //compareToTest();
        //toUpperToLowerTest();
        //forEachLambdaTest();
        //searchTests();
        //replacementTest();
        //trimTest();
        //startsWithEndsWithTest();
        //lastIndexOfTests();

        //splitTest_usingJStrings();
        //splitTest_usingStringBuilders();
        //insertTest();
        //removeTest();
        //subStringTest();
        //substringCountTest();
        //bracketTest();
        //prependTest();
    }
}
