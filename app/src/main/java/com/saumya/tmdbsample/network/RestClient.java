package com.saumya.tmdbsample.network;

import android.content.Context;
import android.util.Log;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {
    static RestService restService = null;

    public static String currentUrl = "https://api.themoviedb.org/";
    private static SSLContext sslContext;
    public static Retrofit retrofit;

    public static RestService getRestService(final Context context) {
        if (restService == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            builder.readTimeout(10, TimeUnit.SECONDS);
            builder.connectTimeout(5, TimeUnit.SECONDS);
            builder.addInterceptor(interceptor);


                builder.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {

                        try {
                            Request request = chain.request().newBuilder().build();
                            return chain.proceed(request);
                        } catch (Exception e) {
                            return chain.proceed(chain.request());
                        }

                    }

                });

            OkHttpClient client = builder.build();

             retrofit =
                    new Retrofit.Builder().baseUrl(currentUrl)
                            .client(client)
                            .addConverterFactory(new RetrofitStringConverter())
                            .addConverterFactory(GsonConverterFactory.create()).build();


            restService = retrofit.create(RestService.class);

        }
        return restService;
    }
}
