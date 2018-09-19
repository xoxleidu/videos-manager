package com.dulei.mapper;

import com.dulei.pojo.Comments;
import com.dulei.pojo.redis.CommentsVO;
import com.dulei.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentsCustomMapper extends MyMapper<Comments> {

    public List<CommentsVO> queryVideoComments(@Param("videoId") String videoId);

}