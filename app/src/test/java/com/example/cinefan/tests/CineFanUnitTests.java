package com.example.cinefan.tests;

// 1. Usa solo las importaciones de TestNG
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*; // Usa las aserciones de TestNG
import static org.mockito.Mockito.*;

import com.example.cinefan.utilidades.Validador;
import com.example.cinefan.utilidades.Constantes;
import com.example.cinefan.utilidades.ApiCliente;
import com.example.cinefan.modelos.Pelicula;
import com.example.cinefan.modelos.Usuario;
import com.example.cinefan.modelos.Resena;

public class CineFanUnitTests {

    private Validador validador;
    private ApiCliente apiCliente;

    // 2. Usa @BeforeMethod para que se ejecute antes de cada @Test
    @BeforeMethod
    public void setUp() {
        validador = new Validador();
        apiCliente = mock(ApiCliente.class);
    }

    // =================== PRUEBAS DE FUNCIONALIDAD ===================

    /**
     * PRUEBA 1: Validación de usuarios
     * Tipo: Funcionalidad
     */
    @Test
    public void testValidacionUsuario() {
        // Casos válidos
        assertTrue(validador.validarNombreUsuario("cinefan123"), "Usuario válido debe pasar validación");
        assertTrue(validador.validarNombreUsuario("abc"), "Usuario con longitud mínima debe ser válido");

        // Casos inválidos
        assertFalse(validador.validarNombreUsuario(""), "Usuario vacío debe fallar");
        assertFalse(validador.validarNombreUsuario("ab"), "Usuario muy corto debe fallar");
        assertFalse(validador.validarNombreUsuario("a".repeat(51)), "Usuario muy largo debe fallar");
        assertFalse(validador.validarNombreUsuario("user@name"), "Usuario con caracteres especiales debe fallar");

        System.out.println("✅ PRUEBA 1 COMPLETADA: Validación de usuarios");
    }

    /**
     * PRUEBA 2: Validación de contraseñas
     * Tipo: Seguridad
     */
    @Test
    public void testValidacionPassword() {
        // Casos válidos
        assertTrue(validador.validarPassword("123456"), "Password válido debe pasar");
        assertTrue(validador.validarPassword("MiPass123!"), "Password con caracteres especiales debe ser válido");

        // Casos inválidos
        assertFalse(validador.validarPassword(""), "Password vacío debe fallar");
        assertFalse(validador.validarPassword("12345"), "Password muy corto debe fallar");
        assertFalse(validador.validarPassword("      "), "Password solo espacios debe fallar");

        System.out.println("✅ PRUEBA 2 COMPLETADA: Validación de contraseñas");
    }

    /**
     * PRUEBA 3: Validación de películas
     * Tipo: Funcionalidad
     */
    @Test
    public void testValidacionPelicula() {
        // Caso válido
        Pelicula peliculaValida = new Pelicula();
        peliculaValida.setTitulo("El Padrino");
        peliculaValida.setDirector("Francis Ford Coppola");
        peliculaValida.setAnoLanzamiento(1972);
        peliculaValida.setDuracionMinutos(175);

        assertTrue(validador.validarPelicula(peliculaValida), "Película válida debe pasar validación");

        // Casos inválidos
        Pelicula peliculaInvalida = new Pelicula();
        peliculaInvalida.setTitulo("");
        peliculaInvalida.setDirector("Director");
        peliculaInvalida.setAnoLanzamiento(1800); // Muy antiguo
        peliculaInvalida.setDuracionMinutos(-10); // Negativo

        assertFalse(validador.validarPelicula(peliculaInvalida), "Película con datos inválidos debe fallar");

        System.out.println("✅ PRUEBA 3 COMPLETADA: Validación de películas");
    }

    /**
     * PRUEBA 4: Validación de reseñas
     * Tipo: Funcionalidad
     */
    @Test
    public void testValidacionResena() {
        // Caso válido
        Resena resenaValida = new Resena();
        resenaValida.setTextoResena("Excelente película, muy recomendada para todos.");
        resenaValida.setPuntuacion(5);

        assertTrue(validador.validarResena(resenaValida), "Reseña válida debe pasar validación");

        // Casos inválidos
        Resena resenaInvalida = new Resena();
        resenaInvalida.setTextoResena("Corta"); // Muy corta
        resenaInvalida.setPuntuacion(0); // Puntuación inválida

        assertFalse(validador.validarTextoResena(resenaInvalida.getTextoResena()), "Reseña con texto muy corto debe fallar");
        assertFalse(validador.validarPuntuacion(resenaInvalida.getPuntuacion()), "Puntuación fuera de rango debe fallar");

        System.out.println("✅ PRUEBA 4 COMPLETADA: Validación de reseñas");
    }

    /**
     * PRUEBA 5: Validación de email
     * Tipo: Funcionalidad
     */
    @Test
    public void testValidacionEmail() {
        // Casos válidos
        assertTrue(validador.validarEmail("usuario@cinefan.com"), "Email válido debe pasar");
        assertTrue(validador.validarEmail("test.user@mail.cinefan.com"), "Email con subdominios debe ser válido");

        // Casos inválidos
        assertFalse(validador.validarEmail("usuariocinefan.com"), "Email sin @ debe fallar");
        assertFalse(validador.validarEmail("usuario@"), "Email sin dominio debe fallar");
        assertFalse(validador.validarEmail(""), "Email vacío debe fallar");

        System.out.println("✅ PRUEBA 5 COMPLETADA: Validación de email");
    }

    // =================== PRUEBAS DE RENDIMIENTO ===================

    /**
     * PRUEBA 6: Rendimiento de validaciones
     * Tipo: Rendimiento
     */
    @Test
    public void testRendimientoValidaciones() {
        long tiempoInicio = System.currentTimeMillis();

        // Ejecutar 1000 validaciones
        for (int i = 0; i < 1000; i++) {
            validador.validarNombreUsuario("usuario" + i);
            validador.validarEmail("usuario" + i + "@test.com");
            validador.validarPassword("password123");
        }

        long tiempoFin = System.currentTimeMillis();
        long tiempoTranscurrido = tiempoFin - tiempoInicio;

        // Debe completarse en menos de 1 segundo
        assertTrue(tiempoTranscurrido < 1000, "Validaciones deben ser rápidas (< 1000ms)");

        System.out.println("✅ PRUEBA 6 COMPLETADA: Rendimiento - " + tiempoTranscurrido + "ms");
    }

    /**
     * PRUEBA 7: Límites de datos
     * Tipo: Rendimiento/Seguridad
     */
    @Test
    public void testLimitesDatos() {
        // Probar límites máximos
        String textoLargo = "a".repeat(Constantes.MAX_LONGITUD_RESENA + 1);
        assertFalse(validador.validarTextoResena(textoLargo), "Texto muy largo debe fallar");

        String textoMaximo = "a".repeat(Constantes.MAX_LONGITUD_RESENA);
        assertTrue(validador.validarTextoResena(textoMaximo), "Texto en límite máximo debe pasar");

        // Probar años límite
        assertFalse(validador.validarAno(Constantes.MIN_ANO_PELICULA - 1), "Año muy antiguo debe fallar");
        assertFalse(validador.validarAno(Constantes.MAX_ANO_PELICULA + 1), "Año futuro muy lejano debe fallar");

        System.out.println("✅ PRUEBA 7 COMPLETADA: Límites de datos");
    }

    // =================== PRUEBAS DE SEGURIDAD ===================

    /**
     * PRUEBA 8: Protección contra inyección SQL
     * Tipo: Seguridad
     */
    @Test
    public void testProteccionSQLInjection() {
        // Intentos de inyección SQL
        String[] intentosInyeccion = {
                "'; DROP TABLE usuarios; --",
                "' OR '1'='1",
                "admin'; DELETE FROM peliculas; --",
                "<script>alert('XSS')</script>",
                "' UNION SELECT password FROM usuarios --"
        };

        for (String intento : intentosInyeccion) {
            // Los validadores deben rechazar estos intentos
            assertFalse(validador.validarNombreUsuario(intento), "Intento de inyección debe ser rechazado: " + intento);
            assertFalse(validador.validarTextoResena(intento), "Intento XSS debe ser rechazado: " + intento);
        }

        System.out.println("✅ PRUEBA 8 COMPLETADA: Protección SQL Injection");
    }

    /**
     * PRUEBA 9: Sanitización de entrada
     * Tipo: Seguridad
     */
    @Test
    public void testSanitizacionEntrada() {
        // Probar caracteres peligrosos
        String entradaPeligrosa = "<script>alert('hack')</script>";
        String entradaSanitizada = validador.sanitizarTexto(entradaPeligrosa);

        assertFalse(entradaSanitizada.contains("<script>"), "Texto sanitizado no debe contener scripts");

        // Probar espacios en blanco
        String entradaEspacios = "   texto con espacios   ";
        String entradaLimpia = validador.limpiarTexto(entradaEspacios);

        assertEquals("texto con espacios", entradaLimpia, "Espacios deben ser eliminados");

        System.out.println("✅ PRUEBA 9 COMPLETADA: Sanitización de entrada");
    }

    /**
     * PRUEBA 10: Integridad de constantes
     * Tipo: Funcionalidad/Seguridad
     */
    @Test
    public void testIntegridadConstantes() {
        // Verificar que las constantes tengan valores lógicos
        assertTrue(Constantes.MIN_LONGITUD_USUARIO > 0, "Longitud mínima usuario debe ser positiva");
        assertTrue(Constantes.MIN_LONGITUD_PASSWORD >= 4, "Longitud mínima password debe ser razonable");
        assertTrue(Constantes.MIN_ANO_PELICULA >= 1888, "Año mínimo película debe ser lógico"); // Primera película
        assertTrue(Constantes.MAX_ANO_PELICULA > 2024, "Año máximo debe estar en el futuro");
        assertFalse(Constantes.URL_BASE_API.isEmpty(), "URL base API no debe estar vacía");

        // Verificar coherencia entre límites
        assertTrue(Constantes.MAX_LONGITUD_USUARIO > Constantes.MIN_LONGITUD_USUARIO, "Límite máximo debe ser mayor que mínimo - Usuario");
        assertTrue(Constantes.MAX_LONGITUD_RESENA > Constantes.MIN_LONGITUD_RESENA, "Límite máximo debe ser mayor que mínimo - Reseña");

        System.out.println("✅ PRUEBA 10 COMPLETADA: Integridad de constantes");
    }
}