package org.example.javabase.aop;


/**
 * jdk8静态代理
 */
public class ProxyServiceA implements IService {
    public IService service;
    public ProxyServiceA(IService service){
        super();
        this.service = service;
    }
    @Override
    public void service(String name) throws Exception {
        System.out.println("log start");
        try{
            service.service(name);
        }catch (Exception e){
            throw e;
        }
        System.out.println("log end");
    }

    public static void main(String []args) throws Exception {
        System.out.println(System.getProperty("b1"));
        System.out.println(System.getProperty("b2"));
        for (String arg : args) {
            System.out.println(arg);
        }

        IService service = new ServiceImplA();
        service = new ProxyServiceA(service);
        service.service("zhjl");
    }
}
