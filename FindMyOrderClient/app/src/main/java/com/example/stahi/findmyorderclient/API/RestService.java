package com.example.stahi.findmyorderclient.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by stahi on 4/15/2018.
 */

public class RestService {
    private static final String URL = "http://findmyorderapi.azurewebsites.net/api/";
    //private static final String URL = "http://10.0.2.2:63238/api/";
    private Retrofit restAdapter;
    private UserService apiService;

    public RestService()
    {
        //HTTP is the way modern applications network
        //OkHttpClient = HTTP but perseveres when the network is troublesome
        OkHttpClient okClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public Response intercept(Interceptor.Chain chain) throws IOException {
                                Request original = chain.request();

                                // Request customization: add request headers
                                Request.Builder requestBuilder = original.newBuilder()
                                        //.header("Host", "localhost")
                                        .method(original.method(), original.body());

                                Request request = requestBuilder.build();
                                return chain.proceed(request);
                            }
                        })
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        //Conexiunea propriu-zisa
        restAdapter = new retrofit2.Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okClient)
                .build();

        apiService = restAdapter.create(UserService.class);
    }

    public UserService getService()
    {
        return apiService;
    }

}
