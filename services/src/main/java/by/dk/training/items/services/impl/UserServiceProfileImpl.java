package by.dk.training.items.services.impl;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.dk.training.items.dataaccess.UserCredentialsDao;
import by.dk.training.items.dataaccess.UserProfileDao;
import by.dk.training.items.dataaccess.filters.UserFilter;
import by.dk.training.items.datamodel.StatusUser;
import by.dk.training.items.datamodel.UserCredentials;
import by.dk.training.items.datamodel.UserProfile;
import by.dk.training.items.services.UserProfileService;

@Service
public class UserServiceProfileImpl implements UserProfileService {

	private static Logger LOGGER = LoggerFactory.getLogger(UserServiceProfileImpl.class);
	@Inject
	private UserProfileDao userDao;
	@Inject
	private UserCredentialsDao userCredentialsDao;

	@Override
	public void register(UserProfile user, UserCredentials userCredentials) {

		Date d = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		d.setTime(cal.getTime().getTime());
		userCredentials.setCreated(d);
		userCredentials.setStatus(StatusUser.OFFICER);
		userCredentials.setUser(user);
		user.setUserCredentials(userCredentials);

		userCredentialsDao.insert(userCredentials);
		userDao.insert(user);

		LOGGER.info("User regirstred: {}", user, userCredentials);
	}

	@Override
	public UserProfile getUser(Long id) {

		LOGGER.info("User select: {}", userDao.get(id));

		return userDao.get(id);

	}

	@Override
	public UserCredentials getUserCredentials(Long id) {

		LOGGER.info("UserCredential select: {}", userCredentialsDao.get(id));

		return userCredentialsDao.get(id);
	}

	@Override
	public void update(UserProfile user) {

		LOGGER.info("User update, new and old: {}", user, userDao.get(user.getId()));

		userDao.update(user);
	}

	@Override
	public void update(UserCredentials userCredentials) {

		LOGGER.info("UserCredentials update, new and old: {}", userCredentials,
				userCredentialsDao.get(userCredentials.getId()));

		Date d = userCredentials.getCreated();
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		d.setTime(cal.getTime().getTime());
		userCredentials.setCreated(d);

		userCredentialsDao.update(userCredentials);
	}

	@Override
	public void delete(Long id) {

		LOGGER.info("User delete: {}", userDao.get(id), userCredentialsDao.get(id));

		userDao.delete(id);
		userCredentialsDao.delete(id);

	}

	@Override
	public List<UserProfile> find(UserFilter userFilter) {

		LOGGER.info("User find by filter: {}", userFilter);
		return userDao.find(userFilter);
	}

	@Override
	public List<UserProfile> getAll() {

		LOGGER.info("User getAll: {}", "Alls users");

		return userDao.getAll();
	}

	@Override
	public Long count(UserFilter filter) {

		LOGGER.info("User count(): {}", filter);

		return userDao.count(filter);
	}

	@Override
	public UserProfile getByNameAndPassword(String login, String password) {

		return userDao.find(login, password);
	}

	@Override
	public Collection<? extends String> resolveRoles(Long id) {
		UserCredentials userCredentials = userCredentialsDao.get(id);
		return Collections.singletonList(userCredentials.getStatus().name());
	}

}
