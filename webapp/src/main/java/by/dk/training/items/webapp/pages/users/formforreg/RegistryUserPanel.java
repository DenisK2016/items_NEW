package by.dk.training.items.webapp.pages.users.formforreg;

import java.util.Arrays;
import java.util.Date;

import javax.inject.Inject;

import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.ui.widget.tooltip.CustomTooltipBehavior;

import by.dk.training.items.datamodel.Ranks;
import by.dk.training.items.datamodel.StatusUser;
import by.dk.training.items.datamodel.UserCredentials;
import by.dk.training.items.datamodel.UserProfile;
import by.dk.training.items.services.UserProfileService;
import by.dk.training.items.webapp.pages.users.UserPage;

@AuthorizeAction(roles = { "ADMIN", "COMMANDER", "OFFICER" }, action = Action.RENDER)
public class RegistryUserPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private UserProfile userProfile;
	private UserCredentials userCredentials;
	@Inject
	private UserProfileService userProfileService;
	private String descLogin = "Введите имя пользователя. Может состоять из любых символов.";
	private String descPass = "Может состоять из латинских букв и цифр.";
	private String descFName = "Введите имя";
	private String descLName = "Введите фамилию";
	private String descEmail = "Введите электронную почту";
	private String descDate = "Дата создания. Будет установлена сегодняшняя дата. Потом можно изменить.";
	private String descStatus = "Выберите роль пользователя";
	private String descPost = "Укажите занимаемую должность пользователя";
	private String descRank = "Укажите звание пользователя";
	private String descSubmit = "Созранить пользователя.";
	private String descLink = "На страницу с продуктами.";

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
		login.add(new CoverTooltipBehavior(descLogin, null));
		form.add(login);

		TextField<String> password = new TextField<String>("password");
		password.setRequired(true);
		password.add(StringValidator.maximumLength(100));
		password.add(StringValidator.minimumLength(6));
		password.add(new PatternValidator("[A-Za-z0-9]+"));
		password.add(new CoverTooltipBehavior(descPass, null));
		form.add(password);

		TextField<String> firstName = new TextField<String>("firstName",
				new PropertyModel<>(userCredentials, "firstName"));
		firstName.setRequired(true);
		firstName.add(StringValidator.maximumLength(100));
		firstName.add(StringValidator.minimumLength(2));
		firstName.add(new PatternValidator("[А-Яа-я]+"));
		firstName.add(new CoverTooltipBehavior(descFName, null));
		form.add(firstName);

		TextField<String> lastName = new TextField<String>("lastName",
				new PropertyModel<>(userCredentials, "lastName"));
		lastName.setRequired(true);
		lastName.add(StringValidator.maximumLength(100));
		lastName.add(StringValidator.minimumLength(2));
		firstName.add(new PatternValidator("[А-Яа-я]+"));
		lastName.add(new CoverTooltipBehavior(descLName, null));
		form.add(lastName);

		TextField<String> email = new TextField<String>("email", new PropertyModel<>(userCredentials, "email"));
		email.setRequired(true);
		email.add(StringValidator.maximumLength(100));
		email.add(StringValidator.minimumLength(2));
		email.add(EmailAddressValidator.getInstance());
		email.add(new CoverTooltipBehavior(descEmail, null));
		form.add(email);

		DateTextField createdField = new DateTextField("created", new PropertyModel<>(userCredentials, "created"));
		userCredentials.setCreated(new Date());
		createdField.add(new DatePicker());
		createdField.setRequired(true);
		createdField.add(new CoverTooltipBehavior(descDate, null));
		form.add(createdField);

		DropDownChoice<StatusUser> status = new DropDownChoice<StatusUser>("status",
				new PropertyModel<StatusUser>(userCredentials, "status"), Arrays.asList(StatusUser.values()));
		status.setNullValid(true);
		status.setRequired(true);
		status.add(new CoverTooltipBehavior(descStatus, null));
		form.add(status);

		TextField<String> post = new TextField<String>("post", new PropertyModel<>(userCredentials, "post"));
		post.add(new CoverTooltipBehavior(descPost, null));
		form.add(post);

		DropDownChoice<Ranks> rank = new DropDownChoice<Ranks>("rank",
				new PropertyModel<Ranks>(userCredentials, "rank"), Arrays.asList(Ranks.values()));
		rank.add(new CoverTooltipBehavior(descRank, null));
		form.add(rank);

		form.add(new SubmitLink("saveUser") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {

				if (userProfile.getId() == null) {

					userProfileService.register(userProfile, userCredentials);
				} else {
					userProfileService.update(userProfile);
					userProfileService.update(userCredentials);
				}

				setResponsePage(new UserPage());
			}
		}.add(new CoverTooltipBehavior(descSubmit, null)));

		add(form);

		form.add(new Link<UserPage>("BackToUsers") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new UserPage());

			}
		}.add(new CoverTooltipBehavior(descLink, null)));

	}

	private static Options newOptions() {
		Options options = new Options();
		options.set("track", true);
		options.set("hide", "{ effect: 'drop', delay: 100 }");

		return options;
	}

	class CoverTooltipBehavior extends CustomTooltipBehavior {
		private static final long serialVersionUID = 1L;

		private final String name;
		private final String url;

		public CoverTooltipBehavior(String name, String url) {
			super(newOptions());

			this.name = name;
			this.url = url;
		}

		@Override
		protected WebMarkupContainer newContent(String markupId) {
			Fragment fragment = new Fragment(markupId, "tooltip-fragment", RegistryUserPanel.this);
			fragment.add(new Label("name", Model.of(this.name)));
			// fragment.add(new ContextImage("cover", Model.of(this.url)));

			return fragment;
		}

	}

}
