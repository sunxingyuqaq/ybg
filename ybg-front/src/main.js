import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'animate.css'
import 'element-ui/lib/theme-chalk/index.css'

//=======================  JS  ===========================
import config from "@/assets/js/config"

//=======================  Utils  ===========================
import {initMenu} from './utils/menu'

// 滚动条
import Nprogress from 'nprogress'
import {getUserInfo} from "./api/consumer/consumerApi"

Vue.config.productionTip = false
Vue.use(ElementUI)
Vue.prototype.config = config;

//=======================  路由守卫  ===========================
router.beforeEach(((to, from, next) => {
    Nprogress.start();
    if (window.sessionStorage.getItem('token')) {
        if (router.options.routes[2].children.length <= 1) {
            initMenu(router, store);
        }
        if (!window.sessionStorage.getItem('userInfo')) {
            getUserInfo()
                .then(res => {
                    store.dispatch('setUserInfo', res.data)
                });
        }
        next();
    } else {
        if (to.path === '/login') {
            next();
        } else {
            next('/login');
        }
    }
}));

router.afterEach(((to, from) => {
    Nprogress.done();
}));

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')
