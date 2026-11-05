# 애노테이션

## 애노테이션이 필요한 이유

### 남은 문제점
- 리플렉션 서블릿은 요청 URL과 메서드 이름이 같다면 해당 메서드를 동적으로 호출할 수 있음
  - 하지만 요청 이름과 메서드 이름을 다르게 하고 싶다면 어떻게 해야할까?
- 앞서 `/`와 같이 자바 메서드 이름으로 처리하기 어려운 URL은 어떻게 해결할 수 있을까?
- URL은 주로 `-`를 구분자로 사용하는데, `/add-member`와 같은 URL은 어떻게 해결할 수 있을까?

### 해결 방안 제안
- 주석으로 `// /site1`과 같이 메서드 위에 적어놓고 프로그램 실행 중에 이를 읽고 처리할 수 있다면?
- 주석은 컴파일 시점에 모두 제거되기 때문에, 주석을 사용할 수는 없음
- 이를 도와주는 것이 애노테이션

### 애노테이션 예제
```java
package annotation.mapping;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SimpleMapping {

    String value();
}
```
- 애노테이션은 `@interface` 키워드를 사용해서 만듦
- `@SimpleMapping`이라는 애노테이션을 하나 만들고, 내부에는 `String value`라는 속성을 하나 가짐
- `@Retention`은 뒤에서 설명(지금은 필수로 사용해야 하는 값 정도로 생각)

### 애노테이션 사용
```java
public class TestController {

    @SimpleMapping(value = "/")
    public void home() {
        System.out.println("TestController.home");
    }

    @SimpleMapping(value = "/site1")
    public void page1() {
        System.out.println("TestController.page1");
    }
}
```
- 애노테이션을 사용할 때는 `@`기호로 시작함
- 참고
  - 애노테이션은 프로그램 코드가 아님
  - home(), page1() 같은 코드를 호출해도 프로그램에는 아무런 영향을 주지 않음(주석과 비슷)
  - 일반적인 주석이 아니라 리플렉션 같은 기술로 실행 시점에 읽어서 활용할 수 있는 특별한 주석

### 리플렉션으로 애노테이션 읽기
```java
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
```
- TestController 클래스에 선언된 메서드를 찾음
- 리플렉션이 제공하는 `getAnnotation()`을 사용해서 붙어있는 애노테이션을 찾을 수 있음
  - Class, Method, Field, Constructor 클래스는 자신에게 붙은 애노테이션을 찾을 수 있는 `getAnnotation()`을 제공함
- 여기서는 Method.getAnnotation(SimpleMapping.class)를 사용해서 해당 메서드에 붙은 @SimpleMapping 애노테이션을 찾을 수 있음
- simpleMapping.value()를 사용해서 찾은 애노테이션에 지정된 값을 조회할 수 있음
  - 여기 value를 애노테이션에 선언한 value를 의미함

---
## 애노테이션 정의
