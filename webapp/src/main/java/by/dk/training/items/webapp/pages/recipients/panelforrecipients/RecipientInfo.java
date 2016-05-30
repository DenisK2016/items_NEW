package by.dk.training.items.webapp.pages.recipients.panelforrecipients;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.googlecode.wicket.jquery.ui.markup.html.link.AjaxLink;

import by.dk.training.items.dataaccess.filters.PackageFilter;
import by.dk.training.items.dataaccess.filters.RecipientFilter;
import by.dk.training.items.datamodel.Package;
import by.dk.training.items.datamodel.Package_;
import by.dk.training.items.datamodel.Product;
import by.dk.training.items.datamodel.Recipient;
import by.dk.training.items.services.PackageService;
import by.dk.training.items.services.RecipientService;

public class RecipientInfo extends Panel {

	@Inject
	private RecipientService recipientService;
	@Inject
	private PackageService packageService;
	private Recipient recipient;
	private RecipientFilter recipientFilter;
	private ModalWindow modalWindow;

	public RecipientInfo(ModalWindow modalWindow, Recipient recipient) {
		super(modalWindow.getContentId());
		this.modalWindow = modalWindow;
		recipientFilter = new RecipientFilter();
		recipientFilter.setFetchPackages(true);
		recipientFilter.setFetchUser(true);
		recipientFilter.setId(recipient.getId());
		this.recipient = recipientService.find(recipientFilter).get(0);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(new Label("nameRec", recipient.getName()));
		add(new Label("cityRec", recipient.getCity()));
		add(new Label("addressRec", recipient.getAddress()));
		String user = String.format("%d, %s", recipient.getIdUser().getId(), recipient.getIdUser().getLogin());
		add(new Label("userRec", user));

		SortablePackageProvider dataProvider = new SortablePackageProvider();
		DataView<Package> dataView = new DataView<Package>("packagelist", dataProvider, 10) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<Package> item) {
				Package pack = item.getModelObject();

				item.add(new Label("packageid", pack.getId()));
				item.add(new Label("price", pack.getPrice()));
				item.add(new Label("weight", pack.getWeight()));
				item.add(new Label("userid", pack.getIdUser().getId()));
				item.add(DateLabel.forDatePattern("date", Model.of(pack.getDate()), "dd-MM-yyyy"));
				item.add(new Label("description", pack.getDescription()));
				item.add(new Label("countrysender", pack.getCountrySender()));
				item.add(new Label("deadline", pack.getPaymentDeadline()));
				item.add(new Label("fine", pack.getFine()));
				item.add(new CheckBox("paid", Model.of(pack.getPaid())).setEnabled(false));
				List<String> listProduct = new ArrayList<>();
				for (Product p : pack.getProducts()) {
					listProduct.add(p.getNameProduct());
				}
				item.add(new DropDownChoice<String>("listProd", listProduct).setNullValid(true));

			}
		};
		add(dataView);

		add(new OrderByBorder("orderById", Package_.id, dataProvider));
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

		add(new AjaxLink<Void>("back") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				modalWindow.close(target);
			}
		});
	}

	private class SortablePackageProvider extends SortableDataProvider<Package, Serializable> {

		private static final long serialVersionUID = 1L;

		private PackageFilter packageFilter;

		public SortablePackageProvider() {
			super();
			packageFilter = new PackageFilter();
			packageFilter.setFetchProduct(true);
			packageFilter.setFetchRecipient(true);
			packageFilter.setFetchUser(true);
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
			packageFilter.setRecipint(recipient);
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
