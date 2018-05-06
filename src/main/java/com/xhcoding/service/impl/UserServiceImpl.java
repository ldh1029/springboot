package com.xhcoding.service.impl;

import com.xhcoding.entity.TopicsUsers;
import com.xhcoding.repository.UserRepository;
import com.xhcoding.service.UserService;
import com.xhcoding.vo.response.VoUserResp;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xin on 2017/11/28.
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<VoUserResp> findAll() {
        List<TopicsUsers> list = userRepository.findAll();
        List<VoUserResp> voUserResps = Lists.newArrayList();
        //String name = RedisUtils.get("username");
        //log.info(name);
        list.stream().forEach((user) -> {
                    VoUserResp voUserResp = new VoUserResp();
                    voUserResp.setUserId(user.getUserId());
                    voUserResp.setUsername(user.getUsername());
                    voUserResps.add(voUserResp);
                }
        );
        return voUserResps;
    }
}
