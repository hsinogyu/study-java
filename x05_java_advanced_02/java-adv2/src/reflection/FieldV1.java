package reflection;

import reflection.data.BasicData;

import java.lang.reflect.Field;

public class FieldV1 {

    public static void main(String[] args) {
        Class<BasicData> helloClass = BasicData.class;

        System.out.println("==== fields() ====");
        for (Field field : helloClass.getFields()) {
            System.out.println("field = " + field); // public 필드만 보여줌(부모 상속 포함)
        }

        System.out.println("==== declaredFields() ====");
        for (Field declaredField : helloClass.getDeclaredFields()) {
            System.out.println("declaredField = " + declaredField); // 내가 선언한 필드만 보여줌
        }
    }
}
