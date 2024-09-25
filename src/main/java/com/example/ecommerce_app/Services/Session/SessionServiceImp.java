package com.example.ecommerce_app.Services.Session;

import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Data
@Slf4j
public class SessionServiceImp implements SessionService{

    private RedisTemplate<String, Object> template;

    @Override
    public Object getSessionData(String sessionId) {
        try {
            return template.opsForValue().get(sessionId);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Could not retrieve data from Session");
        }
    }

    @Override
    public void addToSession(String sessionId , Object obj) {
        try {
            template.opsForValue().set(sessionId , obj);

        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Could not add to Session");
        }
    }

}
