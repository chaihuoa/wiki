package com.chai.wiki.controller;

import com.chai.wiki.domain.Users;
import com.chai.wiki.resp.CommonResp;
import com.chai.wiki.service.UsersService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Resource
    private UsersService usersService;

    @GetMapping("/list")
    public CommonResp list() {
        CommonResp<List<Users>> resp = new CommonResp<>();
        List<Users> list = usersService.list();
        resp.setContent(list);
        return resp;
    }
}
