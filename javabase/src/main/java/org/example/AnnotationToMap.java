package org.example;

import lombok.SneakyThrows;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Author JDragon
 * @Date 2021.03.07 下午 6:41
 * @Email 1061917196@qq.com
 * @Des:
 */

@AnnotationToMap.AnnotationTest(
        value = "ssss", basePackageClasses = AnnotationToMap.class, useDefaultFilters = false,
        includeFilters = @AnnotationToMap.AnnotationTest.ComponentFilter(value = AnnotationToMap.class, type = "tttt", classes = AnnotationToMap.class)
)
public class AnnotationToMap {
    @SneakyThrows
    public static void main(String[] args) {
        Annotation[] annotations = AnnotationToMap.class.getAnnotations();
        for (Annotation annotation : annotations) {
            Map<String, Object> map = translationAnnotation(annotation);
            System.out.println(map);
        }
    }

    public static Map<String, Object> translationAnnotation(Annotation annotation) throws InvocationTargetException, IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Class<? extends Annotation> aClass = annotation.annotationType();
        Method[] declaredMethods = aClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            Class<?> returnType = declaredMethod.getReturnType();
            String name = declaredMethod.getName();
            Object invoke = declaredMethod.invoke(annotation);
            if (returnType.isArray()) {
                List<Map<String, Object>> mapList = new LinkedList<>();
                if (returnType.getComponentType().isAnnotation()) {
                    Annotation[] invokeList = (Annotation[]) invoke;
                    for (Annotation invokeAnnotation : invokeList) {
                        mapList.add(translationAnnotation(invokeAnnotation));
                    }
                    map.put(name, mapList);
                } else {
                    map.put(name, invoke);
                }
            } else if (returnType.isAnnotation()) {
                map.put(name, translationAnnotation((Annotation) invoke));
            } else {
                map.put(name, invoke);
            }
        }
        return map;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface AnnotationTest {
        String value() default "";

        Class<?>[] basePackageClasses() default {};

        boolean useDefaultFilters() default true;

        ComponentFilter[] includeFilters() default {};

        @Retention(RetentionPolicy.RUNTIME)
        @Target({})
        @interface ComponentFilter {

            Class<?>[] value() default {};

            String type() default "";

            Class<?>[] classes() default {};

        }
    }
}
