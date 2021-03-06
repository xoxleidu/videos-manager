package com.dulei.controller;

import com.dulei.pojo.Users;
import com.dulei.pojo.redis.UsersLikeVideo;
import com.dulei.pojo.redis.UsersVO;
import com.dulei.service.UserService;
import com.dulei.utils.FtpUtil;
import com.dulei.utils.IDUtils;
import com.dulei.utils.IMoocJSONResult;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user")
@Api(value="用户操作的接口", tags= {"用户的controller"})
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${FTP_ADDRESS}")
    private String FTP_ADDRESS;
    @Value("${FTP_PORT}")
    private Integer FTP_PORT;
    @Value("${FTP_USERNAME}")
    private String FTP_USERNAME;
    @Value("${FTP_PASSWORD}")
    private String FTP_PASSWORD;
    @Value("${FTP_USER_PATH}")
    private String FTP_USER_PATH;

    @PostMapping("/uploadface")
    @ApiOperation(value="用户上传头像", notes="用户上传头像的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId", value="用户id", required=true, dataType="String", paramType="form"),
            @ApiImplicitParam(name = "headerUserId", value = "验证登录", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "headerUserToken", value = "验证登录超时", required = true, dataType = "String", paramType = "header")
    })
    public IMoocJSONResult uploadFace(String userId, @ApiParam(value = "短视频",required = true) MultipartFile file) {

        if (StringUtils.isBlank(userId)){
            return IMoocJSONResult.errorMsg("用户id不能为空...");
        }

        //根据时间+随机数生成文件名
        String newFileNameDB = IDUtils.genName();
        //源文件扩展名
        String ext = "";
        //根据时间YYYY/MM/DD生成新文件路径（数据库保存路径）
        String newFilePathDB = IDUtils.genPath();

        if (file != null){
            //取源文件名
            String fileName = file.getOriginalFilename();
            //取文件扩展名
            ext = fileName.substring(fileName.lastIndexOf("."));
            //文件上传后编辑前，在服务器的存放路径
            String file_Upload_Path = userId + "/face/" + newFilePathDB;

            try {
                //把图片上传到ftp服务器（文件服务器）
                //需要把ftp的参数配置到配置文件中
                boolean upFileX = FtpUtil.uploadFile(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, FTP_USER_PATH,
                        file_Upload_Path, newFileNameDB + ext, file.getInputStream());
                if (!upFileX){
                    return IMoocJSONResult.errorMsg("文件上传中出错...");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return IMoocJSONResult.errorMsg("文件上传出错啦...");
            }

        } else {
            return IMoocJSONResult.errorMsg("文件不存在...");
        }

        //保存到数据库的路径(时间-文件名)
        String facePathDB = newFilePathDB + "/" + newFileNameDB + ext;

        Users user = new Users();
        user.setId(userId);
        user.setFaceImage(facePathDB);
        userService.updateUserInfo(user);

        return IMoocJSONResult.ok(facePathDB);
    }

    @ApiOperation(value="查询用户信息", notes="查询用户信息的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId", value="用户id", required=true, dataType="String", paramType="query"),
            @ApiImplicitParam(name="fanId", value="查询用户是否关注", required=false, dataType="String", paramType="query"),
            @ApiImplicitParam(name = "headerUserId", value = "验证登录", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "headerUserToken", value = "验证登录超时", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/query")
    public IMoocJSONResult query(String userId, String fanId, String headerUserId, String headerUserToken){

        if (StringUtils.isBlank(userId)) {
            return IMoocJSONResult.errorMsg("用户id不能空...");
        }

        Users userInfo = userService.queryUserInfo(userId);

        if (userInfo != null){
            UsersVO usersVO = new UsersVO();
            BeanUtils.copyProperties(userInfo,usersVO);
            usersVO.setFollow(userService.queryIfFollow(userId,fanId));
            return IMoocJSONResult.ok(usersVO);
        }

        return IMoocJSONResult.errorMsg("用户不存在...");
    }

    @ApiOperation(value="查询用户与视频关系", notes="用户与视频关系接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId", value="当前用户id", required=false, dataType="String", paramType="query"),
            @ApiImplicitParam(name="videoId", value="当前视频id", required=true, dataType="String", paramType="query"),
            @ApiImplicitParam(name="videoCreateId", value="视频作者id", required=true, dataType="String", paramType="query")
    })
    @PostMapping("/queryIsLike")
    public IMoocJSONResult queryIsLike(String userId, String videoId, String videoCreateId){

        if (StringUtils.isBlank(videoCreateId)) {
            return IMoocJSONResult.errorMsg("");
        }
        Users userInfo = userService.queryUserInfo(videoCreateId);

        if (userInfo == null) {
            return IMoocJSONResult.errorMsg("用户不存在...");
        }
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(userInfo,usersVO);

        boolean bool = userService.queryIfLike(userId, videoId);

        UsersLikeVideo ulvResult = new UsersLikeVideo();
        ulvResult.setUsersVOResult(usersVO);
        ulvResult.setUserIsLikeVideo(bool);

        return IMoocJSONResult.ok(ulvResult);
    }

	@ApiOperation(value="保存用户关注信息", notes="保存用户关注信息的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId", value="用户id", required=true, dataType="String", paramType="query"),
            @ApiImplicitParam(name="fanId", value="关注id", required=true, dataType="String", paramType="query"),
            @ApiImplicitParam(name = "headerUserId", value = "验证登录", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "headerUserToken", value = "验证登录超时", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/saveUserFans")
    public IMoocJSONResult saveUserFans(String userId, String fanId, String headerUserId, String headerUserToken){
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)) {
            return IMoocJSONResult.errorMsg("");
        }
        userService.saveUserFans(userId, fanId);
        return IMoocJSONResult.ok("成功关注");
    }

	@ApiOperation(value="取消用户关注信息", notes="取消用户关注信息的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId", value="用户id", required=true, dataType="String", paramType="query"),
            @ApiImplicitParam(name="fanId", value="关注id", required=true, dataType="String", paramType="query"),
            @ApiImplicitParam(name = "headerUserId", value = "验证登录", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "headerUserToken", value = "验证登录超时", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/cleanUserFans")
    public IMoocJSONResult cleanUserFans(String userId, String fanId, String headerUserId, String headerUserToken){
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)) {
            return IMoocJSONResult.errorMsg("");
        }
        userService.cleanUserFans(userId, fanId);
        return IMoocJSONResult.ok("取消关注");
    }

}
