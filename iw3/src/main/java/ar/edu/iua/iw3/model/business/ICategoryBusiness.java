package ar.edu.iua.iw3.model.business;

import java.util.List;

import ar.edu.iua.iw3.model.Category;

/**
 * Interfaz de la capa de negocio para la gestión de categorías.
 * <p>
 * Define las operaciones CRUD principales para la entidad {@link Category},
 * incluyendo la búsqueda por id o nombre, listado completo, creación, actualización
 * y eliminación de categorías.
 * </p>
 */
public interface ICategoryBusiness {
	/**
     * Obtiene una categoría por su identificador único.
     *
     * @param id Identificador de la categoría.
     * @return La categoría encontrada.
     * @throws NotFoundException Si no existe la categoría con el id especificado.
     * @throws BusinessException Si ocurre un error en la capa de negocio.
     */
    public Category load(long id) throws NotFoundException, BusinessException;

	/**
     * Obtiene una categoría por su nombre.
     *
     * @param category Nombre de la categoría.
     * @return La categoría encontrada.
     * @throws NotFoundException Si no existe la categoría con el nombre especificado.
     * @throws BusinessException Si ocurre un error en la capa de negocio.
     */
	public Category load(String category) throws NotFoundException, BusinessException;

	/**
     * Obtiene todas las categorías disponibles.
     *
     * @return Lista de categorías.
     * @throws BusinessException Si ocurre un error en la capa de negocio.
     */
	public List<Category> list() throws BusinessException;

	/**
     * Agrega una nueva categoría.
     *
     * @param category Objeto {@link Category} a agregar.
     * @return La categoría persistida.
     * @throws FoundException Si ya existe una categoría con el mismo id.
     * @throws BusinessException Si ocurre un error en la capa de negocio.
     */
	public Category add(Category category) throws FoundException, BusinessException;

	/**
     * Actualiza una categoría existente.
     *
     * @param category Objeto {@link Category} con los datos actualizados.
     * @return La categoría actualizada.
     * @throws NotFoundException Si no se encuentra la categoría a actualizar.
     * @throws BusinessException Si ocurre un error en la capa de negocio.
     */
	public Category update(Category category) throws NotFoundException, BusinessException;

	/**
     * Elimina una categoría por su identificador.
     *
     * @param id Identificador de la categoría a eliminar.
     * @throws NotFoundException Si no se encuentra la categoría a eliminar.
     * @throws BusinessException Si ocurre un error en la capa de negocio.
     */
	public void delete(long id) throws NotFoundException, BusinessException;
}
