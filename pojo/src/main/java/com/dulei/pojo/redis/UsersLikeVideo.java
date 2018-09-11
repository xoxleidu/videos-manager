package com.dulei.pojo.redis;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;

@ApiModel(value="用户对象", description="这是用户对象")
public class UsersLikeVideo {

    private UsersVO usersVOResult;
    private boolean userIsLikeVideo;

    public UsersVO getUsersVOResult() {
        return usersVOResult;
    }

    public UsersLikeVideo setUsersVOResult(UsersVO usersVOResult) {
        this.usersVOResult = usersVOResult;
        return this;
    }

    public boolean isUserIsLikeVideo() {
        return userIsLikeVideo;
    }

    public UsersLikeVideo setUserIsLikeVideo(boolean userIsLikeVideo) {
        this.userIsLikeVideo = userIsLikeVideo;
        return this;
    }
}