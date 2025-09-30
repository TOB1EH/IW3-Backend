package ar.edu.iua.iw3.integration.cli2.model.controllers;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import ar.edu.iua.iw3.controllers.BaseRestController;
import ar.edu.iua.iw3.controllers.Constants;
import ar.edu.iua.iw3.integration.cli1.model.ProductCli1;
import ar.edu.iua.iw3.integration.cli2.model.ProductCli2;
import ar.edu.iua.iw3.integration.cli2.model.ProductCli2SlimV1JsonSerializer;
import ar.edu.iua.iw3.integration.cli2.model.business.IProductCli2Business;
import ar.edu.iua.iw3.model.business.BusinessException;
import ar.edu.iua.iw3.model.business.EmptyNameException;
import ar.edu.iua.iw3.model.business.FoundException;
import ar.edu.iua.iw3.util.IStandardResponseBusiness;
import ar.edu.iua.iw3.util.JsonUtiles;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST para la integración con CLI2.
 * <p>
 * Expone endpoints relacionados con los productos provenientes de este sistema,
 * en particular la consulta de productos vencidos.
 * </p>
 */
@RestController
@RequestMapping(Constants.URL_INTEGRATION_CLI2 + "/products")
@Slf4j
@Profile("cli2")
public class ProductCli2RestController extends BaseRestController {

    /**
     * Componente de negocio que maneja la lógica de productos de CLI2.
     */
	@Autowired
	private IProductCli2Business productBusiness;

    /**
     * Componente encargado de generar respuestas estandarizadas en caso de errores.
     */
	@Autowired
	private IStandardResponseBusiness response;
	// http://localhost:8080/api/v1/integration/cli2/products/list-expired?since=2025-09-15 18:00:00


    /**
     * Obtiene la lista de productos de CLI2 vencidos a partir de una fecha dada.
     * <p>
     * Si no se especifica el parámetro {@code since}, o si se envía el valor por
     * defecto {@code 1970-01-01 00:00:00}, se utilizará la fecha y hora actuales
     * como referencia.
     * </p>
     *
     * <p><b>Ejemplo de uso:</b></p>
     * {@code GET http://localhost:8080/api/v1/integration/cli2/products/list-expired?since=2025-09-15 18:00:00}
     *
     * @param since Fecha límite para considerar vencidos los productos.
     *              Debe enviarse en formato {@code yyyy-MM-dd HH:mm:ss}.
     * @return Un {@link ResponseEntity} con la lista de productos vencidos (HTTP 200 OK),
     *         o un error interno (HTTP 500) si ocurre una {@link BusinessException}.
     */
	@GetMapping(value = "/list-expired", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> listExpired(
			@RequestParam(name = "since", required = false, defaultValue = "1970-01-01 00:00:00") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date since,
            @RequestParam(name = "slim", required = false, defaultValue = "v0") String slimVersion) {
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(since);
            
            // Si la fecha es la de default (1970), se toma la fecha actual
			if (c.get(Calendar.YEAR) == 1970) {
				since = new Date();
			}
            StdSerializer<ProductCli2> ser = null;
            // Llama a la instancia del serializador v1
			if (slimVersion.equalsIgnoreCase("v1")) {
                ser = new ProductCli2SlimV1JsonSerializer(ProductCli2.class, false);
			}else {
                // Si no se utiliza la version 1 del serializador Slim => no serializa nada
				return new ResponseEntity<>(productBusiness.listExpired(since), HttpStatus.OK);
			}
            // Si el serializador v1 fue llamado => serializa el objeto a JSON para pasarle al Cliente
			String result = JsonUtiles.getObjectMapper(ProductCli2.class, ser, null)
					.writeValueAsString(productBusiness.listExpired(since));

			
			log.debug(since.toString());
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (BusinessException | JsonProcessingException e) {
			return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

    /**
     * Agrega un nuevo producto CLI1 a partir de un mensaje externo en formato JSON.
     * <p>
     * Responde a solicitudes HTTP POST en la ruta
     * <code>/integration/cli1/products/b2b</code>.
     * <br>
     * Se utiliza principalmente para integraciones B2B, donde el cuerpo de la
     * petición se recibe como {@link String} y se procesa internamente.
     * </p>
     *
     * @param httpEntity Entidad HTTP que contiene el JSON con los datos del producto
     *                   en su cuerpo.
     * @return {@link ResponseEntity} con:
     *         - {@link HttpStatus#CREATED} si se crea correctamente (incluyendo la ubicación en el header),
     *         - {@link HttpStatus#FOUND} si ya existe un producto con el mismo código,
     *         - {@link HttpStatus#INTERNAL_SERVER_ERROR} si ocurre un problema de negocio.
     */
	@PostMapping(value = "/b2b")
	public ResponseEntity<?> addExternal(HttpEntity<String> httpEntity) {
		try {
			ProductCli2 response = productBusiness.addExternal(httpEntity.getBody());
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("location", Constants.URL_INTEGRATION_CLI2 + "/products/" + response.getProduct());
			return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
		} catch (BusinessException e) {
			return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (FoundException e) {
			return new ResponseEntity<>(response.build(HttpStatus.FOUND, e, e.getMessage()), HttpStatus.FOUND);
		} catch(EmptyNameException e) {
			/**
			 * Esto comunica claramente al cliente que el problema está en los datos enviados, no en el servidor.
			 * La solicitud del cliente está mal formada
			 * Se decidió crear una excepción personalizada para indicar explícitamente al programador cual fue el problema
			 * que lanzó la misma. Además, se indica en la request 400 BAD REQUEST con un mensaje claro que dice:
			 * El nombre del producto es obligatorio.
			 */
			return new ResponseEntity<>(response.build(HttpStatus.BAD_REQUEST, e, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
	}

}