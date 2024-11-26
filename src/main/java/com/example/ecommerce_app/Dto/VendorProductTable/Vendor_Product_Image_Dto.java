package com.example.ecommerce_app.Dto.VendorProductTable;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class Vendor_Product_Image_Dto {

    @NotNull
    private List<MultipartFile> imageFiles;

    @NotNull
    private long productId;

    @NotNull
    private long vendorId;

}
