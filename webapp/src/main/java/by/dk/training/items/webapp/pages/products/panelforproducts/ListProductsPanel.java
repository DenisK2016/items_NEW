package by.dk.training.items.webapp.pages.products.panelforproducts;

import java.io.Serializable;
import java.util.Iterator;

import javax.inject.Inject;
import javax.persistence.metamodel.SingularAttribute;

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
import by.dk.training.items.webapp.pages.products.ProductPage;
import by.dk.training.items.webapp.pages.products.formforreg.ProductRegPage;

public class ListProductsPanel extends Panel {

	private static final long serialVersionUID = 1L;
	@Inject
	private ProductService productService;

	public ListProductsPanel(String id) {
		super(id);
		SortableProductProvider dataProvider = new SortableProductProvider();
		DataView<Product> dataView = new DataView<Product>("productlist", dataProvider, 5) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<Product> item) {
				Product product = item.getModelObject();

				item.add(new Label("productid", product.getId()));
				item.add(new Label("productname", product.getNameProduct()));
				item.add(new Label("productprice", product.getPriceProduct()));
				item.add(new Label("productlimit", product.getLimit()));
				item.add(new CheckBox("productstatus", Model.of(product.getStatus())).setEnabled(false));
				item.add(new Link("deletelink", item.getModel()) {

					@Override
					public void onClick() {
						productService.delete(product.getId());
						setResponsePage(new ProductPage());

					}
				});
				item.add(new Link("updateLink", item.getModel()) {

					@Override
					public void onClick() {
						setResponsePage(new ProductRegPage(product));

					}

				});

			}
		};
		add(dataView);

		add(new OrderByBorder("orderById", Product_.id, dataProvider));
		add(new OrderByBorder("orderByProductName", Product_.nameProduct, dataProvider));
		add(new OrderByBorder("orderByPrice", Product_.priceProduct, dataProvider));
		add(new OrderByBorder("orderByLimit", Product_.limit, dataProvider));
		add(new OrderByBorder("orderByStatus", Product_.status, dataProvider));

		add(new PagingNavigator("navigator", dataView));
	}

	private class SortableProductProvider extends SortableDataProvider<Product, Serializable> {

		private static final long serialVersionUID = 1L;

		private ProductFilter productFilter;

		public SortableProductProvider() {
			super();
			productFilter = new ProductFilter();
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
