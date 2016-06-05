package by.dk.training.items.webapp.pages.users.formforreg;

import java.util.Arrays;
import java.util.Date;

import javax.inject.Inject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

import by.dk.training.items.datamodel.Ranks;
import by.dk.training.items.datamodel.StatusUser;
import by.dk.training.items.datamodel.UserCredentials;
import by.dk.training.items.datamodel.UserProfile;
import by.dk.training.items.services.UserProfileService;
import by.dk.training.items.webapp.pages.users.UserPage;

@AuthorizeAction(roles = { "ADMIN" }, action = Action.RENDER)
public class RegistryUserPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private UserProfile userProfile;
	private UserCredentials userCredentials;
	@Inject
	private UserProfileService userProfileService;
	private ModalWindow modalWindow;

	public RegistryUserPanel(ModalWindow modalWindow) {
		super(modalWindow.getContentId());
		this.modalWindow = modalWindow;
		userProfile = new UserProfile();
		userCredentials = new UserCredentials();

	}

	public RegistryUserPanel(ModalWindow modalWindow, UserProfile userProfile) {
		super(modalWindow.getContentId());
		this.modalWindow = modalWindow;
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
		login.add(new AjaxFormComponentUpdatingBehavior("change") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {

			}
		});
		login.setRequired(true);
		login.add(StringValidator.maximumLength(100));
		login.add(StringValidator.minimumLength(2));
		form.add(login);

		PasswordTextField password = new PasswordTextField("password");
		password.add(new AjaxFormComponentUpdatingBehavior("change") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {

			}
		});
		password.setRequired(true);
		password.add(StringValidator.maximumLength(100));
		password.add(StringValidator.minimumLength(6));
		password.add(new PatternValidator("[A-Za-z0-9]+"));
		if (userProfile.getId() != null) {
			password.setEnabled(false);
		}
		form.add(password);

		TextField<String> firstName = new TextField<String>("firstName",
				new PropertyModel<>(userCredentials, "firstName"));
		firstName.add(new AjaxFormComponentUpdatingBehavior("change") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {

			}
		});
		firstName.setRequired(true);
		firstName.add(StringValidator.maximumLength(100));
		firstName.add(StringValidator.minimumLength(2));
		firstName.add(new PatternValidator("[А-Яа-я]+"));
		form.add(firstName);

		TextField<String> lastName = new TextField<String>("lastName",
				new PropertyModel<>(userCredentials, "lastName"));
		lastName.add(new AjaxFormComponentUpdatingBehavior("change") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {

			}
		});
		lastName.setRequired(true);
		lastName.add(StringValidator.maximumLength(100));
		lastName.add(StringValidator.minimumLength(2));
		firstName.add(new PatternValidator("[А-Яа-я]+"));
		form.add(lastName);

		TextField<String> email = new TextField<String>("email", new PropertyModel<>(userCredentials, "email"));
		email.add(new AjaxFormComponentUpdatingBehavior("change") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {

			}
		});
		email.setRequired(true);
		email.add(StringValidator.maximumLength(100));
		email.add(StringValidator.minimumLength(2));
		email.add(EmailAddressValidator.getInstance());
		form.add(email);

		DateTextField createdField = new DateTextField("created", new PropertyModel<>(userCredentials, "created"));
		createdField.add(new AjaxFormComponentUpdatingBehavior("change") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {

			}
		});
		userCredentials.setCreated(new Date());
		createdField.add(new DatePicker());
		createdField.setRequired(true);
		form.add(createdField);

		DropDownChoice<StatusUser> status = new DropDownChoice<StatusUser>("status",
				new PropertyModel<StatusUser>(userCredentials, "status"), Arrays.asList(StatusUser.values()));
		status.add(new AjaxFormComponentUpdatingBehavior("change") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {

			}
		});
		status.setNullValid(true);
		status.setRequired(true);
		form.add(status);

		TextField<String> post = new TextField<String>("post", new PropertyModel<>(userCredentials, "post"));
		post.add(new AjaxFormComponentUpdatingBehavior("change") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {

			}
		});
		form.add(post);

		DropDownChoice<Ranks> rank = new DropDownChoice<Ranks>("rank",
				new PropertyModel<Ranks>(userCredentials, "rank"), Arrays.asList(Ranks.values()));
		rank.add(new AjaxFormComponentUpdatingBehavior("change") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {

			}
		});
		form.add(rank);

		form.add(new AjaxLink<Void>("saveUser") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				if (userProfile.getId() == null) {

					userProfileService.register(userProfile, userCredentials);
				} else {
					userProfileService.update(userProfile);
					userProfileService.update(userCredentials);
				}
				modalWindow.close(target);
				setResponsePage(new UserPage());
			}
		});

		add(form);
		form.add(new AjaxLink<Void>("BackToUsers") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				modalWindow.close(target);
			}
		});
		if (userProfile.getId() == null) {
			form.add(new Label("regOrUpdate", "Регистрация нового пользователя"));
		} else {
			form.add(new Label("regOrUpdate", "Редактирование пользователя"));
		}

	}

}
