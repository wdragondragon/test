package com.jdragon.springboot.mybatisPlus;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.08 15:21
 * @Description:
 */
@Repository
@Mapper
public interface UserMapper1 extends BaseMapper<User> {
}
