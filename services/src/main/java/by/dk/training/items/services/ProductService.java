package by.dk.training.items.services;

import java.util.List;

import javax.transaction.Transactional;

import by.dk.training.items.dataaccess.filters.ProductFilter;
import by.dk.training.items.datamodel.Product;

public interface ProductService {

	@Transactional
	void register(Product products);

	Product get(Long id);

	@Transactional
	void update(Product products);

	@Transactional
	void delete(Long id);
	
	List<Product> find(ProductFilter productsFilter);

	List<Product> getAll();
	
	Long count(ProductFilter filter);

}
