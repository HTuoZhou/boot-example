package com.boot.example.controller;

import com.boot.example.annotation.WebLog;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author TuoZhou
 */
@RestController
public class HelloController {

    @ApiOperation("hello")
    @GetMapping("/hello")
    @WebLog
    public String hello() {
        return "hello";
    }
}
