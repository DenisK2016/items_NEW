package by.dk.training.items.services;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import by.dk.training.items.dataaccess.filters.TypeFilter;
import by.dk.training.items.datamodel.Product;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:service-context-test.xml" })
public class NewProductTest {

	@Inject
	private ProductService productService;

	@Inject
	private TypeService typeService;
	
	private TypeFilter typeFilter;

	@Test
	public void testProduct() {

		// List<Product> allProducts = productService.getAll();
		// for (Product pr : allProducts) {
		// productService.delete(pr.getId());
		// }
		//
		// List<Type> allTypes = typeService.getAll();
		// for (Type tp : allTypes) {
		// typeService.delete(tp.getId());
		// }

		// Type parentType = new Type();
		// Type subType1 = new Type();
		// Type subType2 = new Type();
		//
		// parentType.setTypeName("Электроника");
		// subType1.setTypeName("Телевизор");
		// subType2.setTypeName("Телефон");
		//
		// typeService.register(parentType);
		// typeService.register(subType1);
		// typeService.register(subType2);
		//
		// subType1.setParentType(parentType);
		// typeService.update(subType1);

		Product product1 = new Product();

		product1.setWeight(4.0);
		product1.setNameProduct("Horizont 3232");
		product1.setPriceProduct(new BigDecimal(3000000));
		product1.setStatus(true);
		productService.register(product1);

		Product product2 = new Product();

		product2.setWeight(4.0);
		product2.setNameProduct("Lenovo Z510");
		product2.setPriceProduct(new BigDecimal(1000000));
		product2.setStatus(true);

		productService.register(product2);

		Product product3 = new Product();

		product3.setWeight(4.0);
		product3.setNameProduct("IPhone 6S");
		product3.setPriceProduct(new BigDecimal(1000000));
		product3.setStatus(true);
		productService.register(product3);

		Product product;
		for (int i = 0; i < 100; i++) {
			
			product = new Product();

			product.setWeight(4.0);
			product.setNameProduct("Продукт "+i);
			product.setPriceProduct(new BigDecimal(3000000));
			product.setStatus(true);
			productService.register(product);
		}

		// ProductFilter pFilter = new ProductFilter();
		// pFilter.setFetchType(true);
		// pFilter.setTypes(parentType);
		// List<Product> allProduct = productService.find(pFilter);
		//
		// Assert.assertEquals(allProduct.size(), 2);
		//
		// pFilter.setTypes(null);
		// pFilter.setLimitProduct("3 шт/год");
		// allProduct = productService.find(pFilter);
		//
		// Assert.assertEquals(allProduct.size(), 1);
		//
		// allProducts = productService.getAll();
		// for (Product pr : allProducts) {
		// productService.delete(pr.getId());
		// }
		//
		// allTypes = typeService.getAll();
		// for (Type tp : allTypes) {
		// typeService.delete(tp.getId());
		// }

	}
}
