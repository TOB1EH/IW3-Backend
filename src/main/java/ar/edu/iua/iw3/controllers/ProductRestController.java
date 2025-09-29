package ar.edu.iua.iw3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ar.edu.iua.iw3.model.Product;
import ar.edu.iua.iw3.model.business.BusinessException;
import ar.edu.iua.iw3.model.business.FoundException;
import ar.edu.iua.iw3.model.business.IProductBusiness;
import ar.edu.iua.iw3.model.business.NotFoundException;
import ar.edu.iua.iw3.util.IStandartResponseBusiness;


/**
 * Controlador REST para operaciones relacionadas con productos en la aplicación IW3.
 *
 * Esta clase maneja las solicitudes HTTP para gestionar productos, como listar
 * productos disponibles. Utiliza Spring MVC para mapear endpoints REST y
 * delega la lógica de negocio a la capa de servicios.
 *
 * Propósito: Proporcionar una interfaz RESTful para interactuar con productos,
 * permitiendo a clientes externos (como aplicaciones web o móviles) acceder
 * a la funcionalidad de productos de manera estandarizada.
 *
 * Cómo funciona: El controlador está anotado con @RestController, lo que indica
 * que es un controlador especializado en REST que automáticamente serializa
 * las respuestas a JSON. Usa @RequestMapping para mapear todas las rutas
 * relacionadas con productos. Inyecta dependencias como IProductBusiness para
 * la lógica de negocio y IStandartResponseBusiness para construir respuestas
 * de error estandarizadas. Los métodos manejan excepciones de negocio y
 * devuelven respuestas HTTP apropiadas.
 *
 * Cómo usar: Enviar solicitudes HTTP a las rutas mapeadas, por ejemplo:
 * GET /api/v1/products para obtener la lista de productos. El controlador
 * procesa la solicitud, llama a la lógica de negocio, y devuelve una respuesta
 * JSON con los datos o un mensaje de error si ocurre una excepción.
 */
@RestController
@RequestMapping(Constants.URL_PRODUCTS)
public class ProductRestController {

    @Autowired
    private IProductBusiness productBusiness;

    @Autowired
    private IStandartResponseBusiness response;

    /**
     * Endpoint para obtener la lista de todos los productos.
     *
     * Este método maneja solicitudes GET a la ruta base de productos
     * (/api/v1/products) y devuelve una lista de productos en formato JSON.
     *
     * Propósito: Permitir a los clientes recuperar todos los productos
     * disponibles en el sistema.
     *
     * Cómo funciona: Llama al método list() de la interfaz IProductBusiness
     * para obtener la lista de productos. Si la operación es exitosa, devuelve
     * la lista con código HTTP 200 (OK). Si ocurre una BusinessException,
     * construye una respuesta de error estandarizada usando IStandartResponseBusiness
     * y devuelve código HTTP 500 (Internal Server Error).
     *
     * Cómo usar: Enviar una solicitud GET a /api/v1/products. No requiere
     * parámetros. Respuesta exitosa: JSON array de objetos Product.
     * Respuesta de error: JSON con mensaje de error.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> list() {
        try {
            return new ResponseEntity<>(productBusiness.list(), HttpStatus.OK);
        } catch (BusinessException e) {

            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para agregar un nuevo producto.
     *
     * Este método maneja solicitudes POST a la ruta base de productos
     * (/api/v1/products) y crea un nuevo producto con la información proporcionada.
     *
     * Propósito: Permitir a los clientes crear nuevos productos en el sistema.
     *
     * Cómo funciona: Recibe un objeto Product en el cuerpo de la solicitud.
     * Llama al método add() de IProductBusiness para guardar el producto.
     * Si tiene éxito, devuelve una cabecera 'location' con la URL del nuevo
     * producto y un código HTTP 201 (Created). Si ocurre una BusinessException
     * o FoundException, devuelve una respuesta de error estandarizada.
     *
     * Cómo usar: Enviar una solicitud POST a /api/v1/products con un JSON
     * que represente el nuevo producto en el cuerpo de la solicitud.
     */
	@PostMapping(value = "")
	public ResponseEntity<?> add(@RequestBody Product product) {
		try {
			Product response = productBusiness.add(product);
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("location", Constants.URL_PRODUCTS + "/" + response.getId());
			return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
		} catch (BusinessException e) {
			return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (FoundException e) {
			return new ResponseEntity<>(response.build(HttpStatus.FOUND, e, e.getMessage()), HttpStatus.FOUND);
		}
	}

	/**
	 * Endpoint para cargar un producto específico por su ID.
	 *
	 * Este método maneja solicitudes GET a la ruta /api/v1/products/{id}
	 * y devuelve los detalles de un producto basado en su identificador único.
	 *
	 * Propósito: Permitir a los clientes recuperar información detallada
	 * de un producto específico para operaciones de consulta individual.
	 *
	 * Cómo funciona: El parámetro {id} en la ruta se mapea al parámetro long id
	 * del método mediante @PathVariable. Llama al método load(id) de IProductBusiness
	 * para obtener el producto. Si tiene éxito, devuelve el producto en JSON
	 * con código HTTP 200 (OK). Si ocurre NotFoundException, devuelve 404
	 * (Not Found). Para BusinessException, devuelve 500 (Internal Server Error).
	 *
	 * Cómo usar: Enviar una solicitud GET a /api/v1/products/{id}, reemplazando
	 * {id} con el número identificador del producto. Ejemplo: GET /api/v1/products/1
	 * devuelve el producto con ID 1 si existe.
	 */
	// Le suma el /id a la URI que ya venimos trabajando: URL_PRODUCTS
    // se encuentra entre llaves porque en el metodo de abajo Spring gracias al 'PathVariable' que
    // recibe el metodo reemplazara dinamicamente en la peticion por ese id de tipo long. Se puede ser
    // mas especifico usando por ejemplo:
    //
    //      @GetMapping(value = "/{idA}")
    //      public ResponseEntity<?> load(@PathVariable(name = "idA") long id) ..
    //
    // Lo consume de la siguiente manera por ejemplo http://localhost:8080/api/v1/products/1
    @GetMapping(value = "/{id}")
	public ResponseEntity<?> load(@PathVariable long id) {
		try {
			return new ResponseEntity<>(productBusiness.load(id), HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<>(response.build(HttpStatus.NOT_FOUND, e, e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Endpoint para cargar un producto específico por su nombre.
	 *
	 * Este método maneja solicitudes GET a la ruta /api/v1/products/by-name/{product}
	 * y devuelve los detalles de un producto basado en su nombre único.
	 *
	 * Propósito: Facilitar la búsqueda de productos por nombre, útil para
	 * interfaces de usuario que permiten búsqueda por texto o validaciones
	 * de existencia antes de operaciones.
	 *
	 * Cómo funciona: El parámetro {product} en la ruta se mapea al parámetro
	 * String product del método mediante @PathVariable. Llama al método load(product)
	 * de IProductBusiness para buscar por nombre. Si tiene éxito, devuelve el
	 * producto en JSON con código HTTP 200 (OK). Si ocurre NotFoundException,
	 * devuelve 404 (Not Found). Para BusinessException, devuelve 500 (Internal Server Error).
	 *
	 * Cómo usar: Enviar una solicitud GET a /api/v1/products/by-name/{product},
	 * reemplazando {product} con el nombre del producto. Ejemplo: GET /api/v1/products/by-name/Arroz
	 * devuelve el producto llamado "Arroz" si existe.
	 */
	// http://localhost:8080/api/v1/products/by-name/Arroz
	@GetMapping(value = "/by-name/{product}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> load(@PathVariable String product) {
		try {
			return new ResponseEntity<>(productBusiness.load(product), HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<>(response.build(HttpStatus.NOT_FOUND, e, e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Endpoint para actualizar un producto existente.
	 *
	 * Este método maneja solicitudes PUT a la ruta base de productos
	 * (/api/v1/products) y actualiza los datos de un producto existente
	 * con la información proporcionada en el cuerpo de la solicitud.
	 *
	 * Propósito: Permitir a los clientes modificar productos existentes
	 * en el sistema, manteniendo el ID del producto.
	 *
	 * Cómo funciona: Recibe un objeto Product con ID existente y datos
	 * actualizados. Llama al método update() de IProductBusiness para
	 * aplicar los cambios. Si tiene éxito, devuelve código HTTP 200 (OK).
	 * Si ocurre NotFoundException, devuelve 404 (Not Found). Si ocurre
	 * FoundException (por conflicto de unicidad), devuelve 302 (Found).
	 * Para BusinessException, devuelve 500 (Internal Server Error).
	 *
	 * Cómo usar: Enviar una solicitud PUT a /api/v1/products con un JSON
	 * que represente el producto actualizado en el cuerpo. El producto
	 * debe incluir el ID existente.
	 */
	@PutMapping(value = "")
	public ResponseEntity<?> update(@RequestBody Product product) {
		try {
			productBusiness.update(product);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<>(response.build(HttpStatus.NOT_FOUND, e, e.getMessage()), HttpStatus.NOT_FOUND);
		} catch (FoundException e) {
			return new ResponseEntity<>(response.build(HttpStatus.FOUND, e, e.getMessage()), HttpStatus.FOUND);
		}
	}

	/**
	 * Endpoint para eliminar un producto por su ID.
	 *
	 * Este método maneja solicitudes DELETE a la ruta /api/v1/products/{id}
	 * y elimina el producto correspondiente del sistema.
	 *
	 * Propósito: Permitir a los clientes remover productos del sistema
	 * de manera permanente, útil para eliminar productos obsoletos o
	 * incorrectos.
	 *
	 * Cómo funciona: Recibe el ID del producto como parámetro de ruta.
	 * Llama al método delete() de IProductBusiness para eliminar el producto
	 * de la base de datos. Si tiene éxito, devuelve código HTTP 200 (OK).
	 * Si ocurre NotFoundException (producto no encontrado), devuelve 404
	 * (Not Found). Para BusinessException, devuelve 500 (Internal Server Error).
	 *
	 * Cómo usar: Enviar una solicitud DELETE a /api/v1/products/{id}, donde
	 * {id} es el identificador único del producto a eliminar. No requiere
	 * cuerpo en la solicitud.
	 */
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable long id) {
		try {
			productBusiness.delete(id);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<>(response.build(HttpStatus.NOT_FOUND, e, e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}
}
