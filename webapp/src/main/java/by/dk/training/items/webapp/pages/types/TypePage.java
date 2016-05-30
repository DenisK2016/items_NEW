package by.dk.training.items.webapp.pages.types;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.link.Link;

import by.dk.training.items.webapp.app.AuthorizedSession;
import by.dk.training.items.webapp.pages.AbstractPage;
import by.dk.training.items.webapp.pages.home.HomePage;
import by.dk.training.items.webapp.pages.types.formforreg.TypeRegPage;
import by.dk.training.items.webapp.pages.types.panelfortypes.ListTypesOfficer;
import by.dk.training.items.webapp.pages.types.panelfortypes.ListTypesPanel;

@AuthorizeInstantiation(value = { "ADMIN", "OFFICER", "COMMANDER" })
public class TypePage extends AbstractPage {

	private static final long serialVersionUID = 1L;
	boolean admin = AuthorizedSession.get().getRoles().contains("ADMIN");
	boolean commander = AuthorizedSession.get().getRoles().contains("COMMANDER");
	boolean officer = AuthorizedSession.get().getRoles().contains("OFFICER");

	public TypePage() {
		super();

	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(new Link("Back") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new HomePage());
			}
		});

		if (admin || commander) {
			add(new ListTypesPanel("list-panel"));
		} else if (officer) {
			add(new ListTypesOfficer("list-panel"));
		}

		add(new Link("CreateType") {
			@Override
			public void onClick() {
				setResponsePage(new TypeRegPage());
			}
		});
	}
}
