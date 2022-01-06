package com.ybg.meta.core.page;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * describe:
 *
 * @author admin
 * @date 2021/12/27 14:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageVo {

    @ApiModelProperty(name = "current", value = "当前页", required = true, dataType = "long", example = "1")
    private long current;

    @ApiModelProperty(name = "size", value = "每页条数", required = true, dataType = "long", example = "8")
    private long size;
}
