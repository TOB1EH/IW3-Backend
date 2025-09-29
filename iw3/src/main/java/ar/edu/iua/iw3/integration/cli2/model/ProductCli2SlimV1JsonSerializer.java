package ar.edu.iua.iw3.integration.cli2.model;

import static java.time.temporal.ChronoUnit.DAYS;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import ar.edu.iua.iw3.util.JsonUtiles;


/**
* Serializador personalizado para {@link ProductCli2} que genera
* una representación simplificada en formato JSON (versión 1).
*
* <p>Ejemplo de salida JSON:
* <pre>
* {
*   "id": 1,
*   "product": "Pan",
*   "stock": true,
*   "price": 150.0,
*   "category": {
*       "id": 10,
*       "category": "Alimentos"
*   },
*   "expirationDate": "2025-12-31T00:00:00Z",
*   "daysExpired": -90,
*   "components": [
*       { "id": 123, "component": "Harina" },
*       { "id": 124, "component": "Levadura" }
*   ]
* }
* </pre>
* </p>
*/
public class ProductCli2SlimV1JsonSerializer extends StdSerializer<ProductCli2> {

    /**
    * Constructor que permite inicializar el serializer indicando la clase objetivo.
    *
    * @param t     Clase objetivo a serializar.
    * @param dummy Parámetro dummy requerido por la superclase.
    */
	public ProductCli2SlimV1JsonSerializer(Class<?> t, boolean dummy) {
		super(t, dummy);
	}

    /**
    * Serializa una instancia de {@link ProductCli2} a JSON.
    *
    * <p>Los campos generados son:
    * <ul>
    *   <li><b>id</b>: identificador numérico del producto.</li>
    *   <li><b>product</b>: nombre o descripción del producto.</li>
    *   <li><b>stock</b>: disponibilidad en inventario.</li>
    *   <li><b>price</b>: precio del producto.</li>
    *   <li><b>category</b>: objeto anidado con {@code id} y {@code category}, o {@code null} si no existe.</li>
    *   <li><b>expirationDate</b>: fecha de expiración en formato fecha/hora.</li>
    *   <li><b>daysExpired</b>: número de días de diferencia entre la fecha de expiración y la fecha actual (negativo si aún no venció).</li>
    *   <li><b>components</b>: lista de componentes serializada usando {@link ComponentCli2JsonSerializer}.</li>
    * </ul>
    * </p>
    *
    * @param value    Objeto {@link ProductCli2} a serializar.
    * @param gen      Generador JSON utilizado para escribir la salida.
    * @param provider Proveedor de serialización (no utilizado en esta implementación).
    * @throws IOException Si ocurre un error durante la escritura del JSON.
    */
	@Override
	public void serialize(ProductCli2 value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeNumberField("id", value.getId());
		gen.writeStringField("product", value.getProduct());
		gen.writeBooleanField("stock", value.isStock());
		gen.writeNumberField("price", value.getPrice());

        /*
        * {
        * category:null
        * }
        */
        // Serialización de categoría (objeto o null)
		if (value.getCategory() != null) {
			gen.writeObjectFieldStart("category");
			gen.writeNumberField("id", value.getCategory().getId());
			gen.writeStringField("category", value.getCategory().getCategory());
			gen.writeEndObject();
		} else {
			gen.writeNullField("category");
		}

        // Fecha de expiración y días de diferencia
		gen.writeObjectField("expirationDate", value.getExpirationDate());

		gen.writeNumberField("daysExpired", DAYS.between(Instant.ofEpochMilli(value.getExpirationDate().getTime())
				.atZone(ZoneId.systemDefault()).toLocalDateTime(), LocalDateTime.now()));

        // Serialización de la lista de componentes usando ComponentCli2JsonSerializer
		String componentsStr = JsonUtiles
				.getObjectMapper(ComponentCli2.class, new ComponentCli2JsonSerializer(ComponentCli2.class), null)
				.writeValueAsString(value.getComponents());
		gen.writeFieldName("components");
		gen.writeRawValue(componentsStr);

		gen.writeEndObject();

	}

}