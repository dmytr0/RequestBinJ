package com.dmytr0.requestbin.domain;

import com.dmytr0.requestbin.enums.RateType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class StatsMetric {

    private String metricName;

    private RateType rateType;

    private Map<String, Integer> mapResult;

    private Float avr;

}
