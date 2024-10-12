package com.example.ecommerce_app.Redis.database.DataModel;

import com.example.ecommerce_app.Dto.Product_Table.Product_Creation_Dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class RedisUserData {

    List<Product_Creation_Dto> pendingProducts;

    public void addPendingProductData(Product_Creation_Dto pendingProductData){
        pendingProducts.add(pendingProductData);
    }


    public Product_Creation_Dto removePendingProductFromAdminData( String vendorId , String productName)
    {
        int pendingProductIndx = -1;

        for(int i = 0 ; i < pendingProducts.size() ; i++){
            Product_Creation_Dto product_creation_dto = pendingProducts.get(i);
            if(product_creation_dto.getVendorId() == Long.parseLong(vendorId) && Objects.equals(product_creation_dto.getName(), productName)){
                pendingProductIndx = i;
                break;
            }
        }


        return pendingProductIndx >= 0 ? pendingProducts.remove(pendingProductIndx) : null;
    }

}
