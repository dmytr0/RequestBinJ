package com.dmytr0.requestbin.utils;

import redis.clients.jedis.Jedis;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Metrics {

    private static final String KEY_PATTERN = "yyyyMMddHHmmss";
    private static final String PER_SECONDS_PATTERN = "yyyyMMddHHmmss";
    private static final String PER_MINUTES_PATTERN = "yyyyMMddHHmm";
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
        String pattern = now.format(DateTimeFormatter.ofPattern(KEY_PATTERN));
        long current = jedis.get(pattern) == null ? 0L : Long.valueOf(jedis.get(pattern));
        long newValue = current + count;
        jedis.set(pattern, String.valueOf(newValue));
    }

    public String getRatePerSecond(LocalDateTime start, LocalDateTime finish) {
        if (start.isAfter(finish)) {
            throw new IllegalArgumentException("Start time can not be after finish");
        }
        Map<String, String> results = new HashMap<>();
        LocalDateTime cursor = start;
        if (cursor.equals(finish)) {
            String format = cursor.format(DateTimeFormatter.ofPattern(PER_SECONDS_PATTERN));
            results.putAll(getByPattern(format));
        }

        while (cursor.isBefore(finish) || cursor.equals(finish)) {
            String format = cursor.format(DateTimeFormatter.ofPattern(PER_SECONDS_PATTERN));
            results.putAll(getByPattern(format));
            cursor = cursor.plusSeconds(1);
        }

        if (results.size() == 0) {
            return "Data is absent";
        }

        long allCount = 0;
        StringBuilder sb = new StringBuilder(name);
        for (Map.Entry<String, String> entry : results.entrySet()) {
            allCount += Integer.valueOf(entry.getValue());
            sb.append("<br/>").append(entry.getKey()).append(": ").append(entry.getValue());
        }

        long avrCount = allCount / results.size();
        sb.append("<br/><br/>").append("AVR PER SECONDS: ").append(avrCount);
        return sb.toString();
    }

    private Map<String, String> getByPattern(String pat) {
        Set<String> keys = jedis.keys(pat + "*");
        return keys.stream().collect(Collectors.toMap(k -> k, k -> jedis.get(k)));
    }

    public void clear() {
        jedis.flushDB();
    }
}
