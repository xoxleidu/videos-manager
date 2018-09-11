package com.dulei.mapper;

import com.dulei.pojo.Users;
import com.dulei.utils.MyMapper;

public interface UsersMapper extends MyMapper<Users> {

    public void addReceiveLikeCounts(String userId);
    public void delReceiveLikeCounts(String userId);
}