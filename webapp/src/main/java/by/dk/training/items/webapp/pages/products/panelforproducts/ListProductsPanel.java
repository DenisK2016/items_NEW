package by.dk.training.items.webapp.pages.products.panelforproducts;

import java.io.Serializable;
import java.util.Iterator;

import javax.inject.Inject;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import by.dk.training.items.dataaccess.filters.ProductFilter;
import by.dk.training.items.datamodel.Product;
import by.dk.training.items.datamodel.Product_;
import by.dk.training.items.services.ProductService;
import by.dk.training.items.webapp.app.AuthorizedSession;
import by.dk.training.items.webapp.pages.products.ProductPage;
import by.dk.training.items.webapp.pages.products.formforreg.ProductRegPage;

@AuthorizeAction(roles = { "ADMIN", "COMMANDER", "OFFICER" }, action = Action.RENDER)
public class ListProductsPanel extends Panel {

	private static final long serialVersionUID = 1L;
	boolean officer = AuthorizedSession.get().getRoles().contains("OFFICER");
	boolean admin = AuthorizedSession.get().getRoles().contains("ADMIN");
	boolean commander = AuthorizedSession.get().getRoles().contains("COMMANDER");
	@Inject
	private ProductService productService;

	public ListProductsPanel(String id) {
		super(id);

	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		final ModalWindow modal1 = new ModalWindow("modal1");
		modal1.setTitle("Информация о получателе");
		modal1.setWindowClosedCallback(new WindowClosedCallback() {

			@Override
			public void onClose(AjaxRequestTarget target) {
				target.add(ListProductsPanel.this);

			}
		});
		this.setOutputMarkupId(true);
		add(modal1);
		SortableProductProvider dataProvider = new SortableProductProvider();
		DataView<Product> dataView = new DataView<Product>("productlist", dataProvider, 5) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<Product> item) {
				Product product = item.getModelObject();

				item.add(new AjaxLink<Void>("infoProduct") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						modal1.setContent(new ProductInfo(modal1, product));
						modal1.show(target);
					}
				});
				item.add(new Label("productid", product.getId()));
				item.add(new Label("productname", product.getNameProduct()));
				item.add(new Label("productprice", product.getPriceProduct()));
				item.add(new Label("productlimit", product.getLimit()));
				item.add(new CheckBox("productstatus", Model.of(product.getStatus())).setEnabled(false));
				Long id = product.getIdUser().getId();
				Label idUser = new Label("idUser", id);
				item.add(idUser);
				Link delLink = new Link("deletelink", item.getModel()) {

					@Override
					public void onClick() {
						productService.delete(product.getId());
						setResponsePage(new ProductPage());

					}
				};
				item.add(delLink);
				if (officer) {
					delLink.setVisible(false);
					idUser.setVisible(false);
				}
				Link update = new Link("updateLink", item.getModel()) {

					@Override
					public void onClick() {
						setResponsePage(new ProductRegPage(product));

					}

				};
				item.add(update);
				Long idCurrent = AuthorizedSession.get().getUser().getId();
				if (idCurrent != id && !admin && !commander) {
					update.setVisible(false);
				}

			}
		};
		add(dataView);

		add(new OrderByBorder("orderById", Product_.id, dataProvider));
		add(new OrderByBorder("orderByProductName", Product_.nameProduct, dataProvider));
		add(new OrderByBorder("orderByPrice", Product_.priceProduct, dataProvider));
		add(new OrderByBorder("orderByLimit", Product_.limit, dataProvider));
		add(new OrderByBorder("orderByStatus", Product_.status, dataProvider));
		OrderByBorder ord = new OrderByBorder("orderByUser", Product_.idUser, dataProvider);
		add(ord);

		if (officer) {

			ord.setVisible(false);
		}

		add(new PagingNavigator("navigator", dataView));
	}

	private class SortableProductProvider extends SortableDataProvider<Product, Serializable> {

		private static final long serialVersionUID = 1L;

		private ProductFilter productFilter;

		public SortableProductProvider() {
			super();
			productFilter = new ProductFilter();
			productFilter.setFetchUser(true);
			productFilter.setFetchType(true);
			setSort((Serializable) Product_.id, SortOrder.ASCENDING);
		}

		@Override
		public Iterator<Product> iterator(long first, long count) {

			Serializable property = getSort().getProperty();
			SortOrder propertySortOrder = getSortState().getPropertySortOrder(property);

			productFilter.setSortProperty((SingularAttribute) property);
			productFilter.setSortOrder(propertySortOrder.equals(SortOrder.ASCENDING) ? true : false);

			productFilter.setLimit((int) count);
			productFilter.setOffset((int) first);
			return productService.find(productFilter).iterator();

		}

		@Override
		public long size() {
			return productService.count(productFilter);
		}

		@Override
		public IModel<Product> model(Product object) {
			return new Model(object);
		}

	}

}
