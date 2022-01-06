package com.ybg.meta.business.holder;

import cn.dev33.satoken.stp.StpUtil;
import com.ybg.meta.api.security.entity.User;
import com.ybg.meta.core.constant.RedisConst;
import com.ybg.meta.core.utils.RedisCache;
import com.ybg.meta.mapper.mapper.security.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * describe:
 *
 * @author admin
 * @date 2021/12/29 16:08
 */
@Component
public class CurrentUserHolder {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisCache redisCache;

    public User getCurrentUser() {
        int id = StpUtil.getLoginIdAsInt();
        Object object = redisCache.getCacheObject(String.format(RedisConst.USER_CACHE_KEY_PREFIX, id));
        if (object != null) {
            return (User) object;
        }
        User user = userMapper.selectById(id);
        if (user == null) {
            user = new User();
        }
        redisCache.setCacheObject(String.format(RedisConst.USER_CACHE_KEY_PREFIX, id), user, 30, TimeUnit.MINUTES);
        return user;
    }
}
