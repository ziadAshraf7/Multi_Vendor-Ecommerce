package com.example.ecommerce_app.Filters;

import com.example.ecommerce_app.Dto.AutheticatedUser.AuthenticatedUserDto;
import com.example.ecommerce_app.Dto.User.UserInfoDetails;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Mapper.UserMapper;
import com.example.ecommerce_app.Redis.Session.SessionManagement.SessionService;
import com.example.ecommerce_app.Repositery.User.UserRepository;
import com.example.ecommerce_app.Services.JWT.JwtService;
import com.example.ecommerce_app.Services.User.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Objects;

import static com.example.ecommerce_app.Services.Authentication.AuthenticationServiceImp.tokenBlackListRedisKey;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final RedisTemplate<String , Object> redisTemplate;

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final SessionService sessionService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            email = jwtService.extractUserEmail(token);
        }


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        System.out.println("authentication");

        boolean isTokenBlackListed = Objects.requireNonNull(redisTemplate.opsForList().range(tokenBlackListRedisKey, 0, 1)).contains(token);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null && !isTokenBlackListed) {
            try {
                UserInfoDetails userInfo = userMapper.toUserInfoDetails(
                        userRepository.findByEmail(email)
                );
                AuthenticatedUserDto authenticatedUserDto = new AuthenticatedUserDto(userInfo.getEmail() , userInfo.getId() , token);
                if (jwtService.validateToken(token, userInfo)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            authenticatedUserDto,
                            null,
                            userInfo.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }else {
                    throw new CustomRuntimeException("Token is Invalid");
                }

            }catch (CustomRuntimeException e){
                throw new CustomRuntimeException("user is not authorized");
            }

        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String httpMethod = request.getMethod();

        return requestUri.startsWith("/api/public") &
                !(pathMatcher.match("/api/public/product", requestUri) && "GET".equals(httpMethod));
    }
}
