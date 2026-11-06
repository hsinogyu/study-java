package reflection;

import reflection.data.BasicData;

import java.lang.reflect.Method;

public class MethodV1 {

    public static void main(String[] args) {
        Class<BasicData> helloClass = BasicData.class;

        System.out.println("==== methods() ====");
        Method[] methods = helloClass.getMethods(); // 메서드 정보 추출
        for (Method method : methods) {
            System.out.println("method = " + method); // public 메서드만 보임(부모 메서드 포함)
        }

        System.out.println("==== declaredMethods() ====");
        Method[] declaredMethods = helloClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            System.out.println("declaredMethod = " + declaredMethod); // `내`가 선언한 모든 메서드만
        }
    }
}
