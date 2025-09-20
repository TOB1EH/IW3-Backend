package ar.edu.iua.iw3.model.business;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.iua.iw3.model.Category;
import ar.edu.iua.iw3.model.persistence.CategoryRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación de la lógica de negocio para la gestión de categorías.
 * <p>
 * Esta clase implementa {@link ICategoryBusiness} y se encarga de las operaciones
 * de CRUD sobre {@link Category}, incluyendo validaciones de existencia y manejo
 * centralizado de excepciones de negocio.
 * </p>
 */
@Service
@Slf4j
public class CategoryBusiness implements ICategoryBusiness {

    /**
     * Repositorio JPA para la persistencia de categorías.
     */
	@Autowired
	private CategoryRepository categoryDAO;

	/**
     * Obtiene una categoría por su identificador único.
     *
     * @param id Identificador de la categoría.
     * @return La categoría encontrada.
     * @throws NotFoundException Si no se encuentra la categoría con el id especificado.
     * @throws BusinessException Si ocurre un error en la capa de persistencia.
     */
	@Override
	public Category load(long id) throws NotFoundException, BusinessException {
		Optional<Category> r;
		try {
			r = categoryDAO.findById(id);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw BusinessException.builder().ex(e).build();
		}
		if (r.isEmpty()) {
			throw NotFoundException.builder().message("No se encuentra la Categoría id=" + id).build();
		}
		return r.get();
	}

	/**
     * Obtiene una categoría por su nombre.
     *
     * @param category Nombre de la categoría.
     * @return La categoría encontrada.
     * @throws NotFoundException Si no se encuentra la categoría con el nombre especificado.
     * @throws BusinessException Si ocurre un error en la capa de persistencia.
     */
	@Override
	public Category load(String category) throws NotFoundException, BusinessException {
		Optional<Category> r;
		try {
			r = categoryDAO.findOneByCategory(category);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw BusinessException.builder().ex(e).build();
		}
		if (r.isEmpty()) {
			throw NotFoundException.builder().message("No se encuentra la Categoría nombbre=" + category).build();
		}
		return r.get();
	}

	/**
     * Obtiene todas las categorías disponibles.
     *
     * @return Lista de categorías.
     * @throws BusinessException Si ocurre un error en la capa de persistencia.
     */
	@Override
	public List<Category> list() throws BusinessException {
		try {
			return categoryDAO.findAll();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw BusinessException.builder().ex(e).build();
		}
	}

	/**
     * Agrega una nueva categoría.
     * <p>
     * Antes de agregar, verifica que la categoría no exista mediante su id.
     * </p>
     *
     * @param category Objeto {@link Category} a agregar.
     * @return La categoría persistida.
     * @throws FoundException Si ya existe una categoría con el mismo id.
     * @throws BusinessException Si ocurre un error en la capa de persistencia.
     */
	@Override
	public Category add(Category Category) throws FoundException, BusinessException {

		try {
			load(Category.getId());
			throw FoundException.builder().message("Se encuentró la Categoría id=" + Category.getId()).build();
		} catch (NotFoundException e) {
		}

		try {
			return categoryDAO.save(Category);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw BusinessException.builder().ex(e).build();
		}
	}

	/**
     * Actualiza una categoría existente.
     *
     * @param category Objeto {@link Category} con los datos actualizados.
     * @return La categoría actualizada.
     * @throws NotFoundException Si no se encuentra la categoría a actualizar.
     * @throws BusinessException Si ocurre un error en la capa de persistencia.
     */
	@Override
	public Category update(Category Category) throws NotFoundException, BusinessException {
		load(Category.getId());
		try {
			return categoryDAO.save(Category);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw BusinessException.builder().ex(e).build();
		}
	}

	/**
     * Elimina una categoría por su identificador.
     *
     * @param id Identificador de la categoría a eliminar.
     * @throws NotFoundException Si no se encuentra la categoría a eliminar.
     * @throws BusinessException Si ocurre un error en la capa de persistencia.
     */
	@Override
	public void delete(long id) throws NotFoundException, BusinessException {
		load(id);
		try {
			categoryDAO.deleteById(id);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw BusinessException.builder().ex(e).build();
		}
	}

}