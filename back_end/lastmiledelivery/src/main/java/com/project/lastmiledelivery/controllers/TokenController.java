package com.project.lastmiledelivery.controllers;

import com.project.lastmiledelivery.responses.BaseResponse;
import com.project.lastmiledelivery.responses.LoginResponse;
import com.project.lastmiledelivery.services.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}")
@RequiredArgsConstructor
public class TokenController {
    private final TokenService tokenService;

    @PutMapping("/refresh-customer-token")
    public ResponseEntity<BaseResponse> refreshCustomerToken(
            @Valid @RequestBody String refreshToken
    ) throws Exception {
        try {
            LoginResponse refreshedResponse = tokenService.refreshCustomerToken(refreshToken);
            return ResponseEntity.ok(BaseResponse.builder()
                    .message("Refresh token successfully")
                    .status(200)
                    .data(refreshedResponse)
                    .build());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(BaseResponse.builder()
                    .message(e.getMessage())
                    .status(400)
                    .build());
        }
    }

    @PutMapping("/refresh-shipper-token")
    public ResponseEntity<BaseResponse> refreshShipperToken(
            @Valid @RequestBody String refreshToken
    ) throws Exception {
        try {
            LoginResponse refreshedResponse = tokenService.refreshShipperToken(refreshToken);
            return ResponseEntity.ok(BaseResponse.builder()
                    .message("Refresh token successfully")
                    .status(200)
                    .data(refreshedResponse)
                    .build());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(BaseResponse.builder()
                    .message(e.getMessage())
                    .status(400)
                    .build());
        }
    }
}
