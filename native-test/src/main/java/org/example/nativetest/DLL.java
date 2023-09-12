package org.example.nativetest;

import com.sun.jna.Library;
import com.sun.jna.Native;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author JDragon
 * @date 2023/8/23 14:36
 * @description
 */
public interface DLL extends Library {

    DLL dll = Native.load("MathLibrary", DLL.class);

    void fibonacci_init(long a, long b);

    boolean fibonacci_next();

    long fibonacci_current();

    long fibonacci_index();
}
