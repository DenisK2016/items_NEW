package by.dk.training.items.webapp.pages.home;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;

import by.dk.training.items.webapp.pages.AbstractPage;

@AuthorizeInstantiation(value = { "ADMIN", "OFFICER", "COMMANDER" })
public class HomePage extends AbstractPage {

	public HomePage() {
		super();

	}

}
