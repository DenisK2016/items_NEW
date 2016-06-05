package by.dk.training.items.webapp.pages.products.panelforproducts;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.googlecode.wicket.jquery.ui.markup.html.link.AjaxLink;

import by.dk.training.items.dataaccess.filters.ProductFilter;
import by.dk.training.items.datamodel.Product;
import by.dk.training.items.datamodel.Type;
import by.dk.training.items.services.ProductService;

public class ProductInfo extends Panel {

	private ModalWindow modalWindow;
	@Inject
	private ProductService productService;
	private Product product;
	private ProductFilter productFilter;

	public ProductInfo(ModalWindow modalWindow, Product product) {
		super(modalWindow.getContentId());
		productFilter = new ProductFilter();
		productFilter.setFetchType(true);
		productFilter.setFetchUser(true);
		productFilter.setId(product.getId());
		this.product = productService.find(productFilter).get(0);
		this.modalWindow = modalWindow;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(new Label("productid", product.getId()));
		add(new Label("productname", product.getNameProduct()));
		add(new Label("productprice", product.getPriceProduct()));
		add(new Label("productlimit", product.getWeight()));
		CheckBox chk = new CheckBox("productstatus", Model.of(product.getStatus()));
		chk.setEnabled(false);
		if (product.getStatus()) {
			chk.add(AttributeModifier.append("title", "Ввоз разрешен"));
		} else {
			chk.add(AttributeModifier.append("title", "Ввоз запрещен"));
		}
		add(chk);
		add(new Label("user", String.format("%d %s", product.getIdUser().getId(), product.getIdUser().getLogin())));

		List<String> listTypes = new ArrayList<>();
		for (Type t : product.getTypes()) {
			listTypes.add(String.format("%d %s", t.getId(), t.getTypeName()));
		}
		add(new DropDownChoice<>("types", listTypes).setNullValid(true)
				.add(AttributeModifier.append("title", "Типы к которым относится данный продукт")));

		add(new AjaxLink<Void>("back") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				modalWindow.close(target);
			}
		}.add(AttributeModifier.append("title", "Назад")));
	}

}
