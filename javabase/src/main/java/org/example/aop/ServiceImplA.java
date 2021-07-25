package org.example.aop;

public class ServiceImplA implements IService {
    @Override
    public void service(String name) throws Exception {
        System.out.println("ServiceImplA name" + name);
    }
}
