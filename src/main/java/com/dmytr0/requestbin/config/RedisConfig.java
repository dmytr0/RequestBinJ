package com.dmytr0.requestbin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.embedded.RedisServer;

import java.io.IOException;

@Configuration
public class RedisConfig {

    @Value("${statistic.redis.server.port:5052}")
    private int redisPort;

    @Value("${statistic.redis.server.host:localhost}")
    private String redisHost;

    @Bean(destroyMethod = "stop")
    public RedisServer gerRedisServer() throws IOException {
        return new RedisServer(redisPort);
    }

    @Bean(destroyMethod = "close")
    public Jedis getJedis(RedisServer redisServer) {
        redisServer.start();
        return new Jedis(redisHost, redisPort);
    }

}
