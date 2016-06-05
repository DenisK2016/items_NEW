package by.dk.training.items.webapp.pages.users.panelforusers;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
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
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import by.dk.training.items.dataaccess.filters.UserFilter;
import by.dk.training.items.datamodel.Ranks;
import by.dk.training.items.datamodel.StatusUser;
import by.dk.training.items.datamodel.UserCredentials_;
import by.dk.training.items.datamodel.UserProfile;
import by.dk.training.items.datamodel.UserProfile_;
import by.dk.training.items.services.UserProfileService;
import by.dk.training.items.webapp.pages.users.UserPage;
import by.dk.training.items.webapp.pages.users.formforreg.RegistryUserPanel;

@AuthorizeAction(roles = { "ADMIN" }, action = Action.RENDER)
public class ListUsersPanel extends Panel {

	private static final long serialVersionUID = 1L;
	@Inject
	private UserProfileService userProfileService;
	private SortableUsersProvider dataProvider;
	private UserFilter userFilter;
	private Long idFilter;
	private String loginFilter;
	private String passwordFilter;
	private String fNameFilter;
	private String lNameFilter;
	private Date dateFilter;
	private StatusUser statusFilter;
	private String postFilter;
	private Ranks rankFilter;
	private String emailFilter;

	public ListUsersPanel(String id) {
		super(id);

	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		userFilter = new UserFilter();

		final ModalWindow modal1 = new ModalWindow("modal1");
		modal1.setCssClassName("modal_window");
		modal1.setInitialHeight(500);
		modal1.setResizable(false);
		modal1.setWindowClosedCallback(new WindowClosedCallback() {

			@Override
			public void onClose(AjaxRequestTarget target) {
				target.add(ListUsersPanel.this);

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
				target.add(ListUsersPanel.this);

			}
		});
		this.setOutputMarkupId(true);
		add(modalUpdate);
		dataProvider = new SortableUsersProvider(userFilter);
		DataView<UserProfile> dataView = new DataView<UserProfile>("userlist", dataProvider, 5) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<UserProfile> item) {
				UserProfile userProfile = item.getModelObject();

				item.add(new AjaxLink<Void>("infoUser") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						modal1.setContent(new UserInfo(modal1, userProfile));
						modal1.show(target);
					}
				});
				item.add(new Label("userid", userProfile.getId()));
				item.add(new Label("userlogin", userProfile.getLogin()));
				item.add(new Label("userfname", userProfile.getUserCredentials().getFirstName()));
				item.add(new Label("userlname", userProfile.getUserCredentials().getLastName()));
				item.add(new Label("userstatus", userProfile.getUserCredentials().getStatus()));
				item.add(new Label("useremail", userProfile.getUserCredentials().getEmail()));
				item.add(new Link<UserPage>("deletelink") {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						userProfileService.delete(userProfile.getId());
						setResponsePage(new UserPage());
					}
				});
				AjaxLink update = new AjaxLink<Void>("updateUser") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						modalUpdate.setContent(new RegistryUserPanel(modalUpdate, userProfile));
						modalUpdate.show(target);
					}
				};
				update.add(AttributeModifier.append("title", "Изменить пользователя"));
				item.add(update);

			}
		};
		WebMarkupContainer wmk = new WebMarkupContainer("container");
		wmk.setOutputMarkupId(true);
		wmk.add(dataView);
		add(wmk);

		wmk.add(new OrderByBorder("orderById", UserProfile_.id, dataProvider));
		wmk.add(new OrderByBorder("orderByLogin", UserProfile_.login, dataProvider));
		wmk.add(new OrderByBorder("orderByFName", UserCredentials_.firstName, dataProvider));
		wmk.add(new OrderByBorder("orderByLName", UserCredentials_.lastName, dataProvider));
		wmk.add(new OrderByBorder("orderByStatus", UserCredentials_.status, dataProvider));
		wmk.add(new OrderByBorder("orderByMail", UserCredentials_.email, dataProvider));

		wmk.add(new PagingNavigator("navigator", dataView));

		TextField<Long> idFilt = new TextField<Long>("idFilter", new PropertyModel<Long>(this, "idFilter"));
		idFilt.add(new AjaxFormComponentUpdatingBehavior("onkeyup") {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				userFilter.setId(idFilter);
				dataProvider.setUserFilter(userFilter);
				target.add(wmk);
			}
		});

		wmk.add(idFilt);

		TextField<String> logFilt = new TextField<String>("loginFilter",
				new PropertyModel<String>(this, "loginFilter"));
		logFilt.add(new AjaxFormComponentUpdatingBehavior("onkeyup") {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				userFilter.setLogin(loginFilter);
				dataProvider.setUserFilter(userFilter);
				target.add(wmk);
			}
		});
		wmk.add(logFilt);

		TextField<String> fNameFilt = new TextField<String>("fNameFilter",
				new PropertyModel<String>(this, "fNameFilter"));
		fNameFilt.add(new AjaxFormComponentUpdatingBehavior("onkeyup") {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				userFilter.setFirstName(fNameFilter);
				dataProvider.setUserFilter(userFilter);
				target.add(wmk);
			}
		});
		wmk.add(fNameFilt);

		TextField<String> lNameFilt = new TextField<String>("lNameFilter",
				new PropertyModel<String>(this, "lNameFilter"));
		lNameFilt.add(new AjaxFormComponentUpdatingBehavior("onkeyup") {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				userFilter.setLastName(lNameFilter);
				dataProvider.setUserFilter(userFilter);
				target.add(wmk);
			}
		});
		wmk.add(lNameFilt);

		DateTextField dateFilt = new DateTextField("dateFilter", new PropertyModel<>(this, "dateFilter"), "dd-MM-yyyy");
		dateFilt.add(new AjaxFormComponentUpdatingBehavior("onchange") {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				userFilter.setCreated(dateFilter);
				dataProvider.setUserFilter(userFilter);
				target.add(wmk);
			}
		});
		dateFilt.add(new AjaxFormComponentUpdatingBehavior("onkeyup") {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				userFilter.setCreated(dateFilter);
				dataProvider.setUserFilter(userFilter);
				target.add(wmk);
			}

		});
		dateFilt.add(new DatePicker());
		wmk.add(dateFilt);

		DropDownChoice<StatusUser> statusFilt = new DropDownChoice<>("statusFilter",
				new PropertyModel<StatusUser>(this, "statusFilter"), Arrays.asList(StatusUser.values()));
		statusFilt.add(new AjaxFormComponentUpdatingBehavior("onchange") {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				userFilter.setStatus(statusFilter);
				dataProvider.setUserFilter(userFilter);
				target.add(wmk);
			}
		});
		statusFilt.setNullValid(true);
		wmk.add(statusFilt);

		TextField<String> postFilt = new TextField<String>("postFilter", new PropertyModel<String>(this, "postFilter"));
		postFilt.add(new AjaxFormComponentUpdatingBehavior("onkeyup") {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				userFilter.setPost(postFilter);
				dataProvider.setUserFilter(userFilter);
				target.add(wmk);
			}
		});
		wmk.add(postFilt);

		DropDownChoice<Ranks> rankFilt = new DropDownChoice<>("rankFilter",
				new PropertyModel<Ranks>(this, "rankFilter"), Arrays.asList(Ranks.values()));
		rankFilt.add(new AjaxFormComponentUpdatingBehavior("onchange") {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				userFilter.setRank(rankFilter);
				dataProvider.setUserFilter(userFilter);
				target.add(wmk);
			}
		});
		rankFilt.setNullValid(true);
		wmk.add(rankFilt);

		TextField<String> emailFilt = new TextField<String>("emailFilter",
				new PropertyModel<String>(this, "emailFilter"));
		emailFilt.add(new AjaxFormComponentUpdatingBehavior("onkeyup") {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				userFilter.setEmail(emailFilter);
				dataProvider.setUserFilter(userFilter);
				target.add(wmk);
			}
		});
		wmk.add(emailFilt);

	}

	private class SortableUsersProvider extends SortableDataProvider<UserProfile, Serializable> {

		private static final long serialVersionUID = 1L;

		private UserFilter userFilter;

		public UserFilter getUserFilter() {
			return userFilter;
		}

		public void setUserFilter(UserFilter userFilter) {
			this.userFilter = userFilter;
		}

		public SortableUsersProvider(UserFilter userFilter) {
			super();
			this.userFilter = userFilter;
			userFilter.setFetchCredentials(true);
			setSort((Serializable) UserProfile_.id, SortOrder.ASCENDING);
		}

		@Override
		public Iterator<UserProfile> iterator(long first, long count) {

			Serializable property = getSort().getProperty();
			SortOrder propertySortOrder = getSortState().getPropertySortOrder(property);

			userFilter.setSortProperty((SingularAttribute) property);
			userFilter.setSortOrder(propertySortOrder.equals(SortOrder.ASCENDING) ? true : false);

			userFilter.setLimit((int) count);
			userFilter.setOffset((int) first);
			return userProfileService.find(userFilter).iterator();

		}

		@Override
		public long size() {
			return userProfileService.count(userFilter);
		}

		@Override
		public IModel<UserProfile> model(UserProfile object) {
			return new Model(object);
		}

	}
}
