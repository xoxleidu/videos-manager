package com.dulei.controller;

import com.dulei.pojo.Bgm;
import com.dulei.pojo.Videos;
import com.dulei.service.BgmService;
import com.dulei.service.VideoService;
import com.dulei.utils.*;
import com.dulei.utils.enums.VideoStatusEnum;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Date;


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
    @Value("${FTP_BGM_PATH}")
    private String FTP_BGM_PATH;
    @Value("${UPLOADFILES_ROOT_PATH}")
    private String UPLOADFILES_ROOT_PATH;
    @Value("${CLIPVIDEOFILES_EXT}")
    private String CLIPVIDEOFILES_EXT;
    @Value("${CLIPCOVERFILES_EXT}")
    private String CLIPCOVERFILES_EXT;

    @Value("${PAGE_SIZE}")
    private Integer PAGE_SIZE;

    @Autowired
    private BgmService bgmService;
    @Autowired
    private VideoService videoService;


    /**
     *
     * @Description: 分页和搜索查询视频列表
     * isSaveRecord：1 - 需要保存
     * 				 0 - 不需要保存 ，或者为空的时候
     *
     */
    @ApiOperation(value = "按用户查询视频",notes = "查询视频的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "isSaveRecord",value = "是否保存热搜",required = false,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "page",value = "当前页码", defaultValue = "1",required = false,dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "pageSize",value = "每页显示条数",defaultValue = "9",required = false,dataType = "Long",paramType = "query")
    })
    @PostMapping("/showAll")
    public IMoocJSONResult showAll(@RequestBody Videos video, Integer isSaveRecord,
                                         Integer page, Integer pageSize){

        if (page == null) {
            page = 1;
        }

        if (video.getUserId() == null && video.getVideoDesc() == null && isSaveRecord == null && pageSize == null){
            PagedResult likesVideosByDay = videoService.getLikesVideosByDay(page, 6);
            return IMoocJSONResult.ok(likesVideosByDay);
        }

        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }

        PagedResult result = videoService.getAllVideos(video, isSaveRecord, page, pageSize);
        return IMoocJSONResult.ok(result);
    }

    /**
     * 查询热搜词
     * @return
     */
    @ApiOperation(value = "查询热搜词",notes = "查询热搜词的接口")
    @PostMapping("/hot")
    public IMoocJSONResult hot(){
        return IMoocJSONResult.ok(videoService.getHots());
    }

    /**
     * 点赞
     * @return
     */
    @ApiOperation(value = "点赞",notes = "用户点赞接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户ID",required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "videoId",value = "视频ID",required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "videoCreateId",value = "视频发布者ID",required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "headerUserId", value = "验证登录", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "headerUserToken", value = "验证登录超时", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/likeVideo")
    public IMoocJSONResult likeVideo(String userId, String videoId, String videoCreateId) {
        videoService.userLikeVideo(userId,videoId,videoCreateId);
        return IMoocJSONResult.ok();
    }

    /**
     * 取消点赞
     * @return
     */
    @ApiOperation(value = "取消点赞",notes = "取消用户点赞接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户ID",required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "videoId",value = "视频ID",required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "videoCreateId",value = "视频发布者ID",required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "headerUserId", value = "验证登录", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "headerUserToken", value = "验证登录超时", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/unLikeVideo")
    public IMoocJSONResult unLikeVideo(String userId, String videoId, String videoCreateId) {
        videoService.userUnLikeVideo(userId,videoId,videoCreateId);
        return IMoocJSONResult.ok();
    }


    /**
     *
     * @Description: 上传视频
     * ftpUtil：单独服务器上传
     * ffmpeg: 上传服务器剪辑视频
     *
     */
    @ApiOperation(value = "上传视频",notes = "上传视频的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId", value="用户id", required=true, dataType="String", paramType="form"),
            @ApiImplicitParam(name="bgmId", value="背景音乐id", required=false, dataType="String", paramType="form"),
            @ApiImplicitParam(name="videoSeconds", value="背景音乐播放长度", required=true, dataType="String", paramType="form"),
            @ApiImplicitParam(name="videoWidth", value="视频宽度", required=true, dataType="String", paramType="form"),
            @ApiImplicitParam(name="videoHeight", value="视频高度", required=true, dataType="String", paramType="form"),
            @ApiImplicitParam(name="desc", value="视频描述", required=false, dataType="String", paramType="form"),
            @ApiImplicitParam(name = "headerUserId", value = "验证登录", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "headerUserToken", value = "验证登录超时", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/upload")
    public IMoocJSONResult upload(String userId, String bgmId, String desc,
                                  double videoSeconds, int videoWidth, int videoHeight,
                                  @ApiParam(value = "短视频",required = true)
                                  MultipartFile file) {

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
            String file_Upload_Path = userId + "/upload/" + newFilePathDB;

            /*System.out.println(FTP_ADDRESS);
            System.out.println(FTP_PORT);
            System.out.println(FTP_USERNAME);
            System.out.println(FTP_PASSWORD);
            System.out.println(FTP_USER_PATH);
            System.out.println(file_Upload_Path);
            System.out.println(newFileNameDB + ext);*/

            try {
                //把图片上传到ftp服务器（文件服务器）
                //需要把ftp的参数配置到配置文件中
                boolean upFileX = FtpUtil.uploadFile(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, FTP_USER_PATH,
                        file_Upload_Path, newFileNameDB + ext, file.getInputStream());
                if (!upFileX){
                    return IMoocJSONResult.errorMsg("文件上传中出错...");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return IMoocJSONResult.errorMsg("文件上传出错啦...");
            }

        } else {
            return IMoocJSONResult.errorMsg("文件不存在...");
        }

        //视频保存到数据库的路径(时间-文件名)
        String videoPathDB = newFilePathDB + "/" + newFileNameDB + CLIPVIDEOFILES_EXT;
        //视频截图保存到数据库的路径(时间-文件名)
        String coverPathDB = newFilePathDB + "/" + newFileNameDB + CLIPCOVERFILES_EXT;

        //FFmpeg剪辑视频的输入端路径(根目录-文件上传路径-用户ID-upload)
        String ffmpegInputPath = UPLOADFILES_ROOT_PATH + FTP_USER_PATH + "/" + userId + "/upload/";
        //FFmpeg剪辑视频的输出端路径(根目录-文件上传路径-用户ID-videos)
        String ffmpegOutputPath = UPLOADFILES_ROOT_PATH + FTP_USER_PATH + "/" + userId + "/videos/";

        //FFmpeg剪辑的输入端视频文件(输入端路径-文件上传路径-新文件名-源文件扩展名)
        String videoInputFile = ffmpegInputPath + newFilePathDB + "/" + newFileNameDB + ext;
        //FFmpeg剪辑的输出端视频文件(输出端路径-保存到数据库的路径)
        String videoOutPutFile = ffmpegOutputPath + videoPathDB;
        //FFmpeg剪辑的输出端视频文件的截图文件(输出端路径-保存到数据库的路径)
        String coverOutPutFile = ffmpegOutputPath + coverPathDB;

        //视频截取时间
        String time_coverimg = "00:00:01";
        //视频截取几帧(大于1多帧:GIF)
        int frame = 1;

        //FFmpeg剪辑视频的输出端路径
        String filePathMk = ffmpegOutputPath + newFilePathDB;

        //如果路径不存在，新建
        if (!FileBooleanUtil.mkdirs(filePathMk)) {
            return IMoocJSONResult.errorMsg("服务器忙");//创建失败前台显示
        }

        /*File newFlie = new File(filePathMk);
        if(!newFlie.exists() && !newFlie.isDirectory()) {
            boolean mkdirs = newFlie.mkdirs();
            System.out.println("创建目录啦。。。");
            if (!mkdirs){
                return IMoocJSONResult.errorMsg("创建剪辑后文件目录失败...");
            }
        }*/

        //视频剪辑
        try {
            //有音乐合成视频音频  否则直接转换
            if (StringUtils.isNotBlank(bgmId)){
                Bgm bgmResult = bgmService.queryBgmById(bgmId);
                String audioInputFile = UPLOADFILES_ROOT_PATH + FTP_BGM_PATH + bgmResult.getPath();
                FFMpegUtil.convetor(videoInputFile, audioInputFile, videoOutPutFile, videoSeconds);
            } else {
                FFMpegUtil.convetor(videoInputFile, videoOutPutFile);
            }
            // 对视频进行截图
            FFMpegUtil.convetor(time_coverimg,videoOutPutFile,frame,coverOutPutFile);
        } catch (Exception e) {
            e.printStackTrace();
            return IMoocJSONResult.errorMsg("服务器忙");//FFMPEG转换失败
        }

        //判断最后的截图文件是否存在，不存在表示失败，删除上传文件和转换后的文件
        if (!FileBooleanUtil.fileIsExists(coverOutPutFile)){
            FileBooleanUtil.delete(videoOutPutFile);
            FileBooleanUtil.delete(videoInputFile);
            return IMoocJSONResult.errorMsg("文件格式错误");//FFMPEG转换失败
        }

        /*System.out.println(videoInputFile);
        System.out.println(audioInputPath);
        System.out.println(videoOutPutFile);
        System.out.println(videoSeconds);*/

        // 保存视频信息到数据库
        Videos video = new Videos();
        video.setAudioId(bgmId);
        video.setUserId(userId);
        video.setVideoSeconds((float) videoSeconds);
        video.setVideoHeight(videoHeight);
        video.setVideoWidth(videoWidth);
        video.setVideoDesc(desc);
        video.setVideoPath(videoPathDB);
        video.setCoverPath(coverPathDB);
        video.setStatus(VideoStatusEnum.SUCCESS.value);
        video.setCreateTime(new Date());
        video.setLikeCounts((long) 0);
        String videoId = videoService.saveVideo(video);

        return IMoocJSONResult.ok(videoId);

    }


}
