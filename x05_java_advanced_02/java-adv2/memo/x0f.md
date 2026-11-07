# HTTP 서버 활용

## HTTP 서버 7 - 애노테이션 서블릿 1 - 시작

### 컨트롤러 만들기
```java
package was.httpserver.servlet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Mapping {

    String value();
}
```

```java
package was.v7;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.servlet.annotation.Mapping;

public class SearchControllerV7 {

    @Mapping("/search")
    public void search(HttpRequest request, HttpResponse response) {
        String query = request.getParameter("q");
        response.writeBody("<h1>Search</h1>");
        response.writeBody("<ul>");
        response.writeBody("<li>query: " + query + "</li>");
        response.writeBody("</ul>");
    }
}

package was.v7;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.servlet.annotation.Mapping;

public class SiteControllerV7 {

    @Mapping("/")
    public void home(HttpRequest request, HttpResponse response) {
        response.writeBody("<h1>home</h1>");
        response.writeBody("<ul>");
        response.writeBody("<li><a href='/site1'>site1</a></li>");
        response.writeBody("<li><a href='/site2'>site2</a></li>");
        response.writeBody("<li><a href='/search?q=hello'>검색</a></li>");
        response.writeBody("</ul>");
    }

    @Mapping("/site1")
    public void site1(HttpRequest request, HttpResponse response) {
        response.writeBody("<h1>site1</h1>");
    }

    @Mapping("/site2")
    public void site2(HttpRequest request, HttpResponse response) {
        response.writeBody("<h1>site2</h1>");
    }
}
```

```java
package was.httpserver.servlet.annotation;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.HttpServlet;
import was.httpserver.PageNotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class AnnotationServletV1 implements HttpServlet {

    private final List<Object> controllers;

    public AnnotationServletV1(List<Object> controllers) {
        this.controllers = controllers;
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        String path = request.getPath();

        for (Object controller : controllers) {
            Method[] methods = controller.getClass().getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Mapping.class)) {
                    Mapping mapping = method.getAnnotation(Mapping.class);
                    String value = mapping.value();
                    if (value.equals(path)) {
                        invoke(controller, method, request, response);
                        return;
                    }
                }
            }
        }

        throw new PageNotFoundException("request = " + path);
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
package was.v7;

import was.httpserver.HttpServer;
import was.httpserver.ServletManager;
import was.httpserver.servlet.DiscardServlet;
import was.httpserver.servlet.annotation.AnnotationServletV1;

import java.util.List;

public class ServerMainV7 {

    private static final int PORT = 12345;

    public static void main(String[] args) throws Exception {
        List<Object> controllers = List.of(new SiteControllerV7(), new SearchControllerV7());
        AnnotationServletV1 annotationServlet = new AnnotationServletV1(controllers);

        ServletManager servletManager = new ServletManager();
        servletManager.setDefaultServlet(annotationServlet);
        servletManager.add("/favicon.ico", new DiscardServlet());

        HttpServer httpServer = new HttpServer(PORT, servletManager);
        httpServer.start();
    }
}
```

---
## HTTP 서버 8 - 애노테이션 서블릿 2 - 동적 바인딩

### 현재 문제점
- request를 전혀 사용하지 않는 경우도 있음
- 동적으로 필요한 인자만 받을 수 있게 하고 싶음

### 개선
```java
package was.httpserver.servlet.annotation;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.HttpServlet;
import was.httpserver.PageNotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class AnnotationServletV2 implements HttpServlet {

    private final List<Object> controllers;

    public AnnotationServletV2(List<Object> controllers) {
        this.controllers = controllers;
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        String path = request.getPath();

        for (Object controller : controllers) {
            Method[] methods = controller.getClass().getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Mapping.class)) {
                    Mapping mapping = method.getAnnotation(Mapping.class);
                    String value = mapping.value();
                    if (value.equals(path)) {
                        invoke(controller, method, request, response);
                        return;
                    }
                }
            }
        }

        throw new PageNotFoundException("request = " + path);
    }


    private static void invoke(Object controller, Method method, HttpRequest request, HttpResponse response) {

        Class<?>[] parameterTypes = method.getParameterTypes();
        // request, response 즉, 해당 메서드가 받는 파라미터 수에 따라서 동적으로 배열 생성
        Object[] args = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i] == HttpRequest.class) {
                args[i] = request;
            } else if (parameterTypes[i] == HttpResponse.class) {
                args[i] = response;
            } else {
                throw new IllegalArgumentException("Unsupported parameter type: " + parameterTypes[i]);
            }
        }

        try {
            method.invoke(controller, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
```
- 파라미터 수를 확인
- 파라미터 수만큼 배열을 만들고
- 해당 파라미터 배열의 타입을 조회하고 해당 타입이면 넣어줌
- invoke(args)로 배열을 넣음 - 내가 만든 invoke가 아님
- 이후 컨트롤러에서 사용하지 않는 인자 삭제

---
## HTTP 서버 9 - 애노테이션 서블릿 3 - 성능 최적화

### 아쉬운 점
- 성능 최적화
- 중복 매핑 문제

### 성능 최적화
```java
        for (Object controller : controllers) {
            Method[] methods = controller.getClass().getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Mapping.class)) {
                    Mapping mapping = method.getAnnotation(Mapping.class);
                    String value = mapping.value();
                    if (value.equals(path)) {
                        invoke(controller, method, request, response);
                        return;
                    }
                }
            }
        }
```
- 모든 컨트롤러의 메서드를 하나 하나 순서대로 찾음
- 결과적으로 `O(N)`의 성능
- 만약 모든 컨트롤러의 메서드가 100개라면 최악의 경우 100번은 찾아야함
- 더 큰 문제는 고객의 요청마다 이로직이 호출된다는 점

### 중복 매핑 문제
- 개발자가 서로 다른 메서드에 같은 Mapping을 하게 될 경우
- 현재 로직에서는 먼저 찾은 메서드가 호출됨
- **개발에서 가장 좋지 않은 것은 `모호함`**

### 최적화 구현
```java
package was.httpserver.servlet.annotation;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.HttpServlet;
import was.httpserver.PageNotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationServletV3 implements HttpServlet {

    private final Map<String, ControllerMethod> pathMap;

    public AnnotationServletV3(List<Object> controllers) {
        this.pathMap = new HashMap<>();
        initializePathMap(controllers);
    }

    // - 프로그램 처음 동작 시점에 모든 매핑 정보를 저장해둠
    private void initializePathMap(List<Object> controllers) {
        for (Object controller : controllers) {
            Method[] methods = controller.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Mapping.class)) {
                    String path = method.getAnnotation(Mapping.class).value();
                    // 중복 경로 체크
                    if (pathMap.containsKey(path)) {
                        ControllerMethod controllerMethod = pathMap.get(path);
                        throw new IllegalArgumentException("경로 중복 등록, path= " + path + ", method=" + method + "이미 등록된 메서드=" + controllerMethod.method);
                    }

                    // 정보 넣기
                    pathMap.put(path, new ControllerMethod(controller, method));
                }
            }
        }
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        String path = request.getPath();

        ControllerMethod controllerMethod = pathMap.get(path);

        if (controllerMethod == null) {
            throw new PageNotFoundException("request = " + path);
        }

        controllerMethod.invoke(request, response);
    }

    private static class ControllerMethod {
        private final Object controller;
        private final Method method;

        public ControllerMethod(Object controller, Method method) {
            this.controller = controller;
            this.method = method;
        }

        public void invoke(HttpRequest request, HttpResponse response) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            // request, response 즉, 해당 메서드가 받는 파라미터 수에 따라서 동적으로 배열 생성
            Object[] args = new Object[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                if (parameterTypes[i] == HttpRequest.class) {
                    args[i] = request;
                } else if (parameterTypes[i] == HttpResponse.class) {
                    args[i] = response;
                } else {
                    throw new IllegalArgumentException("Unsupported parameter type: " + parameterTypes[i]);
                }
            }

            try {
                method.invoke(controller, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
```

