package by.dk.training.items.services.impl;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.dk.training.items.dataaccess.ProductDao;
import by.dk.training.items.dataaccess.filters.ProductFilter;
import by.dk.training.items.datamodel.Product;
import by.dk.training.items.services.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	private static Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
	@Inject
	private ProductDao productDao;

	@Override
	public void register(Product products) {
		
		LOGGER.info("Product regirstred: {}", products);
		
		productDao.insert(products);

	}

	@Override
	public Product get(Long id) {
		
		LOGGER.info("Product select: {}", productDao.get(id));
		
		return productDao.get(id);
	}

	@Override
	public void update(Product products) {
		
		LOGGER.info("Product update, new and old: {}", products, productDao.get(products.getId()));
		
		productDao.update(products);

	}

	@Override
	public void delete(Long id) {
		
		LOGGER.info("Product delete: {}", productDao.get(id));
		
		productDao.delete(id);

	}

	@Override
	public List<Product> find(ProductFilter productsFilter) {
		
		LOGGER.info("Product find by filter: {}", productsFilter);
		
		return productDao.find(productsFilter);
	}

	@Override
	public List<Product> getAll() {
		
		LOGGER.info("Product getAll: {}", "All products");
		
		return productDao.getAll();
	}

	@Override
	public Long count(ProductFilter filter) {
		
		LOGGER.info("Product count: {}", filter);

		return productDao.count(filter);
	}

	
}
