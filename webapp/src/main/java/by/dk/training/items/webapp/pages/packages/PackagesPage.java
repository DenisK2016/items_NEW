package by.dk.training.items.webapp.pages.packages;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.link.Link;

import by.dk.training.items.webapp.app.AuthorizedSession;
import by.dk.training.items.webapp.pages.AbstractPage;
import by.dk.training.items.webapp.pages.home.HomePage;
import by.dk.training.items.webapp.pages.packages.formforreg.PackRegPage;
import by.dk.training.items.webapp.pages.packages.panelforpackages.ListPackagesOfficer;
import by.dk.training.items.webapp.pages.packages.panelforpackages.ListPackagesPanel;

@AuthorizeInstantiation(value = { "ADMIN", "OFFICER", "COMMANDER" })
public class PackagesPage extends AbstractPage {

	private static final long serialVersionUID = 1L;
	boolean admin = AuthorizedSession.get().getRoles().contains("ADMIN");
	boolean commander = AuthorizedSession.get().getRoles().contains("COMMANDER");
	boolean officer = AuthorizedSession.get().getRoles().contains("OFFICER");

	public PackagesPage() {
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

		if (admin || commander) {
			add(new ListPackagesPanel("list-panel"));
		} else {
			add(new ListPackagesOfficer("list-panel"));
		}

		add(new Link<PackRegPage>("CreatePack") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new PackRegPage());
			}
		});
	}
}
