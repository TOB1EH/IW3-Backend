package ar.edu.iua.iw3.integration.cli1.model.business;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.edu.iua.iw3.integration.cli1.model.ProductCli1;
import ar.edu.iua.iw3.integration.cli1.model.ProductCli1JsonDeserializer;
import ar.edu.iua.iw3.integration.cli1.model.persistence.ProductCli1Repository;
import ar.edu.iua.iw3.model.business.BusinessException;
import ar.edu.iua.iw3.model.business.FoundException;
import ar.edu.iua.iw3.model.business.ICategoryBusiness;
import ar.edu.iua.iw3.model.business.IProductBusiness;
import ar.edu.iua.iw3.model.business.NotFoundException;
import ar.edu.iua.iw3.util.JsonUtiles;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación de la lógica de negocio para productos en el perfil CLI1.
 *
 * Esta clase concreta implementa la interfaz IProductCli1Business, proporcionando
 * métodos específicos para manejar productos con lógica de negocio particular
 * para el perfil CLI1. Incluye operaciones para cargar productos por código,
 * listar todos los productos, agregar productos nuevos y agregar productos
 * desde fuentes externas vía JSON.
 *
 * Propósito: Centralizar la lógica de negocio relacionada con productos CLI1,
 * incluyendo validaciones específicas como unicidad de códigos y integración
 * con productos base y categorías.
 *
 * Cómo funciona: Utiliza Spring @Service para ser detectada como componente
 * de servicio. Inyecta ProductCli1Repository para acceso a datos, IProductBusiness
 * para validaciones base, y ICategoryBusiness opcionalmente para categorías.
 * Usa Lombok @Slf4j para logging. Los métodos implementan operaciones específicas
 * y manejan excepciones lanzando BusinessException, FoundException o NotFoundException
 * según corresponda.
 *
 * Cómo usar: Esta clase es inyectada automáticamente por Spring en componentes
 * que dependan de IProductCli1Business. Por ejemplo, en controladores REST
 * que necesiten operaciones específicas para productos CLI1.
 */
@Service
@Slf4j
public class ProductCli1Business implements IProductCli1Business {

    /**
     * Repositorio para acceso a datos de productos CLI1.
     *
     * Spring inyecta automáticamente una instancia de ProductCli1Repository,
     * que es la interfaz de JPA para operaciones de base de datos sobre
     * la entidad ProductCli1. La inyección es opcional para evitar errores
     * si la clase no está disponible en ciertos perfiles.
     */
    @Autowired(required = false)
    private ProductCli1Repository productDAO;

    /**
     * Servicio de negocio base para productos.
     *
     * Inyectado para realizar validaciones de productos base, como verificar
     * existencia por ID antes de agregar productos CLI1.
     */
    @Autowired
    private IProductBusiness productBaseBusiness;

    /**
     * Servicio de negocio para categorías.
     *
     * Inyectado opcionalmente para operaciones que involucren categorías,
     * como el deserializador JSON que puede necesitar resolver categorías.
     */
    @Autowired(required = false)
    private ICategoryBusiness categoryBusiness;

    /**
     * Carga un producto CLI1 específico por su código único.
     *
     * Este método busca un producto en la base de datos utilizando su código
     * CLI1 como criterio de búsqueda.
     *
     * Propósito: Permitir la recuperación individual de productos CLI1 por
     * código para operaciones de detalle o validación.
     *
     * Cómo funciona: Consulta el repositorio usando findOneByCodCli1. Si no
     * se encuentra, lanza NotFoundException. En caso de error de BD, registra
     * el error y lanza BusinessException.
     *
     * @param codCli1 El código único del producto CLI1 a buscar
     * @return El objeto ProductCli1 correspondiente al código
     * @throws NotFoundException Si no se encuentra un producto con el código dado
     * @throws BusinessException Si ocurre un error al acceder a la base de datos
     */
    @Override
    public ProductCli1 load(String codCli1) throws NotFoundException, BusinessException {
        Optional<ProductCli1> r;
        try {
            r = productDAO.findOneByCodCli1(codCli1);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if (r.isEmpty()) {
            throw NotFoundException.builder().message("No se encuentra el Producto codCli1=" + codCli1).build();
        }
        return r.get();
    }

    /**
     * Obtiene la lista completa de productos CLI1 disponibles.
     *
     * Este método recupera todos los productos CLI1 almacenados en la base de datos
     * sin aplicar ningún filtro.
     *
     * Propósito: Proporcionar acceso a todos los productos CLI1 para operaciones
     * de lectura masiva.
     *
     * Cómo funciona: Utiliza el método findAll() del repositorio JPA. En caso
     * de error, registra el error y lanza BusinessException.
     *
     * @return Lista de objetos ProductCli1 con todos los productos CLI1 de la BD
     * @throws BusinessException Si ocurre un error al acceder a la base de datos
     */
    @Override
    public List<ProductCli1> list() throws BusinessException {
        try {
            return productDAO.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    /**
     * Agrega un nuevo producto CLI1 a la base de datos.
     *
     * Este método permite crear e insertar un nuevo producto CLI1 en el sistema,
     * aplicando validaciones de negocio como verificar unicidad del ID y código CLI1.
     *
     * Propósito: Gestionar la creación de nuevos productos CLI1, asegurando que
     * no se dupliquen IDs o códigos y que se cumplan las reglas de negocio.
     *
     * Cómo funciona: Verifica que no exista un producto base con el mismo ID,
     * y que no exista un producto CLI1 con el mismo código. Si pasa las validaciones,
     * guarda el producto y lo devuelve con ID asignado.
     *
     * @param product El objeto ProductCli1 a agregar (sin ID, será generado)
     * @return El objeto ProductCli1 guardado con ID asignado
     * @throws FoundException Si ya existe un producto con el mismo ID o código
     * @throws BusinessException Si ocurre un error en la lógica de negocio
     */
    @Override
    public ProductCli1 add(ProductCli1 product) throws FoundException, BusinessException {
        try {
            productBaseBusiness.load(product.getId());
            throw FoundException.builder().message("Se encontró el Producto id=" + product.getId()).build();
        } catch (NotFoundException e) {
        }

        if (productDAO.findOneByCodCli1(product.getCodCli1()).isPresent()) {
            throw FoundException.builder().message("Se encontró el Producto código=" + product.getCodCli1()).build();
        }

        try {
            return productDAO.save(product);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    /**
     * Agrega un nuevo producto CLI1 desde una fuente externa vía JSON.
     *
     * Este método permite crear productos CLI1 a partir de datos JSON externos,
     * utilizando un deserializador personalizado que puede resolver categorías.
     *
     * Propósito: Facilitar la integración con sistemas externos que envíen
     * datos de productos en formato JSON.
     *
     * Cómo funciona: Configura un ObjectMapper con un deserializador personalizado
     * (ProductCli1JsonDeserializer) que puede manejar categorías. Parsea el JSON
     * a un objeto ProductCli1 y luego llama al método add() para validaciones
     * y guardado.
     *
     * @param json Cadena JSON que representa un producto CLI1
     * @return El objeto ProductCli1 creado y guardado
     * @throws FoundException Si ya existe un producto con el mismo ID o código
     * @throws BusinessException Si ocurre un error en el parsing JSON o lógica de negocio
     */
    @Override
    public ProductCli1 addExternal(String json) throws FoundException, BusinessException {
        ObjectMapper mapper = JsonUtiles.getObjectMapper(ProductCli1.class,
                new ProductCli1JsonDeserializer(ProductCli1.class, categoryBusiness), null);
        ProductCli1 product = null;
        try {
            product = mapper.readValue(json, ProductCli1.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }

        return add(product);
    }
}
