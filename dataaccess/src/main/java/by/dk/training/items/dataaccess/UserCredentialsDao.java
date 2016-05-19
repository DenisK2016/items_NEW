package by.dk.training.items.dataaccess;

import java.util.List;

import by.dk.training.items.dataaccess.filters.UserFilter;
import by.dk.training.items.datamodel.UserCredentials;
import by.dk.training.items.datamodel.UserProfile;

public interface UserCredentialsDao extends AbstractDao<UserCredentials, Long> {
	
	Long count(UserFilter filter);

	List<UserProfile> find(UserFilter filter);
	
}
