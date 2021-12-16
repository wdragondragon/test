package com.jdragon.springboot.commons.zFeign;

/**
 * @Author JDragon
 * @Date 2021.04.21 下午 2:39
 * @Email 1061917196@qq.com
 * @Des:
 */

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.type.AnnotationMetadata;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.23 00:11
 * @Description:
 */
public class HttpDyanScannerRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        try {
            Properties properties = PropertiesLoaderUtils.loadAllProperties("application.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
