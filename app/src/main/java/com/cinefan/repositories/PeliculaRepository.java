package com.cinefan.repositories;

import com.cinefan.api.ApiClient;
import com.cinefan.api.CineFanApi;
import com.cinefan.models.Pelicula;
import com.cinefan.models.PeliculaResponse;

import java.util.List;

import retrofit2.Callback;

/**
 * Repositorio para la gestión de películas
 */
public class PeliculaRepository {
    
    private static PeliculaRepository instance;
    private CineFanApi apiService;
    
    /**
     * Constructor privado (singleton)
     */
    private PeliculaRepository() {
        apiService = ApiClient.getClient().create(CineFanApi.class);
    }
    
    /**
     * Obtiene la instancia única del repositorio
     * @return Instancia del repositorio
     */
    public static synchronized PeliculaRepository getInstance() {
        if (instance == null) {
            instance = new PeliculaRepository();
        }
        return instance;
    }
    
    /**
     * Obtiene la lista de películas de forma paginada
     * @param page Número de página
     * @param limit Número de elementos por página
     * @param search Texto de búsqueda (opcional)
     * @param genre Género (opcional)
     * @param year Año (opcional)
     * @param callback Callback para manejar la respuesta
     */
    public void getPeliculas(int page, int limit, String search, String genre, Integer year, 
                             Callback<PeliculaResponse> callback) {
        apiService.getPeliculas(page, limit, search, genre, year).enqueue(callback);
    }
    
    /**
     * Obtiene una película por su ID
     * @param id ID de la película
     * @param callback Callback para manejar la respuesta
     */
    public void getPelicula(int id, Callback<PeliculaResponse> callback) {
        apiService.getPelicula(id).enqueue(callback);
    }
    
    /**
     * Crea una nueva película
     * @param pelicula Película a crear
     * @param callback Callback para manejar la respuesta
     */
    public void createPelicula(Pelicula pelicula, Callback<PeliculaResponse> callback) {
        apiService.createPelicula(pelicula).enqueue(callback);
    }
    
    /**
     * Actualiza una película existente
     * @param pelicula Película actualizada
     * @param callback Callback para manejar la respuesta
     */
    public void updatePelicula(Pelicula pelicula, Callback<PeliculaResponse> callback) {
        apiService.updatePelicula(pelicula).enqueue(callback);
    }
    
    /**
     * Elimina una película por su ID
     * @param id ID de la película a eliminar
     * @param callback Callback para manejar la respuesta
     */
    public void deletePelicula(int id, Callback<PeliculaResponse> callback) {
        apiService.deletePelicula(id).enqueue(callback);
    }
    
    /**
     * Valora una película
     * @param peliculaId ID de la película
     * @param puntuacion Puntuación (0-10)
     * @param comentario Comentario (opcional)
     * @param callback Callback para manejar la respuesta
     */
    public void ratePelicula(int peliculaId, float puntuacion, String comentario, 
                              Callback<PeliculaResponse> callback) {
        apiService.ratePelicula(peliculaId, puntuacion, comentario).enqueue(callback);
    }
}