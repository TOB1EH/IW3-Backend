package ar.edu.iua.iw3.model.business;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.iua.iw3.model.Product;
import ar.edu.iua.iw3.model.persistence.ProductRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación de la lógica de negocio para productos en la aplicación IW3.
 *
 * Esta clase concreta implementa la interfaz IProductBusiness, proporcionando
 * la lógica de negocio para operaciones CRUD sobre productos. Actúa como
 * intermediario entre la capa de presentación (controladores) y la capa de
 * persistencia (repositorio), aplicando reglas de negocio, validaciones,
 * y manejo de excepciones.
 *
 * Propósito: Centralizar la lógica de negocio relacionada con productos,
 * asegurando que todas las operaciones cumplan con las reglas del dominio
 * y proporcionando una interfaz consistente para los controladores.
 *
 * Cómo funciona: Utiliza Spring's @Service para ser detectada como componente
 * de servicio. Inyecta ProductRepository para acceder a la base de datos.
 * Usa @Slf4j de Lombok para logging automático. Los métodos implementan
 * operaciones de negocio, lanzando excepciones específicas cuando corresponda.
 * Actualmente, todos los métodos están implementados con validaciones completas.
 *
 * Cómo usar: Esta clase es inyectada automáticamente por Spring en componentes
 * que dependan de IProductBusiness. Por ejemplo, en controladores REST que
 * necesiten realizar operaciones sobre productos. No se instancia manualmente.
 */
@Service
@Slf4j
public class ProductBusiness implements IProductBusiness {

    /**
     * Repositorio para acceso a datos de productos.
     *
     * Spring inyecta automáticamente una instancia de ProductRepository,
     * que es la interfaz de JPA para operaciones de base de datos sobre
     * la entidad Product. Se usa el nombre 'productDAO' siguiendo el patrón
     * Data Access Object (DAO), que abstrae el acceso a datos.
     */
    @Autowired
    private ProductRepository productDAO;

    /**
     * Obtiene la lista completa de productos disponibles.
     *
     * Este método recupera todos los productos almacenados en la base de datos
     * sin aplicar ningún filtro. Es útil para operaciones que requieren el
     * conjunto completo de productos, como listados generales.
     *
     * Propósito: Proporcionar acceso a todos los productos para operaciones
     * de lectura masiva.
     *
     * Cómo funciona: Utiliza el método findAll() del repositorio JPA para
     * ejecutar una consulta SELECT * FROM products. Si la operación es exitosa,
     * devuelve la lista de productos. En caso de error (por ejemplo, problemas
     * de conexión a la BD), registra el error en el log y lanza una BusinessException
     * con detalles del error.
     *
     * Cómo usar: Llamar desde controladores o otros servicios de negocio que
     * necesiten la lista completa de productos. No requiere parámetros.
     * Retorna una lista que puede estar vacía si no hay productos.
     *
     * @return Lista de objetos Product con todos los productos de la BD
     * @throws BusinessException Si ocurre un error al acceder a la base de datos
     */
    @Override
    public List<Product> list() throws BusinessException {
        try {
            // Obtiene todos los productos de la base de datos usando el repositorio JPA
            return productDAO.findAll();
        } catch (Exception e) {
            // Si ocurre cualquier excepción durante la operación, se registra en el log.
            log.error(e.getMessage(), e);
            // Se lanza una excepción de negocio personalizada para notificar a la capa superior.
            throw BusinessException.builder().ex(e).message(e.getMessage()).build();
        }
    }

    /**
     * Carga un producto específico por su ID único.
     *
     * Este método busca un producto en la base de datos utilizando su identificador
     * único. Actualmente no está implementado y requiere desarrollo futuro.
     *
     * Propósito: Permitir la recuperación individual de productos por ID para
     * operaciones de detalle o actualización.
     *
     * Cómo funciona: (No implementado) Debería utilizar el repositorio para
     * buscar por ID, lanzar NotFoundException si no existe, y devolver el
     * producto encontrado.
     *
     * Cómo usar: Una vez implementado, llamar con el ID del producto deseado.
     * Útil en controladores para operaciones GET /products/{id}.
     *
     * @param id El identificador único del producto a buscar
     * @return El objeto Product correspondiente al ID
     * @throws NotFoundException Si no se encuentra un producto con el ID dado
     * @throws BusinessException Si ocurre un error en la lógica de negocio
     */
    @Override
    public Product load(long id) throws NotFoundException, BusinessException {
        Optional<Product> r;
        try {
            r = productDAO.findById(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if (r.isEmpty()) {
            throw NotFoundException.builder().message("No se encuentra el Producto id = " + id).build();
        }
        return r.get();
    }

    /**
     * Carga un producto específico por su nombre.
     *
     * Este método busca un producto en la base de datos utilizando su nombre
     * como criterio de búsqueda. Actualmente no está implementado.
     *
     * Propósito: Facilitar la búsqueda de productos por nombre, útil para
     * validaciones de unicidad o búsquedas específicas.
     *
     * Cómo funciona: (No implementado) Debería consultar el repositorio por
     * nombre, considerando que el nombre es único según la entidad Product.
     *
     * Cómo usar: Una vez implementado, útil para verificar existencia antes
     * de agregar nuevos productos o para búsquedas por nombre.
     *
     * @param product El nombre del producto a buscar
     * @return El objeto Product correspondiente al nombre
     * @throws NotFoundException Si no se encuentra un producto con el nombre dado
     * @throws BusinessException Si ocurre un error en la lógica de negocio
     */
    @Override
    public Product load(String product) throws NotFoundException, BusinessException {
        Optional<Product> r;
        try {
            r = productDAO.findByProduct(product);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if (r.isEmpty()) {
            throw NotFoundException.builder().message("No se encuentra el Producto '"+product+"'").build();
        }
        return r.get();
    }

    /**
     * Agrega un nuevo producto a la base de datos.
     *
     * Este método permite crear e insertar un nuevo producto en el sistema,
     * aplicando validaciones de negocio como verificar unicidad del nombre.
     * Actualmente no está implementado.
     *
     * Propósito: Gestionar la creación de nuevos productos, asegurando que
     * no se dupliquen nombres y que se cumplan las reglas de negocio.
     *
     * Cómo funciona: (No implementado) Debería validar el producto, verificar
     * que no exista uno con el mismo nombre, guardar en BD y devolver el
     * producto con ID asignado.
     *
     * Cómo usar: Una vez implementado, llamar desde controladores POST para
     * crear productos. El producto debe tener nombre y precio válidos.
     *
     * @param product El objeto Product a agregar (sin ID, será generado)
     * @return El objeto Product guardado con ID asignado
     * @throws FoundException Si ya existe un producto con el mismo nombre
     * @throws BusinessException Si ocurre un error en la lógica de negocio
     */
    @Override
    public Product add(Product product) throws FoundException, BusinessException {
        try {
            load(product.getId());
            throw FoundException.builder().message("Se encontro el Producto id=" +product.getId()).build();
        } catch (NotFoundException e) {
        }
        try {
            load(product.getProduct());
            throw FoundException.builder().message("Se encontro el Producto '"+ product.getProduct() + "'").build();
        } catch (Exception e) {
        }
        try {
            return productDAO.save(product);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    /**
     * Actualiza un producto existente en la base de datos.
     *
     * Este método permite modificar los datos de un producto ya existente,
     * manteniendo su ID. Actualmente no está implementado.
     *
     * Propósito: Gestionar modificaciones a productos existentes, aplicando
     * validaciones para asegurar integridad de datos.
     *
     * Cómo funciona: (No implementado) Debería verificar existencia del producto,
     * aplicar validaciones, actualizar en BD y devolver el producto modificado.
     *
     * Cómo usar: Una vez implementado, útil en controladores PUT/PATCH para
     * actualizar productos. El producto debe incluir el ID existente.
     *
     * @param product El objeto Product con datos actualizados e ID existente
     * @return El objeto Product actualizado
     * @throws NotFoundException Si no se encuentra el producto a actualizar
     * @throws BusinessException Si ocurre un error en la lógica de negocio
     */
    @Override
    public Product update(Product product) throws FoundException, NotFoundException, BusinessException {
        load(product.getId());
        Optional<Product> nombreExistente = null;
        try {
            nombreExistente = productDAO.findByProductAndIdNot(product.getProduct(), product.getId());
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if (nombreExistente.isPresent()) {
            throw FoundException.builder().message("Se encontro un producto nombre ="+product.getProduct()).build();
        }
        try {
            return productDAO.save(product);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    /**
     * Elimina un producto de la base de datos por su ID.
     *
     * Este método permite remover un producto del sistema de manera permanente.
     * Actualmente no está implementado.
     *
     * Propósito: Gestionar la eliminación de productos, asegurando que el
     * producto existe antes de eliminarlo.
     *
     * Cómo funciona: (No implementado) Debería verificar existencia, aplicar
     * reglas de negocio (ej. no eliminar si está en uso), y eliminar de BD.
     *
     * Cómo usar: Una vez implementado, llamar desde controladores DELETE.
     * Útil para remover productos obsoletos o incorrectos.
     *
     * @param id El ID del producto a eliminar
     * @throws NotFoundException Si no se encuentra el producto a eliminar
     * @throws BusinessException Si ocurre un error en la lógica de negocio
     */
    @Override
    public void delete(long id) throws NotFoundException, BusinessException {
        load(id);
        try {
            productDAO.deleteById(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }
}
