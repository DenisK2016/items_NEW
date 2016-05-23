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

		for (int i = 0; i < 20; i++) {

			UserProfile user = new UserProfile();
			UserCredentials userCred = new UserCredentials();

			user.setLogin("Admin" + i);
			user.setPassword("2345345");

			userCred.setEmail(i + "@gmail.com");
			userCred.setFirstName("John" + i);
			userCred.setLastName("Statethem" + i);

			userService.register(user, userCred);
			
		}

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
