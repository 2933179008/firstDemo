package com.demo.controller;

import com.demo.dto.UserDto;
import com.demo.dto.UserQuery;
import com.demo.entity.UserEntity;
import com.demo.service.UserService;
import com.demo.utils.result.Result;
import com.demo.utils.result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 登录接口
 *
 * @author pz1004
 */
@RequestMapping("")
@RestController
public class HelloController {
    @GetMapping("/hello")
    public Result addUser() {
        System.out.println("hello world");
    }
}