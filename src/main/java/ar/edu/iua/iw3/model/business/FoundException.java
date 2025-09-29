package ar.edu.iua.iw3.model.business;

import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Excepción personalizada para indicar que una entidad ya existe en la aplicación IW3.
 *
 * Esta clase representa errores que ocurren cuando se intenta crear o agregar
 * una entidad que ya existe en el sistema, violando reglas de unicidad.
 * Es un tipo específico de error de negocio enfocado en conflictos de duplicados.
 *
 * Propósito: Comunicar de manera específica cuando operaciones de creación
 * fallan debido a entidades preexistentes, permitiendo manejo diferenciado
 * en la capa de presentación (por ejemplo, devolver HTTP 409 Conflict).
 *
 * Cómo funciona: Extiende Exception y utiliza Lombok para generar constructores
 * con diferentes combinaciones de parámetros. Similar a BusinessException,
 * pero semánticamente específica para casos de "encontrado cuando no se esperaba".
 *
 * Cómo usar: Lanzar en operaciones de negocio cuando se detecte un conflicto
 * de unicidad, como intentar agregar un producto con nombre ya existente.
 * En controladores, capturar para devolver respuestas apropiadas indicando
 * el conflicto. Ejemplo: throw FoundException.builder()
 * .message("Producto con nombre ya existe").build();
 */
@NoArgsConstructor
public class FoundException extends Exception {

    /**
     * Constructor con mensaje y causa.
     *
     * @param message Mensaje descriptivo del conflicto de duplicado
     * @param ex Excepción original que causó este error (puede ser null)
     */
    @Builder
    public FoundException(String message, Throwable ex) {
        super(message, ex);
    }

    /**
     * Constructor con solo mensaje.
     *
     * @param message Mensaje descriptivo del conflicto de duplicado
     */
    @Builder
    public FoundException(String message) {
        super(message);
    }

    /**
     * Constructor con solo causa.
     *
     * @param ex Excepción original que causó este error
     */
    @Builder
    public FoundException(Throwable ex) {
        super(ex);
    }

}
