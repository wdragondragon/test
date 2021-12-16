package com.jdragon.springboot.user.mapper;

import com.jdragon.springboot.user.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.16 20:27
 * @Description:
 */
@Mapper
@Repository
public interface UserMapper2 {

    List<User> selectUserList();

    User selectUserByName(@Param("username") String user);
}
