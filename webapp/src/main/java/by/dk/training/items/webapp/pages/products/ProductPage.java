package by.dk.training.items.webapp.pages.products;

import org.apache.wicket.markup.html.link.Link;

import by.dk.training.items.webapp.pages.AbstractPage;
import by.dk.training.items.webapp.pages.home.HomePage;
import by.dk.training.items.webapp.pages.products.formforreg.ProductRegPage;
import by.dk.training.items.webapp.pages.products.panelforproducts.ListProductsPanel;

public class ProductPage extends AbstractPage {

	public ProductPage() {
		super();

	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(new Link("Back") {
			@Override
			public void onClick() {
				setResponsePage(new HomePage());
			}
		});

		add(new ListProductsPanel("list-panel"));

		add(new Link("CreateProd") {
			@Override
			public void onClick() {
				setResponsePage(new ProductRegPage());
			}
		});
	}
}
