package com.cinefan.api;

import com.cinefan.models.ApiResponse;
import com.cinefan.models.LoginRequest;
import com.cinefan.models.LoginResponse;
import com.cinefan.models.PeliculaDetailResponse;
import com.cinefan.models.PeliculaRequest;
import com.cinefan.models.PeliculaResponse;
import com.cinefan.models.RegisterRequest;
import com.cinefan.models.RegisterResponse;
import com.cinefan.models.ListaResponse;
import com.cinefan.models.ListaDetailResponse;
import com.cinefan.models.ListaRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Interfaz que define las llamadas a la API
 */
public interface CineFanApi {
    
    // Autenticación
    @POST("auth/login.php")
    Call<LoginResponse> login(@Body LoginRequest request);
    
    @POST("auth/register.php")
    Call<RegisterResponse> register(@Body RegisterRequest request);
    
    // Películas
    @GET("peliculas/get_all.php")
    Call<PeliculaResponse> getPeliculas(
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("search") String search,
            @Query("genre") String genre,
            @Query("year") Integer year
    );
    
    @GET("peliculas/get_one.php")
    Call<PeliculaDetailResponse> getPelicula(@Query("id") int id);
    
    @POST("peliculas/create.php")
    Call<ApiResponse> createPelicula(@Body PeliculaRequest request);
    
    @POST("peliculas/update.php")
    Call<ApiResponse> updatePelicula(@Body PeliculaRequest request);
    
    @POST("peliculas/delete.php")
    Call<ApiResponse> deletePelicula(@Query("id") int id);
    
    // Listas
    @GET("listas/get_all.php")
    Call<ListaResponse> getListas();
    
    @GET("listas/get_one.php")
    Call<ListaDetailResponse> getLista(@Query("id") int id);
    
    @POST("listas/create.php")
    Call<ApiResponse> createLista(@Body ListaRequest request);
    
    @POST("listas/add_movie.php")
    Call<ApiResponse> addMovieToList(@Query("lista_id") int listaId, @Query("pelicula_id") int peliculaId);
    
    @POST("listas/remove_movie.php")
    Call<ApiResponse> removeMovieFromList(@Query("lista_id") int listaId, @Query("pelicula_id") int peliculaId);
    
    // Valoraciones
    @POST("valoraciones/rate.php")
    Call<ApiResponse> ratePelicula(@Query("pelicula_id") int peliculaId, @Query("puntuacion") float puntuacion, @Query("comentario") String comentario);
    
    // Perfil
    @GET("usuarios/profile.php")
    Call<ApiResponse> getProfile();
    
    @POST("usuarios/update_profile.php")
    Call<ApiResponse> updateProfile(@Body Object profileData);
    
    @POST("usuarios/change_password.php")
    Call<ApiResponse> changePassword(@Query("old_password") String oldPassword, @Query("new_password") String newPassword);
}
