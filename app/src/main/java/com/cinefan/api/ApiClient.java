package com.cinefan.api;

import com.cinefan.utils.AuthInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Configuración de Retrofit para las llamadas API
 */
public class ApiClient {
    
    private static final String BASE_URL = "http://10.0.2.2/cinefan/api/"; // Para emulador
    // private static final String BASE_URL = "http://tu-servidor.com/cinefan/api/"; // Para producción
    
    private static Retrofit retrofit = null;
    private static AuthInterceptor authInterceptor = new AuthInterceptor();
    
    public static Retrofit getClient() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(authInterceptor)
                    .addInterceptor(logging)
                    .build();
            
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        
        return retrofit;
    }
    
    public static void setToken(String token) {
        authInterceptor.setToken(token);
    }
    
    public static void clearToken() {
        authInterceptor.clearToken();
    }
}
