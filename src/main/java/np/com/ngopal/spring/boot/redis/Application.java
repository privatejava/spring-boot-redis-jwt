/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package np.com.ngopal.spring.boot.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import np.com.ngopal.spring.boot.redis.jwt.TokenAuthenticationService;
import np.com.ngopal.spring.boot.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author NGM
 */
@SpringBootApplication
@EnableAutoConfiguration
public class Application {

    @Autowired
    private RedisService redisService;

    @Bean
    public TokenAuthenticationService tokenAuthService() {
        return new TokenAuthenticationService(redisService);
    }
    
    @Bean
    public ObjectMapper mapper(){
        return new ObjectMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
