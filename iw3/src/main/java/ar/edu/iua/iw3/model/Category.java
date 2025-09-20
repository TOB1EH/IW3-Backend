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
 * Entidad que representa una categoría de productos en el sistema.
 * <p>
 * Cada categoría tiene un identificador único {@link #id} y un nombre {@link #category} único.
 * Se persiste en la tabla <b>categories</b>.
 * </p>
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {
    /**
     * Identificador único de la categoría.
     * <p>
     * Se genera automáticamente mediante estrategia {@link GenerationType#IDENTITY}.
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Nombre único de la categoría.
     * <p>
     * No puede exceder los 100 caracteres y debe ser único en la tabla.
     * </p>
     */
    @Column(length = 100, unique = true)
    private String category;
    
}
