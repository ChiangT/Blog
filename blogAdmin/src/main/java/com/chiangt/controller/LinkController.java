package com.chiangt.controller;

import com.chiangt.domain.ResponseResult;
import com.chiangt.domain.entity.Link;
import com.chiangt.domain.vo.PageVo;
import com.chiangt.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, Link link){
        PageVo pageVo = linkService.selectLinkPage(pageNum, pageSize, link);
        return ResponseResult.okResult(pageVo);
    }

    @PostMapping
    public ResponseResult add(@RequestBody Link link){
        linkService.save(link);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult getInfo(@PathVariable(value = "id")Long id){
        Link link = linkService.getById(id);
        return ResponseResult.okResult(link);
    }

    @PutMapping
    public ResponseResult edit(@RequestBody Link link){
        linkService.updateById(link);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable(value = "id")Long id){
        linkService.removeById(id);
        return ResponseResult.okResult();
    }
}
