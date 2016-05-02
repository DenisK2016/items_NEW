package by.dk.training.items.service.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.dk.training.items.dataaccess.PackageDao;
import by.dk.training.items.dataaccess.RecipientDao;
import by.dk.training.items.dataaccess.UserDao;
import by.dk.training.items.dataaccess.filters.PackageFilter;
import by.dk.training.items.datamodel.Package;
import by.dk.training.items.service.PackageService;

@Service
public class PackageServiceImpl implements PackageService {

	private static Logger LOGGER = LoggerFactory.getLogger(PackageServiceImpl.class);
	@Inject
	private PackageDao packDao;
	
	@Inject
	private UserDao userDao;
	
	@Inject
	private RecipientDao recipientDao;

	@Override
	public void register(Package pack) {
		
		pack.setDate(new Date());
		packDao.insert(pack);
		recipientDao.update(pack.getIdRecipient());
		userDao.update(pack.getIdUser());	
		
		LOGGER.info("Package regirstred: {}", pack);

	}

	@Override
	public Package getPackage(Long id) {
		
		LOGGER.info("Package select: {}", packDao.get(id));
		
		return packDao.get(id);
	}

	@Override
	public void update(Package pack) {
		
		LOGGER.info("Package update, new and old: {}", pack, packDao.get(pack.getId()));
		
		this.packDao.update(pack);

	}

	@Override
	public void delete(Long id) {
		
		LOGGER.info("Package delete: {}", packDao.get(id));
		
		packDao.delete(id);

	}

	@Override
	public List<Package> find(PackageFilter packageFilter) {
		
		LOGGER.info("Package find by filter: {}", packageFilter);
		
		return packDao.find(packageFilter);
	}

	@Override
	public List<Package> getAll() {
		
		LOGGER.info("Package getAll: {}", "All packages");
		
		return packDao.getAll();
	}

}
