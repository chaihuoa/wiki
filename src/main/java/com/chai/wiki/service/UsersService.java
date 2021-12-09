package com.chai.wiki.service;

import com.chai.wiki.domain.Users;
import com.chai.wiki.mapper.UsersMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UsersService {

    @Resource
    private UsersMapper UsersMapper;

    public List<Users> list() {
        return UsersMapper.selectByExample(null);
    }
}
