package ar.edu.iua.iw3.integration.cli2.model;
import java.util.Date;
import java.util.Set;

import ar.edu.iua.iw3.model.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa un producto proveniente del sistema de integración CLI2.
 * <p>
 * Extiende de {@link Product}, reutilizando los atributos generales de un producto
 * y añadiendo información específica de CLI2, como fecha de vencimiento y componentes asociados.
 * </p>
 * <p>
 * Se persiste en la tabla <b>cli2_products</b>, utilizando la clave primaria
 * compartida con la tabla de productos base mediante {@link PrimaryKeyJoinColumn}.
 * </p>
 */
@Entity
@Table(name = "cli2_products")
@PrimaryKeyJoinColumn(name = "id_product")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductCli2 extends Product{

	private static final long serialVersionUID = 2516446617276638458L;

	/**
     * Fecha de vencimiento del producto.
     * <p>
     * Este campo es obligatorio y se almacena como tipo DATETIME.
     * </p>
     */
	@Column(columnDefinition = "DATETIME NOT NULL")
	private Date expirationDate;
	
	/**
     * Conjunto de componentes asociados al producto.
     * <p>
     * Relación Many-to-Many con la entidad {@link ComponentCli2}.
     * La información se carga de manera EAGER y se persiste en la tabla intermedia
     * <b>cli2_product_component</b>.
     * </p>
     */
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "cli2_product_component", 
		joinColumns = { @JoinColumn(name = "id_product", referencedColumnName = "id_product") }, 
		inverseJoinColumns = {	@JoinColumn(name = "id_component", referencedColumnName = "id") })
	private Set<ComponentCli2> components;

}