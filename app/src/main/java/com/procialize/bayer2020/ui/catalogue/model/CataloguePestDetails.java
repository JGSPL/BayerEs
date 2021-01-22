package com.procialize.bayer2020.ui.catalogue.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.procialize.bayer2020.GetterSetter.Header;

import java.util.List;

public class CataloguePestDetails {

    @SerializedName("detail")
    @Expose
    private String total_recommended_product_count;
    @SerializedName("pest_recommended_product")
    @Expose
    List<CataloguePestRecommendedProducts> pest_recommended_product;
    @SerializedName("pest_imagepath")
    @Expose
    private String pest_imagepath;
    @SerializedName("recommended_product_imagepath")
    @Expose
    private String recommended_product_imagepath;

    public String getTotal_recommended_product_count() {
        return total_recommended_product_count;
    }

    public List<CataloguePestRecommendedProducts> getPest_recommended_product() {
        return pest_recommended_product;
    }

    public String getPest_imagepath() {
        return pest_imagepath;
    }

    public String getRecommended_product_imagepath() {
        return recommended_product_imagepath;
    }
}
