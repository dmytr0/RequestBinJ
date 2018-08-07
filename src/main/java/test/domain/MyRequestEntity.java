package test.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyRequestEntity {

    private String id;
    private String method;
    private String body;
    private Map<String, String> headers;
    private Map<String, String> params;
    private String time;

    public MyRequestEntity(String method, String body, Map<String, String> headers, Map<String, String> params) {
        this.id = UUID.randomUUID().toString();
        this.method = method;
        this.body = body;
        this.headers = headers;
        this.params = params;
        this.time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss"));
    }
}
