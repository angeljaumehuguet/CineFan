package com.cinefan.utils;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Utilidades para la navegación entre fragmentos
 */
public class NavigationUtils {

    /**
     * Navegación simple a un fragmento, reemplazando el actual
     * @param fragmentManager Gestor de fragmentos
     * @param fragment Fragmento a mostrar
     * @param containerId ID del contenedor
     * @param addToBackStack Si se debe añadir a la pila de retroceso
     */
    public static void navigateTo(FragmentManager fragmentManager, Fragment fragment, 
                                  @IdRes int containerId, boolean addToBackStack) {
        if (fragmentManager == null || fragment == null) {
            return;
        }
        
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        
        // Añadir animación
        transaction.setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
        );
        
        // Reemplazar fragmento
        transaction.replace(containerId, fragment, fragment.getClass().getSimpleName());
        
        // Añadir a la pila de retroceso si es necesario
        if (addToBackStack) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        
        // Commit
        transaction.commit();
    }
    
    /**
     * Navegación simple a un fragmento con transición personalizada
     * @param fragmentManager Gestor de fragmentos
     * @param fragment Fragmento a mostrar
     * @param containerId ID del contenedor
     * @param addToBackStack Si se debe añadir a la pila de retroceso
     * @param enterAnim Animación de entrada
     * @param exitAnim Animación de salida
     * @param popEnterAnim Animación de entrada al volver
     * @param popExitAnim Animación de salida al volver
     */
    public static void navigateTo(FragmentManager fragmentManager, Fragment fragment, 
                                  @IdRes int containerId, boolean addToBackStack,
                                  int enterAnim, int exitAnim, int popEnterAnim, int popExitAnim) {
        if (fragmentManager == null || fragment == null) {
            return;
        }
        
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        
        // Añadir animación personalizada
        transaction.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim);
        
        // Reemplazar fragmento
        transaction.replace(containerId, fragment, fragment.getClass().getSimpleName());
        
        // Añadir a la pila de retroceso si es necesario
        if (addToBackStack) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        
        // Commit
        transaction.commit();
    }
    
    /**
     * Navega hacia atrás en la pila de fragmentos
     * @param fragmentManager Gestor de fragmentos
     * @return true si se ha navegado hacia atrás, false si no había fragmentos en la pila
     */
    public static boolean navigateBack(FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return false;
        }
        
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
            return true;
        }
        
        return false;
    }
    
    /**
     * Limpia la pila de retroceso
     * @param fragmentManager Gestor de fragmentos
     */
    public static void clearBackStack(FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return;
        }
        
        // Obtener la entrada más antigua
        if (fragmentManager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(0);
            fragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
    
    /**
     * Comprueba si un fragmento está actualmente visible
     * @param fragmentManager Gestor de fragmentos
     * @param fragmentClass Clase del fragmento a comprobar
     * @return true si el fragmento está visible, false en caso contrario
     */
    public static boolean isFragmentVisible(FragmentManager fragmentManager, 
                                            Class<? extends Fragment> fragmentClass) {
        if (fragmentManager == null || fragmentClass == null) {
            return false;
        }
        
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentClass.getSimpleName());
        return fragment != null && fragment.isVisible();
    }
}