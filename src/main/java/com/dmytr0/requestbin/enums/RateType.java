package com.dmytr0.requestbin.enums;

import lombok.Getter;

@Getter
public enum RateType {

    PER_SECOND("yyyyMMddHHmmss", 1),
    PER_MINUTE("yyyyMMddHHmm", 60),
    PER_HOUR("yyyyMMddHH", 3600);

    private String pattern;
    private long rateInSec;

    RateType(String pattern, long rateInSec) {
        this.pattern = pattern;
        this.rateInSec = rateInSec;
    }
}
