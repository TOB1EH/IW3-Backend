package ar.edu.iua.iw3.integration.cli2.model.business;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.edu.iua.iw3.integration.cli1.model.ProductCli1;
import ar.edu.iua.iw3.integration.cli1.model.ProductCli1JsonDeserializer;
import ar.edu.iua.iw3.integration.cli2.model.ProductCli2;
import ar.edu.iua.iw3.integration.cli2.model.ProductCli2JsonDeserializer;
import ar.edu.iua.iw3.integration.cli2.model.ProductCli2SlimView;
import ar.edu.iua.iw3.integration.cli2.model.persistence.ProductCli2Repository;
import ar.edu.iua.iw3.model.business.BusinessException;
import ar.edu.iua.iw3.model.business.EmptyNameException;
import ar.edu.iua.iw3.model.business.FoundException;
import ar.edu.iua.iw3.model.business.ICategoryBusiness;
import ar.edu.iua.iw3.model.business.IProductBusiness;
import ar.edu.iua.iw3.model.business.NotFoundException;
import ar.edu.iua.iw3.util.JsonUtiles;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación de la capa de negocio para la gestión de productos
 * provenientes del sistema de integración CLI2.
 * <p>
 * Se encarga de aplicar la lógica relacionada con el control de
 * vencimientos de los productos.
 * </p>
 */
@Service
@Slf4j
public class ProductCli2Business implements IProductCli2Business {
     /**
     * Componente de negocio de categorías.
     * <p>
     * Se emplea durante la deserialización JSON en {@link #addExternal(String)}
     * para asociar los productos CLI1 a sus categorías.
     * </p>
     */
	@Autowired(required = false)
	private ICategoryBusiness categoryBusiness;

     /**
     * Componente de negocio de productos base, utilizado para verificar
     * la existencia de productos en el sistema principal antes de
     * registrar uno nuevo en CLI1.
     */
	@Autowired
	private IProductBusiness productBaseBusiness;

	/**
     * Repositorio JPA para el acceso a datos de productos de CLI2.
     */
	// Si la clase que quiero instanciar no existe, le pongo null (con el required = false)
	@Autowired(required = false)
	private ProductCli2Repository productDAO;

	/**
     * Obtiene la lista de productos de CLI2 cuya fecha de vencimiento
     * es anterior a la fecha especificada.
     *
     * @param date Fecha de referencia. Se consideran vencidos todos los
     *             productos con fecha de vencimiento previa a este valor.
     * @return Lista de productos {@link ProductCli2} vencidos, ordenados
     *         de más reciente a más antiguo según la fecha de vencimiento.
     * @throws BusinessException Si ocurre un error en la consulta o en
     *                           la capa de negocio.
     */
	@Override
	public List<ProductCli2> listExpired(Date date) throws BusinessException {
		try {
			return productDAO.findByExpirationDateBeforeOrderByExpirationDateDesc(date);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw BusinessException.builder().ex(e).build();
		}
	}

	/**
     * Obtiene una lista simplificada de productos de CLI2,
     * ordenados por precio de forma descendente.
     * <p>
     * A diferencia de {@link #listExpired(Date)}, este método
     * no devuelve la entidad completa {@link ProductCli2}, sino
     * una vista reducida representada por {@link ProductCli2SlimView},
     * que incluye únicamente los atributos más relevantes.
     * </p>
     *
     * @return Lista de productos en su versión reducida, ordenada por precio descendente.
     * @throws BusinessException Si ocurre un error en la consulta o en la capa de negocio.
     */
	@Override
	public List<ProductCli2SlimView> listSlim() throws BusinessException {
		try {
			return productDAO.findByOrderByPriceDesc();
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			throw BusinessException.builder().ex(e).build();
		}
	}

     /**
     * Agrega un nuevo producto en CLI1.
     * <p>
     * Antes de guardar, valida:
     * <ul>
     *   <li>Que el producto no exista en el sistema base mediante su {@code id}.</li>
     *   <li>Que no exista ya un producto en CLI1 con el mismo código {@code codCli1}.</li>
     * </ul>
     * </p>
     *
     * @param product El objeto {@link ProductCli1} a registrar.
     * @return El producto agregado y persistido en la base de datos.
     * @throws FoundException    Si el producto ya existe en el sistema base o en CLI1.
     * @throws BusinessException Si ocurre un error inesperado durante el guardado.
     */
	@Override
	public ProductCli2 add(ProductCli2 product) throws FoundException, BusinessException {
        // Verifica si existe en el sistema principal
		try {
			productBaseBusiness.load(product.getId());
			throw FoundException.builder().message("Se encontró el Producto id=" + product.getId()).build();
		} catch (NotFoundException e) {
            // Ignorado: significa que no existe en el sistema base y puede continuar
		}

        // Verifica duplicados en CLI1 por código
		if (productDAO.findOneByProduct(product.getProduct()).isPresent()) {
			throw FoundException.builder().message("Se encontró el Producto con nombre=" + product.getProduct()).build();
		}

        // Guarda el producto
		try {
			return productDAO.save(product);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw BusinessException.builder().ex(e).build();
		}
	}

     /**
     * Agrega un nuevo producto a CLI1 a partir de una representación en formato JSON.
     * <p>
     * Utiliza un {@link ObjectMapper} configurado con un deserializador
     * personalizado {@link ProductCli1JsonDeserializer} para convertir
     * la cadena JSON en un objeto {@link ProductCli1}.
     * </p>
     * <p>
     * Una vez creado el objeto, reutiliza la lógica de {@link #add(ProductCli1)}
     * para validar duplicados y registrar el producto.
     * </p>
     *
     * @param json Cadena en formato JSON que representa al producto a registrar.
     * @return El producto agregado y persistido en la base de datos.
     * @throws FoundException    Si el producto ya existe en el sistema base o en CLI2.
     * @throws BusinessException Si ocurre un error inesperado durante la deserialización o el guardado.
     */
	@Override
	public ProductCli2 addExternal(String json) throws FoundException, BusinessException, EmptyNameException {
		ObjectMapper mapper = JsonUtiles.getObjectMapper(ProductCli2.class,
				new ProductCli2JsonDeserializer(ProductCli2.class, categoryBusiness),null);
		ProductCli2 product = null;
		try {
			product = mapper.readValue(json, ProductCli2.class);

            // Se obtiene el nombre del producto del objeto JSON recibido
			String product_name = product.getProduct();

            // Si el nombre del producto viene vacío o es nulo => se lanza la excepcion creada hacia el endpoint b2b
            if (product_name == null || product_name.isBlank()) {
               throw EmptyNameException.builder()
                   .message("El nombre del producto es obligatorio")
                   .build();
            }
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
			throw BusinessException.builder().ex(e).build();
		} 

        // Aqui se guarda en la base de datos el producto deserializado
		return add(product);

	}

     /**
     * Obtiene una lista de productos de CLI2 cuyo precio se encuentra dentro
     * de un rango específico.
     * <p>
     * Este método permite filtrar los productos según su valor monetario,
     * considerando únicamente aquellos cuyo precio sea mayor o igual al
     * valor mínimo establecido ({@code startPrice}) y menor o igual al
     * valor máximo establecido ({@code endPrice}).
     * </p>
     *
     * @param startPrice Precio mínimo del rango (inclusive).
     * @param endPrice   Precio máximo del rango (inclusive).
     * @return Lista de productos {@link ProductCli2} cuyo precio se encuentra
     *         dentro del rango especificado.
     * @throws BusinessException Si ocurre un error en la consulta o en la capa de negocio.
     */
     public List<ProductCli2> listByPrice(Double startPrice, Double endPrice) throws BusinessException {
          try {
               return productDAO.getProductsByPrice(startPrice, endPrice);
          } catch(Exception e) {
               log.error(e.getMessage(), e);
			throw BusinessException.builder().ex(e).build();
          }
     }


}