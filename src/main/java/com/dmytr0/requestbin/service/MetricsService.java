package com.dmytr0.requestbin.service;

import com.dmytr0.requestbin.domain.StatsMetric;
import com.dmytr0.requestbin.enums.HttpMethod;
import com.dmytr0.requestbin.utils.Metrics;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.*;

import static com.dmytr0.requestbin.enums.HttpMethod.*;

@Log4j2
@Service
public class MetricsService {

    private Map<HttpMethod, Metrics> metricsList = new HashMap<>();

    @Autowired
    public MetricsService(Jedis jedis) {
        metricsList.put(ALL, new Metrics(jedis, ALL.name()));
        metricsList.put(GET, new Metrics(jedis, GET.name()));
        metricsList.put(POST, new Metrics(jedis, POST.name()));
        metricsList.put(PUT, new Metrics(jedis, PUT.name()));
        metricsList.put(DELETE, new Metrics(jedis, DELETE.name()));

    }

    public void add(HttpMethod method) {
        add(method, 1);
    }

    public void add(HttpMethod method, int count) {
        metricsList.get(ALL).add();
        metricsList.get(method).add(count);
    }

    public void clear() {
        metricsList.values().forEach(Metrics::clear);
    }

    public void removeOld(int numberOfResults) {
        metricsList.values().forEach(m -> {
            TreeSet<String> keys = new TreeSet<>(m.getAllKeys());
            int size = keys.size();
            if (size >= numberOfResults * 2) {
                int deadLine = size - numberOfResults;
                Iterator<String> iterator = keys.iterator();
                for (int i = 0; i < size; i++, iterator.next()) {
                    if (i >= deadLine) {
                        iterator.remove();
                    }
                }
                m.removeKeys(keys);
            }

        });

    }

    public List<StatsMetric> handleRateRequest(String start, String finish, String ignoreZero, String type) {
        List<StatsMetric> list = new ArrayList<>();
        for (Metrics metrics : metricsList.values()) {
            StatsMetric statsMetric = metrics.handleRateRequest(start, finish, ignoreZero, type);
            list.add(statsMetric);
        }
        return list;
    }
}
