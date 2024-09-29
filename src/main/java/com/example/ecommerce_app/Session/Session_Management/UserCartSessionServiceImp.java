package com.example.ecommerce_app.Session.Session_Management;

import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Session.AnonymousUser.AnonymousUserCartData;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
@Data
@Slf4j
public class UserCartSessionServiceImp implements SessionService{

    private  RedisTemplate<String, Object> template;

    @Override
    public AnonymousUserCartData getSessionData(String sessionId) {
        try {
            return (AnonymousUserCartData) template.opsForValue().get(sessionId);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Could not retrieve data from Session");
        }
    }

    @Override
    public void addToSession(String sessionId , Object data) {
        try {
            template.opsForValue().set(sessionId , (AnonymousUserCartData) data);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Could not add to Session");
        }
    }

    @Override
    public void createSession(HttpSession httpSession) {
         template.opsForValue().set(httpSession.getId() , new AnonymousUserCartData(0,new ArrayList<>(50)));
    }

    @Override
    public void deleteSession(String sessionId) {
        template.opsForValue().set(sessionId , null);
    }

}
