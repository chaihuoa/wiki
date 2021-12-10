package com.chai.wiki.service;

import com.chai.wiki.domain.Users;
import com.chai.wiki.domain.UsersExample;
import com.chai.wiki.mapper.UsersMapper;
import com.chai.wiki.req.UsersReq;
import com.chai.wiki.resp.UsersResp;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsersService {

    @Resource
    private UsersMapper UsersMapper;

    public List<UsersResp> list(UsersReq req) {
        UsersExample usersExample = new UsersExample();
        UsersExample.Criteria criteria = usersExample.createCriteria();
        criteria.andUsernameLike("%" + req.getUsername() + "%");
        List<Users> usersList = UsersMapper.selectByExample(usersExample);

        List<UsersResp> respList = new ArrayList<>();
        for (Users users : usersList) {
            UsersResp usersResp = new UsersResp();
            BeanUtils.copyProperties(users, usersResp);
            respList.add(usersResp);
        }
        return respList;
    }
}
