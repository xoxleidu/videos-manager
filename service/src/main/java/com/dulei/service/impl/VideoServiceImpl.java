package com.dulei.service.impl;

import com.dulei.mapper.*;
import com.dulei.pojo.Comments;
import com.dulei.pojo.SearchRecords;
import com.dulei.pojo.UsersLikeVideos;
import com.dulei.pojo.Videos;
import com.dulei.pojo.redis.CommentsVO;
import com.dulei.pojo.redis.VideosVO;
import com.dulei.service.VideoService;
import com.dulei.utils.PagedResult;
import com.dulei.utils.TimeAgoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideosMapper videosMapper;
    @Autowired
    private VideosCustomMapper videosCustomMapper;
    @Autowired
    private SearchRecordsMapper searchRecordsMapper;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private UsersLikeVideosMapper usersLikeVideosMapper;
    @Autowired
    private CommentsMapper commentsMapper;
    @Autowired
    private CommentsCustomMapper commentsCustomMapper;
    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String saveVideo(Videos video) {
        String id = sid.nextShort();
        video.setId(id);
        videosMapper.insert(video);
        return id;
    }

    /*@Transactional(propagation = Propagation.REQUIRED)
    @Override
    public PagedResult getLikesVideosByDay(Integer page, Integer dayBy) {

        PageHelper.startPage(page,21);
        List<VideosVO> videosVOList = videosCustomMapper.queryAllVideosByLikes(dayBy);

        PageInfo pageInfoList = new PageInfo(videosVOList);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setRecords(pageInfoList.getTotal());
        pagedResult.setRows(videosVOList);
        pagedResult.setTotal(pageInfoList.getPages());

        return pagedResult;

        *//*for (VideosVO v:videosVOList){
            System.out.println(JsonUtils.objectToJson(v));
        }*//*

        //过去七天
        *//*
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();

        c.setTime(new Date());
        c.add(Calendar.DATE, - 7);
        Date d = c.getTime();
        String day = format.format(d);
        System.out.println("过去七天："+ day);*//*

    }*/

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult getAllVideos(Videos video, Integer isSaveRecord, Integer page, Integer pageSize) {

        String desc = video.getVideoDesc();
        String userId = video.getUserId();

        //System.out.println(desc + userId);

        // 保存热搜词
        if (isSaveRecord != null && isSaveRecord == 1) {
            SearchRecords record = new SearchRecords();
            String recordId = sid.nextShort();
            record.setId(recordId);
            record.setContent(desc);
            searchRecordsMapper.insert(record);
        }

        PageHelper.startPage(page,pageSize);
        List<VideosVO> videosVOList = videosCustomMapper.queryAllVideos(desc,userId);

        PageInfo pageInfoList = new PageInfo(videosVOList);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setRecords(pageInfoList.getTotal());
        pagedResult.setRows(videosVOList);
        pagedResult.setTotal(pageInfoList.getPages());

        /*System.out.println(page);
        System.out.println(pageInfoList.getTotal());
        System.out.println(pageSize);
        System.out.println(pageInfoList.getPages());*/

        return pagedResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult getAllVideosByFollows(String userId, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<VideosVO> videosVOList = videosCustomMapper.queryAllVideosByFollows(userId);

        PageInfo pageInfoList = new PageInfo(videosVOList);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setRecords(pageInfoList.getTotal());
        pagedResult.setRows(videosVOList);
        pagedResult.setTotal(pageInfoList.getPages());
        return pagedResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult getAllVideosByLikes(String userId, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<VideosVO> videosVOList = videosCustomMapper.queryAllVideosByLikes(userId);

        PageInfo pageInfoList = new PageInfo(videosVOList);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setRecords(pageInfoList.getTotal());
        pagedResult.setRows(videosVOList);
        pagedResult.setTotal(pageInfoList.getPages());
        return pagedResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<String> getHots() {return searchRecordsMapper.getHots();}

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void userLikeVideo(String userId, String videoId, String videoCreateId) {
        String id = sid.nextShort();
        UsersLikeVideos ulv = new UsersLikeVideos();
        ulv.setId(id);
        ulv.setUserId(userId);
        ulv.setVideoId(videoId);

        // 1. 保存用户和视频的喜欢点赞关联关系表
        usersLikeVideosMapper.insert(ulv);
        // 2. 视频喜欢数量累加
        videosCustomMapper.addLikeCounts(videoId);
        // 3. 用户受喜欢数量的累加
        usersMapper.addReceiveLikeCounts(videoCreateId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void userUnLikeVideo(String userId, String videoId, String videoCreateId) {
        Example example = new Example(UsersLikeVideos.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("videoId",videoId);

        usersLikeVideosMapper.deleteByExample(example);
        videosCustomMapper.delLikeCounts(videoId);
        usersMapper.delReceiveLikeCounts(videoCreateId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveComment(Comments comments) {
        String id = sid.nextShort();
        comments.setId(id);
        commentsMapper.insert(comments);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult getVideoComments(String videoId, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<CommentsVO> commentsVOS = commentsCustomMapper.queryVideoComments(videoId);

        for (CommentsVO c : commentsVOS) {
            String timeAgo = TimeAgoUtils.format(c.getCreateTime());
            c.setTimeAgoStr(timeAgo);
        }

        PageInfo pageInfoList = new PageInfo(commentsVOS);
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setRecords(pageInfoList.getTotal());
        pagedResult.setRows(commentsVOS);
        pagedResult.setTotal(pageInfoList.getPages());

        return pagedResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult getVideoAllComments(String videoId) {
        List<CommentsVO> commentsVOS = commentsCustomMapper.queryVideoComments(videoId);

        for (CommentsVO c : commentsVOS) {
            String timeAgo = TimeAgoUtils.format(c.getCreateTime());
            c.setTimeAgoStr(timeAgo);
        }

        PagedResult pagedResult = new PagedResult();
        pagedResult.setRows(commentsVOS);

        return pagedResult;
    }


}
