package com.dulei.service.impl;

import com.dulei.mapper.SearchRecordsMapper;
import com.dulei.mapper.VideosMapper;
import com.dulei.mapper.VideosMapperCustomMapper;
import com.dulei.pojo.SearchRecords;
import com.dulei.pojo.Videos;
import com.dulei.pojo.redis.VideosVO;
import com.dulei.service.VideoService;
import com.dulei.utils.PagedResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
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
    private VideosMapperCustomMapper videosMapperCustomMapper;
    @Autowired
    private SearchRecordsMapper searchRecordsMapper;
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

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public PagedResult getAllVideos(Videos video, Integer isSaveRecord, Integer page, Integer pageSize) {

        String desc = video.getVideoDesc();
        String userId = video.getUserId();

        System.out.println(desc + userId);

        // 保存热搜词
        if (isSaveRecord != null && isSaveRecord == 1) {
            SearchRecords record = new SearchRecords();
            String recordId = sid.nextShort();
            record.setId(recordId);
            record.setContent(desc);
            searchRecordsMapper.insert(record);
        }

        PageHelper.startPage(page,pageSize);
        List<VideosVO> videosVOList = videosMapperCustomMapper.queryAllVideos(desc,userId);

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
    public List<String> getHots() {
        return searchRecordsMapper.getHots();
    }


}
