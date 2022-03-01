package org.example.javabase;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author JDragon
 * @Date 2021.11.17 下午 10:09
 * @Email 1061917196@qq.com
 * @Des:
 */
public class Test7 {
    public static void main(String[] args) throws InterruptedException {
        List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13);
        int execNum = 3;
        ConcurrentLinkedQueue<Integer> concurrentLinkedQueue = new ConcurrentLinkedQueue<>(integerList);
        ExecutorService exec = Executors.newFixedThreadPool(execNum);
        CountDownLatch latch = new CountDownLatch(execNum);
        for (int i = 0; i < execNum; i++) {
            exec.submit(() -> {
                Integer num;
                while ((num = concurrentLinkedQueue.poll()) != null) {
                    System.out.println(num);
                }
                latch.countDown();
            });
        }
        latch.await();
        exec.shutdown();
        System.out.println("wi");
    }


}
