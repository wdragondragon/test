package org.example.nativetest;

/**
 * Hello world!
 */
public class NativeTest {
    public static void main(String[] args) throws InterruptedException {
        DLL.dll.fibonacci_init(1, 1);
        for (int i = 0; i < 10; i++) {
            DLL.dll.fibonacci_next();
            System.out.println(DLL.dll.fibonacci_index() + ": " + DLL.dll.fibonacci_current());
        }
        DLL.dll.fibonacci_init(1, 1);
        do {
            Thread.sleep(50);
            System.out.println(DLL.dll.fibonacci_index() + ": " + DLL.dll.fibonacci_current());
        } while (DLL.dll.fibonacci_next());
    }

    private static int getUnsignedByte(byte b) {
        return b & 0x0FF;
    }

    private static int getUnsignedShort(short data) {
        return data & 0x0FFFF;
    }

    private static long getUnsignedInt(int data) {
        // data & 0xFFFFFFFF 和 data & 0xFFFFFFFFL 结果是不同的，需要注意，有可能与 JDK 版本有关
        return data & 0xFFFFFFFFL;
    }

    private static long getUnsignedLong(long data) {
        // data & 0xFFFFFFFF 和 data & 0xFFFFFFFFL 结果是不同的，需要注意，有可能与 JDK 版本有关
        return data & 0xFFFFFFFFL;
    }
}
