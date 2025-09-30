package ar.edu.iua.iw3.integration.cli2.model.persistence;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.iua.iw3.integration.cli1.model.ProductCli1;
import ar.edu.iua.iw3.integration.cli2.model.ProductCli2;
import ar.edu.iua.iw3.integration.cli2.model.ProductCli2SlimView;

/**
 * Repositorio JPA para el acceso a datos de productos provenientes del sistema CLI2.
 * <p>
 * Extiende {@link JpaRepository} para heredar operaciones CRUD básicas,
 * y define consultas personalizadas relacionadas con las fechas de vencimiento.
 * </p>
 */
@Repository
public interface ProductCli2Repository extends JpaRepository<ProductCli2, Long> {
	/**
     * Busca todos los productos cuya fecha de vencimiento sea anterior a la fecha especificada.
     * <p>
     * Los resultados se ordenan de manera descendente según la fecha de vencimiento,
     * mostrando primero los productos más recientes dentro de los vencidos.
     * </p>
     *
     * @param expirationDate Fecha de referencia.
     *                       Se consideran vencidos los productos con fecha anterior a este valor.
     * @return Lista de productos vencidos ordenados de forma descendente por fecha de vencimiento.
     */
	public List<ProductCli2> findByExpirationDateBeforeOrderByExpirationDateDesc(Date expirationDate);

     /**
     * Obtiene una vista simplificada de todos los productos de CLI2,
     * ordenados por precio de forma descendente.
     * <p>
     * Este método devuelve proyecciones de tipo {@link ProductCli2SlimView},
     * que contienen solo los atributos más relevantes del producto,
     * optimizando el acceso cuando no se necesita la entidad completa.
     * </p>
     *
     * @return Lista de productos en su versión reducida {@link ProductCli2SlimView},
     *         ordenada por precio descendente.
     */
     public List<ProductCli2SlimView> findByOrderByPriceDesc();

     /**
     * Busca un producto en CLI2 por su código único.
     *
     * @param codCli1 Código identificador del producto en CLI2.
     * @return Un {@link Optional} que contiene el producto si existe,
     *         o vacío en caso contrario.
     */
     Optional<ProductCli2> findOneByProduct(String product);
}