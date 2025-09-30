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
 * Implementación de la lógica de negocio para productos en el perfil CLI2.
 *
 * Esta clase concreta implementa la interfaz IProductCli2Business, proporcionando
 * métodos específicos para manejar productos con lógica de negocio particular
 * para el perfil CLI2. Actúa como intermediario entre los controladores y el
 * repositorio de persistencia ProductCli2Repository.
 *
 * Propósito: Centralizar la lógica de negocio relacionada con productos CLI2,
 * incluyendo operaciones para listar productos expirados y obtener vistas
 * simplificadas ordenadas por precio.
 *
 * Cómo funciona: Utiliza Spring @Service para ser detectada como componente
 * de servicio. Inyecta ProductCli2Repository para acceso a datos. Usa Lombok
 * @Slf4j para logging. Los métodos implementan consultas específicas y manejan
 * excepciones lanzando BusinessException en caso de error.
 *
 * Cómo usar: Esta clase es inyectada automáticamente por Spring en componentes
 * que dependan de IProductCli2Business. Por ejemplo, en controladores REST
 * que necesiten operaciones específicas para productos CLI2.
 */
@Service
@Slf4j
public class ProductCli2Business implements IProductCli2Business {

    /**
     * Repositorio para acceso a datos de productos CLI2.
     *
     * Spring inyecta automáticamente una instancia de ProductCli2Repository,
     * que es la interfaz de JPA para operaciones de base de datos sobre
     * la entidad ProductCli2. La inyección es opcional para evitar errores
     * si la clase no está disponible en ciertos perfiles.
     */
    @Autowired(required = false)
    private ProductCli2Repository productDAO;

    /**
     * Obtiene la lista de productos cuya fecha de expiración es anterior a la fecha dada.
     *
     * Este método consulta el repositorio para obtener todos los productos que
     * han expirado antes de la fecha especificada, ordenados en forma descendente
     * por fecha de expiración.
     *
     * Propósito: Permitir la obtención de productos expirados para su gestión o
     * visualización en el sistema.
     *
     * Cómo funciona: Llama al método findByExpirationDateBeforeOrderByExpirationDateDesc
     * del repositorio. En caso de error, registra el error y lanza una excepción
     * de negocio BusinessException.
     *
     * @param date La fecha límite para considerar un producto como expirado
     * @return Lista de productos expirados ordenados por fecha de expiración descendente
     * @throws BusinessException Si ocurre un error al acceder a la base de datos
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
     * Obtiene una lista simplificada de productos ordenados por precio descendente.
     *
     * Este método devuelve una vista reducida de los productos, ordenada por
     * precio de mayor a menor, para facilitar listados rápidos o resúmenes.
     *
     * Propósito: Proveer una lista optimizada para mostrar información básica
     * de productos ordenados por precio.
     *
     * Cómo funciona: Llama al método findByOrderByPrecioDesc del repositorio.
     * En caso de error, registra el error y lanza una excepción BusinessException.
     *
     * @return Lista de vistas simplificadas de productos ordenados por precio descendente
     * @throws BusinessException Si ocurre un error al acceder a la base de datos
     */
    @Override
    public List<ProductCli2SlimView> listSlim() throws BusinessException {
        try {
            return productDAO.findByOrderByPrecioDesc();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }
}
