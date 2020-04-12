package com.example.salo.prductview.network.api;

import com.example.salo.prductview.network.dto.ProductCreateDTO;
import com.example.salo.prductview.network.dto.ProductCreateResultDTO;
import com.example.salo.prductview.network.dto.ProductDTO;
import com.example.salo.prductview.network.dto.ProductEditDTO;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProductDTOHolderApi {
    @GET("products")
    public Call<List<ProductDTO>> getAllProducts();
    
    @POST("products/create")
    public Call<ProductCreateResultDTO> CreateRequest(@Body ProductCreateDTO product);

    @DELETE("products/delete/{id}")
    public Call<ResponseBody> DeleteRequest(@Path("id") int id);

    @GET("products/edit/{id}")
    public Call<ProductEditDTO> getEditProduct(@Path("id") int id);

    @PUT("products/edit")
    public Call<Void> editProduct(@Body ProductEditDTO productEditDTO);
}