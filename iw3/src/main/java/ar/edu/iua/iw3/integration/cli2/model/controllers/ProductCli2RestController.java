package ar.edu.iua.iw3.integration.cli2.model.controllers;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.iua.iw3.controllers.BaseRestController;
import ar.edu.iua.iw3.controllers.Constants;
import ar.edu.iua.iw3.integration.cli2.model.business.IProductCli2Business;
import ar.edu.iua.iw3.model.business.BusinessException;
import ar.edu.iua.iw3.util.IStandardResponseBusiness;
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
			@RequestParam(name = "since", required = false, defaultValue = "1970-01-01 00:00:00") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date since) {
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(since);
            
            // Si la fecha es la de default (1970), se toma la fecha actual
			if (c.get(Calendar.YEAR) == 1970) {
				since = new Date();
			}
			log.debug(since.toString());
			return new ResponseEntity<>(productBusiness.listExpired(since), HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}