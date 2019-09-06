package com.share.manage.controller;

import com.share.tools.vo.ResultVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
    ResultVO result = new ResultVO();
    @RequestMapping("/toindex")
    public String toIndex(){
        return "login";
    }

    @RequestMapping("/login")
    @ResponseBody
    public String login(String username , String password){

        return "1111";
    }

    @RequestMapping("/registerForword")
    public String registerForword(){

        result.setCode(1L);
        result.setMsg(new Object[]{"注册成功"});
        return "register";
    }

    @RequestMapping("/register")
    public String register(){

        result.setCode(1L);
        result.setMsg(new Object[]{"注册成功"});
        return "register";
    }
}
