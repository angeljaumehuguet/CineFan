package com.cinefan.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

/**
 * Utilidades para la interfaz de usuario
 */
public class UiUtils {

    /**
     * Muestra un mensaje Toast
     * @param context Contexto
     * @param message Mensaje a mostrar
     */
    public static void showToast(Context context, String message) {
        if (context != null && message != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Muestra un mensaje Toast desde un recurso string
     * @param context Contexto
     * @param resId ID del recurso string
     */
    public static void showToast(Context context, @StringRes int resId) {
        if (context != null) {
            Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Oculta el teclado virtual
     * @param activity Actividad actual
     */
    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager) 
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            
            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(
                        activity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
    
    /**
     * Oculta el teclado desde una vista específica
     * @param context Contexto
     * @param view Vista a la que está asociado el teclado
     */
    public static void hideKeyboard(Context context, View view) {
        if (context != null && view != null) {
            InputMethodManager inputManager = (InputMethodManager) 
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
            
            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
    
    /**
     * Carga una imagen en un ImageView usando Glide con configuración por defecto
     * @param context Contexto
     * @param url URL de la imagen
     * @param imageView ImageView donde cargar la imagen
     * @param placeholderResId Recurso a mostrar mientras se carga
     */
    public static void loadImage(Context context, String url, ImageView imageView, 
                                  @DrawableRes int placeholderResId) {
        if (context == null || imageView == null) return;
        
        Glide.with(context)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(placeholderResId)
                .error(placeholderResId)
                .into(imageView);
    }
    
    /**
     * Carga una imagen en un ImageView con forma circular
     * @param context Contexto
     * @param url URL de la imagen
     * @param imageView ImageView donde cargar la imagen
     * @param placeholderResId Recurso a mostrar mientras se carga
     */
    public static void loadCircleImage(Context context, String url, ImageView imageView, 
                                        @DrawableRes int placeholderResId) {
        if (context == null || imageView == null) return;
        
        Glide.with(context)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(RequestOptions.circleCropTransform())
                .placeholder(placeholderResId)
                .error(placeholderResId)
                .into(imageView);
    }
    
    /**
     * Establece el color de un drawable en un ImageView
     * @param imageView ImageView a modificar
     * @param colorResId ID del recurso de color
     */
    public static void setImageViewTint(ImageView imageView, int colorResId) {
        if (imageView != null && imageView.getContext() != null) {
            imageView.setColorFilter(
                    ContextCompat.getColor(imageView.getContext(), colorResId));
        }
    }
}