package com.example.ecommerce_app.Config;


import com.example.ecommerce_app.Filters.JwtAuthFilter;
import com.example.ecommerce_app.Filters.SessionCreationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    private final SessionCreationFilter sessionCreationFilter;

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST" , "OPTIONS"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        RequestCache nullRequestCache = new NullRequestCache();

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors((cs) -> corsConfigurationSource())
                .sessionManagement((htc) -> htc.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore( jwtAuthFilter , UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(sessionCreationFilter , UsernamePasswordAuthenticationFilter.class)
                        .authorizeHttpRequests((authorize) -> authorize
                                        .requestMatchers("/api/vendor/**").hasRole("VENDOR")
                                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                        .requestMatchers("/api/review/**").hasRole("CUSTOMER")
                                        .requestMatchers("/api/products/**").permitAll()
                                        .requestMatchers("/api/products/**").hasAnyRole("ADMIN" , "VENDOR" , "CUSTOMER")
                                        .requestMatchers("/api/category/**").permitAll()
                                        .requestMatchers("/api/category/**").hasAnyRole("ADMIN" , "VENDOR" , "CUSTOMER")                                        .requestMatchers("/api/vendorProducts/**").hasAnyRole("VENDOR" , "ADMIN")
                                        .requestMatchers("/api/brand/**").permitAll()
                                        .requestMatchers("/api/brand/**").hasAnyRole("ADMIN" , "VENDOR" , "CUSTOMER")
                                        .requestMatchers("/api/review/**").hasRole("CUSTOMER")
                                        .requestMatchers("/api/attribute/**").hasAnyRole("ADMIN" , "VENDOR")
                                        .requestMatchers("/api/cart/**").hasAnyRole("CUSTOMER" , "ANONYMOUS")
                                        .requestMatchers("/api/notification/**").hasAnyRole("ADMIN" , "VENDOR")
                                        .requestMatchers("/api/orderItem/**").hasAnyRole("VENDOR" , "CUSTOMER")
                                        .requestMatchers("/api/order/**").hasAnyRole("CUSTOMER")
                                        .requestMatchers("/api/customer/**").hasAnyRole("CUSTOMER" , "ADMIN" , "VENDOR")
                                        .anyRequest().permitAll()
                        );
                      return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
