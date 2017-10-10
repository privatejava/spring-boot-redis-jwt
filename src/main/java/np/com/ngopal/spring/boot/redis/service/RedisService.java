/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package np.com.ngopal.spring.boot.redis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

/**
 *
 * @author Narayan <me@ngopal.com.np>
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Service("redisService")
@Slf4j
public class RedisService {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private RedisTemplate< String, Object> template;

    public synchronized List<String> getKeys(final String pattern) {
        template.setHashValueSerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        Set<String> redisKeys = template.keys(pattern);
        // Store the keys in a List
        List<String> keysList = new ArrayList<>();
        Iterator<String> it = redisKeys.iterator();
        while (it.hasNext()) {
            String data = it.next();
            keysList.add(data);
        }
        return keysList;
    }

    public synchronized Object getValue(final String key) {

        template.setHashValueSerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template.opsForValue().get(key);
    }

    public synchronized Object getValue(final String key, Class clazz) {
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));

        Object obj = template.opsForValue().get(key);
        return mapper.convertValue(obj, clazz);
    }

    public void setValue(final String key, final Object value) {
        setValue(key, value, TimeUnit.HOURS, 5, false);
    }

    public void setValue(final String key, final Object value, boolean marshal) {
        if (marshal) {
            template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
            template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        } else {
            template.setHashValueSerializer(new StringRedisSerializer());
            template.setValueSerializer(new StringRedisSerializer());
        }
        template.opsForValue().set(key, value);
        // set a expire for a message
        template.expire(key, 5, TimeUnit.MINUTES);
    }

    public void setValue(final String key, final Object value, TimeUnit unit, long timeout) {
        setValue(key, value, unit, timeout, false);
    }

    public void setValue(final String key, final Object value, TimeUnit unit, long timeout, boolean marshal) {
        if (marshal) {
            template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
            template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        } else {
            template.setHashValueSerializer(new StringRedisSerializer());
            template.setValueSerializer(new StringRedisSerializer());
        }
        template.opsForValue().set(key, value);
        // set a expire for a message
        template.expire(key, timeout, unit);
    }

}
