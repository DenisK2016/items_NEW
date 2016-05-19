package by.dk.training.items.webapp.pages.types;

import org.apache.wicket.markup.html.link.Link;

import by.dk.training.items.webapp.pages.AbstractPage;
import by.dk.training.items.webapp.pages.home.HomePage;
import by.dk.training.items.webapp.pages.types.panelfortypes.ListTypesPanel;

public class TypePage extends AbstractPage {

	private static final long serialVersionUID = 1L;

	public TypePage() {
		super();
		add(new Link("Back") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new HomePage());
			}
		});
		
		add(new ListTypesPanel("list-panel"));

	}
}
