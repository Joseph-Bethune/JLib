package test;

import jLibrary.JObject;
import jLibrary.miscFunctions.MiscFunctions;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static jLibrary.miscFunctions.MiscFunctions.print;

public class ReflectTests {

    private static Field[] getAllFields(Object targetInstance){
        Class<?> targetClass = targetInstance.getClass();

        Field[] declaredFields = targetClass.getDeclaredFields(); // excludes inherited fields
        Field[] otherFields = targetClass.getFields(); // excludes private or protected fields

        ArrayList<Field> allFields = new ArrayList<>(List.of(declaredFields));
        for(Field f : otherFields){
            boolean alreadyPresent = false;
            for(int x = 0; x < allFields.size() && !alreadyPresent; x += 1){
                Field t = allFields.get(x);
                if(t.getName().equals(f.getName())){
                    alreadyPresent = true;
                }
            }

            if(!alreadyPresent){
                allFields.add(f);
            }
        }

        return allFields.toArray(new Field[0]);
    }

    private static Method[] getAllMethods(Object targetInstance){
        Class<?> targetClass = targetInstance.getClass();

        Method[] declaredMethods = targetClass.getDeclaredMethods();
        Method[] otherMethods = targetClass.getMethods();

        ArrayList<Method> allMethods = new ArrayList<>(List.of(declaredMethods));
        for(Method f : otherMethods){
            boolean alreadyPresent = false;
            for(int x = 0; x < allMethods.size() && !alreadyPresent; x += 1){
                Method t = allMethods.get(x);
                if(t.getName().equals(f.getName())){
                    alreadyPresent = true;
                }
            }

            if(!alreadyPresent){
                allMethods.add(f);
            }
        }

        return allMethods.toArray(new Method[0]);
    }

    private static void displayFields(Field[] elements, Object instanceObj){
        print();
        print("Fields");
        for(Field ele : elements) {
            print();
            print("field name : " + ele.getName());
            print("field type : " + ele.getType().getName());
            print("modifiers : " + ele.getModifiers());
            if (Modifier.isPublic(ele.getModifiers())) {
                print("public");
            } else if (Modifier.isProtected(ele.getModifiers())) {
                print("protected");
            } else if (Modifier.isPrivate(ele.getModifiers())) {
                print("private");
            }
            if (Modifier.isStatic(ele.getModifiers())) {
                print("static");
            }
            if (Modifier.isFinal(ele.getModifiers())) {
                print("final");
            }
            try {
                print("field value : " + ele.get(instanceObj));
            } catch (Exception e) {
                print(e);
            }
            print(ele);
        }
    }

    private static void displayMethods(Method[] elements, Object instanceObj){
        print();
        print("Methods");
        for(Method ele : elements) {
            print();
            print("method name : " + ele.getName());
            print("method return type : " + ele.getReturnType().getName());
            print("modifiers : " + ele.getModifiers());
            if (Modifier.isPublic(ele.getModifiers())) {
                print("public");
            } else if (Modifier.isProtected(ele.getModifiers())) {
                print("protected");
            } else if (Modifier.isPrivate(ele.getModifiers())) {
                print("private");
            }
            if (Modifier.isStatic(ele.getModifiers())) {
                print("static");
            }
            if (Modifier.isFinal(ele.getModifiers())) {
                print("final");
            }
            try {
                print("default value : " + ele.getDefaultValue());
            } catch (Exception e) {
                print(e);
            }

            if(ele.getParameterCount() == 0) {
                try {
                    print(
                        "current value : " +
                        Class.forName(instanceObj.getClass().getName()).getMethod(ele.getName(), ele.getParameterTypes()).invoke(instanceObj, new Object[0])
                    );
                } catch (Exception e) {
                    print(e);
                }
            }
            print(ele);
        }
    }

    static void reflectTest(){
        JObject instanceObj = new JObject();
        Class<?> temp = instanceObj.getClass();
        print();
        print("package name : " + temp.getPackageName());
        print("class name : " + temp.getSimpleName());
        //print(temp.getName());
        //print(temp.getTypeName());
        //print(temp.getCanonicalName());
        if(temp.getSuperclass() == Object.class){
            print("no super class");
        }
        else{
            print("super class : " + temp.getSuperclass().getName());
        }

        if(false){
            displayFields(getAllFields(instanceObj), instanceObj);
        }

        if(false){
            displayMethods(getAllMethods(instanceObj), instanceObj);
        }

        if(true){
            Method[] otherMethods = null;

            ArrayList<Method> allMethods = new ArrayList<>();
            otherMethods = getAllMethods(instanceObj);

            for(Method f : otherMethods){
                if(f.getName().startsWith("get") || f.getName().startsWith("set") || f.getName().startsWith("is")){

                }
                else{
                    continue;
                }

                boolean contains = false;
                for(int x = 0; x < allMethods.size() && !contains; x += 1){
                    Method c = allMethods.get(x);
                    if(c.getName().equals(f.getName())){
                        contains = true;
                    }
                }

                if(!contains){
                    allMethods.add(f);
                }
            }

            displayMethods(allMethods.toArray(new Method[0]), instanceObj);
        }
    }

    public static void main(String[] args){
        MiscFunctions.setDebugMode(false);
        //reflectTest();
        MiscFunctions.setDebugMode(false);
    }
}
