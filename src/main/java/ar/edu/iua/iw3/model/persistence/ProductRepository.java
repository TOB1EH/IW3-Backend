package ar.edu.iua.iw3.model.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ar.edu.iua.iw3.model.Product;

/**
 * Repositorio JPA para operaciones de persistencia de la entidad Product en la aplicación IW3.
 *
 * Esta interfaz define métodos para acceder y manipular datos de productos
 * en la base de datos. Utiliza Spring Data JPA para generar automáticamente
 * implementaciones de consultas basadas en convenciones de nomenclatura.
 *
 * Propósito: Proporcionar una capa de abstracción sobre el acceso a datos,
 * permitiendo operaciones CRUD y consultas personalizadas sobre productos
 * sin escribir código SQL explícito.
 *
 * Cómo funciona: Extiende JpaRepository<Product, Long>, heredando métodos
 * estándar como save(), findById(), findAll(), delete(), etc. Spring Data
 * JPA genera automáticamente la implementación en tiempo de ejecución.
 * El método findByProduct es una consulta derivada que busca por el campo
 * 'product' (nombre del producto).
 *
 * Cómo usar: Inyectar esta interfaz en clases de servicio (como ProductBusiness)
 * usando @Autowired. Llamar a los métodos para realizar operaciones de BD.
 * Ejemplos: productRepository.findAll() para listar, productRepository.findByProduct("nombre")
 * para buscar por nombre. Los métodos devuelven Optional para manejar casos
 * donde no se encuentre el producto.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

    /**
     * Busca un producto por su nombre.
     *
     * Este método ejecuta una consulta equivalente a:
     * SELECT * FROM products WHERE product = ?
     *
     * Propósito: Permitir búsqueda de productos por nombre, útil para
     * validaciones de unicidad o búsquedas específicas.
     *
     * Cómo funciona: Spring Data JPA genera automáticamente la consulta
     * basada en el nombre del método (findBy + Product, donde Product
     * es el nombre del campo en la entidad).
     *
     * Cómo usar: Llamar con el nombre del producto. Retorna Optional<Product>
     * que estará vacío si no se encuentra ningún producto con ese nombre.
     * Ejemplo: Optional<Product> prod = productRepository.findByProduct("Manzana");
     *
     * @param product El nombre del producto a buscar
     * @return Optional con el producto encontrado, o vacío si no existe
     */
    Optional<Product> findByProduct (String product);

    Optional<Product> findByProductAndIdNot (String product, long id);
}
