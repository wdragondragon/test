package org.example.rabbitmq.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @author liuqh
 * @date: 2019年11月08日
 */
@Data
@ApiModel(value = "作业日志信息")
public class JobLogVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;

	@ApiModelProperty(value = "作业ID")
    private Integer jobId;
	
	@ApiModelProperty(value = "作业名称")
	private String jobName;

    @ApiModelProperty(value = "日志名称")
    private String name;

    @ApiModelProperty(value = "源数据表id")
    private Integer sourceTableId;
    
    @ApiModelProperty(value = "源数据表名")
    private String sourceTableName;
    
    @ApiModelProperty(value = "目标表id")
    private String targetTableId;
    
    @ApiModelProperty(value = "目标表名")
    private String targetTableName;
    
    @ApiModelProperty(value = "采集策略")
    private Integer strategy;
    
    @ApiModelProperty(value = "采集模式  0:实时  1:周期")
    private Integer mode;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "执行情况")
    private Integer status;   
    
    @ApiModelProperty(value = "日志路径")
    private String logPath;
    
    @ApiModelProperty(value = "IP地址")
    private String serverIp;

    @ApiModelProperty(value = "端口号")
    private Integer serverPort;

    @ApiModelProperty(value = "用户")
    private String serverUsername;

    @ApiModelProperty(value = "密码")
    private String serverPassword;

    @ApiModelProperty(value = "作业批次")
    private String batch;

    @ApiModelProperty(value = "引擎id")
    private Integer engineId;

    @ApiModelProperty("引擎名称")
    private String engineName;
}
