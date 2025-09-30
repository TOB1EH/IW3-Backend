package ar.edu.iua.iw3.integration.cli2.model;

import ar.edu.iua.iw3.model.Category;

public interface ProductCli2SlimView {

	Long getId();
	String getProduct();
	Double getPrecio();
	
	Category getCategory();
	interface Categrory {
		String getCategory();
	}
}