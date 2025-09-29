package ar.edu.iua.iw3.integration.cli1.model.business;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.iua.iw3.integration.cli1.model.ProductCli1;
import ar.edu.iua.iw3.integration.cli1.model.persistence.ProductCli1Repository;
import ar.edu.iua.iw3.model.business.BusinessException;
import ar.edu.iua.iw3.model.business.FoundException;
import ar.edu.iua.iw3.model.business.IProductBusiness;
import ar.edu.iua.iw3.model.business.NotFoundException;
import lombok.extern.slf4j.Slf4j;


/**
 * Implementación de la lógica de negocio para la gestión de productos
 * provenientes del sistema de integración CLI1.
 * <p>
 * Esta clase implementa la interfaz {@link IProductCli1Business} y se encarga
 * de coordinar las operaciones de acceso y manipulación de {@link ProductCli1}
 * a través del repositorio {@link ProductCli1Repository}.
 * </p>
 * <p>
 * Incluye validaciones de existencia, control de duplicados y manejo centralizado
 * de excepciones de negocio.
 * </p>
 */
@Service
@Slf4j
public class ProductCli1Business implements IProductCli1Business {

    /**
     * Repositorio para el acceso a los datos de productos CLI1.
     */
	// Si la clase que quiero instanciar no existe, le pongo null (con el required = false)
	@Autowired(required = false)
	private ProductCli1Repository productDAO;

    /**
     * Carga un producto CLI1 a partir de su código identificador único.
     *
     * @param codCli1 Código único del producto en el sistema CLI1.
     * @return El producto correspondiente al código solicitado.
     * @throws NotFoundException  Si no se encuentra ningún producto con el código dado.
     * @throws BusinessException Si ocurre un error al acceder a la base de datos.
     */
	@Override
	public ProductCli1 load(String codCli1) throws NotFoundException, BusinessException {
		Optional<ProductCli1> r;
		try {
			r = productDAO.findOneByCodCli1(codCli1);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw BusinessException.builder().ex(e).build();
		}
		if (r.isEmpty()) {
			throw NotFoundException.builder().message("No se encuentra el Producto codCli1=" + codCli1).build();
		}
		return r.get();
	}

    /**
     * Obtiene la lista de todos los productos almacenados en CLI1.
     *
     * @return Una lista con todos los productos disponibles.
     * @throws BusinessException Si ocurre un error durante el acceso a los datos.
     */
	@Override
	public List<ProductCli1> list() throws BusinessException {
		try {
			return productDAO.findAll();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw BusinessException.builder().ex(e).build();
		}
	}

    /**
     * Componente de negocio de productos base, utilizado para verificar
     * la existencia de productos en el sistema principal antes de
     * registrar uno nuevo en CLI1.
     */
	@Autowired
	private IProductBusiness productBaseBusiness;

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
	public ProductCli1 add(ProductCli1 product) throws FoundException, BusinessException {
        // Verifica si existe en el sistema principal
		try {
			productBaseBusiness.load(product.getId());
			throw FoundException.builder().message("Se encontró el Producto id=" + product.getId()).build();
		} catch (NotFoundException e) {
            // Ignorado: significa que no existe en el sistema base y puede continuar
		}

        // Verifica duplicados en CLI1 por código
		if (productDAO.findOneByCodCli1(product.getCodCli1()).isPresent()) {
			throw FoundException.builder().message("Se encontró el Producto código=" + product.getCodCli1()).build();
		}

        // Guarda el producto
		try {
			return productDAO.save(product);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw BusinessException.builder().ex(e).build();
		}
	}

}