package ar.edu.iua.iw3.model.business;

import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Excepción personalizada para errores en la lógica de negocio de la aplicación IW3.
 *
 * Esta clase representa errores que ocurren durante la ejecución de reglas
 * de negocio, validaciones o operaciones que no pueden completarse debido
 * a condiciones del dominio. Se diferencia de excepciones técnicas (como
 * SQLException) al enfocarse en problemas del negocio.
 *
 * Propósito: Proporcionar un mecanismo estandarizado para comunicar errores
 * de negocio a través de las capas de la aplicación, permitiendo manejo
 * específico en controladores y respuestas apropiadas a clientes.
 *
 * Cómo funciona: Extiende la clase Exception de Java, heredando su funcionalidad
 * básica. Utiliza anotaciones de Lombok (@Builder, @NoArgsConstructor) para
 * generar constructores automáticamente, facilitando la creación de instancias
 * con diferentes combinaciones de mensaje y causa. Los constructores permiten
 * crear excepciones con mensaje, causa, o ambos.
 *
 * Cómo usar: Lanzar en clases de negocio cuando se violen reglas del dominio,
 * por ejemplo, al intentar operaciones inválidas o cuando fallan validaciones.
 * En controladores, capturar para devolver respuestas de error apropiadas
 * (códigos HTTP 400-500). Ejemplo: throw BusinessException.builder()
 * .message("Producto no válido").build();
 */
@NoArgsConstructor
public class BusinessException extends Exception {

    /**
     * Constructor con mensaje y causa.
     *
     * @param message Mensaje descriptivo del error de negocio
     * @param ex Excepción original que causó este error (puede ser null)
     */
    @Builder
    public BusinessException(String message, Throwable ex) {
        super(message, ex);
    }

    /**
     * Constructor con solo mensaje.
     *
     * @param message Mensaje descriptivo del error de negocio
     */
    @Builder
    public BusinessException(String message) {
        super(message);
    }

    /**
     * Constructor con solo causa.
     *
     * @param ex Excepción original que causó este error
     */
    @Builder
    public BusinessException(Throwable ex) {
        super(ex);
    }

}
