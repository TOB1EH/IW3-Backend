package ar.edu.iua.iw3.integration.cli2.model;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;


/**
 * Serializador personalizado de {@link ComponentCli2} para generar un JSON
 * con los atributos más relevantes del componente.
 *
 * <p>Ejemplo de salida JSON:
 * <pre>
 * {
 *   "id": 123,
 *   "component": "Harina"
 * }
 * </pre>
 * </p>
 */
public class ComponentCli2JsonSerializer extends StdSerializer<ComponentCli2> {

    /**
    * Constructor que recibe la clase objetivo a serializar.
    *
    * @param t Clase del objeto a serializar.
    */
    protected ComponentCli2JsonSerializer(Class<ComponentCli2> t) {
        super(t);
    }

    /**
    * Serializa una instancia de {@link ComponentCli2} a JSON.
    *
    * <p>La salida incluye los siguientes campos:
    * <ul>
    *   <li><b>id</b>: identificador numérico del componente.</li>
    *   <li><b>component</b>: nombre o descripción del componente.</li>
    * </ul>
    * </p>
    *
    * @param value    Objeto {@link ComponentCli2} a serializar.
    * @param gen      Generador JSON utilizado para escribir la salida.
    * @param provider Proveedor de serialización (no utilizado en esta implementación).
    * @throws IOException Si ocurre un error durante la escritura del JSON.
    */
    @Override
    public void serialize(ComponentCli2 value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        /**
         * {
         * "id": 123,
         * "component": "Harina"
         * }
         */
        gen.writeStartObject(); // {
        gen.writeNumberField("id", value.getId()); // "id": 123,
        gen.writeStringField("component", value.getComponent()); // "component": "Harina"
        gen.writeEndObject(); // }
    }
    
}
