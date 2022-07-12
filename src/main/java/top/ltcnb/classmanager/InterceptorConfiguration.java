package top.ltcnb.classmanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.ltcnb.classmanager.Entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Objects;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {
    StringRedisTemplate stringRedisTemplate;

    public InterceptorConfiguration(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] excludePath = {"/user/login", "/user/register", "/article/articleList/**","/article/info/**", "/error"};
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                String id, token;
                if ((token = request.getHeader("token")) != null && (id = request.getHeader("id")) != null) {
                    return Objects.equals(stringRedisTemplate.opsForValue().get(id), token);
                }
                return false;
            }
        }).addPathPatterns("/**").excludePathPatterns(excludePath);

    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowCredentials(true).allowedOriginPatterns("**/**");
    }
}
