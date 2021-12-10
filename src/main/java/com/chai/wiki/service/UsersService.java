package com.chai.wiki.service;

import com.chai.wiki.domain.Users;
import com.chai.wiki.domain.UsersExample;
import com.chai.wiki.mapper.UsersMapper;
import com.chai.wiki.req.UsersReq;
import com.chai.wiki.resp.UsersResp;
import com.chai.wiki.util.CopyUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

//        List<UsersResp> respList = new ArrayList<>();
//        for (Users users : usersList) {
////            UsersResp usersResp = new UsersResp();
////            BeanUtils.copyProperties(users, usersResp);
//            UsersResp usersResp = CopyUtil.copy(users, UsersResp.class);
//            respList.add(usersResp);
//        }
        List<UsersResp> respList = CopyUtil.copyList(usersList, UsersResp.class);
        return respList;
    }
}
