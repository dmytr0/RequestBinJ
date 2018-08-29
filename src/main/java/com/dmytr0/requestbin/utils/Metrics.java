package com.dmytr0.requestbin.utils;

import com.dmytr0.requestbin.domain.StatsMetric;
import com.dmytr0.requestbin.enums.RateType;
import redis.clients.jedis.Jedis;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static com.dmytr0.requestbin.enums.RateType.PER_SECOND;

public class Metrics {

    private static final String REDIS_KEY_PATTERN = "yyyyMMddHHmmss";
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(REDIS_KEY_PATTERN);

    private Jedis jedis;
    private String name;

    public Metrics(Jedis jedis, String name) {
        this.jedis = jedis;
        this.name = name;
    }

    public void add() {
        add(1);
    }

    public void add(int count) {
        LocalDateTime now = LocalDateTime.now();
        String pattern = name + now.format(dtf);
        long current = jedis.get(pattern) == null ? 0L : Long.valueOf(jedis.get(pattern));
        long newValue = current + count;
        jedis.set(pattern, String.valueOf(newValue));
    }

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

//    @NotNull
//    private String getStringResult(StatsMetric statsMetric) {
//        Map<String, Integer> results = statsMetric.getMapResult();
//        if (results == null || results.isEmpty()) {
//            return "Data is absent";
//        }
//
//        long allCount = 0;
//        StringBuilder sb = new StringBuilder(name);
//        for (Map.Entry<String, Integer> entry : results.entrySet()) {
//            allCount += entry.getValue();
//            sb.append("<br/>").append(entry.getKey()).append(": ").append(entry.getValue());
//        }
//
//        long avrCount = allCount / results.size();
//        sb.append("<br/><br/>").append("AVR: ").append(avrCount);
//        return sb.toString();
//    }

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
