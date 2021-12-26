package com.chai.wiki.service;

import com.chai.wiki.domain.User;
import com.chai.wiki.domain.UserExample;
import com.chai.wiki.mapper.UserMapper;
import com.chai.wiki.req.UserQueryReq;
import com.chai.wiki.req.UserSaveReq;
import com.chai.wiki.resp.UserQueryResp;
import com.chai.wiki.resp.PageResp;
import com.chai.wiki.util.CopyUtil;
import com.chai.wiki.util.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<UserQueryResp> list(UserQueryReq req) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getLoginName())) {
            criteria.andNameLike("%" + req.getLoginName() + "%");
        }
        PageHelper.startPage(req.getPage(), req.getSize());
        List<User> userList = userMapper.selectByExample(userExample);

        PageInfo<User> pageInfo = new PageInfo<>(userList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

//        List<UserResp> respList = new ArrayList<>();
//        for (User user : userList) {
//            UserResp userResp = new UserResp();
//            BeanUtils.copyProperties(user, userResp);
//            respList.add(userResp);
//        }

        List<UserQueryResp> respList = CopyUtil.copyList(userList, UserQueryResp.class);

        PageResp<UserQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        return pageResp;
    }

    public void save(UserSaveReq req) {
        User user = CopyUtil.copy(req, User.class);
        if (ObjectUtils.isEmpty(user.getId())) {
            user.setId(snowFlake.nextId());
            userMapper.insert(user);
        } else {
            userMapper.updateByPrimaryKey(user);
        }
    }

    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }
}
