package ar.edu.iua.iw3.util;

import org.springframework.http.HttpStatus;

/**
 * Interfaz para la construcción de respuestas estándar en la aplicación IW3.
 *
 * Esta interfaz define el contrato para crear instancias de StandartResponse,
 * que encapsulan información de error o respuesta de manera estandarizada.
 * Permite diferentes implementaciones para la construcción de respuestas.
 *
 * Propósito: Establecer un contrato claro para la creación de respuestas
 * estándar, facilitando el desacoplamiento y permitiendo mocking en tests.
 * Asegura consistencia en cómo se construyen las respuestas de error.
 *
 * Cómo funciona: Define un método build() que toma un HttpStatus, una
 * excepción opcional, y un mensaje, retornando un objeto StandartResponse
 * completamente configurado.
 *
 * Cómo usar: Implementar esta interfaz en clases concretas que manejen
 * la lógica de construcción de respuestas. Inyectar en controladores o
 * servicios que necesiten crear respuestas de error. Ejemplo:
 * StandartResponse response = responseBusiness.build(HttpStatus.INTERNAL_SERVER_ERROR, e, "Error interno");
 */
public interface IStandartResponseBusiness {

    /**
     * Construye una respuesta estándar con la información proporcionada.
     *
     * Propósito: Crear un objeto StandartResponse configurado con el estado
     * HTTP, excepción y mensaje especificados.
     *
     * Cómo funciona: La implementación concreta crea una nueva instancia
     * de StandartResponse y configura sus campos con los parámetros dados.
     *
     * Cómo usar: Llamar con los parámetros apropiados según el contexto
     * del error. El HttpStatus determina el código de respuesta HTTP,
     * la excepción proporciona detalles técnicos, y el mensaje es el
     * texto descriptivo para el usuario.
     *
     * @param httpStatus El código de estado HTTP para la respuesta
     * @param ex La excepción que causó el error (puede ser null)
     * @param message El mensaje descriptivo del error
     * @return Una instancia de StandartResponse configurada
     */
    public StandartResponse build(HttpStatus httpStatus, Throwable ex, String message);
}
