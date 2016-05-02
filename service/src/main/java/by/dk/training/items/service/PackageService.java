package by.dk.training.items.service;

import java.util.List;

import javax.transaction.Transactional;

import by.dk.training.items.dataaccess.filters.PackageFilter;
import by.dk.training.items.datamodel.Package;

public interface PackageService {

	@Transactional
	void register(Package pack);

	Package getPackage(Long id);
	
	@Transactional
	void update(Package pack);

	@Transactional
	void delete(Long trackingCode);
	
	List<Package> find(PackageFilter packageFilter);
	
	List<Package> getAll();
}
