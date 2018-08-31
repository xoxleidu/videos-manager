package com.dulei.controller;

import com.dulei.pojo.Users;
import com.dulei.pojo.redis.UsersVO;
import com.dulei.service.UserService;
import com.dulei.utils.IMoocJSONResult;
import com.dulei.utils.MD5Utils;
import com.dulei.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Api(value="用户注册登录的接口", tags= {"注册和登录的controller"})
public class RegistLoginController {

    @Value("${USER_REDIS_SESSION}")
    private String USER_REDIS_SESSION;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redisUtil;

    @PostMapping("/regist")
    @ApiOperation(value="用户注册", notes="用户注册的接口")
    public IMoocJSONResult regist(@RequestBody Users user) throws Exception{

        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())){
            return IMoocJSONResult.errorMsg("用户名密码不能空");
        }

        boolean usernameIsExist = userService.queryUsernameIsExist(user.getUsername());

        if (!usernameIsExist){
            user.setNickname(user.getUsername());
            user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
            user.setFansCounts(0);
            user.setReceiveLikeCounts(0);
            user.setFollowCounts(0);
            userService.saveUser(user);
        }else {
            return IMoocJSONResult.errorMsg("用户名重复");
        }

        user.setPassword("dulei");
        return IMoocJSONResult.ok(user);

    }

    @ApiOperation(value="用户登录", notes="用户登录的接口")
    @PostMapping("/login")
    public IMoocJSONResult login(@RequestBody Users user) throws Exception {
        String username = user.getUsername();
        String password = user.getPassword();
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            return IMoocJSONResult.errorMsg("用户名密码不能空");
        }

        Users result = userService.queryUserForLogin(username,
                MD5Utils.getMD5Str(user.getPassword()));

        if (result != null){
            user.setPassword("dulei");

            //redis
            //setUserRedisSessionToken(user);

            return IMoocJSONResult.ok(setUserRedisSessionToken(result));
        } else {
            return IMoocJSONResult.errorMsg("用户名密码不正确, 请重试...");
        }

    }

    @ApiOperation(value="用户注销", notes="用户注销的接口")
    @ApiImplicitParam(name="userId", value="用户id", required=true,
            dataType="String", paramType="query")
    @PostMapping("/logout")
    public IMoocJSONResult logout(String userId){
        redisUtil.del(USER_REDIS_SESSION + userId );
        return IMoocJSONResult.ok();
    }

    /**
     * 保存redis
     * @param userModel
     * @return
     */
    private UsersVO setUserRedisSessionToken(Users userModel){
        String uuidUserToken = UUID.randomUUID().toString();
        redisUtil.set(USER_REDIS_SESSION + userModel.getId(),uuidUserToken,1000 * 60 *30);

        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(userModel,usersVO);
        usersVO.setUserToken(uuidUserToken);

        return usersVO;
    }


}
