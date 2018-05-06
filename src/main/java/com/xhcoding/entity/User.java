package com.xhcoding.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by xin on 2018/1/24.
 */
@Data
@ApiModel("用户信息")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("用户类型")
    private String type;
}
