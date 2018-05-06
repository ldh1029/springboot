package com.xhcoding.entity;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by xin on 2017/11/28.
 */
@Data
@Entity
@Table(name = "gfb_topics_users")
public class TopicsUsers {
    @Id
    @GeneratedValue
    private Long id;
    private Long userId;
    private String username;
    private String avatar;
    private Integer forceState;
    private Long levelId;
    private Long useIntegral;
    private Long noUseIntegral;
    private Date createDate;
    private Date updateDate;
}
