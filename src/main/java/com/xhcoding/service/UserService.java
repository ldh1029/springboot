package com.xhcoding.service;

import com.xhcoding.vo.response.VoUserResp;

import java.util.List;

/**
 * Created by xin on 2017/11/28.
 */

public interface UserService {
    /**
     * 查询所有用户
     * @return
     */
    List<VoUserResp> findAll();
}
