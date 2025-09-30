package ar.edu.iua.iw3.model;

// Anotaciones para la persistencia con JPA (Jakarta Persistence API)
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// Anotaciones de Lombok para generar automáticamente constructores, getters y setters
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad JPA que representa un producto en la aplicación IW3.
 *
 * Esta clase modela la estructura de datos de un producto, incluyendo
 * su identificador único, nombre, disponibilidad en stock y precio.
 * Se mapea directamente a la tabla "products" en la base de datos.
 *
 * Propósito: Representar productos en el dominio de la aplicación,
 * permitiendo operaciones CRUD a través de JPA. Proporciona una
 * abstracción orientada a objetos sobre los datos relacionales.
 *
 * Cómo funciona: Utiliza anotaciones JPA para mapear la clase a una
 * tabla de BD. Lombok genera automáticamente constructores, getters
 * y setters. El ID se genera automáticamente por la BD. El nombre
 * del producto es único para evitar duplicados.
 *
 * Cómo usar: Instanciar para crear nuevos productos o usar en consultas
 * JPA. Los campos se acceden vía getters/setters generados por Lombok.
 * En repositorios, usar como tipo genérico. Ejemplo:
 * Product p = new Product(); p.setProduct("Manzana"); p.setPrecio(10.5);
 */
@Entity // Indica que esta clase es una entidad JPA (se mapea a una tabla de la BD)
@Table(name = "products") // Especifica el nombre de la tabla en la BD
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor // Genera un constructor con todos los campos
@NoArgsConstructor // Genera un constructor sin argumentos
@Getter // Genera automáticamente los métodos getter para todos los campos
@Setter // Genera automáticamente los métodos setter para todos los campos
public class Product {

    /**
     * Identificador único del producto.
     *
     * Este campo es la clave primaria de la entidad, generado automáticamente
     * por la base de datos usando autoincremento. No debe establecerse manualmente
     * al crear nuevos productos.
     */
    @Id // Indica que este campo es la clave primaria de la entidad
    @GeneratedValue(strategy = GenerationType.IDENTITY) // El valor se genera automáticamente (autoincremental)
    private long id;

    /**
     * Nombre del producto.
     *
     * Campo de texto que identifica al producto. Tiene una longitud máxima
     * de 100 caracteres y debe ser único en la base de datos para evitar
     * productos duplicados.
     */
    @Column(length = 100, unique = true) // Columna con máximo 100 caracteres y valor único
    private String product;

    /**
     * Indicador de disponibilidad en stock.
     *
     * Campo booleano que indica si el producto está disponible en inventario.
     * Por defecto es false (sin stock). Se mapea a un tipo tinyint en la BD.
     */
    @Column(columnDefinition = "tinyint default 0") // Columna tipo tinyint en la BD, valor por defecto 0
    private boolean stock = false;

    /**
     * Precio del producto.
     *
     * Valor numérico que representa el costo del producto. Se almacena como
     * double para permitir decimales. No tiene restricciones específicas
     * de validación en la entidad.
     */ 
    private double precio;

    @ManyToOne
    @JoinColumn(name = "id_category", nullable = true)
    private Category category;

}
