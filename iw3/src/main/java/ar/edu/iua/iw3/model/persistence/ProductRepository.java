package ar.edu.iua.iw3.model.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ar.edu.iua.iw3.model.Product;
import jakarta.transaction.Transactional;

/**
 * Repositorio para la gestión de la persistencia de {@link Product}.
 * <p>
 * Extiende {@link JpaRepository} para proporcionar operaciones CRUD básicas y
 * consultas personalizadas sobre la entidad Product.
 * </p>
 * 
 * <p>
 * Anotaciones:
 * <ul>
 *   <li>{@code @Repository}: Marca la interfaz como un componente de repositorio de Spring.</li>
 * </ul>
 * </p>
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    /**
     * Busca un producto por su nombre o descripción exacta.
     *
     * @param product Nombre o descripción del producto a buscar.
     * @return {@link Optional} que contiene el producto si se encuentra, o vacío si no existe.
     */
    Optional<Product> findByProduct(String product);

    /**
     * Busca un producto por su nombre, excluyendo un identificador específico.
     * Para encontrar al producto que no tenga el mismo id que se pasa por parámetro y así poder actualizarlo correctamente.
     *
     * @param product Nombre o descripción del producto a buscar.
     * @param id Identificador del producto a excluir de la búsqueda.
     * @return {@link Optional} que contiene el producto si se encuentra, o vacío si no existe.
     */
    Optional<Product> findByProductAndIdNot(String product, long id);

    /**
     * Cuenta la cantidad de productos que pertenecen a una categoría específica.
     *
     * @param idCategory Identificador de la categoría.
     * @return Número de productos asociados a la categoría indicada.
     */
    @Query(value = "SELECT count(*) FROM products where id_category=?", nativeQuery = true)
	public Integer countProductsByCategory(long idCategory);
	
    /**
    * Actualiza el estado de stock de un producto específico.
    * <p>
    * Marca el producto como disponible o no disponible según el valor de {@code stock}.
    * Este método se ejecuta dentro de una transacción y utiliza {@link Modifying} 
    * para indicar que es una operación de escritura.
    * </p>
    *
    * @param stock Estado de stock a asignar (true = disponible, false = no disponible).
    * @param idProduct Identificador del producto a actualizar.
    * @return Número de filas afectadas (debería ser 0 o 1).
    */
	@Transactional
    @Modifying
    @Query(value = "UPDATE products SET stock=? WHERE id=?", nativeQuery = true)
	public int setStock(boolean stock, long idProduct);
}
