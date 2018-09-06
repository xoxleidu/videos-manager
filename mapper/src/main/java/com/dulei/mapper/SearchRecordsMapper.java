package com.dulei.mapper;

import com.dulei.pojo.SearchRecords;
import com.dulei.utils.MyMapper;

import java.util.List;

public interface SearchRecordsMapper extends MyMapper<SearchRecords> {

    /**
     * 查询热搜词
     * @return string list
     */
    public List<String> getHots();
}