package by.dk.training.items.services;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import by.dk.training.items.datamodel.UserCredentials;
import by.dk.training.items.datamodel.UserProfile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:service-context-test.xml" })
public class NewUserTest {

	@Inject
	private UserProfileService userService;

	@Test
//	@Transactional
	public void testUserFind() {

//		List<UserProfile> all = userService.getAll();
//		for (UserProfile user : all) {
//			userService.delete(user.getId());
//		}

//		for (int i = 0; i < 20; i++) {

			UserProfile user1 = new UserProfile();
			UserCredentials userCred1 = new UserCredentials();

			user1.setLogin("Admin");
			user1.setPassword("123456");

			userCred1.setEmail("admin@gmail.com");
			userCred1.setFirstName("jason");
			userCred1.setLastName("Statham");
			userService.register(user1, userCred1);
			
			UserProfile user2 = new UserProfile();
			UserCredentials userCred2 = new UserCredentials();
			user2.setLogin("Komandir");
			user2.setPassword("123456");

			userCred2.setEmail("komandir@gmail.com");
			userCred2.setFirstName("Dow");
			userCred2.setLastName("Jones");
			userService.register(user2, userCred2);
			
			UserProfile user3 = new UserProfile();
			UserCredentials userCred3 = new UserCredentials();
			user3.setLogin("User");
			user3.setPassword("123456");

			userCred3.setEmail("user@gmail.com");
			userCred3.setFirstName("Alexander");
			userCred3.setLastName("Pushkin");
			userService.register(user3, userCred3);
			
//		}

//		UserFilter userFilter = new UserFilter();
//		userFilter.setEmail("4@gmail.com");
//		userFilter.setLogin("Admin2");
//		userFilter.setFirstName("John3");
//		userFilter.setLastName("Statethem0");
////		userFilter.setFetchCredentials(true);
//		userFilter.setSortOrder(false);
//		userFilter.setSortProperty(UserProfile_.login);
////		userFilter.setFetchCredentials(true);
////		userFilter.setFetchPackages(true);
//		all = userService.find(userFilter);
//		for (UserProfile us : all) {
//			System.out.println(us);
//		}
//
//		Assert.assertEquals(all.size(), 4);

	}

}
