package org.example.javabase.sh.jsch;

/**
 * @Author JDragon
 * @Date 2021.05.27 上午 11:50
 * @Email 1061917196@qq.com
 * @Des:
 */
@FunctionalInterface
public interface ThrowableFunction {

    boolean apply(String t) throws ExecShellException;
}
