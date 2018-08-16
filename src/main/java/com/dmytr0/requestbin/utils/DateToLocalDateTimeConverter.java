package com.dmytr0.requestbin.utils;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@ReadingConverter
public class DateToLocalDateTimeConverter implements Converter<Date, LocalDateTime> {

    @NotNull
    @Override
    public LocalDateTime convert(@NotNull Date date) {
        return LocalDateTime.ofInstant(
                date.toInstant(), ZoneId.systemDefault());
    }
}
