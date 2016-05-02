package by.dk.training.items.dataaccess;

import java.util.List;

import by.dk.training.items.dataaccess.filters.RecipientFilter;
import by.dk.training.items.datamodel.Recipient;

public interface RecipientDao extends AbstractDao<Recipient, Long> {
	
	List<Recipient> find(RecipientFilter filter);

}
