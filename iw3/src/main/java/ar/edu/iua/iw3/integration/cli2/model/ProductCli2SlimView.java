package ar.edu.iua.iw3.integration.cli2.model;

import ar.edu.iua.iw3.model.Category;

/**
 * Proyección simplificada (vista reducida) de la entidad {@link ProductCli2}.
 * <p>
 * Se utiliza en consultas de JPA para obtener únicamente los campos más
 * relevantes de un producto, sin necesidad de cargar la entidad completa.
 * Esto permite optimizar rendimiento y transferir solo los datos necesarios
 * en integraciones o listados.
 * </p>
 */
public interface ProductCli2SlimView {
    /**
     * Obtiene el identificador único del producto.
     *
     * @return ID del producto.
     */
    Long getId();

    /**
     * Obtiene el nombre o descripción del producto.
     *
     * @return Nombre del producto.
     */
    String getProduct();

    /**
     * Obtiene el precio del producto.
     *
     * @return Precio en formato {@link Double}.
     */
    Double getPrice();

    /**
    * Obtiene la categoría asociada al producto.
    * <p>
    * Devuelve una proyección anidada de {@link Category}, en lugar de la entidad completa.
    * </p>
    *
    * @return Categoría del producto.
    */
    Category getCategory();

    /**
     * Proyección anidada que representa únicamente la categoría de un producto.
     */
    interface Category {
        /**
         * Obtiene el nombre de la categoría.
         *
         * @return Nombre de la categoría.
         */
        String getCategory();
    }
}
