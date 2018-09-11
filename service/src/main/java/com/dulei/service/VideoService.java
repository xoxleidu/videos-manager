package com.dulei.service;

import com.dulei.pojo.Videos;
import com.dulei.utils.PagedResult;

import java.util.List;

public interface VideoService {

    /**
     * 保存视频
     * @param video 参数
     * @return video id
     */
    public String saveVideo (Videos video);

    /**
     * 分页获取最受欢迎视频列表-首页显示
     * 按 likeCounts 排序 dayBy 天内
     * @param page 当前页
     * @param dayBy 几天内
     * @return page list
     *
     */
    public PagedResult getLikesVideosByDay(Integer page, Integer dayBy);

    /**
     * 分页获取视频列表
     * @param video 可根据USERID或VIDEODESC查询，都不写查所有
     * @param isSaveRecord 是否保存热搜词
     * @param page 当前页
     * @param pageSize 每页条数
     * @return page list
     */
    public PagedResult getAllVideos(Videos video, Integer isSaveRecord, Integer page, Integer pageSize);

    /**
     * 查询热搜词
     * @return string list
     */
    public List<String> getHots();

    /**
     * 喜欢、点赞视频
     * @param userId 用户ID
     * @param videoId 视频ID
     * @param videoCreateId 发布视频用户ID
     */
    public void userLikeVideo(String userId, String videoId, String videoCreateId);

    /**
     * 取消喜欢、取消点赞视频
     * @param userId 用户ID
     * @param videoId 视频ID
     * @param videoCreateId 发布视频用户ID
     */
    public void userUnLikeVideo(String userId, String videoId, String videoCreateId);



}
