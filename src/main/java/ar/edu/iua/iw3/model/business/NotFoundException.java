package ar.edu.iua.iw3.model.business;

import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Excepción personalizada para indicar que una entidad no existe en la aplicación IW3.
 *
 * Esta clase representa errores que ocurren cuando se intenta acceder, actualizar
 * o eliminar una entidad que no se encuentra en el sistema. Es un tipo específico
 * de error de negocio para casos de "no encontrado".
 *
 * Propósito: Comunicar de manera específica cuando operaciones fallan porque
 * la entidad objetivo no existe, permitiendo manejo diferenciado en la capa
 * de presentación (por ejemplo, devolver HTTP 404 Not Found).
 *
 * Cómo funciona: Extiende Exception y utiliza Lombok para generar constructores
 * con diferentes combinaciones de parámetros. Similar a BusinessException,
 * pero semánticamente específica para casos de entidades inexistentes.
 *
 * Cómo usar: Lanzar en operaciones de negocio cuando se intente acceder a
 * una entidad que no existe, como cargar un producto por ID inexistente.
 * En controladores, capturar para devolver respuestas HTTP 404.
 * Ejemplo: throw NotFoundException.builder()
 * .message("Producto no encontrado").build();
 */
@NoArgsConstructor
public class NotFoundException extends Exception {

    /**
     * Constructor con mensaje y causa.
     *
     * @param message Mensaje descriptivo de la entidad no encontrada
     * @param ex Excepción original que causó este error (puede ser null)
     */
    @Builder
    public NotFoundException(String message, Throwable ex) {
        super(message, ex);
    }

    /**
     * Constructor con solo mensaje.
     *
     * @param message Mensaje descriptivo de la entidad no encontrada
     */
    @Builder
    public NotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor con solo causa.
     *
     * @param ex Excepción original que causó este error
     */
    @Builder
    public NotFoundException(Throwable ex) {
        super(ex);
    }

}
