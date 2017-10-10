/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package np.com.ngopal.spring.boot.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 *
 * @author NGM
 */
@Configuration
public class RedisConfig {

    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private Integer redisPort;

    @Value("${redis.pass}")
    private String redisPass;

    @Bean
    @Primary
    JedisConnectionFactory jedisConnectionFactory() throws Exception {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(redisHost);
        factory.setPort(redisPort);
        if (redisPass != null) {
            factory.setPassword(redisPass);
        }
        factory.setUsePool(true);

        return factory;
    }

    @Bean
    RedisTemplate< String, Object> redisTemplate() throws Exception {
        final RedisTemplate< String, Object> template = new RedisTemplate< String, Object>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());

        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        return template;
    }
}
