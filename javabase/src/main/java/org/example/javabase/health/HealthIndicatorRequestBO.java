/*
 * Copyright (c) 2001-2022 GuaHao.com Corporation Limited. All rights reserved.
 * This software is the confidential and proprietary information of GuaHao Company.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with GuaHao.com.
 */
package org.example.javabase.health;

import lombok.Data;

import java.io.Serializable;

/**
 * 查询请求参数
 *
 * @author hezj
 * @version V1.0
 * @since 2022-03-23 17:02
 */
@Data
public class HealthIndicatorRequestBO implements Serializable {
    private static final long serialVersionUID = -1747796285447029398L;

    /**
     * 开始日期 格式 yyyy-MM-dd
     */
    private String startDate;


    /**
     * 结束日期 格式 yyyy-MM-dd
     */
    private String endDate;


    /**
     * 来源标识
     */
    private String sourceId;


}
