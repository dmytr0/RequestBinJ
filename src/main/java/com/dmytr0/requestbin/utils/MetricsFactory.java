package com.dmytr0.requestbin.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
public class MetricsFactory {

    private final Jedis jedis;

    @Autowired
    public MetricsFactory(Jedis jedis) {
        this.jedis = jedis;
    }

    public Metrics create(String name) {
        return new Metrics(jedis, name);
    }
}
