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
import ar.edu.iua.iw3.model.business.FoundException;
import ar.edu.iua.iw3.model.business.NotFoundException;
import ar.edu.iua.iw3.util.IStandartResponseBusiness;

/**
 * Controlador REST para operaciones relacionadas con productos CLI1.
 *
 * Esta clase maneja las solicitudes HTTP para gestionar productos específicos
 * del perfil CLI1, incluyendo operaciones de listado, carga por código,
 * agregado y agregado desde fuentes externas. Extiende BaseRestController
 * para heredar funcionalidades comunes.
 *
 * Propósito: Proporcionar una interfaz RESTful para interactuar con productos
 * CLI1, permitiendo a sistemas externos acceder a funcionalidades específicas
 * del perfil CLI1 de manera estandarizada.
 *
 * Cómo funciona: El controlador está anotado con @RestController y @Profile("cli1"),
 * lo que significa que solo se activa cuando el perfil CLI1 está activo. Usa
 * @RequestMapping para mapear rutas bajo /integration/cli1/products. Inyecta
 * dependencias como IProductCli1Business para lógica de negocio y IStandartResponseBusiness
 * para construir respuestas de error. Los métodos manejan excepciones y devuelven
 * respuestas HTTP apropiadas.
 *
 * Cómo usar: Enviar solicitudes HTTP a las rutas mapeadas cuando el perfil CLI1
 * está activo, por ejemplo: GET /integration/cli1/products para listar productos.
 * El controlador procesa la solicitud, llama a la lógica de negocio, y devuelve
 * una respuesta JSON con los datos o un mensaje de error si ocurre una excepción.
 */
@RestController
@RequestMapping(Constants.URL_INTEGRATION_CLI1 + "/products")
@Profile("cli1")
public class ProductCli1RestController extends BaseRestController {

	/**
	 * Servicio de negocio para operaciones relacionadas con productos CLI1.
	 *
	 * Inyectado para delegar la lógica de negocio específica de productos CLI1,
	 * permitiendo operaciones como carga por código y agregado desde JSON externo.
	 */
	@Autowired
	private IProductCli1Business productBusiness;

	/**
	 * Servicio para construir respuestas de error estandarizadas.
	 *
	 * Inyectado para generar respuestas JSON consistentes en caso de errores,
	 * facilitando el manejo uniforme de excepciones en el controlador.
	 */
	@Autowired
	private IStandartResponseBusiness response;

	/**
	 * Endpoint para obtener la lista de todos los productos CLI1.
	 *
	 * Este método maneja solicitudes GET a la ruta base de productos CLI1
	 * (/integration/cli1/products) y devuelve una lista de productos en formato JSON.
	 *
	 * Propósito: Permitir a los clientes recuperar todos los productos CLI1
	 * disponibles en el sistema.
	 *
	 * Cómo funciona: Llama al método list() de IProductCli1Business para obtener
	 * la lista de productos. Si tiene éxito, devuelve la lista con código HTTP
	 * 200 (OK). Si ocurre BusinessException, devuelve 500 (Internal Server Error).
	 *
	 * Cómo usar: Enviar una solicitud GET a /integration/cli1/products.
	 * No requiere parámetros. Respuesta: JSON array de objetos ProductCli1.
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
	 * Endpoint para cargar un producto CLI1 específico por su código.
	 *
	 * Este método maneja solicitudes GET a la ruta /integration/cli1/products/{codCli1}
	 * y devuelve los detalles de un producto basado en su código único.
	 *
	 * Propósito: Permitir a los clientes recuperar información detallada
	 * de un producto CLI1 específico para operaciones de consulta individual.
	 *
	 * Cómo funciona: El parámetro {codCli1} se mapea al parámetro String codCli1
	 * del método. Llama al método load(codCli1) de IProductCli1Business. Si tiene
	 * éxito, devuelve el producto en JSON con código HTTP 200 (OK). Si NotFoundException,
	 * devuelve 404 (Not Found). Para BusinessException, devuelve 500.
	 *
	 * Cómo usar: Enviar GET a /integration/cli1/products/{codCli1}, reemplazando
	 * {codCli1} con el código único del producto CLI1.
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
	 * Endpoint para agregar un nuevo producto CLI1.
	 *
	 * Este método maneja solicitudes POST a la ruta base de productos CLI1
	 * (/integration/cli1/products) y crea un nuevo producto con la información proporcionada.
	 *
	 * Propósito: Permitir a los clientes crear nuevos productos CLI1 en el sistema.
	 *
	 * Cómo funciona: Recibe un objeto ProductCli1 en el cuerpo. Llama al método
	 * add() de IProductCli1Business. Si tiene éxito, devuelve cabecera 'location'
	 * con la URL del nuevo producto y código HTTP 201 (Created). Si FoundException,
	 * devuelve 302 (Found). Para BusinessException, devuelve 500.
	 *
	 * Cómo usar: Enviar POST a /integration/cli1/products con JSON de un
	 * ProductCli1 en el cuerpo.
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
	 * Endpoint para agregar un producto CLI1 desde una fuente externa (B2B).
	 *
	 * Este método maneja solicitudes POST a la ruta /integration/cli1/products/b2b
	 * y crea un producto CLI1 a partir de datos JSON enviados en el cuerpo de la solicitud.
	 *
	 * Propósito: Facilitar la integración B2B (Business to Business) permitiendo
	 * que sistemas externos envíen productos CLI1 en formato JSON para su creación.
	 *
	 * Cómo funciona: Recibe un HttpEntity<String> con el JSON en el cuerpo.
	 * Llama al método addExternal() de IProductCli1Business, que parsea el JSON
	 * y crea el producto. Si tiene éxito, devuelve cabecera 'location' con la
	 * URL del nuevo producto y código HTTP 201 (Created). Maneja excepciones
	 * de la misma manera que el método add().
	 *
	 * Cómo usar: Enviar POST a /integration/cli1/products/b2b con el JSON
	 * del producto en el cuerpo de la solicitud HTTP.
	 */
	@PostMapping(value = "/b2b") // Business To Business
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
		}
	}
}
