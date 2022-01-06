const path = require('path');
const CompressionWebpackPlugin = require("compression-webpack-plugin"); // 开启gzip压缩， 按需引用

const productionGzipExtensions = ['js', 'css']; // 开启gzip压缩， 按需写入

const resolve = (dir) => {
    return path.join(__dirname, dir)
};

let proxyObj = {} // 代理对象

proxyObj['/v2/api'] = {
    // websocket
    ws: false,
    // 代理目标地址
    target: 'http://localhost:9000/ybg',
    // 发送请求头 host 会被设置 target
    changeOrigin: true,
    // 不重写请求地址
    pathRewrite: {
        '^/v2/api': ''
    }
}

// 在线聊天 代理
proxyObj['/ws'] = {
    ws: true,
    target: 'ws://localhost:8081'
}


// 访问的默认的路径和端口
module.exports = {
    devServer: {
        host: 'localhost',
        port: 8080,
        proxy: proxyObj // 代理
    },
    chainWebpack: config => {
        config.resolve.alias
            .set("vue$", "vue/dist/vue.esm.js")
            .set("@", resolve("src"))
            .set("@assets", resolve("src/assets"))
            .set("@api", resolve("src/api"))
            .set("@layout", resolve("src/layout"))
            .set("@utils", resolve("src/utils"))
            .set("@components", resolve("src/components"))
            .set("@views", resolve("src/views"))
            .set("@router", resolve("src/router"))
            .set("@store", resolve("src/store"));
    },
    configureWebpack: config => {
        const plugins = [];
        plugins.push(new CompressionWebpackPlugin({
            filename: "[path].gz[query]", // 压缩后的文件策略
            algorithm: "gzip", // 压缩方式
            test: new RegExp('\\.(' + productionGzipExtensions.join('|') + ')$'), // 可设置需要压缩的文件类型
            threshold: 10240, // 大于10240字节的文件启用压缩
            minRatio: 0.8, // 压缩比率大于等于0.8时不进行压缩
            deleteOriginalAssets: false, // 是否删除压缩前的文件，默认false
        }));
        config.plugins = [...config.plugins, ...plugins];
    }
}
