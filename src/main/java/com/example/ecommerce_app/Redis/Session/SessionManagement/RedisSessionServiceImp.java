package com.example.ecommerce_app.Redis.Session.SessionManagement;

import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Redis.Session.SessionData.AnonymousUserSessionData;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
@Data
@Slf4j
public class RedisSessionServiceImp implements SessionService{

    private HttpSession httpSession;

    @Override
    public AnonymousUserSessionData getSessionData(String sessionId) {
        try {
            Object o = httpSession.getAttribute(sessionId);
            System.out.println(o);
            return (AnonymousUserSessionData) httpSession.getAttribute(sessionId);
        }catch (CustomRuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Could not retrieve data from Session");
        }
    }

    @Override
    public void addToSession(String sessionId , Object data) {
        try {
            httpSession.setAttribute(sessionId , (AnonymousUserSessionData) data);
        }catch (CustomRuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Could not add to Session");
        }
    }

    @Override
    public void createSession(HttpSession httpSession) {
        System.out.println(httpSession.getId());
         httpSession.setAttribute(httpSession.getId() , new AnonymousUserSessionData(new ArrayList<>(50 )));
    }

    @Override
    public void deleteSession(String sessionId) {
        httpSession.setAttribute(sessionId , null);
    }

}
