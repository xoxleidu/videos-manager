package com.dulei.controller;

import com.dulei.utils.FtpUtil;
import com.dulei.utils.IDUtils;
import com.dulei.utils.IMoocJSONResult;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;


@RestController
@RequestMapping("/video")
@Api(value="视频接口", tags= {"视频相关controller"})
public class VideoController {

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

    @ApiOperation(value = "上传视频",notes = "上传视频的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId", value="用户id", required=true,
                    dataType="String", paramType="form"),
            @ApiImplicitParam(name="bgmId", value="背景音乐id", required=false,
                    dataType="String", paramType="form"),
            @ApiImplicitParam(name="videoSeconds", value="背景音乐播放长度", required=true,
                    dataType="String", paramType="form"),
            @ApiImplicitParam(name="videoWidth", value="视频宽度", required=true,
                    dataType="String", paramType="form"),
            @ApiImplicitParam(name="videoHeight", value="视频高度", required=true,
                    dataType="String", paramType="form"),
            @ApiImplicitParam(name="desc", value="视频描述", required=false,
                    dataType="String", paramType="form")
    })
    @PostMapping("/upload")
    public IMoocJSONResult upload(String userId, String bgmId, String desc,
                                  double videoSeconds, int videoWidth, int videoHeight,
                                  @ApiParam(value = "短视频",required = true)
                                  MultipartFile file) {

        if (StringUtils.isBlank(userId)){
            return IMoocJSONResult.errorMsg("用户id不能为空...");
        }

        InputStream in = null;

        if (file != null){

            //取文件扩展名
            String fileName = file.getOriginalFilename();
            String ext = fileName.substring(fileName.lastIndexOf("."));
            //生成新文件名
            //可以是时间+随机数生成文件名
            String filePath = IDUtils.genPath();
            String newFileName = IDUtils.genName();
            //把图片上传到ftp服务器（文件服务器）
            //需要把ftp的参数配置到配置文件中
            //文件在服务器的存放路径，应该使用日期分隔的目录结构
            String finalVideoPath = userId + "/" + filePath;
            System.out.println(FTP_ADDRESS+FTP_PORT+FTP_USERNAME+FTP_PASSWORD+FTP_USER_PATH+
                    finalVideoPath + newFileName + ext);
            try {
                boolean upFileX = FtpUtil.uploadFile(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, FTP_USER_PATH,
                        finalVideoPath, newFileName + ext, file.getInputStream());
                return upFileX ? IMoocJSONResult.ok() : IMoocJSONResult.errorMsg("文件上传中出错...");
            } catch (IOException e) {
                e.printStackTrace();
                return IMoocJSONResult.errorMsg("文件上传出错啦...");
            }

        } else {
            return IMoocJSONResult.errorMsg("文件不存在...");
        }
    }
}
