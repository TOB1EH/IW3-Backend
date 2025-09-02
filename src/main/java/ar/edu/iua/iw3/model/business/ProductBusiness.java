package ar.edu.iua.iw3.model.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.iua.iw3.model.Product;
import ar.edu.iua.iw3.model.persistence.ProductRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductBusiness implements IProductBusiness {

    @Autowired
    private ProductRepository productDAO;

    @Override
    public List<Product> list() throws BusinessException {
        try {
            // Obtiene todos los productos de la base de datos usando el repositorio JPA
            return productDAO.findAll();
        } catch (Exception e) {
            // Si ocurre un error, lo registra en el log con el mensaje y la excepción completa
            log.error(e.getMessage(), e);
            // Lanza una excepción de negocio personalizada con el mensaje y la excepción original
            throw BusinessException.builder().ex(e).message(e.getMessage()).build();
        }
    }

    @Override
    public Product load(long id) throws NotFoundException, BusinessException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Product load(String product) throws NotFoundException, BusinessException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Product add(Product product) throws FoundException, BusinessException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Product update(Product product) throws NotFoundException, BusinessException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void delete(long id) throws NotFoundException, BusinessException {
        // TODO Auto-generated method stub

    }

}
