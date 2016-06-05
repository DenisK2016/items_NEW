package by.dk.training.items.services;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import by.dk.training.items.datamodel.Type;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:service-context-test.xml" })
public class NewTypeTest {

	@Inject
	private TypeService typeService;
	@Inject
	private UserProfileService userProfileService;

	@Test
	public void testType() {

		// List<Type> allTypes = typeService.getAll();
		// for (Type tp : allTypes) {
		// typeService.delete(tp.getId());
		// }

		Type subType1 = new Type();
		Type subType2 = new Type();
		Type subType3 = new Type();
		Type subType4 = new Type();
		Type subType5 = new Type();
		Type subType6 = new Type();
		Type subType7 = new Type();
		Type subType8 = new Type();
		Type subType9 = new Type();
		Type subType10 = new Type();
		
		subType1.setIdUser(userProfileService.getUser(1L));
		subType2.setIdUser(userProfileService.getUser(1L));
		subType3.setIdUser(userProfileService.getUser(1L));
		subType4.setIdUser(userProfileService.getUser(1L));
		subType5.setIdUser(userProfileService.getUser(1L));
		subType6.setIdUser(userProfileService.getUser(1L));
		subType7.setIdUser(userProfileService.getUser(1L));
		subType8.setIdUser(userProfileService.getUser(1L));
		subType9.setIdUser(userProfileService.getUser(1L));
		subType10.setIdUser(userProfileService.getUser(1L));

		subType1.setTypeName("Электроника");
		subType2.setTypeName("Телевизор");
		subType3.setTypeName("Телефон");
		subType4.setTypeName("Компьютер");
		subType5.setTypeName("Мобильный телефон");
		subType6.setTypeName("Ноутбук");
		subType7.setTypeName("Мебель");
		subType8.setTypeName("Шкаф");
		subType9.setTypeName("Стол");
		subType10.setTypeName("Одежда");

		subType2.setParentType(subType1);
		subType3.setParentType(subType1);
		subType4.setParentType(subType1);
		subType5.setParentType(subType3);
		subType6.setParentType(subType4);
		subType8.setParentType(subType7);
		subType9.setParentType(subType7);

		typeService.register(subType1);
		typeService.register(subType2);
		typeService.register(subType3);
		typeService.register(subType4);
		typeService.register(subType5);
		typeService.register(subType6);
		typeService.register(subType7);
		typeService.register(subType8);
		typeService.register(subType9);
		typeService.register(subType10);
		
		Type type = new Type();
		type.setIdUser(userProfileService.getUser(1L));
		type.setTypeName("Другие");
		typeService.register(type);

	}

	// TypeFilter tFilter = new TypeFilter();
	// tFilter.setFetchParentType(true);
	// tFilter.setParentType(parentType);
	//
	// allTypes = typeService.find(tFilter);
	// for(Type t : allTypes){
	// System.out.println(t);
	// }

	// }
}
