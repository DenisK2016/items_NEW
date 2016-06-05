package by.dk.training.items.dataaccess;

import java.util.List;

import by.dk.training.items.dataaccess.filters.UserFilter;
import by.dk.training.items.datamodel.UserProfile;

public interface UserProfileDao extends AbstractDao<UserProfile, Long> {
	
	Long count(UserFilter filter);

	List<UserProfile> find(UserFilter filter);
	
	UserProfile find(String login, String password);
}
