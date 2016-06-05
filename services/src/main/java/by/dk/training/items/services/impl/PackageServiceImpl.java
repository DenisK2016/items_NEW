package by.dk.training.items.services.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import by.dk.training.items.dataaccess.PackageDao;
import by.dk.training.items.dataaccess.RecipientDao;
import by.dk.training.items.dataaccess.UserProfileDao;
import by.dk.training.items.dataaccess.filters.PackageFilter;
import by.dk.training.items.datamodel.Package;
import by.dk.training.items.services.PackageService;

@Service
public class PackageServiceImpl implements PackageService {

	private static Logger LOGGER = LoggerFactory.getLogger(PackageServiceImpl.class);
	@Inject
	private PackageDao packDao;

	@Inject
	private UserProfileDao userDao;

	@Inject
	private RecipientDao recipientDao;

	@Override
	public void register(Package pack) {

		Date d = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		d.setTime(cal.getTime().getTime());
		pack.setDate(d);
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

		Date d = pack.getDate();
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		d.setTime(cal.getTime().getTime());
		pack.setDate(d);

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

	@Override
	public Long count(PackageFilter packagefilter) {

		LOGGER.info("Package count(): {}", packagefilter);

		return packDao.count(packagefilter);
	}

	@Override
	public List<Package> betweenDates(Date startDate, Date endDate) {

		return packDao.betweenDates(startDate, endDate);
	}

	@Override
	public Package maxPrice() {
		if (getAll().isEmpty()) {
			return new Package();
		}
		return packDao.maxPrice();
	}

}
