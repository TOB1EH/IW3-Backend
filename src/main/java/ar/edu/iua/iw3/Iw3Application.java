package ar.edu.iua.iw3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import ar.edu.iua.iw3.integration.cli2.model.business.IProductCli2Business;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase principal de la aplicación Spring Boot IW3.
 *
 * Esta clase sirve como punto de entrada para la aplicación backend.
 * Utiliza la anotación @SpringBootApplication que combina @Configuration,
 * @EnableAutoConfiguration y @ComponentScan, permitiendo la configuración
 * automática de Spring Boot, el escaneo de componentes y la configuración
 * personalizada.
 *
 * Propósito: Iniciar y ejecutar la aplicación Spring Boot, que maneja
 * la lógica de negocio para productos y otras funcionalidades.
 *
 * Cómo funciona: El método main invoca SpringApplication.run() pasando
 * la clase principal y los argumentos de línea de comandos. Spring Boot
 * se encarga de inicializar el contexto de la aplicación, configurar
 * beans, iniciar el servidor embebido (por defecto Tomcat) y manejar
 * las dependencias.
 *
 * Cómo usar: Esta clase se ejecuta automáticamente al iniciar la aplicación
 * con comandos como 'mvn spring-boot:run' o ejecutando el JAR empaquetado.
 * No requiere intervención manual directa.
 */
@SpringBootApplication
@Slf4j
public class Iw3Application extends SpringBootServletInitializer implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Iw3Application.class, args);
	}

	@Value("${spring.profiles.active}")
	private String profile;

	@Autowired
	private IProductCli2Business productCli2Business;


	@Override
	public void run(String... args) throws Exception {
		log.info("Perfil Activo: '{}'", profile);
		/*log.info(
				"Default -------------------------------------------------------------------------------------------------------");
		productCli2Business.listExpired(new Date());
		log.info(
				"Customizada ---------------------------------------------------------------------------------------------------");
		productCli2Business.listSlim();
		
		log.info("Cantidad de productos de la categoría id=3: {}", productDAO.countProductsByCategory(3));
		log.info("Set stock=true producto id que no existe, resultado={}", productDAO.setStock(true, 333));*/
	}


}
