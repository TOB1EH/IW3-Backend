package ar.edu.iua.iw3.model.business;

import lombok.Builder;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class EmptyNameException extends Exception {

    /**
     * Crea una nueva excepción de tipo {@code EmptyNameException} con un mensaje descriptivo 
     * y una causa subyacente.
     *
     * @param message Mensaje que describe el motivo de la excepción.
     * @param ex      Excepción que causó esta excepción.
     */
    @Builder
    public EmptyNameException(String message, Throwable ex) {
        super(message, ex);
    }

    /**
     * Crea una nueva excepción de tipo {@code EmptyNameException} con un mensaje descriptivo.
     *
     * @param message Mensaje que describe el motivo de la excepción.
     */
    @Builder
    public EmptyNameException(String message) {
        super(message);
    }

    /**
     * Crea una nueva excepción de tipo {@code EmptyNameException} a partir de una causa subyacente.
     *
     * @param ex Excepción que causó esta excepción.
     */
    @Builder
    public EmptyNameException(Throwable ex) {
        super(ex);
    }
}