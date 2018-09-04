package com.dulei.service;

import com.dulei.pojo.Videos;
import com.dulei.utils.PagedResult;

import java.util.List;

public interface VideoService {

    public String saveVideo (Videos video);

    PagedResult getAllVideos(Videos video, Integer isSaveRecord, Integer page, Integer pageSize);

    public List<Videos> showAllByUser(String userId);

}
