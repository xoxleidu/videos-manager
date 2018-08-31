package com.dulei.controller;

import com.dulei.service.UserService;
import com.dulei.utils.IMoocJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@Api(value="用户操作的接口", tags= {"用户的controller"})
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${FTP_ADDRESS}")
    private String FTP_ADDRESS;

    @PostMapping("/uploadface")
    @ApiOperation(value="用户上传头像", notes="用户上传头像的接口")
    public IMoocJSONResult uploadFace(String userId,
             @RequestParam("facefile") MultipartFile[] file) {


        return IMoocJSONResult.ok();

    }
}
