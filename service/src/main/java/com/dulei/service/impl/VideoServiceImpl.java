package com.dulei.service.impl;

import com.dulei.mapper.VideosMapper;
import com.dulei.pojo.Videos;
import com.dulei.service.VideoService;
import com.dulei.utils.PagedResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideosMapper videosMapper;
    @Autowired
    private Sid sid;

    @Override
    public String saveVideo(Videos video) {
        String id = sid.nextShort();
        video.setId(id);
        videosMapper.insert(video);
        return id;
    }

    @Override
    public PagedResult getAllVideos(Videos video, Integer isSaveRecord, Integer page, Integer pageSize) {

        String userId = video.getUserId();
        List<Videos> videosList = showAllByUser(userId);

        PageHelper.startPage(page,pageSize);
        PageInfo pageInfoList = new PageInfo(videosList);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setRecords(pageInfoList.getTotal());
        pagedResult.setRows(videosList);
        pagedResult.setTotal(pageInfoList.getPages());

        System.out.println(page);
        System.out.println(pageInfoList.getTotal());
        System.out.println(pageSize);
        System.out.println(pageInfoList.getPages());

        return pagedResult;
    }

    @Override
    public List<Videos> showAllByUser(String userId) {
        Example videosExample = new Example(Videos.class);
        Example.Criteria criteria = videosExample.createCriteria();
        criteria.andEqualTo("userId",userId);
        return videosMapper.selectByExample(videosExample);
    }


}
