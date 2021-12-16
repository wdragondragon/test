package com.jdragon.springboot.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.jdragon.springboot.commons.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.awt.print.Book;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.12.25 11:51
 * @Description:
 */

@RestController
@RequestMapping("test")
public class TestController {
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/download")
    public ResponseEntity download() throws IOException {
        String url = "http://192.168.162.1:18881/datacollect_service/Joblog/downloadJobLog";
        Map<String, String> params = new HashMap<>();
        params.put("logPath", "D:");
        params.put("name", "test.txt");
        ResponseEntity<Resource> entity = restTemplate.postForEntity(url, params, Resource.class);
        Resource body = entity.getBody();
        if (body == null) {
            return ResponseEntity.badRequest().body(Result.error("日志文件未找到"));
        }
        HttpHeaders headers = entity.getHeaders();
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(body.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(body);
    }

    @PostMapping("/token")
    public Result<String> token(@RequestParam String username,
                                @RequestParam String password,
                                @RequestHeader String username1,
                                @RequestHeader String password1,
                                @RequestBody Result<?> body) {

        if (username.equals(password) && username1.equals(password1) && body != null) {
            System.out.println(body);
            return Result.success("123456789");
        } else {
            return Result.error("密码错误");
        }
    }

    @GetMapping("/login")
    public Result<String> token2(@RequestParam String username,
                                 @RequestParam String password) {
        if (username.equals(password)) {
            return Result.success("123456789");
        } else {
            return Result.error("密码错误");
        }
    }

    @PostMapping("/reg")
    public Result<User> reg(@RequestBody User user) {
        User user1 = new User(1, user.getUsername(), user.getPassword(), 22, "17520067544");
        return Result.success(user1);
    }
}
