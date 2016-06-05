package by.dk.training.items.services;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import by.dk.training.items.dataaccess.filters.RecipientFilter;
import by.dk.training.items.datamodel.Recipient;
import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:service-context-test.xml" })
public class NewRecipientTest {
	
	@Inject
	private RecipientService recipientService;

	@Test
	public void restRecipient() {

		List<Recipient> allRecipients = recipientService.getAll();
		for (Recipient r : allRecipients) {
			recipientService.delete(r.getId());
		}

		Recipient recipient;

		for (int i = 0; i < 100; i++) {
			recipient = new Recipient();

			createRecipient(recipient, i);
		}

		RecipientFilter recipientFilter = new RecipientFilter();
		recipientFilter.setCity("Гродно4");
		recipientFilter.setAddress("Болдина 3");
		recipientFilter.setFetchPackages(true);
		allRecipients = recipientService.find(recipientFilter);
		
		Assert.assertEquals(allRecipients.size(), 2);
		System.out.println(allRecipients);
	}

	private void createRecipient(Recipient recipient, int i) {
		recipient.setAddress("Болдина " + i);
		recipient.setCity("Гродно" + i);
		recipient.setName("Иванов Иван " + i);

		recipientService.register(recipient);
	}
}
