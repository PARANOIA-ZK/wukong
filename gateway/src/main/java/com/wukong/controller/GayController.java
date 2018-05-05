package com.wukong.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author PARANOIA_ZK
 * @date 2018/3/7 14:30
 */
@RestController
@RequestMapping("/local")
public class GayController {

    @RequestMapping("/gay")
    public String gay() {
        System.out.println("----------------------------");
        return "你好，小gaygay ~";
    }

}



