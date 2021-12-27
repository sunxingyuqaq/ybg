import {getRequest} from "../../../utils/request";

/**
 * 查询登陆日志信息
 * @param params 参数
 */
export const listLoginLog = (params) => {
    return getRequest('/loginLog/list', params);
};