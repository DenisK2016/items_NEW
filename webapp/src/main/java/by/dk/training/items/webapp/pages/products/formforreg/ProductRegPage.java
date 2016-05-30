package by.dk.training.items.webapp.pages.products.formforreg;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;

import by.dk.training.items.datamodel.Product;
import by.dk.training.items.webapp.pages.AbstractPage;

@AuthorizeInstantiation(value = { "ADMIN", "OFFICER", "COMMANDER" })
public class ProductRegPage extends AbstractPage {

	private static final long serialVersionUID = 1L;
	private Product product;

	public ProductRegPage() {
		super();

	}

	public ProductRegPage(Product product) {
		super();
		this.product = product;

	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		if (product != null) {
			add(new RegistryProductPanel("regPanProd", product, ProductRegPage.this));
		} else {
			add(new RegistryProductPanel("regPanProd", ProductRegPage.this));
		}

	}
}
