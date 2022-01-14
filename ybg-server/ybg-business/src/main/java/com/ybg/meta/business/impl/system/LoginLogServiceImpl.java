package com.ybg.meta.business.impl.system;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ybg.meta.api.security.entity.User;
import com.ybg.meta.api.system.api.LoginLogService;
import com.ybg.meta.api.system.dto.LoginLogDto;
import com.ybg.meta.api.system.entity.LoginLog;
import com.ybg.meta.api.system.vo.LoginLogVo;
import com.ybg.meta.business.runtime.SecurityContextHolder;
import com.ybg.meta.core.constant.ServiceErrorConst;
import com.ybg.meta.core.result.PageResult;
import com.ybg.meta.core.utils.BeanCopyUtil;
import com.ybg.meta.core.utils.IpUtils;
import com.ybg.meta.mapper.mapper.system.LoginLogMapper;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author admin
 * @since 2021-12-04
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

    @Override
    public PageResult<LoginLogDto> listLoginLog(LoginLogVo loginLogVo) {
        /*------------------------  开始查询日志信息  -----------------------------*/
        Page<LoginLog> loginLogPage = baseMapper.selectPage(new Page<LoginLog>(loginLogVo.getCurrent(), loginLogVo.getSize()),
                new LambdaQueryWrapper<LoginLog>()
                        .like(StrUtil.isNotEmpty(loginLogVo.getNickname()), LoginLog::getNickname, loginLogVo.getNickname())
                        .like(StrUtil.isNotEmpty(loginLogVo.getIpSource()), LoginLog::getIpSource, loginLogVo.getIpSource())
                        .eq(ObjectUtil.isNotNull(loginLogVo.getStatus()), LoginLog::getStatus, loginLogVo.getStatus())
                        .between(StrUtil.isNotEmpty(loginLogVo.getBeginTime()) && StrUtil.isNotEmpty(loginLogVo.getEndTime()),
                                LoginLog::getGmtCreate, loginLogVo.getBeginTime(), loginLogVo.getEndTime())
                        .orderByDesc(LoginLog::getGmtCreate));
        /*------------------------  查询日志信息完成  -----------------------------*/
        /*------------------------  数据转换开始  -----------------------------*/
        List<LoginLogDto> loginLogDtos = BeanCopyUtil.copyList(loginLogPage.getRecords(), LoginLogDto.class);
        /*------------------------  数据转换完成  -----------------------------*/
        return new PageResult<>(loginLogDtos, loginLogPage.getTotal());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveLoginLog(HttpServletRequest request, Boolean success) {
        String usename = "";
        String nickname = "";

        if (success) {
            User currentLoginUser = SecurityContextHolder.getCurrentLoginUser();
            usename = currentLoginUser.getUsername();
            nickname = currentLoginUser.getNickname();
        }
        else {
            usename = request.getParameter("username");
            nickname = usename;
        }

        String ipAddress = IpUtils.getIpAddress(request);
        String ipSource = IpUtils.getIpSource(ipAddress);
        UserAgent userAgent = IpUtils.getUserAgent(request);

        LoginLog loginLog = LoginLog
                .builder()
                .username(usename)
                .nickname(nickname)
                .ipAddress(ipAddress)
                .ipSource(ipSource)
                .brower(userAgent.getBrowser().getName())
                .os(userAgent.getOperatingSystem().getName())
                .status(success ? 1 : 0)
                .gmtCreate(new Date())
                .remark(success ? "登陆成功！" : "用户名或密码错误！")
                .build();
        /*------------------------  开始添加数据  -----------------------------*/
        int insert = baseMapper.insert(loginLog);
        log.error(insert == 0 ? ServiceErrorConst.SAVE_DATA_FAIL : "OK");
        /*------------------------  添加数据完成  -----------------------------*/
    }
}
