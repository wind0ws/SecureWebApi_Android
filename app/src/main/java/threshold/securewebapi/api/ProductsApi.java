package threshold.securewebapi.api;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;
import threshold.securewebapi.data.model.Product;

/**
 * Created by Threshold on 2016/1/28.
 */
public interface ProductsApi {
    @GET("api/products/{id}")
    Observable<Product> getProduct(@Path("id")int id);

    @GET("api/products")
    Observable<List<Product>> getProducts();

    @POST("api/products")
    Observable<Product> addProduct(@Body Product product);

}
