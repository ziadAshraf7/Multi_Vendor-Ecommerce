package com.example.ecommerce_app.Services.Session;

public interface SessionService {

    Object getSessionData(String sessionId);

    void addToSession(String sessionId , Object obj);

   }
