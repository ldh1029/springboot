package com.xhcoding.vo.response;

import lombok.Data;

/**
 * Created by xin on 2017/11/28.
 */
@Data
public class VoUserResp {
    private Long id;
    private Long userId;
    private String username;
    private String avatar;
    private Integer forceState;
    private Long levelId;
}
