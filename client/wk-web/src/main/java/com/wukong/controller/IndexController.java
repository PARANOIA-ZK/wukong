package com.wukong.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author PARANOIA_ZK
 * @date 2018/4/12 16:12
 */
@Controller
public class IndexController {
    @RequestMapping("/")
    public String index(){
        System.out.println("进入首页");
        return "index";
    }
}
