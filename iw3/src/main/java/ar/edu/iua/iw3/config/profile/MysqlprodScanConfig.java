package ar.edu.iua.iw3.config.profile;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Configuración de Spring Boot específica para el perfil <b>mysqlprod</b>.
 * 
 * <p>Esta clase define la configuración necesaria para habilitar 
 * los repositorios JPA y el escaneo de entidades cuando el perfil 
 * activo de Spring es {@code mysqlprod}.</p>
 *
 * <p>Incluye:
 * <ul>
 *   <li>Activación de repositorios JPA en el paquete base {@code ar.edu.iua.iw3}.</li>
 *   <li>Definición de filtros de exclusión que impiden la carga de 
 *       los paquetes de integración {@code cli1} y {@code cli2}.</li>
 *   <li>Escaneo de entidades en los paquetes de modelo principal 
 *       ({@code ar.edu.iua.iw3.model}) y autenticación 
 *       ({@code ar.edu.iua.iw3.auth}).</li>
 * </ul>
 * </p>
 *
 * <p>Esta clase se carga únicamente si el perfil activo de Spring 
 * coincide con {@code mysqlprod}.</p>
 *
 */
@Configuration
@EnableJpaRepositories(basePackages = "ar.edu.iua.iw3")
/* excludeFilters = {
		@ComponentScan.Filter(type = FilterType.REGEX, pattern = "ar\\.edu\\.iua\\.iw3\\.integration\\.cli1\\..*" ),
		@ComponentScan.Filter(type = FilterType.REGEX, pattern = "ar\\.edu\\.iua\\.iw3\\.integration\\.cli2\\..*" )
}) */


//Entidades
@EntityScan(basePackages = { 
		"ar.edu.iua.iw3.model", 
		"ar.edu.iua.iw3.auth",
        "ar.edu.iua.iw3.integration.cli1.model",
        "ar.edu.iua.iw3.integration.cli2.model"
})

@Profile("mysqlprod")
public class MysqlprodScanConfig {
    /**
     * Clase de configuración vacía.
     * 
     * <p>No contiene métodos ni atributos explícitos. 
     * Su propósito principal es definir, mediante anotaciones, 
     * la configuración del contexto de Spring para el perfil {@code mysqlprod}.</p>
     */
}