package by.dk.training.items.webapp.pages.login;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

public class PanelError extends Panel {

	private static final long serialVersionUID = 1L;
	private ModalWindow modalWindow;

	public PanelError(ModalWindow modalWindow) {
		super(modalWindow.getContentId());
		this.modalWindow = modalWindow;

	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		Form form = new Form<>("form");
		form.add(new Label("warning", "Пользователь с данным email уже существует"));

		AjaxSubmitLink link = new AjaxSubmitLink("ok") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

				modalWindow.close(target);

			}
		};

		form.add(link);
		add(form);
	}

}
