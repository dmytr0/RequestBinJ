package com.dmytr0.requestbin.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyResponse {

    @JsonProperty("answer")
    private String body;

    @JsonProperty("content_type")
    private String contentType = APPLICATION_JSON_VALUE;

    @JsonProperty("status")
    private int status = 200;

    public MyResponse(String body) {
        this.body = body;
    }

    public ResponseEntity entity() {
        return ResponseEntity.status(status).contentType(MediaType.valueOf(contentType)).body(body);
    }
}
