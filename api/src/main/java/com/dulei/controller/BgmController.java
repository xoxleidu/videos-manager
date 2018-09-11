package com.dulei.controller;

import com.dulei.service.BgmService;
import com.dulei.utils.IMoocJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/bgm")
@Api(value="背景音乐的接口", tags= {"背景音乐的controller"})
public class BgmController {

    @Autowired
    private BgmService bgmService;

    @PostMapping("/list")
    @ApiOperation(value="所有背景音乐的列表", notes="背景音乐LIST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "headerUserId", value = "验证登录", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "headerUserToken", value = "验证登录超时", required = true, dataType = "String", paramType = "header")
    })
    public IMoocJSONResult list(){

        return IMoocJSONResult.ok(bgmService.list());

    }
}
