package by.dk.training.items.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.dk.training.items.dataaccess.RecipientDao;
import by.dk.training.items.dataaccess.filters.RecipientFilter;
import by.dk.training.items.datamodel.Recipient;
import by.dk.training.items.service.RecipientService;

@Service
public class RecipientServiceImpl implements RecipientService {
	
	private static Logger LOGGER = LoggerFactory.getLogger(RecipientServiceImpl.class);
	@Inject
	private RecipientDao recipientDao;

	@Override
	public void register(Recipient recipient) {
		
		LOGGER.info("Recipient register: {}", recipient);
		
		recipientDao.insert(recipient);

	}

	@Override
	public Recipient getRecipient(Long id) {
		
		LOGGER.info("Recipient select: {}", recipientDao.get(id));
		
		return recipientDao.get(id);
	}

	@Override
	public void update(Recipient recipient) {
		
		LOGGER.info("Recipient update, new and old: {}", recipient, recipientDao.get(recipient.getId()));
		
		recipientDao.update(recipient);

	}

	@Override
	public void delete(Long id) {
		
		LOGGER.info("Recipient delete: {}", recipientDao.get(id));
		
		recipientDao.delete(id);

	}

	@Override
	public List<Recipient> find(RecipientFilter recipientFilter) {
		
		LOGGER.info("Recipient find by filter: {}", recipientFilter);
		
		return recipientDao.find(recipientFilter);
	}

	@Override
	public List<Recipient> getAll() {
		
		LOGGER.info("Recipient getAll: {}", "All recipients");
		
		return recipientDao.getAll();
	}

}
