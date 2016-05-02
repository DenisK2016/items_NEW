package by.dk.training.items.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.dk.training.items.dataaccess.UserCredentialsDao;
import by.dk.training.items.dataaccess.UserDao;
import by.dk.training.items.dataaccess.filters.UserFilter;
import by.dk.training.items.datamodel.StatusUser;
import by.dk.training.items.datamodel.User;
import by.dk.training.items.datamodel.UserCredentials;
import by.dk.training.items.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	private static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	@Inject
	private UserDao userDao;
	@Inject
	private UserCredentialsDao userCredentialsDao;

	@Override
	public void register(User user, UserCredentials userCredentials) {

		userCredentials.setCreated(new SimpleDateFormat("dd MMMM yyyy").format(new Date()));
		userCredentials.setStatus(StatusUser.OFFICER);
		userCredentials.setUser(user);
		user.setUserCredentials(userCredentials);

		userCredentialsDao.insert(userCredentials);
		userDao.insert(user);
		
		LOGGER.info("User regirstred: {}", user, userCredentials);
	}

	@Override
	public User getUser(Long id) {
		
		LOGGER.info("User select: {}", userDao.get(id));
		
		return userDao.get(id);

	}

	@Override
	public UserCredentials getUserCredentials(Long id) {
		
		LOGGER.info("UserCredential select: {}", userCredentialsDao.get(id));
		
		return userCredentialsDao.get(id);
	}

	@Override
	public void update(User user) {
		
		LOGGER.info("User update, new and old: {}", user, userDao.get(user.getId()));
		
		userDao.update(user);
	}

	@Override
	public void update(UserCredentials userCredentials) {
		
		LOGGER.info("UserCredentials update, new and old: {}", userCredentials, userCredentialsDao.get(userCredentials.getId()));
		
		userCredentialsDao.update(userCredentials);
	}

	@Override
	public void delete(Long id) {
		
		LOGGER.info("User delete: {}", userDao.get(id), userCredentialsDao.get(id));
		
		userDao.delete(id);
		userCredentialsDao.delete(id);

	}

	@Override
	public List<User> find(UserFilter userFilter) {
		
		LOGGER.info("User find by filter: {}", userFilter);

		return userDao.find(userFilter);
	}

	@Override
	public List<User> getAll() {
		
		LOGGER.info("User getAll: {}", "Alls users");
		
		return userDao.getAll();
	}

}
