package com.dulei.mapper;

import com.dulei.pojo.Videos;
import com.dulei.pojo.redis.VideosVO;
import com.dulei.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideosMapperCustomMapper extends MyMapper<Videos> {

    /**
     * @Description: 条件查询所有视频列表
     */
    public List<VideosVO> queryAllVideos(@Param("videoDesc") String videoDesc,
                                         @Param("userId") String userId);

}