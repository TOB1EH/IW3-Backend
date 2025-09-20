package ar.edu.iua.iw3.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Representa un producto en el sistema.
 * <p>
 * Esta clase está mapeada a la tabla {@code products} en la base de datos y contiene
 * información sobre el nombre del producto, su disponibilidad en stock, el precio
 * y la categoría a la que pertenece.
 * </p>
 * <p>
 * Se utiliza junto con los repositorios y la capa de negocio para realizar operaciones CRUD.
 * </p>
 *
 * <p>
 * Herencia de entidades:
 * <ul>
 *   <li>Se utiliza la estrategia {@link InheritanceType#JOINED} mediante la anotación {@code @Inheritance}.</li>
 *   <li>Cada subclase de {@link Product} (por ejemplo, {@link ProductCli1} y {@link ProductCli2}) 
 *       tiene su propia tabla en la base de datos.</li>
 *   <li>Las tablas de subclases comparten la clave primaria con la tabla {@code products}.</li>
 *   <li>Se realizan JOINs entre la tabla base y la tabla de la subclase para recuperar los datos completos de un producto.</li>
 * </ul>
 * Esta estrategia permite mantener la base de datos normalizada y evita duplicación de columnas comunes.
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
@Inheritance(strategy = InheritanceType.JOINED)
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
    private long id; // Debe ser long para que no permita valores null

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

    /**
     * Categoría a la que pertenece el producto.
     * <p>
     * Relación Many-to-One con la entidad {@link Category}.
     * Puede ser {@code null} si el producto aún no está asignado a ninguna categoría.
     * </p>
     */
    @ManyToOne
    @JoinColumn(name="id_category", nullable = true)
    private Category category;
}
