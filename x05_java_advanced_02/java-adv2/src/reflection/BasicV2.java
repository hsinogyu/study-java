package reflection;

import reflection.data.BasicData;

import java.lang.reflect.Modifier;
import java.util.Arrays;

public class BasicV2 {

    public static void main(String[] args) {
        Class<BasicData> basicData = BasicData.class;

        System.out.println("basicData.getName() = " + basicData.getName());
        System.out.println("basicData.getSimpleName() = " + basicData.getSimpleName());
        System.out.println("basicData.getPackage() = " + basicData.getPackage());
        System.out.println("basicData.getPackageName() = " + basicData.getPackageName());

        System.out.println("basicData.getSuperclass() = " + basicData.getSuperclass());
        System.out.println("Arrays.toString(basicData.getInterfaces()) = " + Arrays.toString(basicData.getInterfaces()));

        System.out.println("basicData.isInterface() = " + basicData.isInterface()); // false
        System.out.println("basicData.isEnum() = " + basicData.isEnum()); // false
        System.out.println("basicData.isAnnotation() = " + basicData.isAnnotation()); // false

        System.out.println("basicData.getModifiers() = " + basicData.getModifiers());
        System.out.println("isPublic = " + Modifier.isPublic(basicData.getModifiers()));
        System.out.println("Modifier.toString() = " + Modifier.toString(basicData.getModifiers()));
    }
}
