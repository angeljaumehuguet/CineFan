package com.cinefan.test;

import android.view.View;
import android.widget.EditText;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.cinefan.R;
import com.cinefan.actividades.LoginActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Pruebas de interfaz para validar el funcionamiento de la UI
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class PruebasInterfaz {
    
    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule = 
            new ActivityScenarioRule<>(LoginActivity.class);
    
    /**
     * Prueba la validación del formulario de login
     */
    @Test
    public void testLoginFormValidation() {
        // Verificar que el botón de login está visible
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()));
        
        // Click en el botón sin rellenar datos
        onView(withId(R.id.btnLogin)).perform(click());
        
        // Verificar mensaje de error en el campo username
        onView(withId(R.id.editUsername))
                .check(matches(hasErrorText(getResourceString(R.string.error_empty_username))));
        
        // Rellenar username y verificar que el error se mueve al password
        onView(withId(R.id.editUsername))
                .perform(typeText("usuario_prueba"), closeSoftKeyboard());
        onView(withId(R.id.btnLogin)).perform(click());
        onView(withId(R.id.editPassword))
                .check(matches(hasErrorText(getResourceString(R.string.error_empty_password))));
        
        // Verificar que el enlace de registro está visible
        onView(withId(R.id.txtRegister)).check(matches(isDisplayed()));
    }
    
    /**
     * Prueba la navegación al registro
     */
    @Test
    public void testNavigationToRegister() {
        // Click en el enlace de registro
        onView(withId(R.id.txtRegister)).perform(click());
        
        // Verificar que estamos en la pantalla de registro
        onView(withId(R.id.btnRegister)).check(matches(isDisplayed()));
        onView(withText(getResourceString(R.string.register))).check(matches(isDisplayed()));
    }
    
    /**
     * Prueba la validación del formulario de registro
     */
    @Test
    public void testRegisterFormValidation() {
        // Primero ir a la pantalla de registro
        onView(withId(R.id.txtRegister)).perform(click());
        
        // Verificar que el botón de registro está visible
        onView(withId(R.id.btnRegister)).check(matches(isDisplayed()));
        
        // Click en el botón sin rellenar datos
        onView(withId(R.id.btnRegister)).perform(click());
        
        // Verificar mensaje de error en el campo username
        onView(withId(R.id.editUsername))
                .check(matches(hasErrorText(getResourceString(R.string.error_empty_username))));
        
        // Rellenar username
        onView(withId(R.id.editUsername))
                .perform(typeText("usuario_prueba"), closeSoftKeyboard());
        onView(withId(R.id.btnRegister)).perform(click());
        
        // Verificar mensaje de error en el campo fullname
        onView(withId(R.id.editFullName))
                .check(matches(hasErrorText(getResourceString(R.string.error_empty_fullname))));
        
        // Rellenar fullname
        onView(withId(R.id.editFullName))
                .perform(typeText("Usuario Prueba"), closeSoftKeyboard());
        onView(withId(R.id.btnRegister)).perform(click());
        
        // Verificar mensaje de error en el campo email
        onView(withId(R.id.editEmail))
                .check(matches(hasErrorText(getResourceString(R.string.error_empty_email))));
        
        // Rellenar email inválido
        onView(withId(R.id.editEmail))
                .perform(typeText("correo_invalido"), closeSoftKeyboard());
        onView(withId(R.id.btnRegister)).perform(click());
        
        // Verificar mensaje de error en el campo email
        onView(withId(R.id.editEmail))
                .check(matches(hasErrorText(getResourceString(R.string.error_invalid_email))));
    }
    
    /**
     * Acción para obtener el string de un recurso
     * @param resourceId ID del recurso string
     * @return String del recurso
     */
    private String getResourceString(int resourceId) {
        final String[] resource = new String[1];
        
        // Obtener el string del recurso
        onView(ViewMatchers.isRoot()).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isRoot();
            }
            
            @Override
            public String getDescription() {
                return "getting resource string";
            }
            
            @Override
            public void perform(UiController uiController, View view) {
                resource[0] = view.getContext().getString(resourceId);
            }
        });
        
        return resource[0];
    }
}