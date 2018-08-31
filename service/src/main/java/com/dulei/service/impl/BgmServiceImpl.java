package com.dulei.service.impl;

import com.dulei.mapper.BgmMapper;
import com.dulei.pojo.Bgm;
import com.dulei.service.BgmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BgmServiceImpl implements BgmService {

    @Autowired
    private BgmMapper bgmMapper;

    @Override
    public List<Bgm> list() {
        return bgmMapper.selectAll();
    }
}