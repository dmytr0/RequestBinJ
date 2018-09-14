package com.dmytr0.requestbin.utils;

import com.dmytr0.requestbin.domain.StatsMetric;
import com.dmytr0.requestbin.enums.RateType;
import lombok.extern.log4j.Log4j2;
import redis.clients.jedis.Jedis;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static com.dmytr0.requestbin.enums.RateType.PER_SECOND;

@Log4j2
public class Metrics {

    private static final String REDIS_KEY_PATTERN = "yyyyMMddHHmmssSSS";
    private static final DateTimeFormatter redisDtf = DateTimeFormatter.ofPattern(REDIS_KEY_PATTERN);


    private Jedis jedis;
    private String name;

    public Metrics(Jedis jedis, String name) {
        this.jedis = jedis;
        this.name = name + "_METRIC";
    }

    public void add() {
        add(1);
    }

    public void add(int count) {
        try {
            LocalDateTime now = LocalDateTime.now();
            String pattern = name + now.format(redisDtf);
            jedis.incrBy(pattern, count);
        } catch (Exception e) {
            log.warn("Error count request: " + e.getMessage(), e);
        }
    }

    //Default
    public StatsMetric getRate() {
        return getRate(LocalDateTime.now().minusHours(1), LocalDateTime.now(), PER_SECOND, true);
    }

    public StatsMetric getRate(LocalDateTime start, LocalDateTime finish, RateType rateType, boolean ignoreZero) {
        if (start.isAfter(finish)) {
            throw new IllegalArgumentException("Start time can not be after finish");
        }
        Map<String, Integer> results = new HashMap<>();
        LocalDateTime cursor = start;


        while (cursor.isBefore(finish) || cursor.equals(finish)) {
            String format = cursor.format(DateTimeFormatter.ofPattern(rateType.getPattern()));
            int count = getCountByPattern(format);
            if (count != 0 || !ignoreZero) {
                results.put(format, count);
            }
            cursor = cursor.plusSeconds(rateType.getRateInSec());
        }
        return new StatsMetric()
                .setMapResult(results)
                .setRateType(rateType)
                .setAvr(average(results))
                .setMetricName(name);
    }

    public StatsMetric handleRateRequest(String start, String finish, String ignoreZero, String rateType) {
        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
        LocalDateTime finishTime = LocalDateTime.now();
        boolean ignoreZeroValue = true;
        RateType type = PER_SECOND;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(PER_SECOND.getPattern());

        if (start != null && !start.isEmpty()) {
            startTime = LocalDateTime.parse(start, dtf);
        }
        if (finish != null && !finish.isEmpty()) {
            finishTime = LocalDateTime.parse(finish, dtf);
        }
        if (ignoreZero != null && !ignoreZero.isEmpty()) {
            ignoreZeroValue = Boolean.valueOf(ignoreZero);
        }
        if (rateType != null && !rateType.isEmpty()) {
            type = RateType.valueOf(rateType.toUpperCase());
        }

        return getRate(startTime, finishTime, type, ignoreZeroValue);
    }


    private float average(Map<String, Integer> results) {
        if (results == null || results.isEmpty()) {
            return 0.0F;
        }
        int sum = results.values().stream().mapToInt(Integer::intValue).sum();
        return (sum * 10F / results.size()) / 10;
    }

    public Set<String> getAllKeys() {
        return jedis.keys(name + "*");
    }

    public void removeKeys(Set<String> keys) {
        String[] keysForDel = keys.toArray(new String[0]);
        jedis.del(keysForDel);
    }

    private int getCountByPattern(String pat) {
        Set<String> keys = jedis.keys(name + pat + "*");

        AtomicInteger count = new AtomicInteger();

        keys.forEach(k -> {
            try {
                String value = jedis.get(k);
                count.addAndGet(Integer.valueOf(value));
            } catch (Exception ignore) {
            }
        });

        return count.get();
    }

    public void clear() {
        jedis.flushDB();
    }
}
