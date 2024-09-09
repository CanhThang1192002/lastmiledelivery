package com.project.lastmiledelivery.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private Object data;

    @JsonProperty("status")
    private Integer status;
}
