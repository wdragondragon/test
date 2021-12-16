package com.jdragon.springboot.h2;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.25 15:57
 * @Description:
 */

@Mapper
@Repository
public interface H2UserMapper {
    List<User> selectUserList();
}
