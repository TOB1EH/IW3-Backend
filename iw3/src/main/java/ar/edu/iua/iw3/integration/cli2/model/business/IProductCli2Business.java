package ar.edu.iua.iw3.integration.cli2.model.business;

import java.util.Date;
import java.util.List;

import ar.edu.iua.iw3.integration.cli2.model.ProductCli2;
import ar.edu.iua.iw3.integration.cli2.model.ProductCli2SlimView;
import ar.edu.iua.iw3.model.business.BusinessException;

/**
 * Interfaz de la capa de negocio para la gestión de productos
 * provenientes del sistema de integración CLI2.
 * <p>
 * Define las operaciones principales relacionadas con los objetos
 * {@link ProductCli2}, en este caso enfocadas en el control de
 * productos vencidos.
 * </p>
 */
public interface IProductCli2Business {
	/**
     * Obtiene la lista de productos de CLI2 cuya fecha de vencimiento
     * es anterior a la fecha especificada.
     *
     * @param date Fecha de referencia. Todos los productos con fecha de
     *             vencimiento anterior a este valor serán considerados vencidos.
     * @return Lista de productos {@link ProductCli2} que cumplen la condición de vencimiento.
     * @throws BusinessException Si ocurre un error en la capa de negocio o en el acceso a datos.
     */
	public List<ProductCli2> listExpired(Date date) throws BusinessException;

     /**
     * Obtiene una lista reducida (vista simplificada) de productos de CLI2.
     * <p>
     * A diferencia de {@link #listExpired(Date)}, este método no devuelve el objeto
     * completo {@link ProductCli2}, sino una proyección simplificada mediante
     * {@link ProductCli2SlimView}, que contiene únicamente los atributos más relevantes.
     * </p>
     *
     * @return Lista de productos en su versión reducida {@link ProductCli2SlimView}.
     * @throws BusinessException Si ocurre un error en la capa de negocio o en el acceso a datos.
     */
     public List<ProductCli2SlimView> listSlim() throws BusinessException;
}