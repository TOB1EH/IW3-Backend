package ar.edu.iua.iw3.model.business;

import java.util.List;
import ar.edu.iua.iw3.model.Product;

/**
 * Interfaz para la lógica de negocio de productos en la aplicación IW3.
 *
 * Esta interfaz define el contrato para las operaciones de negocio relacionadas
 * con productos, incluyendo operaciones CRUD (Crear, Leer, Actualizar, Eliminar).
 * Separa la lógica de negocio de la capa de presentación y persistencia.
 *
 * Propósito: Establecer un contrato claro para las operaciones de productos,
 * permitiendo diferentes implementaciones (por ejemplo, para testing o diferentes
 * estrategias de negocio) mientras mantiene la consistencia en la interfaz.
 *
 * Cómo funciona: Los métodos definidos aquí representan operaciones de negocio
 * que pueden lanzar excepciones específicas como BusinessException para errores
 * generales, NotFoundException cuando no se encuentra un producto, y FoundException
 * cuando se intenta agregar un producto que ya existe. La implementación concreta
 * manejará la lógica específica, como validaciones, transformaciones, o llamadas
 * a la capa de persistencia.
 *
 * Cómo usar: Implementar esta interfaz en clases concretas que manejen la lógica
 * de negocio de productos. Por ejemplo, una clase ProductBusiness implementaría
 * estos métodos. Los controladores pueden depender de esta interfaz para acceder
 * a las operaciones de productos sin conocer la implementación específica.
 */
public interface IProductBusiness {

    /**
     * Obtiene la lista completa de productos disponibles en el sistema.
     *
     * Este método recupera todos los productos almacenados, sin aplicar filtros.
     * Es útil para operaciones que requieren el conjunto completo de productos,
     * como listados generales o exportaciones.
     *
     * @return Lista de objetos Product con todos los productos del sistema
     * @throws BusinessException Si ocurre un error durante la recuperación de datos
     */
    public List<Product> list() throws BusinessException;

    /**
     * Carga un producto específico utilizando su identificador único.
     *
     * Este método busca y devuelve un producto basado en su ID numérico.
     * Es fundamental para operaciones de detalle, actualización o eliminación.
     *
     * @param id El identificador único del producto a buscar
     * @return El objeto Product correspondiente al ID proporcionado
     * @throws NotFoundException Si no existe un producto con el ID especificado
     * @throws BusinessException Si ocurre un error en la lógica de negocio
     */
    public Product load(long id) throws NotFoundException, BusinessException;

    /**
     * Carga un producto específico utilizando su nombre como criterio de búsqueda.
     *
     * Este método busca un producto por su nombre, asumiendo que los nombres
     * son únicos en el sistema. Útil para validaciones de unicidad o búsquedas
     * por texto.
     *
     * @param product El nombre del producto a buscar
     * @return El objeto Product correspondiente al nombre proporcionado
     * @throws NotFoundException Si no existe un producto con el nombre especificado
     * @throws BusinessException Si ocurre un error en la lógica de negocio
     */
    public Product load(String product) throws NotFoundException, BusinessException;

    /**
     * Agrega un nuevo producto al sistema.
     *
     * Este método crea e inserta un nuevo producto, aplicando validaciones
     * de negocio como verificar que no exista un producto con el mismo nombre.
     * El producto debe tener nombre y precio válidos.
     *
     * @param product El objeto Product a agregar (sin ID, será generado)
     * @return El objeto Product agregado con ID asignado
     * @throws FoundException Si ya existe un producto con el mismo nombre
     * @throws BusinessException Si ocurre un error en la lógica de negocio
     */
    public Product add(Product product) throws FoundException, BusinessException;

    /**
     * Actualiza un producto existente en el sistema.
     *
     * Este método modifica los datos de un producto ya existente, manteniendo
     * su ID. Aplica validaciones para asegurar integridad, como verificar
     * unicidad del nombre si se cambia.
     *
     * @param product El objeto Product con datos actualizados e ID existente
     * @return El objeto Product actualizado
     * @throws NotFoundException Si no se encuentra el producto a actualizar
     * @throws FoundException Si el nuevo nombre ya está en uso por otro producto
     * @throws BusinessException Si ocurre un error en la lógica de negocio
     */
    public Product update(Product product) throws FoundException, NotFoundException, BusinessException;

    /**
     * Elimina un producto del sistema por su ID.
     *
     * Este método remueve permanentemente un producto de la base de datos.
     * Debe verificar que el producto existe antes de eliminarlo.
     *
     * @param id El ID del producto a eliminar
     * @throws NotFoundException Si no se encuentra el producto a eliminar
     * @throws BusinessException Si ocurre un error en la lógica de negocio
     */
    public void delete(long id) throws NotFoundException, BusinessException;
}
