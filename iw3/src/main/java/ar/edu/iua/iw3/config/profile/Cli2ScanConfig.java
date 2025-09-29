package ar.edu.iua.iw3.config.profile;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Configuración de Spring Boot específica para el perfil <b>cli2</b>.
 * 
 * <p>Esta clase define la configuración necesaria para habilitar 
 * los repositorios JPA y el escaneo de entidades cuando el perfil 
 * activo de Spring es {@code cli2}.</p>
 *
 * <p>Incluye:
 * <ul>
 *   <li>Activación de repositorios JPA en el paquete base {@code ar.edu.iua.iw3}.</li>
 *   <li>Definición de un filtro de exclusión que impide la carga del 
 *       paquete de integración {@code cli1}.</li>
 *   <li>Escaneo de entidades en los paquetes principales de modelo, 
 *       autenticación y en el módulo de integración {@code cli2}.</li>
 *   <li>Posibilidad de registrar clases de entidades específicas mediante 
 *       {@code basePackageClasses}, aunque actualmente se encuentran comentadas.</li>
 * </ul>
 * </p>
 *
 * <p>Esta clase se carga únicamente si el perfil activo de Spring 
 * coincide con {@code cli2}.</p>
 */
@Configuration
//Repositorios
@EnableJpaRepositories(basePackages = "ar.edu.iua.iw3", 
excludeFilters = {
		@ComponentScan.Filter(type = FilterType.REGEX, pattern = "ar\\.edu\\.iua\\.iw3\\.integration\\.cli1\\..*" )
		//Se pueden definir más filtros de exclusión
		//,@ComponentScan.Filter(type = FilterType.REGEX, pattern = "org\\.magm\\.backend\\.integration\\.cliN\\..*" )
})
//Entidades
@EntityScan(basePackages = { 
		"ar.edu.iua.iw3.model", 
		"ar.edu.iua.iw3.auth", 
		"ar.edu.iua.iw3.integration.cli2.model" 
},
basePackageClasses = {
	// Se pueden cargar entidades particulares que no estén en los paquetes base
	//ar.edu.iua.iw3.integration.cliN.model.Entidad1.class, 
	//ar.edu.iua.iw3.integration.cliN.model.Entidad2.class
})


//@ConditionalOnExpression(value = "'${spring.profiles.active:-}'=='cli1'")
@Profile("cli2")
public class Cli2ScanConfig {
	/**
     * Clase de configuración vacía.
     * 
     * <p>No contiene métodos ni atributos explícitos. 
     * Su propósito principal es definir, mediante anotaciones, 
     * la configuración del contexto de Spring para el perfil {@code cli2}.</p>
     */
}
