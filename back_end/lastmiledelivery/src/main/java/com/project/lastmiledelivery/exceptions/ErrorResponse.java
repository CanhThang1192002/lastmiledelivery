package com.project.lastmiledelivery.exceptions;

import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ErrorResponse {
    private HttpStatus status;
    private String message;
}