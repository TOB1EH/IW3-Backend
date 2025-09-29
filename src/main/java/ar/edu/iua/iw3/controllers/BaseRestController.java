package ar.edu.iua.iw3.controllers;

/**
 * Clase base para controladores REST en la aplicación IW3.
 *
 * Esta clase abstracta sirve como superclase para todos los controladores
 * REST de la aplicación. Está diseñada para contener funcionalidad común
 * que puede ser compartida entre diferentes controladores, como manejo
 * de excepciones, logging, o utilidades generales para respuestas HTTP.
 *
 * Propósito: Proporcionar una base común para los controladores REST,
 * promoviendo la reutilización de código y un diseño consistente.
 *
 * Cómo funciona: Actualmente está vacía, pero se espera que se extienda
 * con métodos y campos comunes. Los controladores específicos heredan
 * de esta clase para beneficiarse de cualquier funcionalidad futura.
 *
 * Cómo usar: Extender esta clase en controladores concretos, por ejemplo:
 * public class MiController extends BaseRestController { ... }
 * Esto permite que los controladores hijos accedan a métodos o propiedades
 * definidas aquí en el futuro.
 */
public class BaseRestController {

}
