package top.gerritchang.tools.configuration;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.gerritchang.tools.interceptor.CheckInterceptor;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Bean
    public HttpMessageConverters useConverters() {
        return new HttpMessageConverters(new FastJsonHttpMessageConverter());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/").setViewName("index");
        registry.addViewController("index").setViewName("index");
        registry.addViewController("sqlGenerate").setViewName("sqlGenerate");
        registry.addViewController("mybatisSqlGenerate").setViewName("mybatisInsertUpdate");
        registry.addViewController("draggable").setViewName("draggable");
        registry.addViewController("setupMenu").setViewName("setupMenu");
        registry.addViewController("columns").setViewName("columns");
        registry.addViewController("upload").setViewName("upload");
        registry.addViewController("websocket").setViewName("websocketTest");
        registry.addViewController("downloadPage").setViewName("download");
        registry.addViewController("import").setViewName("importExcel");
        registry.addViewController("foreach").setViewName("foreach");
        registry.addViewController("activation").setViewName("activation");
        registry.addViewController("setup").setViewName("setup");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/activation","/","/index","/queryMenuList","/bootstrap/**","/images/**","/json/**","/locale/**",
                        "/pages/**","/plugins/**","/public/**","/src/**","/style/**","/themes/**","/vue/**"
                        ,"/easyloader.js","/jquery.easyui.min.js","/jquery.min.js");
    }
}
