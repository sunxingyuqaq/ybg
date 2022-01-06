package com.ybg.meta.api.security.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ybg.meta.api.security.entity.User;
import com.ybg.meta.api.security.vo.LoginVo;

/**
 * describe:
 *
 * @author admin
 * @date 2021/12/29 10:09
 */
public interface UserService extends IService<User> {

    /**
     * 用户登陆
     *
     * @param loginVo 登陆信息 vo类
     * @return token 值
     */
    String userLogin(LoginVo loginVo);
}
