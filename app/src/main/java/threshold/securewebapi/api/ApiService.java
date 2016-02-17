package threshold.securewebapi.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
//import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
//import retrofit2.RxJavaCallAdapterFactory;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import threshold.securewebapi.BuildConfig;
import threshold.securewebapi.Constant;
import threshold.webapiauth.SecureRequestInterpolator;
import timber.log.Timber;

/**
 * Created by Threshold on 2016/2/1.
 */
public class ApiService {
    private Retrofit retrofit;
    private ProductsApi productsApi;
    private ValuesApi valuesApi;
    private FilesApi filesApi;

    private OkHttpClient okHttpClient;
    private String baseUrl;
    private Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();

    public ApiService(String baseUrl) {
        this.baseUrl = baseUrl;
        initOkHttpClient();
        initRetrofit();
    }

    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    private void initOkHttpClient() {
        HttpLoggingInterceptor.Logger logger = new HttpLoggingInterceptor.Logger(){
            @Override
            public void log(String message) {
                Timber.d(message);
            }
        };
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(logger);
        if (BuildConfig.DEBUG) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }
        SecureRequestInterpolator interpolator = new SecureRequestInterpolator(Constant.AppKey, Constant.AppSecret);
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(interpolator)
                .build();
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public ProductsApi getProductsApi() {
        if (productsApi == null) {
            productsApi = retrofit.create(ProductsApi.class);
        }
        return productsApi;
    }

    public ValuesApi getValuesApi() {
        if (valuesApi == null) {
            valuesApi = retrofit.create(ValuesApi.class);
        }
        return valuesApi;
    }


    public FilesApi getFilesApi() {
        if (filesApi == null) {
            filesApi = retrofit.create(FilesApi.class);
        }
        return filesApi;
    }


}
