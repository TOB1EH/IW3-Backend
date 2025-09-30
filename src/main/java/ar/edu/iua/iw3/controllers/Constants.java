package ar.edu.iua.iw3.controllers;

/**
 * Clase de constantes para URLs de la API REST en la aplicación IW3.
 *
 * Esta clase final contiene constantes estáticas que definen las rutas
 * base y específicas para los endpoints de la API. Está diseñada para
 * centralizar la definición de URLs, facilitando el mantenimiento y
 * evitando errores de tipeo en las rutas.
 *
 * Propósito: Proporcionar un lugar centralizado para definir y gestionar
 * las rutas de la API, asegurando consistencia en los controladores.
 *
 * Cómo funciona: Las constantes se concatenan para formar rutas completas.
 * Por ejemplo, URL_PRODUCTS combina URL_BASE con "/products" para obtener
 * "/api/v1/products". Todas las constantes son finales y estáticas para
 * garantizar inmutabilidad.
 *
 * Cómo usar: Importar esta clase en los controladores y usar las constantes
 * en las anotaciones @RequestMapping o similares. Por ejemplo:
 * @RequestMapping(value = Constants.URL_PRODUCTS, method = RequestMethod.GET)
 * Esto asegura que si se cambia una ruta base, se actualice automáticamente
 * en todos los lugares donde se use.
 */
public final class Constants {
    public static final String URL_API = "/api";
    public static final String URL_API_VERSION = "/v1";
    public static final String URL_BASE = URL_API + URL_API_VERSION;
    public static final String URL_PRODUCTS = URL_BASE + "/products";

    public static final String URL_INTEGRATION = URL_BASE + "/integration";
    public static final String URL_INTEGRATION_CLI1 = URL_INTEGRATION + "/cli1";
    public static final String URL_INTEGRATION_CLI2 = URL_INTEGRATION + "/cli2";
}
