package com.ybg.meta.business.runtime;

import cn.dev33.satoken.stp.StpUtil;
import com.ybg.meta.api.security.entity.User;
import com.ybg.meta.core.constant.RedisConst;

/**
 * describe:
 *
 * @author admin
 * @date 2021/12/29 16:08
 */
public class SecurityContextHolder {

    public static User getCurrentLoginUser() {
        String loginId = StpUtil.getLoginIdAsString();
        User model = StpUtil.getSession().getModel(String.format(RedisConst.USER_CACHE_KEY_PREFIX, loginId), User.class);
        if (model != null) {
            return model;
        }
        User anonymousUser = new User();
        anonymousUser.setUsername("未知的用户");
        return anonymousUser;
    }
}
