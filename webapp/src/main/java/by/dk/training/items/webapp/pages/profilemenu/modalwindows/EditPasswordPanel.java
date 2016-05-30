package by.dk.training.items.webapp.pages.profilemenu.modalwindows;

import java.util.Arrays;

import javax.inject.Inject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;

import by.dk.training.items.datamodel.Ranks;
import by.dk.training.items.datamodel.UserCredentials;
import by.dk.training.items.datamodel.UserProfile;
import by.dk.training.items.services.UserProfileService;

public class EditPasswordPanel extends Panel {

	private static final long serialVersionUID = 1L;

	@Inject
	private UserProfileService userProfileService;
	private ModalWindow modalWindow;
	private UserProfile userProfile;
	private UserCredentials userCredentials;
	private TextField<String> password;
	private TextField<String> firstName;
	private TextField<String> lastName;
	private TextField<String> post;
	private DropDownChoice<Ranks> ranks;
	private String fieldName;

	public EditPasswordPanel(ModalWindow modalWindow, UserProfile userProfile, String filedName) {
		super(modalWindow.getContentId());
		this.modalWindow = modalWindow;
		this.userProfile = userProfile;
		this.userCredentials = userProfile.getUserCredentials();
		this.fieldName = filedName;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		Form<UserProfile> form = new Form<UserProfile>("formEdit", new CompoundPropertyModel<UserProfile>(userProfile));

		add(form);

		form.add(password = new TextField<>("password"));
		password.setVisible(false);
		if (fieldName.equals("password")) {
			password.setVisible(true);
		}
		form.add(firstName = new TextField<>("firstName", new PropertyModel<>(userCredentials, "firstName")));
		firstName.setVisible(false);
		if (fieldName.equals("firstName")) {
			firstName.setVisible(true);
		}
		form.add(lastName = new TextField<>("lastName", new PropertyModel<>(userCredentials, "lastName")));
		lastName.setVisible(false);
		if (fieldName.equals("lastName")) {
			lastName.setVisible(true);
		}
		form.add(post = new TextField<>("post", new PropertyModel<>(userCredentials, "post")));
		post.setVisible(false);
		if (fieldName.equals("post")) {
			post.setVisible(true);
		}
		form.add(ranks = new DropDownChoice<Ranks>("rank", new PropertyModel<>(userCredentials, "rank"),
				Arrays.asList(Ranks.values())));
		ranks.setVisible(false);
		if (fieldName.equals("ranks")) {
			ranks.setVisible(true);
		}
		form.add(new AjaxSubmitLink("save") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				userProfileService.update(userProfile);
				userProfileService.update(userCredentials);
				modalWindow.close(target);
			}
		});

		form.add(new AjaxLink("cancel") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				modalWindow.close(target);
			}
		});
	}

}
