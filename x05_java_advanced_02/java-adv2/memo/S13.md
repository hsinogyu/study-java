# 리플렉션

## 리플렉션이 필요한 이유

### 현재 서블릿의 단점
  - 하나의 클래스에 하나의 기능만 만들 수 있음
  - 새로 만든 클래스를 URL 경로와 항상 매핑해야함

### 문제 1: 하나의 클래스에 하나의 기능만 만들 수 있음
- 기능 하나를 만들 때마다 각각 별도의 클래스를 만들고 구현해야함
- 복잡한 기능에서는 효과적이지만 간단한 기능을 만을 때는 클래스가 너무 많이 만들어지기 때문에 부담스러움

### 문제 1 해결 방안

```java
import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;

public class ReflectController {

    public void site1(HttpRequest request, HttpResponse response) {
        response.writeBody("<h1>site1</h1>");
    }

    public void site2(HttpRequest request, HttpResponse response) {
        response.writeBody("<h1>site2</h1>");
    }

    public void search(HttpRequest request, HttpResponse response) {
        // ...
    }
}
```

### 문제 2: 새로 만든 클래스를 URL 경로와 항상 매핑해야함
```java
servletManager.add("/site1", new Site1Servlet());
servletManager.add("/site2", new Site2Servlet());
```

### 문제 2 해결 방안
- URL 경로의 이름과 같은 이름의 메서드를 찾아서 호출할 수 있다면?
- 즉, 클래스에 있는 메서드의 이름을 찾아서 이럴게 호출할 수 있다면, 번거로운 매핑 작업을 제거할 수 있을 것임

---
## 클래스와 메타데이터

### 리플렉션
- 클래스가 제공하는 다양한 정보를 **동적**으로 분석하고 사용하는 기능
- 이를 통해 프로그램 실행 중에 클래스, 메서드, 필드 등에 대한 정보를 얻거나, 새로운 객체를 생성하고 메서드를 호출하며, 필드의 값을 읽고 쓸 수 있음
- 얻을 수 있는 정보
  - 클래스의 메타데이터
  - 필드 정보
  - 메서드 정보
  - 생성자 정보

### 클래스 메타데이터 조회
```java
public class BasicData {

    public String publicField;
    private int privateField;

    public BasicData() {
        System.out.println("BasicData.BasicData");
    }

    private BasicData(String data) {
        System.out.println("BasicData.BasicData: " + data);
    }

    public void call() {
        System.out.println("BasicData.call");
    }

    public String hello(String str) {
        System.out.println("BasicData.hello");
        return str + " hello";
    }

    private void privateMethod() {
        System.out.println("BasicData.privateMethod");
    }

    void defaultMethod() {
        System.out.println("BasicData.defaultMethod");
    }

    protected void protectedMethod() {
        System.out.println("BasicData.protectedMethod");
    }
}
```

```java
package reflection;

import reflection.data.BasicData;

public class BasicV1 {

    public static void main(String[] args) throws ClassNotFoundException {
        // 클래스 메타데이터 조회 방법 3가지

        // 1. 클래스에서 찾기
        Class<BasicData> basicDataClass1 = BasicData.class; // 클래스 정보를 담고 있는 클래스
        System.out.println("basicDataClass1 = " + basicDataClass1);

        // 2. 인스턴스에서 찾기
        BasicData basicInstance = new BasicData();
        Class<? extends BasicData> basicDataClass2 = basicInstance.getClass();
        System.out.println("basicDataClass2 = " + basicDataClass2);

        // 3. 문자로 찾기
        String className = "reflection.data.BasicData"; // 패키지명 주의
        Class<?> basicDataClass3 = Class.forName(className);
        System.out.println("basicDataClass3 = " + basicDataClass3);
    }
}
```
- 클래스 메타데이터 조회 방법 3가지
  - 클래스에서 찾기
  - 인스턴스에서 찾기
    - 인스턴스에서는 .getClass()로 획득할 수 있음
    - Class<? extends BasicData>로 표현됨 => 실제 인스턴스가 BasicData 타입일 수도 있지만 그 자식 타입일 수도 있기 때문
  - 문자로 찾기(클래스 명(패키지 경로 포함))

### 클래스 메타데이터 활용
```java
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
```
- 수정자는 접근 제어자와 비접근 제어자로 나눌 수 있음
  - 접근 제어자: public, protected, default, (package-private), private
  - 비접근 제어자: static final, abstract, synchronized, volatile

- 여기서 수정자가 1이 나오는 이유
  - BasicData라는 클래스 자체의 수정자가 public이기 때문
  - 즉, 수정자라는 건 클래스나 메서드가 어떤 제어자인지를 보여주는 것

- 자바 Modifier의 정수 값 정의
  - public = 1
  - private = 2
  - protected = 4
  - static = 8
  - final = 16

---
## 메서드 탐색과 동적 호출

### 메서드 메타데이터
```java
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
```

```java
package reflection;

import reflection.data.BasicData;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodV2 {

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // 정적 메서드 호출 - 일반적인 메서드 호출
        BasicData helloInstance = new BasicData();
        helloInstance.call(); // 이 부분은 코드를 변경하지 않는 이상 정적임

        // 동적 메서드 호출 - 리플렉션 사용
        Class<? extends BasicData> helloClass = helloInstance.getClass();
        String methodName = "hello";

        // 메서드 이름을 변수로 변경할 수 있음
        Method method1 = helloClass.getDeclaredMethod(methodName, String.class);// (메서드 이름, 인자의 타입)
        Object returnValue = method1.invoke(helloInstance, "hi"); // 인스턴스를 넘기고, 인자를 넘김
        System.out.println("returnValue = " + returnValue);
    }
}
```

### 동적 메서드 호출
```java
package reflection;

import reflection.data.Calculator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;

public class MethodV3 {

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("호출 메서드: ");
        String methodName = scanner.nextLine();

        System.out.print("숫자1: ");
        int num1 = scanner.nextInt();
        System.out.print("숫자2: ");
        int num2 = scanner.nextInt();

        Calculator calculator = new Calculator();
        // 호출할 메서드 변수 이름으로 동적으로 선택
        Class<? extends Calculator> aClass = Calculator.class;
        Method method = aClass.getMethod(methodName, int.class, int.class);

        Object returnValue = method.invoke(calculator, num1, num2);
        System.out.println("returnValue = " + returnValue);
    }
}
```

---
## 필드 탐색 및 값 변경

### 필드 탐색
```java
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
```

### 필드 값 변경
```java
package reflection;

import reflection.data.User;

import java.lang.reflect.Field;

public class FieldV2 {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        User user = new User("id1", "userA", 20);
        System.out.println("기존 이름: " + user.getName());

        Class<? extends User> aClass = user.getClass();
        Field nameField = aClass.getDeclaredField("name");

        // private 필드에 접근 허용, private 메서드도 이렇게 호출 가능
        nameField.setAccessible(true); // private 필드에 접근 허용
        nameField.set(user, "userB");
        System.out.println("변경된 이름 = " + user.getName());
    }
}
```
- `nameField.setAccessible(true)`: private에 직접 접근할 수 있게 해줌 / 메서드도 제공해줌

### 리플렉션과 주의사항
- 리플렉션을 활용하면 private 접근 제어자에도 접근해서 값을 변경할 수 있음
- 단, 이는 객체지향 프로그래밍 원칙을 위반하는 행위로 간주될 수 있음

---
## 리플렉션 - 활용 예제

### 상황
- 프로젝트에서 데이터를 저장해야하는데, 저장할 때 반드시 nnull을 사용하면 안된다고 가정
- 이 경우 null 값을 다른 기본 값으로 모두 변경해야함
  - String이 null이면 ""(빈 문자)로 변경
  - Integer가 null이면 0으로 변경

### 코드
```java
package reflection;

import reflection.data.Team;
import reflection.data.User;

public class FieldV3 {

    public static void main(String[] args) {
        User user = new User("id1", null, null);
        Team team = new Team("team1", null);

        System.out.println("==== before ====");
        System.out.println("user = " + user);
        System.out.println("team = " + team);

        if (user.getId() == null) {
            user.setId("");
        }

        if (user.getName() == null) {
            user.setName("");
        }

        if (user.getAge() == null) {
            user.setAge(0);
        }

        if (team.getName() == null) {
            team.setName("");
        }

        if (team.getId() == null) {
            team.setId("");
        }

        System.out.println("==== after ====");
        System.out.println("user = " + user);
        System.out.println("team = " + team);
    }
}
```
- 굉장히 수고스러운 작업

### 리플렉션 사용
```java
package reflection;

import java.lang.reflect.Field;

public class FieldUtil {

    public static void nullFieldToDefault(Object target) throws IllegalAccessException {
        Class<?> aClass = target.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            if (field.get(target) != null) {
                continue;
            }

            if (field.getType() == String.class) {
                field.set(target, "");
            } else if (field.getType() == Integer.class) {
                field.set(target, 0);
            }
        }
    }
}
```

```java
package reflection;

import reflection.data.Team;
import reflection.data.User;

public class FieldV4 {

    public static void main(String[] args) throws IllegalAccessException {
        User user = new User("id1", null, null);
        Team team = new Team("team1", null);
        System.out.println("==== before ====");
        System.out.println("user = " + user);
        System.out.println("team = " + team);

        FieldUtil.nullFieldToDefault(user);
        FieldUtil.nullFieldToDefault(team);

        System.out.println("==== after ====");
        System.out.println("user = " + user);
        System.out.println("team = " + team);
    }
}
```

---
## 생성자 탐색과 객체 생성

### 생성자 탐색
```java
public class ConstructV1 {

    public static void main(String[] args) throws ClassNotFoundException {
        Class<?> aClass = Class.forName("reflection.data.BasicData");

        System.out.println("==== constructors() ====");
        Constructor<?>[] constructors = aClass.getConstructors();
        for (Constructor<?> constructor : constructors) {
            System.out.println(constructor); // public인 것만 출력(부모 X)
        }

        System.out.println("==== declaredConstructors() ====");
        Constructor<?>[] declaredConstructors = aClass.getDeclaredConstructors();
        for (Constructor<?> declaredConstructor : declaredConstructors) {
            System.out.println(declaredConstructor); // 내가 선언한 모든 생성자를 가져옴
        }
    }
}
```
- 생성자는 상속이라는 개념이 없음
- `super()`호출할 수는 있지만, 이게 상속을 받아서 호출할 수 있는게 아님

### 생성자 활용
```java
public class ConstructV2 {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> aClass = Class.forName("reflection.data.BasicData");

        Constructor<?> constructor = aClass.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);
        Object instance = constructor.newInstance("hello");
        System.out.println("instance = " + instance);

        Method method1 = aClass.getDeclaredMethod("call");
        method1.invoke(instance);
    }
}
```

### 눈여겨볼만한 점
- 코드 어디에도 BasicData라는 것이 없음
- 클래스 찾고 생성하고, 메서드를 호출하는 방법도 모두 동적으로 처리함

### 참고
- 스프링 프레임워크나 다른 프레임워크 기술들을 사용해보면, 내가 만든 클래스를 프레임워크가 대신 생성해줄 때가 있음
- 그때가 되면 리플렉션과 동적 객체 생성 방법들이 떠올를 것

---
## HTTP 서버 6 - 리플렉션 서블릿

### 리플렉션을 처리하는 서블릿 구현
- 서블릿은 그대로 사용하며서 새로운 기능을 구현해보자

### 컨트롤러 구현
```java
public class SiteControllerV6 {

    public void site1(HttpRequest request, HttpResponse response) {
        response.writeBody("<h1>site1</h1>");
    }

    public void site2(HttpRequest request, HttpResponse response) {
        response.writeBody("<h1>site2</h1>");
    }
}
```

```java
package was.v6;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;

public class SearchControllerV6 {

    public void search(HttpRequest request, HttpResponse response) {
        String query = request.getParameter("q");
        response.writeBody("<h1>Search</h1>");
        response.writeBody("<ul>");
        response.writeBody("<li>query: " + query + "</li>");
        response.writeBody("</ul>");
    }
}
```

### 리플렉션 서블릿
```java
package was.httpserver.servlet.reflection;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.HttpServlet;
import was.httpserver.PageNotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectionServlet implements HttpServlet {

    private final List<Object> controllers;

    public ReflectionServlet(List<Object> controllers) {
        this.controllers = controllers;
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        String path = request.getPath();

        for (Object controller : controllers) {
            Class<?> aClass = controller.getClass();
            Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                String methodName = method.getName();
                if (path.equals("/" + methodName)) {
                    invoke(controller, method, request, response);
                    return;
                }
            }
        }

        throw new PageNotFoundException("request = " + request);
    }

    private static void invoke(Object controller, Method method, HttpRequest request, HttpResponse response) {
        try {
            method.invoke(controller, request, response);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
```

```java
package was.v6;

import was.httpserver.HttpServer;
import was.httpserver.ServletManager;
import was.httpserver.servlet.DiscardServlet;
import was.httpserver.servlet.reflection.ReflectionServlet;
import was.v5.HomeServlet;

import java.util.List;

public class ServerMainV6 {

    private static final int PORT = 12345;

    public static void main(String[] args) throws Exception {
        List<Object> controllers = List.of(
                new SiteControllerV6(),
                new SearchControllerV6()
        );

        ReflectionServlet reflectionServlet = new ReflectionServlet(controllers);

        ServletManager servletManager = new ServletManager();
        servletManager.setDefaultServlet(reflectionServlet);
        servletManager.add("/", new HomeServlet());
        servletManager.add("/favicon.ico", new DiscardServlet());

        HttpServer server = new HttpServer(PORT, servletManager);
        server.start();
    }
}
```
- 우리가 등록하지 않은경우 무조건 ReflectionServlet이 호출됨

### 해결한 문제
- 하나의 클래스에 하나의 기능만 만들 수 있음
- 새로 만든 클래스를 URL 경로와 항상 매핑해야함

### 남은 문제점
- 리플렉션 서블릿은 요청 URL과 메서드 이름이 같다면 해당 메서드를 동적으로 호출할 수 있음
- 하지만 요청 이름과 메서드 이름을 다르게 하고 싶다면 어떻게 처리해야할까?
- URL은 주로 -를 구분자로 사용함 /hello-world같은 건 어떻게 처리해할까?
