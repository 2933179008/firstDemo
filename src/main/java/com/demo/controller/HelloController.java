package com.demo.controller;

import org.springframework.web.bind.annotation.*;

/**
 * 登录接口
 *
 * @author pz1004
 */
@RequestMapping("")
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String addUser() {
        return "hello world";
    }
}