package annotation.mapping;

import java.lang.reflect.Method;

public class TestControllerMain {

    public static void main(String[] args) {
        TestController testController = new TestController();

        Class<? extends TestController> aClass = testController.getClass();
        Method[] methods = aClass.getDeclaredMethods();
        for (Method method : methods) {
            SimpleMapping simpleMapping = method.getAnnotation(SimpleMapping.class); // 메서드, 클래스, 필드 등에 붙어있는 애노테이션을 찾아올 수 있음
            if (simpleMapping != null) {
                System.out.println("[" + simpleMapping.value() + "] -> " + method);
            }
        }
    }
}
