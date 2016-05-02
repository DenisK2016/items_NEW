package by.dk.training.items.dataaccess;

import java.util.List;

import by.dk.training.items.dataaccess.filters.UserFilter;
import by.dk.training.items.datamodel.User;

public interface UserDao extends AbstractDao<User, Long> {

	List<User> find(UserFilter filter);
}
