package by.dk.training.items.dataaccess;

import java.util.Date;
import java.util.List;

import by.dk.training.items.dataaccess.filters.PackageFilter;
import by.dk.training.items.datamodel.Package;

public interface PackageDao extends AbstractDao<Package, Long> {

	Long count(PackageFilter filter);

	List<Package> find(PackageFilter filter);

	List<Package> betweenDates(Date startDate, Date endDate);

	Package maxPrice();

}
