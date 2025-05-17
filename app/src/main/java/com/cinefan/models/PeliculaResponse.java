package com.cinefan.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Modelo para la respuesta de película
 */
public class PeliculaResponse {
    @SerializedName("status")
    private String status;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("data")
    private List<Pelicula> data;
    
    @SerializedName("pagination")
    private Pagination pagination;
    
    public String getStatus() {
        return status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public List<Pelicula> getData() {
        return data;
    }
    
    public Pagination getPagination() {
        return pagination;
    }
    
    public static class Pagination {
        @SerializedName("currentPage")
        private int currentPage;
        
        @SerializedName("itemsPerPage")
        private int itemsPerPage;
        
        @SerializedName("totalItems")
        private int totalItems;
        
        @SerializedName("totalPages")
        private int totalPages;
        
        public int getCurrentPage() {
            return currentPage;
        }
        
        public int getItemsPerPage() {
            return itemsPerPage;
        }
        
        public int getTotalItems() {
            return totalItems;
        }
        
        public int getTotalPages() {
            return totalPages;
        }
    }
}
