package com.ybg.meta.business.impl.security;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ybg.meta.api.security.api.UserService;
import com.ybg.meta.api.security.entity.User;
import com.ybg.meta.api.security.vo.LoginVo;
import com.ybg.meta.mapper.mapper.security.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author nanjustar
 * @since 2021-11-13
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public String userLogin(LoginVo loginVo) {
        log.info(loginVo.toString());
        log.info(loginVo.getUsername());
        log.info(loginVo.getPassword());
        log.info(loginVo.getCaptcha());
        return null;
    }
}
