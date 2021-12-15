package org.example.javabase.sh.jsch;

/**
 * @Author JDragon
 * @Date 2021.05.27 上午 11:59
 * @Email 1061917196@qq.com
 * @Des:
 */
public class ExecShellException extends Exception {

    public ExecShellException(){super();}

    public ExecShellException(String message) {
        super(message);
    }

    public ExecShellException(Throwable a) {
        super(a);
    }
}
