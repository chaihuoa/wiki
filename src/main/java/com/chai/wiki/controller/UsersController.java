package com.chai.wiki.controller;

import com.chai.wiki.req.UsersReq;
import com.chai.wiki.resp.CommonResp;
import com.chai.wiki.resp.UsersResp;
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
    public CommonResp list(UsersReq req) {
        CommonResp<List<UsersResp>> resp = new CommonResp<>();
        List<UsersResp> list = usersService.list(req);
        resp.setContent(list);
        return resp;
    }
}
