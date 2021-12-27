/**
 * 时间转换
 * @param str 标准时间
 * @returns {string}
 */
export const formatTimeToYMD = (str) =>{
    return str.getFullYear() + '-' + (str.getMonth() + 1) + '-' + str.getDate() + ' ' + str.getHours() + ':' + str.getMinutes() + ':' + str.getSeconds();
}