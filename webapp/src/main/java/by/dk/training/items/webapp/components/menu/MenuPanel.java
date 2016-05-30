package by.dk.training.items.webapp.components.menu;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

import by.dk.training.items.webapp.app.AuthorizedSession;
import by.dk.training.items.webapp.pages.login.LoginPage;
import by.dk.training.items.webapp.pages.packages.PackagesPage;
import by.dk.training.items.webapp.pages.products.ProductPage;
import by.dk.training.items.webapp.pages.profilemenu.ProfileMenuPage;
import by.dk.training.items.webapp.pages.recipients.RecipientPage;
import by.dk.training.items.webapp.pages.types.TypePage;

public class MenuPanel extends Panel {

	public MenuPanel(String id) {
		super(id);
		// setRenderBodyOnly(true);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(new Link("linkproduct") {
			@Override
			public void onClick() {
				setResponsePage(new ProductPage());
			}
		});

		add(new Link("types") {
			@Override
			public void onClick() {
				setResponsePage(new TypePage());
			}
		});

		add(new Link("packages") {
			@Override
			public void onClick() {
				setResponsePage(new PackagesPage());
			}
		});

		add(new Link("recipient") {
			@Override
			public void onClick() {
				setResponsePage(new RecipientPage());
			}
		});

		add(new Link("logout") {

			@Override
			public void onClick() {
				getSession().invalidate();
				setResponsePage(LoginPage.class);

			}

		}.setVisible(AuthorizedSession.get().isSignedIn()));

		add(new Link("profile") {

			@Override
			public void onClick() {
				setResponsePage(new ProfileMenuPage(AuthorizedSession.get().getUser()));

			}

		});

	}

}
