package ar.edu.iua.iw3.model;

// Anotaciones para la persistencia con JPA (Jakarta Persistence API)
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Anotaciones de Lombok para generar automáticamente constructores, getters y setters
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa un producto en la base de datos.
 */
@Entity // Indica que esta clase es una entidad JPA (se mapea a una tabla de la BD)
@Table(name = "products") // Especifica el nombre de la tabla en la BD
@AllArgsConstructor // Genera un constructor con todos los campos
@NoArgsConstructor // Genera un constructor sin argumentos
@Getter // Genera automáticamente los métodos getter para todos los campos
@Setter // Genera automáticamente los métodos setter para todos los campos
public class Product {

    @Id // Indica que este campo es la clave primaria de la entidad
    @GeneratedValue(strategy = GenerationType.IDENTITY) // El valor se genera automáticamente (autoincremental)
    private long id;

    @Column(length = 100, unique = true) // Columna con máximo 100 caracteres y valor único
    private String product;

    @Column(columnDefinition = "tinyint default 0") // Columna tipo tinyint en la BD, valor por defecto 0
    private boolean stock = false;

    private double precio;

}
