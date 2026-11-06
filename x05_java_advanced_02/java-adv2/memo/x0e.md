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

### 애노테이션 정의 예시
```java
package annotation.basic;

import util.MyLogger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AnnoElement {

    String value();
    int count() default 0; // 속성의 기본값 설정
    String[] tags() default {};
//    MyLogger data(); // 선언할 수 없음(자바의 기본 타입만 선언할 수 있음)
    Class<? extends MyLogger> annoData() default MyLogger.class; // 클래스에 대한 메타데이터는 가능함
}
```
- 애노테이션은 @interface 키워드로 정의함
- 애노테이션은 속성을 가질 수 있는데, 인터페이스와 비슷하게 정의함

### 애노테이션 정의 규칙
- 데이터 타입
  - 기본 타입(int, float, boolean)
  - String
  - Class(메타데이터) 또는 인터페이스
  - enum
  - 다른 애노테이션 타입
  - 위의 타입들의 배열
  - 앞서 설명한 타입 외에는 정의할 수 없음(일반적인 클래스는 사용할 수 없음)

- default 값
  - 요소에 default 값을 지정할 수 있음
  - 예: String value() default "기본값을 적용합니다.";

- 요소 이름
  - 메서드 형태로 정의됨
  - 괄호()를 포함하되 매개변수는 없어야함

- 반환 값
  - void 반환 타입으로 사용할 수 없음

- 예외
  - 에외를 선언할 수 없음

- 특별한 요소 이름
  - value라는 이름의 요소를 하나만 가질 경우, 애노테이션 사용 시 요소 이름을 생략할 수 있음

### 사용
```java
package annotation.basic;

@AnnoElement(value = "data", count = 10, tags = {"t1", "t2"})
public class ElementData1 {


}
```

```java
package annotation.basic;

@AnnoElement(value = "data", tags = "t1")
// - default 값이 있는 경우 생략 가능
// - 배열의 경우 요소가 한 개일 때 {} 생략 가능
public class ElementData2 {


}
```

```java
package annotation.basic;

@AnnoElement("data")
// - 요소를 한 개만 정의할 때, 속성명을 생략할 수 있음(다른 속성들은 default 값이 있으므로)
public class ElementData3 {
}
```

```java
package annotation.basic;

import java.util.Arrays;

public class ElementData1Main {

    public static void main(String[] args) {
        Class<ElementData1> annoClass = ElementData1.class;
        AnnoElement annotation = annoClass.getAnnotation(AnnoElement.class); // 클래스 정보에서 가져왔으므로 클래스 자체에 붙어있는 애노테이션을 가져올 수 있음

        String value = annotation.value();
        System.out.println("value = " + value);

        int count = annotation.count();
        System.out.println("count = " + count);

        String[] tags = annotation.tags();
        System.out.println("tags = " + Arrays.toString(tags));
    }
}
```

---
## 메타 애노테이션

### 정의
- 애노테이션을 정의하는데 사용하는 특별한 애노테이션을 메타 애노테이션이라고 함
- 종류
  - @Retention
    - RetentionPolicy.SOURCE
    - RetentionPolicy.CLASS
    - RetentionPolicy.RUNTIME
  - @Target
  - @Documented
  - @Inherited

### @Retention
- 애노테이션의 생존 기간을 지정함
- RetentionPolicy: 어디까지 생존할거냐
  - SOURCE: 소스 코드에만 남아있다. 컴파일 시점에만 제거됨
  - CLASS: 컴파일 후 class 파일까지는 남아있지만 자바 실행 시점에 제거됨(기본 값)
  - RUNTIME: 자바 실행 중에도 남아있음. 대부분 이 설정을 사용함

### @Target
- 애노테이션을 적용할 수 있는 위치를 지정
- ElementType: 어디까지 붙일 수 있는지
  - 안쓰면 모든 타입(Class, Method 등 다 붙일 수 있음)
  - 배열이여서 여러 개 Enum을 지정할 수 있음
    - TYPE: 클래스에만
    - FIELD: 필드에만
    - METHOD: 메서드에만

### @Documented
- 자바 API 문서를 만들 때 해당 애노테이션이 함께 포함되는지를 지정함
- 보통 함께 사용함

### @Inherited
- 자식 클래스가 애노테이션을 상속 받을 수 있게 함

### 예
```java
package annotation.basic;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface AnnoMeta {
}
```

```java
@AnnoMeta // 타입에 적용
public class MetaData {

//    @AnnoMeta // 필드에 적용 - 컴파일 오류
    private String id;

    @AnnoMeta // 메서드에 적용
    public void call() {

    }

    public static void main(String[] args) throws NoSuchMethodException {
        AnnoMeta typeAnno = MetaData.class.getAnnotation(AnnoMeta.class);
        System.out.println("typeAnno = " + typeAnno);

        AnnoMeta methodAnno = MetaData.class.getMethod("call").getAnnotation(AnnoMeta.class);
        System.out.println("methodAnno = " + methodAnno);
    }
}
```

---
## 애노테이션과 상속

### 애노테이션의 상속
- 모든 애노테이션은 `java.lang.annotation.Annotation` 인터페이스를 `묵시적`으로 상속 받음
```java
package java.lang.annotation;

public interface Annotation {
    boolean equals(Object obj);
    int hashCode();
    String toString():
    Class<? extends Annotation> annotationType();
}
```
- 개발자가 직접 구현하거나 확장하는 것이 아니라 자바 언어 자체에서 애노테이션을 위한 기반으로 사용함
- 모든 애노테이션은 기본적으로 `Annotation` 인터페이스를 확장하며, 이로 인해 자바에서 애노테이션은 특별한 형태의 인터페이스로 간주됨

### 애노테이션 정의
```java
public @interface MyCustomAnnotation {}
```

### 자바가 자동으로 처리
```java
public interface MyCustomAnnotation extends java.lang.annotation.Annotation {}
```

### 애노테이션과 상속
- **애노테이션은 다른 애노테이션이나 인터페이스를 직접 상속할 수 없음**
- 오직 `java.lang.annotaion.Annotation` 인터페이스만 상속함
- **따라서 애노테이션 사이에는 상속이라는 개념이 존재하지 않음**

### @Inherited
- 이것을 사용하면 애노테이션을 적용한 클래스의 자식도 해당 애노테이션을 부여 받을 수 있음
- **주의할 점: 클래스 상속에서만 작동하고, 인터페이스의 구현체에는 적용되지 않음**

### 예시 코드
```java
@Inherited // 클래스 상속 시 자식도 애노테이션 적용
@Retention(RetentionPolicy.RUNTIME)
public @interface InheritedAnnotation {


}
```

```java
package annotation.basic.inherited;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface NoInheritedAnnotation {

    
}
```

```java
package annotation.basic.inherited;

@InheritedAnnotation
@NoInheritedAnnotation
public class Parent {
}
```

```java
package annotation.basic.inherited;

public class Child extends Parent {
}
```

```java
package annotation.basic.inherited;

@InheritedAnnotation
@NoInheritedAnnotation
public interface TestInterface {
}
```

```java
package annotation.basic.inherited;

public class TestInterfaceImpl implements TestInterface {
}
```

```java
public class InheritedMain {

    public static void main(String[] args) {
        print(Parent.class);
        print(Child.class);
        print(TestInterface.class);
        print(TestInterfaceImpl.class);
    }

    private static void print(Class<?> clazz) {
        System.out.println("clazz = " + clazz);

        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println(" - " + annotation.annotationType().getSimpleName());
        }
        System.out.println();
    }
}
```

```text
clazz = class annotation.basic.inherited.Parent
 - InheritedAnnotation
 - NoInheritedAnnotation

clazz = class annotation.basic.inherited.Child
 - InheritedAnnotation

clazz = interface annotation.basic.inherited.TestInterface
 - InheritedAnnotation
 - NoInheritedAnnotation

clazz = class annotation.basic.inherited.TestInterfaceImpl
```

### @Inherited가 클래스 상속에만 적용되는 이유
- 클래스 상속과 인터페이스 구현의 차이
  - 클래스 상속은 자식 클래스가 부모 클래스의 속성과 메서드를 상속받는 개념
  - 즉, 자식 클래스는 부모 클래스의 특성을 이어 받으므로 부모 클래스에서 정의된 애노테이션을 자식 클래스가 자동으로 상속받을 수 있는 논리적인 기반이 있음
  - 인터페이스는 메서드의 시그니처만 정의할 뿐, 상태나 행위를 가지지 않기 때문에 인터페이스의 구현체가 애노테이션을 상속한다는 개념이 잘 맞지 않음

- 인터페이스와 다중 구현, 다이아몬드 문제
  - 인터페이스는 다중 구현이 가능함
  - 만약 인터페이스의 애노테이션을 구현 클래스에서 상속하게 되면 여러 인터페이스의 애노테이션간의 충돌이나 모호한 상황이 발생할 수 있음

---
## 애노테이션 활용 - 검증기

### 검증 예시
```java
package annotation.validator;

import static util.MyLogger.log;

public class ValidatorV1Main {

    public static void main(String[] args) {
        User user = new User("user1", 0);
        Team team = new Team("", 0);

        try {
            log("== user 검증 ==");
            validatorUser(user);
        } catch (Exception e) {
            log(e);
        }

        try {
            log("== team 검증 ==");
            validatorTeam(team);
        } catch (Exception e) {
            log(e);
        }
    }

    private static void validatorUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new RuntimeException("이름이 비었습니다.");
        }

        if (user.getAge() < 1 || user.getAge() > 100) {
            throw new RuntimeException("나이는 1과 100 사이여야 합니다.");
        }
    }

    private static void validatorTeam(Team team) {
        if (team.getName() == null || team.getName().isEmpty()) {
            throw new RuntimeException("이름이 비었습니다.");
        }

        if (team.getMemberCount() < 1 || team.getMemberCount() > 999) {
            throw new RuntimeException("회원 수는 1과 999 사이여야 합니다.");
        }
    }
}
```

### 애노테이션으로 해결
```java
package annotation.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmpty {

    String message() default "값이 비어있습니다";
}
```

```java
package annotation.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Range {

    int min();
    int max();
    String message() default "범위를 넘었습니다.";
}
```

```java
package annotation.validator;

import java.lang.reflect.Field;

public class Validator {

    public static void validate(Object obj) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(NotEmpty.class)) {
                String value = (String) field.get(obj);
                NotEmpty annotation = field.getAnnotation(NotEmpty.class);
                if (value == null || value.isEmpty()) {
                    throw new RuntimeException(annotation.message());
                }
            }

            if (field.isAnnotationPresent(Range.class)) {
                long value = field.getLong(obj);
                Range annotation = field.getAnnotation(Range.class);
                if (value < annotation.min() || value > annotation.max()) {
                    throw new RuntimeException(annotation.message());
                }
            }
        }
    }
}
```

```java
package annotation.validator;

import static util.MyLogger.log;

public class ValidatorV2Main {

    public static void main(String[] args) {
        User user = new User("user1", 0);
        Team team = new Team("", 0);

        try {
            log("== user 검증 ==");
            Validator.validate(user);
        } catch (Exception e) {
            log(e);
        }

        try {
            log("== team 검증 ==");
            Validator.validate(team);
        } catch (Exception e) {
            log(e);
        }
    }
}
```

### 참고
- 자바 진영에서는 애노테이션 기반 검증 기능은 Bean Validation이라는 이름으로 표준화했음
- 스프링 프레임워크, JPA 같은 기술들과도 함께 사용됨

---
## 자바 기본 애노테이션

### @Override
- 메서드 재정의가 정확하게 잘 되었는지 컴파일러가 체크하는데 사용함
- 컴파일 이후에는 사라짐(SOURCE)

### @Deprecated
- 더 이상 사용되지 않는다는 뜻
- 이 애노테이션이 적용된 기능은 사용을 권장하지 않음
  - 해당 요소를 사용하면 오류가 발생할 가능성이 있는 경우
  - 호환되지 않게 변경되거나 향후 버전에서 제거될 수 있는 경우
  - 더 나은 최신 대체 요소로 대체된 경우
  - 더 이상 사용되지 않는 기능인 경우

- `since`: 더 이상 사용하지 않게된 버전 정보
- `forRemoval`: 미래 버전에 코드가 제거될 예정

> 컴파일은 되지만, IDE에서 제공해주는 기능(경고만 해주고 프로그램은 작동됨)

### @SuppressedWarnings
- all: 모든 경고를 억제
- deprecation: 사용이 권장되지 않는 코드를 사용할 때 발생하는 경고를 억제
- unchecked: 제네릭 타입과 관련된 unchecked 경고를 억제
- serial: Serializable 인터페이스를 구현할 때 serialVersionUID 필드를 선언하지 않은 경우 발생하는 경고를 억제
- rawtypes: 재네릭 타입이 명시되지 않은 타입을 사용할 때 발생하는 경고를 억제
- unused: 사용되지 않는 변수, 메서드, 필드 등을 선언했을 때 발생하는 경고를 억제
