package com.dmytr0.requestbin.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "requests")
public class MyRequestEntity {

    @Id
    private String _id;
    private String method;
    private String body;
    private Map<String, String> headers;
    private Map<String, String> params;
    private LocalDateTime time;
    private String stringTime;

    public MyRequestEntity(String method, String body, Map<String, String> headers, Map<String, String> params) {
        this._id = UUID.randomUUID().toString();
        this.method = method;
        this.body = body;
        this.headers = headers;
        this.params = params;
        this.time = LocalDateTime.now();
        this.stringTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss"));
    }
}
