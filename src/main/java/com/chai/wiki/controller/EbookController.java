package com.chai.wiki.controller;

import com.chai.wiki.req.EbookQueryReq;
import com.chai.wiki.req.EbookSaveReq;
import com.chai.wiki.resp.CommonResp;
import com.chai.wiki.resp.EbookQueryResp;
import com.chai.wiki.resp.PageResp;
import com.chai.wiki.service.EbookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp list(@Valid EbookQueryReq req) {
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody EbookSaveReq req) {
        CommonResp resp = new CommonResp<>();
        ebookService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        CommonResp<Object> resp = new CommonResp<>();
        ebookService.delete(id);
        return resp;
    }
}
