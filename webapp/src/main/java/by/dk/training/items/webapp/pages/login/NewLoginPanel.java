package by.dk.training.items.webapp.pages.login;

import java.util.Arrays;

import javax.inject.Inject;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

import by.dk.training.items.datamodel.Ranks;
import by.dk.training.items.datamodel.UserCredentials;
import by.dk.training.items.datamodel.UserProfile;
import by.dk.training.items.services.UserProfileService;
import by.dk.training.items.services.mail.Sender;

public class NewLoginPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private UserProfile userProfile;
	private UserCredentials userCredentials;
	private ModalWindow modalWindow;
	@Inject
	private UserProfileService userProfileService;

	public NewLoginPanel(ModalWindow modalWindow) {
		super(modalWindow.getContentId());
		userProfile = new UserProfile();
		userCredentials = new UserCredentials();

		this.modalWindow = modalWindow;

	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		Form<UserProfile> form = new Form<UserProfile>("formRegUser",
				new CompoundPropertyModel<UserProfile>(userProfile));
		FeedbackPanel feedBackPanel = new FeedbackPanel("feedback");
		form.add(feedBackPanel.setOutputMarkupId(true));

		TextField<String> login = new TextField<String>("login");
		login.setRequired(true);
		login.add(StringValidator.maximumLength(100));
		login.add(StringValidator.minimumLength(2));
		login.add(AttributeModifier.append("title", "Придумайте имя пользователя"));
		form.add(login);

		TextField<String> password = new TextField<String>("password");
		password.setRequired(true);
		password.add(StringValidator.maximumLength(100));
		password.add(StringValidator.minimumLength(6));
		password.add(new PatternValidator("[A-Za-z0-9]+"));
		password.add(AttributeModifier.append("title", "Придумайте пароль"));
		form.add(password);

		TextField<String> firstName = new TextField<String>("firstName",
				new PropertyModel<>(userCredentials, "firstName"));
		firstName.setRequired(true);
		firstName.add(StringValidator.maximumLength(100));
		firstName.add(StringValidator.minimumLength(2));
		firstName.add(new PatternValidator("[А-Яа-я]+"));
		firstName.add(AttributeModifier.append("title", "Введите имя"));
		form.add(firstName);

		TextField<String> lastName = new TextField<String>("lastName",
				new PropertyModel<>(userCredentials, "lastName"));
		lastName.setRequired(true);
		lastName.add(StringValidator.maximumLength(100));
		lastName.add(StringValidator.minimumLength(2));
		firstName.add(new PatternValidator("[А-Яа-я]+"));
		lastName.add(AttributeModifier.append("title", "Введите фамилию"));
		form.add(lastName);

		TextField<String> email = new TextField<String>("email", new PropertyModel<>(userCredentials, "email"));
		email.setRequired(true);
		email.add(StringValidator.maximumLength(100));
		email.add(StringValidator.minimumLength(2));
		email.add(EmailAddressValidator.getInstance());
		email.add(AttributeModifier.append("title", "Введите электронную почту"));
		form.add(email);

		TextField<String> post = new TextField<String>("post", new PropertyModel<>(userCredentials, "post"));
		post.add(AttributeModifier.append("title", "Введите должность"));
		form.add(post);

		DropDownChoice<Ranks> rank = new DropDownChoice<Ranks>("rank",
				new PropertyModel<Ranks>(userCredentials, "rank"), Arrays.asList(Ranks.values()));
		rank.add(AttributeModifier.append("title", "Укажите звание"));
		form.add(rank);

		final ModalWindow modal2;
		add(modal2 = new ModalWindow("modal2"));

		modal2.setResizable(false);
		modal2.setCssClassName("modal_window");
		modal2.setInitialHeight(100);
		modal2.setInitialWidth(700);
		this.setOutputMarkupId(true);

		modal2.setWindowClosedCallback(new WindowClosedCallback() {

			@Override
			public void onClose(AjaxRequestTarget target) {
				target.add(NewLoginPanel.this);

			}
		});
		AjaxSubmitLink link = new AjaxSubmitLink("save") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				try{
					userProfileService.register(userProfile, userCredentials);
					String s = "http://localhost:8081/";
					String text = String.format(
							"Здравствуйте %s. Вы были зарегистрированы на сервисе. Пожалуйста не отвечайте на это письмо. Перейдите по ссылке",
							userProfile.getLogin());
					new Sender("denisov27111990@gmail.com", "php948409php").send("Регистрация в сервисе", text,
							userCredentials.getEmail());
					modalWindow.close(target);
				}catch(Exception e){
					modal2.setContent(new PanelError(modal2));
					modal2.show(target);
				}
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedBackPanel);
			}
		};
		link.add(AttributeModifier.append("title", "Сохранить"));
		form.add(link);

		form.add(new AjaxLink("cancel") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				modalWindow.close(target);
			}
		}.add(AttributeModifier.append("title", "Отменить")));

		add(form);

		// add(new Link<Void>("with") {
		// @Override
		// public void onClick() {
		// PageParameters pageParameters = new PageParameters();
		// pageParameters.add("foo", "foo");
		// pageParameters.add("bar", "bar");
		//
		// setResponsePage(new PageWithParameters11111111(pageParameters));
		//
		// }
		//
		// });

	}

}
