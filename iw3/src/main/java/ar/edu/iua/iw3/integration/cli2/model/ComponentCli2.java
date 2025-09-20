package ar.edu.iua.iw3.integration.cli2.model;

import java.io.Serializable;

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
 * Entidad que representa un componente del sistema de integración CLI2.
 * <p>
 * Cada componente tiene un identificador único {@link #id} y un nombre
 * {@link #component} que no se puede repetir dentro de la base de datos.
 * </p>
 * <p>
 * Se persiste en la tabla <b>cli2_components</b>.
 * </p>
 */
@Entity
@Table(name = "cli2_components")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ComponentCli2 implements Serializable{

	private static final long serialVersionUID = 5695618110757822325L;

    /**
     * Identificador único del componente.
     * <p>
     * Se genera automáticamente con estrategia {@link GenerationType#IDENTITY}.
     * </p>
     */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

    /**
     * Nombre único del componente.
     * <p>
     * No puede exceder los 100 caracteres y debe ser único en la tabla.
     * </p>
     */
	@Column(length = 100, unique = true)
	private String component;
}