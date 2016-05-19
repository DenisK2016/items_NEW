package by.dk.training.items.services;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import by.dk.training.items.dataaccess.UserProfileDao;
import by.dk.training.items.dataaccess.filters.PackageFilter;
import by.dk.training.items.dataaccess.filters.ProductFilter;
import by.dk.training.items.dataaccess.filters.RecipientFilter;
import by.dk.training.items.dataaccess.filters.TypeFilter;
import by.dk.training.items.dataaccess.filters.UserFilter;
import by.dk.training.items.dataaccess.impl.AbstractDaoImpl;
import by.dk.training.items.datamodel.Package;
import by.dk.training.items.datamodel.Product;
import by.dk.training.items.datamodel.Ranks;
import by.dk.training.items.datamodel.Recipient;
import by.dk.training.items.datamodel.StatusUser;
import by.dk.training.items.datamodel.Type;
import by.dk.training.items.datamodel.UserProfile;
import by.dk.training.items.services.PackageService;
import by.dk.training.items.services.ProductService;
import by.dk.training.items.services.RecipientService;
import by.dk.training.items.services.TypeService;
import by.dk.training.items.services.UserProfileService;
import by.dk.training.items.datamodel.UserCredentials;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:service-context-test.xml" })
public class UserServiceTest {

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
	public void test() {
		Assert.assertNotNull(userService);
	}

	@Test
	public void testEntityManagerInitialization()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = AbstractDaoImpl.class.getDeclaredField("entityManager");
		f.setAccessible(true);
		EntityManager em = (EntityManager) f.get(userDao);

		Assert.assertNotNull(em);
	}

	@Test
	public void testRegistration() {
		UserProfile profile = new UserProfile();
		UserCredentials userCredentials = new UserCredentials();

		profile.setLogin("Admin" + 13212);
		profile.setPassword("123456");

		userCredentials.setEmail(13212 + "@gmail.com");
		userCredentials.setFirstName("Alex");
		userCredentials.setLastName("Durov");
		userCredentials.setRank(Ranks.INSPECTOR_FIRST);
		userCredentials.setStatus(StatusUser.OFFICER);
		userService.register(profile, userCredentials);

		UserProfile registredProfile = userService.getUser(profile.getId());
		UserCredentials registredCredentials = userService.getUserCredentials(userCredentials.getId());

		Assert.assertNotNull(registredProfile);
		Assert.assertNotNull(registredCredentials);

		String updatedFName = "updatedName";
		userCredentials.setFirstName(updatedFName);
		userService.update(userCredentials);

		Assert.assertEquals(updatedFName, userService.getUserCredentials(profile.getId()).getFirstName());

		profile.setPassword("654321");
		userService.update(profile);

		UserProfile us = userService.getUser(profile.getId());
		Assert.assertEquals(us.getPassword(), profile.getPassword());

		userService.delete(profile.getId());

		Assert.assertNull(userService.getUser(profile.getId()));
		Assert.assertNull(userService.getUserCredentials(userCredentials.getId()));
	}

	@Test
	public void testNewRecipient()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

		Recipient recipient = new Recipient();

		recipient.setAddress("Горького 52");
		recipient.setCity("Grodno");
		recipient.setName("Иванов Иван Иванович");

		recipientService.register(recipient);

		Recipient recReg = recipientService.getRecipient(recipient.getId());

		Assert.assertNotNull(recReg);

		String updatableName = "Петров Иван Иванович";
		recipient.setName(updatableName);

		recipientService.update(recipient);

		Assert.assertEquals(updatableName, recipientService.getRecipient(recipient.getId()).getName());

		recipientService.delete(recipient.getId());

		Assert.assertNull(recipientService.getRecipient(recipient.getId()));
	}

	@Test
	public void testNewType()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

		Type parentType = new Type();
		Type subType = new Type();
		Type subSubType = new Type();

		parentType.setTypeName("Электроника");

		subType.setTypeName("Связь");
		subType.setParentType(parentType);

		subSubType.setTypeName("Телефон");
		subSubType.setParentType(subType);

		typesService.register(subSubType);

		Assert.assertNotNull(typesService.get(parentType.getId()));
		Assert.assertNotNull(typesService.get(subType.getId()));
		Assert.assertNotNull(typesService.get(subSubType.getId()));

		typesService.delete(subSubType.getId());
		typesService.delete(subType.getId());
		typesService.delete(parentType.getId());

		Assert.assertNull(typesService.get(subSubType.getId()));
		Assert.assertNull(typesService.get(subType.getId()));
		Assert.assertNull(typesService.get(parentType.getId()));
	}

	@Test
	public void testNewProduct()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

		Product product = new Product();

		product.setLimit("2 шт/год");
		product.setNameProduct("TVset");
		product.setPriceProduct(new BigDecimal(1000000));
		product.setStatus(true);

		productsService.register(product);

		Assert.assertNotNull(productsService.get(product.getId()));

		productsService.delete(product.getId());

		Assert.assertNull(productsService.get(product.getId()));
	}

	@Test
	public void testNewPackage()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Package pack = new Package();
		Recipient recipient = new Recipient();
		UserProfile profile = new UserProfile();
		UserCredentials userCredentials = new UserCredentials();
		UserProfile profile1 = new UserProfile();
		UserCredentials userCredentials1 = new UserCredentials();
		UserProfile profile2 = new UserProfile();
		UserCredentials userCredentials2 = new UserCredentials();

		profile.setLogin("Admin54651");
		profile.setPassword("123456");
		profile1.setLogin("Admin3462");
		profile1.setPassword("123456");
		profile2.setLogin("Admin6543");
		profile2.setPassword("123456");

		userCredentials.setEmail(System.currentTimeMillis() - 100 + "123456@gmail.com");
		userCredentials.setFirstName("Alex");
		userCredentials.setLastName("Durov");
		userCredentials.setRank(Ranks.INSPECTOR_FIRST);
		userCredentials.setStatus(StatusUser.OFFICER);
		userCredentials1.setEmail(System.currentTimeMillis() + 100 + "123456@gmail.com");
		userCredentials1.setFirstName("Alex");
		userCredentials1.setLastName("Durov");
		userCredentials1.setRank(Ranks.INSPECTOR_FIRST);
		userCredentials1.setStatus(StatusUser.OFFICER);
		userCredentials2.setEmail(System.currentTimeMillis() + "123456@gmail.com");
		userCredentials2.setFirstName("Alex");
		userCredentials2.setLastName("Durov");
		userCredentials2.setRank(Ranks.INSPECTOR_FIRST);
		userCredentials2.setStatus(StatusUser.OFFICER);
		userService.register(profile, userCredentials);
		userService.register(profile1, userCredentials1);
		userService.register(profile2, userCredentials2);

		recipient.setAddress("Горького 52");
		recipient.setCity("Grodno");
		recipient.setName("Иванов Иван Иванович");
		recipientService.register(recipient);

		pack.setCountrySender("China");
		pack.setDescription("DEscription3462346t54");
		pack.setFine(new BigDecimal(0));
		pack.setIdRecipient(recipient);
		pack.setIdUser(profile);
		pack.setPaid(false);
		pack.setPaymentDeadline(new SimpleDateFormat("dd MMMM yyyy").format(new Date(116, 07, 07)));
		pack.setId(System.currentTimeMillis());
		pack.setPrice(new BigDecimal(300000));
		pack.setWeight(12.00);

		packService.register(pack);

		Assert.assertNotNull(userService.getUser(profile.getId()));
		Assert.assertNotNull(recipientService.getRecipient(recipient.getId()));
		Assert.assertNotNull(packService.getPackage(pack.getId()));

		packService.delete(pack.getId());
		userService.delete(profile.getId());
		userService.delete(profile1.getId());
		userService.delete(profile2.getId());
		recipientService.delete(recipient.getId());

		Assert.assertNull(userService.getUser(profile.getId()));
		Assert.assertNull(recipientService.getRecipient(recipient.getId()));
		Assert.assertNull(packService.getPackage(pack.getId()));

	}

	@Test
	public void testUsersSelect() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException, InterruptedException {

		List<UserCredentials> allCred = new ArrayList<>();
		List<UserProfile> all = userService.getAll();
		for (UserProfile user : all) {
			userService.delete(user.getId());
		}

		UserProfile profile;
		UserCredentials userCredentials;

		for (int i = 0; i < 10; i++) {
			profile = new UserProfile();
			userCredentials = new UserCredentials();

			registerUser(profile, userCredentials, i);
		}

		all = userService.getAll();
		Assert.assertEquals(all.size(), 10);

		UserFilter filter = new UserFilter();
		filter.setFetchCredentials(true);
		filter.setFetchPackages(true);
		filter.setLimit(5);
		filter.setOffset(0);
		all = userService.find(filter);

		Assert.assertEquals(all.size(), 5);

		filter.setLogin("Admin4");
		all = userService.find(filter);

		Assert.assertEquals(all.size(), 1);
		
		filter.setLogin(null);
		filter.setEmail("4@gmail.com");
		all = userService.find(filter);
		
		System.out.println(all.size());
		Assert.assertEquals(all.size(), 1);

	}

	private void registerUser(UserProfile profile, UserCredentials userCredentials, int i) {

		profile = new UserProfile();
		userCredentials = new UserCredentials();

		profile.setLogin("Admin" + i);
		profile.setPassword("123456");

		userCredentials.setEmail(i + "@gmail.com");
		userCredentials.setFirstName("Alex");
		userCredentials.setLastName("Durov");
		userCredentials.setRank(Ranks.INSPECTOR_FIRST);
		userCredentials.setStatus(StatusUser.OFFICER);
		userService.register(profile, userCredentials);
	}

	@Test
	public void recipientSelect() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException, InterruptedException {

		List<Recipient> allRecipients = recipientService.getAll();
		for (Recipient r : allRecipients) {
			recipientService.delete(r.getId());
		}

		Recipient recipient;

		for (int i = 0; i < 10; i++) {
			recipient = new Recipient();

			createRecipient(recipient, i);
		}

		allRecipients = recipientService.getAll();

		Assert.assertEquals(allRecipients.size(), 10);

		RecipientFilter filter = new RecipientFilter();
		filter.setLimit(5);
		filter.setOffset(0);
		filter.setFetchPackages(true);
		allRecipients = recipientService.find(filter);

		Assert.assertEquals(allRecipients.size(), 5);

		filter.setName("Иванов Иван 6");
		allRecipients = recipientService.find(filter);

		Assert.assertEquals(allRecipients.size(), 1);

		filter.setName(null);
		filter.setCity("Гродно");
		allRecipients = recipientService.find(filter);

		Assert.assertEquals(allRecipients.size(), 5);
	}

	private void createRecipient(Recipient recipient, int i) {
		recipient.setAddress("Болдина " + i);
		recipient.setCity("Гродно");
		recipient.setName("Иванов Иван " + i);

		recipientService.register(recipient);
	}

	@Test
	public void typeAndProductSelect() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException, InterruptedException {

		List<Product> allProducts = productsService.getAll();
		for (Product pr : allProducts) {
			productsService.delete(pr.getId());
		}

		List<Type> allTypes = typesService.getAll();
		for (Type tp : allTypes) {
			typesService.delete(tp.getId());
		}

		Product product = new Product();
		Product product1 = new Product();
		Product product2 = new Product();
		Type type = new Type();
		Type type1 = new Type();
		Type type2 = new Type();

		type.setTypeName("Техника");
		typesService.register(type);

		type1.setTypeName("Аудио-видео");
		typesService.register(type1);
		type1.setParentType(type);
		typesService.update(type1);

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

		product1.setNameProduct("Телевизор");
		product1.setPriceProduct(new BigDecimal(500000));
		product1.setLimit("1 шт/год");
		product1.setStatus(true);
		productsService.register(product1);
		product1.setTypes(type);
		product1.setTypes(type1);
		productsService.update(product1);

		product2.setNameProduct("Принтер");
		product2.setPriceProduct(new BigDecimal(200000));
		product2.setLimit("1 шт/год");
		product2.setStatus(true);
		productsService.register(product2);
		product2.setTypes(type);
		product2.setTypes(type2);
		productsService.update(product2);

		allProducts = productsService.getAll();
		allTypes = typesService.getAll();

		Assert.assertEquals(allProducts.size(), 3);
		Assert.assertEquals(allTypes.size(), 3);

		ProductFilter pFilter = new ProductFilter();
		pFilter.setFetchType(true);
		pFilter.setLimit(2);
		pFilter.setOffset(0);
		allProducts = productsService.find(pFilter);

		Assert.assertEquals(allProducts.size(), 2);

		pFilter.setNameProduct("Принтер");
		allProducts = productsService.find(pFilter);

		Assert.assertEquals(allProducts.size(), 1);

		TypeFilter tFilter = new TypeFilter();
		tFilter.setFetchParentType(true);
		tFilter.setLimit(2);
		tFilter.setOffset(0);
		allTypes = typesService.find(tFilter);

		Assert.assertEquals(allTypes.size(), 2);

		tFilter.setTypeName("Компьютеры");
		allTypes = typesService.find(tFilter);

		Assert.assertEquals(allTypes.size(), 1);

	}

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

			user = new UserProfile();
			userCredentials = new UserCredentials();
			pack = new Package();
			recipient = new Recipient();

			user.setLogin("Login " + i);
			user.setPassword("123132");

			userCredentials.setEmail(i + "@mail.ru");
			userCredentials.setFirstName("FirstName " + i);
			userCredentials.setLastName("LastName " + i);
			userCredentials.setPost("Post");
			userCredentials.setRank(Ranks.INSPECTOR_SECOND);

			userService.register(user, userCredentials);

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

		allPack = packService.getAll();

		Assert.assertEquals(allPack.size(), 10);

		PackageFilter pFilter = new PackageFilter();
		pFilter.setCountrySender("China");
		allPack = packService.find(pFilter);

		Assert.assertEquals(allPack.size(), 10);

		pFilter.setCountrySender(null);
		pFilter.setDate(new Date(116, 3, 28));
		allPack = packService.find(pFilter);

		Assert.assertEquals(allPack.size(), 0);

		allPack = packService.getAll();
		for (Package p : allPack) {
			packService.delete(p.getId());
		}

		all = userService.getAll();
		for (UserProfile userr : all) {
			userService.delete(userr.getId());
		}

		allRecipients = recipientService.getAll();
		for (Recipient r : allRecipients) {
			recipientService.delete(r.getId());
		}

		allProducts = productsService.getAll();
		for (Product pr : allProducts) {
			productsService.delete(pr.getId());
		}

		allTypes = typesService.getAll();
		for (Type tp : allTypes) {
			typesService.delete(tp.getId());
		}
	}

}