package ar.edu.iua.iw3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import lombok.extern.slf4j.Slf4j;

/**
 * Clase principal de la aplicación :contentReference[oaicite:0]{index=0}.
 * <p>
 * Esta clase actúa como punto de entrada para la aplicación. Extiende
 * {@link SpringBootServletInitializer} para permitir su despliegue en un
 * contenedor de servlets externo (como :contentReference[oaicite:1]{index=1})
 * y además implementa {@link CommandLineRunner} para ejecutar lógica
 * personalizada inmediatamente después de que el contexto de
 * {@link org.springframework.boot.SpringApplication} haya sido inicializado.
 * </p>
 *
 * <p>
 * Al arrancar, la aplicación registra en el log el perfil activo
 * (por ejemplo, <code>mysqldev</code> o <code>mysqlprod</code>)
 * que ha sido configurado en el archivo <code>pom.xml</code> o en las
 * propiedades de :contentReference[oaicite:2]{index=2}.
 * </p>
 *
 * @author Agustin Brambilla
 * @since 1.0
 */
@SpringBootApplication
@Slf4j
public class Iw3Application extends SpringBootServletInitializer implements CommandLineRunner {

	/**
     * Método principal que inicia la aplicación.
     *
     * @param args argumentos de línea de comandos.
     */
	public static void main(String[] args) {
		SpringApplication.run(Iw3Application.class, args);
	}


	/** 
	 * Obtiene el valor del perfil activo desde el archivo pom.xml
	 * (mysqldev o mysqlprod)
	 */
	@Value("${spring.profiles.active}")
	private String profile;

	/**
     * Ejecuta lógica personalizada al iniciar la aplicación.
     * <p>
     * En este caso, registra en el log el perfil activo actual, lo cual permite
     * verificar en qué entorno (desarrollo, pruebas o producción) se está
     * ejecutando la aplicación.
     * </p>
     *
     * @param args argumentos de línea de comandos pasados al iniciar.
     * @throws Exception si ocurre un error durante la ejecución.
     */
	@Override
	public void run(String... args) throws Exception {
		log.info("Perfil activo '{}'", profile);
	}

}
