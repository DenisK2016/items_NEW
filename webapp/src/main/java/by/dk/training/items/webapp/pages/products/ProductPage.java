package by.dk.training.items.webapp.pages.products;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;

import by.dk.training.items.webapp.pages.AbstractPage;
import by.dk.training.items.webapp.pages.products.formforreg.RegistryProductPanel;
import by.dk.training.items.webapp.pages.products.panelforproducts.ListProductsPanel;

@AuthorizeInstantiation(value = { "ADMIN", "OFFICER", "COMMANDER" })
public class ProductPage extends AbstractPage {

	private static final long serialVersionUID = 1L;

	public ProductPage() {
		super();

	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		final ModalWindow modalCreate = new ModalWindow("modalCreate");
		modalCreate.setCssClassName("modal_window");
		modalCreate.setResizable(false);
		modalCreate.setWindowClosedCallback(new WindowClosedCallback() {

			@Override
			public void onClose(AjaxRequestTarget target) {
				target.add(ProductPage.this);

			}
		});
		this.setOutputMarkupId(true);
		add(modalCreate);
		add(new ListProductsPanel("list-panel"));

		add(new AjaxLink<Void>("createProd") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				modalCreate.setContent(new RegistryProductPanel(modalCreate));
				modalCreate.show(target);
			}
		}.add(AttributeModifier.append("title", "Создать продукт")));
	}
}
