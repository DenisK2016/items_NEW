package by.dk.training.items.webapp.pages.products.formforreg;

import by.dk.training.items.datamodel.Product;
import by.dk.training.items.webapp.pages.AbstractPage;

public class ProductRegPage extends AbstractPage {
	
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
		if(product != null){
			add(new RegistryProductPanel("regPanProd", product));
		}else{
			add(new RegistryProductPanel("regPanProd"));
		}
		
	}
}
