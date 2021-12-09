package com.chai.wiki.service;

import com.chai.wiki.domain.Demo;
import com.chai.wiki.mapper.DemoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DemoService {

    @Resource
    private DemoMapper DemoMapper;

    public List<Demo> list() {
        return DemoMapper.selectByExample(null);
    }
}
