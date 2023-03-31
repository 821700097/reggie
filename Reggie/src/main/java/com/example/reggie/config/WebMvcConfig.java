package com.example.reggie.config;

import com.alibaba.fastjson.serializer.JSONLibDataFormatSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始静态资源映射..");
       registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");

    }

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("extendMessage converters began......");
        //jackson的设置问题，jackson2的默认设置中没有对应的long类型转化string的方法，需要使用手动添加的方式为程序注入对应的转化序列
        MappingJackson2HttpMessageConverter messageConverter=new MappingJackson2HttpMessageConverter();
        Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder=new Jackson2ObjectMapperBuilder();
        jackson2ObjectMapperBuilder.serializerByType(Long.class,ToStringSerializer.instance);
        jackson2ObjectMapperBuilder.dateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
        ObjectMapper build = jackson2ObjectMapperBuilder.build();
        messageConverter.setObjectMapper(build);

        converters.add(0,messageConverter);

    }

    //    @Override
//    protected void extendMessageConverters(List<HttpMessageConverter<?>> converterList){
//        log.info("extend message converter...");
//
//        MappingJackson2HttpMessageConverter messageConverter=new MappingJackson2HttpMessageConverter();
//        Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder = new Jackson2ObjectMapperBuilder();
//        ObjectMapper build = jackson2ObjectMapperBuilder.build();
//        messageConverter.setObjectMapper(build);
//        converterList.add(0,messageConverter);
//    }
}
