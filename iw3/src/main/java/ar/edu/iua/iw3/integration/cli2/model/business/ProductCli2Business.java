package ar.edu.iua.iw3.integration.cli2.model.business;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.iua.iw3.integration.cli2.model.ProductCli2;
import ar.edu.iua.iw3.integration.cli2.model.ProductCli2SlimView;
import ar.edu.iua.iw3.integration.cli2.model.persistence.ProductCli2Repository;
import ar.edu.iua.iw3.model.business.BusinessException;
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

}