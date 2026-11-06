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
