package by.dk.training.items.webapp.pages.users;

import org.apache.wicket.markup.html.link.Link;

import by.dk.training.items.webapp.pages.AbstractPage;
import by.dk.training.items.webapp.pages.home.HomePage;
import by.dk.training.items.webapp.pages.users.panelforusers.ListUsersPanel;

public class UserPage extends AbstractPage {

	public UserPage() {
		super();
		add(new Link("Back") {
			@Override
			public void onClick() {
				setResponsePage(new HomePage());
			}
		});

		add(new ListUsersPanel("list-panel"));

	}
}
