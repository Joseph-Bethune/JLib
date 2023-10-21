package test;

import jLibrary.exceptions.FailedNumericParseException;
import jLibrary.JNumber;

import java.util.Scanner;
import java.util.function.Predicate;

import static jLibrary.miscFunctions.MiscFunctions.*;

public class JNumberTests {

    private static Number[] generateTestNumbers(){
        return new Number[]{
                1.5, 1.5f,
                5L, 5, 0,
                (byte)5, (short)5,
                Byte.MAX_VALUE, Integer.MAX_VALUE, Long.MAX_VALUE
        };
    }

    private static void cloneTests() {
        JNumber val1 = new JNumber("10");
        JNumber val2 = val1.clone();
        val2.setTo("5");

        print(val1);
        print(val2);
    }

    private static void castingTests(){
        JNumber num = new JNumber(122D);
        print(num.toDebugString());
        num.shrinkToSmallestDataType();
        print(num.toDebugString());
    }

    private static void shrinkToTests(){
        print("Type shrinking tests");
        JNumber[] nums = new JNumber[]{
                new JNumber(1.1), new JNumber(1),
                new JNumber(5L), new JNumber(5.5),
                new JNumber(.00009), new JNumber(.99),
                new JNumber(5d), new JNumber(Long.MAX_VALUE + .05)
        };
        for(JNumber num : nums) {
            print();
            print(num.toDebugString());
            num.shrinkToSmallestDataType();
            print(num.toDebugString());
        }
    }

    private static void leastCommonCastTests(){
        JNumber[] nums1 = new JNumber[]{
                new JNumber(1.1), new JNumber(1),
                new JNumber(5L), new JNumber(5.5),
                new JNumber(.00009), new JNumber(.99),
                new JNumber(5d), new JNumber(Long.MAX_VALUE + .05)
        };
        JNumber[] nums2 = new JNumber[]{
                new JNumber(1.1), new JNumber(1),
                new JNumber(5L), new JNumber(5.5),
                new JNumber(.00009), new JNumber(.99),
                new JNumber(5d), new JNumber(Long.MAX_VALUE + .05)
        };
        for(JNumber num1 : nums1) {
            for(JNumber num2: nums2){
                print();
                print(num1.toDebugString());
                print(num2.toDebugString());
                print(num1.getSmallestCommonCastType(num2).name());
            }
        }
    }

    private static void addTests(){
        Number[] nums1 = new Number[]{
                1.5, 1.5f,
                5L, 5,
                (byte)5, (short)5,
                Byte.MAX_VALUE, Integer.MAX_VALUE, Long.MAX_VALUE
        };

        //nums1 = new Number[]{1.5f, 5};

        for(Number num_1 : nums1){
            for(Number num_2 : nums1){
                JNumber num1 = new JNumber(num_1);
                JNumber num2 = new JNumber(num_2);
                print();
                print("_____New Add test_____");
                print();
                print("Numbers to add : ");
                print(num1.toDebugString());
                print(num2.toDebugString());
                print();
                JNumber output1 = JNumber.add(num1, num2);
                print("Static Result : ");
                print(output1.toDebugString());
                print("Numbers post adding : ");
                print(num1.toDebugString());
                print(num2.toDebugString());
                assert (num1.equals(num_1));
                assert (num2.equals(num_2));
                print();
                JNumber output2 = (JNumber) num1.add(num2);
                print("Instance result : ");
                print(output2.toDebugString());
                print("Numbers post adding : ");
                print(num1.toDebugString());
                print(num2.toDebugString());
                assert (num1.equals(num_1));
                assert (num2.equals(num_2));
            }
        }
    }

    private static void subtractTests(){
        Number[] nums1 = new Number[]{
                1.5, 1.5f,
                5L, 5,
                (byte)5, (short)5,
                Byte.MAX_VALUE, Integer.MAX_VALUE, Long.MAX_VALUE
        };

        //nums1 = new Number[]{1.5f, 5};

        for(Number num_1 : nums1){
            for(Number num_2 : nums1){
                JNumber num1 = new JNumber(num_1);
                JNumber num2 = new JNumber(num_2);
                print();
                print("_____New Subtract test_____");
                print();
                print("Numbers to add : ");
                print(num1.toDebugString());
                print(num2.toDebugString());
                print();
                JNumber output1 = JNumber.subtract(num1, num2);
                print("Static Result : ");
                print(output1.toDebugString());
                print("Numbers post adding : ");
                print(num1.toDebugString());
                print(num2.toDebugString());
                assert (num1.equals(num_1));
                assert (num2.equals(num_2));
                print();
                JNumber output2 = new JNumber(num1.subtract(num2));
                print("Instance result : ");
                print(output2.toDebugString());
                print("Numbers post adding : ");
                print(num1.toDebugString());
                print(num2.toDebugString());
                assert (num1.equals(num_1));
                assert (num2.equals(num_2));
            }
        }
    }

    private static void multiplyTests(){
        Number[] nums1 = generateTestNumbers();

        for(Number num_1 : nums1){
            for(Number num_2 : nums1){
                JNumber num1 = new JNumber(num_1);
                JNumber num2 = new JNumber(num_2);
                print();
                print("_____New Multiply test_____");
                print();
                print("Numbers to add : ");
                print(num1.toDebugString());
                print(num2.toDebugString());
                print();
                JNumber output1 = JNumber.multiply(num1, num2);
                print("Static Result : ");
                print(output1.toDebugString());
                print("Numbers post adding : ");
                print(num1.toDebugString());
                print(num2.toDebugString());
                assert (num1.equals(num_1));
                assert (num2.equals(num_2));
                print();
                JNumber output2 = new JNumber(num1.multiply(num2));
                print("Instance result : ");
                print(output2.toDebugString());
                print("Numbers post adding : ");
                print(num1.toDebugString());
                print(num2.toDebugString());
                assert (num1.equals(num_1));
                assert (num2.equals(num_2));
            }
        }
    }

    private static void divideTests(){
        Number[] nums1 = generateTestNumbers();

        for(Number num_1 : nums1){
            for(Number num_2 : nums1){
                JNumber num1 = new JNumber(num_1);
                JNumber num2 = new JNumber(num_2);
                print();
                print("_____New Divide test_____");
                print();
                print("Numbers for operation : ");
                print(num1.toDebugString());
                print(num2.toDebugString());
                print();
                try {
                    JNumber output1 = JNumber.divide(num1, num2);
                    print("Static Result : ");
                    print(output1.toDebugString());
                    print("Numbers post operation : ");
                    print(num1.toDebugString());
                    print(num2.toDebugString());
                    assert (num1.equals(num_1));
                    assert (num2.equals(num_2));
                    print();
                    JNumber output2 = new JNumber(num1.divide(num2));
                    print("Instance result : ");
                    print(output2.toDebugString());
                    print("Numbers post operation : ");
                    print(num1.toDebugString());
                    print(num2.toDebugString());
                    assert (num1.equals(num_1));
                    assert (num2.equals(num_2));
                }
                catch (Exception e){
                    print(e);
                    print(e.getStackTrace());
                }
            }
        }
    }

    public void test_1(){
        Scanner scanner = new Scanner(System.in);
        String input = "";
        JNumber newNumber;
        Predicate<JNumber> isWholeNumber = jNumber -> !jNumber.hasDecimalComponent();
        while (!input.equalsIgnoreCase("end") && !input.equalsIgnoreCase("exit")){
            print("Enter \"end\" to exit loop.");
            print("Enter a number value.");
            input = scanner.nextLine();
            print("You entered \"" + input + "\".");
            try {
                newNumber = new JNumber(input);
                if (isWholeNumber.test(newNumber)) {
                    print("Has decimal component.");
                } else {
                    print("No decimal component.");
                }

                print(newNumber.toDebugString());
            }
            catch (FailedNumericParseException e){
                print("Couldn't convert string into a number.");
            }
            catch (Exception e){
                print("Something went wrong.");
            }
            print();
        }
    }

    public void predicateTests(){

    }

    public static void main(String[] args){

        //cloneTests();
        //castingTests();
        //shrinkToTests();
        //leastCommonCastTests();
        //addTests();
        //subtractTests();
        //multiplyTests();
        divideTests();
    }
}
