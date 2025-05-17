package com.cinefan.repositories;

import com.cinefan.api.ApiClient;
import com.cinefan.api.CineFanApi;
import com.cinefan.models.ApiResponse;
import com.cinefan.models.Lista;
import com.cinefan.models.ListaDetailResponse;
import com.cinefan.models.ListaRequest;
import com.cinefan.models.ListaResponse;

import retrofit2.Callback;

/**
 * Repositorio para la gestión de listas
 */
public class ListaRepository {
    
    private static ListaRepository instance;
    private CineFanApi apiService;
    
    /**
     * Constructor privado (singleton)
     */
    private ListaRepository() {
        apiService = ApiClient.getClient().create(CineFanApi.class);
    }
    
    /**
     * Obtiene la instancia única del repositorio
     * @return Instancia del repositorio
     */
    public static synchronized ListaRepository getInstance() {
        if (instance == null) {
            instance = new ListaRepository();
        }
        return instance;
    }
    
    /**
     * Obtiene todas las listas del usuario
     * @param callback Callback para manejar la respuesta
     */
    public void getListas(Callback<ListaResponse> callback) {
        apiService.getListas().enqueue(callback);
    }
    
    /**
     * Obtiene una lista por su ID
     * @param id ID de la lista
     * @param callback Callback para manejar la respuesta
     */
    public void getLista(int id, Callback<ListaDetailResponse> callback) {
        apiService.getLista(id).enqueue(callback);
    }
    
    /**
     * Crea una nueva lista
     * @param nombre Nombre de la lista
     * @param descripcion Descripción de la lista
     * @param publica Si la lista es pública
     * @param callback Callback para manejar la respuesta
     */
    public void createLista(String nombre, String descripcion, boolean publica, 
                            Callback<ApiResponse> callback) {
        ListaRequest request = new ListaRequest(nombre, descripcion, publica);
        apiService.createLista(request).enqueue(callback);
    }
    
    /**
     * Añade una película a una lista
     * @param listaId ID de la lista
     * @param peliculaId ID de la película
     * @param callback Callback para manejar la respuesta
     */
    public void addMovieToList(int listaId, int peliculaId, Callback<ApiResponse> callback) {
        apiService.addMovieToList(listaId, peliculaId).enqueue(callback);
    }
    
    /**
     * Elimina una película de una lista
     * @param listaId ID de la lista
     * @param peliculaId ID de la película
     * @param callback Callback para manejar la respuesta
     */
    public void removeMovieFromList(int listaId, int peliculaId, Callback<ApiResponse> callback) {
        apiService.removeMovieFromList(listaId, peliculaId).enqueue(callback);
    }
    
    /**
     * Actualiza una lista existente
     * @param id ID de la lista
     * @param nombre Nuevo nombre
     * @param descripcion Nueva descripción
     * @param publica Si la lista es pública
     * @param callback Callback para manejar la respuesta
     */
    public void updateLista(int id, String nombre, String descripcion, boolean publica, 
                            Callback<ApiResponse> callback) {
        Lista lista = new Lista(nombre, descripcion, publica);
        lista.setId(id);
        
        // Suponiendo que la API tenga este endpoint para actualizar listas
        // apiService.updateLista(lista).enqueue(callback);
        
        // Si no existe, se puede implementar en el backend
    }
    
    /**
     * Elimina una lista por su ID
     * @param id ID de la lista
     * @param callback Callback para manejar la respuesta
     */
    public void deleteLista(int id, Callback<ApiResponse> callback) {
        // Suponiendo que la API tenga este endpoint para eliminar listas
        // apiService.deleteLista(id).enqueue(callback);
        
        // Si no existe, se puede implementar en el backend
    }
}