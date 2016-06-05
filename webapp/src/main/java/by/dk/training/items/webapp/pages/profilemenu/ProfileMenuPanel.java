package by.dk.training.items.webapp.pages.profilemenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.googlecode.wicket.jquery.ui.markup.html.link.Link;

import by.dk.training.items.dataaccess.filters.UserFilter;
import by.dk.training.items.datamodel.Package;
import by.dk.training.items.datamodel.UserProfile;
import by.dk.training.items.services.UserProfileService;
import by.dk.training.items.webapp.pages.home.HomePage;
import by.dk.training.items.webapp.pages.profilemenu.modalwindows.EditInfoPanel;

@AuthorizeAction(roles = { "ADMIN", "COMMANDER", "OFFICER" }, action = Action.RENDER)
public class ProfileMenuPanel extends Panel {

	@Inject
	private UserProfileService userProfileService;
	private UserProfile userProfile;
	private Set<Package> listPack;
	private List<Long> listId = new ArrayList<>();
	private UserFilter filt;

	public ProfileMenuPanel(String id, UserProfile userProfile) {
		super(id);
		filt = new UserFilter();
		filt.setFetchCredentials(true);
		filt.setFetchPackages(true);
		filt.setLogin(userProfile.getLogin());
		this.userProfile = userProfileService.find(filt).get(0);
		listPack = this.userProfile.getPackages();
	}

	private static final long serialVersionUID = 1L;

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(new Link<HomePage>("tohome") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new HomePage());

			}
		});

		add(new Label("login", Model.of(userProfile.getLogin())));
		add(new Label("pass", Model.of(userProfile.getPassword())));
		add(new Label("firstName", Model.of(userProfile.getUserCredentials().getFirstName())));
		add(new Label("lastName", Model.of(userProfile.getUserCredentials().getLastName())));
		add(DateLabel.forDatePattern("usercreated", Model.of(userProfile.getUserCredentials().getCreated()),
				"dd-MM-yyyy"));
		add(new Label("status", userProfile.getUserCredentials().getStatus().name()));
		add(new Label("post", Model.of(userProfile.getUserCredentials().getPost())));
		add(new Label("rank", Model.of(userProfile.getUserCredentials().getRank())));
		add(new Label("email", Model.of(userProfile.getUserCredentials().getEmail())));
		for (Package p : listPack) {
			listId.add(p.getId());
		}
		add(new ListChoice<Long>("packages", listId).setMaxRows(10));

		final ModalWindow modal1;
		add(modal1 = new ModalWindow("modal1"));
		modal1.setCssClassName("modal_window");
		modal1.setInitialHeight(300);
		modal1.setResizable(false);
		modal1.setWindowClosedCallback(new WindowClosedCallback() {

			@Override
			public void onClose(AjaxRequestTarget target) {
				target.add(ProfileMenuPanel.this);

			}
		});
		this.setOutputMarkupId(true);

		this.setOutputMarkupId(true);
		add(new AjaxLink<Void>("setPassword") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				modal1.setContent(new EditInfoPanel(modal1, userProfile, "password"));
				modal1.show(target);
			}
		});

		add(new AjaxLink<Void>("setFirstName") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				modal1.setContent(new EditInfoPanel(modal1, userProfile, "firstName"));
				modal1.show(target);
			}
		});
		add(new AjaxLink<Void>("setLastName") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				modal1.setContent(new EditInfoPanel(modal1, userProfile, "lastName"));
				modal1.show(target);
			}
		});
		add(new AjaxLink<Void>("setPost") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				modal1.setContent(new EditInfoPanel(modal1, userProfile, "post"));
				modal1.show(target);
			}
		});
		add(new AjaxLink<Void>("setRank") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				modal1.setContent(new EditInfoPanel(modal1, userProfile, "ranks"));
				modal1.show(target);
			}
		});

		modal1.setWindowClosedCallback(new WindowClosedCallback() {

			@Override
			public void onClose(AjaxRequestTarget target) {
				target.add(ProfileMenuPanel.this);

			}
		});

	}

}
