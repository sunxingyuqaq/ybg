package com.ybg.meta.business.impl.system;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ybg.meta.api.system.api.OptLogService;
import com.ybg.meta.api.system.dto.OptLogBackDto;
import com.ybg.meta.api.system.entity.OptLog;
import com.ybg.meta.api.system.vo.OptLogVo;
import com.ybg.meta.core.enums.LogEnum;
import com.ybg.meta.core.result.PageResult;
import com.ybg.meta.core.utils.BeanCopyUtil;
import com.ybg.meta.mapper.mapper.system.OptLogMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author nanjustar
 * @since 2021-11-13
 */
@Service
public class OptLogServiceImpl extends ServiceImpl<OptLogMapper, OptLog> implements OptLogService {

    @Override
    public PageResult<OptLogBackDto> listOptLogs(OptLogVo optLogVo) {
        /*------------------------  开始查询日志信息  -----------------------------*/
        Page<OptLog> optLogPage = baseMapper.selectPage(new Page<>(optLogVo.getCurrent(), optLogVo.getSize()),
                new LambdaQueryWrapper<OptLog>()
                        .like(StrUtil.isNotEmpty(optLogVo.getOptLogName()), OptLog::getOptName, optLogVo.getOptLogName())
                        .orderByDesc(OptLog::getOptTime));
        /*------------------------  查询日志信息完成  -----------------------------*/
        /*------------------------  数据转换开始  -----------------------------*/
        List<OptLogBackDto> list = new ArrayList<>();
        optLogPage.getRecords().forEach(log -> {
            OptLogBackDto logBackDto = BeanCopyUtil.copyObject(log, OptLogBackDto.class);
            logBackDto.setBusinessType(LogEnum.getOptLogNameByCode(log.getBusinessType()));
            list.add(logBackDto);
        });
        /*------------------------  数据转换完成  -----------------------------*/
        return new PageResult<>(list, optLogPage.getTotal());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteOptLog(Integer id) {
        /*------------------------  开始删除日志信息  -----------------------------*/
        baseMapper.deleteById(id);
        /*------------------------  删除日志信息完成  -----------------------------*/
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteOptLogs(List<Integer> idList) {
        /*------------------------  开始批量删除日志信息  -----------------------------*/
        baseMapper.deleteBatchIds(idList);
        /*------------------------  删除批量日志信息完成  -----------------------------*/
    }
}
