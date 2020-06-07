package com.rahel.lxblog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

	@GetMapping("/user/get")
    public String getUser(){
        return "hi user";
    }
    
    @GetMapping("/admin/get")
    public String getAdmin(){
        return "hi admin";
    }
    
}
