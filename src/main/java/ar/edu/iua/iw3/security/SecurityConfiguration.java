package ar.edu.iua.iw3.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

/**
 * Configuración de seguridad para la aplicación IW3 usando Spring Security.
 *
 * Esta clase configura la seguridad web de la aplicación, definiendo
 * políticas de autorización, manejo de CORS y CSRF, y otros aspectos
 * de seguridad. Actualmente está configurada de manera permisiva para
 * desarrollo.
 *
 * Propósito: Centralizar la configuración de seguridad de la aplicación,
 * permitiendo control de acceso a endpoints y protección contra ataques
 * comunes como CSRF y problemas de CORS.
 *
 * Cómo funciona: Utiliza anotaciones de Spring Security para habilitar
 * la configuración web y la seguridad a nivel de método. Define un bean
 * SecurityFilterChain que configura HttpSecurity con políticas específicas.
 * Actualmente deshabilita CORS y CSRF, y permite todas las solicitudes sin
 * autenticación.
 *
 * Cómo usar: Esta configuración se aplica automáticamente por Spring Security.
 * Para modificar políticas de seguridad, editar el método filterChain().
 * Por ejemplo, cambiar .permitAll() por .authenticated() para requerir login.
 * Las anotaciones @PreAuthorize en métodos requieren @EnableMethodSecurity.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    /**
     * Define la cadena de filtros de seguridad para las solicitudes HTTP.
     *
     * Propósito: Configurar las reglas de seguridad que se aplican a todas
     * las solicitudes entrantes a la aplicación.
     *
     * Cómo funciona: Deshabilita CORS (Cross-Origin Resource Sharing) para
     * evitar problemas con solicitudes desde diferentes orígenes. Deshabilita
     * CSRF (Cross-Site Request Forgery) protection, común en APIs REST.
     * Configura la autorización para permitir todas las solicitudes (/**)
     * sin requerir autenticación, útil para desarrollo o APIs públicas.
     *
     * Cómo usar: Spring aplica automáticamente esta configuración. Para
     * cambiar a un modo más seguro, modificar las reglas de authorizeHttpRequests.
     * Por ejemplo, agregar .requestMatchers("/api/**").authenticated() para
     * requerir autenticación en endpoints de API.
     *
     * @param http El objeto HttpSecurity para configurar
     * @return La SecurityFilterChain configurada
     * @throws Exception Si ocurre un error en la configuración
     */
  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // CORS: https://developer.mozilla.org/es/docs/Web/HTTP/CORS
    // CSRF: https://developer.mozilla.org/es/docs/Glossary/CSRF
    http.cors(CorsConfigurer::disable);
    http.csrf(AbstractHttpConfigurer::disable);
    http.authorizeHttpRequests(auth -> auth
    		.requestMatchers("/**").permitAll()
    	    .anyRequest().authenticated()
    );
    return http.build();
  }
}
