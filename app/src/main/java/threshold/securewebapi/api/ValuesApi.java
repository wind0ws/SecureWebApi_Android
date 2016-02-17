package threshold.securewebapi.api;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Threshold on 2016/2/1.
 */
public interface ValuesApi {

    @GET("api/values")
    Observable<String> getValue(
            @Query("value1")String value1,
            @Query("value2")String value2);
}
