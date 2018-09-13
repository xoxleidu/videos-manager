package com.dulei.service;

import com.dulei.pojo.Users;

public interface UserService {

    /**
     * @Description: 判断用户名是否存在
     */
    public boolean queryUsernameIsExist(String username);

    /**
     * @Description: 保存用户(用户注册)
     */
    public void saveUser(Users users);

    /**
     * @Description: 用户登录，根据用户名和密码查询用户
     */
    public Users queryUserForLogin(String username,String password);

    /**
     * @Description: 用户修改信息
     */
    public void updateUserInfo(Users user);

    /**
     * @Description: 查询用户信息
     */
    public Users queryUserInfo(String userId);

    /**
     * @Description: 查询用户是否关注
     */
    public boolean queryIfFollow(String userId, String fanId);

    /**
     * @Description: 查询用户是否点赞视频
     */
    public boolean queryIfLike(String userId, String videoId);

	/**
     * @Description: 保存用户关注信息
     */
	public void saveUserFans(String userId, String fanId);

	/**
     * @Description: 取消用户关注信息
     */
	public void cleanUserFans(String userId, String fanId);

}
