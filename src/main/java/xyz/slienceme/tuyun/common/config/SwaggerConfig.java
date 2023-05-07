package xyz.slienceme.tuyun.common.config;


import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
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
import xyz.slienceme.tuyun.common.config.interceptor.SecurityInterceptor;
//import xyz.slienceme.tuyun.common.config.interceptor.SecurityInterceptor;
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
@EnableSwaggerBootstrapUI
public class SwaggerConfig implements WebMvcConfigurer {


    @Value("${file.staticAccessPath}")
    String staticAccessPath;
    @Value("${image.staticAccessPath}")
    String imageStaticAccessPath;
    @Value("${file.fileupload}")
    String fileupload;
    @Value("${image.imagesupload}")
    private String imagesupload;
    @Value("${custom.system}")
    private String system;

    @Autowired
    private SecurityInterceptor securityInterceptor;

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
                .title("图云客户端接口文档")
                .description("图云客户端接口文档")
                .contact(
                        new Contact("slience_me", "https://www.slienceme.xyz/", "slience_me@foxmail.com")
                )
                .version("5.0.0")
                .build();
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("系统{},开始进行静态资源映射...",system);
        String parentPathString = new ApplicationHome(getClass()).getSource().getParentFile().toString();
        if ("windows".equals(system)){
            parentPathString = parentPathString.replace("/", "\\");
            fileupload = fileupload.replace("/", "\\");
            imagesupload = imagesupload.replace("/", "\\");
        }
        registry.addResourceHandler(staticAccessPath).addResourceLocations("file:" + parentPathString + fileupload);
        registry.addResourceHandler(imageStaticAccessPath).addResourceLocations("file:" + parentPathString + imagesupload);
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/", "classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/", "classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new DeviceResolverHandlerInterceptor());
        InterceptorRegistration adminInterceptor = registry.addInterceptor(securityInterceptor);
        adminInterceptor.excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**","/doc.html/**")
                .excludePathPatterns("/media/**")
                .addPathPatterns("/file")
                .addPathPatterns("/upload/**");
    }


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