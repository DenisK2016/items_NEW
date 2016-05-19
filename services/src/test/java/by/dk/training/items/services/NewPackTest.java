package by.dk.training.items.services;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import by.dk.training.items.dataaccess.UserProfileDao;
import by.dk.training.items.dataaccess.filters.PackageFilter;
import by.dk.training.items.datamodel.Package;
import by.dk.training.items.datamodel.Product;
import by.dk.training.items.datamodel.Ranks;
import by.dk.training.items.datamodel.Recipient;
import by.dk.training.items.datamodel.StatusUser;
import by.dk.training.items.datamodel.Type;
import by.dk.training.items.datamodel.UserCredentials;
import by.dk.training.items.datamodel.UserProfile;
import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:service-context-test.xml" })
public class NewPackTest {

	@Inject
	private UserProfileService userService;

	@Inject
	private UserProfileDao userDao;

	@Inject
	private RecipientService recipientService;

	@Inject
	private TypeService typesService;

	@Inject
	private ProductService productsService;

	@Inject
	private PackageService packService;

	@Test
	public void packagesSelect() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException, InterruptedException {

		List<Package> allPack = packService.getAll();
		for (Package p : allPack) {
			packService.delete(p.getId());
		}

		List<UserProfile> all = userService.getAll();
		for (UserProfile user : all) {
			userService.delete(user.getId());
		}

		List<Recipient> allRecipients = recipientService.getAll();
		for (Recipient r : allRecipients) {
			recipientService.delete(r.getId());
		}

		List<Product> allProducts = productsService.getAll();
		for (Product pr : allProducts) {
			productsService.delete(pr.getId());
		}

		List<Type> allTypes = typesService.getAll();
		for (Type tp : allTypes) {
			typesService.delete(tp.getId());
		}

		UserProfile user;
		UserCredentials userCredentials;
		Package pack;
		Recipient recipient;
		Product product;
		Product product1;
		Product product2;
		Type type;
		Type type1;
		Type type2;

		user = new UserProfile();
		userCredentials = new UserCredentials();

		user.setLogin("Login" + 1);
		user.setPassword("123132");

		userCredentials.setEmail(1 + "@mail.ru");
		userCredentials.setFirstName("FirstName");
		userCredentials.setLastName("LastName");
		userCredentials.setPost("Post");
		userCredentials.setRank(Ranks.INSPECTOR_SECOND);

		userService.register(user, userCredentials);

		product = new Product();
		type = new Type();

		type.setTypeName("Техника");
		typesService.register(type);

		type1 = new Type();
		type1.setTypeName("Аудио-видео");
		typesService.register(type1);
		type1.setParentType(type);
		typesService.update(type1);

		type2 = new Type();
		type2.setTypeName("Компьютеры");
		typesService.register(type2);
		type2.setParentType(type);
		typesService.update(type2);

		product.setNameProduct("Ноутбук");
		product.setPriceProduct(new BigDecimal(300000));
		product.setLimit("1 шт/год");
		product.setStatus(true);
		productsService.register(product);
		product.setTypes(type);
		product.setTypes(type2);
		productsService.update(product);

		product1 = new Product();
		product1.setNameProduct("Телевизор");
		product1.setPriceProduct(new BigDecimal(500000));
		product1.setLimit("1 шт/год");
		product1.setStatus(true);
		productsService.register(product1);
		product1.setTypes(type);
		product1.setTypes(type1);
		productsService.update(product1);

		product2 = new Product();
		product2.setNameProduct("Принтер");
		product2.setPriceProduct(new BigDecimal(200000));
		product2.setLimit("1 шт/год");
		product2.setStatus(true);
		productsService.register(product2);
		product2.setTypes(type);
		product2.setTypes(type2);
		productsService.update(product2);

		for (int i = 0; i < 10; i++) {

			pack = new Package();
			recipient = new Recipient();

			recipient.setAddress("Болдина " + i);
			recipient.setCity("Гродно");
			recipient.setName("Иванов Иван " + i);

			recipientService.register(recipient);

			pack.setCountrySender("China");
			pack.setDescription("description" + i);
			pack.setIdRecipient(recipient);
			pack.setFine(new BigDecimal(0));
			pack.setIdUser(user);
			pack.setPaid(false);
			pack.setPaymentDeadline(i + " май 2016");
			pack.setPrice(new BigDecimal(2000000));
			pack.setId(System.currentTimeMillis() + i);
			pack.setWeight(2.0);

			packService.register(pack);

			List<Product> products = productsService.getAll();
			pack.setProducts(products);

			packService.update(pack);

		}

		UserProfile user1 = new UserProfile();
		UserCredentials userCred = new UserCredentials();

		user1.setLogin("Admi");
		user1.setPassword("2345345");

		userCred.setEmail("@gmail.com");
		userCred.setFirstName("John");
		userCred.setLastName("Statethem");
		userCred.setStatus(StatusUser.COMMANDER);

		userService.register(user1, userCred);

		PackageFilter pFilter = new PackageFilter();
		pFilter.setUser(user);
		pFilter.setFetchProduct(true);
		pFilter.setFetchRecipient(true);
		pFilter.setFetchUser(true);

		allPack = packService.find(pFilter);

		Assert.assertEquals(allPack.size(), 10);

		pFilter.setUser(null);
		pFilter.setProduct(null);
		pFilter.setDescription("description4");

		allPack = packService.find(pFilter);
		
		Assert.assertEquals(allPack.size(), 1);
		
		pFilter.setDescription(null);
		pFilter.setLimit(5);
		pFilter.setOffset(0);
		
		allPack = packService.find(pFilter);
		
		Assert.assertEquals(allPack.size(), 5);
		
//		allPack = packService.getAll();
//		for (Package p : allPack) {
//			packService.delete(p.getId());
//		}
//
//		all = userService.getAll();
//		for (UserProfile user2 : all) {
//			userService.delete(user2.getId());
//		}
//
//		allRecipients = recipientService.getAll();
//		for (Recipient r : allRecipients) {
//			recipientService.delete(r.getId());
//		}
//
//		allProducts = productsService.getAll();
//		for (Product pr : allProducts) {
//			productsService.delete(pr.getId());
//		}
//
//		allTypes = typesService.getAll();
//		for (Type tp : allTypes) {
//			typesService.delete(tp.getId());
//		}

	}

}
