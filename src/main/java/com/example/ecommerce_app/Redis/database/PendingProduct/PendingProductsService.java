package com.example.ecommerce_app.Redis.database.PendingProduct;

import com.example.ecommerce_app.Dto.PendingProducts.PendingProductGeneralData;
import com.example.ecommerce_app.Dto.Product_Table.Product_Creation_Dto;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Redis.database.DataModel.RedisUserData;
import com.example.ecommerce_app.Redis.database.RedisDatabaseService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@AllArgsConstructor
@Data
@Service
@Slf4j
public class PendingProductsService implements RedisDatabaseService {

    private final RedisTemplate<String, Object> template;

    @Override
    public void addData(String recipientId, Object data) {

        if(getAllData(recipientId) == null) insertKey(recipientId);

        RedisUserData redisUserData = getAllData(recipientId);

        redisUserData.addPendingProductData((Product_Creation_Dto) data);

        template.opsForValue().set(recipientId , redisUserData);

    }

    @Override
    public RedisUserData getAllData(String key) {
        return (RedisUserData) (template.opsForValue().get(key));
    }

    @Override
    public void insertKey(String key ) {
        template.opsForValue().set(key , new RedisUserData(new ArrayList<>()) );
    }

    @Override
    public void clearAllData(String key) {
        template.opsForValue().set(key , new RedisUserData(new ArrayList<>()));
    }

    @Override
    public Object clearData(String key, Object filteringData) {

            RedisUserData redisUserData = getAllData(key);

          return  redisUserData.removePendingProductFromAdminData(
                    ((PendingProductGeneralData) filteringData).getVendorId() ,
                    ((PendingProductGeneralData) filteringData).getProductName());

    }

    @Override
    public Object getData(String key, Object productGeneralData) {

        RedisUserData redisUserData = getAllData(key);

        Product_Creation_Dto product_creation_dto = redisUserData.removePendingProductFromAdminData(
                ((PendingProductGeneralData) productGeneralData).getVendorId() ,
                ((PendingProductGeneralData) productGeneralData).getProductName());


        if(product_creation_dto == null) throw new CustomRuntimeException("Cannot retrieve product data from redis database");

        return (Product_Creation_Dto) product_creation_dto;

    }


}
