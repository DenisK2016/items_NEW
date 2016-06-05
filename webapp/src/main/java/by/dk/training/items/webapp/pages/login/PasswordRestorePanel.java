package by.dk.training.items.webapp.pages.login;

import javax.inject.Inject;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;

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
		// MessageDialog md = new MessageDialog("message", new
		// StringResourceModel("Ошибка", this, null),
		// new StringResourceModel("Введенного адреса в базе не существует",
		// this, null), DialogButtons.OK,
		// DialogIcon.ERROR) {
		//
		// @Override
		// public void onClose(IPartialPageRequestHandler handler, DialogButton
		// button) {
		//
		// }
		//
		// };
		// add(md);

		Form<UserProfile> form = new Form<UserProfile>("formRestorePass");
		FeedbackPanel feedBackPanel = new FeedbackPanel("feedback");
		form.add(feedBackPanel.setOutputMarkupId(true));

		TextField<String> email = new TextField<String>("email", new PropertyModel<String>(this, "emailStr"));
		email.add(
				AttributeModifier.append("title", "Введите адрес электронной почты, по которому вы регистрировались"));
		email.add(EmailAddressValidator.getInstance());
		email.setRequired(true);
		form.add(email);
		Label err = new Label("error", "Введенного адреса в базе не существует.");
		err.setOutputMarkupId(true);
		err.add(AttributeModifier.append("style", "display: none;"));
		form.add(err);

		form.add(new AjaxSubmitLink("request") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				userFilter.setEmail(emailStr);
				if (!userProfileService.find(userFilter).isEmpty()) {
					text = String.format("Логин: %s \nПароль: %s",
							userProfileService.find(userFilter).get(0).getLogin(),
							userProfileService.find(userFilter).get(0).getPassword());
					new Sender("denisov27111990@gmail.com", "php948409php").send("Восстановление пароля", text,
							emailStr);
					modalWindow.close(target);
				} else {
					err.add(AttributeModifier.replace("style", "display: block;"));
					// md.open(target);
					target.add(err);
				}
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedBackPanel);
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
