package org.example.yml.simple;

import lombok.Data;

import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.05 10:40
 * @Description:
 */
@Data
public class Customer {

    private String firstName;

    private String lastName;

    private int age;

    private List<Contact> contactDetails;
}

