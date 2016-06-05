package by.dk.training.items.services;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import by.dk.training.items.dataaccess.filters.UserFilter;
import by.dk.training.items.datamodel.UserCredentials;
import by.dk.training.items.datamodel.UserProfile;

public interface UserProfileService {

	@Transactional
	void register(UserProfile user, UserCredentials userCredentials);

	UserProfile getUser(Long id);

	UserCredentials getUserCredentials(Long id);

	@Transactional
	void update(UserProfile user);

	@Transactional
	void update(UserCredentials userCredentials);

	@Transactional
	void delete(Long id);

	List<UserProfile> find(UserFilter userFilter);

	List<UserProfile> getAll();

	Long count(UserFilter filter);

	UserProfile getByNameAndPassword(String login, String password);
	
	Collection<? extends String> resolveRoles(Long id);

}
