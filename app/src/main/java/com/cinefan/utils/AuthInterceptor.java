package com.cinefan.utils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Interceptor para añadir el token de autenticación a las peticiones
 */
public class AuthInterceptor implements Interceptor {
    
    private String token;
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public void clearToken() {
        this.token = null;
    }
    
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        
        if (token != null && !token.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + token);
        }
        
        return chain.proceed(builder.build());
    }
}
