package org.example.javabase.fieldName;

import java.io.Serializable;

@FunctionalInterface
public interface SFunction<T, R> extends Serializable {
    R get(T source);
}