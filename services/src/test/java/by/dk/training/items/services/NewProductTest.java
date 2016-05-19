package by.dk.training.items.services;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import by.dk.training.items.dataaccess.filters.ProductFilter;
import by.dk.training.items.datamodel.Product;
import by.dk.training.items.datamodel.Type;
import by.dk.training.items.services.ProductService;
import by.dk.training.items.services.TypeService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:service-context-test.xml" })
public class NewProductTest {

	@Inject
	private ProductService productService;

	@Inject
	private TypeService typeService;

	@Test
	public void testProduct() {

		List<Product> allProducts = productService.getAll();
		for (Product pr : allProducts) {
			productService.delete(pr.getId());
		}

		List<Type> allTypes = typeService.getAll();
		for (Type tp : allTypes) {
			typeService.delete(tp.getId());
		}

		Type parentType = new Type();
		Type subType1 = new Type();
		Type subType2 = new Type();

		parentType.setTypeName("Электроника");
		subType1.setTypeName("Телевизор");
		subType2.setTypeName("Телефон");

		typeService.register(parentType);
		typeService.register(subType1);
		typeService.register(subType2);

		subType1.setParentType(parentType);
		typeService.update(subType1);

		Product product1 = new Product();

		product1.setLimit("2 шт/год");
		product1.setNameProduct("TVset");
		product1.setPriceProduct(new BigDecimal(1000000));
		product1.setStatus(true);
		productService.register(product1);
		product1.setTypes(subType1);
		productService.update(product1);

		Product product2 = new Product();

		product2.setLimit("2 шт/год");
		product2.setNameProduct("Ноутбук");
		product2.setPriceProduct(new BigDecimal(1000000));
		product2.setStatus(true);
		productService.register(product2);
		product2.setTypes(subType1);
		productService.update(product2);

		Product product3 = new Product();

		product3.setLimit("3 шт/год");
		product3.setNameProduct("Телефон");
		product3.setPriceProduct(new BigDecimal(1000000));
		product3.setStatus(true);
		productService.register(product3);
		product3.setTypes(subType2);
		productService.update(product3);
		
		
		ProductFilter pFilter = new ProductFilter();
		pFilter.setFetchType(true);
		pFilter.setTypes(parentType);
		List<Product> allProduct = productService.find(pFilter);

		Assert.assertEquals(allProduct.size(), 2);

		pFilter.setTypes(null);
		pFilter.setLimitProduct("3 шт/год");
		allProduct = productService.find(pFilter);

		Assert.assertEquals(allProduct.size(), 1);

		allProducts = productService.getAll();
		for (Product pr : allProducts) {
			productService.delete(pr.getId());
		}

		allTypes = typeService.getAll();
		for (Type tp : allTypes) {
			typeService.delete(tp.getId());
		}

	}
}
