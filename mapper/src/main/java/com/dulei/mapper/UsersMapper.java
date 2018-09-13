package com.dulei.mapper;

import com.dulei.pojo.Users;
import com.dulei.utils.MyMapper;

public interface UsersMapper extends MyMapper<Users> {

	/**
     * 增加点赞视频数量
     * @param userId 用户ID
     */
    public void addReceiveLikeCounts(String userId);

	/**
     * 减少点赞视频数量
     * @param userId 用户ID
     */
    public void delReceiveLikeCounts(String userId);

	/**
     * 增加粉丝数
     * @param userId 用户ID
     */
    public void addUserFansCounts(String userId);

    /**
     * 减少粉丝数
     * @param userId 用户ID
     */
    public void delUserFansCounts(String userId);

	/**
     * 增加关注数
     * @param userId 用户ID
     */
    public void addUserFollowCounts(String userId);

    /**
     * 减少关注数
     * @param userId 用户ID
     */
    public void delUserFollowCounts(String userId);

}