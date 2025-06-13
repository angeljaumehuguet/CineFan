package com.example.cinefan.tests;

import org.testng.annotations.Test;
import org.junit.Before;
import static org.junit.Assert.*;
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

    @Before
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
        assertTrue("Usuario válido debe pasar validación",
                validador.validarNombreUsuario("cinefan123"));
        assertTrue("Usuario con longitud mínima debe ser válido",
                validador.validarNombreUsuario("abc"));

        // Casos inválidos
        assertFalse("Usuario vacío debe fallar",
                validador.validarNombreUsuario(""));
        assertFalse("Usuario muy corto debe fallar",
                validador.validarNombreUsuario("ab"));
        assertFalse("Usuario muy largo debe fallar",
                validador.validarNombreUsuario("a".repeat(51)));
        assertFalse("Usuario con caracteres especiales debe fallar",
                validador.validarNombreUsuario("user@name"));

        System.out.println("✅ PRUEBA 1 COMPLETADA: Validación de usuarios");
    }

    /**
     * PRUEBA 2: Validación de contraseñas
     * Tipo: Seguridad
     */
    @Test
    public void testValidacionPassword() {
        // Casos válidos
        assertTrue("Password válido debe pasar",
                validador.validarPassword("123456"));
        assertTrue("Password con caracteres especiales debe ser válido",
                validador.validarPassword("MiPass123!"));

        // Casos inválidos
        assertFalse("Password vacío debe fallar",
                validador.validarPassword(""));
        assertFalse("Password muy corto debe fallar",
                validador.validarPassword("12345"));
        assertFalse("Password solo espacios debe fallar",
                validador.validarPassword("      "));

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

        assertTrue("Película válida debe pasar validación",
                validador.validarPelicula(peliculaValida));

        // Casos inválidos
        Pelicula peliculaInvalida = new Pelicula();
        peliculaInvalida.setTitulo("");
        peliculaInvalida.setDirector("Director");
        peliculaInvalida.setAnoLanzamiento(1800); // Muy antiguo
        peliculaInvalida.setDuracionMinutos(-10); // Negativo

        assertFalse("Película con datos inválidos debe fallar",
                validador.validarPelicula(peliculaInvalida));

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

        assertTrue("Reseña válida debe pasar validación",
                validador.validarResena(resenaValida));

        // Casos inválidos
        Resena resenaInvalida = new Resena();
        resenaInvalida.setTextoResena("Corta"); // Muy corta
        resenaInvalida.setPuntuacion(0); // Puntuación inválida

        assertFalse("Reseña con texto muy corto debe fallar",
                validador.validarTextoResena(resenaInvalida.getTextoResena()));
        assertFalse("Puntuación fuera de rango debe fallar",
                validador.validarPuntuacion(resenaInvalida.getPuntuacion()));

        System.out.println("✅ PRUEBA 4 COMPLETADA: Validación de reseñas");
    }

    /**
     * PRUEBA 5: Validación de email
     * Tipo: Funcionalidad
     */
    @Test
    public void testValidacionEmail() {
        // Casos válidos
        assertTrue("Email válido debe pasar",
                validador.validarEmail("usuario@cinefan.com"));
        assertTrue("Email con subdominios debe ser válido",
                validador.validarEmail("test.user@mail.cinefan.com"));

        // Casos inválidos
        assertFalse("Email sin @ debe fallar",
                validador.validarEmail("usuariocinefan.com"));
        assertFalse("Email sin dominio debe fallar",
                validador.validarEmail("usuario@"));
        assertFalse("Email vacío debe fallar",
                validador.validarEmail(""));

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
        assertTrue("Validaciones deben ser rápidas (< 1000ms)",
                tiempoTranscurrido < 1000);

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
        assertFalse("Texto muy largo debe fallar",
                validador.validarTextoResena(textoLargo));

        String textoMaximo = "a".repeat(Constantes.MAX_LONGITUD_RESENA);
        assertTrue("Texto en límite máximo debe pasar",
                validador.validarTextoResena(textoMaximo));

        // Probar años límite
        assertFalse("Año muy antiguo debe fallar",
                validador.validarAno(Constantes.MIN_ANO_PELICULA - 1));
        assertFalse("Año futuro muy lejano debe fallar",
                validador.validarAno(Constantes.MAX_ANO_PELICULA + 1));

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
            assertFalse("Intento de inyección debe ser rechazado: " + intento,
                    validador.validarNombreUsuario(intento));
            assertFalse("Intento XSS debe ser rechazado: " + intento,
                    validador.validarTextoResena(intento));
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

        assertFalse("Texto sanitizado no debe contener scripts",
                entradaSanitizada.contains("<script>"));

        // Probar espacios en blanco
        String entradaEspacios = "   texto con espacios   ";
        String entradaLimpia = validador.limpiarTexto(entradaEspacios);

        assertEquals("Espacios deben ser eliminados",
                "texto con espacios", entradaLimpia);

        System.out.println("✅ PRUEBA 9 COMPLETADA: Sanitización de entrada");
    }

    /**
     * PRUEBA 10: Integridad de constantes
     * Tipo: Funcionalidad/Seguridad
     */
    @Test
    public void testIntegridadConstantes() {
        // Verificar que las constantes tengan valores lógicos
        assertTrue("Longitud mínima usuario debe ser positiva",
                Constantes.MIN_LONGITUD_USUARIO > 0);
        assertTrue("Longitud mínima password debe ser razonable",
                Constantes.MIN_LONGITUD_PASSWORD >= 4);
        assertTrue("Año mínimo película debe ser lógico",
                Constantes.MIN_ANO_PELICULA >= 1888); // Primera película
        assertTrue("Año máximo debe estar en el futuro",
                Constantes.MAX_ANO_PELICULA > 2024);
        assertTrue("URL base API no debe estar vacía",
                !Constantes.URL_BASE_API.isEmpty());

        // Verificar coherencia entre límites
        assertTrue("Límite máximo debe ser mayor que mínimo - Usuario",
                Constantes.MAX_LONGITUD_USUARIO > Constantes.MIN_LONGITUD_USUARIO);
        assertTrue("Límite máximo debe ser mayor que mínimo - Reseña",
                Constantes.MAX_LONGITUD_RESENA > Constantes.MIN_LONGITUD_RESENA);

        System.out.println("✅ PRUEBA 10 COMPLETADA: Integridad de constantes");
    }

    // =================== MÉTODOS DE UTILIDAD PARA PRUEBAS ===================

    /**
     * Ejecutar todas las pruebas y mostrar resumen
     */
    public void ejecutarTodasLasPruebas() {
        System.out.println("=".repeat(60));
        System.out.println("🧪 EJECUTANDO SUITE DE PRUEBAS CINEFAN");
        System.out.println("=".repeat(60));

        try {
            testValidacionUsuario();
            testValidacionPassword();
            testValidacionPelicula();
            testValidacionResena();
            testValidacionEmail();
            testRendimientoValidaciones();
            testLimitesDatos();
            testProteccionSQLInjection();
            testSanitizacionEntrada();
            testIntegridadConstantes();

            System.out.println("=".repeat(60));
            System.out.println("✅ TODAS LAS PRUEBAS COMPLETADAS EXITOSAMENTE");
            System.out.println("📊 RESUMEN:");
            System.out.println("   • Pruebas de Funcionalidad: 5/5");
            System.out.println("   • Pruebas de Rendimiento: 2/2");
            System.out.println("   • Pruebas de Seguridad: 3/3");
            System.out.println("   • TOTAL: 10/10 pruebas aprobadas");
            System.out.println("=".repeat(60));

        } catch (Exception e) {
            System.err.println("❌ ERROR EN PRUEBAS: " + e.getMessage());
            e.printStackTrace();
        }
    }
}