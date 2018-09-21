package com.dulei.service;

import com.dulei.pojo.Comments;
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
    //  public PagedResult getLikesVideosByDay(Integer page, Integer dayBy);

    /**
     * 分页获取视频列表
     * @param video 所有视频or我发的视频(userId判断)or模糊查询所有视频(videoDesc判断)
     * @param isSaveRecord 是否保存热搜词
     * @param page 当前页
     * @param pageSize 每页条数
     * @return page list
     */
    public PagedResult getAllVideos(Videos video, Integer isSaveRecord, Integer page, Integer pageSize);

    public PagedResult getAllVideosByFollows(String userId, Integer page, Integer pageSize);

    public PagedResult getAllVideosByLikes(String userId, Integer page, Integer pageSize);

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

    /**
     * 保存留言
     * @param comments
     */
    public void saveComment(Comments comments);

    /**
     * 获取该视频留言
     * @param videoId 视频ID
     * @param page 当前页
     * @param pageSize 页数
     * @return
     */
    public PagedResult getVideoComments(String videoId, Integer page, Integer pageSize);

    /**
     * 获取该视频所有留言
     * @param videoId 视频ID
     * @return
     */
    public PagedResult getVideoAllComments(String videoId);

}
