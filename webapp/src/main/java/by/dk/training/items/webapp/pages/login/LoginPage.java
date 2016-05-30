package by.dk.training.items.webapp.pages.login;

import org.apache.wicket.Application;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.util.string.Strings;

public class LoginPage extends WebPage {

	public static final String ID_FORM = "form";

	private String username;
	private String password;
	private String descLogin = "Введите имя пользователя";
	private String descPassword = "Введите пароль";
	private String descSebmit = "Войти";

	public LoginPage() {
		super();

	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		// if already logged then should not see login page at all
		if (AuthenticatedWebSession.get().isSignedIn()) {
			setResponsePage(Application.get().getHomePage());
		}

		final Form<Void> form = new Form<Void>(ID_FORM);
		form.add(new FeedbackPanel("feedbackpanel"));
		form.setDefaultModel(new CompoundPropertyModel<LoginPage>(this));
		form.add(new RequiredTextField<String>("username"));
		form.add(new PasswordTextField("password"));

		form.add(new SubmitLink("submit-btn") {
			@Override
			public void onSubmit() {
				super.onSubmit();
				if (Strings.isEmpty(username) || Strings.isEmpty(password)) {
					return;
				}
				final boolean authResult = AuthenticatedWebSession.get().signIn(username, password);
				if (authResult) {
					// continueToOriginalDestination();
					setResponsePage(Application.get().getHomePage());
				} else {
					error("authorization error");
				}
			}
		});

		add(form);

		final ModalWindow modal1;
		add(modal1 = new ModalWindow("modal1"));
		modal1.setTitle("Регистрация нового пользователя");

		this.setOutputMarkupId(true);
		add(new AjaxLink<Void>("newUser") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				modal1.setContent(new NewLoginPanel(modal1));
				modal1.show(target);
			}
		});

		modal1.setWindowClosedCallback(new WindowClosedCallback() {

			@Override
			public void onClose(AjaxRequestTarget target) {
				target.add(LoginPage.this);

			}
		});

		final ModalWindow modal2;
		add(modal2 = new ModalWindow("modal2"));
		modal2.setTitle("Восстановление пароля");

		this.setOutputMarkupId(true);
		add(new AjaxLink<Void>("passwordRestore") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				modal2.setContent(new PasswordRestorePanel(modal2));
				modal2.show(target);
			}
		});

		modal2.setWindowClosedCallback(new WindowClosedCallback() {

			@Override
			public void onClose(AjaxRequestTarget target) {
				target.add(LoginPage.this);

			}
		});

	}

}
