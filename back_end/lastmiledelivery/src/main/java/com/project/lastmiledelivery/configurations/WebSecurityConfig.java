package com.project.lastmiledelivery.configurations;

//import com.example.delivery.filters.JwtTokenFilter;
//import com.example.delivery.models.Role;
import com.project.lastmiledelivery.filters.JwtTokenFilter;
import com.project.lastmiledelivery.utils.Enums;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;


import java.util.Arrays;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)  throws Exception {
        http
                .cors(Customizer.withDefaults())
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests
                            .requestMatchers(
                                    String.format("%s/customer/register", apiPrefix),
                                    String.format("%s/customer/login", apiPrefix),

                                    String.format("%s/shipper/register", apiPrefix),
                                    String.format("%s/shipper/login", apiPrefix),

                                    String.format("%s/admin/login", apiPrefix),

                                    String.format("%s/refresh-customer-token", apiPrefix),
                                    String.format("%s/refresh-shipper-token", apiPrefix)

                            )
                            .permitAll()
                            .requestMatchers(POST,
                                    String.format("%s/order/create", apiPrefix)).hasAnyRole(Enums.Role.CUSTOMER.name())
                            .requestMatchers(PUT,
                                    String.format("%s/order/update", apiPrefix)).hasAnyRole(Enums.Role.CUSTOMER.name())
                            .requestMatchers(DELETE,
                                    String.format("%s/order/delete", apiPrefix)).hasAnyRole(Enums.Role.CUSTOMER.name())
                            .requestMatchers(GET,
                                    String.format("%s/order/{orderId}", apiPrefix)).hasAnyRole(Enums.Role.ADMIN.name(), Enums.Role.CUSTOMER.name())
                            .requestMatchers(GET,
                                    String.format("%s/order/user/{customerId}", apiPrefix)).hasAnyRole(Enums.Role.ADMIN.name(),Enums.Role.CUSTOMER.name())
                            .requestMatchers(GET,
                                    String.format("%s/order/all", apiPrefix)).hasAnyRole(Enums.Role.ADMIN.name())
                            .requestMatchers(GET,
                                    String.format("%s/customer/all", apiPrefix)).hasAnyRole(Enums.Role.ADMIN.name())
                            .requestMatchers(GET,
                                    String.format("%s/shipper/all", apiPrefix)).hasAnyRole(Enums.Role.ADMIN.name())
                            .anyRequest().authenticated();
                    //.anyRequest().permitAll();
                })
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
