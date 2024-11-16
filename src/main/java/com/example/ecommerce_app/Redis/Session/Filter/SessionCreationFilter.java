package com.example.ecommerce_app.Redis.Session.Filter;

import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Redis.Session.SessionManagement.SessionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@EqualsAndHashCode(callSuper = true)
@Component
@AllArgsConstructor
@Data
@Slf4j
public class SessionCreationFilter extends OncePerRequestFilter {

    private final SessionService sessionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("filter is invoked");
        try {
            if (request.getSession(false) == null){
                sessionService.createSession(request.getSession(true));
            }
            if(request.getSession(false) != null){
                if(sessionService.getSessionData(request.getSession(false).getId()) == null){
                    sessionService.createSession(request.getSession(true));
                };
            }
        }catch (CustomRuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Error while Creating new User Session");
        }

        filterChain.doFilter(request, response);


    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        return !requestUri.startsWith("/api/public");
    }
}
