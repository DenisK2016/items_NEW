package by.dk.training.items.services;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import by.dk.training.items.datamodel.Product;
import by.dk.training.items.datamodel.Recipient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:service-context-test.xml" })
public class DeleteProductTest {

	@Inject
	private RecipientService recipientService;
	@Inject
	private ProductService productService;

	@Test
	public void testProduct() {
		for (int i = 0; i < 20; i++) {
			Recipient recipient = new Recipient();

			recipient.setAddress("Болдина " + i);
			recipient.setCity("Гродно" + i);
			recipient.setName("Иванов Иван " + i);

			recipientService.register(recipient);

			Product product1 = new Product();

			product1.setWeight(233.0);
			product1.setNameProduct("TVset" + i);
			product1.setPriceProduct(new BigDecimal(1000000));
			product1.setStatus(true);
			productService.register(product1);

		}
	}
}
