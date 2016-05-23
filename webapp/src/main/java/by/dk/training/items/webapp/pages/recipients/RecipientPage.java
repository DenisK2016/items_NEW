package by.dk.training.items.webapp.pages.recipients;

import org.apache.wicket.markup.html.link.Link;

import by.dk.training.items.webapp.pages.AbstractPage;
import by.dk.training.items.webapp.pages.home.HomePage;
import by.dk.training.items.webapp.pages.recipients.formforreg.RecipientRegPage;
import by.dk.training.items.webapp.pages.recipients.panelforrecipients.ListRecipientsPanel;

public class RecipientPage extends AbstractPage {

	public RecipientPage() {
		super();

	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(new Link<HomePage>("Back") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new HomePage());
			}
		});

		add(new ListRecipientsPanel("list-panel"));

		add(new Link<RecipientRegPage>("linkToReg") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new RecipientRegPage());

			}
		});
	}
}
