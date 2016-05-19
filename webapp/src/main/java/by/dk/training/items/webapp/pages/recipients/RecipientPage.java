package by.dk.training.items.webapp.pages.recipients;

import org.apache.wicket.markup.html.link.Link;

import by.dk.training.items.webapp.pages.AbstractPage;
import by.dk.training.items.webapp.pages.home.HomePage;
import by.dk.training.items.webapp.pages.recipients.panelforrecipients.ListRecipientsPanel;

public class RecipientPage extends AbstractPage {

	public RecipientPage() {
		super();
		add(new Link("Back") {
			@Override
			public void onClick() {
				setResponsePage(new HomePage());
			}
		});
		
		add(new ListRecipientsPanel("list-panel"));

	}
}
