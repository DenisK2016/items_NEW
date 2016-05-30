package by.dk.training.items.webapp.pages.types.panelfortypes;

import javax.inject.Inject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import com.googlecode.wicket.jquery.ui.markup.html.link.AjaxLink;

import by.dk.training.items.dataaccess.filters.TypeFilter;
import by.dk.training.items.datamodel.Type;
import by.dk.training.items.services.TypeService;

public class TypeInfo extends Panel {

	@Inject
	private TypeService typeService;
	private ModalWindow modalWindow;
	private Type type;
	private TypeFilter typeFilter;

	public TypeInfo(ModalWindow modalWindow, Type type) {
		super(modalWindow.getContentId());
		typeFilter = new TypeFilter();
		typeFilter.setFetchParentType(true);
		typeFilter.setFetchUser(true);
		typeFilter.setId(type.getId());
		this.type = typeService.find(typeFilter).get(0);
		this.modalWindow = modalWindow;

	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(new Label("typeid", type.getId()));
		add(new Label("typename", type.getTypeName()));
		if (type.getParentType() != null) {
			add(new Label("parenttype",
					String.format("%d %s", type.getParentType().getId(), type.getParentType().getTypeName())));
		} else {
			add(new Label("parenttype", " "));
		}
		add(new Label("user", String.format("%d %s", type.getIdUser().getId(), type.getIdUser().getLogin())));

		add(new AjaxLink<Void>("back") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				modalWindow.close(target);
			}
		});

	}

}
