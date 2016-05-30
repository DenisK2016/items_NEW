package by.dk.training.items.webapp.pages.login;

import javax.inject.Inject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import by.dk.training.items.dataaccess.filters.UserFilter;
import by.dk.training.items.datamodel.UserProfile;
import by.dk.training.items.services.UserProfileService;
import by.dk.training.items.services.mail.Sender;

public class PasswordRestorePanel extends Panel {

	private static final long serialVersionUID = 1L;
	private String emailStr;
	private ModalWindow modalWindow;
	@Inject
	private UserProfileService userProfileService;
	private UserFilter userFilter;
	private String text;

	public PasswordRestorePanel(ModalWindow modalWindow) {
		super(modalWindow.getContentId());
		this.modalWindow = modalWindow;
		userFilter = new UserFilter();
		userFilter.setFetchCredentials(true);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		Form<UserProfile> form = new Form<UserProfile>("formRestorePass");
		form.add(new FeedbackPanel("feedback"));

		TextField<String> email = new TextField<String>("email", new PropertyModel<String>(this, "emailStr"));
		email.setRequired(true);
		form.add(email);

		form.add(new AjaxSubmitLink("request") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				userFilter.setEmail(emailStr);
				text = String.format("Логин: %s \nПароль: %s", userProfileService.find(userFilter).get(0).getLogin(),
						userProfileService.find(userFilter).get(0).getPassword());
				new Sender("denisov27111990@gmail.com", "php948409php").send("Восстановление пароля", text, emailStr);
				modalWindow.close(target);
			}
		});

		form.add(new AjaxLink("cancel") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				modalWindow.close(target);
			}
		});

		add(form);
	}

}
