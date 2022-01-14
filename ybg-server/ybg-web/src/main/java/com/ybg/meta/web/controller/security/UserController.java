package com.ybg.meta.web.controller.security;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ybg.meta.api.security.dto.UserInfoDto;
import com.ybg.meta.api.security.entity.User;
import com.ybg.meta.api.security.vo.LoginVo;
import com.ybg.meta.business.runtime.SecurityContextHolder;
import com.ybg.meta.core.constant.CommonConst;
import com.ybg.meta.core.constant.RedisConst;
import com.ybg.meta.core.enums.HttpResponseEnum;
import com.ybg.meta.core.enums.RoleLevelEnum;
import com.ybg.meta.core.result.ResponseResult;
import com.ybg.meta.core.utils.BeanCopyUtil;
import com.ybg.meta.mapper.mapper.security.UserMapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * describe:
 *
 * @author admin
 * @date 2021/12/29 12:23
 */
@Slf4j
@RestController
public class UserController {

    @Resource
    private UserMapper userMapper;

    @PostMapping("login")
    public ResponseResult<String> login(@RequestBody LoginVo loginVo) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, loginVo.getUsername())
                .eq(User::getIsDisable, CommonConst.NOT_DISABLE)
                .eq(User::getDelFlag, CommonConst.NOT_DELETE)
                .eq(User::getPassword, DigestUtil.md5Hex(loginVo.getPassword())));
        if (user != null) {
            SaLoginModel model = SaLoginModel.create()
                    .setDevice("pc")
                    .setIsLastingCookie(loginVo.getRememberMe());
            StpUtil.login(user.getUserId(), model);
            StpUtil.getSession().set(String.format(RedisConst.USER_CACHE_KEY_PREFIX, user.getUserId()), user);
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            Dict set = Dict.create()
                    .set("tokenName", SaManager.getConfig().getTokenName())
                    .set("tokenPrefix", SaManager.getConfig().getTokenPrefix())
                    .set("tokenValue", tokenInfo.getTokenValue());
            return ResponseResult.success("登录成功！", JSON.toJSONString(set));
        }
        return ResponseResult.fail(HttpResponseEnum.USER_USERNAME_AND_PASSWORD_NOT_CORRECT);
    }

    /**
     * 获取当前用户信息
     *
     * @return {@link UserInfoDto} 当前用户信息
     */
    @ApiOperation(value = "获取用户信息")
    @GetMapping("/getUserInfo")
    public ResponseResult<UserInfoDto> getUserInfo() {
        UserInfoDto userinfo = BeanCopyUtil.copyObject(SecurityContextHolder.getCurrentLoginUser(), UserInfoDto.class);
        List<String> roleList = StpUtil.getRoleList();
        userinfo.setRoleList(roleList);
        userinfo.setConsumerLevel(RoleLevelEnum.getGenderNameByCode(userinfo.getRoleList()));
        return ResponseResult.success("用户信息获取成功！", userinfo);
    }

}
