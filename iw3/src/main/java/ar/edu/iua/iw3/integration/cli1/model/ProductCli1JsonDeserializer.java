package ar.edu.iua.iw3.integration.cli1.model;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import ar.edu.iua.iw3.model.business.BusinessException;
import ar.edu.iua.iw3.model.business.ICategoryBusiness;
import ar.edu.iua.iw3.model.business.NotFoundException;
import ar.edu.iua.iw3.util.JsonUtiles;

/**
 * Deserializador personalizado para {@link ProductCli1} en el contexto de integración CLI1.
 * <p>
 * Permite transformar representaciones JSON de productos en objetos
 * {@link ProductCli1}, admitiendo múltiples posibles nombres de campos
 * (alias) para compatibilidad con distintos orígenes de datos.
 * </p>
 * <p>
 * Además, si se encuentra una categoría en el JSON, se valida su existencia
 * mediante el componente {@link ICategoryBusiness}.
 * </p>
 */
public class ProductCli1JsonDeserializer extends StdDeserializer<ProductCli1> {

    /**
     * Constructor protegido requerido por Jackson.
     *
     * @param vc Clase de la entidad a deserializar.
     */
	protected ProductCli1JsonDeserializer(Class<?> vc) {
		super(vc);
	}

    /**
     * Componente de negocio de categorías.
     * <p>
     * Se utiliza para validar y cargar la categoría indicada en el JSON,
     * en caso de estar presente.
     * </p>
     */
	private ICategoryBusiness categoryBusiness;

    /**
     * Constructor que inicializa el deserializador con acceso al
     * componente de negocio de categorías.
     *
     * @param vc               Clase de la entidad a deserializar.
     * @param categoryBusiness Servicio de negocio para validación de categorías.
     */
	public ProductCli1JsonDeserializer(Class<?> vc, ICategoryBusiness categoryBusiness) {
		super(vc);
		this.categoryBusiness = categoryBusiness;
	}

    /**
     * Convierte un nodo JSON en una instancia de {@link ProductCli1}.
     * <p>
     * Busca distintos nombres de campos equivalentes (por ejemplo:
     * {@code product_code}, {@code code_product}, {@code code})
     * para tolerar variaciones de origen.
     * </p>
     *
     * @param jp    Parser de JSON de Jackson.
     * @param ctxt  Contexto de deserialización.
     * @return Instancia de {@link ProductCli1} creada a partir de los datos JSON.
     * @throws IOException       Si ocurre un error de lectura del JSON.
     * @throws JacksonException  Si ocurre un error de deserialización.
     */
	@Override
	public ProductCli1 deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
		ProductCli1 r = new ProductCli1();
		JsonNode node = jp.getCodec().readTree(jp);

		String code = JsonUtiles.getString(node, "product_code,code_product,code".split(","),
				System.currentTimeMillis() + "");
		String productDesc = JsonUtiles.getString(node,
				"product,description,product_description,product_name".split(","), null);
		double price = JsonUtiles.getDouble(node, "product_price,price_product,price".split(","), 0);
		boolean stock = JsonUtiles.getBoolean(node, "stock,in_stock".split(","), false);
		r.setCodCli1(code);
		r.setProduct(productDesc);
		r.setPrice(price);
		r.setStock(stock);
        // Categoría (opcional)
		String categoryName = JsonUtiles.getString(node, "category,product_category,category_product".split(","), null);
		if (categoryName != null) {
			try {
				r.setCategory(categoryBusiness.load(categoryName));
			} catch (NotFoundException | BusinessException e) {
                // Categoría inexistente o error de negocio → se ignora
			}
		}
		return r;
	}

}