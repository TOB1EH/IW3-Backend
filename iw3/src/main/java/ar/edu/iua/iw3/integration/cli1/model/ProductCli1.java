package ar.edu.iua.iw3.integration.cli1.model;

import ar.edu.iua.iw3.model.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa un producto proveniente del sistema de integración CLI1.
 * <p>
 * Extiende de {@link Product}, reutilizando los atributos generales de un producto
 * y añadiendo un campo propio de CLI1: {@link #codCli1}.
 * </p>
 * <p>
 * Se persiste en la tabla <b>cli1_products</b>, utilizando una clave primaria compartida
 * con la tabla de productos base mediante la anotación {@link PrimaryKeyJoinColumn}.
 * </p>
 */
@Entity
@Table(name="cli1_products")
@PrimaryKeyJoinColumn(name="id_product")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductCli1 extends Product{
    /**
     * Código único del producto en el sistema CLI1.
     * <p>
     * Este campo es obligatorio ({@code nullable = false}) y no puede repetirse
     * ({@code unique = true}).
     * </p>
     */
    @Column(nullable = false, unique = true)
    private String codCli1;
}
