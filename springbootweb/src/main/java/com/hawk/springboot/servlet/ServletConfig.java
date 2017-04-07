package com.hawk.springboot.config;

import com.google.code.kaptcha.servlet.KaptchaServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by yannfeng on 2016/11/8.
 */
@Configuration
public class ServletConfig {

    @Bean
    public ServletRegistrationBean registrationKaptchaServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new KaptchaServlet(), "/kaptcha.jpg");
        bean.addInitParameter("kaptcha.session.key", com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        bean.addInitParameter("kaptcha.border", "no");
        bean.addInitParameter("kaptcha.textproducer.font.color", "black");
        bean.addInitParameter("kaptcha.textproducer.char.space", "5");
        return bean;
    }
}
