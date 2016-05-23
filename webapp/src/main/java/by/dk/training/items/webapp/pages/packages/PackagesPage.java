package by.dk.training.items.webapp.pages.packages;

import org.apache.wicket.markup.html.link.Link;

import by.dk.training.items.webapp.pages.AbstractPage;
import by.dk.training.items.webapp.pages.home.HomePage;
import by.dk.training.items.webapp.pages.packages.formforreg.PackRegPage;
import by.dk.training.items.webapp.pages.packages.panelforpackages.ListPackagesPanel;

public class PackagesPage extends AbstractPage {

	private static final long serialVersionUID = 1L;

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

		add(new ListPackagesPanel("list-panel"));

		add(new Link<PackRegPage>("CreatePack") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new PackRegPage());
			}
		});
	}
}
