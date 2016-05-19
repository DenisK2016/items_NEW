package by.dk.training.items.dataaccess;

import java.util.List;

import by.dk.training.items.dataaccess.filters.ProductFilter;
import by.dk.training.items.datamodel.Product;

public interface ProductDao extends AbstractDao<Product, Long> {
	
	Long count(ProductFilter filter);
	
	List<Product> find(ProductFilter filter);
}
