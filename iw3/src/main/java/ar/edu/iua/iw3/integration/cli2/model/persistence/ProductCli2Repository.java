package ar.edu.iua.iw3.integration.cli2.model.persistence;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.iua.iw3.integration.cli2.model.ProductCli2;

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
}