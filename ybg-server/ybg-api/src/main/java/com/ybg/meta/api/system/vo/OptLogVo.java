package com.ybg.meta.api.system.vo;

import com.ybg.meta.core.page.PageVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptLogVo extends PageVo {

    /**
     * 日志信息
     */
    private String optLogName;

}
