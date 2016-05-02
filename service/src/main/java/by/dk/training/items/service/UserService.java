package by.dk.training.items.service;

import java.util.List;

import javax.transaction.Transactional;

import by.dk.training.items.dataaccess.filters.UserFilter;
import by.dk.training.items.datamodel.User;
import by.dk.training.items.datamodel.UserCredentials;

public interface UserService {

	@Transactional
	void register(User user, UserCredentials userCredentials);

	User getUser(Long id);

	UserCredentials getUserCredentials(Long id);

	@Transactional
	void update(User user);

	@Transactional
	void update(UserCredentials userCredentials);

	@Transactional
	void delete(Long id);

	List<User> find(UserFilter userFilter);

	List<User> getAll();

}
