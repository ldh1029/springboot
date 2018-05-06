package com.xhcoding.controller;

import com.xhcoding.Sender;
import com.xhcoding.service.UserService;
import com.xhcoding.vo.response.VoUserResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by xin on 2017/11/27.
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private Sender sender;
    @GetMapping("/pub/index")
    public List<VoUserResp> list() {
       return userService.findAll();
    }

    @GetMapping("/find/{id}")
    public Integer findOne(@PathVariable Integer id){
        return 100/0;
    }

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "/login";
    }

    @GetMapping("/rabbit")
    public void testRabbit(){
         sender.send();
    }


}
