package by.dk.training.items.webapp.pages.types.panelfortypes;

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
import by.dk.training.items.webapp.pages.types.TypePage;
import by.dk.training.items.webapp.pages.types.formforreg.TypeRegPage;

@AuthorizeAction(roles = { "ADMIN", "COMMANDER" }, action = Action.RENDER)
public class ListTypesPanel extends Panel {

	private static final long serialVersionUID = 1L;
	@Inject
	private TypeService typeService;

	public ListTypesPanel(String id) {
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
				target.add(ListTypesPanel.this);

			}
		});
		this.setOutputMarkupId(true);
		add(modal1);
		SortableTypeProvider dataProvider = new SortableTypeProvider();
		DataView<Type> dataView = new DataView<Type>("simple", dataProvider, 5) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<Type> item) {
				Type type = item.getModelObject();

				item.add(new AjaxLink<Void>("infoType") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						modal1.setContent(new TypeInfo(modal1, type));
						modal1.show(target);
					}
				});
				item.add(new Label("typeid", type.getId()));
				item.add(new Label("typename", type.getTypeName()));
				if (type.getParentType() != null) {
					item.add(new Label("parentname", type.getParentType().getId()));
				} else {
					item.add(new Label("parentname", " "));
				}
				Label idUser = new Label("idUser", type.getIdUser().getId());
				item.add(idUser);

				item.add(new Link<TypePage>("deletelink") {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						typeService.delete(type.getId());
						setResponsePage(new TypePage());
					}
				});

				item.add(new Link<TypeRegPage>("updateType") {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new TypeRegPage(type));

					}
				});

			}

		};
		add(dataView);

		add(new OrderByBorder("orderById", Type_.id, dataProvider));
		add(new OrderByBorder("orderByTypeName", Type_.typeName, dataProvider));
		add(new OrderByBorder("orderByParentTypeId", Type_.parentType, dataProvider));
		OrderByBorder ord = new OrderByBorder("orderByUser", Type_.idUser, dataProvider);
		add(ord);

		add(new PagingNavigator("navigator", dataView));

	}

	private class SortableTypeProvider extends SortableDataProvider<Type, Serializable> {

		private static final long serialVersionUID = 1L;

		private TypeFilter typeFilter;

		public SortableTypeProvider() {
			super();
			typeFilter = new TypeFilter();
			typeFilter.setFetchUser(true);
			typeFilter.setFetchParentType(true);
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
