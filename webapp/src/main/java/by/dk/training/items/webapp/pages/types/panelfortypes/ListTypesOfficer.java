package by.dk.training.items.webapp.pages.types.panelfortypes;

import java.io.Serializable;
import java.util.Iterator;

import javax.inject.Inject;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import by.dk.training.items.dataaccess.filters.TypeFilter;
import by.dk.training.items.datamodel.Type;
import by.dk.training.items.datamodel.Type_;
import by.dk.training.items.datamodel.UserProfile;
import by.dk.training.items.services.TypeService;
import by.dk.training.items.services.UserProfileService;
import by.dk.training.items.webapp.app.AuthorizedSession;
import by.dk.training.items.webapp.pages.types.formforreg.RegistryTypePanel;

@AuthorizeAction(roles = { "OFFICER" }, action = Action.RENDER)
public class ListTypesOfficer extends Panel {

	private static final long serialVersionUID = 1L;
	@Inject
	private TypeService typeService;
	@Inject
	private UserProfileService userProfileService;
	private SortableTypeProvider dataProvider;
	private Long idFilter;
	private String nameFilter;
	private TypeFilter typeFilter;
	boolean bool;

	public boolean isBool() {
		return bool;
	}

	public void setBool(boolean bool) {
		this.bool = bool;
	}

	public ListTypesOfficer(String id) {
		super(id);

	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		typeFilter = new TypeFilter();

		final ModalWindow modal1 = new ModalWindow("modal1");
		modal1.setCssClassName("modal_window");
		modal1.setInitialHeight(400);
		modal1.setResizable(false);
		modal1.setWindowClosedCallback(new WindowClosedCallback() {

			@Override
			public void onClose(AjaxRequestTarget target) {
				target.add(ListTypesOfficer.this);

			}
		});
		this.setOutputMarkupId(true);
		add(modal1);
		final ModalWindow modalUpdate = new ModalWindow("modalUpdate");
		modalUpdate.setCssClassName("modal_window");
		modalUpdate.setResizable(false);
		modalUpdate.setWindowClosedCallback(new WindowClosedCallback() {

			@Override
			public void onClose(AjaxRequestTarget target) {
				target.add(ListTypesOfficer.this);

			}
		});
		this.setOutputMarkupId(true);
		add(modalUpdate);
		dataProvider = new SortableTypeProvider(typeFilter);
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
				AjaxLink update = new AjaxLink<Void>("updateType") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						modalUpdate.setContent(new RegistryTypePanel(modalUpdate, type));
						modalUpdate.show(target);
					}
				};
				update.add(AttributeModifier.append("title", "Обновить тип"));
				item.add(update);
				if (AuthorizedSession.get().getUser().getId() != type.getIdUser().getId()) {
					update.setVisible(false);
				}

			}
		};
		WebMarkupContainer wmk = new WebMarkupContainer("container");
		wmk.setOutputMarkupId(true);
		wmk.add(dataView);
		add(wmk);

		wmk.add(new OrderByBorder("orderById", Type_.id, dataProvider));
		wmk.add(new OrderByBorder("orderByTypeName", Type_.typeName, dataProvider));

		wmk.add(new PagingNavigator("navigator", dataView));

		TextField<Long> idFilt = new TextField<Long>("idFilter", new PropertyModel<Long>(this, "idFilter"));
		idFilt.add(new AjaxFormComponentUpdatingBehavior("onkeyup") {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				typeFilter.setId(idFilter);
				dataProvider.setTypeFilter(typeFilter);
				target.add(wmk);
			}
		});
		add(idFilt);
		TextField<String> nameFilt = new TextField<String>("nameFilter", new PropertyModel<String>(this, "nameFilter"));
		nameFilt.add(new AjaxFormComponentUpdatingBehavior("onkeyup") {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				typeFilter.setTypeName(nameFilter);
				dataProvider.setTypeFilter(typeFilter);
				target.add(wmk);
			}
		});
		add(nameFilt);

		AjaxCheckBox chkb = new AjaxCheckBox("myType", new PropertyModel<Boolean>(this, "bool")) {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				if (this.getDefaultModelObjectAsString().equals("true")) {
					Long idUser = AuthorizedSession.get().getUser().getId();
					UserProfile currUser = userProfileService.getUser(idUser);
					typeFilter.setUser(currUser);
				} else {
					typeFilter.setUser(null);
				}
				dataProvider.setTypeFilter(typeFilter);
				target.add(wmk);
			}
		};
		add(chkb);

	}

	private class SortableTypeProvider extends SortableDataProvider<Type, Serializable> {

		private static final long serialVersionUID = 1L;

		private TypeFilter typeFilter;

		public TypeFilter getTypeFilter() {
			return typeFilter;
		}

		public void setTypeFilter(TypeFilter typeFilter) {
			this.typeFilter = typeFilter;
		}

		public SortableTypeProvider(TypeFilter typeFilter) {
			super();
			this.typeFilter = typeFilter;
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
