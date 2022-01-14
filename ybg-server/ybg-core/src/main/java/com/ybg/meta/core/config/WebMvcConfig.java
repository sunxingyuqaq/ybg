package com.ybg.meta.core.config;

import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.ybg.meta.core.constant.SecurityConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * describe:
 *
 * @author admin
 * @date 2021/12/29 10:32
 */
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedOriginPatterns("*")
                .allowedMethods("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String collect = String.join(",", SecurityConst.WHITE_PAGE_LIST);
        log.info("【本系统配置的白名单地址：】 {}", collect);
        registry.addInterceptor(new SaRouteInterceptor((req, res, handler) -> {
                    SaRouter.match("/**").notMatch(SecurityConst.WHITE_PAGE_LIST).check(r -> StpUtil.checkLogin());
                    SaRouter.match("/dict/**", r -> StpUtil.hasPermission("/dict"));
                    SaRouter.match("/loginLog/**", r -> StpUtil.hasPermission("/log-sunmenu"));
                    SaRouter.match("/optLog/**", r -> StpUtil.hasPermission("/optlog"));
                }))
                .addPathPatterns("/**");
    }
}
