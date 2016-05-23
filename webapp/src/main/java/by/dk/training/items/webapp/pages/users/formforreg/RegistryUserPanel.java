package by.dk.training.items.webapp.pages.users.formforreg;

import java.util.Arrays;
import java.util.Date;

import javax.inject.Inject;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;

import by.dk.training.items.datamodel.Ranks;
import by.dk.training.items.datamodel.StatusUser;
import by.dk.training.items.datamodel.UserCredentials;
import by.dk.training.items.datamodel.UserProfile;
import by.dk.training.items.services.UserProfileService;
import by.dk.training.items.webapp.pages.users.UserPage;

public class RegistryUserPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private UserProfile userProfile;
	private UserCredentials userCredentials;
	@Inject
	private UserProfileService userProfileService;

	public RegistryUserPanel(String id) {
		super(id);
		userProfile = new UserProfile();
		userCredentials = new UserCredentials();

	}

	public RegistryUserPanel(String id, UserProfile userProfile) {
		super(id);
		this.userProfile = userProfile;
		this.userCredentials = userProfile.getUserCredentials();

	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		Form<UserProfile> form = new Form<UserProfile>("formRegUser",
				new CompoundPropertyModel<UserProfile>(userProfile));
		form.add(new FeedbackPanel("feedback"));

		TextField<String> login = new TextField<String>("login");
		login.setRequired(true);
		login.add(StringValidator.maximumLength(100));
		login.add(StringValidator.minimumLength(2));
		form.add(login);

		TextField<String> password = new TextField<String>("password");
		password.setRequired(true);
		password.add(StringValidator.maximumLength(100));
		password.add(StringValidator.minimumLength(6));
		form.add(password);

		TextField<String> firstName = new TextField<String>("firstName",
				new PropertyModel<>(userCredentials, "firstName"));
		firstName.setRequired(true);
		firstName.add(StringValidator.maximumLength(100));
		firstName.add(StringValidator.minimumLength(2));
		form.add(firstName);

		TextField<String> lastName = new TextField<String>("lastName",
				new PropertyModel<>(userCredentials, "lastName"));
		lastName.setRequired(true);
		lastName.add(StringValidator.maximumLength(100));
		lastName.add(StringValidator.minimumLength(2));
		form.add(lastName);

		TextField<String> email = new TextField<String>("email", new PropertyModel<>(userCredentials, "email"));
		email.setRequired(true);
		email.add(StringValidator.maximumLength(100));
		email.add(StringValidator.minimumLength(2));
		form.add(email);

		DateTextField createdField = new DateTextField("created", new PropertyModel<>(userCredentials, "created"));
		userCredentials.setCreated(new Date());
		createdField.add(new DatePicker());
		createdField.setRequired(true);
		form.add(createdField);

		DropDownChoice<StatusUser> status = new DropDownChoice<StatusUser>("status",
				new PropertyModel<StatusUser>(userCredentials, "status"), Arrays.asList(StatusUser.values()));
		status.setNullValid(true);
		form.add(status);

		TextField<String> post = new TextField<String>("post", new PropertyModel<>(userCredentials, "post"));
		post.add(StringValidator.maximumLength(100));
		post.add(StringValidator.minimumLength(2));
		form.add(post);

		DropDownChoice<Ranks> rank = new DropDownChoice<Ranks>("rank",
				new PropertyModel<Ranks>(userCredentials, "rank"), Arrays.asList(Ranks.values()));
		form.add(rank);

		form.add(new SubmitLink("saveUser") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {

				userProfileService.register(userProfile, userCredentials);

				setResponsePage(new UserPage());
			}
		});

		add(form);

		add(new Link<UserPage>("BackToUsers") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new UserPage());

			}
		});

	}

}
