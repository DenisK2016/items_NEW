package by.dk.training.items.webapp.pages.types.panelfortypes;

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

import by.dk.training.items.dataaccess.filters.TypeFilter;
import by.dk.training.items.datamodel.Type;
import by.dk.training.items.datamodel.Type_;
import by.dk.training.items.services.TypeService;

public class ListTypesPanel extends Panel {

	private static final long serialVersionUID = 1L;
	@Inject
	private TypeService typeService;

	public ListTypesPanel(String id) {
		super(id);

		SortableTypeProvider dataProvider = new SortableTypeProvider();
		DataView<Type> dataView = new DataView<Type>("simple", dataProvider, 5) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<Type> item) {
				Type type = item.getModelObject();

				item.add(new Label("typeid", type.getId()));
				item.add(new Label("typename", type.getTypeName()));
				if (type.getParentType() != null) {
					item.add(new Label("parentname", type.getParentType().getId()));
				} else {
					item.add(new Label("parentname", " "));
				}
				item.add(new Link("deletelink", item.getModel()) {

					@Override
					public void onClick() {
						typeService.delete(type.getId());
						
					}
				});
			}
		};
		add(dataView);
		
		add(new OrderByBorder("orderById", Type_.id, dataProvider));
		add(new OrderByBorder("orderByTypeName", Type_.typeName, dataProvider));
		add(new OrderByBorder("orderByParentTypeId", Type_.parentType, dataProvider));
		
		add(new PagingNavigator("navigator", dataView));

	}

	private class SortableTypeProvider extends SortableDataProvider<Type, Serializable> {

		private static final long serialVersionUID = 1L;

		private TypeFilter typeFilter;

		public SortableTypeProvider() {
			super();
			typeFilter = new TypeFilter();
			setSort((Serializable) Type_.id, SortOrder.ASCENDING);
		}

		@Override
		public Iterator<Type> iterator(long first, long count) {

			Serializable property = getSort().getProperty();
			SortOrder propertySortOrder = getSortState().getPropertySortOrder(property);

			typeFilter.setSortProperty((SingularAttribute) property);
			typeFilter.setSortOrder(propertySortOrder.equals(SortOrder.ASCENDING) ? true : false);

			typeFilter.setLimit((int) count);
			typeFilter.setOffset((int) first);
			return typeService.find(typeFilter).iterator();
			
		}

		@Override
		public long size() {
			return typeService.count(typeFilter);
		}

		@Override
		public IModel<Type> model(Type object) {
			return new Model(object);
		}

	}
}
