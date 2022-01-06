package com.ybg.meta.mapper.handle;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ybg.meta.api.security.entity.User;
import com.ybg.meta.core.constant.RedisConst;
import com.ybg.meta.core.utils.RedisCache;
import com.ybg.meta.core.utils.SpringUtils;
import com.ybg.meta.mapper.mapper.security.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 时间信息自动注入
 *
 * @author sxy
 */
@Slf4j
@Component
public class MeatObjectHandlerImpl implements MetaObjectHandler {

    @Autowired
    private RedisCache redisCache;

    @Override
    public void insertFill(MetaObject metaObject) {
        if (log.isDebugEnabled()) {
            log.debug("insertFill @ {}", LocalDate.now());
        }
        this.setFieldValByName("gmtCreate", new Date(), metaObject);
        this.setFieldValByName("gmtUpdate", new Date(), metaObject);
        this.setFieldValByName("lastLoginTime", new Date(), metaObject);
        this.setFieldValByName("createBy", getCurrentUserName(), metaObject);
        this.setFieldValByName("updateBy", getCurrentUserName(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("gmtUpdate", new Date(), metaObject);
        this.setFieldValByName("updateBy", getCurrentUserName(), metaObject);
    }

    private String getCurrentUserName() {
        String id = StpUtil.getLoginIdAsString();
        Object object = redisCache.getCacheObject(String.format(RedisConst.USER_CACHE_KEY_PREFIX, id));
        if (object != null) {
            User u = (User) object;
            return u.getUsername();
        }
        UserMapper userMapper = SpringUtils.getBean(UserMapper.class);
        User user = userMapper.selectById(id);
        if (user == null) {
            user = new User();
        }
        redisCache.setCacheObject(String.format(RedisConst.USER_CACHE_KEY_PREFIX, id), user, 30, TimeUnit.MINUTES);
        return user.getUsername();
    }
}
