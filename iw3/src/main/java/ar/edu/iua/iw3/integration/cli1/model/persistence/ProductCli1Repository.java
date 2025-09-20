package ar.edu.iua.iw3.integration.cli1.model.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.edu.iua.iw3.integration.cli1.model.ProductCli1;

/**
 * Repositorio de persistencia para la entidad {@link ProductCli1}.
 * <p>
 * Extiende {@link JpaRepository} para heredar operaciones básicas de CRUD y
 * define un método adicional de consulta específico por código <code>codCli1</code>.
 * </p>
 */
public interface ProductCli1Repository extends JpaRepository<ProductCli1, Long>{
    /**
     * Busca un producto en CLI1 por su código único.
     *
     * @param codCli1 Código identificador del producto en CLI1.
     * @return Un {@link Optional} que contiene el producto si existe,
     *         o vacío en caso contrario.
     */
    Optional<ProductCli1> findOneByCodCli1(String codCli1);
}
