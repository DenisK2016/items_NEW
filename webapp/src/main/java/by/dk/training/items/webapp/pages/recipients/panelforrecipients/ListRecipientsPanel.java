package by.dk.training.items.webapp.pages.recipients.panelforrecipients;

import java.io.Serializable;
import java.util.Iterator;

import javax.inject.Inject;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
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
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import by.dk.training.items.dataaccess.filters.RecipientFilter;
import by.dk.training.items.datamodel.Recipient;
import by.dk.training.items.datamodel.Recipient_;
import by.dk.training.items.services.RecipientService;
import by.dk.training.items.services.UserProfileService;
import by.dk.training.items.webapp.app.AuthorizedSession;
import by.dk.training.items.webapp.pages.recipients.RecipientPage;
import by.dk.training.items.webapp.pages.recipients.formforreg.RegistryRecipientPanel;

@AuthorizeAction(roles = { "ADMIN", "COMMANDER", "OFFICER" }, action = Action.RENDER)
public class ListRecipientsPanel extends Panel {

	private static final long serialVersionUID = 1L;
	boolean officer = AuthorizedSession.get().getRoles().contains("OFFICER");
	boolean admin = AuthorizedSession.get().getRoles().contains("ADMIN");
	boolean commander = AuthorizedSession.get().getRoles().contains("COMMANDER");
	@Inject
	private RecipientService recipientService;
	@Inject
	private UserProfileService userProfileService;
	private RecipientFilter recipientFilter;
	SortableRecipientProvider dataProvider;
	private Long idFilter;
	private String nameFilter;
	private String cityFilter;
	private String addressFilter;
	private Long userFilter;

	public ListRecipientsPanel(String id) {
		super(id);

	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		recipientFilter = new RecipientFilter();
		final ModalWindow modal1 = new ModalWindow("modal1");
		modal1.setCssClassName("modal_window");
		modal1.setInitialHeight(500);
		modal1.setResizable(false);
		modal1.setWindowClosedCallback(new WindowClosedCallback() {

			@Override
			public void onClose(AjaxRequestTarget target) {
				target.add(ListRecipientsPanel.this);

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
				target.add(ListRecipientsPanel.this);

			}
		});
		this.setOutputMarkupId(true);
		add(modalUpdate);
		dataProvider = new SortableRecipientProvider(recipientFilter);
		DataView<Recipient> dataView = new DataView<Recipient>("recipientlist", dataProvider, 5) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<Recipient> item) {
				Recipient recipient = item.getModelObject();
				item.add(new AjaxLink<Void>("infoRecipient") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						modal1.setContent(new RecipientInfo(modal1, recipient));
						modal1.show(target);
					}
				});
				item.add(new Label("recipientid", recipient.getId()));
				item.add(new Label("recipientname", recipient.getName()));
				item.add(new Label("recipientcity", recipient.getCity()));
				item.add(new Label("recipientaddress", recipient.getAddress()));
				Long id = recipient.getIdUser().getId();
				Label idUser = new Label("idUser", id);
				item.add(idUser);
				Link delLink = new Link<RecipientPage>("deletelink") {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						recipientService.delete(recipient.getId());
						setResponsePage(new RecipientPage());
					}
				};
				item.add(delLink);
				if (officer) {
					delLink.setVisible(false);
					idUser.setVisible(false);
				}
				AjaxLink update = new AjaxLink<Void>("updateRecipient") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						modalUpdate.setContent(new RegistryRecipientPanel(modalUpdate, recipient));
						modalUpdate.show(target);
					}
				};
				update.add(AttributeModifier.append("title", "Изменить получателя"));
				item.add(update);
				Long idCurrentUser = AuthorizedSession.get().getUser().getId();
				if (idCurrentUser != id && !admin && !commander) {
					update.setVisible(false);
				}

			}
		};
		WebMarkupContainer wmk = new WebMarkupContainer("container");
		wmk.setOutputMarkupId(true);
		wmk.add(dataView);
		add(wmk);

		wmk.add(new OrderByBorder("orderById", Recipient_.id, dataProvider));
		wmk.add(new OrderByBorder("orderByName", Recipient_.name, dataProvider));
		wmk.add(new OrderByBorder("orderByCity", Recipient_.city, dataProvider));
		wmk.add(new OrderByBorder("orderByAddress", Recipient_.address, dataProvider));
		OrderByBorder ord = new OrderByBorder("orderByUser", Recipient_.idUser, dataProvider);
		wmk.add(ord);

		if (officer) {

			ord.setVisible(false);
		}

		wmk.add(new PagingNavigator("navigator", dataView));

		TextField<Long> idFilt = new TextField<Long>("idFilter", new PropertyModel<Long>(this, "idFilter"));
		idFilt.add(new AjaxFormComponentUpdatingBehavior("onkeyup") {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				recipientFilter.setId(idFilter);
				dataProvider.setRecipientFilter(recipientFilter);
				target.add(wmk);
			}
		});
		wmk.add(idFilt);

		TextField<String> nameFilt = new TextField<String>("nameFilter", new PropertyModel<String>(this, "nameFilter"));
		nameFilt.add(new AjaxFormComponentUpdatingBehavior("onkeyup") {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				recipientFilter.setName(nameFilter);
				dataProvider.setRecipientFilter(recipientFilter);
				target.add(wmk);
			}
		});
		wmk.add(nameFilt);

		TextField<String> cityFilt = new TextField<String>("cityFilter", new PropertyModel<String>(this, "cityFilter"));
		cityFilt.add(new AjaxFormComponentUpdatingBehavior("onkeyup") {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				recipientFilter.setCity(cityFilter);
				dataProvider.setRecipientFilter(recipientFilter);
				target.add(wmk);
			}
		});
		wmk.add(cityFilt);

		TextField<String> addressFilt = new TextField<String>("addressFilter",
				new PropertyModel<String>(this, "addressFilter"));
		addressFilt.add(new AjaxFormComponentUpdatingBehavior("onkeyup") {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				recipientFilter.setAddress(addressFilter);
				dataProvider.setRecipientFilter(recipientFilter);
				target.add(wmk);
			}
		});
		wmk.add(addressFilt);

		TextField<Long> userFilt = new TextField<Long>("userFilter", new PropertyModel<Long>(this, "userFilter"));
		userFilt.add(new AjaxFormComponentUpdatingBehavior("onkeyup") {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {

				if (userFilter != null && userProfileService.getUser(userFilter) != null) {
					recipientFilter.setUser(userProfileService.getUser(userFilter));
					dataProvider.setRecipientFilter(recipientFilter);
				} else if (userFilter != null && userProfileService.getUser(userFilter) == null) {
					RecipientFilter rf = new RecipientFilter();
					rf.setId(0L);
					dataProvider.setRecipientFilter(rf);
				} else {
					recipientFilter.setUser(null);
					dataProvider.setRecipientFilter(recipientFilter);
				}
				target.add(wmk);
			}
		});
		wmk.add(userFilt);
	}

	private class SortableRecipientProvider extends SortableDataProvider<Recipient, Serializable> {

		private static final long serialVersionUID = 1L;

		private RecipientFilter recipientFilter;

		public RecipientFilter getRecipientFilter() {
			return recipientFilter;
		}

		public void setRecipientFilter(RecipientFilter recipientFilter) {
			this.recipientFilter = recipientFilter;
		}

		public SortableRecipientProvider(RecipientFilter recipientFilter) {
			super();
			this.recipientFilter = recipientFilter;
			recipientFilter.setFetchPackages(true);
			recipientFilter.setFetchUser(true);
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
