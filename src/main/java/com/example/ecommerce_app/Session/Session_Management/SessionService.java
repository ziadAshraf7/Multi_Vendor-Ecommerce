package com.example.ecommerce_app.Session.Session_Management;

import jakarta.servlet.http.HttpSession;

public interface SessionService {

    Object getSessionData(String sessionId);

    void addToSession(String sessionId , Object obj);

    void createSession(HttpSession httpSession);

    void deleteSession(String sessionId);
   }
