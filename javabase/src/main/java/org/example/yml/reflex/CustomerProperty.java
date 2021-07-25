package org.example.yml.reflex;

import lombok.Data;
import org.example.yml.simple.Contact;

import java.util.List;
import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.05 18:10
 * @Description:
 */

@Data
@Property("reflex.customer")
public class CustomerProperty {

    private String firstName;

    private String lastName;

    private int age;

    private List<Contact> contactDetails;

    private List<Map<String,String>> friends;

    private Map<String,String> hobby;

    private String isnull;
}
