package ar.edu.iua.iw3.config.profile;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


/**
 * Configuración de Spring Boot específica para el perfil <b>cli1</b>.
 * 
 * <p>Esta clase define la configuración necesaria para inicializar 
 * los repositorios JPA y el escaneo de entidades cuando el 
 * perfil activo de Spring es {@code cli1}.</p>
 *
 * <p>Incluye:
 * <ul>
 *   <li>Activación de repositorios JPA mediante {@link EnableJpaRepositories}.</li>
 *   <li>Exclusión de ciertos paquetes de integración específicos 
 *       (por ejemplo, {@code ar.edu.iua.iw3.integration.cli2}).</li>
 *   <li>Escaneo de entidades en paquetes relevantes del dominio, 
 *       seguridad y módulos de integración relacionados con {@code cli1}.</li>
 * </ul>
 * </p>
 *
 * <p>Esta clase se carga solo si el perfil activo de Spring 
 * coincide con {@code cli1}.</p>
 *
 */
@Configuration
//Repositorios
@EnableJpaRepositories(basePackages = "ar.edu.iua.iw3", 
excludeFilters = {
		@ComponentScan.Filter(type = FilterType.REGEX, pattern = "ar\\.edu\\.iua\\.iw3\\.integration\\.cli2\\..*" )
		//Se pueden definir más filtros de exclusión
		//,@ComponentScan.Filter(type = FilterType.REGEX, pattern = "org\\.magm\\.backend\\.integration\\.cliN\\..*" )
})
//Entidades
@EntityScan(basePackages = { 
		"ar.edu.iua.iw3.model", 
		"ar.edu.iua.iw3.auth", 
		"ar.edu.iua.iw3.integration.cli1.model" 
},
basePackageClasses = {
	// Se pueden cargar entidades particulares que no estén en los paquetes base
	//ar.edu.iua.iw3.integration.cliN.model.Entidad1.class, 
	//ar.edu.iua.iw3.integration.cliN.model.Entidad2.class
})


//@ConditionalOnExpression(value = "'${spring.profiles.active:-}'=='cli1'")
@Profile("cli1")
public class Cli1ScanConfig {
    /**
     * Clase de configuración vacía.
     * 
     * <p>No contiene métodos ni atributos explícitos. 
     * Su propósito principal es definir, mediante anotaciones, 
     * la configuración del contexto de Spring para el perfil {@code cli1}.</p>
     */
}