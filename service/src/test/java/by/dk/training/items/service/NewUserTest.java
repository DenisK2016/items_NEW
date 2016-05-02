package by.dk.training.items.service;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import by.dk.training.items.dataaccess.filters.UserFilter;
import by.dk.training.items.datamodel.User;
import by.dk.training.items.datamodel.UserCredentials;
import by.dk.training.items.datamodel.User_;
import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:service-context-test.xml" })
public class NewUserTest {

	@Inject
	private UserService userService;

	@Test
	public void testUserFind() {

		List<User> all = userService.getAll();
		for (User user : all) {
			userService.delete(user.getId());
		}

		for (int i = 0; i < 5; i++) {

			User user = new User();
			UserCredentials userCred = new UserCredentials();

			user.setLogin("Admin" + i);
			user.setPassword("2345345");

			userCred.setEmail(i + "@gmail.com");
			userCred.setFirstName("John" + i);
			userCred.setLastName("Statethem" + i);

			userService.register(user, userCred);
		}

		UserFilter userFilter = new UserFilter();
		userFilter.setEmail("4@gmail.com");
		userFilter.setLogin("Admin2");
		userFilter.setFirstName("John3");
		userFilter.setLastName("Statethem0");
		userFilter.setSortOrder(false);
		userFilter.setSortProperty(User_.id);
		userFilter.setFetchCredentials(true);
		userFilter.setFetchPackages(true);
		all = userService.find(userFilter);
		for (User us : all) {
			System.out.println(us);
		}

		Assert.assertEquals(all.size(), 4);

	}

}
