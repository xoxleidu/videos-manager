package com.dulei.mapper;

import com.dulei.pojo.Videos;
import com.dulei.pojo.redis.VideosVO;
import com.dulei.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideosCustomMapper extends MyMapper<Videos> {

    /**
     * @Description: 查询所有视频or我发的视频(userId判断)or模糊查询所有视频(videoDesc判断)
     */
    public List<VideosVO> queryAllVideos(@Param("videoDesc") String videoDesc,
                                         @Param("userId") String userId);

    /**
     * @Description: 查询我关注的人发的视频
     */
    public List<VideosVO> queryAllVideosByFollows(@Param("userId") String userId);

    /**
     * @Description: 查询我喜欢的视频
     */
    public List<VideosVO> queryAllVideosByLikes(@Param("userId") String userId);

    public void addLikeCounts(String videoId);
    public void delLikeCounts(String videoId);

}