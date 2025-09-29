package ar.edu.iua.iw3.util;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase wrapper para respuestas estándar en la aplicación IW3.
 *
 * Esta clase encapsula información de respuesta para casos de error o
 * información, proporcionando una estructura consistente para respuestas
 * JSON en la API REST. Similar a las excepciones, pero para respuestas HTTP.
 *
 * Propósito: Estandarizar el formato de respuestas de error en la API,
 * incluyendo mensaje, código HTTP, y opcionalmente información de desarrollo
 * para debugging. Facilita el manejo consistente de errores en controladores.
 *
 * Cómo funciona: Utiliza Lombok para generar constructores y getters/setters.
 * Los campos con @JsonIgnore no se incluyen en la serialización JSON.
 * El método getCode() devuelve el valor numérico del HttpStatus.
 * getDevInfo() proporciona stack trace solo si devInfoEnabled es true.
 * getMessage() prioriza el mensaje personalizado sobre el de la excepción.
 *
 * Cómo usar: Instanciar a través de StandartResponseBusiness.build() en
 * controladores cuando ocurra un error. La respuesta se serializa automáticamente
 * a JSON. En desarrollo, habilitar devInfoEnabled para obtener stack traces.
 * Ejemplo de uso en controlador: return new ResponseEntity<>(response.build(...), status);
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StandartResponse {

    private String message;

    /**
     * Excepción que causó el error (no se incluye en JSON).
     */
    @JsonIgnore
    private Throwable ex;

    /**
     * Código de estado HTTP (no se incluye en JSON, pero se usa para getCode()).
     */
    @JsonIgnore
    private HttpStatus httpStatus;

    /**
     * Obtiene el código numérico del estado HTTP.
     *
     * @return El valor entero del HttpStatus (ej. 500 para INTERNAL_SERVER_ERROR)
     */
    public int getCode() {
        return httpStatus.value();
    }

    /**
     * Flag para habilitar información de desarrollo (no se incluye en JSON).
     */
    @JsonIgnore
    private boolean devInfoEnabled;

    /**
     * Obtiene información de desarrollo (stack trace) si está habilitado.
     *
     * Propósito: Proporcionar detalles técnicos para debugging en desarrollo.
     *
     * Cómo funciona: Si devInfoEnabled es true, devuelve el stack trace de la
     * excepción usando Apache Commons Lang. Si no hay excepción, retorna mensaje
     * indicando que no hay stack trace.
     *
     * @return Stack trace de la excepción si devInfoEnabled=true, null en caso contrario
     */
    public String getDevInfo() {
        if (devInfoEnabled) {
            if (ex != null) {
                return ExceptionUtils.getStackTrace(ex);
            } else {
                return "No stack trace";
            }
        } else {
            return null;
        }
    }

    /**
     * Obtiene el mensaje de la respuesta, con lógica de fallback.
     *
     * Propósito: Asegurar que siempre se devuelva un mensaje, priorizando
     * el mensaje personalizado sobre el de la excepción.
     *
     * Cómo funciona: Primero intenta devolver el mensaje establecido,
     * luego el mensaje de la excepción, y finalmente null si ninguno existe.
     *
     * @return El mensaje de la respuesta o null
     */
    public String getMessage() {
        if (message != null)
            return message;
        if (ex != null)
            return ex.getMessage();
        return null;
    }

}
