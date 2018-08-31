package com.dulei.service.impl;

import com.dulei.mapper.UsersMapper;
import com.dulei.pojo.Users;
import com.dulei.service.UserService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;

    @Override
    public boolean queryUsernameIsExist(String username) {
        Users users = new Users();
        users.setUsername(username);

        Users selectOne = usersMapper.selectOne(users);
        return selectOne == null ? false : true;
    }

    @Override
    public void saveUser(Users users) {
        String userId = sid.nextShort();
        users.setId(userId);
        usersMapper.insert(users);
    }

    @Override
    public Users queryUserForLogin(String username, String password) {
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("username",username);
        criteria.andEqualTo("password",password);

        Users user = usersMapper.selectOneByExample(userExample);
        return user;

    }
}
