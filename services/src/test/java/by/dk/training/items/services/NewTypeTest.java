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

	@Test
	public void testType() {

		// List<Type> allTypes = typeService.getAll();
		// for (Type tp : allTypes) {
		// typeService.delete(tp.getId());
		// }

		for (int i = 0; i < 30; i++) {
			Type parentType = new Type();
			Type subType1 = new Type();
			Type subType2 = new Type();

			parentType.setTypeName("Электроника" + i);
			subType1.setTypeName("Телевизор" + i);
			subType2.setTypeName("Телефон" + i);

			typeService.register(parentType);
			typeService.register(subType1);
			typeService.register(subType2);

			subType1.setParentType(parentType);
			typeService.update(subType1);
			subType2.setParentType(parentType);
			typeService.update(subType2);
		}

		// TypeFilter tFilter = new TypeFilter();
		// tFilter.setFetchParentType(true);
		// tFilter.setParentType(parentType);
		//
		// allTypes = typeService.find(tFilter);
		// for(Type t : allTypes){
		// System.out.println(t);
		// }

	}
}
