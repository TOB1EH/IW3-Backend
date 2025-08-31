package ar.edu.iua.iw3.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Representa un producto en el sistema.
 * <p>
 * Esta clase está mapeada a la tabla {@code products} en la base de datos y contiene
 * información sobre el nombre del producto, su disponibilidad en stock y el precio.
 * </p>
 * <p>
 * Se utiliza junto con los repositorios y la capa de negocio para realizar operaciones CRUD.
 * </p>
 * 
 * <p>
 * Anotaciones de Lombok:
 * <ul>
 *   <li>{@code @Getter} y {@code @Setter}: Generan los métodos getter y setter para todos los campos.</li>
 *   <li>{@code @NoArgsConstructor} y {@code @AllArgsConstructor}: Generan constructores sin y con todos los campos.</li>
 * </ul>
 * </p>
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    /**
     * Identificador único del producto.
     * <p>
     * Se genera automáticamente mediante la estrategia {@link GenerationType#IDENTITY}.
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre o descripción del producto.
     * <p>
     * Debe ser único en la tabla y tiene un máximo de 100 caracteres.
     * </p>
     */
    @Column(length = 100, unique = true)
    private String product;

    /**
     * Indica si el producto está disponible en stock.
     * <p>
     * Se almacena en la base de datos como un {@code tinyint} con valor por defecto {@code 0} (falso).
     * </p>
     */
    @Column(columnDefinition = "tinyint default 0")
    private boolean stock = false;

    /**
     * Precio del producto.
     */
    private double price;
}
