package xyz.slienceme.tuyun.common.config;


import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
//import xyz.slienceme.tuyun.common.config.interceptor.SecurityInterceptor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author slience_me
 * @since 2022-01-15
 */
@Slf4j
@Configuration
@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer {


    @Value("${file.staticAccessPath}")
    String staticAccessPath;
    @Value("${file.fileupload}")
    String fileupload;

//    @Autowired
//    private SecurityInterceptor securityInterceptor;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("衡水大威箱包仓库管理系统接口文档")
                .description("衡水大威箱包仓库管理接口文档")
                .contact(
                        new Contact("slience_me", "https://www.slienceme.xyz/", "slience_me@foxmail.com")
                )
                .version("1.0.0")
                .build();
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始进行静态资源映射...");
        ApplicationHome ah = new ApplicationHome(getClass());
//        File parentPathStringLinux = ah.getSource().getParentFile().getParentFile().getParentFile();  //windows
        File parentPathStringLinux = ah.getSource().getParentFile();   //linux
        registry.addResourceHandler(staticAccessPath).addResourceLocations("file:" + parentPathStringLinux + fileupload);
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/", "classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/", "classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new DeviceResolverHandlerInterceptor());
//        InterceptorRegistration adminInterceptor = registry.addInterceptor(securityInterceptor);
//        adminInterceptor.excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**")
//                .excludePathPatterns("/admin/login")
//                .excludePathPatterns("/image/file/**")
//                .excludePathPatterns("/export/**")
//                .excludePathPatterns("/upload/img")
//                .excludePathPatterns("/static/**")
//                .excludePathPatterns("/qrcode/**")
//                .excludePathPatterns("/admin/rolelist/**");
//
//    }


    @Bean
    public DeviceResolverHandlerInterceptor deviceResolverHandlerInterceptor() {
        return new DeviceResolverHandlerInterceptor();
    }

    @Bean
    public DeviceHandlerMethodArgumentResolver
    deviceHandlerMethodArgumentResolver() {
        return new DeviceHandlerMethodArgumentResolver();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new DeviceHandlerMethodArgumentResolver());
    }

}