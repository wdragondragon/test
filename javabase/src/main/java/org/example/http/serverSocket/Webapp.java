package org.example.http.serverSocket;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.26 13:02
 * @Description:
 */
public class Webapp {

    public static Servlet getServlet(String url) {
        String className= "org.example.http.serverSocket.LoginServlet";
        Servlet temp=null;
        Class<?> clz=null;
        try {
            System.out.println("classname:"+className);
            if(className!=null)
                clz=Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
        try {
            if(clz!=null)
                temp=(Servlet)clz.newInstance();
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
        return temp;
    }
}
