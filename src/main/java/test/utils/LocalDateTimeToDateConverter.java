package test.utils;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@WritingConverter
public class LocalDateTimeToDateConverter implements Converter<LocalDateTime, Date> {

    @NotNull
    @Override
    public Date convert(@NotNull LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }
}
