package by.dk.training.items.webapp.pages.packages;

import org.apache.wicket.markup.html.link.Link;

import by.dk.training.items.webapp.pages.AbstractPage;
import by.dk.training.items.webapp.pages.home.HomePage;
import by.dk.training.items.webapp.pages.packages.panelforpackages.ListPackagesPanel;

public class PackagesPage extends AbstractPage {

	public PackagesPage() {
		super();
		add(new Link("Back") {
			@Override
			public void onClick() {
				setResponsePage(new HomePage());
			}
		});
		
		add(new ListPackagesPanel("list-panel"));

	}
}
