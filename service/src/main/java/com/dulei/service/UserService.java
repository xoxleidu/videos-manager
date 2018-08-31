package com.dulei.service;

import com.dulei.pojo.Users;

public interface UserService {

    public boolean queryUsernameIsExist(String username);

    public void saveUser(Users users);

    public Users queryUserForLogin(String username,String password);

}
