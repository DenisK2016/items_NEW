package by.dk.training.items.webapp.pages.recipients.panelforrecipients;

import java.io.Serializable;
import java.util.Iterator;

import javax.inject.Inject;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import by.dk.training.items.dataaccess.filters.RecipientFilter;
import by.dk.training.items.datamodel.Recipient;
import by.dk.training.items.datamodel.Recipient_;
import by.dk.training.items.services.RecipientService;

public class ListRecipientsPanel extends Panel {

	private static final long serialVersionUID = 1L;
	@Inject
	private RecipientService recipientService;

	public ListRecipientsPanel(String id) {
		super(id);

		SortableRecipientProvider dataProvider = new SortableRecipientProvider();
		DataView<Recipient> dataView = new DataView<Recipient>("recipientlist", dataProvider, 5) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<Recipient> item) {
				Recipient recipient = item.getModelObject();

				item.add(new Label("recipientid", recipient.getId()));
				item.add(new Label("recipientname", recipient.getName()));
				item.add(new Label("recipientcity", recipient.getCity()));
				item.add(new Label("recipientaddress", recipient.getAddress()));
				item.add(new Link("deletelink", item.getModel()) {

					@Override
					public void onClick() {
						recipientService.delete(recipient.getId());
						
					}
				});
				
			}
		};
		add(dataView);
		
		add(new OrderByBorder("orderById", Recipient_.id, dataProvider));
		add(new OrderByBorder("orderByName", Recipient_.name, dataProvider));
		add(new OrderByBorder("orderByCity", Recipient_.city, dataProvider));
		add(new OrderByBorder("orderByAddress", Recipient_.address, dataProvider));
		
		add(new PagingNavigator("navigator", dataView));

	}

	private class SortableRecipientProvider extends SortableDataProvider<Recipient, Serializable> {

		private static final long serialVersionUID = 1L;

		private RecipientFilter recipientFilter;

		public SortableRecipientProvider() {
			super();
			recipientFilter = new RecipientFilter();
			setSort((Serializable) Recipient_.id, SortOrder.ASCENDING);
		}

		@Override
		public Iterator<Recipient> iterator(long first, long count) {

			Serializable property = getSort().getProperty();
			SortOrder propertySortOrder = getSortState().getPropertySortOrder(property);

			recipientFilter.setSortProperty((SingularAttribute) property);
			recipientFilter.setSortOrder(propertySortOrder.equals(SortOrder.ASCENDING) ? true : false);

			recipientFilter.setLimit((int) count);
			recipientFilter.setOffset((int) first);
			return recipientService.find(recipientFilter).iterator();
			
		}

		@Override
		public long size() {
			return recipientService.count(recipientFilter);
		}

		@Override
		public IModel<Recipient> model(Recipient object) {
			return new Model(object);
		}

	}
}
