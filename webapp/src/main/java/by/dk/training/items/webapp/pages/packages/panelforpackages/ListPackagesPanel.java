package by.dk.training.items.webapp.pages.packages.panelforpackages;

import java.io.Serializable;
import java.util.Iterator;

import javax.inject.Inject;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.wicket.datetime.markup.html.basic.DateLabel;
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

import by.dk.training.items.dataaccess.filters.PackageFilter;
import by.dk.training.items.datamodel.Package;
import by.dk.training.items.datamodel.Package_;
import by.dk.training.items.services.PackageService;

public class ListPackagesPanel extends Panel {

	private static final long serialVersionUID = 1L;
	@Inject
	private PackageService packageService;

	public ListPackagesPanel(String id) {
		super(id);

		SortablePackageProvider dataProvider = new SortablePackageProvider();
		DataView<Package> dataView = new DataView<Package>("packagelist", dataProvider, 5) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<Package> item) {
				Package packages = item.getModelObject();

				item.add(new Label("packageid", packages.getId()));
				item.add(new Label("recipientid", packages.getIdRecipient().getId()));
				item.add(new Label("price", packages.getPrice()));
				item.add(new Label("weight", packages.getWeight()));
				item.add(new Label("userid", packages.getIdUser().getId()));
				item.add(DateLabel.forDatePattern("date", Model.of(packages.getDate()), "dd-MM-yyyy"));
				item.add(new Label("description", packages.getDescription()));
				item.add(new Label("countrysender", packages.getCountrySender()));
				item.add(new Label("deadline", packages.getPaymentDeadline()));
				item.add(new Label("fine", packages.getFine()));				
				item.add(new CheckBox("paid", Model.of(packages.getPaid())));
				item.add(new Link("deletelink", item.getModel()) {

					@Override
					public void onClick() {
						packageService.delete(packages.getId());
						
					}
				});

			}
		};
		add(dataView);

		add(new OrderByBorder("orderById", Package_.id, dataProvider));
		add(new OrderByBorder("orderByRec", Package_.idRecipient, dataProvider));
		add(new OrderByBorder("orderByPrice", Package_.price, dataProvider));
		add(new OrderByBorder("orderByWeight", Package_.weight, dataProvider));
		add(new OrderByBorder("orderByUser", Package_.idUser, dataProvider));
		add(new OrderByBorder("orderByDate", Package_.date, dataProvider));
		add(new OrderByBorder("orderByDesc", Package_.description, dataProvider));
		add(new OrderByBorder("orderBySend", Package_.countrySender, dataProvider));
		add(new OrderByBorder("orderByDead", Package_.paymentDeadline, dataProvider));
		add(new OrderByBorder("orderByFine", Package_.fine, dataProvider));
		add(new OrderByBorder("orderByPaid", Package_.paid, dataProvider));

		add(new PagingNavigator("navigator", dataView));

	}

	private class SortablePackageProvider extends SortableDataProvider<Package, Serializable> {

		private static final long serialVersionUID = 1L;

		private PackageFilter packageFilter;

		public SortablePackageProvider() {
			super();
			packageFilter = new PackageFilter();
			setSort((Serializable) Package_.id, SortOrder.ASCENDING);
		}

		@Override
		public Iterator<Package> iterator(long first, long count) {

			Serializable property = getSort().getProperty();
			SortOrder propertySortOrder = getSortState().getPropertySortOrder(property);

			packageFilter.setSortProperty((SingularAttribute) property);
			packageFilter.setSortOrder(propertySortOrder.equals(SortOrder.ASCENDING) ? true : false);

			packageFilter.setLimit((int) count);
			packageFilter.setOffset((int) first);
			return packageService.find(packageFilter).iterator();

		}

		@Override
		public long size() {
			return packageService.count(packageFilter);
		}

		@Override
        public IModel<Package> model(Package object) {
            return new Model(object);
        }

	}
}
