package ar.edu.iua.iw3.integration.cli1.model.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.iua.iw3.controllers.BaseRestController;
import ar.edu.iua.iw3.controllers.Constants;
import ar.edu.iua.iw3.integration.cli1.model.ProductCli1;
import ar.edu.iua.iw3.integration.cli1.model.business.IProductCli1Business;
import ar.edu.iua.iw3.model.business.BusinessException;
import ar.edu.iua.iw3.model.business.EmptyNameException;
import ar.edu.iua.iw3.model.business.FoundException;
import ar.edu.iua.iw3.model.business.NotFoundException;
import ar.edu.iua.iw3.util.IStandardResponseBusiness;

/**
 * Controlador REST para la gestión de productos del sistema de integración CLI1.
 * <p>
 * Expone endpoints para listar, obtener y agregar productos específicos de CLI1.
 * Las rutas están definidas en {@link Constants#URL_INTEGRATION_CLI1}.
 * </p>
 */
@RestController
@RequestMapping(Constants.URL_INTEGRATION_CLI1 + "/products")
@Profile("cli1")
public class ProductCli1RestController extends BaseRestController {
	/**
     * Componente de negocio encargado de la lógica de productos CLI1.
     */
	@Autowired
	private IProductCli1Business productBusiness;

	/**
     * Componente encargado de construir respuestas estándar de error.
     */
	@Autowired
	private IStandardResponseBusiness response;

	/**
     * Obtiene la lista de todos los productos de CLI1.
     * <p>
     * Responde a solicitudes HTTP GET en la ruta <code>/integration/cli1/products</code>.
     * </p>
     *
     * @return {@link ResponseEntity} con:
     *         - Lista de productos en formato JSON y estado {@link HttpStatus#OK}, o
     *         - Mensaje de error y estado {@link HttpStatus#INTERNAL_SERVER_ERROR} si ocurre un problema de negocio.
     */
	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> list() {
		try {
			return new ResponseEntity<>(productBusiness.list(), HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
     * Obtiene un producto CLI1 según su código único.
     * <p>
     * Responde a solicitudes HTTP GET en la ruta
     * <code>/integration/cli1/products/{codCli1}</code>.
     * </p>
     *
     * @param codCli1 Código identificador único del producto en CLI1.
     * @return {@link ResponseEntity} con:
     *         - El producto encontrado (HTTP 200 OK),
     *         - Mensaje de error si no existe (HTTP 404 NOT FOUND),
     *         - Mensaje de error si ocurre un problema de negocio (HTTP 500 INTERNAL_SERVER_ERROR).
     */
	@GetMapping(value = "/{codCli1}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> loadByCode(@PathVariable("codCli1") String codCli1) {
		try {
			return new ResponseEntity<>(productBusiness.load(codCli1), HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<>(response.build(HttpStatus.NOT_FOUND, e, e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}

	/**
     * Agrega un nuevo producto CLI1.
     * <p>
     * Responde a solicitudes HTTP POST en la ruta
     * <code>/integration/cli1/products</code>.
     * Devuelve la ubicación del recurso creado en el encabezado <b>Location</b>.
     * </p>
     *
     * @param product Objeto {@link ProductCli1} con los datos del producto a registrar.
     * @return {@link ResponseEntity} con:
     *         - {@link HttpStatus#CREATED} si se crea correctamente (incluyendo la ubicación en el header),
     *         - {@link HttpStatus#FOUND} si ya existe un producto con el mismo código,
     *         - {@link HttpStatus#INTERNAL_SERVER_ERROR} si ocurre un problema de negocio.
     */
	@PostMapping(value = "")
	public ResponseEntity<?> add(@RequestBody ProductCli1 product) {
		try {
			ProductCli1 response = productBusiness.add(product);
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("location", Constants.URL_INTEGRATION_CLI1 + "/products/" + response.getCodCli1());
			return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
		} catch (BusinessException e) {
			return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (FoundException e) {
			return new ResponseEntity<>(response.build(HttpStatus.FOUND, e, e.getMessage()), HttpStatus.FOUND);
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
			ProductCli1 response = productBusiness.addExternal(httpEntity.getBody());
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("location", Constants.URL_INTEGRATION_CLI1 + "/products/" + response.getCodCli1());
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