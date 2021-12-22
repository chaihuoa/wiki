package com.chai.wiki.controller;

import com.chai.wiki.req.DocQueryReq;
import com.chai.wiki.req.DocSaveReq;
import com.chai.wiki.resp.CommonResp;
import com.chai.wiki.resp.DocQueryResp;
import com.chai.wiki.resp.PageResp;
import com.chai.wiki.service.DocService;
import com.chai.wiki.util.ArrayUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocController {

    @Resource
    private DocService docService;

    @GetMapping("/all")
    public CommonResp all() {
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list = docService.all();
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/list")
    public CommonResp list(@Valid DocQueryReq req) {
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        PageResp<DocQueryResp> list = docService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody DocSaveReq req) {
        CommonResp resp = new CommonResp<>();
        docService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{idsStr}")
    public CommonResp delete(@PathVariable String idsStr) {
        CommonResp<Object> resp = new CommonResp<>();
        List<Long> list = ArrayUtil.strArrayConvertToLongList(idsStr);
        docService.delete(list);
        return resp;
    }
}
