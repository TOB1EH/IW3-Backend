package ar.edu.iua.iw3.integration.cli1.model.business;

import java.util.List;

import ar.edu.iua.iw3.integration.cli1.model.ProductCli1;
import ar.edu.iua.iw3.model.business.BusinessException;
import ar.edu.iua.iw3.model.business.FoundException;
import ar.edu.iua.iw3.model.business.NotFoundException;

/**
 * Interfaz de la capa de negocio para la gestión de productos en la integración con CLI1.
 * <p>
 * Define las operaciones principales que se pueden realizar sobre los objetos
 * {@link ProductCli1}, tales como carga individual, listado y alta de nuevos productos.
 * </p>
 * <p>
 * Esta interfaz establece el contrato que deberán implementar las clases concretas
 * encargadas de la lógica de negocio vinculada a productos del sistema CLI1.
 * </p>
 */
public interface IProductCli1Business {
	/**
     * Carga un producto de CLI1 a partir de su código identificador.
     *
     * @param codCli1 Código único del producto en el sistema CLI1.
     * @return El producto correspondiente al código indicado.
     * @throws NotFoundException Si no se encuentra ningún producto con el código especificado.
     * @throws BusinessException Si ocurre un error inesperado en la capa de negocio.
     */
    public ProductCli1 load(String codCli1) throws NotFoundException, BusinessException;

	/**
     * Obtiene la lista de todos los productos disponibles en CLI1.
     *
     * @return Una lista de productos {@link ProductCli1}.
     * @throws BusinessException Si ocurre un error inesperado en la capa de negocio.
     */
	public List<ProductCli1> list() throws BusinessException;

	/**
     * Agrega un nuevo producto al sistema CLI1.
     *
     * @param product Objeto {@link ProductCli1} que representa el producto a registrar.
     * @return El producto agregado, con la información generada tras su registro.
     * @throws FoundException Si ya existe un producto con las mismas características o identificador.
     * @throws BusinessException Si ocurre un error inesperado en la capa de negocio.
     */
	public ProductCli1 add(ProductCli1 product) throws FoundException, BusinessException;
}
