/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package np.com.ngopal.spring.boot.redis.config;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import np.com.ngopal.spring.boot.redis.jwt.JWTAuthenticationFilter;
import np.com.ngopal.spring.boot.redis.jwt.JWTLoginFilter;
import np.com.ngopal.spring.boot.redis.jwt.TokenAuthenticationService;
import np.com.ngopal.spring.boot.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 *
 * @author Narayan <me@ngopal.com.np>
 */
@Slf4j
@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private TokenAuthenticationService tokenService;

    @Autowired
    private RedisService redisService;

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(Arrays.asList((AuthenticationProviderImpl) new AuthenticationProviderImpl(redisService)));
    }

    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.headers().cacheControl();

        http.csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .httpBasic().disable()
                .addFilterBefore(new JWTLoginFilter("/login", authenticationManager(), tokenService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JWTAuthenticationFilter(tokenService), UsernamePasswordAuthenticationFilter.class);

    }

}
