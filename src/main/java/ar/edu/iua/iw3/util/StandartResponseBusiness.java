package ar.edu.iua.iw3.util;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

/**
 * Implementación del servicio para construcción de respuestas estándar en la aplicación IW3.
 *
 * Esta clase concreta implementa IStandartResponseBusiness, proporcionando
 * la lógica para crear objetos StandartResponse con configuración externa.
 * Utiliza propiedades de configuración para determinar si incluir información
 * de desarrollo en las respuestas.
 *
 * Propósito: Centralizar la creación de respuestas de error estandarizadas,
 * permitiendo configuración externa para incluir o excluir detalles técnicos
 * según el entorno (desarrollo vs producción).
 *
 * Cómo funciona: Anotada con @Service para ser gestionada por Spring.
 * Inyecta el valor de la propiedad 'dev.info.enabled' desde application.properties
 * (por defecto false). El método build() crea una nueva instancia de StandartResponse
 * y configura todos sus campos con los parámetros proporcionados.
 *
 * Cómo usar: Inyectar en controladores REST que necesiten devolver respuestas
 * de error. Llamar al método build() con el HttpStatus apropiado, la excepción
 * y el mensaje. Ejemplo: responseBusiness.build(HttpStatus.NOT_FOUND, null, "Producto no encontrado")
 * En application.properties, configurar dev.info.enabled=true para incluir
 * stack traces en desarrollo.
 */
@Service
public class StandartResponseBusiness implements IStandartResponseBusiness {

    /**
     * Flag que indica si se debe incluir información de desarrollo en las respuestas.
     *
     * Este valor se inyecta desde las propiedades de configuración de Spring.
     * Por defecto es false para evitar exponer información sensible en producción.
     */
    @Value("${dev.info.enabled:false}")
    private boolean devInfoEnabled;

    /**
     * Construye una respuesta estándar con los parámetros especificados.
     *
     * Propósito: Crear y configurar un objeto StandartResponse completamente
     * inicializado para su uso inmediato en respuestas HTTP.
     *
     * Cómo funciona: Instancia un nuevo StandartResponse, configura el flag
     * de información de desarrollo con el valor inyectado, establece el mensaje,
     * el estado HTTP y la excepción. Todos los campos se configuran para que
     * el objeto esté listo para serialización JSON.
     *
     * Cómo usar: Llamar desde controladores cuando se produzca un error o
     * se necesite una respuesta estandarizada. Los parámetros determinan el
     * contenido de la respuesta. Ejemplo típico en catch block:
     * return new ResponseEntity<>(responseBusiness.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
     *
     * @param httpStatus El código de estado HTTP para la respuesta
     * @param ex La excepción que causó el error (puede ser null)
     * @param message El mensaje descriptivo del error o respuesta
     * @return Una instancia completamente configurada de StandartResponse
     */
    @Override
    public StandartResponse build(HttpStatus httpStatus, Throwable ex, String message) {
        StandartResponse sr=new StandartResponse();
        sr.setDevInfoEnabled(devInfoEnabled);
        sr.setMessage(message);
        sr.setHttpStatus(httpStatus);
        sr.setEx(ex);
        return sr;
    }
}
