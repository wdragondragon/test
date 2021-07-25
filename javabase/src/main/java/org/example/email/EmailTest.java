package org.example.email;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.17 00:11
 * @Description:
 */
public class EmailTest {
    public static void main(String[] args) throws Exception {
        Email e = Email.builder()
                .myEmailSMTPHost("smtp.qq.com")
                .myEmailAccount("1061917196@qq.com")
                .myEmailPassword("nuivmtotgfjpbcje")
                .personal("谭宇")
                .build();

        e.sendEmail("1061917196@qq.com","你好啊","你好啊我想认识你");
    }
}
