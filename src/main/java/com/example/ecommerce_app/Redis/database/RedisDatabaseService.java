package com.example.ecommerce_app.Redis.database;

public interface RedisDatabaseService {

    void addData(String key , Object data);

    Object getAllData(String key);

    void insertKey(String key );

    void clearAllData(String key);

    Object clearData(String key , Object data);

    Object getData(String key , Object filteringData);

}
