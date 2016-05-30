package by.dk.training.items.webapp.pages.users.panelforusers;

import java.io.Serializable;
import java.util.Iterator;

import javax.inject.Inject;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
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

import by.dk.training.items.dataaccess.filters.UserFilter;
import by.dk.training.items.datamodel.UserCredentials_;
import by.dk.training.items.datamodel.UserProfile;
import by.dk.training.items.datamodel.UserProfile_;
import by.dk.training.items.services.UserProfileService;
import by.dk.training.items.webapp.pages.users.UserPage;
import by.dk.training.items.webapp.pages.users.formforreg.UserRegPage;

@AuthorizeAction(roles = { "ADMIN" }, action = Action.RENDER)
public class ListUsersPanel extends Panel {

	private static final long serialVersionUID = 1L;
	@Inject
	private UserProfileService userProfileService;

	public ListUsersPanel(String id) {
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
				target.add(ListUsersPanel.this);

			}
		});
		this.setOutputMarkupId(true);
		add(modal1);
		SortableUsersProvider dataProvider = new SortableUsersProvider();
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
				item.add(new Label("userpassword", userProfile.getPassword()));
				item.add(new Label("userfname", userProfile.getUserCredentials().getFirstName()));
				item.add(new Label("userlname", userProfile.getUserCredentials().getLastName()));
				item.add(DateLabel.forDatePattern("usercreated",
						Model.of(userProfile.getUserCredentials().getCreated()), "dd-MM-yyyy"));
				item.add(new Label("userstatus", userProfile.getUserCredentials().getStatus()));
				item.add(new Label("userpost", userProfile.getUserCredentials().getPost()));
				item.add(new Label("userrank", userProfile.getUserCredentials().getRank()));
				item.add(new Label("useremail", userProfile.getUserCredentials().getEmail()));
				item.add(new Link<UserPage>("deletelink") {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						userProfileService.delete(userProfile.getId());
						setResponsePage(new UserPage());
					}
				});
				item.add(new Link<UserRegPage>("updateUser") {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new UserRegPage(userProfile));

					}
				});

			}
		};
		add(dataView);

		add(new OrderByBorder("orderById", UserProfile_.id, dataProvider));
		add(new OrderByBorder("orderByLogin", UserProfile_.login, dataProvider));
		add(new OrderByBorder("orderByPassword", UserProfile_.password, dataProvider));
		add(new OrderByBorder("orderByFName", UserCredentials_.firstName, dataProvider));
		add(new OrderByBorder("orderByLName", UserCredentials_.lastName, dataProvider));
		add(new OrderByBorder("orderByCreated", UserCredentials_.created, dataProvider));
		add(new OrderByBorder("orderByStatus", UserCredentials_.status, dataProvider));
		add(new OrderByBorder("orderByPost", UserCredentials_.post, dataProvider));
		add(new OrderByBorder("orderByRank", UserCredentials_.rank, dataProvider));
		add(new OrderByBorder("orderByMail", UserCredentials_.email, dataProvider));

		add(new PagingNavigator("navigator", dataView));
	}

	private class SortableUsersProvider extends SortableDataProvider<UserProfile, Serializable> {

		private static final long serialVersionUID = 1L;

		private UserFilter userFilter;

		public SortableUsersProvider() {
			super();
			userFilter = new UserFilter();
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
